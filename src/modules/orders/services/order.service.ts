import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, DataSource, Like } from 'typeorm';
import { Order } from '../entities/order.entity';
import { OrderItem } from '../entities/order-item.entity';
import { OrderItemOption } from '../entities/order-item-option.entity';
import { OrderCreateRequest } from '../dto/order-create.request';
import { Customer } from '../../customers/entities/customer.entity';
import { Product } from '../../products/entities/product.entity';
import { Option } from '../../products/entities/option.entity';

@Injectable()
export class OrderService {
  constructor(
    private dataSource: DataSource,
    @InjectRepository(Order)
    private orderRepository: Repository<Order>,
    @InjectRepository(Customer)
    private customerRepository: Repository<Customer>,
    @InjectRepository(Product)
    private productRepository: Repository<Product>,
    @InjectRepository(Option)
    private optionRepository: Repository<Option>,
  ) {}

  async createOrder(request: OrderCreateRequest): Promise<any> {
    const queryRunner = this.dataSource.createQueryRunner();
    await queryRunner.connect();
    await queryRunner.startTransaction();

    try {
      // 1. Tìm hoặc tạo khách hàng (Like Java logic)
      let customer = await queryRunner.manager.findOneBy(Customer, {
        phone: request.receiverPhone,
      });

      if (!customer) {
        customer = queryRunner.manager.create(Customer, {
          phone: request.receiverPhone,
          totalOrders: 0,
          totalSpent: 0,
        });
        customer = await queryRunner.manager.save(customer);
      }

      // 2. Khởi tạo Order (Lưu trước một lần để lấy ID giống mã Java)
      let order = queryRunner.manager.create(Order, {
        status: 'NEW',
        customer: customer,
        receiverName: request.receiverName,
        receiverPhone: request.receiverPhone,
        deliveryAddress: request.deliveryAddress,
        note: request.note,
        totalAmount: 0,
        items: [],
      });
      // Lưu order để lấy ID trước khi tạo items
      order = await queryRunner.manager.save(order);

      let totalAmount = 0;

      // 3. Xử lý từng món trong đơn (Loop items as in Java)
      const orderItems: OrderItem[] = [];
      for (const itemReq of request.items) {
        const product = await queryRunner.manager.findOneBy(Product, {
          id: itemReq.productId,
        });
        if (!product) throw new NotFoundException(`Product ${itemReq.productId} not found`);

        let orderItem = queryRunner.manager.create(OrderItem, {
          order: order,
          product: product,
          quantity: itemReq.quantity,
          basePrice: product.price,
          note: itemReq.note,
          options: [],
        });

        // Lưu OrderItem để lấy ID trước khi tạo options
        orderItem = await queryRunner.manager.save(orderItem);

        let itemTotal = product.price * itemReq.quantity;

        // Xử lý Options
        if (itemReq.optionIds && itemReq.optionIds.length > 0) {
          for (const optionId of itemReq.optionIds) {
            const option = await queryRunner.manager.findOneBy(Option, { id: optionId });
            if (!option) throw new NotFoundException(`Option ${optionId} not found`);

            const oio = queryRunner.manager.create(OrderItemOption, {
              orderItem: orderItem,
              optionId: option.id,
              optionName: option.name,
              optionPrice: option.price,
            });

            orderItem.options.push(oio);
            itemTotal += option.price;
          }
          // Lưu lại OrderItem kèm theo các options đã thêm
          await queryRunner.manager.save(orderItem);
        }

        totalAmount += itemTotal;
        orderItems.push(orderItem);
      }


      // Cập nhật lại thông tin Order
      order.totalAmount = totalAmount;
      order.items = orderItems;

      // 4. Cập nhật thống kê khách hàng
      customer.totalOrders += 1;
      customer.totalSpent += totalAmount;
      await queryRunner.manager.save(customer);

      // 5. Lưu Order lần cuối
      const savedOrder = await queryRunner.manager.save(order);
      await queryRunner.commitTransaction();

      return this.toResponseDto(savedOrder);


    } catch (err) {
      await queryRunner.rollbackTransaction();
      throw err;
    } finally {
      await queryRunner.release();
    }
  }

  // Bổ sung phương thức findAll giống Page<OrderDTO> getAllOrders trong Java
  async findAll(page: number = 1, limit: number = 10, search: string = '') {
    const [orders, total] = await this.orderRepository.findAndCount({
      where: search
        ? [{ receiverName: Like(`%${search}%`) }, { receiverPhone: Like(`%${search}%`) }]
        : {},
      relations: ['customer', 'items', 'items.product', 'items.options'],
      order: { createdAt: 'DESC' },
      skip: (page - 1) * limit,
      take: limit,
    });

    return {
      items: orders.map((order) => this.toResponseDto(order)),
      meta: {
        totalItems: total,
        itemCount: orders.length,
        itemsPerPage: limit,
        totalPages: Math.ceil(total / limit),
        currentPage: page,
      },
    };
  }

  async getById(id: number): Promise<any> {
    const order = await this.orderRepository.findOne({
      where: { id },
      relations: ['customer', 'items', 'items.product', 'items.options'],
    });
    if (!order) throw new NotFoundException('Order not found');
    return this.toResponseDto(order);
  }

  // Hàm Mapper tương đương mapToResponse trong Java
  private toResponseDto(order: Order) {
    return {
      id: order.id,
      status: order.status,
      totalAmount: order.totalAmount,
      receiverName: order.receiverName,
      receiverPhone: order.receiverPhone,
      deliveryAddress: order.deliveryAddress,
      note: order.note,
      createdAt: order.createdAt,
      customer: order.customer
        ? {
            id: order.customer.id,
            phone: order.customer.phone,
            totalOrders: order.customer.totalOrders,
            totalSpent: order.customer.totalSpent,
          }
        : null,
      items: order.items?.map((item) => ({
        productName: item.product?.name,
        quantity: item.quantity,
        basePrice: item.basePrice,
        note: item.note,
        options: item.options?.map((opt) => ({
          name: opt.optionName,
          price: opt.optionPrice,
        })),
      })),
    };
  }
}


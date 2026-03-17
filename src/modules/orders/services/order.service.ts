import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, DataSource } from 'typeorm';
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

  async createOrder(request: OrderCreateRequest): Promise<Order> {
    const queryRunner = this.dataSource.createQueryRunner();
    await queryRunner.connect();
    await queryRunner.startTransaction();

    try {
      // 1. Tìm hoặc tạo khách hàng
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

      // 2. Khởi tạo Order
      const order = queryRunner.manager.create(Order, {
        status: 'NEW',
        customer: customer,
        receiverName: request.receiverName,
        receiverPhone: request.receiverPhone,
        deliveryAddress: request.deliveryAddress,
        note: request.note,
        totalAmount: 0,
        items: [],
      });

      let totalAmount = 0;

      // 3. Xử lý từng món trong đơn
      for (const itemReq of request.items) {
        const product = await queryRunner.manager.findOneBy(Product, {
          id: itemReq.productId,
        });
        if (!product) throw new NotFoundException(`Product ${itemReq.productId} not found`);

        const orderItem = queryRunner.manager.create(OrderItem, {
          order: order,
          product: product,
          quantity: itemReq.quantity,
          basePrice: product.price,
          note: itemReq.note,
          options: [],
        });

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
        }

        totalAmount += itemTotal;
        order.items.push(orderItem);
      }

      order.totalAmount = totalAmount;

      // 4. Cập nhật thống kê khách hàng
      customer.totalOrders += 1;
      customer.totalSpent += totalAmount;
      await queryRunner.manager.save(customer);

      // 5. Lưu Order (Cascade sẽ lưu cả items và options)
      const savedOrder = await queryRunner.manager.save(order);

      await queryRunner.commitTransaction();
      return savedOrder;
    } catch (err) {
      await queryRunner.rollbackTransaction();
      throw err;
    } finally {
      await queryRunner.release();
    }
  }

  async getById(id: number): Promise<Order> {
    const order = await this.orderRepository.findOne({
      where: { id },
      relations: ['customer', 'items', 'items.product', 'items.options'],
    });
    if (!order) throw new NotFoundException('Order not found');
    return order;
  }
}

import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, Between } from 'typeorm';
import { Order, OrderStatus } from '../../orders/entities/order.entity';
@Injectable()
export class StatisticsService {
  constructor(
    @InjectRepository(Order)
    private orderRepository: Repository<Order>,
  ) {}

  async getRevenue(startDate: Date, endDate: Date) {
    const orders = await this.orderRepository.find({
      where: {
        status: OrderStatus.COMPLETED,
        createdAt: Between(startDate, endDate),
      },
    });

    const totalRevenue = orders.reduce(
      (sum, order) => sum + Number(order.totalAmount || 0),
      0,
    );
    const orderCount = orders.length;

    return {
      totalRevenue,
      orderCount,
      orders,
    };
  }

  async getDailyRevenue() {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const tomorrow = new Date(today);
    tomorrow.setDate(tomorrow.getDate() + 1);

    return this.getRevenue(today, tomorrow);
  }
}

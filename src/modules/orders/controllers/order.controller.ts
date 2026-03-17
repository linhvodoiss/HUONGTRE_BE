import { Controller, Post, Body, Get, Param, ParseIntPipe } from '@nestjs/common';
import { OrderService } from '../services/order.service';
import { OrderCreateRequest } from '../dto/order-create.request';

@Controller('orders')
export class OrderController {
  constructor(private readonly orderService: OrderService) {}

  @Post()
  async create(@Body() request: OrderCreateRequest) {
    const order = await this.orderService.createOrder(request);
    return {
      statusCode: 200,
      message: 'Tạo đơn hàng thành công!',
      data: order,
    };
  }

  @Get(':id')
  async getById(@Param('id', ParseIntPipe) id: number) {
    const order = await this.orderService.getById(id);
    return {
      statusCode: 200,
      message: `Lấy chi tiết đơn hàng ${id} thành công!`,
      data: order,
    };
  }
}

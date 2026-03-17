import { Controller, Post, Body, Get, Param, ParseIntPipe, Query } from '@nestjs/common';
import { OrderService } from '../services/order.service';
import { OrderCreateRequest } from '../dto/order-create.request';

@Controller('orders')
export class OrderController {
  constructor(private readonly orderService: OrderService) {}

  @Post()
  async create(@Body() request: OrderCreateRequest) {
    const data = await this.orderService.createOrder(request);
    return {
      statusCode: 200,
      message: 'Tạo đơn hàng thành công!',
      data,
    };
  }

  @Get()
  async findAll(
    @Query('page') page: number = 1,
    @Query('limit') limit: number = 10,
    @Query('search') search: string = '',
  ) {
    const data = await this.orderService.findAll(Number(page), Number(limit), search);
    return {
      statusCode: 200,
      message: 'Lấy danh sách đơn hàng thành công!',
      data,
    };
  }

  @Get(':id')
  async getById(@Param('id', ParseIntPipe) id: number) {
    const data = await this.orderService.getById(id);
    return {
      statusCode: 200,
      message: `Lấy chi tiết đơn hàng ${id} thành công!`,
      data,
    };
  }
}


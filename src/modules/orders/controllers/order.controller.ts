import {
  Controller,
  Post,
  Body,
  Get,
  Param,
  ParseIntPipe,
  Query,
  Put,
} from '@nestjs/common';
import { OrderService } from '../services/order.service';
import { OrderCreateRequest } from '../dto/order-create.request';
import { ResponseMessage } from '../../../common/decorators/response-message.decorator';
import { OrderStatus } from '../entities/order.entity';

@Controller('orders')
export class OrderController {
  constructor(private readonly orderService: OrderService) {}

  @Post()
  @ResponseMessage('Tạo đơn hàng thành công!')
  async create(@Body() request: OrderCreateRequest) {
    return this.orderService.createOrder(request);
  }

  @Get()
  @ResponseMessage('Lấy danh sách đơn hàng thành công!')
  async findAll(
    @Query('page') page: number = 1,
    @Query('limit') limit: number = 10,
    @Query('search') search: string = '',
  ) {
    return this.orderService.findAll(Number(page), Number(limit), search);
  }

  @Get(':id')
  @ResponseMessage('Lấy chi tiết đơn hàng thành công!')
  async getById(@Param('id', ParseIntPipe) id: number) {
    return this.orderService.getById(id);
  }

  @Put(':id/status')
  @ResponseMessage('Cập nhật trạng thái đơn hàng thành công!')
  async updateStatus(
    @Param('id', ParseIntPipe) id: number,
    @Body('status') status: OrderStatus,
  ) {
    return this.orderService.updateStatus(id, status);
  }
}

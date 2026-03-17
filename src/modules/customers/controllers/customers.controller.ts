import {
  Controller,
  Get,
  Post,
  Body,
  Param,
  Put,
  Query,
  ParseIntPipe,
} from '@nestjs/common';
import { CustomersService } from '../services/customers.service';
import { ResponseMessage } from '../../../common/decorators/response-message.decorator';

@Controller('customers')
export class CustomersController {
  constructor(private readonly customersService: CustomersService) {}

  @Get()
  @ResponseMessage('Lấy danh sách khách hàng thành công')
  findAll(@Query('search') search?: string) {
    return this.customersService.findAll(search);
  }

  @Get(':id')
  @ResponseMessage('Lấy chi tiết khách hàng thành công')
  findOne(@Param('id', ParseIntPipe) id: number) {
    return this.customersService.findOne(id);
  }

  @Post()
  @ResponseMessage('Tạo khách hàng mới thành công')
  create(@Body() data: any) {
    return this.customersService.create(data);
  }

  @Put(':id')
  @ResponseMessage('Cập nhật khách hàng thành công')
  update(@Param('id', ParseIntPipe) id: number, @Body() data: any) {
    return this.customersService.update(id, data);
  }
}

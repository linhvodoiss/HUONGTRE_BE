import { Controller, Get, Param, Query, ParseIntPipe } from '@nestjs/common';
import { CustomersService } from '../services/customers.service';
import { ResponseMessage } from '../../../common/decorators/response-message.decorator';

@Controller('customers')
export class CustomersController {
  constructor(private readonly customersService: CustomersService) {}

  @Get('search')
  @ResponseMessage('Tìm kiếm khách hàng thành công')
  async findByPhone(@Query('phone') phone: string) {
    return this.customersService.findByPhone(phone);
  }

  @Get()
  @ResponseMessage('Lấy danh sách khách hàng thành công')
  findAll(@Query('page') page?: string, @Query('limit') limit?: string) {
    const pageNum = page ? parseInt(page) : 1;
    const limitNum = limit ? parseInt(limit) : 10;
    return this.customersService.findAll(pageNum, limitNum);
  }

  @Get(':id')
  @ResponseMessage('Lấy chi tiết khách hàng thành công')
  findOne(@Param('id', ParseIntPipe) id: number) {
    return this.customersService.findOne(id);
  }
}

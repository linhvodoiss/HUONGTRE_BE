import {
  Controller,
  Get,
  Post,
  Body,
  Param,
  Delete,
  Put,
  Query,
  ParseIntPipe,
} from '@nestjs/common';
import { ProductsService } from '../services/products.service';
import { ResponseMessage } from '../../../common/decorators/response-message.decorator';

@Controller('products')
export class ProductsController {
  constructor(private readonly productsService: ProductsService) {}

  @Get()
  @ResponseMessage('Lấy danh sách sản phẩm thành công')
  findAll(@Query('page') page?: string, @Query('limit') limit?: string) {
    const pageNum = page ? parseInt(page) : 1;
    const limitNum = limit ? parseInt(limit) : 10;
    return this.productsService.findAll(pageNum, limitNum);
  }

  @Get(':id')
  @ResponseMessage('Lấy chi tiết sản phẩm thành công')
  findOne(@Param('id', ParseIntPipe) id: number) {
    return this.productsService.findOne(id);
  }

  @Post()
  @ResponseMessage('Tạo sản phẩm mới thành công')
  create(@Body() data: any) {
    return this.productsService.create(data);
  }

  @Put(':id')
  @ResponseMessage('Cập nhật sản phẩm thành công')
  update(@Param('id', ParseIntPipe) id: number, @Body() data: any) {
    return this.productsService.update(id, data);
  }

  @Delete(':id')
  @ResponseMessage('Xóa sản phẩm thành công')
  remove(@Param('id', ParseIntPipe) id: number) {
    return this.productsService.remove(id);
  }

  @Post('delete-multiple')
  @ResponseMessage('Xóa nhiều sản phẩm thành công')
  removeMultiple(@Body('ids') ids: number[]) {
    return this.productsService.removeMultiple(ids);
  }
}

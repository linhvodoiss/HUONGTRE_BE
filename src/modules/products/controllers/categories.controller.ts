import {
  Controller,
  Get,
  Post,
  Body,
  Param,
  Delete,
  Put,
  ParseIntPipe,
} from '@nestjs/common';
import { CategoriesService } from '../services/categories.service';
import { ResponseMessage } from '../../../common/decorators/response-message.decorator';

@Controller('categories')
export class CategoriesController {
  constructor(private readonly categoriesService: CategoriesService) {}

  @Get()
  @ResponseMessage('Lấy danh sách danh mục thành công')
  findAll() {
    return this.categoriesService.findAll();
  }

  @Get('menu')
  @ResponseMessage('Lấy menu thành công')
  getMenu() {
    return this.categoriesService.getFullMenu();
  }

  @Get(':id')
  @ResponseMessage('Lấy chi tiết danh mục thành công')
  findOne(@Param('id', ParseIntPipe) id: number) {
    return this.categoriesService.findOne(id);
  }

  @Post()
  @ResponseMessage('Tạo danh mục mới thành công')
  create(@Body() data: any) {
    return this.categoriesService.create(data);
  }

  @Put(':id')
  @ResponseMessage('Cập nhật danh mục thành công')
  update(@Param('id', ParseIntPipe) id: number, @Body() data: any) {
    return this.categoriesService.update(id, data);
  }

  @Delete(':id')
  @ResponseMessage('Xóa danh mục thành công')
  remove(@Param('id', ParseIntPipe) id: number) {
    return this.categoriesService.remove(id);
  }
}

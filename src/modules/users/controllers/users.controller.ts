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
import { UsersService } from '../services/users.service';
import { ResponseMessage } from '../../../common/decorators/response-message.decorator';

@Controller('users')
export class UsersController {
  constructor(private readonly usersService: UsersService) {}

  @Get()
  @ResponseMessage('Lấy danh sách người dùng thành công')
  findAll(@Query('page') page?: string, @Query('limit') limit?: string) {
    const pageNum = page ? parseInt(page) : 1;
    const limitNum = limit ? parseInt(limit) : 10;
    return this.usersService.findAll(pageNum, limitNum);
  }

  @Get(':id')
  @ResponseMessage('Lấy chi tiết người dùng thành công')
  findOne(@Param('id', ParseIntPipe) id: number) {
    return this.usersService.findOneById(id);
  }

  @Post()
  @ResponseMessage('Tạo người dùng mới thành công')
  create(@Body() data: any) {
    return this.usersService.create(data);
  }

  @Put(':id')
  @ResponseMessage('Cập nhật người dùng thành công')
  update(@Param('id', ParseIntPipe) id: number, @Body() data: any) {
    return this.usersService.update(id, data);
  }

  @Delete(':id')
  @ResponseMessage('Xóa người dùng thành công')
  remove(@Param('id', ParseIntPipe) id: number) {
    return this.usersService.remove(id);
  }
}

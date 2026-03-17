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
import { OptionsService } from '../services/options.service';
import { ResponseMessage } from '../../../common/decorators/response-message.decorator';

@Controller('options')
export class OptionsController {
  constructor(private readonly optionsService: OptionsService) {}

  // Option Groups
  @Get('groups')
  @ResponseMessage('Lấy danh sách nhóm lựa chọn thành công')
  async findAllGroups(
    @Query('page') page?: string,
    @Query('limit') limit?: string,
  ) {
    const pageNum = page ? parseInt(page) : 1;
    const limitNum = limit ? parseInt(limit) : 10;
    return this.optionsService.findAllGroups(pageNum, limitNum);
  }

  @Get('groups/:id')
  @ResponseMessage('Lấy chi tiết nhóm lựa chọn thành công')
  async findOneGroup(@Param('id', ParseIntPipe) id: number) {
    return this.optionsService.findOneGroup(id);
  }

  @Post('groups')
  @ResponseMessage('Tạo nhóm lựa chọn mới thành công')
  async createGroup(@Body() data: any) {
    return this.optionsService.createGroup(data);
  }

  @Put('groups/:id')
  @ResponseMessage('Cập nhật nhóm lựa chọn thành công')
  async updateGroup(@Param('id', ParseIntPipe) id: number, @Body() data: any) {
    return this.optionsService.updateGroup(id, data);
  }

  @Delete('groups/:id')
  @ResponseMessage('Xóa nhóm lựa chọn thành công')
  async removeGroup(@Param('id', ParseIntPipe) id: number) {
    return this.optionsService.removeGroup(id);
  }

  // Options
  @Post(':groupId/items')
  @ResponseMessage('Tạo tùy chọn mới thành công')
  async createOption(
    @Param('groupId', ParseIntPipe) groupId: number,
    @Body() data: any,
  ) {
    return this.optionsService.createOption(groupId, data);
  }

  @Put('items/:id')
  @ResponseMessage('Cập nhật tùy chọn thành công')
  async updateOption(@Param('id', ParseIntPipe) id: number, @Body() data: any) {
    return this.optionsService.updateOption(id, data);
  }

  @Delete('items/:id')
  @ResponseMessage('Xóa tùy chọn thành công')
  async removeOption(@Param('id', ParseIntPipe) id: number) {
    return this.optionsService.removeOption(id);
  }
}

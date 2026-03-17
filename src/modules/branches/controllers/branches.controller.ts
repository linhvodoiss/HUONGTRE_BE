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
import { BranchesService } from '../services/branches.service';
import { CreateBranchDto, UpdateBranchDto } from '../dto/branch.dto';
import { ResponseMessage } from '../../../common/decorators/response-message.decorator';

@Controller('branches')
export class BranchesController {
  constructor(private readonly branchesService: BranchesService) {}

  @Get()
  @ResponseMessage('Lấy danh sách chi nhánh thành công')
  findAll(
    @Query('search') search?: string,
    @Query('isActive') isActive?: string,
  ) {
    const active = isActive === 'true' ? true : isActive === 'false' ? false : undefined;
    return this.branchesService.findAll(search, active);
  }

  @Get(':id')
  @ResponseMessage('Lấy chi tiết chi nhánh thành công')
  findOne(@Param('id', ParseIntPipe) id: number) {
    return this.branchesService.findOne(id);
  }

  @Post()
  @ResponseMessage('Tạo chi nhánh mới thành công')
  create(@Body() createBranchDto: CreateBranchDto) {
    return this.branchesService.create(createBranchDto);
  }

  @Put(':id')
  @ResponseMessage('Cập nhật chi nhánh thành công')
  update(
    @Param('id', ParseIntPipe) id: number,
    @Body() updateBranchDto: UpdateBranchDto,
  ) {
    return this.branchesService.update(id, updateBranchDto);
  }

  @Delete(':id')
  @ResponseMessage('Xóa chi nhánh thành công')
  remove(@Param('id', ParseIntPipe) id: number) {
    return this.branchesService.remove(id);
  }

  @Post('delete-multiple')
  @ResponseMessage('Xóa nhiều chi nhánh thành công')
  removeMultiple(@Body('ids') ids: number[]) {
    return this.branchesService.removeMultiple(ids);
  }
}

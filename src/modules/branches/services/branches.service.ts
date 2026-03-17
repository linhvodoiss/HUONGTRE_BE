import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, Like } from 'typeorm';
import { Branch } from '../entities/branch.entity';
import { CreateBranchDto, UpdateBranchDto } from '../dto/branch.dto';

@Injectable()
export class BranchesService {
  constructor(
    @InjectRepository(Branch)
    private branchRepository: Repository<Branch>,
  ) {}

  async findAll(search?: string, isActive?: boolean) {
    const where: any = { isDeleted: false };
    if (search) {
      where.name = Like(`%${search}%`);
    }
    if (isActive !== undefined) {
      where.isActive = isActive;
    }

    return this.branchRepository.find({
      where,
      order: { id: 'DESC' },
    });
  }

  async findOne(id: number) {
    const branch = await this.branchRepository.findOne({
      where: { id, isDeleted: false },
    });
    if (!branch) {
      throw new NotFoundException(`Chi nhánh với ID ${id} không tồn tại`);
    }
    return branch;
  }

  async create(dto: CreateBranchDto) {
    const branch = this.branchRepository.create(dto);
    return this.branchRepository.save(branch);
  }

  async update(id: number, dto: UpdateBranchDto) {
    const branch = await this.findOne(id);
    Object.assign(branch, dto);
    return this.branchRepository.save(branch);
  }

  async remove(id: number) {
    const branch = await this.findOne(id);
    branch.isDeleted = true;
    return this.branchRepository.save(branch);
  }

  async removeMultiple(ids: number[]) {
    return this.branchRepository.update(ids, { isDeleted: true });
  }
}

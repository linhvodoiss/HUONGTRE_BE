import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Branch } from '../entities/branch.entity';
import { CreateBranchDto, UpdateBranchDto } from '../dto/branch.dto';

@Injectable()
export class BranchesService {
  constructor(
    @InjectRepository(Branch)
    private branchRepository: Repository<Branch>,
  ) {}

  async findAll(page: number = 1, limit: number = 10) {
    const [data, total] = await this.branchRepository.findAndCount({
      where: { isDeleted: false },
      order: { id: 'DESC' },
      skip: (page - 1) * limit,
      take: limit,
    });

    const lastPage = Math.ceil(total / limit);

    return {
      data,
      meta: {
        total,
        page,
        limit,
        lastPage,
      },
    };
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

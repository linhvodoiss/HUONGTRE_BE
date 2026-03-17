import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { DeepPartial, Repository } from 'typeorm';
import { OptionGroup } from '../entities/option-group.entity';
import { Option } from '../entities/option.entity';

@Injectable()
export class OptionsService {
  constructor(
    @InjectRepository(OptionGroup)
    private optionGroupRepository: Repository<OptionGroup>,
    @InjectRepository(Option)
    private optionRepository: Repository<Option>,
  ) {}

  // --- Option Group CRUD ---
  async findAllGroups(page: number = 1, limit: number = 10) {
    const [data, total] = await this.optionGroupRepository.findAndCount({
      where: { isDeleted: false },
      order: { displayOrder: 'ASC', id: 'DESC' },
      skip: (page - 1) * limit,
      take: limit,
    });

    return {
      data,
      meta: {
        total,
        page,
        limit,
        lastPage: Math.ceil(total / limit),
      },
    };
  }

  async findOneGroup(id: number) {
    const group = await this.optionGroupRepository.findOne({
      where: { id, isDeleted: false },
      relations: ['options'],
    });
    if (!group)
      throw new NotFoundException(`Nhóm lựa chọn ID ${id} không tồn tại`);
    return group;
  }

  async createGroup(data: DeepPartial<OptionGroup>) {
    const group = this.optionGroupRepository.create(data);
    return this.optionGroupRepository.save(group);
  }

  async updateGroup(id: number, data: DeepPartial<OptionGroup>) {
    const group = await this.findOneGroup(id);
    Object.assign(group, data);
    return this.optionGroupRepository.save(group);
  }

  async removeGroup(id: number) {
    const group = await this.findOneGroup(id);
    group.isDeleted = true;
    return this.optionGroupRepository.save(group);
  }

  // --- Option CRUD ---
  async createOption(groupId: number, data: DeepPartial<Option>) {
    const group = await this.findOneGroup(groupId);
    const option = this.optionRepository.create({
      ...data,
      optionGroup: group,
    });
    return this.optionRepository.save(option);
  }

  async updateOption(id: number, data: DeepPartial<Option>) {
    const option = await this.optionRepository.findOne({
      where: { id, isDeleted: false },
    });
    if (!option) throw new NotFoundException(`Tùy chọn ID ${id} không tồn tại`);
    Object.assign(option, data);
    return this.optionRepository.save(option);
  }

  async removeOption(id: number) {
    const option = await this.optionRepository.findOne({
      where: { id, isDeleted: false },
    });
    if (!option) throw new NotFoundException(`Tùy chọn ID ${id} không tồn tại`);
    option.isDeleted = true;
    return this.optionRepository.save(option);
  }
}

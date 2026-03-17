import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, Like } from 'typeorm';
import { Customer } from '../entities/customer.entity';

@Injectable()
export class CustomersService {
  constructor(
    @InjectRepository(Customer)
    private customerRepository: Repository<Customer>,
  ) {}

  async findAll(search?: string) {
    const where: any = { isDeleted: false };
    if (search) {
      where.phone = Like(`%${search}%`);
    }

    return this.customerRepository.find({
      where,
      order: { id: 'DESC' },
    });
  }

  async findOne(id: number) {
    const customer = await this.customerRepository.findOne({
      where: { id, isDeleted: false },
    });
    if (!customer) {
      throw new NotFoundException(`Khách hàng ID ${id} không tồn tại`);
    }
    return customer;
  }

  async findByPhone(phone: string) {
    return this.customerRepository.findOne({
      where: { phone, isDeleted: false },
    });
  }

  async create(data: any) {
    const customer = this.customerRepository.create(data);
    return this.customerRepository.save(customer);
  }

  async update(id: number, data: any) {
    const customer = await this.findOne(id);
    Object.assign(customer, data);
    return this.customerRepository.save(customer);
  }
}

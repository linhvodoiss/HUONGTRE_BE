import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Customer } from '../entities/customer.entity';

@Injectable()
export class CustomersService {
  constructor(
    @InjectRepository(Customer)
    private customerRepository: Repository<Customer>,
  ) {}

  async findAll(page: number = 1, limit: number = 10) {
    const [data, total] = await this.customerRepository.findAndCount({
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
}

import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, Like } from 'typeorm';
import { Product } from '../entities/product.entity';

@Injectable()
export class ProductsService {
  constructor(
    @InjectRepository(Product)
    private productRepository: Repository<Product>,
  ) {}

  async findAll(search?: string, isActive?: boolean, categoryId?: number) {
    const where: any = { isDeleted: false };
    if (search) {
      where.name = Like(`%${search}%`);
    }
    if (isActive !== undefined) {
      where.isActive = isActive;
    }
    if (categoryId) {
      where.category = { id: categoryId };
    }

    return this.productRepository.find({
      where,
      relations: ['category'],
      order: { id: 'DESC' },
    });
  }

  async findOne(id: number) {
    const product = await this.productRepository.findOne({
      where: { id, isDeleted: false },
      relations: ['category'],
    });
    if (!product) {
      throw new NotFoundException(`Sản phẩm ID ${id} không tồn tại`);
    }
    return product;
  }

  async create(data: any) {
    const product = this.productRepository.create(data);
    return this.productRepository.save(product);
  }

  async update(id: number, data: any) {
    const product = await this.findOne(id);
    Object.assign(product, data);
    return this.productRepository.save(product);
  }

  async remove(id: number) {
    const product = await this.findOne(id);
    product.isDeleted = true;
    return this.productRepository.save(product);
  }

  async removeMultiple(ids: number[]) {
    return this.productRepository.update(ids, { isDeleted: true });
  }
}

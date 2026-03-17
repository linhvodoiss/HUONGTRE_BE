import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Category } from '../entities/category.entity';
import { Product } from '../entities/product.entity';

@Injectable()
export class CategoriesService {
  constructor(
    @InjectRepository(Category)
    private categoryRepository: Repository<Category>,
  ) {}

  async findAll() {
    return this.categoryRepository.find({
      where: { isDeleted: false },
      order: { id: 'DESC' },
    });
  }

  async findOne(id: number) {
    const category = await this.categoryRepository.findOne({
      where: { id, isDeleted: false },
    });
    if (!category) {
      throw new NotFoundException(`Danh mục ID ${id} không tồn tại`);
    }
    return category;
  }

  async getFullMenu() {
    const categories = await this.categoryRepository
      .createQueryBuilder('category')
      .leftJoinAndSelect('category.products', 'product')
      .leftJoinAndSelect('product.productOptionGroups', 'pog')
      .leftJoinAndSelect('pog.optionGroup', 'og')
      .leftJoinAndSelect('og.options', 'opt')
      .where('category.isDeleted = :isDeleted', { isDeleted: false })
      .andWhere('category.isActive = :isActive', { isActive: true })
      .orderBy('category.id', 'ASC')
      .addOrderBy('product.id', 'ASC')
      .getMany();

    return categories.map((category) => ({
      id: Number(category.id),
      name: category.name,
      description: category.description,
      imageUrl: category.imageUrl,
      products: (category.products || [])
        .filter((p) => p.isActive && !p.isDeleted)
        .map((p) => ({
          id: Number(p.id),
          name: p.name,
          description: p.description,
          imageUrl: p.imageUrl,
          price: Number(p.price),
          optionGroups: (p.productOptionGroups || [])
            .map((pog) => pog.optionGroup)
            .filter((og) => og && og.isActive && !og.isDeleted)
            .map((og) => ({
              id: Number(og.id),
              name: og.name,
              selectType: og.selectType,
              required: og.required,
              minSelect: og.minSelect,
              maxSelect: og.maxSelect,
              displayOrder: og.displayOrder,
              options: (og.options || [])
                .filter((opt) => opt.isActive && !opt.isDeleted)
                .map((opt) => ({
                  id: Number(opt.id),
                  name: opt.name,
                  description: opt.description,
                  price: Number(opt.price),
                  displayOrder: opt.displayOrder,
                  isActive: opt.isActive,
                })),
            })),
        })),
    }));
  }

  async create(data: any) {
    const category = this.categoryRepository.create(data);
    return this.categoryRepository.save(category);
  }

  async update(id: number, data: any) {
    const category = await this.findOne(id);
    Object.assign(category, data);
    return this.categoryRepository.save(category);
  }

  async remove(id: number) {
    const category = await this.findOne(id);
    category.isDeleted = true;
    return this.categoryRepository.save(category);
  }
}

import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { DeepPartial, Repository } from 'typeorm';
import { Product } from '../entities/product.entity';
import { ProductOptionGroup } from '../entities/product-option-group.entity';

@Injectable()
export class ProductsService {
  constructor(
    @InjectRepository(Product)
    private productRepository: Repository<Product>,
    @InjectRepository(ProductOptionGroup)
    private productOptionGroupRepository: Repository<ProductOptionGroup>,
  ) {}

  async findAll(page: number = 1, limit: number = 10) {
    const [data, total] = await this.productRepository.findAndCount({
      where: { isDeleted: false },
      relations: ['category'],
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
    const product = await this.productRepository.findOne({
      where: { id, isDeleted: false },
      relations: [
        'category',
        'productOptionGroups',
        'productOptionGroups.optionGroup',
        'productOptionGroups.optionGroup.options',
      ],
    });
    if (!product) {
      throw new NotFoundException(`Sản phẩm ID ${id} không tồn tại`);
    }
    return product;
  }

  async create(data: DeepPartial<Product> & { optionGroupIds?: number[] }) {
    const { optionGroupIds, ...rest } = data;
    const product = this.productRepository.create(rest as object);
    const savedProduct = await this.productRepository.save(product);

    // Nếu có chọn nhóm tùy chọn ngay lúc tạo
    if (
      optionGroupIds &&
      Array.isArray(optionGroupIds) &&
      optionGroupIds.length > 0
    ) {
      const links = optionGroupIds.map((ogId: number) => ({
        product: { id: savedProduct.id },
        optionGroup: { id: ogId },
      }));
      await this.productOptionGroupRepository.save(links);
    }

    return this.findOne(savedProduct.id);
  }

  async update(
    id: number,
    data: DeepPartial<Product> & { optionGroupIds?: number[] },
  ) {
    const product = await this.findOne(id);
    const { optionGroupIds, ...rest } = data;

    Object.assign(product, rest);
    await this.productRepository.save(product);

    // Nếu truyền lên danh sách IDs, thực hiện đồng bộ liên kết
    if (optionGroupIds && Array.isArray(optionGroupIds)) {
      // Xóa các liên kết cũ
      await this.productOptionGroupRepository.delete({ product: { id } });

      // Tạo các liên kết mới
      if (optionGroupIds.length > 0) {
        const newLinks = optionGroupIds.map((ogId) => ({
          product: { id },
          optionGroup: { id: ogId },
        }));
        await this.productOptionGroupRepository.save(newLinks);
      }
    }

    return this.findOne(id); // Trả về data đầy đủ sau khi update
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

import { Injectable } from '@nestjs/common';
import { Seeder } from 'nestjs-seeder';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Product } from '../../modules/products/entities/product.entity';
import { Category } from '../../modules/products/entities/category.entity';

@Injectable()
export class ProductsSeeder implements Seeder {
  constructor(
    @InjectRepository(Product)
    private readonly productRepository: Repository<Product>,
    @InjectRepository(Category)
    private readonly categoryRepository: Repository<Category>,
  ) {}

  async seed(): Promise<any> {
    const teaCat = await this.categoryRepository.findOneBy({ name: 'Trà Sữa' });
    const fruitCat = await this.categoryRepository.findOneBy({
      name: 'Trà Trái Cây',
    });

    const products = [
      {
        name: 'Trà Sữa Truyền Thống',
        price: 35000,
        description: 'Trà sữa đậm béo',
        category: teaCat,
        imageUrl: 'https://images.unsplash.com/photo-1544436429-c728d797652e',
      },
      {
        name: 'Trà Đào Hồng Đài',
        price: 40000,
        description: 'Thanh mát vị đào',
        category: fruitCat,
        imageUrl: 'https://images.unsplash.com/photo-1556679343-c7306c1976bc',
      },
    ];

    return this.productRepository.save(products as any[]);
  }

  async drop(): Promise<any> {
    return this.productRepository.delete({});
  }
}

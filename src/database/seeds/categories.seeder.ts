import { Injectable } from '@nestjs/common';
import { Seeder } from 'nestjs-seeder';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Category } from '../../modules/products/entities/category.entity';

@Injectable()
export class CategoriesSeeder implements Seeder {
  constructor(
    @InjectRepository(Category)
    private readonly categoryRepository: Repository<Category>,
  ) {}

  async seed(): Promise<any> {
    const categories = [
      { name: 'Trà Sữa', description: 'Trà sữa đậm vị Hương Tre' },
      { name: 'Trà Trái Cây', description: 'Trà kết hợp trái cây tươi 100%' },
      { name: 'Topping', description: 'Các loại thạch' },
    ];

    return this.categoryRepository.save(categories);
  }

  async drop(): Promise<any> {
    return this.categoryRepository.delete({});
  }
}

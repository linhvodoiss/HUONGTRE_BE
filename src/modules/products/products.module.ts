import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Product } from './entities/product.entity';
import { Category } from './entities/category.entity';
import { Option } from './entities/option.entity';
import { OptionGroup } from './entities/option-group.entity';
import { ProductsService } from './services/products.service';
import { CategoriesService } from './services/categories.service';
import { ProductsController } from './controllers/products.controller';
import { CategoriesController } from './controllers/categories.controller';
import { ProductOptionGroup } from './entities/product-option-group.entity';

@Module({
  imports: [TypeOrmModule.forFeature([Product, Category, Option, OptionGroup, ProductOptionGroup])],
  controllers: [ProductsController, CategoriesController],
  providers: [ProductsService, CategoriesService],
  exports: [TypeOrmModule, ProductsService],
})
export class ProductsModule {}

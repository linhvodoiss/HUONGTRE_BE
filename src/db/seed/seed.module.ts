import { Module } from '@nestjs/common';
import { SeedService } from './seed.service';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Branch } from '../../modules/branches/entities/branch.entity';
import { Category } from '../../modules/products/entities/category.entity';
import { Product } from '../../modules/products/entities/product.entity';
import { OptionGroup } from '../../modules/products/entities/option-group.entity';
import { Option } from '../../modules/products/entities/option.entity';
import { ProductOptionGroup } from '../../modules/products/entities/product-option-group.entity';
import { User } from '../../modules/users/entities/user.entity';
import { Customer } from '../../modules/customers/entities/customer.entity';

@Module({
  imports: [
    TypeOrmModule.forFeature([
      Branch,
      Category,
      Product,
      OptionGroup,
      Option,
      ProductOptionGroup,
      User,
      Customer,
    ]),
  ],
  providers: [SeedService],
  exports: [SeedService],
})
export class SeedModule {}

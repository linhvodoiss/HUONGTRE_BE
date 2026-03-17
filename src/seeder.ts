import { seeder } from 'nestjs-seeder';
import { TypeOrmModule } from '@nestjs/typeorm';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { User } from './modules/users/entities/user.entity';
import { Branch } from './modules/branches/entities/branch.entity';
import { Category } from './modules/products/entities/category.entity';
import { Product } from './modules/products/entities/product.entity';
import { OptionGroup } from './modules/products/entities/option-group.entity';
import { Option } from './modules/products/entities/option.entity';
import { ProductOptionGroup } from './modules/products/entities/product-option-group.entity';
import { Customer } from './modules/customers/entities/customer.entity';
import { UsersSeeder } from './database/seeds/users.seeder';
import { BranchesSeeder } from './database/seeds/branches.seeder';
import { CategoriesSeeder } from './database/seeds/categories.seeder';
import { ProductsSeeder } from './database/seeds/products.seeder';
import { OptionsSeeder } from './database/seeds/options.seeder';

seeder({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true,
    }),
    TypeOrmModule.forRootAsync({
      imports: [ConfigModule],
      useFactory: (configService: ConfigService) => ({
        type: 'mysql',
        host: configService.get<string>('DB_HOST'),
        port: configService.get<number>('DB_PORT'),
        username: configService.get<string>('DB_USERNAME'),
        password: configService.get<string>('DB_PASSWORD'),
        database: configService.get<string>('DB_NAME'),
        entities: [__dirname + '/**/*.entity{.ts,.js}'],
        synchronize: true, // Tận dụng seeder để tạo luôn bảng nếu chưa có (Rất tiện cho setup mới)
        logging: false,
      }),
      inject: [ConfigService],
    }),
    TypeOrmModule.forFeature([
      User,
      Branch,
      Category,
      Product,
      OptionGroup,
      Option,
      ProductOptionGroup,
      Customer,
    ]),
  ],
}).run([UsersSeeder, BranchesSeeder, CategoriesSeeder, ProductsSeeder, OptionsSeeder]);

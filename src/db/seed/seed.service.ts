import { Injectable, OnModuleInit, Logger } from '@nestjs/common';
import { DataSource } from 'typeorm';
import * as bcrypt from 'bcrypt';
import { Branch } from '../../modules/branches/entities/branch.entity';
import { Category } from '../../modules/products/entities/category.entity';
import { Product } from '../../modules/products/entities/product.entity';
import { OptionGroup } from '../../modules/products/entities/option-group.entity';
import { Option } from '../../modules/products/entities/option.entity';
import { ProductOptionGroup } from '../../modules/products/entities/product-option-group.entity';
import { User } from '../../modules/users/entities/user.entity';
import { Customer } from '../../modules/customers/entities/customer.entity';
import { OptionSelectType } from '../../modules/products/enums/option.enum';
import { Role, UserStatus } from '../../modules/users/enums/user.enum';

@Injectable()
export class SeedService implements OnModuleInit {
  private readonly logger = new Logger(SeedService.name);

  constructor(private readonly dataSource: DataSource) {}

  async onModuleInit() {
    this.logger.log('🚀 Checking database for seeding...');
    await this.seedData();
  }

  async seedData() {
    // 🛡️ GLOBAL GUARD: Nếu đã có User (Admin/Staff) thì coi như DB đã có dữ liệu, KHÔNG seed nữa.
    const userCount = await this.dataSource.manager.count(User);
    if (userCount > 0) {
      this.logger.log('✅ Database already initialized. Skipping seed process.');
      return;
    }

    const queryRunner = this.dataSource.createQueryRunner();
    await queryRunner.connect();
    await queryRunner.startTransaction();

    try {
      this.logger.log('📋 Starting full database seeding...');

      // 1. Seed Users (Admin/Staff)
      const hashedPassword = await bcrypt.hash('admin123', 10);
      await queryRunner.manager.save(User, [
        {
          username: 'admin',
          email: 'admin@huongtre.com',
          password: hashedPassword,
          firstName: 'Admin',
          lastName: 'System',
          phoneNumber: '0900000001',
          role: Role.ADMIN,
          status: UserStatus.ACTIVE,
        },
        {
          username: 'staff1',
          email: 'staff1@huongtre.com',
          password: hashedPassword,
          firstName: 'Nguyễn',
          lastName: 'Nhân Viên',
          phoneNumber: '0900000002',
          role: Role.STAFF,
          status: UserStatus.ACTIVE,
        },
      ]);

      // 2. Seed Branches
      await queryRunner.manager.save(Branch, [
        {
          name: 'Hương Tre - Quận 1',
          address: '123 Nguyễn Huệ, P. Bến Nghé, Q.1',
          phone: '0283800111',
        },
        {
          name: 'Hương Tre - Bình Thạnh',
          address: '45 Phan Xích Long, Bình Thạnh',
          phone: '0283800222',
        },
      ]);

      // 3. Seed Categories
      const categories = await queryRunner.manager.save(Category, [
        { name: 'Trà Sữa', description: 'Trà sữa đậm vị Hương Tre' },
        { name: 'Trà Trái Cây', description: 'Trà kết hợp trái cây tươi 100%' },
        { name: 'Topping', description: 'Các loại thạch và trân châu' },
      ]);
      const teaCat = categories.find((c) => c.name === 'Trà Sữa');
      const fruitCat = categories.find((c) => c.name === 'Trà Trái Cây');

      // 4. Seed OptionGroups
      const sugarGroup = await queryRunner.manager.save(OptionGroup, {
        name: 'Chọn mức đường',
        selectType: OptionSelectType.SINGLE,
        required: true,
        minSelect: 1,
        maxSelect: 1,
        displayOrder: 1,
      });

      const toppingGroup = await queryRunner.manager.save(OptionGroup, {
        name: 'Chọn Topping',
        selectType: OptionSelectType.MULTIPLE,
        required: false,
        minSelect: 0,
        maxSelect: 5,
        displayOrder: 2,
      });

      // 4b. Seed Options
      await queryRunner.manager.save(Option, [
        { name: '100% Đường', price: 0, optionGroup: sugarGroup, displayOrder: 1 },
        { name: '70% Đường', price: 0, optionGroup: sugarGroup, displayOrder: 2 },
        { name: 'Trân châu đen', price: 5000, optionGroup: toppingGroup, displayOrder: 1 },
        { name: 'Thạch sương sáo', price: 5000, optionGroup: toppingGroup, displayOrder: 2 },
      ] as any[]);

      // 5. Seed Products
      const products = await queryRunner.manager.save(Product, [
        {
          name: 'Trà Sữa Truyền Thống',
          price: 35000,
          description: 'Trà sữa đậm béo',
          category: teaCat as any,
          imageUrl: 'https://images.unsplash.com/photo-1544436429-c728d797652e',
        },
        {
          name: 'Trà Đào Hồng Đài',
          price: 40000,
          description: 'Thanh mát vị đào',
          category: fruitCat as any,
          imageUrl: 'https://images.unsplash.com/photo-1556679343-c7306c1976bc',
        },
      ] as any[]);

      // 6. Link Product to OptionGroups
      await queryRunner.manager.save(ProductOptionGroup, [
        { product: products[0], optionGroup: sugarGroup },
        { product: products[0], optionGroup: toppingGroup },
        { product: products[1], optionGroup: sugarGroup },
      ] as any[]);

      // 7. Seed Customers
      await queryRunner.manager.save(Customer, [
        { phone: '0987654321', totalOrders: 0, totalSpent: 0 },
      ]);

      await queryRunner.commitTransaction();
      this.logger.log('🏁 Full database seeding completed successfully!');
    } catch (error) {
      await queryRunner.rollbackTransaction();
      this.logger.error(
        '❌ Seeding failed: ' + (error instanceof Error ? error.message : String(error)),
      );
    } finally {
      await queryRunner.release();
    }
  }
}

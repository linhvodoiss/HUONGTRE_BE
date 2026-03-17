import { Injectable } from '@nestjs/common';
import { Seeder } from 'nestjs-seeder';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { OptionGroup } from '../../modules/products/entities/option-group.entity';
import { Option } from '../../modules/products/entities/option.entity';
import { Product } from '../../modules/products/entities/product.entity';
import { ProductOptionGroup } from '../../modules/products/entities/product-option-group.entity';
import { OptionSelectType } from '../../modules/products/enums/option.enum';

@Injectable()
export class OptionsSeeder implements Seeder {
  constructor(
    @InjectRepository(OptionGroup)
    private readonly optionGroupRepository: Repository<OptionGroup>,
    @InjectRepository(Option)
    private readonly optionRepository: Repository<Option>,
    @InjectRepository(Product)
    private readonly productRepository: Repository<Product>,
    @InjectRepository(ProductOptionGroup)
    private readonly productOptionGroupRepository: Repository<ProductOptionGroup>,
  ) {}

  async seed(): Promise<any> {
    // 1. Tạo Nhóm Tùy Chọn
    const groups = await this.optionGroupRepository.save([
      {
        name: 'Chọn mức đường',
        selectType: OptionSelectType.SINGLE,
        required: true,
        minSelect: 1,
        maxSelect: 1,
        displayOrder: 1,
      },
      {
        name: 'Chọn Topping',
        selectType: OptionSelectType.MULTIPLE,
        required: false,
        minSelect: 0,
        maxSelect: 5,
        displayOrder: 2,
      },
    ]);

    const sugarGroup = groups.find(g => g.name === 'Chọn mức đường');
    const toppingGroup = groups.find(g => g.name === 'Chọn Topping');

    // 2. Tạo các Tùy chọn (Options) cho từng nhóm
    await this.optionRepository.save([
      { name: '100% Đường', price: 0, optionGroup: sugarGroup, displayOrder: 1 },
      { name: '70% Đường', price: 0, optionGroup: sugarGroup, displayOrder: 2 },
      { name: '50% Đường', price: 0, optionGroup: sugarGroup, displayOrder: 3 },
      { name: 'Trân châu đen', price: 5000, optionGroup: toppingGroup, displayOrder: 1 },
      { name: 'Thạch sương sáo', price: 5000, optionGroup: toppingGroup, displayOrder: 2 },
      { name: 'Kem Cheese', price: 10000, optionGroup: toppingGroup, displayOrder: 3 },
    ] as any[]);

    // 3. Liên kết Nhóm vào Sản phẩm
    const products = await this.productRepository.find();
    const links: any[] = [];

    for (const product of products) {
      links.push({ product, optionGroup: sugarGroup });
      if (product.name.includes('Trà Sữa')) {
        links.push({ product, optionGroup: toppingGroup });
      }
    }

    return this.productOptionGroupRepository.save(links as any[]);
  }

  async drop(): Promise<any> {
    await this.productOptionGroupRepository.delete({});
    await this.optionRepository.delete({});
    return this.optionGroupRepository.delete({});
  }
}

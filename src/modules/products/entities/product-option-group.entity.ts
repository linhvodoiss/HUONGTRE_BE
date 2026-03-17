import { Entity, PrimaryGeneratedColumn, ManyToOne, JoinColumn } from 'typeorm';
import { Product } from './product.entity';
import { OptionGroup } from './option-group.entity';

@Entity({ name: 'productoptiongroup' })
export class ProductOptionGroup {
  @PrimaryGeneratedColumn({ type: 'bigint' })
  id: number;

  @ManyToOne(() => Product, (product) => product.productOptionGroups)
  @JoinColumn({ name: 'product_id' })
  product: Product;

  @ManyToOne(() => OptionGroup)
  @JoinColumn({ name: 'option_group_id' })
  optionGroup: OptionGroup;
}

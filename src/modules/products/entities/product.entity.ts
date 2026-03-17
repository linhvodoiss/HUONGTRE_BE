import { Entity, Column, ManyToOne, JoinColumn, OneToMany } from 'typeorm';
import { AbstractEntity } from '../../../common/entities/abstract.entity';
import { Category } from './category.entity';
import { ProductOptionGroup } from './product-option-group.entity';

@Entity({ name: 'product' })
export class Product extends AbstractEntity {
  @Column({ unique: true })
  name: string;

  @Column({ nullable: true })
  description: string;

  @Column({ name: 'imageUrl', nullable: true })
  imageUrl: string;

  @Column({ type: 'double' })
  price: number;

  @Column({
    name: 'isActive',
    type: 'bit',
    width: 1,
    default: () => "'\\1'",
    transformer: {
      from: (v: Buffer) => v !== null && v.length > 0 && v[0] === 1,
      to: (v: boolean) => (v ? 1 : 0),
    },
  })
  isActive: boolean;

  @ManyToOne(() => Category, (category) => category.products)
  @JoinColumn({ name: 'category_id' })
  category: Category;

  // Tạm thời comment các relate chưa tạo entity
  // @OneToMany(() => BranchProduct, (bp) => bp.product)
  // branchProducts: BranchProduct[];

  @OneToMany(() => ProductOptionGroup, (pog) => pog.product)
  productOptionGroups: ProductOptionGroup[];
}

import { Entity, Column, OneToMany } from 'typeorm';
import { AbstractEntity } from '../../../common/entities/abstract.entity';
import { Product } from './product.entity';

@Entity({ name: 'category' })
export class Category extends AbstractEntity {
  @Column({ unique: true })
  name: string;

  @Column({ nullable: true })
  description: string;

  @Column({ name: 'imageUrl', nullable: true })
  imageUrl: string;

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

  @OneToMany(() => Product, (product) => product.category)
  products: Product[];
}

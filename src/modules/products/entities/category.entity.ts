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
    default: true,
  })
  isActive: boolean;

  @OneToMany(() => Product, (product) => product.category)
  products: Product[];
}

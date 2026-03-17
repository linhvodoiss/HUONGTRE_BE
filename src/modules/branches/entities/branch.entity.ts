import { Entity, Column } from 'typeorm';
import { AbstractEntity } from '../../../common/entities/abstract.entity';

@Entity({ name: 'branch' })
export class Branch extends AbstractEntity {
  @Column({ unique: true })
  name: string;

  @Column({ nullable: true })
  description: string;

  @Column({ name: 'imageUrl', nullable: true })
  imageUrl: string;

  @Column({ nullable: true })
  address: string;

  @Column({ nullable: true })
  phone: string;

  @Column({
    name: 'isActive',
    type: 'tinyint',
    width: 1,
    default: 1,
  })
  isActive: boolean;
}

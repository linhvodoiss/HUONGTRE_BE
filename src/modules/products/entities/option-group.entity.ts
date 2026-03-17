import { Entity, Column, ManyToOne, JoinColumn, OneToMany } from 'typeorm';
import { AbstractEntity } from '../../../common/entities/abstract.entity';
import { OptionSelectType } from '../enums/option.enum';
import { Option } from './option.entity';

@Entity({ name: 'optiongroup' })
export class OptionGroup extends AbstractEntity {
  @Column()
  name: string;

  @Column({
    type: 'enum',
    enum: OptionSelectType,
    name: 'select_type',
  })
  selectType: OptionSelectType;

  @Column({
    type: 'bit',
    width: 1,
    default: () => "'\\0'",
    transformer: {
      from: (v: Buffer) => v !== null && v.length > 0 && v[0] === 1,
      to: (v: boolean) => (v ? 1 : 0),
    },
  })
  required: boolean;

  @Column({ name: 'min_select', default: 0 })
  minSelect: number;

  @Column({ name: 'max_select', default: 1 })
  maxSelect: number;

  @Column({ name: 'display_order', default: 0 })
  displayOrder: number;

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

  @OneToMany(() => Option, (option) => option.optionGroup)
  options: Option[];
}

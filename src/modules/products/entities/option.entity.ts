import { Entity, Column, ManyToOne, JoinColumn } from 'typeorm';
import { AbstractEntity } from '../../../common/entities/abstract.entity';
import { OptionGroup } from './option-group.entity';

@Entity({ name: 'options' })
export class Option extends AbstractEntity {
  @Column()
  name: string;

  @Column({ nullable: true })
  description: string;

  @Column({ type: 'double', default: 0.0 })
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

  @Column({ name: 'display_order', default: 0 })
  displayOrder: number;

  @ManyToOne(() => OptionGroup, (group) => group.options)
  @JoinColumn({ name: 'option_group_id' })
  optionGroup: OptionGroup;
}

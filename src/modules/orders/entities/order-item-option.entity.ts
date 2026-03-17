import { Entity, Column, ManyToOne, JoinColumn } from 'typeorm';
import { AbstractEntity } from '../../../common/entities/abstract.entity';
import { OrderItem } from './order-item.entity';

@Entity({ name: 'OrderItemOption' })
export class OrderItemOption extends AbstractEntity {
  @Column({ name: 'option_id' })
  optionId: number;

  @Column({ name: 'option_name' })
  optionName: string;

  @Column({ name: 'option_price', type: 'double' })
  optionPrice: number;

  @ManyToOne(() => OrderItem, (item) => item.options)
  @JoinColumn({ name: 'order_item_id' })
  orderItem: OrderItem;
}

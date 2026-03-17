import { Entity, Column, ManyToOne, JoinColumn, OneToMany } from 'typeorm';
import { AbstractEntity } from '../../../common/entities/abstract.entity';
import { Order } from './order.entity';
import { Product } from '../../products/entities/product.entity';
import { OrderItemOption } from './order-item-option.entity';

@Entity({ name: 'OrderItem' })
export class OrderItem extends AbstractEntity {
  @Column()
  quantity: number;

  @Column({ name: 'base_price', type: 'double' })
  basePrice: number;

  @Column({ nullable: true })
  note: string;

  @ManyToOne(() => Order, (order) => order.items)
  @JoinColumn({ name: 'order_id' })
  order: Order;

  @ManyToOne(() => Product)
  @JoinColumn({ name: 'product_id' })
  product: Product;

  @OneToMany(() => OrderItemOption, (option) => option.orderItem, {
    cascade: true,
  })
  options: OrderItemOption[];
}

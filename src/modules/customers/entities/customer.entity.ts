import { Entity, Column, OneToMany } from 'typeorm';
import { AbstractEntity } from '../../../common/entities/abstract.entity';
import { Order } from '../../orders/entities/order.entity';

@Entity({ name: 'customer' })
export class Customer extends AbstractEntity {
  @Column()
  phone: string;

  @Column({ name: 'totalOrders', default: 0 })
  totalOrders: number;

  @Column({ name: 'totalSpent', type: 'double', default: 0.0 })
  totalSpent: number;

  @Column({ nullable: true })
  note: string;

  @OneToMany(() => Order, (order) => order.customer)
  orders: Order[];
}

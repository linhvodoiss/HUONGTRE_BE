import { Entity, Column, ManyToOne, JoinColumn, OneToMany } from 'typeorm';
import { AbstractEntity } from '../../../common/entities/abstract.entity';
import { Customer } from '../../customers/entities/customer.entity';
import { OrderItem } from './order-item.entity';

@Entity({ name: 'order' })
export class Order extends AbstractEntity {
  @Column({ name: 'totalAmount' })
  totalAmount: number;

  @Column()
  status: string; // Chờ xác nhận, Hoàn thành, Hủy...

  @Column({ name: 'receiverName', nullable: true })
  receiverName: string;

  @Column({ name: 'receiverPhone', nullable: true })
  receiverPhone: string;

  @Column({ name: 'deliveryAddress', nullable: true })
  deliveryAddress: string;

  @Column({ nullable: true })
  note: string;

  @ManyToOne(() => Customer, (customer) => customer.orders)
  @JoinColumn({ name: 'customer_id' })
  customer: Customer;

  @OneToMany(() => OrderItem, (item) => item.order, { cascade: true })
  items: OrderItem[];
}

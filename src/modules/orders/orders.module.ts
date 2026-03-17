import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Order } from './entities/order.entity';
import { OrderItem } from './entities/order-item.entity';
import { OrderItemOption } from './entities/order-item-option.entity';
import { OrderService } from './services/order.service';
import { OrderController } from './controllers/order.controller';
import { Customer } from '../customers/entities/customer.entity';
import { ProductsModule } from '../products/products.module';

@Module({
  imports: [
    TypeOrmModule.forFeature([Order, OrderItem, OrderItemOption, Customer]),
    ProductsModule,
  ],
  controllers: [OrderController],
  providers: [OrderService],
})
export class OrdersModule {}

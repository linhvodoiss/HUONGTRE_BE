import { Module } from '@nestjs/common';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { TypeOrmModule } from '@nestjs/typeorm';
import { OrdersModule } from './modules/orders/orders.module';
import { ProductsModule } from './modules/products/products.module';
import { AuthModule } from './modules/auth/auth.module';
import { UsersModule } from './modules/users/users.module';
import { CustomersModule } from './modules/customers/customers.module';
import { BranchesModule } from './modules/branches/branches.module';

@Module({
  imports: [
    // Thay thế cấu hình application.properties của Spring
    ConfigModule.forRoot({
      isGlobal: true,
    }),
    TypeOrmModule.forRootAsync({
      imports: [ConfigModule],
      useFactory: (configService: ConfigService) => ({
        type: 'mysql',
        host: configService.get<string>('DB_HOST'),
        port: configService.get<number>('DB_PORT'),
        username: configService.get<string>('DB_USERNAME'),
        password: configService.get<string>('DB_PASSWORD'),
        database: configService.get<string>('DB_NAME'),
        entities: [__dirname + '/**/*.entity{.ts,.js}'],
        autoLoadEntities: true,
        synchronize: false, // Để chạy được với DB cũ, hãy dùng false. Khi thêm bảng mới hãy bật lại true.
        logging: true,
      }),
      inject: [ConfigService],
    }),
    AuthModule,
    UsersModule,
    ProductsModule,
    OrdersModule,
    CustomersModule,
    BranchesModule,
  ],
})
export class AppModule {}

import { Injectable } from '@nestjs/common';
import { Seeder, DataFactory } from 'nestjs-seeder';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import * as bcrypt from 'bcrypt';
import { User } from '../../modules/users/entities/user.entity';
import { Role, UserStatus } from '../../modules/users/enums/user.enum';

@Injectable()
export class UsersSeeder implements Seeder {
  constructor(
    @InjectRepository(User)
    private readonly userRepository: Repository<User>,
  ) {}

  async seed(): Promise<any> {
    const hashedPassword = await bcrypt.hash('admin123', 10);
    
    const users = [
      {
        username: 'admin',
        email: 'admin@huongtre.com',
        password: hashedPassword,
        firstName: 'Admin',
        lastName: 'System',
        phoneNumber: '0900000001',
        role: Role.ADMIN,
        status: UserStatus.ACTIVE,
      },
      {
        username: 'staff1',
        email: 'staff1@huongtre.com',
        password: hashedPassword,
        firstName: 'Nguyễn',
        lastName: 'Nhân Viên',
        phoneNumber: '0900000002',
        role: Role.STAFF,
        status: UserStatus.ACTIVE,
      }
    ];

    return this.userRepository.save(users);
  }

  async drop(): Promise<any> {
    return this.userRepository.delete({});
  }
}

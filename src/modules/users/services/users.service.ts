import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import * as bcrypt from 'bcrypt';
import { User } from '../entities/user.entity';

@Injectable()
export class UsersService {
  constructor(
    @InjectRepository(User)
    private usersRepository: Repository<User>,
  ) {}

  async findAll(page: number = 1, limit: number = 10) {
    const [data, total] = await this.usersRepository.findAndCount({
      where: { isDeleted: false },
      order: { id: 'DESC' },
      skip: (page - 1) * limit,
      take: limit,
    });

    return {
      data,
      meta: {
        total,
        page,
        limit,
        lastPage: Math.ceil(total / limit),
      },
    };
  }

  async findOneByUsername(username: string): Promise<User | null> {
    return this.usersRepository.findOne({
      where: { username },
      select: [
        'id',
        'username',
        'password',
        'email',
        'role',
        'firstName',
        'lastName',
      ],
    });
  }

  async findOneByEmail(email: string): Promise<User | null> {
    return this.usersRepository.findOne({ where: { email } });
  }

  async findOneById(id: number): Promise<User | null> {
    return this.usersRepository.findOne({ where: { id } });
  }

  async create(data: any) {
    if (data.password) {
      data.password = await bcrypt.hash(data.password, 10);
    }
    const user = this.usersRepository.create(data);
    return this.usersRepository.save(user);
  }

  async update(id: number, data: any) {
    const user = await this.findOneById(id);
    if (!user) throw new NotFoundException('User not found');

    if (data.password) {
      data.password = await bcrypt.hash(data.password, 10);
    }

    Object.assign(user, data);
    return this.usersRepository.save(user);
  }

  async remove(id: number) {
    const user = await this.findOneById(id);
    if (!user) throw new NotFoundException('User not found');
    user.isDeleted = true;
    return this.usersRepository.save(user);
  }
}

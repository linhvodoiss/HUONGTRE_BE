import { Test, TestingModule } from '@nestjs/testing';
import { getRepositoryToken } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { UsersService } from './users.service';
import { User } from '../entities/user.entity';
import { NotFoundException } from '@nestjs/common';
import * as bcrypt from 'bcrypt';

jest.mock('bcrypt');

const mockUser = {
  id: 1,
  username: 'admin',
  password: 'hashed_password',
  email: 'admin@example.com',
  isDeleted: false,
};

const mockUsersRepository = () => ({
  findAndCount: jest.fn(),
  findOne: jest.fn(),
  create: jest.fn(),
  save: jest.fn(),
});

describe('UsersService', () => {
  let service: UsersService;
  let repository: Repository<User>;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [
        UsersService,
        {
          provide: getRepositoryToken(User),
          useFactory: mockUsersRepository,
        },
      ],
    }).compile();

    service = module.get<UsersService>(UsersService);
    repository = module.get<Repository<User>>(getRepositoryToken(User));
  });

  describe('create', () => {
    it('should hash password and save user', async () => {
      const dto = { username: 'test', password: 'password123' };
      (bcrypt.hash as jest.Mock).mockResolvedValue('hashed_pass');
      (repository.create as jest.Mock).mockReturnValue(mockUser);
      (repository.save as jest.Mock).mockResolvedValue(mockUser);

      const result = await service.create(dto);

      expect(bcrypt.hash).toHaveBeenCalledWith('password123', 10);
      expect(repository.save).toHaveBeenCalled();
      expect(result).toEqual(mockUser);
    });
  });

  describe('update', () => {
    it('should throw NotFoundException if user not found', async () => {
      (repository.findOne as jest.Mock).mockResolvedValue(null);
      await expect(service.update(999, {})).rejects.toThrow(NotFoundException);
    });

    it('should update user and hash password if provided', async () => {
      (repository.findOne as jest.Mock).mockResolvedValue(mockUser);
      (bcrypt.hash as jest.Mock).mockResolvedValue('new_hashed_pass');
      (repository.save as jest.Mock).mockResolvedValue({
        ...mockUser,
        email: 'new@email.com',
      });

      const result = await service.update(1, {
        password: 'new_password',
        email: 'new@email.com',
      });

      expect(bcrypt.hash).toHaveBeenCalled();
      expect(repository.save).toHaveBeenCalled();
      expect(result.email).toBe('new@email.com');
    });
  });
});

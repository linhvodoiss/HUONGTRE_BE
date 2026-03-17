import { Test, TestingModule } from '@nestjs/testing';
import { getRepositoryToken } from '@nestjs/typeorm';
import { Repository, DataSource } from 'typeorm';
import { OrderService } from './order.service';
import { Order, OrderStatus } from '../entities/order.entity';
import { Customer } from '../../customers/entities/customer.entity';
import { Product } from '../../products/entities/product.entity';
import { Option } from '../../products/entities/option.entity';
import { NotFoundException } from '@nestjs/common';

const mockOrder = {
  id: 1,
  totalAmount: 100,
  status: OrderStatus.PENDING,
};

const mockDataSource = () => ({
  createQueryRunner: jest.fn().mockReturnValue({
    connect: jest.fn(),
    startTransaction: jest.fn(),
    commitTransaction: jest.fn(),
    rollbackTransaction: jest.fn(),
    release: jest.fn(),
    manager: {
      findOneBy: jest.fn(),
      create: jest.fn(),
      save: jest.fn(),
    },
  }),
});

const mockRepository = () => ({
  findAndCount: jest.fn(),
  findOne: jest.fn(),
  save: jest.fn(),
});

describe('OrderService', () => {
  let service: OrderService;
  let orderRepo: Repository<Order>;
  let dataSource: DataSource;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [
        OrderService,
        { provide: DataSource, useFactory: mockDataSource },
        { provide: getRepositoryToken(Order), useFactory: mockRepository },
        { provide: getRepositoryToken(Customer), useFactory: mockRepository },
        { provide: getRepositoryToken(Product), useFactory: mockRepository },
        { provide: getRepositoryToken(Option), useFactory: mockRepository },
      ],
    }).compile();

    service = module.get<OrderService>(OrderService);
    orderRepo = module.get<Repository<Order>>(getRepositoryToken(Order));
    dataSource = module.get<DataSource>(DataSource);
  });

  it('should be defined', () => {
    expect(service).toBeDefined();
  });

  describe('updateStatus', () => {
    it('should update order status', async () => {
      (orderRepo.findOne as jest.Mock).mockResolvedValue(mockOrder);
      (orderRepo.save as jest.Mock).mockResolvedValue({
        ...mockOrder,
        status: OrderStatus.COMPLETED,
      });

      const getByIdSpy = jest
        .spyOn(service, 'getById')
        .mockResolvedValue({ ...mockOrder, status: OrderStatus.COMPLETED });

      const result = await service.updateStatus(1, OrderStatus.COMPLETED);

      expect(orderRepo.save).toHaveBeenCalled();
      expect(result.status).toBe(OrderStatus.COMPLETED);

      getByIdSpy.mockRestore();
    });

    it('should throw error if order not found', async () => {
      (orderRepo.findOne as jest.Mock).mockResolvedValue(null);
      await expect(
        service.updateStatus(999, OrderStatus.COMPLETED),
      ).rejects.toThrow(NotFoundException);
    });
  });

  describe('getById', () => {
    it('should fetch order by id', async () => {
      (orderRepo.findOne as jest.Mock).mockResolvedValue(mockOrder);
      const result = await service.getById(1);
      expect(result.id).toBe(1);
    });
  });
});

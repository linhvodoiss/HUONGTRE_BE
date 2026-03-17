import { Test, TestingModule } from '@nestjs/testing';
import { getRepositoryToken } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { BranchesService } from './branches.service';
import { Branch } from '../entities/branch.entity';
import { NotFoundException } from '@nestjs/common';

const mockBranch = {
  id: 1,
  name: 'Chi nhánh 1',
  address: 'Địa chỉ 1',
  phone: '0123456789',
  isDeleted: false,
};

const mockBranchRepository = () => ({
  findAndCount: jest.fn(),
  findOne: jest.fn(),
  create: jest.fn(),
  save: jest.fn(),
  update: jest.fn(),
});

describe('BranchesService', () => {
  let service: BranchesService;
  let repository: Repository<Branch>;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [
        BranchesService,
        {
          provide: getRepositoryToken(Branch),
          useFactory: mockBranchRepository,
        },
      ],
    }).compile();

    service = module.get<BranchesService>(BranchesService);
    repository = module.get<Repository<Branch>>(getRepositoryToken(Branch));
  });

  it('should be defined', () => {
    expect(service).toBeDefined();
  });

  describe('findAll', () => {
    it('should return paginated branches', async () => {
      const branches = [mockBranch];
      const total = 1;
      (repository.findAndCount as jest.Mock).mockResolvedValue([
        branches,
        total,
      ]);

      const result = await service.findAll(1, 10);

      expect(repository.findAndCount).toHaveBeenCalled();
      expect(result.data).toEqual(branches);
      expect(result.meta.total).toEqual(total);
    });
  });

  describe('findOne', () => {
    it('should return a branch if found', async () => {
      (repository.findOne as jest.Mock).mockResolvedValue(mockBranch);

      const result = await service.findOne(1);

      expect(repository.findOne).toHaveBeenCalled();
      expect(result).toEqual(mockBranch);
    });

    it('should throw NotFoundException if branch not found', async () => {
      (repository.findOne as jest.Mock).mockResolvedValue(null);

      await expect(service.findOne(999)).rejects.toThrow(NotFoundException);
    });
  });

  describe('create', () => {
    it('should create and save a branch', async () => {
      const dto = { name: 'New Branch', address: 'Address', phone: '090' };
      (repository.create as jest.Mock).mockReturnValue(mockBranch);
      (repository.save as jest.Mock).mockResolvedValue(mockBranch);

      const result = await service.create(dto as any);

      expect(repository.create).toHaveBeenCalledWith(dto);
      expect(repository.save).toHaveBeenCalled();
      expect(result).toEqual(mockBranch);
    });
  });

  describe('remove', () => {
    it('should mark branch as deleted', async () => {
      (repository.findOne as jest.Mock).mockResolvedValue(mockBranch);
      (repository.save as jest.Mock).mockResolvedValue({
        ...mockBranch,
        isDeleted: true,
      });

      const result = await service.remove(1);

      expect(repository.save).toHaveBeenCalled();
      expect(result.isDeleted).toBe(true);
    });
  });
});

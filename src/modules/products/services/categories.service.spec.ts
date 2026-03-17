import { Test, TestingModule } from '@nestjs/testing';
import { getRepositoryToken } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { CategoriesService } from './categories.service';
import { Category } from '../entities/category.entity';

const mockCategory = {
  id: 1,
  name: 'Cà phê',
  isDeleted: false,
};

const mockCategoryRepository = () => ({
  findAndCount: jest.fn(),
  findOne: jest.fn(),
  create: jest.fn(),
  save: jest.fn(),
  createQueryBuilder: jest.fn(() => ({
    leftJoinAndSelect: jest.fn().mockReturnThis(),
    where: jest.fn().mockReturnThis(),
    andWhere: jest.fn().mockReturnThis(),
    orderBy: jest.fn().mockReturnThis(),
    addOrderBy: jest.fn().mockReturnThis(),
    getMany: jest.fn().mockResolvedValue([]),
  })),
});

describe('CategoriesService', () => {
  let service: CategoriesService;
  let repository: Repository<Category>;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [
        CategoriesService,
        {
          provide: getRepositoryToken(Category),
          useFactory: mockCategoryRepository,
        },
      ],
    }).compile();

    service = module.get<CategoriesService>(CategoriesService);
    repository = module.get<Repository<Category>>(getRepositoryToken(Category));
  });

  describe('findAll', () => {
    it('should return paginated categories', async () => {
      const categories = [mockCategory];
      (repository.findAndCount as jest.Mock).mockResolvedValue([categories, 1]);

      const result = await service.findAll(1, 10);

      expect(result.data).toEqual(categories);
    });
  });

  describe('getFullMenu', () => {
    it('should return menu list', async () => {
      const result = await service.getFullMenu();
      expect(result).toBeDefined();
      expect(Array.isArray(result)).toBe(true);
    });
  });

  describe('create', () => {
    it('should create and save a category', async () => {
      const dto = { name: 'Mới' };
      (repository.create as jest.Mock).mockReturnValue(mockCategory);
      (repository.save as jest.Mock).mockResolvedValue(mockCategory);

      const result = (await service.create(dto)) as any;

      expect(repository.create).toHaveBeenCalledWith(dto);
      expect(result.name).toBe('Cà phê');
    });
  });
});

import { Test, TestingModule } from '@nestjs/testing';
import { getRepositoryToken } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { ProductsService } from './products.service';
import { Product } from '../entities/product.entity';
import { ProductOptionGroup } from '../entities/product-option-group.entity';
import { NotFoundException } from '@nestjs/common';

const mockProduct = {
  id: 1,
  name: 'Sản phẩm 1',
  price: 50000,
  isDeleted: false,
};

const mockProductRepository = () => ({
  findAndCount: jest.fn(),
  findOne: jest.fn(),
  create: jest.fn(),
  save: jest.fn(),
  update: jest.fn(),
});

const mockProductOptionGroupRepository = () => ({
  save: jest.fn(),
  delete: jest.fn(),
});

describe('ProductsService', () => {
  let service: ProductsService;
  let productRepo: Repository<Product>;
  let pogRepo: Repository<ProductOptionGroup>;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [
        ProductsService,
        {
          provide: getRepositoryToken(Product),
          useFactory: mockProductRepository,
        },
        {
          provide: getRepositoryToken(ProductOptionGroup),
          useFactory: mockProductOptionGroupRepository,
        },
      ],
    }).compile();

    service = module.get<ProductsService>(ProductsService);
    productRepo = module.get<Repository<Product>>(getRepositoryToken(Product));
    pogRepo = module.get<Repository<ProductOptionGroup>>(
      getRepositoryToken(ProductOptionGroup),
    );
  });

  it('should be defined', () => {
    expect(service).toBeDefined();
  });

  describe('findAll', () => {
    it('should return paginated products', async () => {
      const products = [mockProduct];
      const total = 1;
      (productRepo.findAndCount as jest.Mock).mockResolvedValue([
        products,
        total,
      ]);

      const result = await service.findAll(1, 10);

      expect(productRepo.findAndCount).toHaveBeenCalled();
      expect(result.data).toEqual(products);
    });
  });

  describe('create', () => {
    it('should create a product with option groups', async () => {
      const dto = { name: 'New', price: 10, optionGroupIds: [1, 2] };
      (productRepo.create as jest.Mock).mockReturnValue(mockProduct);
      (productRepo.save as jest.Mock).mockResolvedValue(mockProduct);

      const findOneSpy = jest
        .spyOn(service, 'findOne')
        .mockResolvedValue(mockProduct as any);

      const result = await service.create(dto);

      expect(productRepo.create).toHaveBeenCalled();
      expect(pogRepo.save).toHaveBeenCalled();
      expect(result).toEqual(mockProduct);

      findOneSpy.mockRestore();
    });
  });

  describe('remove', () => {
    it('should soft delete product', async () => {
      jest.spyOn(service, 'findOne').mockResolvedValue(mockProduct as any);
      (productRepo.save as jest.Mock).mockResolvedValue({
        ...mockProduct,
        isDeleted: true,
      });

      const result = await service.remove(1);

      expect(productRepo.save).toHaveBeenCalled();
      expect(result.isDeleted).toBe(true);
    });
  });
});

import { Test, TestingModule } from '@nestjs/testing';
import { getRepositoryToken } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { OptionsService } from './options.service';
import { OptionGroup } from '../entities/option-group.entity';
import { Option } from '../entities/option.entity';
import { NotFoundException } from '@nestjs/common';

const mockOptionGroup = {
  id: 1,
  name: 'Size',
  isDeleted: false,
};

const mockOption = {
  id: 1,
  name: 'Lớn',
  price: 5000,
  isDeleted: false,
};

const mockRepo = () => ({
  findAndCount: jest.fn(),
  findOne: jest.fn(),
  create: jest.fn(),
  save: jest.fn(),
});

describe('OptionsService', () => {
  let service: OptionsService;
  let groupRepo: Repository<OptionGroup>;
  let optionRepo: Repository<Option>;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [
        OptionsService,
        {
          provide: getRepositoryToken(OptionGroup),
          useFactory: mockRepo,
        },
        {
          provide: getRepositoryToken(Option),
          useFactory: mockRepo,
        },
      ],
    }).compile();

    service = module.get<OptionsService>(OptionsService);
    groupRepo = module.get<Repository<OptionGroup>>(
      getRepositoryToken(OptionGroup),
    );
    optionRepo = module.get<Repository<Option>>(getRepositoryToken(Option));
  });

  describe('findAllGroups', () => {
    it('should return paginated groups', async () => {
      (groupRepo.findAndCount as jest.Mock).mockResolvedValue([
        [mockOptionGroup],
        1,
      ]);
      const result = await service.findAllGroups(1, 10);
      expect(result.data).toHaveLength(1);
    });
  });

  describe('createOption', () => {
    it('should create option for a group', async () => {
      (groupRepo.findOne as jest.Mock).mockResolvedValue(mockOptionGroup);
      (optionRepo.create as jest.Mock).mockReturnValue(mockOption);
      (optionRepo.save as jest.Mock).mockResolvedValue(mockOption);

      const result = (await service.createOption(1, { name: 'Lớn' })) as any;

      expect(optionRepo.create).toHaveBeenCalled();
      expect(result.name).toBe('Lớn');
    });
  });

  describe('updateOption', () => {
    it('should throw error if option not found', async () => {
      (optionRepo.findOne as jest.Mock).mockResolvedValue(null);
      await expect(service.updateOption(1, {})).rejects.toThrow(
        NotFoundException,
      );
    });
  });
});

import { Injectable } from '@nestjs/common';
import { Seeder } from 'nestjs-seeder';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Branch } from '../../modules/branches/entities/branch.entity';

@Injectable()
export class BranchesSeeder implements Seeder {
  constructor(
    @InjectRepository(Branch)
    private readonly branchRepository: Repository<Branch>,
  ) {}

  async seed(): Promise<any> {
    const branches = [
      {
        name: 'Hương Tre - Quận 1',
        address: '123 Nguyễn Huệ, P. Bến Nghé, Q.1',
        phone: '0283800111',
      },
      {
        name: 'Hương Tre - Bình Thạnh',
        address: '45 Phan Xích Long, Bình Thạnh',
        phone: '0283800222',
      }
    ];

    return this.branchRepository.save(branches);
  }

  async drop(): Promise<any> {
    return this.branchRepository.delete({});
  }
}

import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Branch } from './entities/branch.entity';
import { BranchesService } from './services/branches.service';
import { BranchesController } from './controllers/branches.controller';

@Module({
  imports: [TypeOrmModule.forFeature([Branch])],
  controllers: [BranchesController],
  providers: [BranchesService],
  exports: [BranchesService],
})
export class BranchesModule {}

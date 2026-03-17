import {
  CreateDateColumn,
  UpdateDateColumn,
  PrimaryGeneratedColumn,
  Column,
} from 'typeorm';

export abstract class AbstractEntity {
  @PrimaryGeneratedColumn({ type: 'bigint' })
  id: number;

  @CreateDateColumn({ name: 'created_at' })
  createdAt: Date;

  @UpdateDateColumn({ name: 'updated_at' })
  updatedAt: Date;

  @Column({
    name: 'is_deleted',
    type: 'bit',
    width: 1,
    default: () => "'\\0'",
    transformer: {
      from: (v: Buffer) => v !== null && v.length > 0 && v[0] === 1,
      to: (v: boolean) => (v ? 1 : 0),
    },
  })
  isDeleted: boolean;
}

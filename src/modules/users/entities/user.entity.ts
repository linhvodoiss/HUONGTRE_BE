import { Entity, Column } from 'typeorm';
import { AbstractEntity } from '../../../common/entities/abstract.entity';
import { Role, UserStatus } from '../enums/user.enum';

@Entity({ name: 'User' })
export class User extends AbstractEntity {
  @Column({ name: 'username', length: 50, unique: true })
  username: string;

  @Column({ length: 50, unique: true })
  email: string;

  @Column({ length: 800, select: false }) // select: false để không tự động load password
  password: string;

  @Column({ name: 'firstName', length: 50 })
  firstName: string;

  @Column({ name: 'lastName', length: 50 })
  lastName: string;

  @Column({ name: 'phoneNumber', length: 50 })
  phoneNumber: string;

  @Column({
    type: 'enum',
    enum: Role,
    default: Role.CUSTOMER,
  })
  role: Role;

  @Column({
    type: 'int',
    default: UserStatus.NOT_ACTIVE,
  })
  status: UserStatus;

  @Column({ 
    name: 'isActive', 
    type: 'tinyint', 
    width: 1, 
    default: 1 
  })
  isActive: boolean;

  @Column({ name: 'avatarUrl', nullable: true })
  avatarUrl: string;

  // TypeScript getter thay thế cho @Formula của Hibernate
  get fullName(): string {
    return `${this.firstName} ${this.lastName}`;
  }
}

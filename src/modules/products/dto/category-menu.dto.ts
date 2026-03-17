import { IsNotEmpty, IsOptional, IsString, IsBoolean, IsArray } from 'class-validator';

export class OptionMenuDto {
  id: number;
  name: string;
  price: number;
  description?: string;
  isActive: boolean;
}

export class OptionGroupMenuDto {
  id: number;
  name: string;
  selectType: string;
  required: boolean;
  minSelect: number;
  maxSelect: number;
  isActive: boolean;
  options: OptionMenuDto[];
}

export class ProductMenuDto {
  id: number;
  name: string;
  price: number;
  imageUrl?: string;
  description?: string;
  isActive: boolean;
  productOptionGroups: any[]; // Bạn có thể định nghĩa interface sâu hơn nếu cần
}

export class CategoryMenuDto {
  id: number;
  name: string;
  description?: string;
  imageUrl?: string;
  products: ProductMenuDto[];
}

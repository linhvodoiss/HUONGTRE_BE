import {
  IsString,
  IsNotEmpty,
  IsNumber,
  IsOptional,
  IsArray,
  ValidateNested,
  Min,
} from 'class-validator';
import { Type } from 'class-transformer';

export class OrderItemOptionRequest {
  @IsNumber()
  @IsNotEmpty()
  optionId: number;
}

export class OrderItemRequest {
  @IsNumber()
  @IsNotEmpty()
  productId: number;

  @IsNumber()
  @Min(1)
  quantity: number;

  @IsString()
  @IsOptional()
  note?: string;

  @IsArray()
  @IsOptional()
  optionIds?: number[];
}

export class OrderCreateRequest {
  @IsString()
  @IsNotEmpty()
  receiverName: string;

  @IsString()
  @IsNotEmpty()
  receiverPhone: string;

  @IsString()
  @IsNotEmpty()
  deliveryAddress: string;

  @IsString()
  @IsOptional()
  note?: string;

  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => OrderItemRequest)
  items: OrderItemRequest[];
}

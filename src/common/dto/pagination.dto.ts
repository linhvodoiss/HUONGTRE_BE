export class PaginationDto {
  page: number;
  limit: number;
  total: number;
  lastPage: number;
}

export class PaginatedResponseDto<T> {
  data: T[];
  meta: PaginationDto;
}

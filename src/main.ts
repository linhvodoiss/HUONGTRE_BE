import { NestFactory, Reflector } from '@nestjs/core';
import { AppModule } from './app.module';
import { ValidationPipe } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { TransformInterceptor } from './common/interceptors/transform.interceptor';
import { DocumentBuilder, SwaggerModule } from '@nestjs/swagger';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);

  app.setGlobalPrefix('api');

  // Cấu hình Swagger
  const config = new DocumentBuilder()
    .setTitle('Hương Tre API')
    .setDescription('Tài liệu API cho hệ thống quản lý Hương Tre')
    .setVersion('1.0')
    .addBearerAuth()
    .build();
  const document = SwaggerModule.createDocument(app, config);
  SwaggerModule.setup('swagger', app, document);
  
  // Kích hoạt chuẩn chung Response
  app.useGlobalInterceptors(new TransformInterceptor(app.get(Reflector)));

  const configService = app.get(ConfigService);
  const port = configService.get<number>('PORT') || 8081;

  // Tương đương với việc bật Validation trong Spring Boot
  app.useGlobalPipes(
    new ValidationPipe({
      whitelist: true,
      forbidNonWhitelisted: true,
      transform: true,
    }),
  );

  // Kích hoạt CORS
  app.enableCors({
    origin: configService.get<string>('FRONTEND_URL'),
    credentials: true,
  });

  await app.listen(port);
  console.log(`🚀 Server is running on: http://localhost:${port}`);
}
bootstrap();

---
description: Cách khởi chạy dự án Spring Boot HUONGTRE_BE
---

## Yêu cầu môi trường

- Java JDK 17 tại: `C:\Program Files\Java\jdk-17`
- Maven 3.9.13 tại: `C:\Maven\apache-maven-3.9.13`
- MySQL 8.x chạy local, password root: `12345`, DB: `HuongTreSystem`

## Các bước chạy dự án

// turbo
1. Kiểm tra port 8081 có đang bị chiếm không:
```powershell
netstat -ano | findstr :8081
```

2. Nếu port đang bị chiếm, tìm PID và kill tiến trình:
```powershell
# Thay <PID> bằng số PID tìm được ở bước 1
taskkill /PID <PID> /F
```

// turbo
3. Chạy dự án (bỏ qua test vì file test đang bị lỗi compile):
```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"; & "C:\Maven\apache-maven-3.9.13\bin\mvn.cmd" "spring-boot:run" "-Dmaven.test.skip=true"
```

4. Kiểm tra dự án chạy thành công khi thấy log:
```
Started ShopApplication in X.XXX seconds
Tomcat started on port(s): 8081 (http)
```

Truy cập Swagger UI: http://localhost:8081/swagger-ui/index.html

# 🍵 HUONGTRE_BE — Project Context Prompt

## 📌 Tổng quan dự án
Đây là **Backend** của hệ thống quản lý **Quán Nước Hương Tre**.
- **Ngôn ngữ:** Java 17
- **Framework:** Spring Boot 2.7.18
- **Build tool:** Maven 3.9.13
- **Database:** MySQL 8.x — tên DB: `HuongTreSystem`
- **Port:** `8081`
- **Base package:** `com.fpt`
- **Main class:** `com.fpt.ShopApplication`

---

## 🏗️ Kiến trúc dự án

```
src/main/java/com/fpt/
├── ShopApplication.java         # Entry point
├── annotation/                  # Custom annotations (vd: @CurrentUserId)
├── authentication/              # JWT filter, token handler
├── config/                      # Spring config (Security, CORS, Swagger, v.v.)
├── constant/                    # Hằng số toàn cục
├── controller/                  # REST API controllers
├── dto/                         # Data Transfer Objects
├── entity/                      # JPA Entities (ánh xạ DB)
├── enums/                       # Enum types
├── event/                       # Application events
├── exception/                   # Global exception handling
├── form/                        # Request form/body objects
├── init/                        # Data khởi tạo ban đầu
├── payload/                     # Response payload wrappers
├── repository/                  # Spring Data JPA repositories
├── resolver/                    # Argument resolvers
├── security/                    # Security utilities
├── service/
│   ├── interfaces/              # Interface định nghĩa service
│   └── implementations/        # Implement logic nghiệp vụ
├── specification/               # JPA Specifications (filter/search)
├── utils/                       # Tiện ích dùng chung
└── websocket/                   # WebSocket config & handler
```

---

## 🗄️ Các Entity chính (Domain Model)

| Entity | Mô tả |
|---|---|
| `User` | Tài khoản hệ thống (admin, nhân viên) |
| `Customer` | Khách hàng của quán |
| `Product` | Sản phẩm (đồ uống, món ăn) |
| `Category` | Danh mục sản phẩm |
| `Option` | Tuỳ chọn thêm (size, topping...) |
| `OptionGroup` | Nhóm tuỳ chọn |
| `ProductOptionGroup` | Liên kết Product ↔ OptionGroup |
| `Order` | Đơn hàng của khách |
| `OrderItem` | Chi tiết sản phẩm trong đơn |
| `OrderItemOption` | Tuỳ chọn được chọn trong đơn |
| `Branch` | Chi nhánh quán |
| `BranchProduct` | Sản phẩm theo từng chi nhánh |
| `PaymentOrder` | Thông tin thanh toán |
| `SubscriptionPackage` | Gói đăng ký dịch vụ |
| `License` | Giấy phép / bản quyền sử dụng |
| `Role` | Phân quyền người dùng |

---

## 🔌 Các Controller & API

| Controller | Chức năng |
|---|---|
| `UserController` | Quản lý tài khoản người dùng |
| `CustomerController` | Quản lý khách hàng |
| `ProductController` | CRUD sản phẩm |
| `CategoryController` | CRUD danh mục |
| `OptionController` | Quản lý tuỳ chọn |
| `OrderController` | Xử lý đơn hàng |
| `BranchController` | Quản lý chi nhánh |
| `PaymentOrderController` | Xử lý thanh toán |
| `PayOSController` | Tích hợp cổng thanh toán PayOS |
| `SubscriptionPackageController` | Gói đăng ký |
| `LicenseController` | Quản lý license |
| `FileController` | Upload/download file |
| `AdminController` | Chức năng dành riêng admin |

---

## 🔐 Security & Auth

- **JWT Authentication** — filter: `JWTAuthenticationFilter`, `JWTOtpAuthenticationFilter`
- **JWT Authorization** — filter: `JWTAuthorizationFilter`
- **OTP** — hỗ trợ xác thực 2 bước qua email
- **Spring Security** đã được cấu hình với CORS và custom filter chain

---

## ⚙️ Các tích hợp bên ngoài

| Tích hợp | Chi tiết |
|---|---|
| **PayOS** | Cổng thanh toán, webhook URL cần ngrok khi dev local |
| **Gmail SMTP** | Gửi email OTP, thông báo — `tangbang250820@gmail.com` |
| **WebSocket** | Real-time (SimpleBroker) |
| **Swagger** | `http://localhost:8081/swagger-ui/index.html` |

---

## 🚀 Cách chạy dự án

```powershell
# Yêu cầu: Java 17 tại C:\Program Files\Java\jdk-17
#           Maven 3.9.13 tại C:\Maven\apache-maven-3.9.13
#           MySQL chạy local với password root: 12345

$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
& "C:\Maven\apache-maven-3.9.13\bin\mvn.cmd" "spring-boot:run" "-Dmaven.test.skip=true"
```

> **Lưu ý:** Nếu lỗi `Port 8081 already in use`:
> ```powershell
> netstat -ano | findstr :8081   # Tìm PID
> taskkill /PID <PID> /F         # Dừng tiến trình
> ```

---

## 📐 Coding Conventions

- **Lombok** được dùng xuyên suốt: `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`
- **Soft delete** pattern: các entity dùng `isDeleted = false` + `@SQLDelete` + `@Where`
- **Service pattern**: Interface (`IXxxService`) + Implementation (`XxxService`)
- **Timestamp**: `@CreationTimestamp` / `@UpdateTimestamp` tự động
- **Naming strategy**: `PhysicalNamingStrategyStandardImpl` (tên cột = tên field)
- **Pagination**: Spring Data Web, 1-indexed, default 10 items/page

---

## ⚠️ Lưu ý quan trọng

1. **File test bị lỗi compile** — luôn thêm `-Dmaven.test.skip=true` khi chạy
2. **PayOS webhook** sẽ báo lỗi 404 khi dev local (cần ngrok) — không ảnh hưởng các API khác
3. **WebSocket** đang dùng `SimpleBroker`, chưa dùng RabbitMQ hay Redis
4. **`spring-boot-starter-websocket`** bị khai báo 2 lần trong `pom.xml` (warning nhỏ, không ảnh hưởng)

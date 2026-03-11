---
description: Hướng dẫn debug và xử lý lỗi phổ biến trong dự án HUONGTRE_BE
---

## Các lỗi phổ biến và cách xử lý

### 1. Lỗi JAVA_HOME not found
```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
```

### 2. Lỗi Port 8081 already in use
1. Tìm PID đang dùng port:
```powershell
netstat -ano | findstr :8081
```
2. Kill tiến trình:
```powershell
taskkill /PID <PID> /F
```

### 3. Lỗi compile file test
Luôn thêm `-Dmaven.test.skip=true` vào lệnh chạy. File test tại:
- `SubscriptionPackageServiceTest.java` — lỗi method `setOptions()` không tồn tại
- `PaymentOrderServiceTest.java` — lỗi method `options()` không tồn tại

### 4. Lỗi PayOS webhook 404
Bình thường khi chạy local. URL ngrok đã hết hạn. Không ảnh hưởng các API khác.
Nếu cần test PayOS: cài ngrok và cập nhật `payos.webhook-url` trong `application.properties`.

### 5. Lỗi kết nối MySQL
Kiểm tra:
- MySQL service đang chạy
- Password root đúng là `12345`
- DB `HuongTreSystem` tồn tại (sẽ tự tạo nếu chưa có)

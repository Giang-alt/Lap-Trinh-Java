# 📦 EV MARKETPLACE - PHẦN CODE CỦA GIANG

## 👤 Thành viên: Giang

## 🎯 Nhiệm vụ

Bạn cần implement **4 API endpoints**:

### 1. Authentication APIs (3 APIs)
- **POST** `/api/auth/login` - Đăng nhập
- **POST** `/api/auth/register/consumer` - Đăng ký Consumer
- **POST** `/api/auth/register/provider` - Đăng ký Provider

### 2. Data Package API (1 API)
- **GET** `/api/data-packages` - Lấy tất cả data packages

---

## 📁 Cấu trúc dự án

```
EV_marketplace/
├── src/main/java/com/evmarketplace/
│   ├── controller/
│   │   ├── AuthController.java          ✅ ĐÃ CÓ (Authentication APIs)
│   │   └── DataPackageController.java   ✅ ĐÃ CÓ (Get All API)
│   ├── dto/
│   │   ├── LoginRequest.java            ✅ ĐÃ CÓ
│   │   ├── RegisterRequest.java         ✅ ĐÃ CÓ
│   │   └── LoginResponse.java           ✅ ĐÃ CÓ
│   ├── entity/
│   │   ├── User.java                    ✅ ĐÃ CÓ
│   │   ├── DataConsumer.java            ✅ ĐÃ CÓ
│   │   ├── DataProvider.java            ✅ ĐÃ CÓ
│   │   └── DataPackage.java             ✅ ĐÃ CÓ
│   ├── repository/
│   │   ├── UserRepository.java          ✅ ĐÃ CÓ
│   │   ├── DataConsumerRepository.java  ✅ ĐÃ CÓ
│   │   ├── DataProviderRepository.java  ✅ ĐÃ CÓ
│   │   └── DataPackageRepository.java   ✅ ĐÃ CÓ
│   ├── service/
│   │   ├── UserService.java             ✅ ĐÃ CÓ
│   │   ├── CustomUserDetailsService.java ✅ ĐÃ CÓ
│   │   └── DataPackageService.java      ✅ ĐÃ CÓ
│   ├── security/
│   │   ├── JwtUtil.java                 ✅ ĐÃ CÓ
│   │   ├── JwtAuthenticationFilter.java ✅ ĐÃ CÓ
│   │   └── JwtAuthenticationEntryPoint.java ✅ ĐÃ CÓ
│   ├── config/
│   │   └── SecurityConfig.java          ✅ ĐÃ CÓ
│   └── EvDataMarketplaceApplication.java ✅ ĐÃ CÓ
├── frontend/                            ✅ ĐÃ CÓ (React app)
├── database/
│   └── schema.sql                       ✅ ĐÃ CÓ
├── pom.xml                              ✅ ĐÃ CÓ
└── README_GIANG.md                      📄 File này
```

---

## ✅ Trạng thái code

### ✅ Code đã sẵn sàng 100%

Tất cả code đã được implement đầy đủ:
- ✅ AuthController.java - Có 3 methods: login, registerConsumer, registerProvider
- ✅ DataPackageController.java - Có 1 method: getAllDataPackages
- ✅ Tất cả DTOs, Entities, Repositories, Services đã có
- ✅ Security configuration đã setup
- ✅ JWT authentication đã hoạt động
- ✅ Database schema đã có

### 🎯 Nhiệm vụ của bạn

1. **Đọc hiểu code** - Hiểu cách hoạt động của từng API
2. **Setup project** - Cài đặt database và chạy project
3. **Test APIs** - Test 4 APIs với Postman/curl
4. **Commit lên GitHub** - Push code lên repository cá nhân
5. **Chuẩn bị demo** - Sẵn sàng giải thích code cho thầy

---

## 🚀 Hướng dẫn Setup

### Bước 1: Cài đặt MySQL

1. Tạo database:
```sql
CREATE DATABASE ev_marketplace;
```

2. Import schema:
```bash
mysql -u root -p ev_marketplace < database/schema.sql
```

### Bước 2: Cấu hình application.yml

File: `src/main/resources/application.yml`

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ev_marketplace
    username: root
    password: your_password  # Thay bằng password MySQL của bạn
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

server:
  port: 8080

jwt:
  secret: mySecretKey123456789012345678901234567890
  expiration: 86400000  # 24 hours
```

### Bước 3: Compile project

```bash
mvn clean compile
```

### Bước 4: Chạy project

```bash
mvn spring-boot:run
```

Server sẽ chạy tại: `http://localhost:8080`

---

## 📝 Chi tiết các API

### 1. POST /api/auth/login

**Mô tả:** Đăng nhập vào hệ thống

**Request:**
```json
{
  "username": "user1",
  "password": "password123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "user1",
  "role": "CONSUMER"
}
```

**Test với curl:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user1","password":"password123"}'
```

---

### 2. POST /api/auth/register/consumer

**Mô tả:** Đăng ký tài khoản Consumer (người mua data)

**Request:**
```json
{
  "username": "consumer1",
  "password": "password123",
  "email": "consumer1@example.com",
  "fullName": "Nguyen Van A",
  "phoneNumber": "0123456789",
  "companyName": "ABC Company"
}
```

**Response (200 OK):**
```json
{
  "message": "Consumer registered successfully"
}
```

**Test với curl:**
```bash
curl -X POST http://localhost:8080/api/auth/register/consumer \
  -H "Content-Type: application/json" \
  -d '{
    "username":"consumer1",
    "password":"password123",
    "email":"consumer1@example.com",
    "fullName":"Nguyen Van A",
    "phoneNumber":"0123456789",
    "companyName":"ABC Company"
  }'
```

---

### 3. POST /api/auth/register/provider

**Mô tả:** Đăng ký tài khoản Provider (người bán data)

**Request:**
```json
{
  "username": "provider1",
  "password": "password123",
  "email": "provider1@example.com",
  "fullName": "Tran Thi B",
  "phoneNumber": "0987654321",
  "companyName": "XYZ Data Corp",
  "businessLicense": "BL123456"
}
```

**Response (200 OK):**
```json
{
  "message": "Provider registered successfully"
}
```

**Test với curl:**
```bash
curl -X POST http://localhost:8080/api/auth/register/provider \
  -H "Content-Type: application/json" \
  -d '{
    "username":"provider1",
    "password":"password123",
    "email":"provider1@example.com",
    "fullName":"Tran Thi B",
    "phoneNumber":"0987654321",
    "companyName":"XYZ Data Corp",
    "businessLicense":"BL123456"
  }'
```

---

### 4. GET /api/data-packages

**Mô tả:** Lấy danh sách tất cả data packages

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "EV Battery Performance Data",
    "description": "Comprehensive battery performance metrics",
    "dataType": "RAW_DATA",
    "format": "CSV",
    "size": 1024000,
    "price": 99.99,
    "pricingModel": "ONE_TIME",
    "status": "ACTIVE",
    "filePath": "/data/battery_data.csv"
  },
  {
    "id": 2,
    "name": "Charging Station Analytics",
    "description": "Analytics report on charging patterns",
    "dataType": "ANALYTICS_REPORT",
    "format": "PDF",
    "size": 512000,
    "price": 149.99,
    "pricingModel": "SUBSCRIPTION",
    "status": "ACTIVE",
    "filePath": "/data/charging_analytics.pdf"
  }
]
```

**Test với curl:**
```bash
curl -X GET http://localhost:8080/api/data-packages
```

---

## 🧪 Hướng dẫn Test

### Test với Postman

1. **Import Collection:**
   - Tạo collection mới tên "EV Marketplace - Giang"
   - Thêm 4 requests như trên

2. **Test từng API:**
   - Test Register Consumer
   - Test Register Provider
   - Test Login
   - Test Get All Packages

3. **Kiểm tra Response:**
   - Status code: 200 OK
   - Response body đúng format
   - Token được trả về (với login)

### Test với curl

Chạy lần lượt các lệnh curl ở trên và kiểm tra kết quả.

---

## 📊 Database

### Kiểm tra dữ liệu

```sql
-- Xem users
SELECT * FROM users;

-- Xem consumers
SELECT * FROM data_consumers;

-- Xem providers
SELECT * FROM data_providers;

-- Xem data packages
SELECT * FROM data_packages;
```

---

## ✅ Checklist hoàn thành

### Setup (30 phút)
- [ ] Clone project về máy
- [ ] Cài đặt MySQL
- [ ] Tạo database `ev_marketplace`
- [ ] Import schema từ `database/schema.sql`
- [ ] Cấu hình `application.yml`
- [ ] Chạy `mvn clean compile` thành công
- [ ] Chạy `mvn spring-boot:run` thành công

### Đọc hiểu code (1 giờ)
- [ ] Đọc `AuthController.java`
- [ ] Đọc `DataPackageController.java`
- [ ] Hiểu cách JWT authentication hoạt động
- [ ] Hiểu flow đăng ký/đăng nhập
- [ ] Hiểu cách lấy danh sách packages

### Test APIs (1 giờ)
- [ ] Test POST /api/auth/register/consumer
- [ ] Test POST /api/auth/register/provider
- [ ] Test POST /api/auth/login
- [ ] Test GET /api/data-packages
- [ ] Kiểm tra database sau mỗi API call
- [ ] Screenshot kết quả test

### Commit & Push (15 phút)
- [ ] Tạo GitHub repository mới
- [ ] Commit code
- [ ] Push lên GitHub
- [ ] Gửi link cho leader

### Chuẩn bị demo (30 phút)
- [ ] Chuẩn bị giải thích AuthController
- [ ] Chuẩn bị giải thích DataPackageController
- [ ] Chuẩn bị demo 4 APIs
- [ ] Chuẩn bị trả lời câu hỏi

---

## 💡 Lưu ý quan trọng

### Về Authentication
- Password được mã hóa bằng BCrypt
- JWT token có thời hạn 24 giờ
- Token được trả về sau khi login thành công

### Về Registration
- Username và email phải unique
- Password tối thiểu 6 ký tự
- Tất cả fields đều required

### Về Get All Packages
- API này không cần authentication
- Trả về tất cả packages trong database
- Nếu chưa có data, trả về array rỗng []

---

## 🐛 Troubleshooting

### Lỗi: "Access denied for user"
**Giải pháp:** Kiểm tra username/password MySQL trong `application.yml`

### Lỗi: "Table doesn't exist"
**Giải pháp:** Import lại schema: `mysql -u root -p ev_marketplace < database/schema.sql`

### Lỗi: "Port 8080 already in use"
**Giải pháp:** Đổi port trong `application.yml` hoặc kill process đang dùng port 8080

### Lỗi: "BUILD FAILURE"
**Giải pháp:** 
- Kiểm tra Java version (cần Java 17+)
- Chạy `mvn clean` rồi `mvn compile` lại

---

## 📚 Tài liệu tham khảo

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [JWT Introduction](https://jwt.io/introduction)
- [MySQL Documentation](https://dev.mysql.com/doc/)

---

## 📞 Hỗ trợ

Nếu gặp vấn đề:
1. Đọc lại file README này
2. Kiểm tra console log
3. Google error message
4. Hỏi leader
5. Hỏi nhóm

---

**Chúc bạn thành công! 🚀**

**Ngày tạo:** 2025-01-20
**Version:** 1.0


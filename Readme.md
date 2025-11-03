# EV Data Analytics Marketplace

**Chợ dữ liệu phân tích xe điện** - Hệ thống marketplace cho việc mua bán và chia sẻ dữ liệu xe điện.

## Tổng quan

EV Data Analytics Marketplace là một nền tảng cho phép:
- **Người dùng dữ liệu (Data Consumers)**: Tìm kiếm, mua và sử dụng dữ liệu EV
- **Nhà cung cấp dữ liệu (Data Providers)**: Chia sẻ và bán dữ liệu EV
- **Quản trị viên (Admin)**: Quản lý hệ thống và kiểm duyệt dữ liệu

## Công nghệ sử dụng

### Backend
- **Spring Boot 3.2.0** - Framework Java
- **Spring Security** - Authentication & Authorization
- **Spring Data JPA** - ORM với Hibernate
- **MySQL 8.0** - Cơ sở dữ liệu
- **JWT** - Token-based authentication
- **Maven** - Dependency management

### Frontend
- **React 18** - UI Framework
- **React Router** - Client-side routing
- **Bootstrap 5** - CSS Framework
- **Axios** - HTTP client
- **React Bootstrap** - UI components

## Cài đặt và chạy ứng dụng

### Yêu cầu hệ thống
- Java 17+
- Node.js 16+
- MySQL 8.0+
- Maven 3.6+

### 1. Cài đặt cơ sở dữ liệu

```sql
-- Tạo database
CREATE DATABASE ev_marketplace;

-- Import schema
mysql -u root -p ev_marketplace < database/schema.sql
```

### 2. Cấu hình Backend

1. Cập nhật file `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ev_marketplace?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: root
    password: password
```

2. Chạy backend:
```bash
mvn spring-boot:run
```

Backend sẽ chạy tại: http://localhost:8080

### 3. Cài đặt và chạy Frontend

```bash
cd frontend
npm start
```

Frontend sẽ chạy tại: http://localhost:3000

## API Endpoints

### Authentication
- `POST /api/auth/login` - Đăng nhập
- `POST /api/auth/register/consumer` - Đăng ký Data Consumer
- `POST /api/auth/register/provider` - Đăng ký Data Provider

### Data Packages
- `GET /api/data-packages` - Lấy danh sách gói dữ liệu
- `GET /api/data-packages/{id}` - Lấy chi tiết gói dữ liệu
- `GET /api/data-packages/search` - Tìm kiếm gói dữ liệu
- `POST /api/data-packages` - Tạo gói dữ liệu mới
- `PUT /api/data-packages/{id}` - Cập nhật gói dữ liệu
- `DELETE /api/data-packages/{id}` - Xóa gói dữ liệu

## Cấu trúc Database

### Các bảng chính:
- `users` - Bảng người dùng cơ bản
- `data_consumers` - Người dùng dữ liệu
- `data_providers` - Nhà cung cấp dữ liệu
- `admins` - Quản trị viên
- `data_sources` - Nguồn dữ liệu
- `data_packages` - Gói dữ liệu

## Tài khoản mẫu

### Admin
- Username: `admin`
- Password: `password`
- Email: `admin@evmarketplace.com`

### Data Consumer
- Username: `consumer1`
- Password: `password`
- Email: `consumer1@example.com`

### Data Provider
- Username: `provider1`
- Password: `password`
- Email: `provider1@example.com`

## Hướng dẫn sử dụng

### Cho Data Consumer
1. Đăng ký tài khoản với vai trò "Người dùng dữ liệu"
2. Đăng nhập và truy cập trang "Dữ liệu"
3. Sử dụng bộ lọc để tìm kiếm gói dữ liệu phù hợp
4. Xem chi tiết và mua gói dữ liệu

### Cho Data Provider
1. Đăng ký tài khoản với vai trò "Nhà cung cấp dữ liệu"
2. Đăng nhập và truy cập "Dashboard Nhà cung cấp"
3. Tạo nguồn dữ liệu và gói dữ liệu
4. Quản lý và theo dõi doanh thu

### Cho Admin
1. Đăng nhập với tài khoản admin
2. Truy cập "Dashboard Admin"
3. Duyệt các gói dữ liệu chờ phê duyệt
4. Quản lý người dùng và hệ thống

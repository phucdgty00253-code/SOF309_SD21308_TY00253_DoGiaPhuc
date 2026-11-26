# Lab 8 - Tài liệu kỹ thuật và triển khai PolyCoffee

## Bài 1. Danh mục tài liệu

**Đối tượng đọc:** lập trình viên mới hoặc người bảo trì, tester, người triển khai, giảng viên chấm bài, quản lý quán (hướng dẫn sử dụng).

| Tài liệu | Mục tiêu | Người đọc chính |
|---|---|---|
| Tổng quan hệ thống (Bài 2.1) | Hiểu dự án làm gì, cho ai | Tất cả |
| Kiến trúc (Bài 2.2, 3) | Biết code đặt ở đâu và request đi thế nào | Lập trình viên, tester |
| Cài đặt và triển khai (Bài 4) | Chạy được trên máy mới và trên server | Người triển khai, giảng viên |
| Đặc tả ([SPECIFICATIONS](../SPECIFICATIONS.md)) | Quy tắc nghiệp vụ và ràng buộc dữ liệu | Lập trình viên, tester |
| Kiểm thử ([TEST_PLAN](../TEST_PLAN.md), [TEST_CASES](../TEST_CASES.md)) | Biết đã kiểm tra gì | Tester, giảng viên |

## Bài 2.1. Giới thiệu dự án
* **Tên:** PolyCoffee.
* **Mục tiêu nghiệp vụ:** giúp quán bán nước uống ghi phiếu, thu tiền, quản lý thực đơn và nhân sự, xem doanh thu trên web.
* **Chức năng chính:** thực đơn công khai, đăng nhập và phân quyền, bán hàng POS, quản lý loại đồ uống, đồ uống, nhân viên, hóa đơn, thống kê.
* **Đối tượng sử dụng:** khách hàng, nhân viên, quản lý.
* **Luồng hoạt động:** khách gọi món → nhân viên thêm vào phiếu (`waiting`) và có thể sửa → khách trả tiền → nhân viên thanh toán (`finish`) → pha chế và giao đồ uống → quản lý xem doanh thu. Phiếu có thể bị hủy (`cancel`); phiếu đã thanh toán chỉ quản lý được hủy.

## Bài 2.2. Kiến trúc
* **Client - Server:** trình duyệt gửi HTTP tới Tomcat; server dựng HTML và trả về. Chỉ server nói chuyện với CSDL.
* **MVC:** Model là `entity`, `dto`, `service`, `dao`; View là JSP; Controller là Servlet.

| Thành phần | Vai trò |
|---|---|
| Client | Hiển thị HTML/CSS, kiểm tra form cơ bản bằng HTML5 |
| Server | Filter (UTF-8, quyền), Servlet, Service, DAO |
| Database | SQL Server: 6 bảng, 2 stored procedure |

**Nguyên tắc thiết kế:** mỗi tầng một nhiệm vụ; controller nhận service qua constructor để unit test thay được; kiểm tra dữ liệu ở server dù client đã kiểm tra; thay đổi dữ liệu dùng POST rồi redirect (PRG).

## Bài 3. Công nghệ và cấu trúc mã nguồn

| Loại | Công nghệ |
|---|---|
| Ngôn ngữ | Java 17, SQL, HTML, CSS |
| Công nghệ web | Jakarta Servlet 6.1, JSP, JSTL 3, JDBC |
| Application server | Apache Tomcat 11 |
| CSDL | SQL Server 2022 (driver `mssql-jdbc`) |
| Thư viện | jBCrypt, Angus Mail |
| Công cụ | Maven, Git, GitHub Actions, JUnit 5, Mockito |

```text
src/main/java/com/polycoffee/
├── controller/   # Controller: auth, home, category, drink, staff, pos, bill, stats
├── service/      # Service: quy tắc nghiệp vụ
├── dao/          # DAO: SQL
├── entity/, dto/ # Entity
├── filter/       # Utf8Filter, AuthFilter
├── exception/    # Ngoại lệ nghiệp vụ
└── util/         # JdbcUtil, AuthUtil, ParamUtil, FileUtil, PasswordUtil, MailUtil
src/main/webapp/  # View: WEB-INF/views/*.jsp, assets/css
database/         # polycoffee.sql
src/test/java/    # Unit test
```

**Quy ước code:** servlet không viết SQL; JSP không gọi DAO; SQL luôn dùng tham số `?`; dữ liệu người dùng in qua `<c:out>`; thông báo lỗi nghiệp vụ bằng tiếng Việt; không commit `db.properties`, `mail.properties`, mật khẩu, khóa API.

## Bài 4. Cài đặt và triển khai

### 4.1. Chạy trên máy local
**Cần:** JDK 17 trở lên, Maven 3.9+, Tomcat 11, SQL Server (cài trực tiếp trên Windows hoặc Docker).

**1. Chạy SQL Server bằng Docker** (macOS/Linux). Tự đặt mật khẩu mạnh, không ghi vào Git:
```bash
read -s SA_PASSWORD && export SA_PASSWORD
docker run -d --name polycoffee-sql --platform linux/amd64 \
  -e ACCEPT_EULA=Y -e MSSQL_SA_PASSWORD="$SA_PASSWORD" \
  -p 127.0.0.1:1433:1433 mcr.microsoft.com/mssql/server:2022-latest
```
**2. Tạo CSDL** (chờ SQL Server khởi động xong). Script **xóa database `polycoffee` cũ** rồi tạo lại:
```bash
docker cp database/polycoffee.sql polycoffee-sql:/tmp/
docker exec polycoffee-sql /opt/mssql-tools18/bin/sqlcmd -C -S localhost -U sa -P "$SA_PASSWORD" -f 65001 -b -i /tmp/polycoffee.sql
```
Trên Windows có thể mở file bằng SQL Server Management Studio rồi bấm Execute.

**3. Cấu hình:** sao chép `src/main/resources/db.properties.example` thành `db.properties`, điền mật khẩu. Muốn gửi mail thì làm tương tự với `mail.properties.example`. Cũng có thể dùng biến môi trường `DB_URL`, `DB_USER`, `DB_PASSWORD`, `MAIL_HOST`, `MAIL_PORT`...

**4. Build và deploy:**
```bash
mvn clean package
cp target/polycoffee.war <TOMCAT>/webapps/
<TOMCAT>/bin/catalina.sh run
```
**5. Kiểm tra chạy thành công:** mở `http://localhost:8080/polycoffee/home` thấy thực đơn có ảnh/giá; đăng nhập `admin@polycoffee.vn` / `123456`; mở được màn Doanh thu.

| Lỗi thường gặp | Cách xử lý |
|---|---|
| Trang chủ báo lỗi 500 | Kiểm tra SQL Server đã chạy và `db.properties` đúng |
| `Login failed for user 'sa'` | Sai mật khẩu |
| Cổng 8080 hoặc 1433 đã bị chiếm | Dừng tiến trình đang dùng hoặc đổi cổng |
| "Chưa cấu hình máy chủ gửi mail" | Thiếu `mail.properties` |

### 4.2. Triển khai lên server/VPS (hướng dẫn, chưa thực hiện thật)
1. VPS Linux cài JDK 17, Tomcat 11 (chạy bằng user riêng, không dùng root), SQL Server hoặc máy CSDL riêng.
2. Chạy script tạo CSDL; tạo **tài khoản CSDL riêng** chỉ có quyền trên `polycoffee`, không dùng `sa`.
3. Đặt `DB_*`, `MAIL_*` bằng biến môi trường trong `setenv.sh` (quyền file 600).
4. Chép WAR vào `webapps/`; đặt Nginx phía trước và bật HTTPS; bật `secure` cho cookie phiên.
5. Chỉ mở cổng 80/443; **không** mở 1433 ra Internet.

**Vận hành, bảo mật, bảo trì:** đổi mật khẩu mọi tài khoản mẫu và xóa dữ liệu mẫu trước khi dùng thật (bỏ phần dữ liệu mẫu trong script); backup CSDL hằng ngày và thử khôi phục; theo dõi log Tomcat; cập nhật bản vá JDK, Tomcat, thư viện; xem mục "Giới hạn đã biết" trong [SPECIFICATIONS](../SPECIFICATIONS.md).

## Bài 5
Đề ghi "Giảng viên cho thêm", chưa có nội dung.

# ĐẶC TẢ DỰ ÁN POLYCOFFEE (SPECIFICATIONS)

> Tài liệu gốc của dự án. Mỗi quy tắc nghiệp vụ có một mã **BR-...**.
> Thứ tự truy vết: **SPECIFICATIONS (BR) → [TEST_PLAN](TEST_PLAN.md) → [TEST_CASES](TEST_CASES.md) (TC_...) → file test trong `src/test/java`**.
> Nguồn yêu cầu: đề Assignment, slide 1-8 và Lab 1-8 môn JAV102 (Dự án Java 1).

## 1. Thông tin dự án
| Mục | Nội dung |
|---|---|
| Tên đề tài | PolyCoffee - website quản lý quán cà phê |
| Môn gốc | JAV102 - Dự án Java 1 (đối tượng kiểm thử của môn SOF309) |
| Repo | `phucdgty00253-code/SOF309_SD21308_TY00253_DoGiaPhuc` |
| Mục tiêu nghiệp vụ | Nhân viên bán hàng và quản lý quán nhanh chóng trên web; khách xem được đồ uống và giá |

## 2. Công nghệ và cách chạy
* Java 17, Jakarta Servlet 6.1, JSP + JSTL 3, JDBC, **SQL Server**, Tomcat 11, Maven (WAR).
* URL local: `http://localhost:8080/polycoffee/home`
* Cài đặt và triển khai chi tiết: [Lab 8](labs/Lab8_Tai_lieu_ky_thuat.md).
* Chạy test: `mvn test`

## 3. Kiến trúc
Luồng bắt buộc: **JSP ← Controller (Servlet) → Service → DAO → SQL Server**

| Tầng | Package | Trách nhiệm |
|---|---|---|
| Filter | `com.polycoffee.filter` | UTF-8, kiểm tra đăng nhập và quyền |
| Controller | `com.polycoffee.controller.<chức năng>` | Đọc tham số, gọi service, forward hoặc redirect. Không viết SQL |
| Service | `com.polycoffee.service` | Quy tắc nghiệp vụ, kiểm tra dữ liệu, ném ngoại lệ có thông báo tiếng Việt |
| DAO | `com.polycoffee.dao` | Chỉ SQL, luôn dùng `PreparedStatement` |
| Entity / DTO | `com.polycoffee.entity`, `dto` | Dữ liệu |
| View | `src/main/webapp/WEB-INF/views` | Chỉ hiển thị, dữ liệu người dùng in qua `c:out` |

## 4. Vai trò, màn hình và URL
| URL | Phương thức | Khách | Nhân viên | Quản lý | Controller |
|---|---|:-:|:-:|:-:|---|
| `/home` | GET | ✔ | ✔ | ✔ | `HomeServlet` |
| `/auth/login` | GET, POST | ✔ | ✔ | ✔ | `AuthServlet` |
| `/auth/logout` | POST | | ✔ | ✔ | `AuthServlet` |
| `/auth/forgot-password` | GET, POST | ✔ | ✔ | ✔ | `ForgotPasswordServlet` |
| `/account/profile`, `/account/password` | GET, POST | | ✔ | ✔ | `AccountServlet` |
| `/employee/pos` + `/init`, `/update-quantity`, `/checkout`, `/cancel` | GET / POST | | ✔ | ✔ | `PosServlet` |
| `/employee/bills`, `/employee/bills/detail` | GET | | ✔ | ✔ | `EmployeeBillServlet` |
| `/manager/categories` + `/create`, `/update`, `/delete` | GET / POST | | | ✔ | `CategoryServlet` |
| `/manager/drinks` + `/form`, `/create`, `/update`, `/delete` | GET / POST | | | ✔ | `DrinkServlet` |
| `/manager/staff` + `/form`, `/create`, `/update`, `/status`, `/reset-password` | GET / POST | | | ✔ | `StaffServlet` |
| `/manager/bills`, `/detail`, `/cancel` | GET / POST | | | ✔ | `BillManageServlet` |
| `/manager/stats/top-drinks`, `/manager/stats/revenue` | GET | | | ✔ | `StatsServlet` |

## 5. Ràng buộc dữ liệu (kiểm tra ở cả client và server)
| Trường | Ràng buộc |
|---|---|
| Email | Đúng định dạng, tối đa 150 ký tự, không phân biệt hoa thường, không trùng |
| Mật khẩu | 6 - 50 ký tự, không toàn khoảng trắng; lưu dạng băm BCrypt |
| Họ tên | 2 - 100 ký tự |
| Số điện thoại | Đúng 10 chữ số, bắt đầu bằng 0 |
| Tên loại | 2 - 50 ký tự, không trùng |
| Tên đồ uống | 2 - 100 ký tự, không trùng |
| Giá | Số nguyên từ 1.000 đến 1.000.000 đồng |
| Mô tả | Tối đa 500 ký tự |
| Ảnh | JPG, PNG, WEBP; tối đa 2 MB |
| Số lượng mỗi món | 1 - 99 |
| Ngày lọc | `yyyy-MM-dd`; ngày bắt đầu không sau ngày kết thúc |

## 6. Quy tắc nghiệp vụ

### 6.1. Tài khoản (BR-AUTH)
| Mã | Quy tắc |
|---|---|
| BR-AUTH-01 | Đăng nhập đúng thì lưu người dùng vào session, đổi session id, chuyển tới trang đang định vào (nếu có) hoặc màn bán hàng |
| BR-AUTH-02 | Sai email hoặc sai mật khẩu trả về cùng một thông báo |
| BR-AUTH-03 | Tài khoản bị khóa không đăng nhập được |
| BR-AUTH-04 | Email và mật khẩu bắt buộc, email đúng định dạng; dữ liệu sai thì không truy vấn CSDL |
| BR-AUTH-05 | Đăng xuất bằng POST và hủy toàn bộ session |
| BR-AUTH-06 | Đổi mật khẩu: đúng mật khẩu cũ, mật khẩu mới 6-50 ký tự, khác mật khẩu cũ, nhập lại phải khớp |
| BR-AUTH-07 | Quên mật khẩu: sinh mật khẩu mới, lưu, gửi email. Email không tồn tại vẫn hiện cùng thông báo. Gửi mail lỗi thì giữ mật khẩu cũ |
| BR-AUTH-08 | Chỉ sửa được thông tin của chính mình |
| BR-AUTH-09 | Xử lý đăng nhập dưới 2 giây |

### 6.2. Phân quyền (BR-SEC) và trang chủ (BR-HOME)
| Mã | Quy tắc |
|---|---|
| BR-SEC-01 | Chưa đăng nhập vào `/manager/*`, `/employee/*`, `/account/*` thì chuyển tới trang đăng nhập |
| BR-SEC-02 | `/manager/*` chỉ dành cho quản lý |
| BR-SEC-03 | Chỉ nhớ trang cần quay lại với request GET |
| BR-HOME-01 | Khách xem được đồ uống đang bán, thuộc loại đang hoạt động |
| BR-HOME-02 | Tham số lọc loại không hợp lệ được hiểu là "tất cả" |

### 6.3. Loại đồ uống (BR-CAT)
| Mã | Quy tắc |
|---|---|
| BR-CAT-01 | Xem danh sách, thêm, sửa loại |
| BR-CAT-02 | Tên 2-50 ký tự, không trùng (được giữ tên của chính nó khi sửa) |
| BR-CAT-03 | Chỉ xóa được loại chưa có đồ uống |
| BR-CAT-04 | Mã không tồn tại trả lỗi 404 |

### 6.4. Đồ uống (BR-DRK)
| Mã | Quy tắc |
|---|---|
| BR-DRK-01 | Tìm theo tên, loại, trạng thái; 10 dòng mỗi trang; số trang sai được đưa về khoảng hợp lệ |
| BR-DRK-02 | Tên, loại, giá, mô tả đúng ràng buộc ở mục 5 |
| BR-DRK-03 | Tên đồ uống không trùng |
| BR-DRK-04 | Chỉ xóa được đồ uống chưa từng được bán |
| BR-DRK-05 | Đường dẫn ảnh do server quyết định, không nhận từ client |

### 6.5. Nhân viên (BR-STF)
| Mã | Quy tắc |
|---|---|
| BR-STF-01 | Tìm theo tên, email, trạng thái; 10 dòng mỗi trang |
| BR-STF-02 | Họ tên, email, số điện thoại, mật khẩu đúng ràng buộc ở mục 5 |
| BR-STF-03 | Email không trùng |
| BR-STF-04 | Không xóa tài khoản, chỉ khóa hoặc mở khóa |
| BR-STF-05 | Màn này chỉ tác động lên nhân viên; vai trò không nhận từ client |
| BR-STF-06 | Cấp lại mật khẩu: ngẫu nhiên 10 ký tự, lưu và gửi email; gửi lỗi thì giữ mật khẩu cũ |

### 6.6. Bán hàng POS (BR-POS)
| Mã | Quy tắc |
|---|---|
| BR-POS-01 | Thêm món đầu tiên thì tạo hóa đơn `waiting` cùng món đó (một giao dịch) |
| BR-POS-02 | Món đã có thì tăng số lượng 1; chưa có thì thêm dòng mới; tính lại tổng |
| BR-POS-03 | Chỉ bán đồ uống đang kinh doanh |
| BR-POS-04 | Số lượng 1-99; giảm về 0 hoặc bấm xóa thì bỏ món |
| BR-POS-05 | Chỉ sửa, thanh toán, hủy ở POS khi hóa đơn đang `waiting` |
| BR-POS-06 | Nhân viên chỉ thao tác trên hóa đơn của mình; quản lý thao tác được mọi hóa đơn |
| BR-POS-07 | Chỉ thanh toán hóa đơn có ít nhất một món |
| BR-POS-08 | Thay đổi dữ liệu chỉ qua POST với thao tác hợp lệ |

### 6.7. Hóa đơn (BR-BILL)
| Mã | Quy tắc |
|---|---|
| BR-BILL-01 | Chuyển trạng thái hợp lệ: `waiting → finish`, `waiting → cancel`, `finish → cancel` (chỉ quản lý) |
| BR-BILL-02 | Trạng thái đã bị người khác đổi thì từ chối và yêu cầu tải lại |
| BR-BILL-03 | Nhân viên chỉ xem hóa đơn của mình |
| BR-BILL-04 | Quản lý xem mọi hóa đơn, 10 dòng mỗi trang, lọc theo ngày |
| BR-BILL-05 | Khoảng ngày không hợp lệ thì báo lỗi, không truy vấn |
| BR-BILL-06 | Chi tiết gồm món, số lượng, đơn giá, tổng tiền, trạng thái, nhân viên tạo, ngày tạo |
| BR-BILL-07 | Hóa đơn không tồn tại trả lỗi 404 |

### 6.8. Thống kê (BR-STAT)
| Mã | Quy tắc |
|---|---|
| BR-STAT-01 | Top 5 đồ uống bán chạy theo số lượng, chỉ tính hóa đơn `finish`, lọc theo thời gian |
| BR-STAT-02 | Doanh thu theo ngày của hóa đơn `finish`, tăng dần theo ngày, có bảng và biểu đồ |
| BR-STAT-03 | Khoảng thời gian không hợp lệ thì báo lỗi; một ngày (từ = đến) là hợp lệ |
| BR-STAT-04 | Trang thống kê xử lý dữ liệu lớn dưới 2 giây |

## 7. Cơ sở dữ liệu
Script: [`database/polycoffee.sql`](../database/polycoffee.sql). Bảng: `categories`, `drinks`, `users`, `cards`, `bills`, `bill_details`.
Thủ tục: `sp_top5_best_selling`, `sp_revenue_by_day`. Sơ đồ: [Lab 2](labs/Lab2_ERD_va_trien_khai.md).

## 8. Tài khoản dữ liệu mẫu (chỉ dùng khi học tập)
| Email | Mật khẩu | Vai trò |
|---|---|---|
| `admin@polycoffee.vn` | `123456` | Quản lý |
| `nhanvien@polycoffee.vn` | `123456` | Nhân viên |
| `khoa@polycoffee.vn` | `123456` | Nhân viên bị khóa |

## 9. Giới hạn đã biết
* Khóa một nhân viên đang đăng nhập chỉ có hiệu lực ở lần đăng nhập sau.
* Chưa giới hạn số lần đăng nhập sai và số lần yêu cầu quên mật khẩu.
* Chưa có CSRF token; hiện dựa vào cookie `SameSite=Lax`.
* Khi mail server lỗi, trang quên mật khẩu chỉ báo lỗi với email có tồn tại, nên có thể lộ email đó tồn tại.
* Ảnh upload nằm trong thư mục ứng dụng đã deploy; deploy lại sạch sẽ mất ảnh.

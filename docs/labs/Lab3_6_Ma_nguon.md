# Lab 3 - 6: bản đồ mã nguồn

Đường dẫn Java tính từ [`src/main/java/com/polycoffee/`](../../src/main/java/com/polycoffee/).

## Lab 3
| Bài | Yêu cầu | File |
|---|---|---|
| 1 | CSDL SQL Server, bảng, PK, FK | [`database/polycoffee.sql`](../../database/polycoffee.sql) |
| 2 | Entity `Category`, `Drink`, `User`, `Bill`, `BillDetail` (+ `Card`) | `entity/` |
| 3 | `JdbcUtil` (getConnection, executeQuery, executeUpdate), `CategoryDAO` | `util/JdbcUtil.java`, `dao/CategoryDAO.java` |
| 4 | DAO còn lại | `dao/DrinkDAO.java`, `UserDAO.java`, `BillDAO.java`, `BillDetailDAO.java`, `CardDAO.java`; giao diện chung `dao/CrudDAO.java` |

**Khác với code mẫu trong đề:** mật khẩu CSDL đọc từ `db.properties` (không ghi cứng trong mã); mọi kết nối đều được đóng; `create` trả về khóa vừa tạo thay vì số dòng.

## Lab 4
| Bài | Yêu cầu | File |
|---|---|---|
| 1 | `AuthUtil`, `ParamUtil` (và `FileUtil`) | `util/` |
| 2 | `Utf8Filter`, `AuthFilter` | `filter/` |
| 3 | `findByEmail`, `AuthServlet`: đăng nhập, session, đăng xuất | `dao/UserDAO.java`, `controller/auth/AuthServlet.java` |
| 4 | CRUD loại đồ uống | `controller/category/CategoryServlet.java`, view `manager/category/list.jsp` |

## Lab 5
| Bài | Yêu cầu | File |
|---|---|---|
| 1 | Tìm đồ uống theo tên, loại, trạng thái; 10 dòng/trang | `controller/drink/DrinkServlet.java`, `DrinkDAO.search` |
| 2 | Tìm nhân viên theo tên, email, trạng thái; 10 dòng/trang | `controller/staff/StaffServlet.java`, `UserDAO.searchStaff` |
| 3 | Mật khẩu ngẫu nhiên, lưu CSDL, gửi email | `service/PasswordReset.java`, `util/MailUtil.java` |

## Lab 6
| Bài | Yêu cầu | File |
|---|---|---|
| 1 | Danh sách hóa đơn phân trang, chi tiết, hủy | `controller/bill/BillManageServlet.java`, view `bill/` |
| 2 | Top 5 bán chạy theo thời gian | `sp_top5_best_selling`, `dao/StatisticDAO.java`, `controller/stats/StatsServlet.java` |
| 3 | Doanh thu theo thời gian, có biểu đồ | `sp_revenue_by_day`, view `manager/stats/revenue.jsp` |

Màn bán hàng POS (slide 6): `controller/pos/PosServlet.java`, `service/PosService.java`.

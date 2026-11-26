# KẾ HOẠCH KIỂM THỬ (TEST PLAN) - POLYCOFFEE

Đầu vào: [SPECIFICATIONS.md](SPECIFICATIONS.md). Đầu ra: [TEST_CASES.md](TEST_CASES.md) và mã test trong `src/test/java`.

## 1. Mục tiêu
1. Chứng minh mỗi quy tắc BR ở tầng Controller có ít nhất một test case.
2. Bắt lỗi ở giá trị biên của form và các thao tác trái quyền.
3. Đạt `mvn test` pass 100% (yêu cầu Y3, tuần 2).

## 2. Phạm vi
**Trong phạm vi tuần 1-2:** controller và quy tắc nghiệp vụ của 9 nhóm: đăng nhập/tài khoản, phân quyền, trang chủ, loại đồ uống, đồ uống, nhân viên, bán hàng POS, hóa đơn, thống kê.

**Kiểm tra tích hợp (chạy thủ công/bằng script với CSDL thật):** SQL trong DAO, stored procedure, JSP, filter chạy trên Tomcat.

**Ngoài phạm vi đợt này:** Selenium UI (tuần 3), TestNG (tuần 4), CI/CD và báo cáo debug (tuần 5), kiểm thử hiệu năng tải lớn, kiểm thử bảo mật chuyên sâu, gửi email thật.

## 3. Cách tiếp cận
| Mức | Công cụ | Thật / Giả |
|---|---|---|
| Unit test controller | JUnit 5 + Mockito | **Thật:** controller (hoặc filter) + service. **Giả:** DAO, MailSender, request, response, session |
| Tích hợp | Tomcat 11 + SQL Server + script HTTP | Thật toàn bộ, trừ SMTP |

Giả DAO nên unit test chạy không cần CSDL và chạy được trên GitHub Actions. Đổi lại, unit test **không** kiểm tra được câu SQL; phần đó do kiểm tra tích hợp (TC_IT).

## 4. Kỹ thuật thiết kế test
| Kỹ thuật | Áp dụng | Ví dụ |
|---|---|---|
| Phân vùng tương đương | Dữ liệu đúng / sai | TC_AUTH_001, TC_AUTH_003 |
| Giá trị biên (`@ParameterizedTest` + `@CsvSource`) | Độ dài, giá, trang, số lượng | TC_DRINK_004, TC_CAT_003 |
| Bảng quyết định | Quyền và trạng thái | TC_SEC_003, TC_BILL_001 |
| Chuyển trạng thái | Hóa đơn, phiên đăng nhập | TC_POS_009, TC_AUTH_007 |
| Ngoại lệ (`assertThrows`) | Trùng dữ liệu, vi phạm nghiệp vụ | TC_CAT_004, TC_POS_010 |
| Thời gian (`@Timeout`) | Không bị treo | TC_AUTH_016, TC_STATS_006 |
| Đoán lỗi | Giả mạo tham số | TC_STAFF_010, TC_DRINK_010 |

## 5. Quy ước ID và tổ chức mã test
* ID: `TC_<NHÓM>_<3 số>`. Mỗi ID là **một file**: `TC_<NHÓM>_<số>_<Mô tả>ControllerTest.java`.
* Mỗi nhóm là một package, trong đó `<Nhóm>ControllerTest.java` là lớp nền (abstract) dựng controller được test.
* Sơ đồ package và luồng gọi: [`controllers/README.md`](../src/test/java/com/poly/sof309/controllers/README.md).

## 6. Môi trường và nguồn lực
| Mục | Giá trị |
|---|---|
| Người thực hiện | Đỗ Gia Phúc (TY00253), có AI hỗ trợ (ghi ở `ai-logs/`) |
| JDK | 17 (biên dịch `release 17`) |
| Thư viện test | JUnit 5.10.2, Mockito 5.17.0 |
| Web / CSDL | Tomcat 11, SQL Server 2022 (Docker) |

## 7. Lịch (theo README.md)
| Tuần | Việc | Sản phẩm |
|---|---|---|
| 1 | Web chạy local, đặc tả, test plan, test case | `SPECIFICATIONS.md`, `TEST_PLAN.md`, `TEST_CASES.md`, `ai-logs/01_test_design_ai.md` |
| 2 | Unit test controller | `src/test/java/.../controllers/`, `ai-logs/02_junit_script_ai.md` |

## 8. Tiêu chí bắt đầu và kết thúc
**Bắt đầu:** đặc tả có mã BR; dự án biên dịch được.

**Kết thúc:** 100% test case unit đã chạy và pass; không còn lỗi mức Critical/High đang mở; mọi quy tắc BR hoặc có test case hoặc được ghi là chỉ kiểm tra tích hợp.

## 9. Rủi ro
| Rủi ro | Ảnh hưởng | Cách giảm |
|---|---|---|
| Giả DAO nên lỗi SQL không bị phát hiện | Test xanh nhưng web lỗi | Chạy kiểm tra tích hợp với SQL Server thật |
| SQL Server trên máy Mac ARM phải chạy giả lập | Khởi động chậm, khó cài | Dùng Docker image `linux/amd64` |
| Không có SMTP thật | Không kiểm tra được thư đến | Giả `MailSender` ở unit test; dùng SMTP bẫy thư khi chạy local |
| Test do cùng công cụ viết với code | Test có thể lặp lại hiểu sai của code | Sinh viên đối chiếu từng TC với BR; thử cố ý làm sai code xem test có đỏ không |

## 10. Cách chạy
```bash
mvn test                                  # tất cả
mvn test -Dtest='*ControllerTest'         # nghiệm thu tuần 2
mvn test -Dtest='TC_AUTH_001*'            # một test case theo ID
mvn test -Dgroups=TC_AUTH_001             # theo nhãn @Tag
mvn test -Dtest='com.poly.sof309.controllers.auth.*Test'   # một nhóm
```

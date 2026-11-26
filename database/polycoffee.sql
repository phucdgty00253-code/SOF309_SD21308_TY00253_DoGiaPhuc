/* PolyCoffee - script tạo CSDL SQL Server (Lab 3) kèm dữ liệu mẫu.
   Chạy lại được nhiều lần: script XÓA database polycoffee cũ rồi tạo mới. */
USE master;
GO
IF DB_ID('polycoffee') IS NOT NULL
BEGIN
    ALTER DATABASE polycoffee SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE polycoffee;
END
GO
CREATE DATABASE polycoffee;
GO
USE polycoffee;
GO
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
GO

CREATE TABLE categories (
    id      INT IDENTITY(1,1) PRIMARY KEY,
    name    NVARCHAR(100) NOT NULL,
    active  BIT NOT NULL DEFAULT 1,
    CONSTRAINT uq_categories_name UNIQUE (name)
);

CREATE TABLE drinks (
    id           INT IDENTITY(1,1) PRIMARY KEY,
    category_id  INT NOT NULL,
    name         NVARCHAR(150) NOT NULL,
    price        INT NOT NULL,
    image        NVARCHAR(255) NULL,
    description  NVARCHAR(500) NULL,
    active       BIT NOT NULL DEFAULT 1,
    CONSTRAINT fk_drinks_category FOREIGN KEY (category_id) REFERENCES categories(id),
    CONSTRAINT ck_drinks_price CHECK (price BETWEEN 1000 AND 1000000)
);

/* role: 1 = quản lý, 0 = nhân viên */
CREATE TABLE users (
    id         INT IDENTITY(1,1) PRIMARY KEY,
    email      VARCHAR(150) NOT NULL,
    password   VARCHAR(100) NOT NULL,
    full_name  NVARCHAR(100) NOT NULL,
    phone      VARCHAR(15) NULL,
    role       BIT NOT NULL DEFAULT 0,
    active     BIT NOT NULL DEFAULT 1,
    CONSTRAINT uq_users_email UNIQUE (email)
);

/* Thẻ bàn (slide 3). Hóa đơn có thể không gắn thẻ. */
CREATE TABLE cards (
    id      INT IDENTITY(1,1) PRIMARY KEY,
    code    VARCHAR(20) NOT NULL,
    status  BIT NOT NULL DEFAULT 1,
    CONSTRAINT uq_cards_code UNIQUE (code)
);

CREATE TABLE bills (
    id          INT IDENTITY(1,1) PRIMARY KEY,
    user_id     INT NOT NULL,
    card_id     INT NULL,
    code        VARCHAR(30) NOT NULL,
    created_at  DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    total       INT NOT NULL DEFAULT 0,
    status      VARCHAR(10) NOT NULL DEFAULT 'waiting',
    CONSTRAINT uq_bills_code UNIQUE (code),
    CONSTRAINT fk_bills_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_bills_card FOREIGN KEY (card_id) REFERENCES cards(id),
    CONSTRAINT ck_bills_status CHECK (status IN ('waiting', 'finish', 'cancel')),
    CONSTRAINT ck_bills_total CHECK (total >= 0)
);

CREATE TABLE bill_details (
    id        INT IDENTITY(1,1) PRIMARY KEY,
    bill_id   INT NOT NULL,
    drink_id  INT NOT NULL,
    quantity  INT NOT NULL,
    price     INT NOT NULL,
    CONSTRAINT fk_details_bill FOREIGN KEY (bill_id) REFERENCES bills(id) ON DELETE CASCADE,
    CONSTRAINT fk_details_drink FOREIGN KEY (drink_id) REFERENCES drinks(id),
    CONSTRAINT uq_details_bill_drink UNIQUE (bill_id, drink_id),
    CONSTRAINT ck_details_quantity CHECK (quantity > 0),
    CONSTRAINT ck_details_price CHECK (price >= 0)
);
CREATE INDEX ix_bills_user_created ON bills(user_id, created_at);
CREATE INDEX ix_bills_status_created ON bills(status, created_at);
GO

/* Top 5 đồ uống bán chạy. Chỉ tính hóa đơn 'finish'. Ngày để NULL = không giới hạn. */
CREATE PROCEDURE sp_top5_best_selling @from DATE = NULL, @to DATE = NULL
AS
BEGIN
    SET NOCOUNT ON;
    SELECT TOP 5 d.id, d.name, d.price, d.image,
           SUM(bd.quantity) AS total_quantity,
           SUM(CAST(bd.quantity AS BIGINT) * bd.price) AS total_revenue
    FROM bill_details bd
    JOIN bills b ON b.id = bd.bill_id
    JOIN drinks d ON d.id = bd.drink_id
    WHERE b.status = 'finish'
      AND (@from IS NULL OR b.created_at >= @from)
      AND (@to IS NULL OR b.created_at < DATEADD(DAY, 1, @to))
    GROUP BY d.id, d.name, d.price, d.image
    ORDER BY total_quantity DESC, total_revenue DESC, d.id;
END
GO

/* Doanh thu theo ngày, sắp xếp tăng dần theo thời gian. */
CREATE PROCEDURE sp_revenue_by_day @from DATE = NULL, @to DATE = NULL
AS
BEGIN
    SET NOCOUNT ON;
    SELECT CAST(b.created_at AS DATE) AS revenue_date,
           COUNT(*) AS bill_count,
           SUM(CAST(b.total AS BIGINT)) AS revenue
    FROM bills b
    WHERE b.status = 'finish'
      AND (@from IS NULL OR b.created_at >= @from)
      AND (@to IS NULL OR b.created_at < DATEADD(DAY, 1, @to))
    GROUP BY CAST(b.created_at AS DATE)
    ORDER BY revenue_date;
END
GO

/* ===== DỮ LIỆU MẪU =====
   Mật khẩu của mọi tài khoản mẫu: 123456 (đã băm BCrypt). Chỉ dùng cho môi trường học tập. */
INSERT INTO users(email, password, full_name, phone, role, active) VALUES
('admin@polycoffee.vn', '$2a$10$sGnC0BauqfDABv/9aE.4KOpLLmn/GxasQx75Klh8tpOXwSkURWZBS', N'Quản lý PolyCoffee', '0900000001', 1, 1),
('nhanvien@polycoffee.vn', '$2a$10$sGnC0BauqfDABv/9aE.4KOpLLmn/GxasQx75Klh8tpOXwSkURWZBS', N'Nguyễn Văn An', '0900000002', 0, 1),
('khoa@polycoffee.vn', '$2a$10$sGnC0BauqfDABv/9aE.4KOpLLmn/GxasQx75Klh8tpOXwSkURWZBS', N'Trần Thị Khóa', '0900000003', 0, 0);

INSERT INTO categories(name, active) VALUES
(N'Cà phê', 1), (N'Trà', 1), (N'Đá xay', 1), (N'Nước ép', 1);

INSERT INTO drinks(category_id, name, price, image, description, active) VALUES
(1, N'Cà phê đen đá', 25000, NULL, N'Cà phê rang xay pha phin', 1),
(1, N'Cà phê sữa đá', 29000, NULL, N'Cà phê phin cùng sữa đặc', 1),
(1, N'Bạc xỉu', 32000, NULL, N'Nhiều sữa, ít cà phê', 1),
(1, N'Cappuccino', 45000, NULL, N'Espresso, sữa nóng và bọt sữa', 1),
(2, N'Trà đào cam sả', 42000, NULL, N'Trà đen, đào, cam và sả', 1),
(2, N'Trà vải', 40000, NULL, N'Trà xanh cùng vải', 1),
(2, N'Trà sữa trân châu', 38000, NULL, N'Trà sữa truyền thống', 1),
(3, N'Matcha đá xay', 49000, NULL, N'Matcha, sữa và kem tươi', 1),
(3, N'Chocolate đá xay', 49000, NULL, N'Chocolate đậm vị', 1),
(4, N'Nước ép cam', 35000, NULL, N'Cam tươi vắt', 1),
(4, N'Nước ép dưa hấu', 32000, NULL, N'Dưa hấu ép lạnh', 1),
(4, N'Sinh tố bơ', 45000, NULL, N'Tạm ngưng bán', 0);

INSERT INTO cards(code, status) VALUES ('TABLE-01', 1), ('TABLE-02', 1), ('TABLE-03', 1);

INSERT INTO bills(user_id, card_id, code, created_at, total, status) VALUES
(2, 1, 'HD-SEED-001', DATEADD(DAY, -3, SYSDATETIME()), 83000, 'finish'),
(2, 2, 'HD-SEED-002', DATEADD(DAY, -2, SYSDATETIME()), 126000, 'finish'),
(1, NULL, 'HD-SEED-003', DATEADD(DAY, -1, SYSDATETIME()), 49000, 'finish'),
(2, NULL, 'HD-SEED-004', DATEADD(DAY, -1, SYSDATETIME()), 25000, 'cancel');

INSERT INTO bill_details(bill_id, drink_id, quantity, price) VALUES
(1, 2, 2, 29000), (1, 1, 1, 25000),
(2, 5, 3, 42000),
(3, 8, 1, 49000),
(4, 1, 1, 25000);
GO

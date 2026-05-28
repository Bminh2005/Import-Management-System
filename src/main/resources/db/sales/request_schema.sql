-- Schema cho feature sales/request.
-- Idempotent: chạy lại nhiều lần vẫn an toàn.

CREATE TABLE IF NOT EXISTS sales_request (
    code           VARCHAR(50)  PRIMARY KEY,
    created_date   VARCHAR(20),
    status         VARCHAR(30),
    created_by     VARCHAR(150),
    assigned_to    VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS sales_request_item (
    request_code   VARCHAR(50)  NOT NULL REFERENCES sales_request(code) ON DELETE CASCADE,
    item_code      VARCHAR(50)  NOT NULL,
    name           VARCHAR(200),
    quantity       INTEGER      NOT NULL DEFAULT 0,
    unit           VARCHAR(30),
    delivery_date  VARCHAR(20),
    status         VARCHAR(30),
    position       INTEGER      NOT NULL DEFAULT 0,
    PRIMARY KEY (request_code, item_code)
);

CREATE TABLE IF NOT EXISTS sales_request_rejected_item (
    id             BIGSERIAL    PRIMARY KEY,
    request_code   VARCHAR(50)  NOT NULL REFERENCES sales_request(code) ON DELETE CASCADE,
    item_code      VARCHAR(50)  NOT NULL,
    name           VARCHAR(200),
    quantity       INTEGER      NOT NULL DEFAULT 0,
    unit           VARCHAR(30),
    rejected_by    VARCHAR(30),
    reason         TEXT,
    rejected_date  VARCHAR(20),
    UNIQUE (request_code, item_code)
);

CREATE TABLE IF NOT EXISTS sales_related_order (
    request_code   VARCHAR(50)  NOT NULL REFERENCES sales_request(code) ON DELETE CASCADE,
    code           VARCHAR(50)  NOT NULL,
    order_date     VARCHAR(20),
    site           VARCHAR(200),
    item_count     INTEGER      NOT NULL DEFAULT 0,
    status         VARCHAR(30),
    total_value    BIGINT       NOT NULL DEFAULT 0,
    PRIMARY KEY (request_code, code)
);

-- Seed dữ liệu mẫu (idempotent qua ON CONFLICT).
INSERT INTO sales_request (code, created_date, status, created_by, assigned_to)
VALUES ('REQ-2024-001', '2024-05-08', 'processing',
        'Nguyễn Văn A', 'Trần Thị B - Bộ phận Đặt hàng Quốc tế')
ON CONFLICT (code) DO NOTHING;

INSERT INTO sales_request_item
    (request_code, item_code, name, quantity, unit, delivery_date, status, position)
VALUES
    ('REQ-2024-001', 'MH001', 'Laptop Dell XPS 13',           2, 'cái', '2024-05-15', 'approved', 0),
    ('REQ-2024-001', 'MH002', 'Bàn phím cơ Keychron K2',      3, 'cái', '2024-05-16', 'pending',  1),
    ('REQ-2024-001', 'MH003', 'Chuột Logitech MX Master 3',   1, 'cái', '2024-05-15', 'pending',  2)
ON CONFLICT (request_code, item_code) DO NOTHING;

INSERT INTO sales_request_rejected_item
    (request_code, item_code, name, quantity, unit, rejected_by, reason, rejected_date)
VALUES
    ('REQ-2024-001', 'MH004', 'Màn hình LG UltraWide 34"', 2, 'cái',
     'overseas', 'Không tìm thấy Site có đủ tồn kho', '2024-05-09'),
    ('REQ-2024-001', 'MH005', 'Webcam Logitech C920',      5, 'cái',
     'user',     'Đã đặt hàng từ nguồn khác',           '2024-05-08')
ON CONFLICT (request_code, item_code) DO NOTHING;

INSERT INTO sales_related_order
    (request_code, code, order_date, site, item_count, status, total_value)
VALUES
    ('REQ-2024-001', 'DH-2024-101', '2024-05-09', 'SITE01 - Kho Hong Kong',  2, 'processing', 75000000),
    ('REQ-2024-001', 'DH-2024-102', '2024-05-10', 'SITE02 - Kho Singapore', 1, 'pending',    25000000),
    ('REQ-2024-001', 'DH-2024-103', '2024-05-11', 'SITE03 - Kho Bangkok',   1, 'completed',  30000000)
ON CONFLICT (request_code, code) DO NOTHING;

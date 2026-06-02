-- =========================================================================
-- 1. BẢNG "Users"
-- =========================================================================
INSERT INTO "Users" (username, password, role) VALUES
                                                   ('admin_system', 'hash_pass_999', 'ADMIN'),
                                                   ('sales_hn_01', 'hash_pass_111', 'SALES'),
                                                   ('procurement_01', 'hash_pass_222', 'PROCUREMENT'),
                                                   ('site_manager_hcm', 'hash_pass_333', 'SITE'),
                                                   ('warehouse_main', 'hash_pass_444', 'WAREHOUSE');

-- =========================================================================
-- 2. BẢNG "Merchandise"
-- =========================================================================
INSERT INTO "Merchandise" (merchandise_name, subscription) VALUES
                                                               ('Laptop Dell XPS 13', 'Bảo hành chính hãng 24 tháng'),
                                                               ('iPhone 15 Pro Max 256GB', 'Phiên bản VN/A Quốc tế'),
                                                               ('Màn hình ASUS ProArt 27 inch', 'Độ phân giải 2K chuyên đồ họa'),
                                                               ('Bàn phím cơ Logitech G Pro', 'Bản switch Linear SE');

-- =========================================================================
-- 3. BẢNG "MerchandiseDetail"
-- =========================================================================
INSERT INTO "MerchandiseDetail" (merchandise_id, unit, reference_price) VALUES
                                                                            (1, 'Cái', 35000000),
                                                                            (2, 'Chiếc', 30000000),
                                                                            (2, 'Kiện (10 chiếc)', 290000000),
                                                                            (3, 'Cái', 12500000),
                                                                            (4, 'Chiếc', 2500000);

-- =========================================================================
-- 4. BẢNG "Site"
-- =========================================================================
INSERT INTO "Site" (site_name, site_url, site_address, site_distance, more_info, delivery_by_ship, delivery_by_air) VALUES
                                                                                                                        ('Kho Tổng Miền Bắc', 'https://hn-wh.example.com', 'Số 1 Đống Đa, Hà Nội', 12.5, 'Kho chính trung tâm miền bắc', 3, 1),
                                                                                                                        ('Kho Vệ Tinh Miền Nam', 'https://hcm-wh.example.com', 'Quận 9, TP. Hồ Chí Minh', 1720.0, 'Kho phục vụ bán lẻ phía Nam', 7, 2),
                                                                                                                        ('Chi nhánh Đà Nẵng', 'https://dn-store.example.com', 'Hải Châu, Đà Nẵng', 790.5, 'Cửa hàng trưng bày kiêm kho phụ', 5, 1);

-- =========================================================================
-- 5. BẢNG "ImportRequest"
-- =========================================================================
INSERT INTO "ImportRequest" (user_id, status, desired_date) VALUES
                                                                (2, 'PROCESSED', '2026-06-15'),
                                                                (2, 'PROCESSING', '2026-06-22'),
                                                                (4, 'PENDING', '2026-07-05');

-- =========================================================================
-- 6. BẢNG "RequestDetail" (Đã bỏ trường unit)
-- =========================================================================
INSERT INTO "RequestDetail" (request_id, merchandise_detail_id, quantity) VALUES
                                                                              (1, 1, 10),
                                                                              (1, 3, 5),
                                                                              (2, 4, 15),
                                                                              (3, 5, 8);

-- =========================================================================
-- 7. BẢNG "Order"
-- =========================================================================
INSERT INTO "Order" (site_id, expected_delivery_date, user_id, request_id, status, delivery) VALUES
                                                                                                 (1, '2026-06-12', 3, 1, 'ACCEPTED', 'SHIP'),
                                                                                                 (2, '2026-06-20', 3, 2, 'PROCESSING', 'AIR'),
                                                                                                 (3, '2026-07-01', 3, NULL, 'PENDING', 'SHIP'),
                                                                                                 (1, '2026-05-20', 3, NULL, 'REFUSED', 'AIR');

-- =========================================================================
-- 8. BẢNG "OrderDetail" (Đã bỏ trường unit)
-- =========================================================================
INSERT INTO "OrderDetail" (order_id, merchandise_detail_id, quantity, refused_reason, status) VALUES
                                                                                                  (1, 1, 10, NULL, 'ACCEPTED'),
                                                                                                  (1, 3, 5, NULL, 'ACCEPTED'),
                                                                                                  (2, 4, 15, NULL, 'PROCESSING'),
                                                                                                  (4, 5, 50, 'Vượt quá hạn mức ngân sách phòng ban', 'REFUSED');

-- =========================================================================
-- 9. BẢNG "SiteInventory"
-- =========================================================================
INSERT INTO "SiteInventory" (site_id, merchandise_detail_id, quantity, price) VALUES
                                                                                  (1, 1, 15, 34500000),
                                                                                  (1, 2, 40, 29500000),
                                                                                  (1, 3, 3, 285000000),
                                                                                  (2, 2, 25, 29800000),
                                                                                  (2, 4, 12, 12200000),
                                                                                  (3, 5, 9, 2400000);
DROP TABLE IF EXISTS "OrderDetail" CASCADE;
DROP TABLE IF EXISTS "RequestDetail" CASCADE;
DROP TABLE IF EXISTS "Order" CASCADE;
DROP TABLE IF EXISTS "ImportRequest" CASCADE;
DROP TABLE IF EXISTS "SiteInventory" CASCADE;
DROP TABLE IF EXISTS "MerchandiseDetail" CASCADE;
DROP TABLE IF EXISTS "Merchandise" CASCADE;
DROP TABLE IF EXISTS "Site" CASCADE;
DROP TABLE IF EXISTS "Users" CASCADE;

DROP TYPE IF EXISTS request_status CASCADE;
DROP TYPE IF EXISTS order_status CASCADE;
DROP TYPE IF EXISTS delivery_type CASCADE;
DROP TYPE IF EXISTS user_role CASCADE;

CREATE TYPE request_status AS ENUM ('PENDING', 'PROCESSING', 'PROCESSED');
CREATE TYPE order_status AS ENUM ('PENDING', 'PROCESSING', 'ACCEPTED', 'REFUSED');
CREATE TYPE delivery_type AS ENUM ('SHIP', 'AIR');
CREATE TYPE user_role AS ENUM ('ADMIN', 'SALES', 'PROCUREMENT', 'SITE', 'WAREHOUSE');

CREATE TABLE "ImportRequest" (
                                 id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
                                 created_at timestamp with time zone NOT NULL DEFAULT now(),
                                 user_id bigint NOT NULL,
                                 status request_status DEFAULT 'PENDING'::request_status,
                                 desired_date date,
                                 CONSTRAINT ImportRequest_pkey PRIMARY KEY (id)
);

CREATE TABLE "Merchandise" (
                               id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
                               created_at timestamp with time zone NOT NULL DEFAULT now(),
                               merchandise_name text,
                               subscription text,
                               CONSTRAINT Merchandise_pkey PRIMARY KEY (id)
);

CREATE TABLE "MerchandiseDetail" (
                                     id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
                                     merchandise_id bigint,
                                     unit text,
                                     reference_price numeric,
                                     CONSTRAINT MerchandiseDetail_pkey PRIMARY KEY (id),
                                     CONSTRAINT merchandisedetail_merchandise_id_fkey FOREIGN KEY (merchandise_id) REFERENCES "Merchandise"(id)
);

CREATE TABLE "Order" (
                         id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
                         created_at timestamp with time zone NOT NULL DEFAULT now(),
                         site_id bigint,
                         expected_delivery_date date,
                         user_id bigint NOT NULL,
                         request_id bigint,
                         status order_status DEFAULT 'PENDING'::order_status,
                         delivery delivery_type,
                         CONSTRAINT Order_pkey PRIMARY KEY (id),
                         CONSTRAINT order_request_id_fkey FOREIGN KEY (request_id) REFERENCES "ImportRequest"(id)
);

CREATE TABLE "OrderDetail" (
                               id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
                               order_id bigint,
                               merchandise_detail_id bigint,
                               quantity bigint,
                               refused_reason text,
                               status order_status,
                               CONSTRAINT OrderDetail_pkey PRIMARY KEY (id),
                               CONSTRAINT orderdetail_order_id_fkey FOREIGN KEY (order_id) REFERENCES "Order"(id),
                               CONSTRAINT orderdetail_merchandise_detail_id_fkey FOREIGN KEY (merchandise_detail_id) REFERENCES "MerchandiseDetail"(id)
);

CREATE TABLE "RequestDetail" (
                                 id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
                                 quantity bigint,
                                 request_id bigint,
                                 merchandise_detail_id bigint,
                                 CONSTRAINT RequestDetail_pkey PRIMARY KEY (id),
                                 CONSTRAINT RequestDetail_merchandise_detail_id_fkey FOREIGN KEY (merchandise_detail_id) REFERENCES "MerchandiseDetail"(id),
                                 CONSTRAINT detailrequest_request_id_fkey FOREIGN KEY (request_id) REFERENCES "ImportRequest"(id)
);

CREATE TABLE "Site" (
                        id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
                        created_at timestamp with time zone NOT NULL DEFAULT now(),
                        site_name text,
                        site_url text,
                        site_address text,
                        site_distance numeric,
                        more_info text,
                        delivery_by_ship bigint,
                        delivery_by_air bigint,
                        CONSTRAINT Site_pkey PRIMARY KEY (id)
);

CREATE TABLE "SiteInventory" (
                                 id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
                                 created_at timestamp with time zone NOT NULL DEFAULT now(),
                                 site_id bigint NOT NULL,
                                 merchandise_detail_id bigint,
                                 quantity bigint,
                                 price numeric,
                                 CONSTRAINT SiteInventory_pkey PRIMARY KEY (id),
                                 CONSTRAINT SiteInventory_merchandise_detail_id_fkey FOREIGN KEY (merchandise_detail_id) REFERENCES "MerchandiseDetail"(id)
);

CREATE TABLE "Users" (
                         id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
                         created_at timestamp with time zone NOT NULL DEFAULT now(),
                         username text NOT NULL UNIQUE,
                         password text NOT NULL,
                         role user_role NOT NULL,
                         CONSTRAINT Users_pkey PRIMARY KEY (id)
);

-- Thêm các ràng buộc khóa ngoại vòng chéo sau khi các bảng đã khởi tạo xong
ALTER TABLE "ImportRequest" ADD CONSTRAINT importrequest_user_id_fkey FOREIGN KEY (user_id) REFERENCES "Users"(id);
ALTER TABLE "Order" ADD CONSTRAINT order_site_id_fkey FOREIGN KEY (site_id) REFERENCES "Site"(id);
ALTER TABLE "Order" ADD CONSTRAINT order_user_id_fkey FOREIGN KEY (user_id) REFERENCES "Users"(id);
ALTER TABLE "SiteInventory" ADD CONSTRAINT siteinventory_site_id_fkey FOREIGN KEY (site_id) REFERENCES "Site"(id);
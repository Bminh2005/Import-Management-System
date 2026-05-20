# Project Structure

## Overview

Project được tổ chức theo kiến trúc:

```text id="x80zns"
Module -> Feature -> Layer
```

Ví dụ:

```text id="mt9od4"
modules -> sales -> request -> service
```

Trong đó:

* **Module**: nhóm chức năng lớn theo actor/nghiệp vụ
* **Feature**: chức năng cụ thể trong module
* **Layer**: tầng xử lý bên trong feature

---

# Root Structure

```text id="7qmc91"
com.importorder.system
│
├── app/                            # Khởi động và quản lý ứng dụng
│
├── common/                         # Thành phần dùng chung
│
├── infrastructure/                 # Hạ tầng hệ thống
│   ├── database/
│   ├── notification/
│   └── payment/
│
├── auth/                           # Authentication & Authorization
│
├── modules/                        # Business modules
│   ├── sales/
│   ├── procurement/
│   ├── warehouse/
│   ├── site/
│   └── admin/
│
└── resources/
```

---

# Modules

## sales/

Bộ phận bán hàng.

Ví dụ chức năng:

```text id="bj3k5q"
sales/
├── dashboard/
├── product/
└── request/
```

---

## procurement/

Bộ phận đặt hàng quốc tế.

Ví dụ:

```text id="h0txef"
procurement/
├── supplier/
├── importorder/
└── tracking/
```

---

## warehouse/

Bộ phận quản lý kho.

Ví dụ:

```text id="r6s3w8"
warehouse/
├── inventory/
├── inbound/
└── outbound/
```

---

## site/

Website/client cho người dùng.

Ví dụ:

```text id="up9bkm"
site/
├── home/
├── cart/
├── product/
└── profile/
```

---

## admin/

Quản trị hệ thống.

Ví dụ:

```text id="y9qzlw"
admin/
├── account/
├── role/
├── permission/
└── auditlog/
```

---

# Feature Structure

Mỗi feature sẽ được tổ chức theo các layer:

```text id="2z3e9o"
feature/
├── ui/
├── service/
├── repository/
├── dto/
└── entity/
```

---

# Layer Responsibilities

## ui/

Chứa:

* JavaFX pages
* FXML files
* JavaFX controllers
* custom UI components
* dialogs

Ví dụ:

```text id="ql55yb"
RequestListPage.fxml
RequestListController.java
```

---

## service/

Chứa business logic.

Ví dụ:

```java id="k3gzt1"
RequestService.java
```

Nhiệm vụ:

* xử lý nghiệp vụ
* validate dữ liệu
* điều phối workflow
* gọi repository

---

## repository/

Chứa logic truy cập database.

Ví dụ:

```java id="gshbrv"
RequestRepository.java
```

Nhiệm vụ:

* SELECT
* INSERT
* UPDATE
* DELETE

---

## dto/

Chứa các object truyền dữ liệu giữa các layer.

Ví dụ:

```java id="mb6km6"
CreateRequestDTO.java
RequestResponse.java
```

---

## entity/

Chứa entity/model đại diện dữ liệu hệ thống.

Ví dụ:

```java id="tijz6r"
Request.java
```

---

# Example Feature Structure

Ví dụ feature `sales/request`:

```text id="rg2ol7"
modules/
└── sales/
    └── request/
        │
        ├── ui/
        │   ├── RequestListPage.fxml
        │   ├── RequestListController.java
        │   │
        │   ├── CreateRequestPage.fxml
        │   ├── CreateRequestController.java
        │   │
        │   ├── RequestDetailPage.fxml
        │   └── RequestDetailController.java
        │
        ├── service/
        │   └── RequestService.java
        │
        ├── repository/
        │   └── RequestRepository.java
        │
        ├── dto/
        │   ├── CreateRequestDTO.java
        │   ├── UpdateRequestDTO.java
        │   └── RequestResponse.java
        │
        └── entity/
            └── Request.java
```

---

# Application Flow

Luồng xử lý cơ bản:

```text id="zjlwmv"
UI
 ↓
Controller
 ↓
Service
 ↓
Repository
 ↓
Database
```

---

# Naming Convention

## UI

```text id="jj3y5j"
ProductListPage.fxml
CreateRequestPage.fxml
```

---

## Controller

```text id="2nb7ef"
ProductListController.java
RequestDetailController.java
```

---

## Service

```text id="x0w6ye"
ProductService.java
RequestService.java
```

---

## Repository

```text id="fgnm8f"
ProductRepository.java
RequestRepository.java
```

---

## DTO

```text id="2mq8m4"
CreateRequestDTO.java
UpdateProductDTO.java
ProductResponse.java
```

---

# Design Principles

## 1. Feature-based Architecture

Mỗi feature tự chứa toàn bộ logic liên quan.

---

## 2. Separation of Concerns

Mỗi layer chỉ chịu trách nhiệm một nhiệm vụ cụ thể.

---

## 3. Scalability

Dễ mở rộng khi project lớn hơn.

---

## 4. Maintainability

Dễ đọc, dễ maintain và dễ debug.

---

# Important Notes

* Không viết SQL trong UI Controller
* Không xử lý business logic trong Repository
* UI chỉ gọi Service
* Repository chỉ xử lý database
* DTO dùng để truyền dữ liệu giữa các layer
* Shared component nên đặt trong `common/ui/`

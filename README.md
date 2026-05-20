# Project Structure

## Overview

Project được tổ chức theo kiến trúc:

```text
Actor -> Feature -> Layer
```

Ví dụ:

```text
sales -> request -> service
```

Trong đó:

* **Actor**: bộ phận/người dùng của hệ thống
* **Feature**: chức năng cụ thể của actor
* **Layer**: tầng xử lý bên trong feature

---

# Project Structure

```text
com.importorder.system
│
├── app/                    # Khởi động ứng dụng
├── common/                 # Thành phần dùng chung
├── database/               # Database subsystem
├── auth/                   # Authentication subsystem
│
├── sales/                  # Bộ phận bán hàng
├── procurement/            # Bộ phận đặt hàng quốc tế
├── warehouse/              # Bộ phận quản lý kho
├── site/                   # Website/client
├── admin/                  # Quản trị hệ thống
│
├── notification/           # Hệ thống thông báo
└── payment/                # Hệ thống thanh toán
```

---

# Actor Structure

Mỗi actor được chia thành nhiều feature.

Ví dụ:

```text
sales/
├── dashboard/
├── product/
└── request/
```

---

# Feature Structure

Mỗi feature sẽ chứa đầy đủ các layer:

```text
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

Ví dụ:

```text
RequestListPage.fxml
RequestListController.java
```

---

## service/

Chứa business logic.

Ví dụ:

```java
RequestService.java
```

Nhiệm vụ:

* validate dữ liệu
* xử lý nghiệp vụ
* gọi repository
* điều phối workflow

---

## repository/

Chứa logic truy cập database.

Ví dụ:

```java
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

```java
CreateRequestDTO.java
RequestResponse.java
```

---

## entity/

Chứa model/entity đại diện cho dữ liệu hệ thống.

Ví dụ:

```java
Request.java
```

---

# Example Feature Structure

Ví dụ feature `sales/request`:

```text
sales/
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

# Flow Architecture

Luồng xử lý cơ bản:

```text
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

```text
ProductListPage.fxml
CreateRequestPage.fxml
```

---

## Controller

```text
ProductListController.java
RequestDetailController.java
```

---

## Service

```text
ProductService.java
RequestService.java
```

---

## Repository

```text
ProductRepository.java
RequestRepository.java
```

---

## DTO

```text
CreateRequestDTO.java
UpdateProductDTO.java
ProductResponse.java
```

---

# Design Principles

## 1. Feature-based Architecture

Mỗi feature độc lập và tự chứa toàn bộ logic liên quan.

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

# Notes

* Không viết SQL trong UI Controller
* Không xử lý business logic trong Repository
* UI chỉ gọi Service
* Repository chỉ xử lý database
* DTO dùng để truyền dữ liệu giữa các layer

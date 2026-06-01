# Sales Department Application - JavaFX
## Ứng dụng Bộ phận Bán hàng

Ứng dụng JavaFX hoàn chỉnh cho Bộ phận Bán hàng với đầy đủ chức năng quản lý mặt hàng và tạo yêu cầu nhập hàng.

---

## 🎯 Tính năng

### 1. **Dashboard (Trang chủ)**
- ✅ 4 Metric cards: Tổng mặt hàng, Yêu cầu chờ, Tháng này, Đã duyệt
- ✅ Quick actions: Tạo yêu cầu, Thêm hàng, Xem tất cả
- ✅ Bảng yêu cầu gần đây với status badges

### 2. **Quản lý Mặt hàng**
- ✅ Table view với search realtime
- ✅ Expandable details panel (bên phải)
- ✅ Xem chi tiết: Giá, Nhà cung cấp, Mô tả
- ✅ Actions: View, Edit, Delete
- ✅ 10 mặt hàng mẫu (Laptop, Chuột, Bàn phím...)

### 3. **Yêu cầu Nhập hàng** ⭐
- ✅ Table view với filters
- ✅ Status badges (Draft, Pending, Processing, Completed)
- ✅ Actions: View, Send (draft only), Edit (draft only)

### 4. **Tạo Yêu cầu Mới** ⭐⭐⭐ (Chức năng chính)

**Dialog chọn mặt hàng:**
- ✅ Search box (tìm theo tên, mã)
- ✅ Category filter dropdown
- ✅ Result counter: "Hiển thị X trong Y mặt hàng"
- ✅ Full table với: Mã, Tên, Danh mục, Đơn vị, Giá
- ✅ Info button (ℹ️) để xem chi tiết
- ✅ Details side panel: Nhà cung cấp, Mô tả đầy đủ
- ✅ Select button để chọn

**Request item cards:**
- ✅ Badge số thứ tự (1, 2, 3...)
- ✅ Hiển thị: Tên hàng, Mã, Danh mục, Giá
- ✅ Input: Số lượng, Đơn vị (auto), Ngày nhận
- ✅ Nút "Đổi mặt hàng" để chọn lại
- ✅ Nút Delete để xóa item
- ✅ Empty state khi chưa có hàng

**Bottom actions:**
- ✅ Counter: "Tổng: X mặt hàng"
- ✅ Lưu nháp (status = draft)
- ✅ Gửi yêu cầu (status = pending)
- ✅ Validation: Phải có đủ số lượng + ngày

### 5. **Layout & UI**
- ✅ Sidebar xanh (#1E3A8A) với navigation
- ✅ Header: Search, Language switcher, Notifications, Avatar
- ✅ Responsive content area
- ✅ CSS styling giống React app (Tailwind-like)

### 6. **I18n (Đa ngôn ngữ)**
- ✅ Tiếng Việt (default)
- ✅ English
- ✅ ComboBox switch ngôn ngữ
- ✅ Reload toàn bộ UI khi đổi

---

## 📂 Cấu trúc Project

```
javafx-sample/
├── src/main/
│   ├── java/com/importassignment/
│   │   ├── SalesMainApp.java                    # Main entry point
│   │   ├── models/
│   │   │   ├── Merchandise.java                 # Model mặt hàng
│   │   │   ├── ImportRequest.java               # Model yêu cầu
│   │   │   └── RequestItem.java                 # Mặt hàng trong yêu cầu
│   │   ├── services/
│   │   │   └── MockDataService.java             # 10 mặt hàng + 4 requests mẫu
│   │   └── controllers/
│   │       ├── SalesMainLayoutController.java   # Main layout + sidebar
│   │       ├── SalesDashboardController.java    # Dashboard
│   │       ├── MerchandiseManagementController.java
│   │       ├── MerchandiseSelectionDialogController.java  # ⭐ Chọn hàng
│   │       ├── CreateImportRequestDialogController.java   # ⭐ Tạo yêu cầu
│   │       └── ImportRequestsViewController.java
│   │
│   └── resources/
│       ├── fxml/
│       │   ├── SalesMainLayout.fxml             # Main layout
│       │   ├── SalesDashboard.fxml
│       │   ├── MerchandiseManagement.fxml
│       │   ├── MerchandiseSelectionDialog.fxml  # ⭐ Search/filter dialog
│       │   ├── CreateImportRequestDialog.fxml   # ⭐ Create request
│       │   └── ImportRequestsView.fxml
│       │
│       ├── css/
│       │   ├── sales-application.css            # Main styles
│       │   └── sales-layout.css                 # Sidebar styles
│       │
│       └── i18n/
│           ├── messages_vi_VN.properties        # Tiếng Việt (130+ keys)
│           └── messages_en_US.properties        # English
│
└── pom.xml
```

---

## 🚀 Cách chạy

### Yêu cầu:
- **Java 17+**
- **Maven 3.6+**
- **JavaFX 17+** (Maven sẽ tự download)

### Bước 1: Compile

```bash
cd javafx-sample
mvn clean compile
```

### Bước 2: Chạy

```bash
mvn javafx:run -Dexec.mainClass="com.importassignment.SalesMainApp"
```

Hoặc trong IntelliJ IDEA:
1. Right-click `SalesMainApp.java`
2. Run 'SalesMainApp.main()'

---

## 🎨 Design System

### Colors:
- **Primary**: `#2563EB` (Blue)
- **Secondary**: `#10B981` (Green)
- **Accent**: `#F59E0B` (Orange)
- **Danger**: `#EF4444` (Red)
- **Sidebar**: `#1E3A8A` (Dark Blue)

### Status Badges:
- **Draft**: Gray (`#F3F4F6` / `#1F2937`)
- **Pending**: Yellow (`#FEF3C7` / `#92400E`)
- **Processing**: Blue (`#DBEAFE` / `#1E40AF`)
- **Completed**: Green (`#D1FAE5` / `#065F46`)
- **Cancelled**: Red (`#FEE2E2` / `#991B1B`)

### Typography:
- **Font**: Segoe UI, Roboto
- **Page Title**: 28px Bold
- **Section Title**: 20px Semi-bold
- **Body**: 14px Regular

---

## 📋 Use Cases

### Use Case 1: Tạo Yêu cầu Nhập hàng Mới

**Luồng chính:**
1. Click "Dashboard" → "+ Tạo Yêu cầu Nhập hàng"
2. Hoặc "Yêu cầu Nhập hàng" → "+ Tạo Yêu cầu Mới"
3. Dialog mở → Click "+ Thêm Mặt hàng"
4. **Merchandise Selection Dialog** hiện:
   - Search: "laptop" → Tìm thấy "Laptop Dell XPS 13"
   - Click ℹ️ → Xem chi tiết bên phải
   - Đọc: Giá, Nhà cung cấp, Mô tả
   - Click "Chọn"
5. **Request Item Card** xuất hiện:
   - Badge: "1"
   - Tên: "Laptop Dell XPS 13"
   - Input số lượng: "5"
   - Đơn vị: "cái" (auto)
   - Chọn ngày: "2024-06-01"
6. Click "+ Thêm Mặt hàng" → Chọn thêm "Chuột Logitech"
7. Counter: "Tổng: 2 mặt hàng"
8. Click "Gửi Yêu cầu" → Status = "pending"
9. Success dialog → Đóng
10. Refresh table → Thấy yêu cầu mới

**Luồng phụ (Search & Filter):**
- Search: "key" → Tìm "Bàn phím"
- Filter: "Phụ kiện" → Chỉ show Chuột, Bàn phím, USB Hub...
- Kết hợp: Search "usb" + Filter "Phụ kiện" → "USB-C Hub"

---

## 🧪 Test Scenarios

### Test 1: Search hoạt động
1. Mở Merchandise Selection Dialog
2. Gõ "dell" → Thấy "Laptop Dell XPS 13"
3. Gõ "sony" → Thấy "Tai nghe Sony" (nhưng inactive)
4. Gõ "xyz" → "No merchandise found"

### Test 2: Filter hoạt động
1. Chọn "Điện tử" → Thấy 3 items (Laptop, Màn hình, Tai nghe)
2. Chọn "Phụ kiện" → Thấy 5 items
3. Chọn "Linh kiện" → Thấy 2 items (SSD, RAM)

### Test 3: Details panel
1. Click ℹ️ trên Laptop
2. Side panel hiện với:
   - Mã: MH001
   - Nhà cung cấp: Dell Vietnam
   - Mô tả: "Laptop cao cấp, màn hình..."
3. Click ℹ️ khác → Panel update

### Test 4: Validation
1. Tạo request → Thêm hàng → Không điền số lượng
2. Click "Gửi" → Error: "Vui lòng điền đầy đủ..."
3. Điền số lượng → Không chọn ngày
4. Click "Gửi" → Error
5. Chọn ngày → Click "Gửi" → Success!

### Test 5: Đổi ngôn ngữ
1. ComboBox: "Tiếng Việt" → "English"
2. Tất cả text đổi:
   - "Trang Chủ" → "Dashboard"
   - "Quản lý Mặt hàng" → "Merchandise Management"
   - "Mã Hàng" → "Code"
3. Mở dialog → Text cũng đổi
4. Switch lại → Về Tiếng Việt

---

## 💡 Features Highlight

### 1. **Smart Search**
- Realtime filtering
- Search cả tên và mã
- Case-insensitive
- No lag với 10+ items

### 2. **Category Filter**
- Dynamic từ data
- "Tất cả danh mục" option
- Combine với search
- Result counter update

### 3. **Expandable Details**
- Click anywhere → Show details
- Side panel smooth
- Scroll nếu dài
- Close button

### 4. **Request Item Cards**
- Badge số thứ tự
- Info grid: Danh mục, Giá
- Change merchandise
- Delete với confirm
- Auto re-number khi xóa

### 5. **Empty States**
- "Chưa có mặt hàng..." với icon
- "Không tìm thấy..." khi search rỗng
- Disable buttons khi empty

---

## 🐛 Known Issues / Todo

- [ ] Edit merchandise chưa implement
- [ ] View request details chưa có dialog
- [ ] Pagination chưa có (hiện tại < 20 items OK)
- [ ] Export to Excel chưa có
- [ ] Print request chưa có

---

## 📞 Support

**Nếu gặp lỗi:**

1. **"JavaFX runtime not found"**
   - Maven sẽ tự download
   - Hoặc download manual: https://gluonhq.com/products/javafx/

2. **"Cannot find bundle"**
   - Check: `target/classes/i18n/messages_vi_VN.properties`
   - Run: `mvn clean compile`

3. **"NullPointerException"**
   - Check FXML fx:id match controller @FXML fields
   - Check resource paths (bắt đầu bằng `/`)

4. **Encoding lỗi (tiếng Việt)**
   - File properties phải UTF-8
   - IDE settings → File Encodings → UTF-8

---

## ✨ So sánh với React App

| Feature | React App | JavaFX App | Status |
|---------|-----------|------------|--------|
| Sidebar collapse | ✅ | ❌ (static) | Có thể thêm |
| Search realtime | ✅ | ✅ | ✅ Done |
| Category filter | ✅ | ✅ | ✅ Done |
| Details panel | ✅ | ✅ | ✅ Done |
| Request cards | ✅ | ✅ | ✅ Done |
| Empty states | ✅ | ✅ | ✅ Done |
| Status badges | ✅ | ✅ | ✅ Done |
| I18n | ✅ | ✅ | ✅ Done |
| Metrics cards | ✅ | ✅ | ✅ Done |
| Keyboard shortcuts | ✅ | ⚠️ (partial) | Sidebar buttons only |

---

**🎉 Application hoàn chỉnh và sẵn sàng chạy!**

Chỉ cần:
1. Download project về máy
2. `mvn clean compile`
3. `mvn javafx:run -Dexec.mainClass="com.importassignment.SalesMainApp"`
4. Enjoy! 🚀

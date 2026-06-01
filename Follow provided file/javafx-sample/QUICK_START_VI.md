# Hướng dẫn Chạy Nhanh - JavaFX Import Assignment System

## 🚀 Chạy trong 3 bước

### Bước 1: Kiểm tra Đa ngôn ngữ

```bash
# Compile test class
javac -cp src/main/resources -d bin TEST_I18N.java

# Run test
java -cp bin:src/main/resources TEST_I18N
```

**Kết quả mong đợi:**
```
TEST TIẾNG VIỆT:
✅ File loaded successfully!
- app.title = Hệ thống Đặt hàng Nhập khẩu
- menu.dashboard = Trang Chủ
...

TEST ENGLISH:
✅ File loaded successfully!
- app.title = Import Assignment System
- menu.dashboard = Dashboard
...
```

### Bước 2: Compile Project

**Với Maven:**
```bash
cd javafx-sample
mvn clean compile
```

**Hoặc thủ công:**
```bash
# Tạo thư mục output
mkdir -p bin

# Compile với JavaFX
javac --module-path /path/to/javafx-sdk/lib \
      --add-modules javafx.controls,javafx.fxml \
      -d bin \
      -cp src/main/resources \
      src/main/java/com/importassignment/**/*.java
```

### Bước 3: Chạy Application

**Với Maven (Khuyên dùng):**
```bash
mvn javafx:run
```

**Hoặc trực tiếp:**
```bash
java --module-path /path/to/javafx-sdk/lib \
     --add-modules javafx.controls,javafx.fxml \
     -cp bin:src/main/resources \
     com.importassignment.MainApp
```

**Với IDE (IntelliJ IDEA):**
1. Open project: `File > Open` → chọn folder `javafx-sample`
2. Import as Maven project
3. Right-click `MainApp.java` → `Run 'MainApp.main()'`
4. Nếu lỗi "JavaFX runtime not found":
   - `Run > Edit Configurations`
   - VM Options: `--module-path "path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml`

## 🌍 Test Chuyển Đổi Ngôn Ngữ

### Sau khi app chạy:

1. **Mặc định**: App khởi động bằng **Tiếng Việt**
   - Window title: "Hệ thống Đặt hàng Nhập khẩu"
   - Dashboard title: "Trang Chủ"
   - Menu items: "Trang Chủ", "Quản lý Đơn hàng", etc.

2. **Chuyển sang English**:
   - Click ComboBox ở góc phải header (hiện "Tiếng Việt")
   - Chọn "English"
   - UI reload tự động
   - Kiểm tra: Window title → "Import Assignment System"

3. **Chuyển lại Tiếng Việt**:
   - Click ComboBox (hiện "English")
   - Chọn "Tiếng Việt"
   - UI reload về Tiếng Việt

### ✅ Kiểm tra các phần sau khi đổi ngôn ngữ:

**Tiếng Việt → English:**
- [ ] Window title: "Hệ thống Đặt hàng Nhập khẩu" → "Import Assignment System"
- [ ] Sidebar: "Trang Chủ" → "Dashboard"
- [ ] Sidebar: "Quản lý Đơn hàng" → "Orders Management"
- [ ] Search placeholder: "Tìm kiếm" → "Search"
- [ ] Dashboard subtitle: "Chào mừng trở lại..." → "Welcome back..."
- [ ] Quick Actions: "Tạo Đơn hàng Mới" → "Create New Order"
- [ ] Table header: "Mã Đơn hàng" → "Order ID"
- [ ] Status: "Chờ Xét duyệt" → "Pending Review"

## 🐛 Nếu Gặp Lỗi

### Lỗi 1: "Cannot find messages_vi_VN.properties"

**Nguyên nhân**: Properties files không được copy vào classpath

**Giải pháp**:
```bash
# Với Maven
mvn clean compile

# Kiểm tra files đã được copy
ls target/classes/i18n/
# Phải thấy: messages_vi_VN.properties, messages_en_US.properties
```

### Lỗi 2: Chuyển ngôn ngữ không có gì thay đổi

**Nguyên nhân**: Event handler không được trigger

**Giải pháp**:
1. Kiểm tra console có log không
2. Thêm debug vào `MainLayoutController.onLanguageChange()`:
```java
@FXML
private void onLanguageChange() {
    System.out.println("Language change triggered!");
    String selected = languageComboBox.getValue();
    System.out.println("Selected: " + selected);
    // ... rest of code
}
```

### Lỗi 3: "JavaFX runtime components are missing"

**Giải pháp**: Thêm VM options
```
--module-path "C:\javafx-sdk-17\lib" --add-modules javafx.controls,javafx.fxml
```

## 📱 Demo Screenshots (Mô tả)

### Màn hình khởi động (Tiếng Việt):
```
┌─────────────────────────────────────────────────────┐
│  🌍 Import System                                    │
│      Assignment                                      │
├─────────────────────────────────────────────────────┤
│  🏠  Trang Chủ                    (Ctrl+1)          │
│  🛒  Quản lý Đơn hàng             (Ctrl+2)          │
│  🌍  Quản lý Site                 (Ctrl+3)          │
│  📦  Kho hàng                     (Ctrl+4)          │
│  📊  Báo cáo & Phân tích          (Ctrl+5)          │
│  ⚙️   Cài đặt                     (Ctrl+,)          │
├─────────────────────────────────────────────────────┤
│  Vai trò hiện tại:                                  │
│  Bộ phận Bán hàng                                   │
└─────────────────────────────────────────────────────┘
```

### Sau khi chuyển sang English:
```
┌─────────────────────────────────────────────────────┐
│  🌍 Import System                                    │
│      Assignment                                      │
├─────────────────────────────────────────────────────┤
│  🏠  Dashboard                     (Ctrl+1)          │
│  🛒  Orders Management             (Ctrl+2)          │
│  🌍  Site Management               (Ctrl+3)          │
│  📦  Warehouse                     (Ctrl+4)          │
│  📊  Reports & Analytics           (Ctrl+5)          │
│  ⚙️   Settings                     (Ctrl+,)          │
├─────────────────────────────────────────────────────┤
│  Current Role:                                       │
│  Sales Department                                    │
└─────────────────────────────────────────────────────┘
```

## 💡 Tips

1. **ComboBox không hiển thị?**
   - Kiểm tra header bar có load không
   - Refresh UI: Resize window

2. **Text bị cut off?**
   - Resize window lớn hơn
   - Hoặc set minimum width trong code

3. **Muốn default English?**
   ```java
   // Trong MainApp.java
   private static Locale currentLocale = new Locale("en", "US"); // Thay "vi", "VN"
   ```

4. **Thêm log để debug:**
   ```java
   // Trong MainApp.changeLanguage()
   System.out.println("Changing to: " + locale);
   System.out.println("New title: " + resourceBundle.getString("app.title"));
   ```

## 📞 Hỗ trợ

Nếu vẫn không được, kiểm tra:
1. ✅ Java version: `java -version` (phải ≥ 17)
2. ✅ JavaFX installed
3. ✅ Maven working: `mvn -version`
4. ✅ Properties files exist: `ls src/main/resources/i18n/`
5. ✅ Run TEST_I18N.java trước

---
**Chúc may mắn!** 🎉

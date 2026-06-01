# Hướng dẫn Khắc phục Lỗi - JavaFX Import Assignment System

## ❌ Lỗi: Không chuyển đổi được ngôn ngữ

### Nguyên nhân:
1. ResourceBundle không được reload đúng cách
2. Views đã load không tự động update
3. ComboBox không trigger event đúng

### ✅ Giải pháp đã áp dụng:

**1. Sửa MainApp.changeLanguage():**
```java
public static void changeLanguage(Locale locale) {
    currentLocale = locale;
    resourceBundle = ResourceBundle.getBundle("i18n.messages", currentLocale);
    
    // Reload toàn bộ UI
    FXMLLoader loader = new FXMLLoader(
        MainApp.class.getResource("/fxml/MainLayout.fxml")
    );
    loader.setResources(resourceBundle);
    Parent root = loader.load();
    
    // Update scene
    Scene scene = primaryStage.getScene();
    scene.setRoot(root);
    
    // Reapply CSS
    scene.getStylesheets().clear();
    scene.getStylesheets().add(
        MainApp.class.getResource("/css/application.css").toExternalForm()
    );
    
    // Update title
    primaryStage.setTitle(resourceBundle.getString("app.title"));
}
```

**2. Cập nhật MainLayoutController:**
- ComboBox value được set dựa trên locale hiện tại
- Khi chuyển đổi, toàn bộ UI được reload

**3. Fix SidebarController:**
- Navigation menu được rebuild với ResourceBundle mới
- Role label update theo ngôn ngữ

### 🧪 Cách Test:

**Test 1: Chuyển từ Tiếng Việt sang English**
1. Chạy app (mặc định Tiếng Việt)
2. Click ComboBox ở header
3. Chọn "English"
4. ✅ Kiểm tra: Tất cả text đổi sang English

**Test 2: Chuyển ngược lại**
1. Từ English, click ComboBox
2. Chọn "Tiếng Việt"
3. ✅ Kiểm tra: Tất cả text về Tiếng Việt

### 📝 Kiểm tra các thành phần:

**Phải đổi ngôn ngữ:**
- ✅ Window title
- ✅ Sidebar menu items
- ✅ Role badge
- ✅ Header (search placeholder)
- ✅ Dashboard title & subtitle
- ✅ Quick action buttons
- ✅ Table column headers
- ✅ Status badges (text)
- ✅ Filter chips
- ✅ Context menu items

### 🔍 Debug Steps nếu vẫn lỗi:

**Bước 1: Kiểm tra properties files**
```bash
# Đảm bảo 2 files tồn tại:
src/main/resources/i18n/messages_vi_VN.properties
src/main/resources/i18n/messages_en_US.properties
```

**Bước 2: Kiểm tra encoding**
- Properties files phải là UTF-8
- Ký tự Tiếng Việt phải hiển thị đúng

**Bước 3: Rebuild project**
```bash
mvn clean compile
```

**Bước 4: Check console output**
```java
System.out.println("Current Locale: " + resourceBundle.getLocale());
System.out.println("Test Key: " + resourceBundle.getString("menu.dashboard"));
```

### 🐛 Các lỗi phổ biến khác:

**1. MissingResourceException**
```
Lỗi: Can't find bundle for base name i18n.messages
```
**Giải pháp:**
- Kiểm tra đường dẫn: `resources/i18n/messages_vi_VN.properties`
- Đảm bảo Maven đã copy resources vào target/classes

**2. Text hiển thị ???key???**
```
Hiển thị: ???menu.dashboard???
```
**Giải pháp:**
- Key không tồn tại trong properties file
- Kiểm tra spelling của key
- Thêm key vào cả 2 files (vi & en)

**3. Ký tự Tiếng Việt bị lỗi (Ã¡, Ã©)**
```
Hiển thị: Äá»n hÃ ng thay vì Đơn hàng
```
**Giải pháp:**
- Chuyển encoding sang UTF-8
- Hoặc dùng unicode escape: `Đơn hàng`

### 💡 Tips:

**Native2ASCII (nếu cần):**
```bash
# Convert Vietnamese to Unicode escape
native2ascii -encoding UTF-8 messages_vi_VN.properties messages_vi_VN_escaped.properties
```

**Properties Editor trong IDE:**
- IntelliJ IDEA: File > Settings > Editor > File Encodings
  - Set "Properties Files" to UTF-8
  - Enable "Transparent native-to-ascii conversion"

- Eclipse: Install "Properties Editor" plugin
  - Auto converts to Unicode escape

### 📞 Nếu vẫn không được:

**1. Xóa cache:**
```bash
mvn clean
rm -rf target/
```

**2. Hardcode test:**
```java
// Trong MainLayoutController.initialize()
System.out.println("=== LANGUAGE TEST ===");
System.out.println("Locale: " + resources.getLocale());
System.out.println("Dashboard (VI): " + resources.getString("dashboard.title"));

// Thử load trực tiếp
ResourceBundle testBundle = ResourceBundle.getBundle(
    "i18n.messages", 
    new Locale("vi", "VN")
);
System.out.println("Test VI: " + testBundle.getString("dashboard.title"));

ResourceBundle testBundleEN = ResourceBundle.getBundle(
    "i18n.messages", 
    new Locale("en", "US")
);
System.out.println("Test EN: " + testBundleEN.getString("dashboard.title"));
```

**3. Kiểm tra path:**
```java
// Print classpath
System.out.println(getClass().getResource("/i18n/messages_vi_VN.properties"));
```

### ✅ Checklist hoàn chỉnh:

- [ ] Properties files tồn tại đúng vị trí
- [ ] Encoding là UTF-8
- [ ] Tất cả keys có trong cả 2 files
- [ ] Maven đã compile resources
- [ ] ComboBox có options "Tiếng Việt" và "English"
- [ ] onLanguageChange() được trigger khi chọn
- [ ] MainApp.changeLanguage() được gọi đúng
- [ ] UI reload sau khi đổi locale
- [ ] Console không có error

---

**Lưu ý**: Sau khi sửa code, nhớ:
1. Clean & Rebuild project
2. Restart application
3. Test cả 2 chiều: VI ↔ EN

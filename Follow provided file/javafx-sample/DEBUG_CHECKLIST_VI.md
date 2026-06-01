# Debug Checklist - Không Chuyển Được Ngôn Ngữ

## 🔍 Bước 1: Kiểm tra Properties Files

### 1.1 Files có tồn tại không?

```bash
cd javafx-sample
ls -la src/main/resources/i18n/
```

**Kết quả mong đợi:**
```
messages_vi_VN.properties
messages_en_US.properties
```

**❌ Nếu không thấy files:**
- Files bị đặt sai vị trí
- Tạo lại thư mục: `mkdir -p src/main/resources/i18n`
- Copy lại files vào đúng chỗ

### 1.2 Kiểm tra nội dung file

```bash
head -5 src/main/resources/i18n/messages_vi_VN.properties
```

**Phải thấy:**
```properties
# Vietnamese Language Resources
app.title=Hệ thống Đặt hàng Nhập khẩu
menu.dashboard=Trang Chủ
menu.orders=Quản lý Đơn hàng
```

**❌ Nếu thấy ký tự lỗi (Ã, Ä):**
- File không phải UTF-8
- Mở bằng editor và Save As UTF-8

### 1.3 Kiểm tra encoding

```bash
file -i src/main/resources/i18n/messages_vi_VN.properties
```

**Phải thấy:** `charset=utf-8` hoặc `charset=us-ascii`

## 🔍 Bước 2: Test Load ResourceBundle

### 2.1 Chạy Simple Test

```bash
# Compile
javac --module-path /path/to/javafx-sdk/lib \
      --add-modules javafx.controls \
      -cp src/main/resources \
      -d bin \
      src/main/java/com/importassignment/SimpleI18nTest.java

# Run
java --module-path /path/to/javafx-sdk/lib \
     --add-modules javafx.controls \
     -cp bin:src/main/resources \
     com.importassignment.SimpleI18nTest
```

**Hoặc với Maven:**
```bash
mvn compile exec:java -Dexec.mainClass="com.importassignment.SimpleI18nTest"
```

### 2.2 Xem Console Output

**Kết quả OK:**
```
✅ Bundle loaded: vi_VN
UI updated with locale: vi_VN
```

**Kết quả LỖI:**
```
❌ Error loading bundle: Can't find bundle for base name i18n.messages
```

## 🔍 Bước 3: Kiểm tra Maven Compile

### 3.1 Clean và Compile lại

```bash
mvn clean compile
```

### 3.2 Kiểm tra target/classes

```bash
ls -la target/classes/i18n/
```

**Phải thấy:**
```
messages_vi_VN.properties
messages_en_US.properties
```

**❌ Nếu không có folder i18n trong target/classes:**

Thêm vào `pom.xml`:
```xml
<build>
    <resources>
        <resource>
            <directory>src/main/resources</directory>
            <includes>
                <include>**/*.properties</include>
                <include>**/*.fxml</include>
                <include>**/*.css</include>
            </includes>
        </resource>
    </resources>
</build>
```

Sau đó:
```bash
mvn clean compile
```

## 🔍 Bước 4: Kiểm tra Code

### 4.1 Test trực tiếp trong Java

Tạo file `DirectTest.java`:

```java
import java.util.Locale;
import java.util.ResourceBundle;

public class DirectTest {
    public static void main(String[] args) {
        try {
            // Test VI
            ResourceBundle vi = ResourceBundle.getBundle(
                "i18n.messages", 
                new Locale("vi", "VN")
            );
            System.out.println("VI: " + vi.getString("app.title"));
            
            // Test EN
            ResourceBundle en = ResourceBundle.getBundle(
                "i18n.messages", 
                new Locale("en", "US")
            );
            System.out.println("EN: " + en.getString("app.title"));
            
            System.out.println("✅ SUCCESS!");
        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```

Chạy:
```bash
javac -cp src/main/resources DirectTest.java
java -cp .:src/main/resources DirectTest
```

## 🔍 Bước 5: Kiểm tra Event Handler

### 5.1 Thêm Debug Log

Trong `MainLayoutController.java`:

```java
@FXML
private void onLanguageChange() {
    System.out.println("=================================");
    System.out.println("EVENT TRIGGERED!");
    System.out.println("ComboBox value: " + languageComboBox.getValue());
    
    String selected = languageComboBox.getValue();
    System.out.println("Selected: " + selected);
    
    Locale newLocale;
    if ("Tiếng Việt".equals(selected)) {
        newLocale = new Locale("vi", "VN");
        System.out.println("Switching to Vietnamese");
    } else {
        newLocale = new Locale("en", "US");
        System.out.println("Switching to English");
    }
    
    System.out.println("Calling MainApp.changeLanguage()...");
    MainApp.changeLanguage(newLocale);
    System.out.println("=================================");
}
```

### 5.2 Rebuild và Run

```bash
mvn clean compile javafx:run
```

### 5.3 Kiểm tra Console

Khi click ComboBox, console **PHẢI** hiển thị:
```
=================================
EVENT TRIGGERED!
ComboBox value: English
Selected: English
Switching to English
Calling MainApp.changeLanguage()...
=================================
```

**❌ Nếu KHÔNG thấy gì:**
- Event handler không được bind
- Kiểm tra FXML: `onAction="#onLanguageChange"`

## 🔍 Bước 6: Các Lỗi Phổ Biến

### Lỗi A: ComboBox null

**Triệu chứng:** NullPointerException khi click ComboBox

**Nguyên nhân:** `@FXML` không match với fx:id trong FXML

**Giải pháp:**
- Kiểm tra `MainLayout.fxml`: `<ComboBox fx:id="languageComboBox" ...`
- Kiểm tra Controller: `@FXML private ComboBox<String> languageComboBox;`

### Lỗi B: onAction không trigger

**Triệu chứng:** Click ComboBox không có phản ứng

**Giải pháp:**
- Thay `onAction` bằng `setOnAction` trong initialize():
```java
languageComboBox.setOnAction(event -> onLanguageChange());
```

### Lỗi C: UI không reload

**Triệu chứng:** Console log OK nhưng UI không đổi

**Giải pháp:** 
- Thử force reload:
```java
public static void changeLanguage(Locale locale) {
    currentLocale = locale;
    resourceBundle = ResourceBundle.getBundle("i18n.messages", currentLocale);
    
    Platform.runLater(() -> {
        try {
            FXMLLoader loader = new FXMLLoader(
                MainApp.class.getResource("/fxml/MainLayout.fxml")
            );
            loader.setResources(resourceBundle);
            Parent root = loader.load();
            
            primaryStage.getScene().setRoot(root);
            primaryStage.setTitle(resourceBundle.getString("app.title"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    });
}
```

## ✅ Solution: Nếu Tất Cả Đều Fail

### Plan B: Hardcode Test

Thay toàn bộ `onLanguageChange()` bằng:

```java
@FXML
private void onLanguageChange() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Test");
    alert.setHeaderText("Event Triggered!");
    alert.setContentText("ComboBox value: " + languageComboBox.getValue());
    alert.showAndWait();
    
    // Manually change
    if (languageComboBox.getValue().equals("English")) {
        changeToEnglish();
    } else {
        changeToVietnamese();
    }
}

private void changeToEnglish() {
    primaryStage.setTitle("Import Assignment System");
    // Manually update all labels...
}

private void changeToVietnamese() {
    primaryStage.setTitle("Hệ thống Đặt hàng Nhập khẩu");
    // Manually update all labels...
}
```

## 📞 Cuối Cùng: Report Info

Nếu vẫn không được, cung cấp thông tin sau:

1. **Java Version:**
   ```bash
   java -version
   ```

2. **JavaFX Version:**
   ```bash
   mvn dependency:tree | grep javafx
   ```

3. **Files Structure:**
   ```bash
   tree src/main/resources/
   ```

4. **Console Output** khi chạy `SimpleI18nTest`

5. **Screenshot** của error (nếu có)

---

**Thử các bước theo thứ tự từ 1→6. Tìm ra bước nào fail đầu tiên!**

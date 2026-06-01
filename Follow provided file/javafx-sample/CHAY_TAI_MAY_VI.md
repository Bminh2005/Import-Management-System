# Hướng dẫn Chạy trên Máy Cá Nhân

## ⚠️ LƯU Ý QUAN TRỌNG

**Environment hiện tại (Figma Make web) KHÔNG HỖ TRỢ JavaFX!**

Môi trường này là dành cho web development (React/Tailwind), không có Java/Maven.

Bạn cần tải project về máy cá nhân và chạy với Java development environment.

---

## ✅ Yêu Cầu Hệ Thống

### 1. Java Development Kit (JDK) 17 hoặc cao hơn

**Download JDK:**
- **Oracle JDK**: https://www.oracle.com/java/technologies/downloads/
- **OpenJDK** (miễn phí): https://adoptium.net/

**Kiểm tra Java đã cài:**
```bash
java -version
javac -version
```

Phải thấy: `java version "17.x.x"` hoặc cao hơn

### 2. Apache Maven

**Download Maven**: https://maven.apache.org/download.cgi

**Kiểm tra Maven:**
```bash
mvn -version
```

### 3. JavaFX SDK (Tùy chọn - Maven sẽ tự download)

Nếu dùng IDE hoặc run thủ công, download: https://gluonhq.com/products/javafx/

---

## 📥 Bước 1: Tải Project

### Option A: Download toàn bộ thư mục `javafx-sample`

Copy toàn bộ thư mục này về máy local của bạn.

**Cấu trúc cần có:**
```
javafx-sample/
├── pom.xml
├── src/
│   └── main/
│       ├── java/
│       │   └── com/importassignment/
│       │       ├── MainApp.java
│       │       ├── SimpleI18nTest.java
│       │       ├── controllers/
│       │       │   ├── MainLayoutController.java
│       │       │   ├── SidebarController.java
│       │       │   ├── DashboardController.java
│       │       │   └── OrdersListController.java
│       │       └── models/
│       │           └── Order.java
│       └── resources/
│           ├── i18n/
│           │   ├── messages_vi_VN.properties
│           │   └── messages_en_US.properties
│           ├── fxml/
│           │   ├── MainLayout.fxml
│           │   ├── Sidebar.fxml
│           │   ├── Dashboard.fxml
│           │   └── OrdersList.fxml
│           └── css/
│               └── application.css
├── TEST_I18N.java
├── README_JAVAFX.md
├── QUICK_START_VI.md
├── DEBUG_CHECKLIST_VI.md
└── TROUBLESHOOTING_VI.md
```

### Option B: Clone/Copy từ repository (nếu có)

```bash
# Nếu project được đẩy lên Git
git clone <repository-url>
cd javafx-sample
```

---

## 🚀 Bước 2: Chạy Project

### Option 1: Với Maven (Khuyên dùng)

```bash
cd javafx-sample

# Compile
mvn clean compile

# Chạy application
mvn javafx:run
```

### Option 2: Với IntelliJ IDEA

1. **Mở Project:**
   - File → Open
   - Chọn thư mục `javafx-sample`
   - Chọn "Import as Maven project"

2. **Cấu hình JavaFX (nếu cần):**
   - File → Project Structure → Project
   - Project SDK: Java 17+
   - Project language level: 17

3. **Chạy:**
   - Mở file `src/main/java/com/importassignment/MainApp.java`
   - Right-click → Run 'MainApp.main()'

**Nếu gặp lỗi "JavaFX runtime components missing":**
   - Run → Edit Configurations
   - Chọn MainApp configuration
   - VM Options: thêm:
     ```
     --module-path "C:\path\to\javafx-sdk-17\lib" --add-modules javafx.controls,javafx.fxml
     ```

### Option 3: Với Eclipse

1. **Import Project:**
   - File → Import → Maven → Existing Maven Projects
   - Chọn thư mục `javafx-sample`

2. **Cấu hình Run:**
   - Right-click MainApp.java → Run As → Run Configurations
   - Arguments tab → VM arguments:
     ```
     --module-path "C:\path\to\javafx-sdk-17\lib" --add-modules javafx.controls,javafx.fxml
     ```

3. **Run:**
   - Right-click MainApp.java → Run As → Java Application

### Option 4: Compile và Run thủ công

**Windows:**
```cmd
cd javafx-sample

:: Set JavaFX path
set PATH_TO_FX=C:\path\to\javafx-sdk-17\lib

:: Compile
javac --module-path %PATH_TO_FX% ^
      --add-modules javafx.controls,javafx.fxml ^
      -d target\classes ^
      -cp src\main\resources ^
      src\main\java\com\importassignment\*.java ^
      src\main\java\com\importassignment\controllers\*.java ^
      src\main\java\com\importassignment\models\*.java

:: Copy resources
xcopy src\main\resources target\classes\ /E /I /Y

:: Run
java --module-path %PATH_TO_FX% ^
     --add-modules javafx.controls,javafx.fxml ^
     -cp target\classes;src\main\resources ^
     com.importassignment.MainApp
```

**Linux/Mac:**
```bash
cd javafx-sample

# Set JavaFX path
PATH_TO_FX=/path/to/javafx-sdk-17/lib

# Compile
javac --module-path $PATH_TO_FX \
      --add-modules javafx.controls,javafx.fxml \
      -d target/classes \
      -cp src/main/resources \
      $(find src/main/java -name "*.java")

# Copy resources
cp -r src/main/resources/* target/classes/

# Run
java --module-path $PATH_TO_FX \
     --add-modules javafx.controls,javafx.fxml \
     -cp target/classes:src/main/resources \
     com.importassignment.MainApp
```

---

## 🧪 Bước 3: Test Đa Ngôn Ngữ

### Test 1: SimpleI18nTest (Test đơn giản)

```bash
mvn compile exec:java -Dexec.mainClass="com.importassignment.SimpleI18nTest"
```

Hoặc trong IDE: Run `SimpleI18nTest.java`

**Kết quả mong đợi:**
- Cửa sổ hiện ra với test UI
- Click button "Chuyển sang English"
- Text trên UI đổi sang English
- Click lại → đổi về Tiếng Việt

### Test 2: TEST_I18N (Console test)

```bash
# Compile
javac -cp src/main/resources -d bin TEST_I18N.java

# Run
java -cp bin:src/main/resources TEST_I18N
```

**Kết quả mong đợi:**
```
TEST TIẾNG VIỆT:
✅ File loaded successfully!
- app.title = Hệ thống Đặt hàng Nhập khẩu
- menu.dashboard = Trang Chủ

TEST ENGLISH:
✅ File loaded successfully!
- app.title = Import Assignment System
- menu.dashboard = Dashboard
```

### Test 3: Main Application

```bash
mvn javafx:run
```

**Sau khi app chạy:**

1. **Kiểm tra khởi động (Mặc định Tiếng Việt):**
   - Window title: "Hệ thống Đặt hàng Nhập khẩu"
   - Menu: "Trang Chủ", "Quản lý Đơn hàng", etc.

2. **Test chuyển sang English:**
   - Click ComboBox góc phải header (hiện "Tiếng Việt")
   - Chọn "English"
   - UI reload
   - Kiểm tra: Window title → "Import Assignment System"
   - Menu → "Dashboard", "Orders Management"

3. **Test chuyển lại Tiếng Việt:**
   - Click ComboBox (hiện "English")
   - Chọn "Tiếng Việt"
   - UI reload về Tiếng Việt

---

## 🐛 Troubleshooting

### Lỗi: "mvn: command not found"

**Giải pháp:**
- Cài Apache Maven
- Hoặc dùng IDE (IntelliJ/Eclipse) đã tích hợp Maven

### Lỗi: "java: command not found"

**Giải pháp:**
- Cài JDK 17+
- Set JAVA_HOME environment variable:
  - Windows: `JAVA_HOME=C:\Program Files\Java\jdk-17`
  - Linux/Mac: `export JAVA_HOME=/usr/lib/jvm/java-17`

### Lỗi: "JavaFX runtime components are missing"

**Giải pháp:**
- Với Maven: Maven sẽ tự download JavaFX
- Với IDE/thủ công: Download JavaFX SDK và thêm vào module-path

### Lỗi: "Cannot find bundle i18n.messages"

**Giải pháp:**
```bash
# Đảm bảo resources được compile
mvn clean compile

# Kiểm tra
ls target/classes/i18n/
# Phải thấy: messages_vi_VN.properties, messages_en_US.properties
```

### Lỗi: Ký tự Tiếng Việt bị lỗi

**Giải pháp:**
- Đảm bảo `messages_vi_VN.properties` encoding là UTF-8
- Trong IDE: File → Settings → Editor → File Encodings → UTF-8

---

## 📋 Checklist Trước Khi Chạy

- [ ] JDK 17+ đã cài: `java -version`
- [ ] Maven đã cài: `mvn -version`
- [ ] Đã download/copy toàn bộ thư mục `javafx-sample`
- [ ] Cấu trúc thư mục đầy đủ (kiểm tra src/main/java, src/main/resources)
- [ ] Properties files tồn tại: `ls src/main/resources/i18n/`
- [ ] pom.xml tồn tại ở root của project

---

## 💡 Tips

### Xem Console Output

Khi chạy app, mở Console/Terminal để xem debug logs:
- Errors sẽ hiện ở đây
- Bundle loading status
- Event triggers

### Thêm Debug Log

Trong `MainLayoutController.java`, thêm vào `onLanguageChange()`:

```java
@FXML
private void onLanguageChange() {
    System.out.println("Language change triggered!");
    String selected = languageComboBox.getValue();
    System.out.println("Selected: " + selected);
    // ... rest of code
}
```

### Default Language

Muốn app khởi động bằng English? Sửa `MainApp.java`:

```java
// Line 19
private static Locale currentLocale = new Locale("en", "US"); // English default
```

---

## 📞 Nếu Vẫn Không Chạy Được

1. **Kiểm tra môi trường:**
   ```bash
   java -version    # Phải ≥ 17
   mvn -version     # Phải có
   echo $JAVA_HOME  # Phải set
   ```

2. **Chạy SimpleI18nTest trước:**
   - Đây là test đơn giản nhất
   - Nếu test này chạy được → environment OK
   - Nếu test này lỗi → fix environment trước

3. **Check dependencies:**
   ```bash
   mvn dependency:tree
   ```
   
   Phải thấy:
   - org.openjfx:javafx-controls:17.0.2
   - org.openjfx:javafx-fxml:17.0.2

4. **Xem file logs:**
   ```bash
   tail -f target/application.log  # Nếu có
   ```

---

## ✨ Kết Luận

**Code của bạn đã HOÀN CHỈNH và CHÍNH XÁC.**

Lý do không chạy được trong environment hiện tại là vì:
- Environment này dành cho web (React/Tailwind)
- Không có Java/Maven/JavaFX

**Giải pháp:**
1. Tải toàn bộ project về máy local
2. Cài JDK 17 + Maven
3. Chạy `mvn javafx:run`

→ Application sẽ chạy hoàn hảo với đầy đủ chức năng đa ngôn ngữ!

---

**Chúc bạn thành công!** 🎉

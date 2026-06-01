# Import Assignment System - JavaFX Implementation
# Hệ thống Quản lý Đặt hàng Nhập khẩu - Triển khai JavaFX

## 📁 Cấu trúc Dự án

```
javafx-sample/
├── src/main/
│   ├── java/com/importassignment/
│   │   ├── MainApp.java                 # Main Application class
│   │   ├── controllers/                 # Controllers cho FXML
│   │   │   ├── MainLayoutController.java
│   │   │   ├── SidebarController.java
│   │   │   ├── DashboardController.java
│   │   │   └── OrdersListController.java
│   │   ├── models/                      # Data models
│   │   │   └── Order.java
│   │   ├── services/                    # Business logic & API
│   │   └── utils/                       # Utility classes
│   └── resources/
│       ├── fxml/                        # FXML view files
│       │   ├── MainLayout.fxml
│       │   ├── Sidebar.fxml
│       │   ├── Dashboard.fxml
│       │   └── OrdersList.fxml
│       ├── css/                         # Stylesheets
│       │   └── application.css
│       ├── i18n/                        # Internationalization
│       │   ├── messages_vi_VN.properties  # Tiếng Việt
│       │   └── messages_en_US.properties  # English
│       └── images/                      # Icons and images
└── lib/                                 # External JAR files
```

## 🌍 Hỗ trợ Đa ngôn ngữ (i18n)

### Cách chuyển đổi ngôn ngữ:

**Trong code:**
```java
// Vietnamese
MainApp.changeLanguage(new Locale("vi", "VN"));

// English
MainApp.changeLanguage(new Locale("en", "US"));
```

**Trong UI:**
- Sử dụng ComboBox ở header để chuyển đổi
- Hệ thống tự động reload giao diện với ngôn ngữ mới

### Thêm ngôn ngữ mới:

1. Tạo file `messages_[language]_[COUNTRY].properties` trong `resources/i18n/`
2. Thêm tất cả keys từ file tiếng Việt
3. Dịch các values sang ngôn ngữ mới

## 🎨 Design System

### Màu sắc chính:
- **Primary**: #2563EB (Blue) - Tin cậy, chuyên nghiệp
- **Secondary**: #10B981 (Green) - Thành công, xác nhận
- **Accent**: #F59E0B (Amber) - Cảnh báo, pending
- **Danger**: #EF4444 (Red) - Lỗi, critical
- **Text Primary**: #111827
- **Text Secondary**: #6B7280
- **Background**: #F9FAFB

### Typography:
- Font Family: Segoe UI, SF Pro Display
- Page Title: 32px, Bold
- Section Title: 20px, Semibold
- Body: 14px, Regular
- Caption: 12px, Regular

## 🚀 Cách Chạy Ứng dụng

### Yêu cầu:
- **JDK**: 17 hoặc cao hơn
- **JavaFX SDK**: 17 hoặc cao hơn
- **IDE**: IntelliJ IDEA, Eclipse, hoặc NetBeans

### Các bước:

1. **Cài đặt JavaFX SDK**:
   - Tải từ: https://openjfx.io/
   - Giải nén vào thư mục `lib/javafx-sdk-17/`

2. **Configure IDE**:

   **IntelliJ IDEA**:
   ```
   File > Project Structure > Libraries
   Add JavaFX SDK library
   
   Run > Edit Configurations
   VM Options: --module-path "lib/javafx-sdk-17/lib" --add-modules javafx.controls,javafx.fxml
   ```

   **Eclipse**:
   ```
   Project Properties > Java Build Path > Libraries
   Add External JARs (from JavaFX SDK)
   
   Run Configurations > Arguments
   VM Arguments: --module-path "lib/javafx-sdk-17/lib" --add-modules javafx.controls,javafx.fxml
   ```

3. **Compile và Run**:
   ```bash
   javac -p lib/javafx-sdk-17/lib --add-modules javafx.controls,javafx.fxml \
         -d bin src/main/java/com/importassignment/*.java
   
   java -p lib/javafx-sdk-17/lib --add-modules javafx.controls,javafx.fxml \
        -cp bin com.importassignment.MainApp
   ```

4. **Hoặc dùng Maven**:
   ```xml
   <dependency>
       <groupId>org.openjfx</groupId>
       <artifactId>javafx-controls</artifactId>
       <version>17</version>
   </dependency>
   <dependency>
       <groupId>org.openjfx</groupId>
       <artifactId>javafx-fxml</artifactId>
       <version>17</version>
   </dependency>
   ```

## 📦 Các Tính năng Đã Triển khai

### ✅ Hoàn thành:
- [x] Main Application với đa ngôn ngữ
- [x] Sidebar navigation với keyboard shortcuts
- [x] Dashboard với metric cards
- [x] Orders List với table, filters, sorting
- [x] Context menu (right-click)
- [x] Row selection và multi-select
- [x] Status badges với màu sắc
- [x] Progress bars cho completion %
- [x] Responsive layout
- [x] CSS styling theo design system
- [x] i18n (Tiếng Việt + English)

### 🔄 Cần Triển khai Thêm:
- [ ] Create Order form (multi-step wizard)
- [ ] Order Details view
- [ ] Site Management screens
- [ ] Warehouse screens
- [ ] Reports & Analytics
- [ ] Settings screens
- [ ] User authentication
- [ ] API integration với Java backend
- [ ] Database connection (JDBC)
- [ ] Export to Excel/PDF
- [ ] Search functionality
- [ ] Pagination logic
- [ ] Validation forms

## 🔌 Kết nối với Java Backend

### REST API Structure (Đề xuất):

```java
// Service class example
public class OrderService {
    private final String BASE_URL = "http://localhost:8080/api";
    
    public List<Order> getAllOrders() {
        // HTTP GET request
        // Parse JSON response
        return orders;
    }
    
    public Order createOrder(Order order) {
        // HTTP POST request
        return createdOrder;
    }
}
```

### Database (JDBC):

```java
// Database connection
public class DatabaseUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/import_db";
    private static final String USER = "root";
    private static final String PASS = "password";
    
    public static Connection getConnection() {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
```

## 📚 Tài liệu Tham khảo

### JavaFX Documentation:
- [Official Docs](https://openjfx.io/)
- [FXML Guide](https://docs.oracle.com/javafx/2/fxml_get_started/jfxpub-fxml_get_started.htm)
- [CSS Reference](https://openjfx.io/javadoc/17/javafx.graphics/javafx/scene/doc-files/cssref.html)

### Design Patterns:
- MVC (Model-View-Controller)
- Observer Pattern cho data binding
- Singleton cho database connection

## 🎯 Best Practices

1. **FXML vs Java Code**:
   - Dùng FXML cho layout và UI structure
   - Dùng Java code cho business logic
   - CSS cho styling

2. **Data Binding**:
   - Sử dụng Properties (StringProperty, IntegerProperty)
   - ObservableList cho TableView

3. **Resource Bundle**:
   - Tất cả text phải từ .properties files
   - Dễ dàng thêm ngôn ngữ mới

4. **Separation of Concerns**:
   - Controllers chỉ handle UI logic
   - Services handle business logic
   - Models chứa data structures

## 🐛 Troubleshooting

### JavaFX không tìm thấy:
```
Error: JavaFX runtime components are missing
```
**Giải pháp**: Thêm VM options với module-path

### FXML không load:
```
FXMLLoader Exception
```
**Kiểm tra**:
- Đường dẫn file FXML đúng chưa
- fx:controller class name chính xác
- @FXML annotations trên fields

### CSS không áp dụng:
**Kiểm tra**:
- styleClass name trong FXML khớp với CSS
- CSS file được load trong Scene
- Selector syntax đúng

## 📞 Support

Để được hỗ trợ:
1. Kiểm tra file logs
2. Debug với breakpoints trong Controllers
3. Test từng component riêng lẻ

---

**Developed by**: Import Assignment Team
**Version**: 1.0.0
**Last Updated**: 2026-05-08

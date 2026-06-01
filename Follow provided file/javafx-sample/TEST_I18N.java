import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Test I18N - Kiểm tra chức năng đa ngôn ngữ
 * Chạy file này để test ResourceBundle trước khi chạy app chính
 */
public class TEST_I18N {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("TEST ĐA NGÔN NGỮ - I18N TEST");
        System.out.println("========================================\n");

        // Test Tiếng Việt
        System.out.println("1. TEST TIẾNG VIỆT:");
        System.out.println("   Path: src/main/resources/i18n/messages_vi_VN.properties");
        try {
            ResourceBundle vi = ResourceBundle.getBundle("i18n.messages", new Locale("vi", "VN"));
            System.out.println("   ✅ File loaded successfully!");
            System.out.println("   Locale: " + vi.getLocale());
            System.out.println("\n   Sample Keys:");
            System.out.println("   - app.title = " + vi.getString("app.title"));
            System.out.println("   - menu.dashboard = " + vi.getString("menu.dashboard"));
            System.out.println("   - menu.orders = " + vi.getString("menu.orders"));
            System.out.println("   - dashboard.welcome = " + vi.getString("dashboard.welcome"));
            System.out.println("   - orders.title = " + vi.getString("orders.title"));
            System.out.println("   - status.pending = " + vi.getString("status.pending"));
        } catch (Exception e) {
            System.out.println("   ❌ LỖI: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n========================================\n");

        // Test English
        System.out.println("2. TEST ENGLISH:");
        System.out.println("   Path: src/main/resources/i18n/messages_en_US.properties");
        try {
            ResourceBundle en = ResourceBundle.getBundle("i18n.messages", new Locale("en", "US"));
            System.out.println("   ✅ File loaded successfully!");
            System.out.println("   Locale: " + en.getLocale());
            System.out.println("\n   Sample Keys:");
            System.out.println("   - app.title = " + en.getString("app.title"));
            System.out.println("   - menu.dashboard = " + en.getString("menu.dashboard"));
            System.out.println("   - menu.orders = " + en.getString("menu.orders"));
            System.out.println("   - dashboard.welcome = " + en.getString("dashboard.welcome"));
            System.out.println("   - orders.title = " + en.getString("orders.title"));
            System.out.println("   - status.pending = " + en.getString("status.pending"));
        } catch (Exception e) {
            System.out.println("   ❌ LỖI: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n========================================\n");

        // Test switching
        System.out.println("3. TEST CHUYỂN ĐỔI NGÔN NGỮ:");
        try {
            ResourceBundle currentBundle = ResourceBundle.getBundle("i18n.messages", new Locale("vi", "VN"));
            System.out.println("   Initial (VI): " + currentBundle.getString("menu.dashboard"));

            currentBundle = ResourceBundle.getBundle("i18n.messages", new Locale("en", "US"));
            System.out.println("   After switch (EN): " + currentBundle.getString("menu.dashboard"));

            currentBundle = ResourceBundle.getBundle("i18n.messages", new Locale("vi", "VN"));
            System.out.println("   Switch back (VI): " + currentBundle.getString("menu.dashboard"));

            System.out.println("\n   ✅ Chuyển đổi thành công!");
        } catch (Exception e) {
            System.out.println("   ❌ LỖI khi chuyển đổi: " + e.getMessage());
        }

        System.out.println("\n========================================");
        System.out.println("KẾT LUẬN:");
        System.out.println("Nếu tất cả đều ✅, bạn có thể chạy MainApp.java");
        System.out.println("Nếu có ❌, kiểm tra:");
        System.out.println("  1. Files .properties có trong đúng thư mục chưa");
        System.out.println("  2. Encoding của files là UTF-8");
        System.out.println("  3. Maven đã compile resources chưa (mvn compile)");
        System.out.println("========================================\n");
    }
}

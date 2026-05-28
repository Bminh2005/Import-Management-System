package com.app.modules.sales.apptest;

/**
 * Launcher cho SalesRequestTestApplication.
 * Class này KHÔNG extend javafx.application.Application nên bypass được
 * yêu cầu "JavaFX modules must be on module path" của JDK 17+ khi chạy
 * thẳng từ IntelliJ với JavaFX trên classpath.
 *
 * Trong Run Configuration, đặt Main class là class này thay vì
 * SalesRequestTestApplication.
 */
public class SalesRequestTestLauncher {
    public static void main(String[] args) {
        SalesRequestTestApplication.main(args);
    }
}

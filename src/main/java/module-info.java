module com.app.Ioms {
    requires javafx.controls;
    requires javafx.fxml;

    // App entrypoint
    opens com.app.Ioms to javafx.fxml;
    exports com.app.Ioms;

    // Modules - sales/request
    opens com.app.modules.sales.request.ui to javafx.fxml;
    opens com.app.modules.sales.request.entity to javafx.fxml;

    // Modules - procurement/inventory (Bộ phận Đặt hàng Quốc tế)
    opens com.app.modules.procurement.inventory.ui to javafx.fxml;
    opens com.app.modules.procurement.inventory.entity to javafx.fxml;
}

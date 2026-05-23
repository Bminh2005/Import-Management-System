module com.app.Ioms {
    requires javafx.controls;
    requires javafx.fxml;

    // App entrypoint
    opens com.app.Ioms to javafx.fxml;
    exports com.app.Ioms;

    // Modules - sales/request
    opens com.app.modules.sales.request.ui to javafx.fxml;
    opens com.app.modules.sales.request.entity to javafx.fxml;

    // Modules - warehouse/inventory
    opens com.app.modules.warehouse.inventory.ui to javafx.fxml;
    opens com.app.modules.warehouse.inventory.entity to javafx.fxml;

    // Modules - procurement/importorder
    opens com.app.modules.procurement.importorder.ui to javafx.fxml;
}

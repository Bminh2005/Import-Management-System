module com.app.Ioms {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.app.Ioms to javafx.fxml;
    exports com.app.Ioms;

    opens com.app.common.ui.components to javafx.fxml;
    opens com.app.modules.procurement.product.ui to javafx.fxml;
    opens com.app.modules.site.catalog.ui to javafx.fxml;
    opens com.app.modules.site.catalog.ui.components to javafx.fxml;

    exports com.app.common.ui.components;
    exports com.app.common.util;
    exports com.app.modules.procurement.product.ui;
    exports com.app.modules.site.catalog.ui;
    exports com.app.modules.site.catalog.ui.components;
}

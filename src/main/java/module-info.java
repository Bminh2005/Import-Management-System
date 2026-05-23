module com.app.Ioms {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.app.Ioms to javafx.fxml;
    opens com.app.common.ui.components to javafx.fxml;
    opens com.app.modules.warehouse.dashboard.ui to javafx.fxml;
    opens com.app.modules.warehouse.inbound.ui to javafx.fxml;
    exports com.app.Ioms;
    exports com.app.Ioms.navigation;
    exports com.app.common.ui.components;
    exports com.app.modules.warehouse.dashboard.dto;
    exports com.app.modules.warehouse.dashboard.service;
    exports com.app.modules.warehouse.dashboard.ui;
    exports com.app.modules.warehouse.inbound.dto;
    exports com.app.modules.warehouse.inbound.repository;
    exports com.app.modules.warehouse.inbound.service;
    exports com.app.modules.warehouse.inbound.ui;
    exports com.app.vinh.test;
}

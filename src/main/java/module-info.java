module com.app.Ioms {
    requires javafx.controls;
    requires javafx.fxml;
        opens com.app.Ioms to javafx.fxml;
        opens com.app.Ioms.ui.main to javafx.fxml;
        opens com.app.Ioms.ui.orders to javafx.fxml;

    opens com.app.Ioms.ui.main to javafx.fxml;
    opens com.app.Ioms.ui.orders to javafx.fxml;

    exports com.app.Ioms;
}
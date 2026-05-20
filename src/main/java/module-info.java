module com.app.Ioms {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.app.Ioms to javafx.fxml;
    exports com.app.Ioms;
}
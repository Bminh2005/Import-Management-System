package com.app.modules.sales.request.requestlist.ui;

import javafx.scene.control.Alert;
import javafx.stage.Window;

/** Alert cho màn danh sách yêu cầu nhập hàng. */
public class RequestListAlertController {

    public void showLoadError(Window owner, Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Danh sách yêu cầu");
        alert.setHeaderText("Không tải được dữ liệu");
        alert.setContentText(ex.getMessage());
        if (owner != null) {
            alert.initOwner(owner);
        }
        alert.showAndWait();
    }
}

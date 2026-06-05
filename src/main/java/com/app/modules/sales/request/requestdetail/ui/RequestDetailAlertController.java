package com.app.modules.sales.request.requestdetail.ui;

import javafx.scene.control.Alert;
import javafx.stage.Window;

/** Alert cho màn chi tiết yêu cầu nhập hàng. */
public class RequestDetailAlertController {

    public void showLoadError(Window owner, String code, Exception ex) {
        String detail = ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Không tải được yêu cầu");
        alert.setHeaderText("Mã yêu cầu: " + code);
        alert.setContentText(detail + "\n\nKiểm tra kết nối Supabase trong PostgreSQLProvider "
                + "và đảm bảo bản ghi tồn tại trong bảng ImportRequest.");
        if (owner != null) {
            alert.initOwner(owner);
        }
        alert.showAndWait();
    }
}

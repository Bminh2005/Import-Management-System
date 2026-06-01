package com.app.modules.sales.request.editrequest.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

/**
 * Controller chuyên biệt xử lý hiển thị cảnh báo, thông báo lỗi và hộp thoại xác nhận
 * cho màn hình Chỉnh sửa Yêu cầu Nhập hàng (EditRequestUI).
 */
public class EditRequestAlertController {

    /**
     * Hiển thị cảnh báo lỗi/chú ý.
     */
    public void showWarning(Window owner, String header, String message) {
        Alert err = new Alert(Alert.AlertType.WARNING, message);
        err.setHeaderText(header);
        err.initOwner(owner);
        err.showAndWait();
    }

    /**
     * Hiển thị thông báo thông tin thành công/hoàn thành.
     */
    public void showInfo(Window owner, String message) {
        Alert info = new Alert(Alert.AlertType.INFORMATION, message);
        info.setHeaderText(null);
        info.initOwner(owner);
        info.showAndWait();
    }

    /**
     * Hiển thị cảnh báo khi yêu cầu không còn ở trạng thái PENDING và không thể chỉnh sửa.
     */
    public void showCompletedAlert(Window owner, String requestCode, String status) {
        String statusLabel = "completed".equals(status) ? "đã hoàn tất" : "đang được xử lý";
        Alert info = new Alert(Alert.AlertType.INFORMATION,
                "Yêu cầu " + requestCode
                        + " " + statusLabel + " nên không thể chỉnh sửa. "
                        + "Bạn chỉ có thể xem thông tin.");
        info.setHeaderText("Không thể chỉnh sửa");
        info.initOwner(owner);
        info.show();
    }

    /**
     * Hiển thị hộp thoại xác nhận xóa một mặt hàng khỏi yêu cầu.
     * @return true nếu người dùng chọn OK, ngược lại false
     */
    public boolean confirmDelete(Window owner, String itemCode) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn có chắc muốn xóa mặt hàng " + itemCode + " không?",
                ButtonType.OK, ButtonType.CANCEL);
        confirm.setHeaderText("Xác nhận xóa mặt hàng");
        confirm.initOwner(owner);
        return confirm.showAndWait().filter(b -> b == ButtonType.OK).isPresent();
    }

    /**
     * Hiển thị hộp thoại xác nhận quay lại khi có thay đổi chưa lưu.
     * @return true nếu người dùng chọn OK, ngược lại false
     */
    public boolean confirmBack(Window owner) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Mọi thay đổi chưa lưu sẽ bị hủy. Bạn có chắc muốn quay lại?",
                ButtonType.OK, ButtonType.CANCEL);
        confirm.setHeaderText("Thoát mà không lưu");
        confirm.initOwner(owner);
        return confirm.showAndWait().filter(b -> b == ButtonType.OK).isPresent();
    }
}

package com.app.modules.sales.request.editrequest.ui;

import com.app.common.util.FxmlUiHelper;
import com.app.modules.sales.request.entity.RelatedOrder;
import com.app.modules.sales.request.editrequest.service.RequestService;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Popup "Hủy đơn hàng" — modal xác nhận hủy 1 đơn hàng liên quan.
 * Yêu cầu người dùng nhập lý do trước khi xác nhận.
 *
 * Mở qua tiện ích {@link #show(Window, RelatedOrder, Runnable)}.
 */
public class CancelOrderDialogUI extends VBox {

    @FXML private Label subtitleLabel;
    @FXML private Label warningLabel;
    @FXML private Label siteLabel;
    @FXML private TextArea reasonField;
    @FXML private Label errorLabel;

    private final RequestService service;
    private Stage stage;
    private RelatedOrder order;
    private Runnable onCancelled;

    public CancelOrderDialogUI() {
        this(new RequestService());
    }

    public CancelOrderDialogUI(RequestService service) {
        this.service = service;
        FxmlUiHelper.loadSelf(this, "CancelOrderDialogPage.fxml");
    }

    /**
     * Tiện ích: mở popup modal trên owner cho 1 đơn hàng.
     * @param onCancelled callback chạy khi user xác nhận hủy thành công
     *                    (để màn cha refresh bảng đơn hàng).
     */
    public static void show(Window owner, RelatedOrder order, Runnable onCancelled) {
        CancelOrderDialogUI dialog = new CancelOrderDialogUI();
        dialog.bind(order, onCancelled);

        Stage stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Hủy đơn hàng");
        Scene scene = new Scene(dialog);
        scene.setFill(null);
        stage.setScene(scene);
        dialog.stage = stage;
        stage.showAndWait();
    }

    private void bind(RelatedOrder order, Runnable onCancelled) {
        this.order = order;
        this.onCancelled = onCancelled;
        subtitleLabel.setText("Xác nhận hủy đơn hàng " + order.getCode());
        warningLabel.setText("Cảnh báo: Hành động này sẽ hủy đơn hàng "
                + order.getCode() + ". Đơn hàng sẽ không thể khôi phục sau khi hủy.");
        siteLabel.setText(order.getSite());
    }

    @FXML
    private void onBack() {
        if (stage != null) stage.close();
    }

    @FXML
    private void onConfirm() {
        String reason = reasonField.getText() == null ? "" : reasonField.getText().trim();
        if (reason.isEmpty()) {
            errorLabel.setText("Vui lòng nhập lý do hủy đơn hàng.");
            errorLabel.setVisible(true);
            errorLabel.setManaged(true);
            reasonField.requestFocus();
            return;
        }

        service.cancelRelatedOrder(order.getCode(), reason);
        if (onCancelled != null) onCancelled.run();
        if (stage != null) stage.close();
    }
}

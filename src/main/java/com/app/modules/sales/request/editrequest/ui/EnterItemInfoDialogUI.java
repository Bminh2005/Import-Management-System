package com.app.modules.sales.request.editrequest.ui;

import com.app.common.util.FxmlUiHelper;
import com.app.modules.sales.request.entity.RequestItem;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.time.LocalDate;
import java.util.function.Consumer;

/**
 * Popup nhập số lượng + ngày nhận cho 1 sản phẩm đã chọn từ
 * {@link SelectProductDialogUI}. Bước 2 của flow "Thêm mặt hàng".
 *
 * Mở qua {@link #show(Window, RequestItem, Consumer)}.
 */
public class EnterItemInfoDialogUI extends VBox {

    @FXML private Label productNameLabel;
    @FXML private Label codeLabel;
    @FXML private Label unitLabel;
    @FXML private Spinner<Integer> quantitySpinner;
    @FXML private DatePicker deliveryDatePicker;
    @FXML private Label errorLabel;

    private Stage stage;
    private RequestItem product;
    private Consumer<RequestItem> onConfirm;

    public EnterItemInfoDialogUI() {
        FxmlUiHelper.loadSelf(this, "EnterItemInfoDialogPage.fxml");

        quantitySpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9999, 1));
        deliveryDatePicker.setValue(LocalDate.now().plusDays(7));
    }

    /** Tiện ích: mở popup nhập SL/ngày, callback nhận RequestItem hoàn chỉnh. */
    public static void show(Window owner, RequestItem product,
                            Consumer<RequestItem> onConfirm) {
        EnterItemInfoDialogUI dialog = new EnterItemInfoDialogUI();
        dialog.bind(product, onConfirm);

        Stage stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Thêm vào yêu cầu");
        Scene scene = new Scene(dialog);
        scene.setFill(null);
        stage.setScene(scene);
        dialog.stage = stage;
        stage.showAndWait();
    }

    private void bind(RequestItem product, Consumer<RequestItem> onConfirm) {
        this.product = product;
        this.onConfirm = onConfirm;
        productNameLabel.setText(product.getName());
        codeLabel.setText(product.getCode());
        unitLabel.setText(product.getUnit());
    }

    @FXML
    private void onCancel() {
        if (stage != null) stage.close();
    }

    @FXML
    private void onConfirm() {
        Integer quantity = readQuantity();
        LocalDate date = deliveryDatePicker.getValue();

        if (quantity == null || quantity <= 0) {
            showError("Số lượng phải là số nguyên dương.");
            return;
        }
        if (date == null) {
            showError("Vui lòng chọn ngày nhận mong muốn.");
            return;
        }

        RequestItem result = new RequestItem(
                product.getCode(), product.getName(), quantity,
                product.getUnit(), date.toString(), "pending");
        if (onConfirm != null) onConfirm.accept(result);
        if (stage != null) stage.close();
    }

    private Integer readQuantity() {
        Integer value = quantitySpinner.getValue();
        if (value != null) return value;
        String text = quantitySpinner.getEditor().getText();
        if (text == null || text.isBlank()) return null;
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
}

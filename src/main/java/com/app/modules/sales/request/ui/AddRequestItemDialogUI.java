package com.app.modules.sales.request.ui;

import com.app.common.util.FxmlUiHelper;
import com.app.modules.sales.request.entity.RequestItem;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Popup nhập thông tin mặt hàng mới khi thêm vào yêu cầu nhập hàng.
 * Theo quy ước README: UI chỉ thu thập input, không chạm DB —
 * caller nhận RequestItem qua onSave rồi gọi service.
 */
public class AddRequestItemDialogUI extends VBox {

    @FXML private TextField codeField;
    @FXML private TextField nameField;
    @FXML private TextField unitField;
    @FXML private Spinner<Integer> quantitySpinner;
    @FXML private DatePicker deliveryDatePicker;
    @FXML private ComboBox<String> statusCombo;
    @FXML private Label errorLabel;

    private Consumer<RequestItem> onSave;
    private Runnable onClose;
    /** Trả về true nếu mã hàng đã tồn tại trong yêu cầu hiện tại. */
    private Predicate<String> duplicateCodeCheck = code -> false;

    public AddRequestItemDialogUI() {
        FxmlUiHelper.loadSelf(this, "AddRequestItemDialogPage.fxml");

        quantitySpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9999, 1));
        // Cho phép gõ tay vào spinner mà không bị reset nếu nhập sai
        quantitySpinner.getValueFactory().setValue(1);

        statusCombo.setItems(FXCollections.observableArrayList("pending", "approved"));
        statusCombo.getSelectionModel().selectFirst();

        deliveryDatePicker.setValue(LocalDate.now().plusDays(7));
    }

    public void setOnSave(Consumer<RequestItem> onSave) {
        this.onSave = onSave;
    }

    public void setOnClose(Runnable onClose) {
        this.onClose = onClose;
    }

    public void setDuplicateCodeCheck(Predicate<String> check) {
        this.duplicateCodeCheck = check == null ? c -> false : check;
    }

    /** Gợi ý mã hàng kế tiếp (vd "MH-NEW-04"). */
    public void suggestCode(String codeHint) {
        codeField.setText(codeHint == null ? "" : codeHint);
    }

    @FXML
    private void onCancel() {
        if (onClose != null) onClose.run();
    }

    @FXML
    private void onSave() {
        String code = trim(codeField.getText());
        String name = trim(nameField.getText());
        String unit = trim(unitField.getText());
        Integer quantity = readQuantity();
        LocalDate deliveryDate = deliveryDatePicker.getValue();
        String status = statusCombo.getValue();

        String error = validate(code, name, unit, quantity, deliveryDate);
        if (error != null) {
            showError(error);
            return;
        }

        RequestItem item = new RequestItem(
                code, name, quantity, unit,
                deliveryDate.toString(),
                status == null ? "pending" : status);
        if (onSave != null) onSave.accept(item);
    }

    private Integer readQuantity() {
        Integer value = quantitySpinner.getValue();
        if (value != null) return value;
        // Người dùng có thể gõ tay vào editor
        String text = quantitySpinner.getEditor().getText();
        if (text == null || text.isBlank()) return null;
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String validate(String code, String name, String unit,
                            Integer quantity, LocalDate deliveryDate) {
        if (code.isEmpty()) return "Vui lòng nhập Mã hàng.";
        if (duplicateCodeCheck.test(code))
            return "Mã hàng \"" + code + "\" đã có trong yêu cầu.";
        if (name.isEmpty()) return "Vui lòng nhập Tên mặt hàng.";
        if (unit.isEmpty()) return "Vui lòng nhập Đơn vị tính.";
        if (quantity == null || quantity <= 0)
            return "Số lượng phải là số nguyên dương.";
        if (deliveryDate == null) return "Vui lòng chọn Ngày nhận.";
        return null;
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private static String trim(String s) {
        return s == null ? "" : s.trim();
    }
}

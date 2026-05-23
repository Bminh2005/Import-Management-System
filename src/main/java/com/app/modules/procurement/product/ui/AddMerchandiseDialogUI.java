package com.app.modules.procurement.product.ui;

import com.app.common.util.FxmlUiHelper;
import java.util.List;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/** UI popup Thêm mặt hàng. */
public class AddMerchandiseDialogUI extends StackPane {

  @FXML private Label titleLabel;
  @FXML private TextField codeField;
  @FXML private TextField nameField;
  @FXML private TextField unitField;
  @FXML private ComboBox<String> categoryCombo;
  @FXML private TextField priceField;
  @FXML private TextField supplierField;
  @FXML private TextArea descriptionField;

  private Runnable onClose;
  private Consumer<AddMerchandiseDialogUI> onSave;

  public AddMerchandiseDialogUI() {
    FxmlUiHelper.loadSelf(this, "AddMerchandiseDialogPage.fxml");
    FxmlUiHelper.addStylesheet(this, "add-merchandise-dialog.css");
    categoryCombo.getItems().setAll(List.of("Điện tử", "Phụ kiện", "Nội thất"));
    resetForm("MH005");
  }

  public void setOnSave(Consumer<AddMerchandiseDialogUI> onSave) {
    this.onSave = onSave;
  }

  public void setOnClose(Runnable onClose) {
    this.onClose = onClose;
  }

  public void prepareForNewItem(String nextCodeHint) {
    resetForm(nextCodeHint);
  }

  public String getNameValue() {
    return nameField.getText();
  }

  public String getUnitValue() {
    return unitField.getText();
  }

  public String getCategoryValue() {
    return categoryCombo.getValue();
  }

  private void resetForm(String codeHint) {
    titleLabel.setText("Thêm Mặt hàng Mới");
    codeField.setText("Tự động tạo (VD: " + codeHint + ")");
    nameField.clear();
    unitField.clear();
    priceField.clear();
    supplierField.clear();
    descriptionField.clear();
    categoryCombo.getSelectionModel().clearSelection();
  }

  @FXML
  private void onCancel() {
    if (onClose != null) {
      onClose.run();
    }
  }

  @FXML
  private void onSave() {
    if (onSave != null) {
      onSave.accept(this);
    }
  }

  @FXML
  private void onOverlayClick(MouseEvent event) {
    if (onClose != null) {
      onClose.run();
    }
  }

  @FXML
  private void stopPropagation(MouseEvent event) {
    event.consume();
  }
}

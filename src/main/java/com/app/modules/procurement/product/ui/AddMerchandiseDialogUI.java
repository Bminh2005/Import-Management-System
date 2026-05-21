package com.app.modules.procurement.product.ui;

import com.app.modules.procurement.product.dto.AddMerchandiseDTO;
import java.util.function.Consumer;
import com.app.common.util.FxmlUiHelper;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/** UI popup Thêm mặt hàng — chỉ xử lý giao diện. */
public class AddMerchandiseDialogUI extends StackPane {
  @FXML private Label titleLabel;
  @FXML private TextField codeField;
  @FXML private TextField nameField;
  @FXML private TextField unitField;
  @FXML private ComboBox<String> categoryCombo;
  @FXML private TextField priceField;
  @FXML private TextField supplierField;
  @FXML private TextArea descriptionField;

  private final AddMerchandiseDTO formDto = new AddMerchandiseDTO();
  private Consumer<AddMerchandiseDTO> onSave;
  private Runnable onClose;

  public AddMerchandiseDialogUI() {
    FxmlUiHelper.loadSelf(this, "AddMerchandiseDialogPage.fxml");
    FxmlUiHelper.addStylesheet(this, "add-merchandise-dialog.css");
    formDto.setCategoryOptions(java.util.List.of("Điện tử", "Phụ kiện", "Nội thất"));
    categoryCombo.getItems().setAll(formDto.getCategoryOptions());
    applyFormDtoToView();
  }

  public AddMerchandiseDTO getFormDto() {
    return formDto;
  }

  public void setOnSave(Consumer<AddMerchandiseDTO> onSave) {
    this.onSave = onSave;
  }

  public void setOnClose(Runnable onClose) {
    this.onClose = onClose;
  }

  public void prepareForNewItem(String nextCodeHint) {
    formDto.clearForm();
    formDto.setCodeHint("Tự động tạo (VD: " + nextCodeHint + ")");
    applyFormDtoToView();
  }

  public void applyFormDtoToView() {
    titleLabel.setText(formDto.getDialogTitle());
    codeField.setText(formDto.getCodeHint());
    nameField.setText(formDto.getName());
    unitField.setText(formDto.getUnit());
    priceField.setText(formDto.getPrice());
    supplierField.setText(formDto.getSupplier());
    descriptionField.setText(formDto.getDescription());
    if (formDto.getCategory() != null && !formDto.getCategory().isBlank()) {
      categoryCombo.setValue(formDto.getCategory());
    } else {
      categoryCombo.getSelectionModel().clearSelection();
    }
  }

  private void readViewIntoFormDto() {
    formDto.setName(nameField.getText());
    formDto.setUnit(unitField.getText());
    formDto.setCategory(categoryCombo.getValue());
    formDto.setPrice(priceField.getText());
    formDto.setSupplier(supplierField.getText());
    formDto.setDescription(descriptionField.getText());
  }

  @FXML
  private void onCancel() {
    com.app.common.util.ActionLog.stub("Thêm mặt hàng: Hủy");
    if (onClose != null) {
      onClose.run();
    }
  }

  @FXML
  private void onSave() {
    readViewIntoFormDto();
    com.app.common.util.ActionLog.stub("Thêm mặt hàng: Lưu Mặt hàng");
    if (onSave != null) {
      onSave.accept(formDto);
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

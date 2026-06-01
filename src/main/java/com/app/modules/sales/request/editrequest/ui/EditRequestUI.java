package com.app.modules.sales.request.editrequest.ui;

import com.app.common.util.FxmlUiHelper;
import com.app.common.util.StatusStyle;
import com.app.modules.sales.request.editrequest.dto.RequestResponse;
import com.app.modules.sales.request.entity.RequestItem;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.util.function.Consumer;

/**
 * UI Class cho màn "Chỉnh sửa Yêu cầu Nhập hàng".
 * Chỉ đóng vai trò hiển thị (View), không tự tạo Controller hay tự cấu hình ô TableView.
 * Tuân thủ nghiêm ngặt nguyên tắc Single Responsibility (SRP) và Dependency Inversion (DIP).
 */
public class EditRequestUI extends ScrollPane {

    @FXML private Label titleLabel;

    // --- Info card ---
    @FXML private Label codeLabel;
    @FXML private Label createdDateLabel;
    @FXML private Label statusLabel;
    @FXML private Label itemCountLabel;

    // --- Items table ---
    @FXML private TableView<RequestItem> itemsTable;
    @FXML private TableColumn<RequestItem, String> codeColumn;
    @FXML private TableColumn<RequestItem, String> nameColumn;
    @FXML private TableColumn<RequestItem, RequestItem> quantityColumn;
    @FXML private TableColumn<RequestItem, String> unitColumn;
    @FXML private TableColumn<RequestItem, RequestItem> deliveryDateColumn;
    @FXML private TableColumn<RequestItem, Void> actionsColumn;


    // --- Action buttons ---
    @FXML private Button addItemButton;
    @FXML private Button saveButton;

    private EditRequestController controller;
    private RequestResponse current;
    private boolean dirty = false;
    private boolean editable = true;

    private Runnable onBack;
    private Consumer<String> onSaved;

    private Runnable saveAction;
    private Runnable addItemAction;
    private Runnable backAction;

    public EditRequestUI() {
        FxmlUiHelper.loadSelf(this, "EditRequestPage.fxml");
        itemsTable.setEditable(false);
    }

    public EditRequestUI(EditRequestController controller) {
        this();
        this.controller = controller;
    }

    public void setController(EditRequestController controller) {
        this.controller = controller;
    }

    public void setActionForSaveButton(Runnable r) {
        this.saveAction = r;
    }

    public void setActionForAddItemButton(Runnable r) {
        this.addItemAction = r;
    }

    public void setActionForBackButton(Runnable r) {
        this.backAction = r;
    }

    public RequestResponse getCurrent() {
        return current;
    }

    public boolean hasCurrentRequest() {
        return current != null;
    }

    public String getRequestCode() {
        return current != null ? current.getCode() : "";
    }

    public java.util.List<RequestItem> getCurrentRequestItemsList() {
        return current != null ? new java.util.ArrayList<>(current.getItems()) : new java.util.ArrayList<>();
    }

    public void addRequestItem(RequestItem item) {
        if (current != null) {
            current.getItems().add(item);
            setDirty(true);
        }
    }

    public void removeRequestItem(RequestItem item) {
        if (current != null) {
            current.getItems().remove(item);
            setDirty(true);
        }
    }

    public boolean isEditable() {
        return editable;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean isDirty() {
        return dirty;
    }

    public javafx.stage.Window getSceneWindow() {
        return getScene() != null ? getScene().getWindow() : null;
    }

    public Consumer<String> getOnSaved() {
        return onSaved;
    }

    public void setOnBack(Runnable callback) { this.onBack = callback; }
    public void setOnSaved(Consumer<String> callback) { this.onSaved = callback; }

    public Runnable getOnBack() { return onBack; }

    public void loadRequest(String code) {
        if (controller != null) {
            controller.loadRequest(code);
        }
    }

    public void renderRequest(RequestResponse response) {
        this.current = response;
        this.editable = "pending".equals(response.getStatus());

        titleLabel.setText("Chỉnh sửa Yêu cầu: " + response.getCode());
        codeLabel.setText(response.getCode());
        createdDateLabel.setText(response.getCreatedDate());
        itemCountLabel.setText(String.valueOf(response.getItemCount()));

        statusLabel.setText(StatusStyle.requestStatusLabel(response.getStatus()));
        statusLabel.setStyle(StatusStyle.badgeStyle(response.getStatus()));

        itemsTable.setItems(response.getItems());
        response.getItems().addListener((javafx.collections.ListChangeListener<RequestItem>)
                c -> itemCountLabel.setText(String.valueOf(response.getItems().size())));
        applyEditableState();
    }

    private void applyEditableState() {
        if (addItemButton != null) {
            addItemButton.setDisable(!editable);
            addItemButton.setVisible(editable);
            addItemButton.setManaged(editable);
        }
        if (saveButton != null) saveButton.setDisable(!editable);
    }

    @FXML
    private void onAddItem() {
        if (addItemAction != null) {
            addItemAction.run();
        }
    }

    @FXML
    private void onSave() {
        if (saveAction != null) {
            saveAction.run();
        }
    }

    @FXML
    private void onBack() {
        if (backAction != null) {
            backAction.run();
        }
    }

    // --- Getters for Table and Columns ---
    public TableView<RequestItem> getItemsTable() { return itemsTable; }
    public TableColumn<RequestItem, String> getCodeColumn() { return codeColumn; }
    public TableColumn<RequestItem, String> getNameColumn() { return nameColumn; }
    public TableColumn<RequestItem, RequestItem> getQuantityColumn() { return quantityColumn; }
    public TableColumn<RequestItem, String> getUnitColumn() { return unitColumn; }
    public TableColumn<RequestItem, RequestItem> getDeliveryDateColumn() { return deliveryDateColumn; }
    public TableColumn<RequestItem, Void> getActionsColumn() { return actionsColumn; }
}

package com.importassignment.controllers;

import com.importassignment.models.ImportRequest;
import com.importassignment.models.Merchandise;
import com.importassignment.models.RequestItem;
import com.importassignment.services.MockDataService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Controller for Create Import Request Dialog
 */
public class CreateImportRequestDialogController implements Initializable {

    @FXML private VBox itemsContainer;
    @FXML private Label itemCountLabel;
    @FXML private Label emptyStateLabel;
    @FXML private Button saveDraftButton;
    @FXML private Button sendRequestButton;

    private ResourceBundle resources;
    private MockDataService dataService;
    private ObservableList<RequestItemCard> requestItems;
    private int itemIdCounter = 1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        this.dataService = MockDataService.getInstance();
        this.requestItems = FXCollections.observableArrayList();

        updateUI();
    }

    @FXML
    private void onAddItem() {
        showMerchandiseSelectionDialog();
    }

    private void showMerchandiseSelectionDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/MerchandiseSelectionDialog.fxml"),
                resources
            );
            Parent root = loader.load();

            MerchandiseSelectionDialogController controller = loader.getController();
            controller.setCallback(merchandise -> {
                addRequestItem(merchandise);
            });

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Chọn Mặt hàng");
            dialog.setScene(new Scene(root));
            dialog.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addRequestItem(Merchandise merchandise) {
        RequestItem item = new RequestItem(itemIdCounter++);
        item.setMerchandise(merchandise);

        RequestItemCard card = new RequestItemCard(item, requestItems.size() + 1);
        card.setOnRemove(() -> removeItem(card));
        card.setOnChange(() -> showMerchandiseSelectionDialog());

        requestItems.add(card);
        updateUI();
    }

    private void removeItem(RequestItemCard card) {
        requestItems.remove(card);
        updateUI();
        renumberItems();
    }

    private void renumberItems() {
        for (int i = 0; i < requestItems.size(); i++) {
            requestItems.get(i).setItemNumber(i + 1);
        }
    }

    private void updateUI() {
        itemsContainer.getChildren().clear();

        if (requestItems.isEmpty()) {
            emptyStateLabel.setVisible(true);
            emptyStateLabel.setManaged(true);
        } else {
            emptyStateLabel.setVisible(false);
            emptyStateLabel.setManaged(false);

            for (RequestItemCard card : requestItems) {
                itemsContainer.getChildren().add(card.getView());
            }
        }

        itemCountLabel.setText("Tổng: " + requestItems.size() + " mặt hàng");
        saveDraftButton.setDisable(requestItems.isEmpty());
        sendRequestButton.setDisable(requestItems.isEmpty());
    }

    @FXML
    private void onSaveDraft() {
        if (validateItems()) {
            ImportRequest request = createRequest("draft");
            dataService.addImportRequest(request);
            showSuccess("Đã lưu bản nháp");
            closeDialog();
        }
    }

    @FXML
    private void onSendRequest() {
        if (validateItems()) {
            ImportRequest request = createRequest("pending");
            dataService.addImportRequest(request);
            showSuccess("Đã gửi yêu cầu nhập hàng");
            closeDialog();
        }
    }

    private boolean validateItems() {
        for (RequestItemCard card : requestItems) {
            if (!card.isValid()) {
                showError("Vui lòng điền đầy đủ thông tin cho tất cả các mặt hàng");
                return false;
            }
        }
        return true;
    }

    private ImportRequest createRequest(String status) {
        String code = dataService.generateNextRequestCode();
        String date = LocalDate.now().toString();

        ImportRequest request = new ImportRequest(
            requestItems.size() + 1,
            code,
            date,
            requestItems.size(),
            status
        );

        for (RequestItemCard card : requestItems) {
            request.addItem(card.getRequestItem());
        }

        return request;
    }

    @FXML
    private void onCancel() {
        closeDialog();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thành công");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeDialog() {
        Stage stage = (Stage) itemsContainer.getScene().getWindow();
        stage.close();
    }

    /**
     * Inner class representing a Request Item Card UI component
     */
    private class RequestItemCard {
        private final RequestItem item;
        private int itemNumber;
        private VBox view;

        private Label numberLabel;
        private Label nameLabel;
        private Label codeLabel;
        private Label categoryLabel;
        private Label priceLabel;
        private TextField quantityField;
        private TextField unitField;
        private DatePicker datePicker;

        private Runnable onRemove;
        private Runnable onChange;

        public RequestItemCard(RequestItem item, int itemNumber) {
            this.item = item;
            this.itemNumber = itemNumber;
            createView();
        }

        private void createView() {
            view = new VBox(12);
            view.getStyleClass().add("request-item-card");
            view.setStyle(
                "-fx-background-color: white; " +
                "-fx-border-color: #E5E7EB; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 8; " +
                "-fx-background-radius: 8; " +
                "-fx-padding: 20;"
            );

            // Header
            HBox header = new HBox(12);
            header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

            Label badge = new Label(String.valueOf(itemNumber));
            badge.setStyle(
                "-fx-background-color: #DBEAFE; " +
                "-fx-text-fill: #1E40AF; " +
                "-fx-background-radius: 16; " +
                "-fx-min-width: 32; " +
                "-fx-min-height: 32; " +
                "-fx-alignment: center; " +
                "-fx-font-weight: bold;"
            );

            VBox nameBox = new VBox(2);
            nameLabel = new Label(item.getMerchandise() != null ? item.getMerchandise().getName() : "");
            nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            codeLabel = new Label(item.getMerchandise() != null ? "Mã: " + item.getMerchandise().getCode() : "");
            codeLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 12;");
            nameBox.getChildren().addAll(nameLabel, codeLabel);

            javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
            javafx.scene.layout.HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

            Button changeButton = new Button("Đổi mặt hàng");
            changeButton.setStyle(
                "-fx-background-color: transparent; " +
                "-fx-text-fill: #2563EB; " +
                "-fx-cursor: hand; " +
                "-fx-font-weight: bold;"
            );
            changeButton.setOnAction(e -> {
                if (onChange != null) onChange.run();
            });

            Button removeButton = new Button("🗑");
            removeButton.setStyle("-fx-cursor: hand; -fx-background-color: transparent;");
            removeButton.setOnAction(e -> {
                if (onRemove != null) onRemove.run();
            });

            header.getChildren().addAll(badge, nameBox, spacer, changeButton, removeButton);

            // Fields grid
            javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
            grid.setHgap(12);
            grid.setVgap(12);

            // Quantity
            Label qtyLabel = new Label("Số lượng *");
            qtyLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12;");
            quantityField = new TextField();
            quantityField.setPromptText("Nhập số lượng");
            quantityField.textProperty().addListener((obs, old, newVal) -> {
                if (newVal != null && !newVal.isEmpty()) {
                    try {
                        item.setQuantity(Integer.parseInt(newVal));
                    } catch (NumberFormatException ignored) {}
                }
            });

            grid.add(qtyLabel, 0, 0);
            grid.add(quantityField, 0, 1);

            // Unit
            Label unitLabel = new Label("Đơn vị");
            unitLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12;");
            unitField = new TextField();
            unitField.setDisable(true);
            unitField.setText(item.getMerchandise() != null ? item.getMerchandise().getUnit() : "");
            unitField.setStyle("-fx-opacity: 0.6;");

            grid.add(unitLabel, 1, 0);
            grid.add(unitField, 1, 1);

            // Date
            Label dateLabel = new Label("Ngày nhận mong muốn *");
            dateLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12;");
            datePicker = new DatePicker();
            datePicker.setPromptText("Chọn ngày");
            datePicker.valueProperty().addListener((obs, old, newVal) -> {
                item.setDeliveryDate(newVal);
            });

            grid.add(dateLabel, 2, 0);
            grid.add(datePicker, 2, 1);

            // Details
            if (item.getMerchandise() != null) {
                javafx.scene.layout.HBox detailsBox = new javafx.scene.layout.HBox(20);
                detailsBox.setStyle("-fx-padding: 12 0 0 0; -fx-border-width: 1 0 0 0; -fx-border-color: #E5E7EB;");

                categoryLabel = new Label("Danh mục: " + item.getMerchandise().getCategory());
                categoryLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 12;");

                priceLabel = new Label("Giá: " + item.getMerchandise().getPrice());
                priceLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 12;");

                detailsBox.getChildren().addAll(categoryLabel, priceLabel);
                view.getChildren().addAll(header, grid, detailsBox);
            } else {
                view.getChildren().addAll(header, grid);
            }
        }

        public VBox getView() {
            return view;
        }

        public RequestItem getRequestItem() {
            return item;
        }

        public boolean isValid() {
            return item.getMerchandise() != null &&
                   quantityField.getText() != null &&
                   !quantityField.getText().isEmpty() &&
                   item.getQuantity() > 0 &&
                   datePicker.getValue() != null;
        }

        public void setItemNumber(int number) {
            this.itemNumber = number;
            // Update badge in view
            createView();
        }

        public void setOnRemove(Runnable onRemove) {
            this.onRemove = onRemove;
        }

        public void setOnChange(Runnable onChange) {
            this.onChange = onChange;
        }
    }
}

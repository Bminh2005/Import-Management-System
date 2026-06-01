package com.importassignment.controllers;

import com.importassignment.models.ImportRequest;
import com.importassignment.services.MockDataService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for Import Requests View
 */
public class ImportRequestsViewController implements Initializable {

    @FXML private TableView<ImportRequest> requestsTable;
    @FXML private TableColumn<ImportRequest, String> codeColumn;
    @FXML private TableColumn<ImportRequest, String> dateColumn;
    @FXML private TableColumn<ImportRequest, Integer> itemCountColumn;
    @FXML private TableColumn<ImportRequest, String> statusColumn;

    private ResourceBundle resources;
    private MockDataService dataService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        this.dataService = MockDataService.getInstance();

        setupTable();
        loadData();
    }

    private void setupTable() {
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        itemCountColumn.setCellValueFactory(new PropertyValueFactory<>("itemCount"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Status column with badges
        statusColumn.setCellFactory(column -> new TableCell<ImportRequest, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(getStatusText(status));
                    setStyle(getStatusStyle(status));
                }
            }
        });

        // Action column
        TableColumn<ImportRequest, Void> actionColumn = new TableColumn<>("Thao tác");
        actionColumn.setCellFactory(column -> new TableCell<>() {
            private final Button viewButton = new Button("Xem");
            private final Button sendButton = new Button("Gửi");
            private final Button editButton = new Button("Sửa");

            {
                viewButton.setStyle("-fx-background-color: #2563EB; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 6 12;");
                sendButton.setStyle("-fx-background-color: #10B981; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 6 12;");
                editButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #6B7280; -fx-cursor: hand; -fx-padding: 6 12;");

                viewButton.setOnAction(e -> {
                    ImportRequest req = getTableView().getItems().get(getIndex());
                    handleView(req);
                });

                sendButton.setOnAction(e -> {
                    ImportRequest req = getTableView().getItems().get(getIndex());
                    handleSend(req);
                });

                editButton.setOnAction(e -> {
                    ImportRequest req = getTableView().getItems().get(getIndex());
                    handleEdit(req);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    ImportRequest req = getTableView().getItems().get(getIndex());
                    javafx.scene.layout.HBox buttons = new javafx.scene.layout.HBox(8);

                    buttons.getChildren().add(viewButton);
                    if (req.isDraft()) {
                        buttons.getChildren().addAll(sendButton, editButton);
                    }

                    setGraphic(buttons);
                }
            }
        });

        requestsTable.getColumns().add(actionColumn);
    }

    private void loadData() {
        requestsTable.setItems(dataService.getAllImportRequests());
    }

    private String getStatusText(String status) {
        switch (status) {
            case "pending": return "Chờ xử lý";
            case "processing": return "Đang xử lý";
            case "completed": return "Hoàn thành";
            case "cancelled": return "Đã hủy";
            case "draft": return "Bản nháp";
            default: return status;
        }
    }

    private String getStatusStyle(String status) {
        String baseStyle = "-fx-padding: 6 12; -fx-border-radius: 12; -fx-background-radius: 12; -fx-font-weight: bold;";
        switch (status) {
            case "pending":
                return baseStyle + "-fx-background-color: #FEF3C7; -fx-text-fill: #92400E;";
            case "processing":
                return baseStyle + "-fx-background-color: #DBEAFE; -fx-text-fill: #1E40AF;";
            case "completed":
                return baseStyle + "-fx-background-color: #D1FAE5; -fx-text-fill: #065F46;";
            case "cancelled":
                return baseStyle + "-fx-background-color: #FEE2E2; -fx-text-fill: #991B1B;";
            case "draft":
                return baseStyle + "-fx-background-color: #F3F4F6; -fx-text-fill: #1F2937;";
            default:
                return baseStyle;
        }
    }

    @FXML
    private void onCreateNew() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/CreateImportRequestDialog.fxml"),
                resources
            );
            Parent root = loader.load();

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Tạo Yêu cầu Nhập hàng Mới");
            dialog.setScene(new Scene(root));
            dialog.showAndWait();

            // Refresh table
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleView(ImportRequest request) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/ViewImportRequestDialog.fxml"),
                resources
            );
            Parent root = loader.load();

            ViewImportRequestDialogController controller = loader.getController();
            controller.setImportRequest(request);

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Chi tiết Yêu cầu - " + request.getCode());
            dialog.setScene(new Scene(root));
            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Lỗi", "Không thể mở chi tiết yêu cầu: " + e.getMessage());
        }
    }

    private void handleSend(ImportRequest request) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận gửi");
        confirm.setHeaderText("Gửi yêu cầu nhập hàng?");
        confirm.setContentText("Yêu cầu " + request.getCode() + " sẽ được gửi đến Bộ phận Đặt hàng Quốc tế.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                request.setStatus("pending");
                loadData();
                showSuccess("Đã gửi yêu cầu thành công");
            }
        });
    }

    private void handleEdit(ImportRequest request) {
        System.out.println("Edit request: " + request.getCode());
        // Show edit dialog
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thành công");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

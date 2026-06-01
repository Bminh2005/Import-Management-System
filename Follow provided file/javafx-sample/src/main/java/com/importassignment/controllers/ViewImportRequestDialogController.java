package com.importassignment.controllers;

import com.importassignment.models.ImportRequest;
import com.importassignment.models.Order;
import com.importassignment.models.RequestItem;
import com.importassignment.services.MockDataService;
import javafx.collections.ObservableList;
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
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Controller for View Import Request Dialog
 */
public class ViewImportRequestDialogController implements Initializable {

    @FXML private Label titleLabel;
    @FXML private Label subtitleLabel;
    @FXML private Label requestCodeLabel;
    @FXML private Label createdDateLabel;
    @FXML private Label statusLabel;
    @FXML private Label itemCountLabel;
    @FXML private Label orderCountLabel;
    @FXML private Label noOrdersLabel;

    @FXML private TableView<RequestItem> requestItemsTable;
    @FXML private TableColumn<RequestItem, String> itemCodeColumn;
    @FXML private TableColumn<RequestItem, String> itemNameColumn;
    @FXML private TableColumn<RequestItem, Integer> itemQuantityColumn;
    @FXML private TableColumn<RequestItem, String> itemUnitColumn;
    @FXML private TableColumn<RequestItem, LocalDate> itemDeliveryDateColumn;

    @FXML private TableView<Order> ordersTable;
    @FXML private TableColumn<Order, String> orderCodeColumn;
    @FXML private TableColumn<Order, LocalDate> orderDateColumn;
    @FXML private TableColumn<Order, String> orderSiteColumn;
    @FXML private TableColumn<Order, Integer> orderItemsColumn;
    @FXML private TableColumn<Order, String> orderStatusColumn;
    @FXML private TableColumn<Order, Void> orderActionsColumn;

    private ResourceBundle resources;
    private MockDataService dataService;
    private ImportRequest currentRequest;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        this.dataService = MockDataService.getInstance();

        setupRequestItemsTable();
        setupOrdersTable();
    }

    private void setupRequestItemsTable() {
        itemCodeColumn.setCellValueFactory(cellData ->
            cellData.getValue().getMerchandise().codeProperty());
        itemNameColumn.setCellValueFactory(cellData ->
            cellData.getValue().getMerchandise().nameProperty());
        itemQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        itemUnitColumn.setCellValueFactory(cellData ->
            cellData.getValue().getMerchandise().unitProperty());
        itemDeliveryDateColumn.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));
    }

    private void setupOrdersTable() {
        orderCodeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        orderSiteColumn.setCellValueFactory(new PropertyValueFactory<>("assignedSite"));

        // Item count column
        orderItemsColumn.setCellFactory(column -> new TableCell<Order, Integer>() {
            @Override
            protected void updateItem(Integer count, boolean empty) {
                super.updateItem(count, empty);
                if (empty) {
                    setText(null);
                } else {
                    Order order = getTableView().getItems().get(getIndex());
                    setText(String.valueOf(order.getItemCount()));
                }
            }
        });

        // Status column with badges
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        orderStatusColumn.setCellFactory(column -> new TableCell<Order, String>() {
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

        // Actions column
        orderActionsColumn.setCellFactory(column -> new TableCell<>() {
            private final Button viewButton = new Button("Xem");

            {
                viewButton.setStyle("-fx-background-color: #2563EB; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 6 12;");
                viewButton.setOnAction(e -> {
                    Order order = getTableView().getItems().get(getIndex());
                    handleViewOrder(order);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(viewButton);
                }
            }
        });
    }

    public void setImportRequest(ImportRequest request) {
        this.currentRequest = request;
        loadRequestData();
        loadRelatedOrders();
    }

    private void loadRequestData() {
        if (currentRequest == null) return;

        requestCodeLabel.setText(currentRequest.getCode());
        createdDateLabel.setText(currentRequest.getCreatedDate());
        itemCountLabel.setText(String.valueOf(currentRequest.getItemCount()));

        // Set status badge
        String status = currentRequest.getStatus();
        statusLabel.setText(getStatusText(status));
        statusLabel.setStyle(getStatusStyle(status));

        // Load request items
        requestItemsTable.setItems(currentRequest.getItems());
    }

    private void loadRelatedOrders() {
        if (currentRequest == null) return;

        ObservableList<Order> orders = dataService.getOrdersByRequestCode(currentRequest.getCode());
        ordersTable.setItems(orders);

        // Update order count label
        orderCountLabel.setText("(" + orders.size() + " đơn)");

        // Show/hide empty state
        boolean hasOrders = !orders.isEmpty();
        noOrdersLabel.setVisible(!hasOrders);
        noOrdersLabel.setManaged(!hasOrders);
        ordersTable.setVisible(hasOrders);
        ordersTable.setManaged(hasOrders);
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

    private void handleViewOrder(Order order) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/ViewOrderDetailsDialog.fxml"),
                resources
            );
            Parent root = loader.load();

            ViewOrderDetailsDialogController controller = loader.getController();
            controller.setOrder(order);

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Chi tiết Đơn hàng - " + order.getCode());
            dialog.setScene(new Scene(root));
            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Lỗi", "Không thể mở chi tiết đơn hàng: " + e.getMessage());
        }
    }

    @FXML
    private void onClose() {
        Stage stage = (Stage) titleLabel.getScene().getWindow();
        stage.close();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

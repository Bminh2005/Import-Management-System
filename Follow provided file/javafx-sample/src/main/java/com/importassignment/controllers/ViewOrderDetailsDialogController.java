package com.importassignment.controllers;

import com.importassignment.models.Order;
import com.importassignment.models.OrderItem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controller for View Order Details Dialog
 */
public class ViewOrderDetailsDialogController implements Initializable {

    @FXML private Label titleLabel;
    @FXML private Label orderCodeLabel;
    @FXML private Label orderDateLabel;
    @FXML private Label statusLabel;
    @FXML private Label requestCodeLabel;
    @FXML private Label siteLabel;
    @FXML private Label totalValueLabel;

    @FXML private TableView<OrderItem> orderItemsTable;
    @FXML private TableColumn<OrderItem, String> itemCodeColumn;
    @FXML private TableColumn<OrderItem, String> itemNameColumn;
    @FXML private TableColumn<OrderItem, Integer> itemQuantityColumn;
    @FXML private TableColumn<OrderItem, String> itemUnitColumn;
    @FXML private TableColumn<OrderItem, Double> itemUnitPriceColumn;
    @FXML private TableColumn<OrderItem, Double> itemTotalPriceColumn;
    @FXML private TableColumn<OrderItem, LocalDate> itemDeliveryDateColumn;

    private ResourceBundle resources;
    private Order currentOrder;
    private NumberFormat currencyFormat;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        setupOrderItemsTable();
    }

    private void setupOrderItemsTable() {
        itemCodeColumn.setCellValueFactory(cellData ->
            cellData.getValue().getMerchandise().codeProperty());
        itemNameColumn.setCellValueFactory(cellData ->
            cellData.getValue().getMerchandise().nameProperty());
        itemQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        itemUnitColumn.setCellValueFactory(cellData ->
            cellData.getValue().getMerchandise().unitProperty());

        // Unit price column with currency formatting
        itemUnitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        itemUnitPriceColumn.setCellFactory(column -> new TableCell<OrderItem, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });

        // Total price column
        itemTotalPriceColumn.setCellFactory(column -> new TableCell<OrderItem, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    OrderItem item = getTableView().getItems().get(getIndex());
                    setText(currencyFormat.format(item.getTotalPrice()));
                    setStyle("-fx-font-weight: bold; -fx-text-fill: #2563EB;");
                }
            }
        });

        itemDeliveryDateColumn.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));
    }

    public void setOrder(Order order) {
        this.currentOrder = order;
        loadOrderData();
    }

    private void loadOrderData() {
        if (currentOrder == null) return;

        orderCodeLabel.setText(currentOrder.getCode());
        orderDateLabel.setText(currentOrder.getOrderDate().toString());
        requestCodeLabel.setText(currentOrder.getImportRequestCode());
        siteLabel.setText(currentOrder.getAssignedSite());
        totalValueLabel.setText(currencyFormat.format(currentOrder.getTotalValue()));

        // Set status badge
        String status = currentOrder.getStatus();
        statusLabel.setText(getStatusText(status));
        statusLabel.setStyle(getStatusStyle(status));

        // Load order items
        orderItemsTable.setItems(currentOrder.getItems());
    }

    private String getStatusText(String status) {
        switch (status) {
            case "pending": return "Chờ xử lý";
            case "processing": return "Đang xử lý";
            case "completed": return "Hoàn thành";
            case "cancelled": return "Đã hủy";
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
            default:
                return baseStyle;
        }
    }

    @FXML
    private void onClose() {
        Stage stage = (Stage) titleLabel.getScene().getWindow();
        stage.close();
    }
}

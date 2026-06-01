package com.importassignment.controllers;

import com.importassignment.models.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private GridPane metricsGrid;

    @FXML
    private TableView<Order> recentOrdersTable;

    @FXML
    private TableColumn<Order, String> orderIdColumn;

    @FXML
    private TableColumn<Order, String> dateColumn;

    @FXML
    private TableColumn<Order, String> statusColumn;

    @FXML
    private TableColumn<Order, String> siteColumn;

    @FXML
    private TableColumn<Order, Integer> itemsColumn;

    @FXML
    private TableColumn<Order, Void> actionsColumn;

    private ResourceBundle resourceBundle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;

        setupMetricsCards();
        setupRecentOrdersTable();
        loadSampleData();
    }

    private void setupMetricsCards() {
        metricsGrid.getChildren().clear();

        // Create 4 metric cards
        addMetricCard(0, 0, "24", "Đơn hàng Chờ xử lý", "+12%", true);
        addMetricCard(1, 0, "42", "Site Đang hoạt động", "+3", true);
        addMetricCard(2, 0, "87%", "Hàng Trong kho", "-2%", false);
        addMetricCard(3, 0, "156", "Giao hàng Tháng này", "+28%", true);
    }

    private void addMetricCard(int col, int row, String value, String label, String trend, boolean positive) {
        VBox card = new VBox(12);
        card.getStyleClass().add("metric-card");
        card.setPadding(new Insets(24));
        card.setMaxWidth(Double.MAX_VALUE);

        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("metric-value");
        valueLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");

        Label titleLabel = new Label(label);
        titleLabel.getStyleClass().add("metric-label");
        titleLabel.setStyle("-fx-text-fill: #6B7280;");

        Label trendLabel = new Label(trend);
        trendLabel.setStyle(
            "-fx-font-size: 14px; -fx-text-fill: " +
            (positive ? "#10B981" : "#EF4444")
        );

        card.getChildren().addAll(valueLabel, titleLabel, trendLabel);
        GridPane.setConstraints(card, col, row);
        metricsGrid.getChildren().add(card);
    }

    private void setupRecentOrdersTable() {
        // Setup columns
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        siteColumn.setCellValueFactory(new PropertyValueFactory<>("site"));
        itemsColumn.setCellValueFactory(new PropertyValueFactory<>("totalItems"));

        // Status column with custom cell factory for colored badges
        statusColumn.setCellFactory(col -> new TableCell<Order, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label badge = new Label(status);
                    badge.getStyleClass().add("status-badge");

                    // Set color based on status
                    String color = switch (status) {
                        case "Pending Review", "Chờ Xét duyệt" -> "#F59E0B";
                        case "Processing", "Đang Xử lý" -> "#2563EB";
                        case "Completed", "Hoàn thành" -> "#10B981";
                        default -> "#6B7280";
                    };

                    badge.setStyle(
                        "-fx-background-color: " + color + "22; " +
                        "-fx-text-fill: " + color + "; " +
                        "-fx-padding: 4 12; " +
                        "-fx-background-radius: 12;"
                    );

                    setGraphic(badge);
                    setText(null);
                }
            }
        });

        // Actions column with buttons
        actionsColumn.setCellFactory(col -> new TableCell<Order, Void>() {
            private final Button viewBtn = new Button("👁");

            {
                viewBtn.getStyleClass().add("icon-button");
                viewBtn.setOnAction(e -> {
                    Order order = getTableView().getItems().get(getIndex());
                    showOrderDetails(order);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(viewBtn);
                }
            }
        });
    }

    private void loadSampleData() {
        ObservableList<Order> orders = FXCollections.observableArrayList(
            new Order("ORD-20260508-001", "2026-05-08", "Chờ Xét duyệt", "Site CN-01", 12),
            new Order("ORD-20260507-045", "2026-05-07", "Đang Xử lý", "Site US-03", 8),
            new Order("ORD-20260507-032", "2026-05-07", "Hoàn thành", "Site JP-02", 15),
            new Order("ORD-20260506-089", "2026-05-06", "Đang Xử lý", "Site DE-01", 6),
            new Order("ORD-20260506-071", "2026-05-06", "Có Vấn đề", "Site UK-04", 10)
        );

        recentOrdersTable.setItems(orders);
    }

    @FXML
    private void onCreateNewOrder() {
        showInfo("Tạo đơn hàng mới");
        // Navigate to create order view
    }

    @FXML
    private void onCheckInventory() {
        showInfo("Kiểm tra tồn kho");
    }

    @FXML
    private void onViewReports() {
        showInfo("Xem báo cáo");
    }

    @FXML
    private void onViewAllOrders() {
        showInfo("Xem tất cả đơn hàng");
        // Navigate to orders list
    }

    private void showOrderDetails(Order order) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Chi tiết Đơn hàng");
        alert.setHeaderText("Đơn hàng: " + order.getOrderId());
        alert.setContentText(
            "Ngày: " + order.getDate() + "\n" +
            "Trạng thái: " + order.getStatus() + "\n" +
            "Site: " + order.getSite() + "\n" +
            "Số mặt hàng: " + order.getTotalItems()
        );
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

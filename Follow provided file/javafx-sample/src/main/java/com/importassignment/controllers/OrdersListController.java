package com.importassignment.controllers;

import com.importassignment.models.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class OrdersListController implements Initializable {

    @FXML private TextField searchField;
    @FXML private HBox statusFilters;
    @FXML private HBox selectedBadge;
    @FXML private Label selectedCountLabel;

    @FXML private TableView<Order> ordersTable;
    @FXML private TableColumn<Order, Boolean> selectColumn;
    @FXML private TableColumn<Order, String> orderIdColumn;
    @FXML private TableColumn<Order, String> dateColumn;
    @FXML private TableColumn<Order, Integer> itemsColumn;
    @FXML private TableColumn<Order, String> statusColumn;
    @FXML private TableColumn<Order, String> priorityColumn;
    @FXML private TableColumn<Order, String> deliveryColumn;
    @FXML private TableColumn<Order, Integer> sitesColumn;
    @FXML private TableColumn<Order, Integer> completionColumn;
    @FXML private TableColumn<Order, Void> actionsColumn;

    @FXML private ContextMenu tableContextMenu;
    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Label paginationLabel;

    private ObservableList<Order> allOrders;
    private Set<Order> selectedOrders = new HashSet<>();
    private ResourceBundle resourceBundle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;

        setupTable();
        setupStatusFilters();
        loadSampleData();

        // Context menu on right-click
        ordersTable.setContextMenu(tableContextMenu);
    }

    private void setupTable() {
        // Selection column with checkboxes
        selectColumn.setCellFactory(col -> new TableCell<Order, Boolean>() {
            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.setOnAction(e -> {
                    Order order = getTableView().getItems().get(getIndex());
                    if (checkBox.isSelected()) {
                        selectedOrders.add(order);
                    } else {
                        selectedOrders.remove(order);
                    }
                    updateSelectedBadge();
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(checkBox);
                }
            }
        });

        // Basic columns
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        itemsColumn.setCellValueFactory(new PropertyValueFactory<>("totalItems"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        completionColumn.setCellValueFactory(new PropertyValueFactory<>("completion"));

        // Status column with badges
        statusColumn.setCellFactory(col -> new TableCell<Order, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                } else {
                    Label badge = createStatusBadge(status);
                    setGraphic(badge);
                }
            }
        });

        // Priority column with badges
        priorityColumn.setCellFactory(col -> new TableCell<Order, String>() {
            @Override
            protected void updateItem(String priority, boolean empty) {
                super.updateItem(priority, empty);
                if (empty || priority == null) {
                    setGraphic(null);
                } else {
                    Label badge = createPriorityBadge(priority);
                    setGraphic(badge);
                }
            }
        });

        // Completion column with progress bar
        completionColumn.setCellFactory(col -> new TableCell<Order, Integer>() {
            @Override
            protected void updateItem(Integer completion, boolean empty) {
                super.updateItem(completion, empty);
                if (empty || completion == null) {
                    setGraphic(null);
                } else {
                    ProgressBar progressBar = new ProgressBar(completion / 100.0);
                    progressBar.setPrefWidth(100);
                    Label percentLabel = new Label(completion + "%");
                    HBox box = new HBox(8, progressBar, percentLabel);
                    box.setAlignment(Pos.CENTER_LEFT);
                    setGraphic(box);
                }
            }
        });

        // Actions column
        actionsColumn.setCellFactory(col -> new TableCell<Order, Void>() {
            private final Button viewBtn = new Button("👁");
            private final Button editBtn = new Button("✏️");
            private final Button deleteBtn = new Button("🗑️");

            {
                viewBtn.setOnAction(e -> onViewDetails());
                editBtn.setOnAction(e -> onEditOrder());
                deleteBtn.setOnAction(e -> onCancelOrder());

                viewBtn.getStyleClass().add("icon-button");
                editBtn.getStyleClass().add("icon-button");
                deleteBtn.getStyleClass().add("icon-button");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(4, viewBtn, editBtn, deleteBtn);
                    setGraphic(buttons);
                }
            }
        });

        // Row click selection
        ordersTable.setRowFactory(tv -> {
            TableRow<Order> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    onViewDetails();
                }
            });
            return row;
        });
    }

    private void setupStatusFilters() {
        String[] filters = {
            "Tất cả (24)",
            "Chờ Xét duyệt (8)",
            "Đang Xử lý (10)",
            "Hoàn thành Một phần (3)",
            "Hoàn thành (2)",
            "Có Vấn đề (1)"
        };

        for (int i = 0; i < filters.length; i++) {
            ToggleButton btn = new ToggleButton(filters[i]);
            btn.getStyleClass().add("filter-chip");
            if (i == 0) {
                btn.setSelected(true);
                btn.getStyleClass().add("active");
            }

            btn.setOnAction(e -> {
                // Clear other selections
                statusFilters.getChildren().forEach(node -> {
                    if (node instanceof ToggleButton) {
                        ((ToggleButton) node).setSelected(false);
                        node.getStyleClass().remove("active");
                    }
                });
                btn.setSelected(true);
                btn.getStyleClass().add("active");
            });

            statusFilters.getChildren().add(btn);
        }
    }

    private void loadSampleData() {
        allOrders = FXCollections.observableArrayList(
            createOrder("ORD-20260508-001", "2026-05-08 09:30", 12, "Chờ Xét duyệt", "High", 0),
            createOrder("ORD-20260507-045", "2026-05-07 14:20", 8, "Đang Xử lý", "Medium", 45),
            createOrder("ORD-20260507-032", "2026-05-07 10:15", 15, "Hoàn thành", "Medium", 100),
            createOrder("ORD-20260506-089", "2026-05-06 16:45", 6, "Đang Xử lý", "Low", 30),
            createOrder("ORD-20260506-071", "2026-05-06 11:00", 10, "Có Vấn đề", "High", 65)
        );

        ordersTable.setItems(allOrders);
    }

    private Order createOrder(String id, String date, int items, String status, String priority, int completion) {
        Order order = new Order(id, date, status, "Site CN-01", items);
        order.setPriority(priority);
        order.setCompletion(completion);
        return order;
    }

    private Label createStatusBadge(String status) {
        Label badge = new Label(status);
        badge.getStyleClass().add("status-badge");

        String color = switch (status) {
            case "Chờ Xét duyệt" -> "#F59E0B";
            case "Đang Xử lý" -> "#2563EB";
            case "Hoàn thành" -> "#10B981";
            case "Có Vấn đề" -> "#EF4444";
            default -> "#6B7280";
        };

        badge.setStyle(
            "-fx-background-color: " + color + "22; " +
            "-fx-text-fill: " + color + "; " +
            "-fx-padding: 4 12; " +
            "-fx-background-radius: 12; " +
            "-fx-font-size: 12px;"
        );

        return badge;
    }

    private Label createPriorityBadge(String priority) {
        Label badge = new Label(priority);
        String color = switch (priority) {
            case "High" -> "#EF4444";
            case "Medium" -> "#F59E0B";
            case "Low" -> "#6B7280";
            default -> "#6B7280";
        };

        badge.setStyle(
            "-fx-background-color: " + color + "22; " +
            "-fx-text-fill: " + color + "; " +
            "-fx-padding: 4 12; " +
            "-fx-background-radius: 12; " +
            "-fx-font-size: 12px;"
        );

        return badge;
    }

    private void updateSelectedBadge() {
        if (selectedOrders.isEmpty()) {
            selectedBadge.setVisible(false);
        } else {
            selectedBadge.setVisible(true);
            selectedCountLabel.setText(selectedOrders.size() + " đã chọn");
        }
    }

    @FXML
    private void clearSelection() {
        selectedOrders.clear();
        updateSelectedBadge();
        ordersTable.refresh();
    }

    @FXML
    private void onCreateOrder() {
        showInfo("Tạo đơn hàng mới");
    }

    @FXML
    private void refreshData() {
        loadSampleData();
    }

    @FXML
    private void onViewDetails() {
        Order selected = ordersTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showInfo("Xem chi tiết: " + selected.getOrderId());
        }
    }

    @FXML
    private void onEditOrder() {
        showInfo("Sửa đơn hàng");
    }

    @FXML
    private void onDuplicateOrder() {
        showInfo("Nhân bản đơn hàng");
    }

    @FXML
    private void onCancelOrder() {
        showConfirm("Bạn có chắc chắn muốn hủy đơn hàng này?");
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showConfirm(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

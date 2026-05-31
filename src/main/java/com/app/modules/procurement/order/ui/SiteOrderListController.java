package com.app.modules.procurement.order.ui;

import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import com.app.modules.procurement.order.model.OrderStatus;
import com.app.modules.procurement.order.model.SiteOrder;
import com.app.modules.procurement.order.service.SiteOrderService;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SiteOrderListController implements Initializable {

    @FXML private Label lblTotal;
    @FXML private Label lblPending;
    @FXML private Label lblAccepted;
    @FXML private Label lblRefused;

    @FXML private TextField searchField;
    @FXML private ToggleGroup statusToggleGroup;
    @FXML private ToggleButton tabAll;
    @FXML private ToggleButton tabPending;
    @FXML private ToggleButton tabAccepted;
    @FXML private ToggleButton tabRefused;

    @FXML private TableView<SiteOrder> orderTable;
    @FXML private TableColumn<SiteOrder, String> colOrderId;
    @FXML private TableColumn<SiteOrder, String> colOrderer;
    @FXML private TableColumn<SiteOrder, String> colItemCount;
    @FXML private TableColumn<SiteOrder, String> colSiteCode;
    @FXML private TableColumn<SiteOrder, String> colSiteName;
    @FXML private TableColumn<SiteOrder, String> colDelivery;
    @FXML private TableColumn<SiteOrder, String> colTotalValue;
    @FXML private TableColumn<SiteOrder, String> colStatus;
    @FXML private TableColumn<SiteOrder, Object> colAction;

    private final SiteOrderService service = new SiteOrderService();
    private ObservableList<SiteOrder> allOrders;
    private FilteredList<SiteOrder> filteredOrders;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tabAll.setSelected(true);
        validateInjectedFields();
        setupTableColumns();
        setupFilters();
        loadOrders();
    }

    private void validateInjectedFields() {
        Objects.requireNonNull(lblTotal);
        Objects.requireNonNull(lblPending);
        Objects.requireNonNull(lblAccepted);
        Objects.requireNonNull(lblRefused);
        Objects.requireNonNull(searchField);
        Objects.requireNonNull(statusToggleGroup);
        Objects.requireNonNull(tabAll);
        Objects.requireNonNull(tabPending);
        Objects.requireNonNull(tabAccepted);
        Objects.requireNonNull(tabRefused);
        Objects.requireNonNull(orderTable);
        Objects.requireNonNull(colOrderId);
        Objects.requireNonNull(colOrderer);
        Objects.requireNonNull(colItemCount);
        Objects.requireNonNull(colSiteCode);
        Objects.requireNonNull(colSiteName);
        Objects.requireNonNull(colDelivery);
        Objects.requireNonNull(colTotalValue);
        Objects.requireNonNull(colStatus);
        Objects.requireNonNull(colAction);
    }

    private void setupTableColumns() {
        colOrderId.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(String.valueOf(cell.getValue().getId())));
        colOrderer.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(cell.getValue().getOrdererName()));
        colItemCount.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(String.valueOf(cell.getValue().getItemCount())));
        colSiteCode.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(String.valueOf(cell.getValue().getSiteId())));
        colSiteName.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(cell.getValue().getSiteName()));
        colDelivery.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(formatDelivery(cell.getValue().getDelivery())));
        colTotalValue.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(formatMoney(cell.getValue().getItems().stream()
                        .mapToDouble(i -> i.getPrice() * i.getQuantity()).sum())));
        colStatus.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(formatStatus(cell.getValue().getStatus())));

        colAction.setCellFactory(column -> new TableCell<>() {
            private final Button detailButton = new Button("Chi tiết");
            private final Button reallocButton = new Button("Phân bổ");
            private final HBox container = new HBox(8, detailButton, reallocButton);

            {
                detailButton.getStyleClass().add("btn-link");
                reallocButton.getStyleClass().add("btn-realloc");
            }

            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }
                SiteOrder order = getTableRow().getItem();
                detailButton.setOnAction(event -> openDetail(order));
                reallocButton.setOnAction(event -> openReallocation(order));
                setGraphic(container);
            }
        });
    }

    private void setupFilters() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> refreshFilter());
        statusToggleGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> refreshFilter());
    }

    private void loadOrders() {
        List<SiteOrder> orders = service.getAllOrders();
        allOrders = FXCollections.observableArrayList(orders);
        filteredOrders = new FilteredList<>(allOrders, this::matchesFilter);
        orderTable.setItems(filteredOrders);
        refreshStats();
    }

    private boolean matchesFilter(SiteOrder order) {
        if (order == null) {
            return false;
        }
        String filterText = searchField.getText();
        boolean matchesSearch = filterText == null || filterText.isBlank()
                || order.getOrdererName().toLowerCase(Locale.ROOT).contains(filterText.toLowerCase(Locale.ROOT))
                || String.valueOf(order.getId()).contains(filterText)
                || order.getSiteName().toLowerCase(Locale.ROOT).contains(filterText.toLowerCase(Locale.ROOT));
        if (!matchesSearch) {
            return false;
        }
        return matchesStatus(order);
    }

    private boolean matchesStatus(SiteOrder order) {
        if (tabPending.isSelected()) {
            return order.getStatus() == OrderStatus.PENDING || order.getStatus() == OrderStatus.PROCESSING;
        }
        if (tabAccepted.isSelected()) {
            return order.getStatus() == OrderStatus.ACCEPTED;
        }
        if (tabRefused.isSelected()) {
            return order.getStatus() == OrderStatus.REFUSED;
        }
        return true;
    }

    private void refreshFilter() {
        if (filteredOrders != null) {
            filteredOrders.setPredicate(this::matchesFilter);
        }
    }

    private void refreshStats() {
        if (allOrders == null) {
            return;
        }
        lblTotal.setText(String.valueOf(allOrders.size()));
        lblPending.setText(String.valueOf(
                allOrders.stream().filter(o -> o.getStatus() == OrderStatus.PENDING || o.getStatus() == OrderStatus.PROCESSING).count()));
        lblAccepted.setText(String.valueOf(
                allOrders.stream().filter(o -> o.getStatus() == OrderStatus.ACCEPTED).count()));
        lblRefused.setText(String.valueOf(
                allOrders.stream().filter(o -> o.getStatus() == OrderStatus.REFUSED).count()));
    }

    private String formatStatus(OrderStatus status) {
        return switch (status) {
            case ACCEPTED -> "Đã xử lý";
            case REFUSED -> "Bị hủy";
            case PROCESSING -> "Đang xử lý";
            default -> "Chưa xử lý";
        };
    }

    private String formatDelivery(Object delivery) {
        return delivery == null ? "-" : delivery.toString();
    }

    private String formatMoney(double value) {
        return NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN")).format(value) + " đ";
    }

    private void openDetail(SiteOrder order) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SiteOrderDetailView.fxml"));
            Parent root = loader.load();
            SiteOrderDetailController controller = loader.getController();
            controller.loadOrder(order.getId());
            Scene scene = orderTable.getScene();
            scene.setRoot(root);
            ((Stage) scene.getWindow()).setTitle("Đơn Đặt Hàng - Chi tiết");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void openReallocation(SiteOrder order) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SiteOrderReallocationView.fxml"));
            Parent root = loader.load();
            SiteOrderReallocationController controller = loader.getController();
            controller.loadOrder(order.getId());
            Scene scene = orderTable.getScene();
            scene.setRoot(root);
            ((Stage) scene.getWindow()).setTitle("Đơn Đặt Hàng - Phân bổ lại");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    private void handleViewDetail() {
        SiteOrder selected = orderTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openDetail(selected);
        }
    }

    @FXML
    private void handleReallocate() {
        SiteOrder selected = orderTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openReallocation(selected);
        }
    }
}

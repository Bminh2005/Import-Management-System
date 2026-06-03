package com.app.modules.procurement.order.ui;

import java.net.URL;
import java.time.format.DateTimeFormatter;
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
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

public class SiteOrderListController implements Initializable {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
    @FXML private TableColumn<SiteOrder, String> colSiteName;
    @FXML private TableColumn<SiteOrder, String> colCreatedAt;
    @FXML private TableColumn<SiteOrder, String> colStatus;
    @FXML private TableColumn<SiteOrder, Object> colAction;

    private final SiteOrderService service = SiteOrderNavigator.service();
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

    public void refresh() {
        loadOrders();
        updateTabCounts();
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
        Objects.requireNonNull(colSiteName);
        Objects.requireNonNull(colCreatedAt);
        Objects.requireNonNull(colStatus);
        Objects.requireNonNull(colAction);
    }

    private void setupTableColumns() {
        colOrderId.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(SiteOrderService.formatOrderCode(cell.getValue().getId())));
        colOrderer.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(nullToDash(cell.getValue().getOrdererName())));
        colItemCount.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(String.valueOf(cell.getValue().getItemCount())));
        colSiteName.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(nullToDash(cell.getValue().getSiteName())));
        colCreatedAt.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
                cell.getValue().getCreatedAt() == null ? "-" : cell.getValue().getCreatedAt().format(DATE_FMT)));

        colStatus.setCellFactory(col -> new TableCell<>() {
            private final Label badge = new Label();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }
                SiteOrder order = getTableRow().getItem();
                badge.setText(formatStatus(order.getStatus()));
                badge.getStyleClass().setAll("status-pill", statusStyle(order.getStatus()));
                setGraphic(badge);
                setText(null);
            }
        });
        colStatus.setCellValueFactory(cell -> new ReadOnlyStringWrapper(formatStatus(cell.getValue().getStatus())));

        colAction.setCellFactory(column -> new TableCell<>() {
            private final Button viewButton = new Button("Xem");
            private final Button processButton = new Button("Xử lý");
            private final HBox container = new HBox(8, viewButton, processButton);

            {
                viewButton.getStyleClass().add("btn-view");
                processButton.getStyleClass().add("btn-process");
                container.setAlignment(Pos.CENTER_LEFT);
                viewButton.setOnAction(e -> {
                    SiteOrder order = getTableRow() != null ? getTableRow().getItem() : null;
                    if (order != null) {
                        openDetail(order);
                    }
                });
                processButton.setOnAction(e -> {
                    SiteOrder order = getTableRow() != null ? getTableRow().getItem() : null;
                    if (order != null) {
                        openReallocation(order);
                    }
                });
            }

            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }
                SiteOrder order = getTableRow().getItem();
                boolean refused = order.getStatus() == OrderStatus.REFUSED;
                processButton.setVisible(refused);
                processButton.setManaged(refused);
                setGraphic(container);
            }
        });
    }

    private void setupFilters() {
        searchField.textProperty().addListener((o, ov, nv) -> refreshFilter());
        statusToggleGroup.selectedToggleProperty().addListener((o, ov, nv) -> refreshFilter());
    }

    private void loadOrders() {
        List<SiteOrder> orders = service.getAllOrders();
        allOrders = FXCollections.observableArrayList(orders);
        filteredOrders = new FilteredList<>(allOrders, this::matchesFilter);
        orderTable.setItems(filteredOrders);
        refreshStats();
        updateTabCounts();
    }

    private void updateTabCounts() {
        if (allOrders == null) {
            return;
        }
        long pending = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.PENDING
                || o.getStatus() == OrderStatus.PROCESSING).count();
        long accepted = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.ACCEPTED).count();
        long refused = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.REFUSED).count();
        tabAll.setText("Tất cả (" + allOrders.size() + ")");
        tabPending.setText("Chưa xử lý (" + pending + ")");
        tabAccepted.setText("Đã xử lý (" + accepted + ")");
        tabRefused.setText("Bị hủy (" + refused + ")");
    }

    private boolean matchesFilter(SiteOrder order) {
        if (order == null) {
            return false;
        }
        String filterText = searchField.getText();
        String needle = filterText == null ? "" : filterText.toLowerCase(Locale.ROOT);
        boolean matchesSearch = needle.isBlank()
                || nullToDash(order.getOrdererName()).toLowerCase(Locale.ROOT).contains(needle)
                || String.valueOf(order.getId()).contains(needle)
                || SiteOrderService.formatOrderCode(order.getId()).toLowerCase(Locale.ROOT).contains(needle)
                || nullToDash(order.getSiteName()).toLowerCase(Locale.ROOT).contains(needle);
        return matchesSearch && matchesStatus(order);
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
                allOrders.stream().filter(o -> o.getStatus() == OrderStatus.PENDING
                        || o.getStatus() == OrderStatus.PROCESSING).count()));
        lblAccepted.setText(String.valueOf(
                allOrders.stream().filter(o -> o.getStatus() == OrderStatus.ACCEPTED).count()));
        lblRefused.setText(String.valueOf(
                allOrders.stream().filter(o -> o.getStatus() == OrderStatus.REFUSED).count()));
    }

    private static String formatStatus(OrderStatus status) {
        return switch (status) {
            case ACCEPTED -> "Đã xử lý";
            case REFUSED -> "Bị hủy";
            case PROCESSING -> "Đang xử lý";
            default -> "Chưa xử lý";
        };
    }

    private static String statusStyle(OrderStatus status) {
        return switch (status) {
            case ACCEPTED -> "pill-green";
            case REFUSED -> "pill-red";
            case PROCESSING -> "pill-blue";
            default -> "pill-yellow";
        };
    }

    private static String nullToDash(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }

    private void openDetail(SiteOrder order) {
        SiteOrderNavigator.showDetail(orderTable.getScene(), order.getId());
    }

    private void openReallocation(SiteOrder order) {
        if (order.getStatus() == OrderStatus.REFUSED) {
            SiteOrderNavigator.showReallocation(orderTable.getScene(), order.getId());
        }
    }
}

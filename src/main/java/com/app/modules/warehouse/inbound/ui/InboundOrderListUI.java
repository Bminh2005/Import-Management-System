package com.app.modules.warehouse.inbound.ui;

import com.app.Ioms.navigation.WarehouseNavigation;
import com.app.common.util.FxmlUiHelper;
import com.app.modules.warehouse.dashboard.ui.WarehouseSidebar;
import com.app.modules.warehouse.common.ui.WarehouseInboundStatus;
import com.app.modules.warehouse.common.ui.WarehouseStatusCell;
import com.app.modules.warehouse.inbound.dto.InboundOrderResponse;
import com.app.modules.warehouse.inbound.service.InboundOrderService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.text.Normalizer;
import java.util.Comparator;
import java.util.Locale;

public class InboundOrderListUI extends BorderPane {
    private static final String ALL_STATUSES = "Tất cả trạng thái";

    private final InboundOrderService inboundOrderService = new InboundOrderService();

    @FXML
    private TextField keywordField;

    @FXML
    private ComboBox<String> statusFilter;

    @FXML
    private Label resultCountLabel;

    @FXML
    private TableView<InboundOrderResponse> inboundOrderTable;

    @FXML
    private TableColumn<InboundOrderResponse, String> orderCodeColumn;

    @FXML
    private TableColumn<InboundOrderResponse, String> requestCodeColumn;

    @FXML
    private TableColumn<InboundOrderResponse, String> supplierColumn;

    @FXML
    private TableColumn<InboundOrderResponse, String> receivedDateColumn;

    @FXML
    private TableColumn<InboundOrderResponse, String> expectedDateColumn;

    @FXML
    private TableColumn<InboundOrderResponse, Integer> itemCountColumn;

    @FXML
    private TableColumn<InboundOrderResponse, Integer> totalQuantityColumn;

    @FXML
    private TableColumn<InboundOrderResponse, String> statusColumn;

    @FXML
    private TableColumn<InboundOrderResponse, String> actionColumn;

    @FXML
    private WarehouseSidebar sidebar;

    private ObservableList<InboundOrderResponse> inboundOrders = FXCollections.observableArrayList();
    private ObservableList<InboundOrderResponse> allInboundOrders = FXCollections.observableArrayList();
    private boolean sortAscending;

    public InboundOrderListUI() {
        FxmlUiHelper.loadSelf(this, "InboundOrderListPage.fxml");
    }

    @FXML
    private void initialize() {
        sidebar.setActiveMenu("inbound");
        statusFilter.setItems(FXCollections.observableArrayList(
                ALL_STATUSES,
                WarehouseInboundStatus.label("PENDING"),
                WarehouseInboundStatus.label("PROCESSING"),
                WarehouseInboundStatus.label("IMPORTED"),
                WarehouseInboundStatus.label("MISMATCH")
        ));
        statusFilter.getSelectionModel().selectFirst();
        configureTable();
        keywordField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        statusFilter.valueProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        reloadInboundOrders();
    }

    @FXML
    private void onSearchClick() {
        System.out.println("Nội dung chức năng: Tìm kiếm đơn nhập kho - " + getKeyword());
        applyFilters();
    }

    @FXML
    private void onSortClick() {
        sortAscending = !sortAscending;
        Comparator<InboundOrderResponse> comparator = Comparator.comparing(InboundOrderResponse::getReceivedDate,
                Comparator.nullsLast(String::compareTo));
        if (!sortAscending) {
            comparator = comparator.reversed();
        }
        inboundOrders = FXCollections.observableArrayList(inboundOrders.stream()
                .sorted(comparator)
                .toList());
        setInboundOrders(inboundOrders);
    }

    private void configureTable() {
        orderCodeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOrderCode()));
        orderCodeColumn.setCellFactory(column -> new OrderLinkCell());
        requestCodeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRequestCode()));
        supplierColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSupplier()));
        receivedDateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReceivedDate()));
        expectedDateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getExpectedDate()));
        itemCountColumn.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getItemCount()).asObject());
        totalQuantityColumn.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getExpectedQuantity()).asObject());
        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatusCode()));
        statusColumn.setCellFactory(column -> new WarehouseStatusCell<>());
        actionColumn.setCellValueFactory(data -> new SimpleStringProperty("Xử lý →"));
        actionColumn.setCellFactory(column -> new ActionCell());
        inboundOrderTable.setRowFactory(tableView -> {
            TableRow<InboundOrderResponse> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    WarehouseNavigation.showInboundOrderProcess(inboundOrderTable, row.getItem().getOrderId());
                }
            });
            return row;
        });
        inboundOrderTable.setPlaceholder(new Label("Không tìm thấy đơn nhập kho phù hợp"));
    }

    private void reloadInboundOrders() {
        allInboundOrders = FXCollections.observableArrayList(inboundOrderService.getAllInboundOrders());
        applyFilters();
    }

    private void applyFilters() {
        if (allInboundOrders == null) {
            return;
        }
        String keyword = normalize(getKeyword());
        String selectedStatus = mapStatusCode(getSelectedStatus());
        ObservableList<InboundOrderResponse> filtered = FXCollections.observableArrayList(
                allInboundOrders.stream()
                        .filter(order -> keyword.isBlank()
                                || normalize(order.getOrderCode()).contains(keyword)
                                || normalize(order.getRequestCode()).contains(keyword)
                                || normalize(order.getSupplier()).contains(keyword))
                        .filter(order -> matchesSelectedStatus(order, selectedStatus))
                        .toList()
        );
        setInboundOrders(filtered);
    }

    private String mapStatusCode(String value) {
        if (value == null || value.isBlank() || ALL_STATUSES.equals(value)) {
            return "";
        }
        if (WarehouseInboundStatus.label("PENDING").equals(value)) {
            return "PENDING";
        }
        if (WarehouseInboundStatus.label("PROCESSING").equals(value)) {
            return "PROCESSING";
        }
        if (WarehouseInboundStatus.label("IMPORTED").equals(value)) {
            return "IMPORTED";
        }
        if (WarehouseInboundStatus.label("MISMATCH").equals(value)) {
            return "MISMATCH";
        }
        return "";
    }

    private boolean matchesSelectedStatus(InboundOrderResponse order, String selectedStatus) {
        if (selectedStatus.isBlank()) {
            return true;
        }
        String statusCode = order.getStatusCode();
        if ("PENDING".equals(selectedStatus)) {
            return "PENDING".equals(statusCode) || "ACCEPTED".equals(statusCode);
        }
        return selectedStatus.equals(statusCode);
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return Normalizer.normalize(value.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT);
    }

    public String getKeyword() {
        return keywordField.getText();
    }

    public void setKeyword(String keyword) {
        keywordField.setText(keyword);
    }

    public String getSelectedStatus() {
        return statusFilter.getValue();
    }

    public void setSelectedStatus(String selectedStatus) {
        statusFilter.getSelectionModel().select(selectedStatus);
    }

    public void setSelectedStatusCode(String statusCode) {
        if (statusCode == null || statusCode.isBlank()) {
            statusFilter.getSelectionModel().select(ALL_STATUSES);
        } else {
            statusFilter.getSelectionModel().select(WarehouseInboundStatus.label(statusCode));
        }
        applyFilters();
    }

    public ObservableList<InboundOrderResponse> getInboundOrders() {
        return inboundOrders;
    }

    public void setInboundOrders(ObservableList<InboundOrderResponse> inboundOrders) {
        this.inboundOrders = inboundOrders;
        inboundOrderTable.setItems(inboundOrders);
        resultCountLabel.setText("Hiển thị " + inboundOrders.size() + " đơn hàng");
    }

    private class OrderLinkCell extends TableCell<InboundOrderResponse, String> {
        private final Button linkButton = new Button();

        private OrderLinkCell() {
            linkButton.getStyleClass().add("table-link");
            linkButton.setOnAction(event -> openOrderAt(getIndex(), linkButton));
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
                return;
            }
            linkButton.setText(item);
            setGraphic(linkButton);
            setText(null);
        }
    }

    private class ActionCell extends TableCell<InboundOrderResponse, String> {
        private final Button actionButton = new Button("Xử lý →");

        private ActionCell() {
            actionButton.getStyleClass().add("table-action");
            actionButton.setOnAction(event -> openOrderAt(getIndex(), actionButton));
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setGraphic(empty ? null : actionButton);
            setText(null);
        }
    }

    private void openOrderAt(int index, Button source) {
        if (index < 0 || index >= inboundOrderTable.getItems().size()) {
            return;
        }
        InboundOrderResponse order = inboundOrderTable.getItems().get(index);
        WarehouseNavigation.showInboundOrderProcess(source, order.getOrderId());
    }

}

package com.app.Ioms.ui.orders;
package com.app.Ioms.ui.orders;

import com.app.Ioms.domain.Order;
import com.app.Ioms.domain.OrderItem;
import com.app.Ioms.domain.Site;
import com.app.Ioms.navigation.Router;
import com.app.Ioms.service.AllocationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AllocationView extends BorderPane {

    @FXML
    private Label lblOrderId;

    @FXML
    private TableView<OrderItem> tblItems;

    @FXML
    private TableColumn<OrderItem, String> colSku;

    @FXML
    private TableColumn<OrderItem, String> colName;

    @FXML
    private TableColumn<OrderItem, Integer> colQty;

    @FXML
    private TableColumn<OrderItem, Site> colSite;

    @FXML
    private Button btnNext;

    private final AllocationService allocationService = new AllocationService();
    private final ObservableList<OrderItem> data = FXCollections.observableArrayList();
    private final Map<String, String> chosen = new HashMap<>();
    private final Order order;

    public AllocationView(Order order) {
        this.order = order;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("allocation.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Cannot load allocation.fxml", e);
        }
        getStylesheets().add(Objects.requireNonNull(getClass().getResource("allocation.css")).toExternalForm());
    }

    @FXML
    private void initialize() {
        if (order == null) {
            lblOrderId.setText("Chọn đơn từ 'Đơn hủy' để bắt đầu phân bổ");
            btnNext.setDisable(true);
            return;
        }
        lblOrderId.setText(order.getId());
        colSku.setCellValueFactory(new PropertyValueFactory<>("sku"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        colSite.setCellFactory(col -> new TableCell<>() {
            private final ComboBox<Site> chooser = new ComboBox<>();
            {
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                chooser.setMaxWidth(Double.MAX_VALUE);
                chooser.valueProperty().addListener((obs, o, n) -> {
                    OrderItem item = getTableView().getItems().get(getIndex());
                    if (n != null && item != null) {
                        chosen.put(item.getSku(), n.getCode());
                    }
                });
            }
            @Override
            protected void updateItem(Site item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    OrderItem rowItem = getTableView().getItems().get(getIndex());
                    List<Site> suggested = allocationService.suggestedSitesForItem(rowItem);
                    chooser.getItems().setAll(suggested);
                    if (!suggested.isEmpty()) {
                        chooser.getSelectionModel().select(0);
                        chosen.put(rowItem.getSku(), chooser.getValue().getCode());
                    }
                    setGraphic(chooser);
                }
            }
        });

        data.setAll(order.getItems());
        tblItems.setItems(data);

        btnNext.setOnAction(e -> {
            if (!allocationService.canAllocate(order, chosen)) {
                new Alert(Alert.AlertType.WARNING, "Phân bổ không hợp lệ hoặc thiếu tồn kho.").showAndWait();
                return;
            }
            AllocationResult result = new AllocationResult(order, Map.copyOf(chosen));
            Router.goToConfirm(result);
        });
    }
}
import com.app.Ioms.domain.Order;
import com.app.Ioms.domain.OrderItem;
import com.app.Ioms.domain.Site;
import com.app.Ioms.navigation.Router;
import com.app.Ioms.service.AllocationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.*;

public class AllocationView extends BorderPane {

    @FXML
    private Label lblOrderId;

    @FXML
    private TableView<OrderItem> tblItems;

    @FXML
    private TableColumn<OrderItem, String> colSku;

    @FXML
    private TableColumn<OrderItem, String> colName;

    @FXML
    private TableColumn<OrderItem, Integer> colQty;

    @FXML
    private TableColumn<OrderItem, Site> colSite;

    @FXML
    private Button btnNext;

    private final AllocationService allocationService = new AllocationService();
    private final ObservableList<OrderItem> data = FXCollections.observableArrayList();
    private final Map<String, String> chosen = new HashMap<>(); // SKU -> siteCode
    private final Order order;

    public AllocationView(Order order) {
        this.order = order;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("allocation.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Cannot load allocation.fxml", e);
        }
        getStylesheets().add(Objects.requireNonNull(getClass().getResource("allocation.css")).toExternalForm());
    }

    @FXML
    private void initialize() {
        if (order == null) {
            lblOrderId.setText("Chọn từ 'Đơn hủy' để bắt đầu phân bổ");
            btnNext.setDisable(true);
            return;
        }
        lblOrderId.setText(order.getId());
        colSku.setCellValueFactory(new PropertyValueFactory<>("sku"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        colSite.setCellFactory(col -> new TableCell<>() {
            private final ComboBox<Site> chooser = new ComboBox<>();

            {
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                chooser.setMaxWidth(Double.MAX_VALUE);
                chooser.valueProperty().addListener((obs, o, n) -> {
                    OrderItem item = getTableView().getItems().get(getIndex());
                    if (n != null && item != null) {
                        chosen.put(item.getSku(), n.getCode());
                    }
                });
            }

            @Override
            protected void updateItem(Site item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    OrderItem rowItem = getTableView().getItems().get(getIndex());
                    List<Site> suggested = allocationService.suggestedSitesForItem(rowItem);
                    chooser.getItems().setAll(suggested);
                    // auto-select highest priority
                    if (!suggested.isEmpty()) {
                        chooser.getSelectionModel().select(0);
                        chosen.put(rowItem.getSku(), chooser.getValue().getCode());
                    }
                    setGraphic(chooser);
                }
            }
        });

        data.setAll(order.getItems());
        tblItems.setItems(data);

        btnNext.setOnAction(e -> {
            if (!allocationService.canAllocate(order, chosen)) {
                new Alert(Alert.AlertType.WARNING, "Phân bổ không hợp lệ hoặc thiếu tồn kho.").showAndWait();
                return;
            }
            AllocationResult result = new AllocationResult(order, new HashMap<>(chosen));
            Router.goToConfirm(result);
        });
    }
}

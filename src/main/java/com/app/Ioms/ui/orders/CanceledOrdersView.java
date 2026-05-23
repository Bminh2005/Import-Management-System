package com.app.Ioms.ui.orders;

import com.app.Ioms.domain.Order;
import com.app.Ioms.domain.Site;
import com.app.Ioms.navigation.Router;
import com.app.Ioms.service.AllocationService;
import com.app.Ioms.service.OrderService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class CanceledOrdersView extends BorderPane {

    @FXML
    private ComboBox<Site> cboSite;

    @FXML
    private TableView<Order> tblCanceled;

    @FXML
    private TableColumn<Order, String> colId;

    @FXML
    private TableColumn<Order, String> colCustomer;
package com.app.Ioms.ui.orders;

import com.app.Ioms.domain.Order;
import com.app.Ioms.domain.Site;
import com.app.Ioms.navigation.Router;
import com.app.Ioms.service.AllocationService;
import com.app.Ioms.service.OrderService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class CanceledOrdersView extends BorderPane {

    @FXML
    private ComboBox<Site> cboSite;

    @FXML
    private TableView<Order> tblCanceled;

    @FXML
    private TableColumn<Order, String> colId;

    @FXML
    private TableColumn<Order, String> colCustomer;

    @FXML
    private TableColumn<Order, String> colSite;

    @FXML
    private Button btnReprocess;

    private final OrderService orderService = OrderService.getInstance();
    private final AllocationService allocationService = new AllocationService();
    private final ObservableList<Order> data = FXCollections.observableArrayList();

    public CanceledOrdersView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("canceled-orders.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Cannot load canceled-orders.fxml", e);
        }
        getStylesheets().add(Objects.requireNonNull(getClass().getResource("canceled-orders.css")).toExternalForm());
    }

    @FXML
    private void initialize() {
        cboSite.getItems().setAll(allocationService.listSitesByPriority());
        cboSite.valueProperty().addListener((obs, o, n) -> refresh());
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colSite.setCellValueFactory(new PropertyValueFactory<>("canceledBySite"));
        tblCanceled.setItems(data);
        refresh();
        btnReprocess.setOnAction(e -> {
            Order sel = tblCanceled.getSelectionModel().getSelectedItem();
            if (sel != null) {
                Router.goToAllocation(sel);
            }
        });
    }

    private void refresh() {
        Site s = cboSite.getValue();
        String code = s == null ? null : s.getCode();
        List<Order> list = orderService.listCanceledBySite(code);
        data.setAll(list);
    }
}
    @FXML
    private TableColumn<Order, String> colSite;

    @FXML
    private Button btnReprocess;

    private final OrderService orderService = OrderService.getInstance();
    private final AllocationService allocationService = new AllocationService();
    private final ObservableList<Order> data = FXCollections.observableArrayList();

    public CanceledOrdersView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("canceled-orders.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Cannot load canceled-orders.fxml", e);
        }
        getStylesheets().add(Objects.requireNonNull(getClass().getResource("canceled-orders.css")).toExternalForm());
    }

    @FXML
    private void initialize() {
        cboSite.getItems().setAll(allocationService.listSitesByPriority());
        cboSite.valueProperty().addListener((obs, o, n) -> refresh());
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colSite.setCellValueFactory(new PropertyValueFactory<>("canceledBySite"));
        tblCanceled.setItems(data);
        refresh();
        btnReprocess.setOnAction(e -> {
            Order sel = tblCanceled.getSelectionModel().getSelectedItem();
            if (sel != null) {
                Router.goToAllocation(sel);
            }
        });
    }

    private void refresh() {
        Site s = cboSite.getValue();
        String code = s == null ? null : s.getCode();
        List<Order> list = orderService.listCanceledBySite(code);
        data.setAll(list);
    }
}

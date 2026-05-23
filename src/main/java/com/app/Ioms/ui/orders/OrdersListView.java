package com.app.Ioms.ui.orders;

import com.app.Ioms.domain.Order;
import com.app.Ioms.domain.OrderStatus;
import com.app.Ioms.navigation.Router;
import com.app.Ioms.service.OrderService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Objects;

public class OrdersListView extends BorderPane {

    @FXML
    private TableView<Order> tblOrders;

    @FXML
    private TableColumn<Order, String> colId;

    @FXML
    private TableColumn<Order, String> colCustomer;

    @FXML
    private TableColumn<Order, OrderStatus> colStatus;
package com.app.Ioms.ui.orders;

import com.app.Ioms.domain.Order;
import com.app.Ioms.domain.OrderStatus;
import com.app.Ioms.navigation.Router;
import com.app.Ioms.service.OrderService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Objects;

public class OrdersListView extends BorderPane {

    @FXML
    private TableView<Order> tblOrders;

    @FXML
    private TableColumn<Order, String> colId;

    @FXML
    private TableColumn<Order, String> colCustomer;

    @FXML
    private TableColumn<Order, OrderStatus> colStatus;

    @FXML
    private Button btnOpen;

    @FXML
    private Button btnCanceled;

    private final OrderService orderService = OrderService.getInstance();
    private final ObservableList<Order> data = FXCollections.observableArrayList();

    public OrdersListView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("orders-list.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Cannot load orders-list.fxml", e);
        }
        getStylesheets().add(Objects.requireNonNull(getClass().getResource("orders-list.css")).toExternalForm());
    }

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tblOrders.setItems(data);
        refresh();
        btnOpen.setOnAction(e -> {
            Order sel = tblOrders.getSelectionModel().getSelectedItem();
            if (sel != null) {
                Router.goToOrderDetail(sel);
            }
        });
        btnCanceled.setOnAction(e -> Router.goToCanceledOrders());
    }

    @FXML
    private void refresh() {
        data.setAll(orderService.listAll());
    }
}
    @FXML
    private Button btnOpen;

    @FXML
    private Button btnCanceled;

    private final OrderService orderService = OrderService.getInstance();
    private final ObservableList<Order> data = FXCollections.observableArrayList();

    public OrdersListView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("orders-list.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Cannot load orders-list.fxml", e);
        }
        getStylesheets().add(Objects.requireNonNull(getClass().getResource("orders-list.css")).toExternalForm());
    }

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tblOrders.setItems(data);
        refresh();
        btnOpen.setOnAction(e -> {
            Order sel = tblOrders.getSelectionModel().getSelectedItem();
            if (sel != null) {
                Router.goToOrderDetail(sel);
            }
        });
        btnCanceled.setOnAction(e -> Router.goToCanceledOrders());
    }

    @FXML
    private void refresh() {
        data.setAll(orderService.listAll());
    }
}

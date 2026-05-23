package com.app.Ioms.ui.orders;

import com.app.Ioms.domain.Order;
import com.app.Ioms.domain.OrderItem;
import com.app.Ioms.domain.OrderStatus;
import com.app.Ioms.navigation.Router;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Objects;

public class OrderDetailView extends BorderPane {

    @FXML
    private Label lblOrderId;

    @FXML
    private Label lblCustomer;

    @FXML
    private Label lblStatus;

    @FXML
    private TableView<OrderItem> tblItems;
package com.app.Ioms.ui.orders;

import com.app.Ioms.domain.Order;
import com.app.Ioms.domain.OrderItem;
import com.app.Ioms.domain.OrderStatus;
import com.app.Ioms.navigation.Router;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Objects;

public class OrderDetailView extends BorderPane {

    @FXML
    private Label lblOrderId;

    @FXML
    private Label lblCustomer;

    @FXML
    private Label lblStatus;

    @FXML
    private TableView<OrderItem> tblItems;

    @FXML
    private TableColumn<OrderItem, String> colSku;

    @FXML
    private TableColumn<OrderItem, String> colName;

    @FXML
    private TableColumn<OrderItem, Integer> colQty;

    @FXML
    private Button btnReprocess;

    private final Order order;

    public OrderDetailView(Order order) {
        this.order = order;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("order-detail.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Cannot load order-detail.fxml", e);
        }
        getStylesheets().add(Objects.requireNonNull(getClass().getResource("order-detail.css")).toExternalForm());
    }

    @FXML
    private void initialize() {
        lblOrderId.setText(order.getId());
        lblCustomer.setText(order.getCustomerName());
        lblStatus.setText(order.getStatus().name());
        colSku.setCellValueFactory(new PropertyValueFactory<>("sku"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tblItems.setItems(FXCollections.observableArrayList(order.getItems()));
        btnReprocess.setDisable(order.getStatus() != OrderStatus.CANCELED);
        btnReprocess.setOnAction(e -> Router.goToAllocation(order));
    }
}
    @FXML
    private TableColumn<OrderItem, String> colSku;

    @FXML
    private TableColumn<OrderItem, String> colName;

    @FXML
    private TableColumn<OrderItem, Integer> colQty;

    @FXML
    private Button btnReprocess;

    private final Order order;

    public OrderDetailView(Order order) {
        this.order = order;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("order-detail.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Cannot load order-detail.fxml", e);
        }
        getStylesheets().add(Objects.requireNonNull(getClass().getResource("order-detail.css")).toExternalForm());
    }

    @FXML
    private void initialize() {
        lblOrderId.setText(order.getId());
        lblCustomer.setText(order.getCustomerName());
        lblStatus.setText(order.getStatus().name());
        colSku.setCellValueFactory(new PropertyValueFactory<>("sku"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tblItems.setItems(FXCollections.observableArrayList(order.getItems()));
        btnReprocess.setDisable(order.getStatus() != OrderStatus.CANCELED);
        btnReprocess.setOnAction(e -> Router.goToAllocation(order));
    }
}

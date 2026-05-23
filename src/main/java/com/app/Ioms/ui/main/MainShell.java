package com.app.Ioms.ui.main;

import com.app.Ioms.navigation.Router;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.Objects;

public class MainShell extends BorderPane {

    @FXML
    private StackPane contentOutlet;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnCanceled;

    @FXML
    private Button btnAllocation;

    @FXML
    private Button btnConfirm;

    public MainShell() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main-shell.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Cannot load main-shell.fxml", e);
        }
        // attach stylesheet colocated with FXML
        String css = Objects.requireNonNull(getClass().getResource("main-shell.css")).toExternalForm();
        getStylesheets().add(css);
    }

    @FXML
    private void initialize() {
        Router.init(contentOutlet);
        Router.goToOrdersList();
        if (btnOrders != null) btnOrders.getStyleClass().add("primary");
    }

    @FXML
    private void onOrders() {
        Router.goToOrdersList();
    }

    @FXML
    private void onCanceled() {
        Router.goToCanceledOrders();
    }

    @FXML
    private void onAllocation() {
        Router.goToAllocationStart();
    }
package com.app.Ioms.ui.main;

import com.app.Ioms.navigation.Router;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Objects;

public class MainShell extends BorderPane {

    @FXML
    private StackPane contentOutlet;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnCanceled;

    @FXML
    private Button btnAllocation;

    @FXML
    private Button btnConfirm;

    public MainShell() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main-shell.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Cannot load main-shell.fxml", e);
        }
        getStylesheets().add(Objects.requireNonNull(getClass().getResource("main-shell.css")).toExternalForm());
    }

    @FXML
    private void initialize() {
        Router.init(contentOutlet);
        Router.goToOrdersList();
    }

    @FXML
    private void onOrders() {
        Router.goToOrdersList();
    }

    @FXML
    private void onCanceled() {
        Router.goToCanceledOrders();
    }

    @FXML
    private void onAllocation() {
        Router.goToAllocationStart();
    }

    @FXML
    private void onConfirm() {
        Router.goToConfirmPending();
    }
}
    @FXML
    private void onConfirm() {
        Router.goToConfirmPending();
    }
}

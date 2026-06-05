package com.app.modules.sales.dashboard.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class SalesDashboardUI extends ScrollPane {
    @FXML
    private HBox addNewProduct;

    @FXML
    private HBox createNewRequest;

    @FXML
    private TableView<?> requestTable;

    @FXML
    private HBox viewAllRequests;
    @FXML
    private Label viewAllLabel;

    public SalesDashboardUI() {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/app/modules/sales/dashboard/ui/SalesDashboard.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        addNewProduct.setOnMouseClicked(mouseEvent -> {
            System.out.println("Add New Product Clicked");
        });
        createNewRequest.setOnMouseClicked(mouseEvent -> {
            System.out.println("Create New Request Clicked");
        });
        viewAllRequests.setOnMouseClicked(mouseEvent -> {
            System.out.println("View All Requests Clicked");
        });
        viewAllLabel.setOnMouseClicked(e -> {
            System.out.println("View All Requests Clicked");
        });

    }

    public void setActionOnCreateNewRequest(Runnable r) {
        createNewRequest.setOnMouseClicked(e -> {
            r.run();
        });
    }

    public void setActionOnAddNewProduct(Runnable r) {
        addNewProduct.setOnMouseClicked(e -> {
            r.run();
        });
    }

    public void setActionOnViewAllRequests(Runnable r) {
        viewAllRequests.setOnMouseClicked(e -> r.run());
        viewAllLabel.setOnMouseClicked(e -> r.run());
    }
}

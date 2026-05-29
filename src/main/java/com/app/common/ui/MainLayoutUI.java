package com.app.common.ui;

import com.app.modules.sales.dashboard.ui.SalesSidebar;
import com.app.modules.sales.dashboard.ui.SalesDashboardUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainLayoutUI extends BorderPane {
    @FXML
    private VBox mainContent;
    @FXML
    private HBox left;

    private Node page;

    public MainLayoutUI() {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/app/common/ui/MainLayout.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setLeft(new SalesSidebar());
        setPage(new SalesDashboardUI());
    }

    public void setPage(Node page) {
        if (this.page != null) {
            mainContent.getChildren().remove(this.page);
        }
        this.page = page;
        mainContent.getChildren().add(page);
    }
}

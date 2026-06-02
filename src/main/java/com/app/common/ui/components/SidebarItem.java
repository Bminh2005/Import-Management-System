package com.app.common.ui.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class SidebarItem extends HBox {
    @FXML
    private Label iconLabel;
    @FXML
    private Label textLabel;
    @FXML
    private Label shortCutLabel;

    private Runnable onAction;

    public SidebarItem() {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("com/app/common/ui/components/SidebarItem.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setOnMouseEntered(e -> {
            getStyleClass().add("hover");
        });

        setOnMouseExited(e -> {
            getStyleClass().removeAll("hover");
        });
        setOnMouseClicked(e -> {
            onAction.run();
        });
    }

    public SidebarItem(String icon, String text, String shortCut) {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/app/common/ui/components/SidebarItem.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setOnMouseEntered(e -> {
            getStyleClass().add("hover");
        });

        setOnMouseExited(e -> {
            getStyleClass().removeAll("hover");
        });
        setOnMouseClicked(e -> {
            onAction.run();
        });
        iconLabel.setText(icon);
        textLabel.setText(text);
        shortCutLabel.setText(shortCut);
    }

    public String getTextLabel() {
        return textLabel.getText();
    }

    public void setIconLabel(Label iconLabel) {
        this.iconLabel = iconLabel;
    }

    public void setTextLabel(String s) {
        textLabel.setText(s);
    }

    public void setShortCutLabel(String s) {
        shortCutLabel.setText(s);
    }

    public void setOnAction(Runnable r) {
        onAction = r;
    }

    public void activeItem() {
        if (!getStyleClass().contains("active")) {
            getStyleClass().add("active");
        }
    }

    public void unactiveItem() {
        if (getStyleClass().contains("active")) {
            getStyleClass().remove("active");
        }
    }

    public void collapse() {

        textLabel.setVisible(false);
        textLabel.setManaged(false);

        shortCutLabel.setVisible(false);
        shortCutLabel.setManaged(false);

        setAlignment(Pos.CENTER);
    }

    public void expand() {

        textLabel.setVisible(true);
        textLabel.setManaged(true);

        shortCutLabel.setVisible(true);
        shortCutLabel.setManaged(true);

        setAlignment(Pos.CENTER_LEFT);
    }
}

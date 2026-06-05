package com.app.common.ui.components;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Sidebar extends VBox {
    private static final double EXPANDED_WIDTH = 250;
    private static final double COLLAPSED_WIDTH = 80;
    @FXML
    private Label systemName;
    @FXML
    private Label departmentName;
    @FXML
    private VBox menuItems;
    @FXML
    private Label currentRole;
    @FXML
    private Button logoutButton;
    @FXML
    private HBox toggleButton;
    @FXML
    private Label toggleLabel;
    private SidebarItem selected;
    private final List<SidebarItem> items = new ArrayList<>();
    private boolean collapsed = false;

    public Sidebar() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/app/common/ui/components/Sidebar.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        toggleButton.setOnMouseClicked(e -> handleToggleButton());
    }

    public void addMenuItem(SidebarItem item) {
        addMenuItem(item, null);
    }

    /** Thêm menu; {@code onNavigate} chạy sau khi đổi trạng thái selected. */
    public void addMenuItem(SidebarItem item, Runnable onNavigate) {
        items.add(item);
        menuItems.getChildren().add(item);

        if (selected == null) {
            setSelectedItem(item);
        }

        item.setOnAction(() -> {
            setSelected(item);
            if (onNavigate != null) {
                onNavigate.run();
            } else {
                System.out.println("You navigated to " + item.getTextLabel());
            }
        });
    }

    /**
     * Gắn lại hành động cho item đã thêm (dùng sau khi có MainLayout / navigator).
     */
    public void bindItemAction(SidebarItem item, Runnable onNavigate) {
        item.setOnAction(() -> {
            setSelected(item);
            if (onNavigate != null) {
                onNavigate.run();
            }
        });
    }

    public void setActionLogoutButton(Runnable onAction) {
        this.logoutButton.setOnAction(e -> {
            onAction.run();
            System.out.println("Logout Button Clicked!");
        });
    }

    public void handleToggleButton() {

        collapsed = !collapsed;

        double targetWidth = collapsed ? COLLAPSED_WIDTH : EXPANDED_WIDTH;

        Timeline timeline = new Timeline();

        KeyValue kv = new KeyValue(
                prefWidthProperty(),
                targetWidth);

        KeyFrame kf = new KeyFrame(
                Duration.millis(150),
                kv);

        timeline.getKeyFrames().add(kf);

        timeline.play();

        // Update UI
        if (collapsed) {
            systemName.setVisible(false);
            systemName.setManaged(false);

            departmentName.setVisible(false);
            departmentName.setManaged(false);

            currentRole.setVisible(false);
            currentRole.setManaged(false);

            logoutButton.setText("⏻");

            toggleLabel.setText(">");

            for (SidebarItem item : items) {
                item.collapse();
            }

        } else {
            systemName.setVisible(true);
            systemName.setManaged(true);

            departmentName.setVisible(true);
            departmentName.setManaged(true);

            currentRole.setVisible(true);
            currentRole.setManaged(true);

            logoutButton.setText("Đăng xuất");

            toggleLabel.setText("< Thu Gọn");

            for (SidebarItem item : items) {
                item.expand();
            }
        }
    }

    public void selectMenu(SidebarItem item) {
        setSelected(item);
    }

    protected void setSelectedItem(SidebarItem item) {

        // Bỏ chọn item cũ
        if (selected != null) {
            selected.unactiveItem();
        }

        // Chọn item mới
        selected = item;
        selected.activeItem();
    }

    public void showMenu() {
        menuItems.setVisible(true);
    }

    public void hideMenu() {
        menuItems.setVisible(false);
    }

    public void setDepartmentName(Label departmentName) {
        this.departmentName = departmentName;
    }

    public void setCurrentRole(Label currentRole) {
        this.currentRole = currentRole;
    }

    public Label getDepartmentName() {
        return departmentName;
    }

    public Label getCurrentRole() {
        return currentRole;
    }

    public Button getLogoutButton() {
        return logoutButton;
    }
}

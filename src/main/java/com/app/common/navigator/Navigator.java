package com.app.common.navigator;

import com.app.common.ui.IScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Stack;

public class Navigator {
    private static Navigator instance;
    private Scene mainScene; // Scene chính của ứng dụng
    private final Stack<IScreen> history = new Stack<>();
    private IScreen currentView;
//Singleton new Navigator(); Navigator.getInstance()
    private Navigator() {}

    public static Navigator getInstance() {
        if (instance == null) {
            instance = new Navigator();
        }
        return instance;
    }

    // Khởi tạo Scene ban đầu từ class Main (Stage)
    public void setMainScene(Scene scene) {
        this.mainScene = scene;
    }

    // Chuyển trang
    public void navigateTo(IScreen view) {
        if (mainScene == null) {
            System.err.println("Chưa khởi tạo Main Scene cho Navigator!");
            return;
        }
        if (currentView != null) {
            history.push(currentView); // Lưu vào lịch sử
        }
        currentView = view;
        mainScene.setRoot(view.getRoot()); // Tráo đổi giao diện cực mượt
    }

    // Quay lại trang trước
    public void goBack() {
        if (!history.isEmpty()) {
            currentView = history.pop();
            mainScene.setRoot(currentView.getRoot());
        } else {
            System.out.println("Không có trang trước đó để quay lại.");
        }
    }
}

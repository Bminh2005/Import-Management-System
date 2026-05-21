package com.app.Ioms;

import com.app.Ioms.ui.MainUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

  @Override
  public void start(Stage stage) {
    MainUI root = new MainUI();

    Scene scene = new Scene(root, 1280, 800);
    scene.getStylesheets().add(getClass().getResource("/com/app/common/ui/theme.css").toExternalForm());
    scene.getStylesheets().add(getClass().getResource("/com/app/common/ui/components.css").toExternalForm());

    stage.setTitle("Hệ thống Quản lý Đặt hàng Nhập khẩu");
    stage.setScene(scene);
    stage.setMinWidth(1100);
    stage.setMinHeight(700);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

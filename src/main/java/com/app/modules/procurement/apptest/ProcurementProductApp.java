package com.app.modules.procurement.apptest;

import com.app.modules.procurement.product.ui.MerchandiseListUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Chạy thử riêng màn Quản lý Mặt hàng (không sidebar). */
public class ProcurementProductApp extends Application {

  @Override
  public void start(Stage stage) {
    MerchandiseListUI root = new MerchandiseListUI();

    Scene scene = new Scene(root, 1280, 800);
    scene.getStylesheets().add(getClass().getResource("/com/app/common/ui/theme.css").toExternalForm());
    scene.getStylesheets().add(getClass().getResource("/com/app/common/ui/components.css").toExternalForm());

    stage.setTitle("Preview — Quản lý Mặt hàng");
    stage.setScene(scene);
    stage.setMinWidth(1100);
    stage.setMinHeight(700);
    stage.show();
  }
}

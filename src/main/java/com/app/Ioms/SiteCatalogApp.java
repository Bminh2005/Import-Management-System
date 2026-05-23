package com.app.Ioms;

import com.app.modules.site.catalog.ui.SiteDetailUI;
import com.app.modules.site.catalog.ui.SitesListUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Chạy thử module site/catalog (không sidebar). */
public class SiteCatalogApp extends Application {

  private SitesListUI sitesListView;

  @Override
  public void start(Stage stage) {
    sitesListView = new SitesListUI();
    sitesListView.setOnViewDetail(
        siteId -> {
          SiteDetailUI detail = new SiteDetailUI();
          detail.loadSite(siteId);
          detail.setOnBack(() -> stage.getScene().setRoot(sitesListView));
          stage.getScene().setRoot(detail);
        });

    Scene scene = new Scene(sitesListView, 1280, 800);
    scene.getStylesheets().add(getClass().getResource("/com/app/common/ui/theme.css").toExternalForm());
    scene.getStylesheets().add(getClass().getResource("/com/app/common/ui/components.css").toExternalForm());

    stage.setTitle("Preview — Danh sách Site");
    stage.setScene(scene);
    stage.setMinWidth(1100);
    stage.setMinHeight(700);
    stage.show();
  }
}

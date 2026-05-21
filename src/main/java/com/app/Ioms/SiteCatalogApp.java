package com.app.Ioms;

import com.app.common.ui.navigation.NavigationHandler;
import com.app.modules.site.catalog.ui.SiteDetailUI;
import com.app.modules.site.catalog.ui.SitesListUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Chạy thử riêng module site/catalog: Danh sách Site → Chi tiết Site. */
public class SiteCatalogApp extends Application {

  private SitesListUI sitesListView;

  private final NavigationHandler navigation =
      new NavigationHandler() {
        @Override
        public void showMerchandiseList() {}

        @Override
        public void showSitesList() {
          Stage stage = (Stage) sitesListView.getScene().getWindow();
          stage.getScene().setRoot(sitesListView);
        }

        @Override
        public void showSiteDetail(String siteId) {
          SiteDetailUI detail = new SiteDetailUI();
          detail.setNavigationHandler(navigation);
          detail.loadSite(siteId);
          Stage stage = (Stage) sitesListView.getScene().getWindow();
          stage.getScene().setRoot(detail);
        }
      };

  @Override
  public void start(Stage stage) {
    sitesListView = new SitesListUI();
    sitesListView.setNavigationHandler(navigation);

    Scene scene = new Scene(sitesListView, 1280, 800);
    scene.getStylesheets().add(getClass().getResource("/com/app/common/ui/theme.css").toExternalForm());
    scene.getStylesheets().add(getClass().getResource("/com/app/common/ui/components.css").toExternalForm());

    stage.setTitle("Hệ thống Quản lý Nhập khẩu - Danh sách Site");
    stage.setScene(scene);
    stage.setMinWidth(1100);
    stage.setMinHeight(700);
    stage.show();
  }
}

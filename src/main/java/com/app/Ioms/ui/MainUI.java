package com.app.Ioms.ui;

import com.app.common.ui.navigation.NavigationHandler;
import com.app.common.util.ActionLog;
import com.app.common.util.FxmlUiHelper;
import com.app.modules.procurement.product.ui.MerchandiseListUI;
import com.app.modules.site.catalog.ui.SiteDetailUI;
import com.app.modules.site.catalog.ui.SitesListUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

/** Điều hướng giữa các màn hình chính. */
public class MainUI extends BorderPane implements NavigationHandler {

  @FXML private StackPane contentStack;
  @FXML private Button navMerchandiseBtn;
  @FXML private Button navSitesBtn;

  private MerchandiseListUI merchandiseView;
  private SitesListUI sitesListView;

  public MainUI() {
    FxmlUiHelper.loadSelf(this, "MainPage.fxml");
    FxmlUiHelper.addStylesheet(this, "main.css");
    merchandiseView = new MerchandiseListUI();
    sitesListView = new SitesListUI();
    sitesListView.setNavigationHandler(this);
    showMerchandiseList();
  }

  @FXML
  private void onNavMerchandise() {
    ActionLog.stub("Điều hướng: Quản lý Mặt hàng");
    showMerchandiseList();
  }

  @FXML
  private void onNavSites() {
    ActionLog.stub("Điều hướng: Danh sách Site");
    showSitesList();
  }

  @Override
  public void showMerchandiseList() {
    setActiveNav(navMerchandiseBtn);
    contentStack.getChildren().setAll(merchandiseView);
  }

  @Override
  public void showSitesList() {
    setActiveNav(navSitesBtn);
    contentStack.getChildren().setAll(sitesListView);
  }

  @Override
  public void showSiteDetail(String siteId) {
    ActionLog.stub("Điều hướng: Chi tiết Site — " + siteId);
    SiteDetailUI detail = new SiteDetailUI();
    detail.setNavigationHandler(this);
    detail.loadSite(siteId);
    contentStack.getChildren().setAll(detail);
  }

  private void setActiveNav(Button active) {
    navMerchandiseBtn.getStyleClass().removeAll("nav-btn-active");
    navMerchandiseBtn.getStyleClass().add("nav-btn");
    navSitesBtn.getStyleClass().removeAll("nav-btn-active");
    navSitesBtn.getStyleClass().add("nav-btn");
    active.getStyleClass().removeAll("nav-btn");
    active.getStyleClass().add("nav-btn-active");
  }
}

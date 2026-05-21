package com.app.modules.site.catalog.ui;

import com.app.common.ui.navigation.NavigationHandler;
import com.app.common.util.ActionLog;
import com.app.modules.site.catalog.dto.SiteCategorySectionDTO;
import com.app.modules.site.catalog.dto.SiteDetailPageDTO;
import com.app.modules.site.catalog.dto.SiteInfoRowDTO;
import com.app.modules.site.catalog.service.SiteService;
import com.app.modules.site.catalog.ui.components.SiteCategorySectionUI;
import com.app.modules.site.catalog.ui.components.SiteInfoRowUI;
import com.app.common.util.FxmlUiHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/** UI màn Chi tiết Site — chỉ bind UI và gọi Service. */
public class SiteDetailUI extends javafx.scene.control.ScrollPane {

  @FXML private Label titleLabel;
  @FXML private Label subtitleLabel;
  @FXML private Label statusBadge;
  @FXML private VBox siteInfoBox;
  @FXML private Label totalItemsLabel;
  @FXML private VBox itemsByCategoryBox;
  @FXML private Label shippingCostLabel;
  @FXML private Label deliveryLabel;
  @FXML private Label ratingLabel;
  @FXML private Label totalOrdersLabel;
  @FXML private Label availableItemsLabel;
  @FXML private Label inventoryValueLabel;
  @FXML private Button createOrderButton;
  @FXML private Button orderHistoryButton;

  private final SiteDetailPageDTO pageDto = new SiteDetailPageDTO();
  private final SiteService siteService = SiteService.getInstance();
  private NavigationHandler navigationHandler;

  public void setNavigationHandler(NavigationHandler navigationHandler) {
    this.navigationHandler = navigationHandler;
  }

  public SiteDetailPageDTO getPageDto() {
    return pageDto;
  }

  public void loadSite(String siteId) {
    SiteDetailPageDTO loaded = siteService.getSiteDetailPage(siteId);
    copyPageDto(loaded);
    applyPageDtoToView();
  }

  public SiteDetailUI() {
    FxmlUiHelper.loadSelf(this, "SiteDetailPage.fxml");
    FxmlUiHelper.addStylesheet(this, "site-detail.css");
    createOrderButton.setOnAction(e -> onCreateOrder());
    orderHistoryButton.setOnAction(e -> onOrderHistory());
  }

  private void copyPageDto(SiteDetailPageDTO source) {
    pageDto.setSiteId(source.getSiteId());
    pageDto.setPageTitle(source.getPageTitle());
    pageDto.setPageSubtitle(source.getPageSubtitle());
    pageDto.setStatusLabel(source.getStatusLabel());
    pageDto.setStatusStyleClass(source.getStatusStyleClass());
    pageDto.setShippingCostText(source.getShippingCostText());
    pageDto.setDeliveryText(source.getDeliveryText());
    pageDto.setRatingText(source.getRatingText());
    pageDto.setTotalOrdersText(source.getTotalOrdersText());
    pageDto.setAvailableItemsText(source.getAvailableItemsText());
    pageDto.setTotalItemsSummary(source.getTotalItemsSummary());
    pageDto.setInventoryValueText(source.getInventoryValueText());
    pageDto.getInfoRows().clear();
    pageDto.getInfoRows().addAll(source.getInfoRows());
    pageDto.getCategorySections().clear();
    pageDto.getCategorySections().addAll(source.getCategorySections());
  }

  private void applyPageDtoToView() {
    titleLabel.setText(pageDto.getPageTitle());
    subtitleLabel.setText(pageDto.getPageSubtitle());
    statusBadge.setText(pageDto.getStatusLabel());
    statusBadge.getStyleClass().removeAll("badge-active", "badge-inactive", "badge-status-large");
    statusBadge.getStyleClass().addAll(pageDto.getStatusStyleClass(), "badge-status-large");
    shippingCostLabel.setText(pageDto.getShippingCostText());
    deliveryLabel.setText(pageDto.getDeliveryText());
    ratingLabel.setText(pageDto.getRatingText());
    totalOrdersLabel.setText(pageDto.getTotalOrdersText());
    availableItemsLabel.setText(pageDto.getAvailableItemsText());
    inventoryValueLabel.setText(pageDto.getInventoryValueText());
    totalItemsLabel.setText(pageDto.getTotalItemsSummary());
    renderInfoRows();
    renderCategorySections();
  }

  private void renderInfoRows() {
    siteInfoBox.getChildren().clear();
    for (SiteInfoRowDTO rowDto : pageDto.getInfoRows()) {
      SiteInfoRowUI row = new SiteInfoRowUI();
      row.bind(rowDto);
      siteInfoBox.getChildren().add(row);
    }
  }

  private void renderCategorySections() {
    itemsByCategoryBox.getChildren().clear();
    for (SiteCategorySectionDTO sectionDto : pageDto.getCategorySections()) {
      SiteCategorySectionUI section = new SiteCategorySectionUI();
      section.bind(sectionDto);
      itemsByCategoryBox.getChildren().add(section);
    }
  }

  @FXML
  private void onBack() {
    ActionLog.stub("Chi tiết Site: Quay lại Danh sách Site");
    if (navigationHandler != null) {
      navigationHandler.showSitesList();
    }
  }

  @FXML
  private void onCreateOrder() {
    ActionLog.stub("Chi tiết Site: Tạo đơn hàng mới");
  }

  @FXML
  private void onOrderHistory() {
    ActionLog.stub("Chi tiết Site: Xem lịch sử đơn hàng");
  }
}

package com.app.modules.site.catalog.ui;

import com.app.common.util.FxmlUiHelper;
import com.app.modules.site.catalog.ui.components.SiteCategorySectionUI;
import com.app.modules.site.catalog.ui.components.SiteCategorySectionUI.ItemRow;
import com.app.modules.site.catalog.ui.components.SiteInfoRowUI;
import java.util.List;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/** UI màn Chi tiết Site (dữ liệu mẫu — chưa có Service). */
public class SiteDetailUI extends ScrollPane {

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

  private Runnable onBack;

  public SiteDetailUI() {
    FxmlUiHelper.loadSelf(this, "SiteDetailPage.fxml");
    FxmlUiHelper.addStylesheet(this, "site-detail.css");
    createOrderButton.setOnAction(e -> {});
    orderHistoryButton.setOnAction(e -> {});
    loadSite("1");
  }

  public void setOnBack(Runnable onBack) {
    this.onBack = onBack;
  }

  public void loadSite(String siteId) {
    titleLabel.setText("Chi tiết Site: Site A - Hà Nội");
    subtitleLabel.setText("Thông tin chi tiết và danh sách mặt hàng có sẵn");
    statusBadge.setText("Đang hoạt động");
    statusBadge.getStyleClass().removeAll("badge-active", "badge-inactive", "badge-status-large");
    statusBadge.getStyleClass().addAll("badge-active", "badge-status-large");
    shippingCostLabel.setText("500,000 VND");
    deliveryLabel.setText("2-3 ngày");
    ratingLabel.setText("4.5");
    totalOrdersLabel.setText("245");
    availableItemsLabel.setText("6");
    inventoryValueLabel.setText("425,000,000 VND");
    totalItemsLabel.setText("Tổng: 6 mặt hàng");
    renderInfoRows();
    renderCategorySections();
  }

  private void renderInfoRows() {
    siteInfoBox.getChildren().clear();
    addInfoRow("📍", "ĐỊA CHỈ", "Tầng 5, Tòa nhà ABC, Số 10 Lê Duẩn",
        "Hoàn Kiếm, Hà Nội, Việt Nam", false, true);
    addInfoRow("👤", "NGƯỜI LIÊN HỆ", "Nguyễn Văn An", null, false, false);
    addInfoRow("📞", "SỐ ĐIỆN THOẠI", "024-3942-1234", null, true, false);
    addInfoRow("✉", "EMAIL", "an.nguyen@siteA.com", null, true, false);
  }

  private void addInfoRow(
      String icon, String label, String primary, String secondary, boolean link, boolean sep) {
    SiteInfoRowUI row = new SiteInfoRowUI();
    row.bind(icon, label, primary, secondary, link, sep);
    siteInfoBox.getChildren().add(row);
  }

  private void renderCategorySections() {
    itemsByCategoryBox.getChildren().clear();
    SiteCategorySectionUI electronics = new SiteCategorySectionUI();
    electronics.bind(
        "Máy tính",
        "1 mặt hàng",
        List.of(
            new ItemRow("MH001", "Laptop Dell XPS 13", "10 cái", "25,000,000 VND", "2024-05-10")));
    itemsByCategoryBox.getChildren().add(electronics);

    SiteCategorySectionUI accessories = new SiteCategorySectionUI();
    accessories.bind(
        "Phụ kiện",
        "5 mặt hàng",
        List.of(
            new ItemRow("MH002", "Bàn phím cơ Keychron K2", "25 cái", "2,700,000 VND", "2024-05-10"),
            new ItemRow("MH003", "Chuột Logitech MX Master 3", "15 cái", "2,000,000 VND", "2024-05-09"),
            new ItemRow("MH005", "Webcam Logitech C920", "20 cái", "1,800,000 VND", "2024-05-10")));
    itemsByCategoryBox.getChildren().add(accessories);
  }

  @FXML
  private void onBack() {
    if (onBack != null) {
      onBack.run();
    }
  }

  @FXML
  private void onCreateOrder() {}

  @FXML
  private void onOrderHistory() {}
}

package com.app.modules.site.catalog.ui;

import com.app.common.util.FxmlUiHelper;
import com.app.modules.site.catalog.ui.components.SiteCategorySectionUI;
import com.app.modules.site.catalog.ui.components.SiteCategorySectionUI.ItemRow;
import com.app.modules.site.catalog.ui.components.SiteInfoRowUI;
import java.util.ArrayList;
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
    long id = Long.parseLong(siteId);
    String sql = "SELECT s.id, s.site_name, s.site_address, s.site_url, s.more_info, s.delivery_by_ship, s.delivery_by_air "
        + "FROM \"Site\" s WHERE s.id = ?";
    
    try (java.sql.Connection conn = com.app.database.manager.DatabaseManager.getConnection();
         java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, id);
      try (java.sql.ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          String name = rs.getString("site_name");
          String address = rs.getString("site_address");
          String url = rs.getString("site_url");
          String moreInfo = rs.getString("more_info");
          long ship = rs.getLong("delivery_by_ship");
          long air = rs.getLong("delivery_by_air");

          titleLabel.setText("Chi tiết Site: " + name);
          subtitleLabel.setText("Thông tin chi tiết và danh sách mặt hàng có sẵn");
          statusBadge.setText("Đang hoạt động");
          statusBadge.getStyleClass().removeAll("badge-active", "badge-inactive", "badge-status-large");
          statusBadge.getStyleClass().addAll("badge-active", "badge-status-large");
          
          shippingCostLabel.setText("500,000 VND"); // Phí vận chuyển mặc định
          
          String deliveryText = "Đường biển: " + ship + " ngày | Đường hàng không: " + air + " ngày";
          deliveryLabel.setText(deliveryText);
          
          ratingLabel.setText("5.0"); // Đánh giá mặc định
          
          // Lấy tổng số đơn hàng đã tạo cho site này
          int totalOrders = 0;
          try (java.sql.PreparedStatement oStmt = conn.prepareStatement("SELECT COUNT(*) FROM \"Order\" WHERE site_id = ?")) {
            oStmt.setLong(1, id);
            try (java.sql.ResultSet oRs = oStmt.executeQuery()) {
              if (oRs.next()) {
                totalOrders = oRs.getInt(1);
              }
            }
          }
          totalOrdersLabel.setText(String.valueOf(totalOrders));

          // Lấy số mặt hàng khả dụng và tổng giá trị tồn kho
          int availableItems = 0;
          double inventoryValue = 0;
          try (java.sql.PreparedStatement iStmt = conn.prepareStatement("SELECT COUNT(*), COALESCE(SUM(quantity * price), 0) FROM \"SiteInventory\" WHERE site_id = ?")) {
            iStmt.setLong(1, id);
            try (java.sql.ResultSet iRs = iStmt.executeQuery()) {
              if (iRs.next()) {
                availableItems = iRs.getInt(1);
                inventoryValue = iRs.getDouble(2);
              }
            }
          }
          availableItemsLabel.setText(String.valueOf(availableItems));
          inventoryValueLabel.setText(String.format("%,.0f VND", inventoryValue));
          totalItemsLabel.setText("Tổng: " + availableItems + " mặt hàng");

          // Hiển thị thông tin liên hệ
          siteInfoBox.getChildren().clear();
          addInfoRow("📍", "ĐỊA CHỈ", address != null ? address : "Chưa xác định", null, false, true);
          addInfoRow("🔗", "WEBSITE", url != null ? url : "Không có", null, url != null, false);
          addInfoRow("ℹ️", "THÔNG TIN KHÁC", moreInfo != null ? moreInfo : "Không có", null, false, false);
          
          // Hiển thị danh mục mặt hàng thực tế từ database
          renderDbCategorySections(id);
        }
      }
    } catch (java.sql.SQLException e) {
      e.printStackTrace();
    }
  }

  private void addInfoRow(
      String icon, String label, String primary, String secondary, boolean link, boolean sep) {
    SiteInfoRowUI row = new SiteInfoRowUI();
    row.bind(icon, label, primary, secondary, link, sep);
    siteInfoBox.getChildren().add(row);
  }

  private void renderDbCategorySections(long siteId) {
    itemsByCategoryBox.getChildren().clear();
    
    List<ItemRow> itemsList = new ArrayList<>();
    String sql = "SELECT si.merchandise_detail_id, si.quantity, si.price, md.unit, m.merchandise_name "
        + "FROM \"SiteInventory\" si "
        + "JOIN \"MerchandiseDetail\" md ON si.merchandise_detail_id = md.id "
        + "JOIN \"Merchandise\" m ON md.merchandise_id = m.id "
        + "WHERE si.site_id = ? AND si.quantity > 0";
        
    try (java.sql.Connection conn = com.app.database.manager.DatabaseManager.getConnection();
         java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, siteId);
      try (java.sql.ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          long mdId = rs.getLong("merchandise_detail_id");
          int qty = rs.getInt("quantity");
          double price = rs.getDouble("price");
          String unit = rs.getString("unit");
          String name = rs.getString("merchandise_name");
          
          String code = "MH" + String.format("%03d", mdId);
          String qtyText = qty + " " + (unit != null ? unit : "cái");
          String priceText = String.format("%,.0f VND", price);
          String dateText = java.time.LocalDate.now().toString();
          
          itemsList.add(new ItemRow(code, name, qtyText, priceText, dateText));
        }
      }
    } catch (java.sql.SQLException e) {
      e.printStackTrace();
    }
    
    if (!itemsList.isEmpty()) {
      SiteCategorySectionUI section = new SiteCategorySectionUI();
      section.bind(
          "Danh mục mặt hàng có sẵn",
          itemsList.size() + " mặt hàng",
          itemsList);
      itemsByCategoryBox.getChildren().add(section);
    }
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

package com.app.modules.site.catalog.ui;

import com.app.common.ui.components.FilterChipsUI;
import com.app.common.ui.components.FilterChipsUI.ChipOption;
import com.app.common.ui.components.PageHeaderUI;
import com.app.common.ui.components.SearchBarUI;
import com.app.common.util.FxmlUiHelper;
import com.app.modules.site.catalog.ui.components.SiteCardUI;
import com.app.database.manager.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/** UI màn Danh sách Site. */
public class SitesListUI extends ScrollPane {

  private record SiteCardData(
      String id,
      String code,
      String name,
      String warehouse,
      String location,
      String inventoryText,
      boolean active,
      String status,
      String statusKey) {}

  @FXML private StackPane headerPlaceholder;
  @FXML private StackPane searchPlaceholder;
  @FXML private StackPane statusChipsPlaceholder;
  @FXML private FlowPane sitesGrid;
  @FXML private VBox emptyState;
  @FXML private Label emptyLabel;

  private PageHeaderUI pageHeader;
  private SearchBarUI searchBar;
  private FilterChipsUI filterChips;

  private final List<SiteCardData> allSites = loadSitesFromDb();
  private String searchQuery = "";
  private String selectedStatus = "all";
  private Consumer<String> onViewDetail;

  public SitesListUI() {
    FxmlUiHelper.loadSelf(this, "SitesListPage.fxml");
    FxmlUiHelper.addStylesheet(this, "sites-list.css");
    loadComponents();
    bindHeaderAndSearch();
    setupStatusFilters();
    renderSites();
  }

  public void setOnViewDetail(Consumer<String> onViewDetail) {
    this.onViewDetail = onViewDetail;
  }

  private List<SiteCardData> loadSitesFromDb() {
    List<SiteCardData> list = new ArrayList<>();
    String sql = "SELECT s.id, s.site_name, s.site_address, s.site_url, s.more_info, "
        + "  (SELECT COALESCE(SUM(si.quantity), 0) FROM \"SiteInventory\" si WHERE si.site_id = s.id) as total_qty "
        + "FROM \"Site\" s "
        + "ORDER BY s.id ASC";
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
      while (rs.next()) {
        long id = rs.getLong("id");
        String name = rs.getString("site_name");
        String address = rs.getString("site_address");
        String url = rs.getString("site_url");
        String moreInfo = rs.getString("more_info");
        int totalQty = rs.getInt("total_qty");

        String code = "SITE" + String.format("%02d", id);
        String warehouseName = name + " Warehouse";
        String location = address != null ? address : "Chưa xác định";
        String inventoryText = String.format("%,d mặt hàng", totalQty);

        list.add(new SiteCardData(
            String.valueOf(id),
            code,
            name,
            warehouseName,
            location,
            inventoryText,
            true, // active
            "Hoạt động", // statusText
            "active" // statusKey
        ));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }

  private void loadComponents() {
    pageHeader = new PageHeaderUI();
    headerPlaceholder.getChildren().setAll(pageHeader);
    searchBar = new SearchBarUI();
    searchPlaceholder.getChildren().setAll(searchBar);
    filterChips = new FilterChipsUI();
    statusChipsPlaceholder.getChildren().setAll(filterChips);
  }

  private void bindHeaderAndSearch() {
    pageHeader.setTitle("Danh sách Site");
    pageHeader.setSubtitle("Quản lý các site và kho hàng trên toàn hệ thống");
    pageHeader.hideAction();
    searchBar.setPromptText("Tìm kiếm theo mã site, tên hoặc địa điểm...");
    searchBar.setOnSearchChanged(
        q -> {
          searchQuery = q == null ? "" : q;
          renderSites();
        });
    emptyLabel.setText("Không tìm thấy site nào phù hợp với tiêu chí tìm kiếm");
  }

  private void setupStatusFilters() {
    long allCount = allSites.size();
    long activeCount = allSites.stream().filter(SiteCardData::active).count();
    long inactiveCount = allCount - activeCount;

    filterChips.bindChips(
        List.of(
            new ChipOption("all", "Tất cả", String.valueOf(allCount), true),
            new ChipOption("active", "Hoạt động", String.valueOf(activeCount), false),
            new ChipOption("inactive", "Không hoạt động", String.valueOf(inactiveCount), false)));
    filterChips.setOnFilterSelected(
        value -> {
          selectedStatus = value;
          renderSites();
        });
  }

  private void renderSites() {
    sitesGrid.getChildren().clear();
    String q = searchQuery.toLowerCase();
    List<SiteCardData> visible = new ArrayList<>();
    for (SiteCardData site : allSites) {
      if (!"all".equals(selectedStatus) && !selectedStatus.equals(site.statusKey())) {
        continue;
      }
      if (!q.isBlank()
          && !site.code().toLowerCase().contains(q)
          && !site.name().toLowerCase().contains(q)
          && !site.location().toLowerCase().contains(q)) {
        continue;
      }
      visible.add(site);
    }

    for (SiteCardData site : visible) {
      SiteCardUI card = new SiteCardUI();
      card.bind(
          site.id(),
          site.code(),
          site.name(),
          site.warehouse(),
          site.location(),
          site.inventoryText(),
          site.active(),
          site.status());
      card.setOnViewDetail(onViewDetail);
      sitesGrid.getChildren().add(card);
    }

    boolean empty = visible.isEmpty();
    emptyState.setVisible(empty);
    emptyState.setManaged(empty);
  }
}

package com.app.modules.site.catalog.ui;

import com.app.common.ui.components.FilterChipsUI;
import com.app.common.ui.components.FilterChipsUI.ChipOption;
import com.app.common.ui.components.PageHeaderUI;
import com.app.common.ui.components.SearchBarUI;
import com.app.common.util.FxmlUiHelper;
import com.app.modules.site.catalog.ui.components.SiteCardUI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/** UI màn Danh sách Site (dữ liệu mẫu — chưa có Service). */
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

  private final List<SiteCardData> allSites = createMockSites();
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

  private static List<SiteCardData> createMockSites() {
    return List.of(
        new SiteCardData("1", "SITE01", "Kho Hong Kong", "Hong Kong Central Warehouse", "Hong Kong",
            "15,000 mặt hàng", true, "Hoạt động", "active"),
        new SiteCardData("2", "SITE02", "Kho Singapore", "Singapore Distribution Center", "Singapore",
            "12,000 mặt hàng", true, "Hoạt động", "active"),
        new SiteCardData("3", "SITE03", "Kho Bangkok", "Bangkok Logistics Hub", "Bangkok, Thailand",
            "8,500 mặt hàng", true, "Hoạt động", "active"),
        new SiteCardData("4", "SITE04", "Kho Shanghai", "Shanghai Regional Warehouse", "Shanghai, China",
            "20,000 mặt hàng", true, "Hoạt động", "active"),
        new SiteCardData("5", "SITE05", "Kho Kuala Lumpur", "KL Storage Facility", "Kuala Lumpur, Malaysia",
            "3,000 mặt hàng", false, "Không hoạt động", "inactive"));
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
    filterChips.bindChips(
        List.of(
            new ChipOption("all", "Tất cả", "5", true),
            new ChipOption("active", "Hoạt động", "4", false),
            new ChipOption("inactive", "Không hoạt động", "1", false)));
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

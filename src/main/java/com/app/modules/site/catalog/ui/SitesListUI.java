package com.app.modules.site.catalog.ui;

import com.app.common.dto.FilterChipDTO;
import com.app.common.ui.components.FilterChipsUI;
import com.app.common.ui.components.PageHeaderUI;
import com.app.common.ui.components.SearchBarUI;
import com.app.common.ui.navigation.NavigationHandler;
import com.app.common.util.ActionLog;
import com.app.modules.site.catalog.dto.SiteCardDTO;
import com.app.modules.site.catalog.dto.SitesListPageDTO;
import com.app.modules.site.catalog.service.SiteService;
import com.app.modules.site.catalog.ui.components.SiteCardUI;
import java.util.List;
import com.app.common.util.FxmlUiHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/** UI màn Danh sách Site — chỉ bind UI và gọi Service. */
public class SitesListUI extends javafx.scene.control.ScrollPane {

  @FXML private StackPane headerPlaceholder;
  @FXML private StackPane searchPlaceholder;
  @FXML private StackPane statusChipsPlaceholder;
  @FXML private FlowPane sitesGrid;
  @FXML private VBox emptyState;
  @FXML private Label emptyLabel;

  private PageHeaderUI pageHeaderController;
  private SearchBarUI searchBarController;
  private FilterChipsUI filterChipsController;

  private final SitesListPageDTO pageDto = new SitesListPageDTO();
  private final SiteService siteService = SiteService.getInstance();
  private NavigationHandler navigationHandler;
  private String selectedStatus = "all";

  public void setNavigationHandler(NavigationHandler navigationHandler) {
    this.navigationHandler = navigationHandler;
  }

  public SitesListPageDTO getPageDto() {
    return pageDto;
  }

  public SitesListUI() {
    FxmlUiHelper.loadSelf(this, "SitesListPage.fxml");
    FxmlUiHelper.addStylesheet(this, "sites-list.css");
    loadComponents();
    bindPageDtoToView();
    setupStatusFilters();
    renderSites();
  }

  private void loadComponents() {
    pageHeaderController = mountHeader();
    searchBarController = mountSearch();
    filterChipsController = mountStatusChips();
  }

  private PageHeaderUI mountHeader() {
    PageHeaderUI ui = new PageHeaderUI();
    headerPlaceholder.getChildren().setAll(ui);
    return ui;
  }

  private SearchBarUI mountSearch() {
    SearchBarUI ui = new SearchBarUI();
    searchPlaceholder.getChildren().setAll(ui);
    return ui;
  }

  private FilterChipsUI mountStatusChips() {
    FilterChipsUI ui = new FilterChipsUI();
    statusChipsPlaceholder.getChildren().setAll(ui);
    return ui;
  }

  private void bindPageDtoToView() {
    pageHeaderController.setTitle(pageDto.getPageTitle());
    pageHeaderController.setSubtitle(pageDto.getPageSubtitle());
    pageHeaderController.hideAction();
    searchBarController.setPromptText(pageDto.getSearchPrompt());
    searchBarController.setOnSearchChanged(
        q -> {
          pageDto.setSearchQuery(q);
          renderSites();
        });
    emptyLabel.setText("Không tìm thấy site nào phù hợp với tiêu chí tìm kiếm");
  }

  private void setupStatusFilters() {
    List<FilterChipDTO> chips =
        List.of(
            new FilterChipDTO("all", "Tất cả", "5", true),
            new FilterChipDTO("active", "Hoạt động", "4", false),
            new FilterChipDTO("inactive", "Không hoạt động", "1", false));
    filterChipsController.bindChips(chips);
    filterChipsController.setOnFilterSelected(
        value -> {
          selectedStatus = value;
          ActionLog.stub("Danh sách Site: Lọc trạng thái — " + value);
          renderSites();
        });
  }

  private void renderSites() {
    sitesGrid.getChildren().clear();
    List<SiteCardDTO> cards =
        siteService.searchSiteCards(pageDto.getSearchQuery(), selectedStatus);
    for (SiteCardDTO card : cards) {
      sitesGrid.getChildren().add(createSiteCard(card));
    }
    pageDto.setEmptyVisible(cards.isEmpty());
    emptyState.setVisible(pageDto.isEmptyVisible());
    emptyState.setManaged(pageDto.isEmptyVisible());
  }

  private SiteCardUI createSiteCard(SiteCardDTO card) {
    SiteCardUI cardUi = new SiteCardUI();
    cardUi.bind(card);
    cardUi.setOnViewDetail(
        siteId -> {
          if (navigationHandler != null) {
            navigationHandler.showSiteDetail(siteId);
          }
        });
    return cardUi;
  }
}

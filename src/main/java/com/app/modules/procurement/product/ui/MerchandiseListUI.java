package com.app.modules.procurement.product.ui;

import com.app.common.dto.FilterChipDTO;
import com.app.common.ui.components.FilterChipsUI;
import com.app.common.ui.components.PageHeaderUI;
import com.app.common.ui.components.PaginationBarUI;
import com.app.common.ui.components.SearchBarUI;
import com.app.common.util.ActionLog;
import com.app.modules.procurement.product.dto.AddMerchandiseDTO;
import com.app.modules.procurement.product.dto.MerchandiseListPageDTO;
import com.app.modules.procurement.product.dto.MerchandiseTableRowDTO;
import com.app.modules.procurement.product.entity.Merchandise;
import com.app.modules.procurement.product.service.MerchandiseService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.app.common.util.FxmlUiHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/** UI màn Quản lý Mặt hàng — chỉ bind UI và gọi Service. */
public class MerchandiseListUI extends StackPane {

  @FXML private StackPane headerPlaceholder;
  @FXML private StackPane searchPlaceholder;
  @FXML private StackPane categoryChipsPlaceholder;
  @FXML private TableView<MerchandiseTableRowDTO> merchandiseTable;
  @FXML private TableColumn<MerchandiseTableRowDTO, Void> expandColumn;
  @FXML private TableColumn<MerchandiseTableRowDTO, String> codeColumn;
  @FXML private TableColumn<MerchandiseTableRowDTO, String> nameColumn;
  @FXML private TableColumn<MerchandiseTableRowDTO, String> unitColumn;
  @FXML private TableColumn<MerchandiseTableRowDTO, String> categoryColumn;
  @FXML private TableColumn<MerchandiseTableRowDTO, String> statusColumn;
  @FXML private TableColumn<MerchandiseTableRowDTO, Void> actionsColumn;
  @FXML private StackPane paginationPlaceholder;
  @FXML private StackPane dialogOverlay;

  private PageHeaderUI pageHeaderController;
  private SearchBarUI searchBarController;
  private FilterChipsUI filterChipsController;
  private PaginationBarUI paginationController;

  private final MerchandiseListPageDTO pageDto = new MerchandiseListPageDTO();
  private final MerchandiseService merchandiseService = MerchandiseService.getInstance();
  private final ObservableList<MerchandiseTableRowDTO> tableRows = FXCollections.observableArrayList();
  private final Set<String> expandedIds = new HashSet<>();
  private String selectedCategory = "all";

  public MerchandiseListUI() {
    FxmlUiHelper.loadSelf(this, "MerchandiseListPage.fxml");
    FxmlUiHelper.addStylesheet(this, "merchandise-list.css");
    loadComponents();
    bindPageDtoToView();
    setupCategoryFilters();
    setupTable();
    rebuildTable();
  }

  private void loadComponents() {
    pageHeaderController = mountHeader();
    searchBarController = mountSearch();
    filterChipsController = mountCategoryChips();
    paginationController = mountPagination();
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

  private FilterChipsUI mountCategoryChips() {
    FilterChipsUI ui = new FilterChipsUI();
    categoryChipsPlaceholder.getChildren().setAll(ui);
    return ui;
  }

  private PaginationBarUI mountPagination() {
    PaginationBarUI ui = new PaginationBarUI();
    paginationPlaceholder.getChildren().setAll(ui);
    return ui;
  }

  private void bindPageDtoToView() {
    pageHeaderController.setTitle(pageDto.getPageTitle());
    pageHeaderController.setSubtitle(pageDto.getPageSubtitle());
    pageHeaderController.setActionButton(
        pageDto.getAddButtonText(),
        () -> {
          ActionLog.stub("Quản lý Mặt hàng: Mở popup Thêm Mặt hàng Mới");
          openAddDialog();
        });
    searchBarController.setPromptText(pageDto.getSearchPrompt());
    searchBarController.setOnSearchChanged(
        query -> {
          pageDto.setSearchQuery(query);
          rebuildTable();
        });
    paginationController.setSummaryText(pageDto.getPaginationText());
  }

  private void setupCategoryFilters() {
    List<FilterChipDTO> chips =
        List.of(
            new FilterChipDTO("all", "Tất cả", "24", true),
            new FilterChipDTO("electronics", "Điện tử", "12", false),
            new FilterChipDTO("accessories", "Phụ kiện", "8", false),
            new FilterChipDTO("furniture", "Nội thất", "4", false));
    filterChipsController.bindChips(chips);
    filterChipsController.setOnFilterSelected(
        value -> {
          selectedCategory = value;
          ActionLog.stub("Quản lý Mặt hàng: Lọc danh mục — " + value);
          rebuildTable();
        });
  }

  private void setupTable() {
    merchandiseTable.setItems(tableRows);
    merchandiseTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    expandColumn.setResizable(false);

    codeColumn.setCellValueFactory(
        d ->
            new javafx.beans.property.SimpleStringProperty(
                d.getValue().isDetail() ? "" : d.getValue().getMerchandise().getCode()));
    nameColumn.setCellValueFactory(
        d ->
            new javafx.beans.property.SimpleStringProperty(
                d.getValue().isDetail() ? "" : d.getValue().getMerchandise().getName()));
    unitColumn.setCellValueFactory(
        d ->
            new javafx.beans.property.SimpleStringProperty(
                d.getValue().isDetail() ? "" : d.getValue().getMerchandise().getUnit()));
    categoryColumn.setCellValueFactory(
        d ->
            new javafx.beans.property.SimpleStringProperty(
                d.getValue().isDetail() ? "" : d.getValue().getMerchandise().getCategory()));
    statusColumn.setCellValueFactory(
        d ->
            new javafx.beans.property.SimpleStringProperty(
                d.getValue().isDetail() ? "" : d.getValue().getMerchandise().getStatus()));

    codeColumn.setCellFactory(c -> dataCell(true));
    nameColumn.setCellFactory(c -> dataCell(true));
    unitColumn.setCellFactory(c -> dataCell(false));
    categoryColumn.setCellFactory(c -> dataCell(false));
    statusColumn.setCellFactory(c -> statusCell());
    expandColumn.setCellFactory(c -> expandCell());
    actionsColumn.setCellFactory(c -> actionsCell());

    merchandiseTable.setRowFactory(
        tv ->
            new TableRow<>() {
              @Override
              protected void updateItem(MerchandiseTableRowDTO item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().remove("detail-row");
                if (item != null && item.isDetail()) {
                  getStyleClass().add("detail-row");
                }
              }
            });
  }

  private TableCell<MerchandiseTableRowDTO, String> dataCell(boolean codeStyle) {
    return new TableCell<>() {
      @Override
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || getTableRow() == null || getTableRow().getItem() == null) {
          setGraphic(null);
          setText(null);
          return;
        }
        MerchandiseTableRowDTO row = getTableRow().getItem();
        if (row.isDetail()) {
          if (getTableColumn() == codeColumn) {
            Merchandise m = row.getMerchandise();
            GridPane grid = new GridPane();
            grid.setHgap(12);
            grid.setVgap(4);
            grid.add(pair("Giá:", m.getPrice()), 0, 0);
            grid.add(pair("Nhà cung cấp:", m.getSupplier()), 1, 0);
            Label t = new Label("Mô tả:");
            t.getStyleClass().add("detail-label");
            Label d = new Label(m.getDescription());
            d.getStyleClass().add("detail-text");
            d.setWrapText(true);
            grid.add(t, 0, 1);
            grid.add(d, 1, 1);
            setGraphic(grid);
            setText(null);
          } else {
            setGraphic(null);
            setText(null);
          }
        } else {
          setGraphic(null);
          if (item == null) {
            setText(null);
          } else if (codeStyle && getTableColumn() == codeColumn) {
            setText(item);
            getStyleClass().setAll("code-link");
          } else if (getTableColumn() == nameColumn) {
            setText(item);
            getStyleClass().setAll("text-xs-bold");
          } else {
            setText(item);
            getStyleClass().setAll("text-xs");
          }
        }
      }
    };
  }

  private HBox pair(String label, String value) {
    Label l = new Label(label);
    l.getStyleClass().add("detail-label");
    Label v = new Label(value);
    v.getStyleClass().add("detail-text");
    return new HBox(8, l, v);
  }

  private TableCell<MerchandiseTableRowDTO, String> statusCell() {
    return new TableCell<>() {
      @Override
      protected void updateItem(String status, boolean empty) {
        super.updateItem(status, empty);
        if (empty || getTableRow() == null || getTableRow().getItem() == null
            || getTableRow().getItem().isDetail()) {
          setGraphic(null);
          return;
        }
        Label badge =
            new Label("active".equals(status) ? "Đang hoạt động" : "Ngừng hoạt động");
        badge.getStyleClass().add("active".equals(status) ? "badge-active" : "badge-inactive");
        setGraphic(badge);
        setText(null);
      }
    };
  }

  private TableCell<MerchandiseTableRowDTO, Void> expandCell() {
    return new TableCell<>() {
      private final Button btn = new Button("▼");

      {
        btn.getStyleClass().add("expand-btn");
        btn.setOnAction(
            e -> {
              MerchandiseTableRowDTO row =
                  getTableRow() != null ? getTableRow().getItem() : null;
              if (row != null && !row.isDetail()) {
                toggleExpand(row.getMerchandise());
              }
            });
      }

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || getTableRow() == null || getTableRow().getItem() == null
            || getTableRow().getItem().isDetail()) {
          setGraphic(null);
        } else {
          Merchandise m = getTableRow().getItem().getMerchandise();
          btn.setText(expandedIds.contains(m.getId()) ? "▲" : "▼");
          setGraphic(btn);
        }
      }
    };
  }

  private TableCell<MerchandiseTableRowDTO, Void> actionsCell() {
    return new TableCell<>() {
      private final Button editBtn = new Button("✎");
      private final Button deleteBtn = new Button("🗑");

      {
        editBtn.getStyleClass().addAll("icon-btn", "icon-btn-edit");
        deleteBtn.getStyleClass().addAll("icon-btn", "icon-btn-delete");
        editBtn.setOnAction(
            e -> {
              MerchandiseTableRowDTO row =
                  getTableRow() != null ? getTableRow().getItem() : null;
              if (row != null && !row.isDetail()) {
                ActionLog.stub(
                    "Quản lý Mặt hàng: Sửa mặt hàng " + row.getMerchandise().getCode());
              }
            });
        deleteBtn.setOnAction(
            e -> {
              MerchandiseTableRowDTO row =
                  getTableRow() != null ? getTableRow().getItem() : null;
              if (row != null && !row.isDetail()) {
                ActionLog.stub(
                    "Quản lý Mặt hàng: Xóa mặt hàng " + row.getMerchandise().getCode());
              }
            });
      }

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || getTableRow() == null || getTableRow().getItem() == null
            || getTableRow().getItem().isDetail()) {
          setGraphic(null);
        } else {
          HBox box = new HBox(4, editBtn, deleteBtn);
          box.getStyleClass().add("actions-box");
          setGraphic(box);
        }
      }
    };
  }

  private void toggleExpand(Merchandise item) {
    if (expandedIds.contains(item.getId())) {
      expandedIds.remove(item.getId());
    } else {
      expandedIds.add(item.getId());
      ActionLog.stub("Quản lý Mặt hàng: Mở rộng chi tiết " + item.getCode());
    }
    rebuildTable();
  }

  private void rebuildTable() {
    List<Merchandise> items =
        merchandiseService.searchMerchandise(pageDto.getSearchQuery(), selectedCategory);
    tableRows.setAll(merchandiseService.buildTableRows(items, expandedIds));
    pageDto.setPaginationText(merchandiseService.buildPaginationSummary(items.size()));
    paginationController.setSummaryText(pageDto.getPaginationText());
  }

  private void openAddDialog() {
    AddMerchandiseDialogUI dialog = new AddMerchandiseDialogUI();
    dialog.prepareForNewItem(merchandiseService.nextMerchandiseCode());
    dialog.setOnClose(this::closeAddDialog);
    dialog.setOnSave(this::handleSaveNewItem);
    dialogOverlay.getChildren().setAll(dialog);
    dialogOverlay.setVisible(true);
    dialogOverlay.setManaged(true);
  }

  private void closeAddDialog() {
    dialogOverlay.getChildren().clear();
    dialogOverlay.setVisible(false);
    dialogOverlay.setManaged(false);
  }

  private void handleSaveNewItem(AddMerchandiseDTO form) {
    String error = merchandiseService.createMerchandise(form);
    if (error != null) {
      ActionLog.stub("Thêm mặt hàng: " + error);
      return;
    }
    rebuildTable();
    closeAddDialog();
  }

  public MerchandiseListPageDTO getPageDto() {
    return pageDto;
  }
}

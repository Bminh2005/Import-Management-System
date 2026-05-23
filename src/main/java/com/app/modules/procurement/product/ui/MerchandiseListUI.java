package com.app.modules.procurement.product.ui;

import com.app.common.ui.components.FilterChipsUI;
import com.app.common.ui.components.FilterChipsUI.ChipOption;
import com.app.common.ui.components.PageHeaderUI;
import com.app.common.ui.components.PaginationBarUI;
import com.app.common.ui.components.SearchBarUI;
import com.app.common.util.FxmlUiHelper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

/** UI màn Quản lý Mặt hàng (dữ liệu mẫu — chưa có Service). */
public class MerchandiseListUI extends StackPane {

  private static final class MerchandiseItem {
    final String id;
    final String code;
    final String name;
    final String unit;
    final String category;
    final String categoryKey;
    final String status;
    final String price;
    final String supplier;
    final String description;

    MerchandiseItem(
        String id,
        String code,
        String name,
        String unit,
        String category,
        String categoryKey,
        String status,
        String price,
        String supplier,
        String description) {
      this.id = id;
      this.code = code;
      this.name = name;
      this.unit = unit;
      this.category = category;
      this.categoryKey = categoryKey;
      this.status = status;
      this.price = price;
      this.supplier = supplier;
      this.description = description;
    }
  }

  private static final class TableRowItem {
    final MerchandiseItem item;
    final boolean detail;

    TableRowItem(MerchandiseItem item, boolean detail) {
      this.item = item;
      this.detail = detail;
    }
  }

  @FXML private StackPane headerPlaceholder;
  @FXML private StackPane searchPlaceholder;
  @FXML private StackPane categoryChipsPlaceholder;
  @FXML private TableView<TableRowItem> merchandiseTable;
  @FXML private TableColumn<TableRowItem, Void> expandColumn;
  @FXML private TableColumn<TableRowItem, String> codeColumn;
  @FXML private TableColumn<TableRowItem, String> nameColumn;
  @FXML private TableColumn<TableRowItem, String> unitColumn;
  @FXML private TableColumn<TableRowItem, String> categoryColumn;
  @FXML private TableColumn<TableRowItem, String> statusColumn;
  @FXML private TableColumn<TableRowItem, Void> actionsColumn;
  @FXML private StackPane paginationPlaceholder;
  @FXML private StackPane dialogOverlay;

  private PageHeaderUI pageHeader;
  private SearchBarUI searchBar;
  private FilterChipsUI filterChips;
  private PaginationBarUI paginationBar;

  private final List<MerchandiseItem> allItems = new ArrayList<>(createMockItems());
  private final ObservableList<TableRowItem> tableRows = FXCollections.observableArrayList();
  private final Set<String> expandedIds = new HashSet<>();
  private String searchQuery = "";
  private String selectedCategory = "all";

  public MerchandiseListUI() {
    FxmlUiHelper.loadSelf(this, "MerchandiseListPage.fxml");
    FxmlUiHelper.addStylesheet(this, "merchandise-list.css");
    loadComponents();
    bindHeaderAndSearch();
    setupCategoryFilters();
    setupTable();
    rebuildTable();
  }

  private static List<MerchandiseItem> createMockItems() {
    return List.of(
        new MerchandiseItem(
            "1", "MH001", "Laptop Dell XPS 13", "cái", "Điện tử", "electronics", "active",
            "25,000,000 VND", "Dell Vietnam", "Laptop cao cấp cho doanh nghiệp"),
        new MerchandiseItem(
            "2", "MH002", "Bàn phím cơ Keychron K2", "cái", "Phụ kiện", "accessories", "active",
            "2,500,000 VND", "Keychron", "Bàn phím cơ không dây"),
        new MerchandiseItem(
            "3", "MH003", "Chuột Logitech MX Master 3", "cái", "Phụ kiện", "accessories", "active",
            "2,200,000 VND", "Logitech", "Chuột không dây cao cấp"),
        new MerchandiseItem(
            "4", "MH004", "Màn hình LG UltraWide 34\"", "cái", "Điện tử", "electronics", "inactive",
            "12,000,000 VND", "LG Electronics", "Màn hình siêu rộng cho đa nhiệm"));
  }

  private void loadComponents() {
    pageHeader = new PageHeaderUI();
    headerPlaceholder.getChildren().setAll(pageHeader);
    searchBar = new SearchBarUI();
    searchPlaceholder.getChildren().setAll(searchBar);
    filterChips = new FilterChipsUI();
    categoryChipsPlaceholder.getChildren().setAll(filterChips);
    paginationBar = new PaginationBarUI();
    paginationPlaceholder.getChildren().setAll(paginationBar);
  }

  private void bindHeaderAndSearch() {
    pageHeader.setTitle("Quản lý Mặt hàng");
    pageHeader.setSubtitle("Danh sách và quản lý thông tin mặt hàng nhập khẩu");
    pageHeader.setActionButton("+ Thêm Mặt hàng Mới", this::openAddDialog);
    searchBar.setPromptText("Tìm kiếm theo mã hàng hoặc tên...");
    searchBar.setOnSearchChanged(
        query -> {
          searchQuery = query == null ? "" : query;
          rebuildTable();
        });
    paginationBar.setSummaryText("Hiển thị 1-4 trong 24 mặt hàng");
  }

  private void setupCategoryFilters() {
    filterChips.bindChips(
        List.of(
            new ChipOption("all", "Tất cả", "24", true),
            new ChipOption("electronics", "Điện tử", "12", false),
            new ChipOption("accessories", "Phụ kiện", "8", false),
            new ChipOption("furniture", "Nội thất", "4", false)));
    filterChips.setOnFilterSelected(
        value -> {
          selectedCategory = value;
          rebuildTable();
        });
  }

  private void setupTable() {
    merchandiseTable.setItems(tableRows);
    merchandiseTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    expandColumn.setResizable(false);

    codeColumn.setCellValueFactory(
        d -> new javafx.beans.property.SimpleStringProperty(
            d.getValue().detail ? "" : d.getValue().item.code));
    nameColumn.setCellValueFactory(
        d -> new javafx.beans.property.SimpleStringProperty(
            d.getValue().detail ? "" : d.getValue().item.name));
    unitColumn.setCellValueFactory(
        d -> new javafx.beans.property.SimpleStringProperty(
            d.getValue().detail ? "" : d.getValue().item.unit));
    categoryColumn.setCellValueFactory(
        d -> new javafx.beans.property.SimpleStringProperty(
            d.getValue().detail ? "" : d.getValue().item.category));
    statusColumn.setCellValueFactory(
        d -> new javafx.beans.property.SimpleStringProperty(
            d.getValue().detail ? "" : d.getValue().item.status));

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
              protected void updateItem(TableRowItem item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().remove("detail-row");
                if (item != null && item.detail) {
                  getStyleClass().add("detail-row");
                }
              }
            });
  }

  private TableCell<TableRowItem, String> dataCell(boolean codeStyle) {
    return new TableCell<>() {
      @Override
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || getTableRow() == null || getTableRow().getItem() == null) {
          setGraphic(null);
          setText(null);
          return;
        }
        TableRowItem row = getTableRow().getItem();
        if (row.detail) {
          if (getTableColumn() == codeColumn) {
            MerchandiseItem m = row.item;
            GridPane grid = new GridPane();
            grid.setHgap(12);
            grid.setVgap(4);
            grid.add(pair("Giá:", m.price), 0, 0);
            grid.add(pair("Nhà cung cấp:", m.supplier), 1, 0);
            Label t = new Label("Mô tả:");
            t.getStyleClass().add("detail-label");
            Label d = new Label(m.description);
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

  private TableCell<TableRowItem, String> statusCell() {
    return new TableCell<>() {
      @Override
      protected void updateItem(String status, boolean empty) {
        super.updateItem(status, empty);
        if (empty || getTableRow() == null || getTableRow().getItem() == null
            || getTableRow().getItem().detail) {
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

  private TableCell<TableRowItem, Void> expandCell() {
    return new TableCell<>() {
      private final Button btn = new Button("▼");

      {
        btn.getStyleClass().add("expand-btn");
        btn.setOnAction(
            e -> {
              TableRowItem row = getTableRow() != null ? getTableRow().getItem() : null;
              if (row != null && !row.detail) {
                toggleExpand(row.item);
              }
            });
      }

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || getTableRow() == null || getTableRow().getItem() == null
            || getTableRow().getItem().detail) {
          setGraphic(null);
        } else {
          MerchandiseItem m = getTableRow().getItem().item;
          btn.setText(expandedIds.contains(m.id) ? "▲" : "▼");
          setGraphic(btn);
        }
      }
    };
  }

  private TableCell<TableRowItem, Void> actionsCell() {
    return new TableCell<>() {
      private final Button editBtn = new Button("✎");
      private final Button deleteBtn = new Button("🗑");

      {
        editBtn.getStyleClass().addAll("icon-btn", "icon-btn-edit");
        deleteBtn.getStyleClass().addAll("icon-btn", "icon-btn-delete");
      }

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || getTableRow() == null || getTableRow().getItem() == null
            || getTableRow().getItem().detail) {
          setGraphic(null);
        } else {
          HBox box = new HBox(4, editBtn, deleteBtn);
          box.getStyleClass().add("actions-box");
          setGraphic(box);
        }
      }
    };
  }

  private void toggleExpand(MerchandiseItem item) {
    if (expandedIds.contains(item.id)) {
      expandedIds.remove(item.id);
    } else {
      expandedIds.add(item.id);
    }
    rebuildTable();
  }

  private void rebuildTable() {
    String q = searchQuery.toLowerCase();
    List<MerchandiseItem> filtered = new ArrayList<>();
    for (MerchandiseItem m : allItems) {
      if (!q.isBlank()
          && !m.code.toLowerCase().contains(q)
          && !m.name.toLowerCase().contains(q)) {
        continue;
      }
      if (!"all".equals(selectedCategory) && !selectedCategory.equals(m.categoryKey)) {
        continue;
      }
      filtered.add(m);
    }

    List<TableRowItem> rows = new ArrayList<>();
    for (MerchandiseItem m : filtered) {
      rows.add(new TableRowItem(m, false));
      if (expandedIds.contains(m.id)) {
        rows.add(new TableRowItem(m, true));
      }
    }
    tableRows.setAll(rows);
    paginationBar.setSummaryText(
        String.format("Hiển thị 1-%d trong 24 mặt hàng", Math.min(filtered.size(), 4)));
  }

  private void openAddDialog() {
    AddMerchandiseDialogUI dialog = new AddMerchandiseDialogUI();
    dialog.prepareForNewItem("MH005");
    dialog.setOnClose(this::closeAddDialog);
    dialog.setOnSave(
        form -> {
          if (form.getNameValue() != null && !form.getNameValue().isBlank()) {
            allItems.add(
                new MerchandiseItem(
                    String.valueOf(System.currentTimeMillis()),
                    "MH00" + (allItems.size() + 1),
                    form.getNameValue(),
                    form.getUnitValue() == null || form.getUnitValue().isBlank()
                        ? "cái"
                        : form.getUnitValue(),
                    form.getCategoryValue() == null ? "Điện tử" : form.getCategoryValue(),
                    categoryKey(form.getCategoryValue()),
                    "active",
                    "",
                    "",
                    ""));
          }
          rebuildTable();
          closeAddDialog();
        });
    dialogOverlay.getChildren().setAll(dialog);
    dialogOverlay.setVisible(true);
    dialogOverlay.setManaged(true);
  }

  private static String categoryKey(String category) {
    if (category == null) {
      return "other";
    }
    return switch (category) {
      case "Điện tử" -> "electronics";
      case "Phụ kiện" -> "accessories";
      case "Nội thất" -> "furniture";
      default -> "other";
    };
  }

  private void closeAddDialog() {
    dialogOverlay.getChildren().clear();
    dialogOverlay.setVisible(false);
    dialogOverlay.setManaged(false);
  }
}

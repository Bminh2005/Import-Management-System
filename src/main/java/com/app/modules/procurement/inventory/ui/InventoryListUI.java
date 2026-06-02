package com.app.modules.procurement.inventory.ui;

import com.app.modules.procurement.inventory.dto.InventoryItemResponse;
import com.app.modules.procurement.inventory.entity.SiteStock;
import com.app.modules.procurement.inventory.service.InventoryService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * UI Class cho màn "Mặt hàng Tồn kho" (danh sách) của Bộ phận Đặt hàng Quốc tế.
 * Hiển thị tồn kho của các mặt hàng tại các site quốc tế để phục vụ
 * việc phân bổ đơn hàng.
 *
 * Mỗi dòng có nút "Xem chi tiết" để mở rộng inline hiển thị
 * các thẻ tồn kho theo từng site; click lại sẽ thu gọn.
 *
 * Theo README: chỉ gọi service, không chạm repository.
 */
public class InventoryListUI extends ScrollPane {

    @FXML private TextField searchField;
    @FXML private ToggleGroup categoryGroup;
    @FXML private ToggleButton chipAll;

    @FXML private VBox itemsList;
    @FXML private Pane emptyContainer;
    @FXML private Label countLabel;

    private final InventoryService service = new InventoryService();
    private List<InventoryItemResponse> allItems = new ArrayList<>();
    private final Set<String> expanded = new HashSet<>();

    public InventoryListUI() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "InventoryListPage.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadData();
        setupFilters();
        renderList();
    }

    private void loadData() {
        this.allItems = new ArrayList<>(service.getAllItems());
    }

    private void setupFilters() {
        searchField.textProperty().addListener((obs, oldV, newV) -> renderList());
        categoryGroup.selectedToggleProperty().addListener((obs, oldT, newT) -> {
            // Luôn giữ 1 chip được chọn
            if (newT == null && oldT != null) {
                oldT.setSelected(true);
                return;
            }
            renderList();
        });
    }

    private void renderList() {
        itemsList.getChildren().clear();
        List<InventoryItemResponse> filtered = applyFilter();

        countLabel.setText("(" + filtered.size() + " mặt hàng)");

        boolean hasResult = !filtered.isEmpty();
        itemsList.setVisible(hasResult);
        itemsList.setManaged(hasResult);
        emptyContainer.setVisible(!hasResult);
        emptyContainer.setManaged(!hasResult);

        for (InventoryItemResponse item : filtered) {
            itemsList.getChildren().add(buildItemRow(item));
        }
    }

    private List<InventoryItemResponse> applyFilter() {
        String keyword = searchField.getText() == null
                ? "" : searchField.getText().trim().toLowerCase();
        Toggle selected = categoryGroup.getSelectedToggle();
        String category = selected == null
                ? "Tất cả" : String.valueOf(selected.getUserData());

        List<InventoryItemResponse> result = new ArrayList<>();
        for (InventoryItemResponse item : allItems) {
            boolean matchKeyword = keyword.isEmpty()
                    || item.getCode().toLowerCase().contains(keyword)
                    || item.getName().toLowerCase().contains(keyword);
            boolean matchCategory = "Tất cả".equals(category)
                    || category.equals(item.getCategory());
            if (matchKeyword && matchCategory) {
                result.add(item);
            }
        }
        return result;
    }

    // ============================================================
    //   Build 1 dòng (gồm phần chính + phần mở rộng nếu có)
    // ============================================================
    private VBox buildItemRow(InventoryItemResponse item) {
        VBox container = new VBox();

        GridPane main = buildMainRow(item);
        container.getChildren().add(main);

        if (expanded.contains(item.getCode())) {
            container.getChildren().add(buildExpansion(item));
        }
        return container;
    }

    private GridPane buildMainRow(InventoryItemResponse item) {
        GridPane row = new GridPane();
        row.getStyleClass().add("list-row");
        row.setHgap(12);
        applyColumnConstraints(row);

        // 0: Mã
        Label code = new Label(item.getCode());
        code.getStyleClass().add("cell-code");
        row.add(code, 0, 0);

        // 1: Icon + Tên
        Label icon = new Label("📦");
        icon.getStyleClass().add("package-icon");
        Label name = new Label(item.getName());
        name.getStyleClass().add("cell-name");
        HBox nameBox = new HBox(8, icon, name);
        nameBox.setAlignment(Pos.CENTER_LEFT);
        row.add(nameBox, 1, 0);

        // 2: Danh mục badge
        Label category = new Label(item.getCategory());
        category.getStyleClass().add("category-badge");
        HBox categoryBox = new HBox(category);
        categoryBox.setAlignment(Pos.CENTER_LEFT);
        row.add(categoryBox, 2, 0);

        // 3: Tổng tồn kho
        Label totalStock = new Label(
                String.format("%,d %s", item.getTotalStock(), item.getUnit()));
        totalStock.getStyleClass().add("total-stock-value");
        row.add(totalStock, 3, 0);

        // 4: Số site (pill tròn)
        Label sitePill = new Label(String.valueOf(item.getSiteCount()));
        sitePill.getStyleClass().add("site-count-pill");
        HBox siteBox = new HBox(sitePill);
        siteBox.setAlignment(Pos.CENTER_LEFT);
        row.add(siteBox, 4, 0);

        // 5: Nút toggle "Xem chi tiết ▼ / Thu gọn ▲"
        boolean isExpanded = expanded.contains(item.getCode());
        Button toggle = new Button(isExpanded
                ? "Thu gọn  ▲"
                : "Xem chi tiết  ▼");
        toggle.getStyleClass().add("detail-toggle");
        toggle.setOnAction(e -> toggleExpand(item.getCode()));

        HBox actionBox = new HBox(toggle);
        actionBox.setAlignment(Pos.CENTER_RIGHT);
        row.add(actionBox, 5, 0);
        GridPane.setHalignment(actionBox, HPos.RIGHT);

        return row;
    }

    private VBox buildExpansion(InventoryItemResponse item) {
        VBox area = new VBox(10);
        area.getStyleClass().add("expansion-area");

        Label title = new Label("Tồn kho tại các site:");
        title.getStyleClass().add("expansion-title");
        area.getChildren().add(title);

        HBox cards = new HBox(12);
        for (SiteStock site : item.getSites()) {
            cards.getChildren().add(buildSiteCard(site, item.getUnit()));
        }
        area.getChildren().add(cards);

        return area;
    }

    private Node buildSiteCard(SiteStock site, String unit) {
        // Left: SITE code + tên
        Label codeLbl = new Label(site.getSiteCode());
        codeLbl.getStyleClass().add("site-card-code");
        Label nameLbl = new Label(site.getSiteName());
        nameLbl.getStyleClass().add("site-card-name");
        VBox leftCol = new VBox(2, codeLbl, nameLbl);
        leftCol.setAlignment(Pos.CENTER_LEFT);

        // Right: stock + unit
        Label stockLbl = new Label(String.format("%,d", site.getStock()));
        stockLbl.getStyleClass().add("site-card-stock");
        Label unitLbl = new Label(unit);
        unitLbl.getStyleClass().add("site-card-unit");
        VBox rightCol = new VBox(0, stockLbl, unitLbl);
        rightCol.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox card = new HBox(12, leftCol, spacer, rightCol);
        card.getStyleClass().add("site-card");
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPrefWidth(280);
        card.setMinWidth(220);
        HBox.setHgrow(card, Priority.SOMETIMES);
        return card;
    }

    private void toggleExpand(String code) {
        if (expanded.contains(code)) {
            expanded.remove(code);
            System.out.println("Nội dung chức năng: Thu gọn mặt hàng " + code);
        } else {
            expanded.add(code);
            System.out.println("Nội dung chức năng: Xem chi tiết mặt hàng " + code);
        }
        renderList();
    }

    /** ColumnConstraints dùng chung cho header và mỗi dòng để các cột thẳng hàng. */
    private void applyColumnConstraints(GridPane grid) {
        addColumn(grid, 13);
        addColumn(grid, 30);
        addColumn(grid, 13);
        addColumn(grid, 17);
        addColumn(grid, 10);
        addColumn(grid, 17);
    }

    private void addColumn(GridPane grid, double percent) {
        ColumnConstraints c = new ColumnConstraints();
        c.setPercentWidth(percent);
        grid.getColumnConstraints().add(c);
    }
}

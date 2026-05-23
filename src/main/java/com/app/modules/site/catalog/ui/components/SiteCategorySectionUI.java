package com.app.modules.site.catalog.ui.components;

import com.app.common.util.FxmlUiHelper;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

/** Bảng mặt hàng theo một danh mục trên Chi tiết Site. */
public class SiteCategorySectionUI extends VBox {

  public static class ItemRow {
    private final StringProperty code = new SimpleStringProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty quantityText = new SimpleStringProperty();
    private final StringProperty unitPriceText = new SimpleStringProperty();
    private final StringProperty lastUpdated = new SimpleStringProperty();

    public ItemRow(
        String code, String name, String quantityText, String unitPriceText, String lastUpdated) {
      this.code.set(code);
      this.name.set(name);
      this.quantityText.set(quantityText);
      this.unitPriceText.set(unitPriceText);
      this.lastUpdated.set(lastUpdated);
    }

    public String getCode() {
      return code.get();
    }

    public String getName() {
      return name.get();
    }

    public String getQuantityText() {
      return quantityText.get();
    }

    public String getUnitPriceText() {
      return unitPriceText.get();
    }

    public String getLastUpdated() {
      return lastUpdated.get();
    }
  }

  @FXML private Label categoryLabel;
  @FXML private Label countLabel;
  @FXML private TableView<ItemRow> itemsTable;
  @FXML private TableColumn<ItemRow, String> codeColumn;
  @FXML private TableColumn<ItemRow, String> nameColumn;
  @FXML private TableColumn<ItemRow, String> quantityColumn;
  @FXML private TableColumn<ItemRow, String> priceColumn;
  @FXML private TableColumn<ItemRow, String> updatedColumn;

  public SiteCategorySectionUI() {
    FxmlUiHelper.loadSelf(this, "SiteCategorySection.fxml");
    FxmlUiHelper.addStylesheet(this, "site-category-section.css");
    codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantityText"));
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPriceText"));
    updatedColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));
    itemsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
  }

  public void bind(String categoryName, String itemCountText, List<ItemRow> items) {
    categoryLabel.setText(categoryName);
    countLabel.setText(itemCountText);
    itemsTable.setItems(FXCollections.observableArrayList(items));
    itemsTable.setPrefHeight(Math.min(200, 40 + items.size() * 36));
  }
}

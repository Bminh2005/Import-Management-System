package com.app.modules.site.catalog.ui.components;

import com.app.common.util.FxmlUiHelper;
import com.app.modules.site.catalog.dto.SiteCategorySectionDTO;
import com.app.modules.site.catalog.dto.SiteItemRowDTO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

/** Bảng mặt hàng theo một danh mục trên Chi tiết Site. */
public class SiteCategorySectionUI extends VBox {

  @FXML private Label categoryLabel;
  @FXML private Label countLabel;
  @FXML private TableView<SiteItemRowDTO> itemsTable;
  @FXML private TableColumn<SiteItemRowDTO, String> codeColumn;
  @FXML private TableColumn<SiteItemRowDTO, String> nameColumn;
  @FXML private TableColumn<SiteItemRowDTO, String> quantityColumn;
  @FXML private TableColumn<SiteItemRowDTO, String> priceColumn;
  @FXML private TableColumn<SiteItemRowDTO, String> updatedColumn;

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

  public void bind(SiteCategorySectionDTO vm) {
    categoryLabel.setText(vm.getCategoryName());
    countLabel.setText(vm.getItemCountText());
    itemsTable.setItems(FXCollections.observableArrayList(vm.getItems()));
    itemsTable.setPrefHeight(Math.min(200, 40 + vm.getItems().size() * 36));
  }
}

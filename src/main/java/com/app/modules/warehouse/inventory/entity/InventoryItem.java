package com.app.modules.warehouse.inventory.entity;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Một mặt hàng tồn kho: tổng tồn + danh sách tồn theo site.
 * Tổng tồn hiển thị ở danh sách; danh sách site hiển thị ở chi tiết.
 */
public class InventoryItem {

    private final StringProperty code;
    private final StringProperty name;
    private final StringProperty category;
    private final StringProperty unit;
    private final IntegerProperty totalStock;
    private final ObservableList<SiteStock> sites = FXCollections.observableArrayList();

    public InventoryItem(String code, String name, String category, String unit,
                         int totalStock) {
        this.code = new SimpleStringProperty(code);
        this.name = new SimpleStringProperty(name);
        this.category = new SimpleStringProperty(category);
        this.unit = new SimpleStringProperty(unit);
        this.totalStock = new SimpleIntegerProperty(totalStock);
    }

    // ----- Getters -----
    public String getCode() { return code.get(); }
    public String getName() { return name.get(); }
    public String getCategory() { return category.get(); }
    public String getUnit() { return unit.get(); }
    public int getTotalStock() { return totalStock.get(); }
    public ObservableList<SiteStock> getSites() { return sites; }
    public int getSiteCount() { return sites.size(); }

    // ----- Setters -----
    public void setCode(String value) { code.set(value); }
    public void setName(String value) { name.set(value); }
    public void setCategory(String value) { category.set(value); }
    public void setUnit(String value) { unit.set(value); }
    public void setTotalStock(int value) { totalStock.set(value); }

    // ----- Property accessors (cho TableView) -----
    public StringProperty codeProperty() { return code; }
    public StringProperty nameProperty() { return name; }
    public StringProperty categoryProperty() { return category; }
    public StringProperty unitProperty() { return unit; }
    public IntegerProperty totalStockProperty() { return totalStock; }
}

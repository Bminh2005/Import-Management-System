package com.app.modules.warehouse.inventory.entity;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Tồn kho của một mặt hàng tại một site.
 * Dùng làm dòng trong bảng "Tồn kho tại các site" của màn chi tiết.
 */
public class SiteStock {

    private final StringProperty siteCode;
    private final StringProperty siteName;
    private final IntegerProperty stock;

    public SiteStock(String siteCode, String siteName, int stock) {
        this.siteCode = new SimpleStringProperty(siteCode);
        this.siteName = new SimpleStringProperty(siteName);
        this.stock = new SimpleIntegerProperty(stock);
    }

    // ----- Getters -----
    public String getSiteCode() { return siteCode.get(); }
    public String getSiteName() { return siteName.get(); }
    public int getStock() { return stock.get(); }

    // ----- Setters -----
    public void setSiteCode(String value) { siteCode.set(value); }
    public void setSiteName(String value) { siteName.set(value); }
    public void setStock(int value) { stock.set(value); }

    // ----- Property accessors (cho TableView) -----
    public StringProperty siteCodeProperty() { return siteCode; }
    public StringProperty siteNameProperty() { return siteName; }
    public IntegerProperty stockProperty() { return stock; }
}

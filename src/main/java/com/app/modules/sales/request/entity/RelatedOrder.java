package com.app.modules.sales.request.entity;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Đơn hàng đặt quốc tế được tạo ra từ một yêu cầu nhập hàng.
 * Tham chiếu lại request thông qua mã yêu cầu (chưa modelhóa quan hệ).
 */
public class RelatedOrder {

    private final StringProperty code;
    private final StringProperty orderDate;
    private final StringProperty site;
    private final IntegerProperty itemCount;
    private final StringProperty status;
    private final LongProperty totalValue;

    public RelatedOrder(String code, String orderDate, String site,
                        int itemCount, String status, long totalValue) {
        this.code = new SimpleStringProperty(code);
        this.orderDate = new SimpleStringProperty(orderDate);
        this.site = new SimpleStringProperty(site);
        this.itemCount = new SimpleIntegerProperty(itemCount);
        this.status = new SimpleStringProperty(status);
        this.totalValue = new SimpleLongProperty(totalValue);
    }

    // ----- Getters -----
    public String getCode() { return code.get(); }
    public String getOrderDate() { return orderDate.get(); }
    public String getSite() { return site.get(); }
    public int getItemCount() { return itemCount.get(); }
    public String getStatus() { return status.get(); }
    public long getTotalValue() { return totalValue.get(); }

    // ----- Property accessors (cho TableView) -----
    public StringProperty codeProperty() { return code; }
    public StringProperty orderDateProperty() { return orderDate; }
    public StringProperty siteProperty() { return site; }
    public IntegerProperty itemCountProperty() { return itemCount; }
    public StringProperty statusProperty() { return status; }
    public LongProperty totalValueProperty() { return totalValue; }
}

package com.app.modules.warehouse.inventory.dto;

import com.app.modules.warehouse.inventory.entity.InventoryItem;
import com.app.modules.warehouse.inventory.entity.SiteStock;
import javafx.collections.ObservableList;

/**
 * DTO trả từ service xuống UI cho một mặt hàng tồn kho.
 * Giữ tham chiếu thẳng tới ObservableList của entity để TableView
 * bind trực tiếp; UI không cần biết đến entity.
 */
public class InventoryItemResponse {

    private final String code;
    private final String name;
    private final String category;
    private final String unit;
    private final int totalStock;
    private final ObservableList<SiteStock> sites;

    public InventoryItemResponse(InventoryItem item) {
        this.code = item.getCode();
        this.name = item.getName();
        this.category = item.getCategory();
        this.unit = item.getUnit();
        this.totalStock = item.getTotalStock();
        this.sites = item.getSites();
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getUnit() { return unit; }
    public int getTotalStock() { return totalStock; }
    public int getSiteCount() { return sites == null ? 0 : sites.size(); }
    public ObservableList<SiteStock> getSites() { return sites; }
}

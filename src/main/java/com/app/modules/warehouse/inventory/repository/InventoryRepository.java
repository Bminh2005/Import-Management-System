package com.app.modules.warehouse.inventory.repository;

import com.app.modules.warehouse.inventory.entity.InventoryItem;
import com.app.modules.warehouse.inventory.entity.SiteStock;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Repository cho InventoryItem. In-memory; khi nối DB chỉ cần thay
 * findAll / findByCode / save bằng SQL/JDBC, các tầng khác giữ nguyên.
 */
public class InventoryRepository {

    private final Map<String, InventoryItem> store = new LinkedHashMap<>();

    public InventoryRepository() {
        seed();
    }

    /** SELECT tất cả mặt hàng tồn kho. */
    public ObservableList<InventoryItem> findAll() {
        return FXCollections.observableArrayList(store.values());
    }

    /** SELECT theo mã mặt hàng. */
    public Optional<InventoryItem> findByCode(String code) {
        return Optional.ofNullable(store.get(code));
    }

    /** INSERT / UPDATE. */
    public void save(InventoryItem item) {
        if (item == null || item.getCode() == null) {
            return;
        }
        store.put(item.getCode(), item);
    }

    // -------- Dữ liệu mẫu (bỏ khi nối DB) --------
    private void seed() {
        InventoryItem item1 = new InventoryItem("MH001", "Vải lụa cao cấp",
                "Vải", "mét", 2500);
        item1.getSites().add(new SiteStock("SITE01", "Kho Hong Kong", 2000));
        item1.getSites().add(new SiteStock("SITE03", "Kho Bangkok", 500));
        store.put(item1.getCode(), item1);

        InventoryItem item2 = new InventoryItem("MH002", "Vải cotton organic",
                "Vải", "mét", 3200);
        item2.getSites().add(new SiteStock("SITE01", "Kho Hong Kong", 1850));
        item2.getSites().add(new SiteStock("SITE02", "Kho Singapore", 1350));
        store.put(item2.getCode(), item2);

        InventoryItem item3 = new InventoryItem("MH003", "Đá granite tự nhiên",
                "Đá", "viên", 1500);
        item3.getSites().add(new SiteStock("SITE01", "Kho Hong Kong", 450));
        item3.getSites().add(new SiteStock("SITE04", "Kho Shanghai", 1050));
        store.put(item3.getCode(), item3);

        InventoryItem item4 = new InventoryItem("MH004", "Màn hình LG UltraWide 34\"",
                "Điện tử", "cái", 150);
        item4.getSites().add(new SiteStock("SITE02", "Kho Singapore", 80));
        item4.getSites().add(new SiteStock("SITE03", "Kho Bangkok", 70));
        store.put(item4.getCode(), item4);

        InventoryItem item5 = new InventoryItem("MH005", "Webcam Logitech C920",
                "Điện tử", "cái", 300);
        item5.getSites().add(new SiteStock("SITE01", "Kho Hong Kong", 150));
        item5.getSites().add(new SiteStock("SITE02", "Kho Singapore", 100));
        item5.getSites().add(new SiteStock("SITE04", "Kho Shanghai", 50));
        store.put(item5.getCode(), item5);
    }
}

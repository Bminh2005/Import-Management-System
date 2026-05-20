package com.app.modules.warehouse.inventory.service;

import com.app.modules.warehouse.inventory.dto.InventoryItemResponse;
import com.app.modules.warehouse.inventory.entity.InventoryItem;
import com.app.modules.warehouse.inventory.repository.InventoryRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.NoSuchElementException;

/**
 * Service cho InventoryItem: điều phối giữa UI và Repository.
 *
 * Theo README: UI chỉ gọi service; service trả về DTO.
 */
public class InventoryService {

    private final InventoryRepository repository;

    public InventoryService() {
        this(new InventoryRepository());
    }

    public InventoryService(InventoryRepository repository) {
        this.repository = repository;
    }

    /** Lấy toàn bộ danh sách Mặt hàng Tồn kho (cho màn list). */
    public ObservableList<InventoryItemResponse> getAllItems() {
        ObservableList<InventoryItemResponse> result = FXCollections.observableArrayList();
        for (InventoryItem item : repository.findAll()) {
            result.add(new InventoryItemResponse(item));
        }
        return result;
    }

    /** Lấy chi tiết 1 mặt hàng tồn kho (cho màn detail). */
    public InventoryItemResponse getItemDetail(String code) {
        InventoryItem item = repository.findByCode(code)
                .orElseThrow(() -> new NoSuchElementException(
                        "Không tìm thấy mặt hàng " + code));
        return new InventoryItemResponse(item);
    }
}

package com.app.modules.sales.request.dto;

import com.app.modules.sales.request.entity.RequestItem;

import java.util.List;

/**
 * DTO mang dữ liệu chỉnh sửa từ UI xuống service khi nhấn "Lưu thay đổi"
 * ở màn hình chỉnh sửa yêu cầu nhập hàng.
 */
public class UpdateRequestDTO {

    private final String code;
    private final List<RequestItem> items;

    public UpdateRequestDTO(String code, List<RequestItem> items) {
        this.code = code;
        this.items = items;
    }

    public String getCode() { return code; }
    public List<RequestItem> getItems() { return items; }
}

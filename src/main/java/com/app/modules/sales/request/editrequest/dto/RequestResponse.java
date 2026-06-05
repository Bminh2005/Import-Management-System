package com.app.modules.sales.request.editrequest.dto;

import com.app.modules.sales.request.entity.Request;
import com.app.modules.sales.request.entity.RequestItem;
import javafx.collections.ObservableList;

/**
 * DTO trả từ service xuống UI cho một yêu cầu nhập hàng.
 * Giữ tham chiếu thẳng tới ObservableList items trong entity để
 * TableView ở UI bind trực tiếp mà không phải copy dữ liệu.
 */
public class RequestResponse {

    private final String code;
    private final String createdDate;
    private final String status;
    private final ObservableList<RequestItem> items;

    public RequestResponse(Request request) {
        this.code = request.getCode();
        this.createdDate = request.getCreatedDate();
        this.status = request.getStatus();
        this.items = request.getItems();
    }

    public String getCode() { return code; }
    public String getCreatedDate() { return createdDate; }
    public String getStatus() { return status; }
    public int getItemCount() { return items == null ? 0 : items.size(); }
    public ObservableList<RequestItem> getItems() { return items; }
}

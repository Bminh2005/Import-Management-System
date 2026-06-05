package com.app.modules.sales.request.requestdetail.dto;

import com.app.modules.sales.request.entity.RejectedItem;
import com.app.modules.sales.request.entity.RelatedOrder;
import com.app.modules.sales.request.entity.Request;
import com.app.modules.sales.request.entity.RequestItem;
import javafx.collections.ObservableList;

/**
 * DTO trả từ service xuống UI cho một yêu cầu nhập hàng.
 * Giữ tham chiếu thẳng tới các ObservableList trong entity để
 * TableView ở UI có thể bind trực tiếp mà không phải copy dữ liệu.
 */
public class RequestResponse {

    private final String code;
    private final String createdDate;
    private final String status;
    private final String createdBy;
    private final String assignedTo;
    private final ObservableList<RequestItem> items;
    private final ObservableList<RejectedItem> rejectedItems;
    private final ObservableList<RelatedOrder> orders;

    public RequestResponse(Request request) {
        this.code = request.getCode();
        this.createdDate = request.getCreatedDate();
        this.status = request.getStatus();
        this.createdBy = request.getCreatedBy();
        this.assignedTo = request.getAssignedTo();
        this.items = request.getItems();
        this.rejectedItems = request.getRejectedItems();
        this.orders = request.getOrders();
    }

    public String getCode() { return code; }
    public String getCreatedDate() { return createdDate; }
    public String getStatus() { return status; }
    public String getCreatedBy() { return createdBy; }
    public String getAssignedTo() { return assignedTo; }
    public int getItemCount() { return items == null ? 0 : items.size(); }

    public ObservableList<RequestItem> getItems() { return items; }
    public ObservableList<RejectedItem> getRejectedItems() { return rejectedItems; }
    public ObservableList<RelatedOrder> getOrders() { return orders; }
}

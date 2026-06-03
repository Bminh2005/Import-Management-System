package com.app.modules.sales.request.dto;

import com.app.modules.sales.request.entity.OrderItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * DTO trả từ service xuống UI cho popup "Chi tiết Đơn hàng".
 * Không kèm đơn giá / tổng tiền.
 */
public class OrderDetailResponse {

    private final String code;
    private final String orderDate;
    private final String status;
    private final String requestCode;
    private final String site;
    private final ObservableList<OrderItem> items;

    public OrderDetailResponse(String code, String orderDate, String status,
                               String requestCode, String site,
                               List<OrderItem> items) {
        this.code = code;
        this.orderDate = orderDate;
        this.status = status;
        this.requestCode = requestCode;
        this.site = site;
        this.items = FXCollections.observableArrayList(items);
    }

    public String getCode() { return code; }
    public String getOrderDate() { return orderDate; }
    public String getStatus() { return status; }
    public String getRequestCode() { return requestCode; }
    public String getSite() { return site; }
    public ObservableList<OrderItem> getItems() { return items; }
}

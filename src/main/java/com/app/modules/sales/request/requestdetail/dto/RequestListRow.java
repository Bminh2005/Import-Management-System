package com.app.modules.sales.request.requestdetail.dto;

/**
 * Một dòng trên màn danh sách yêu cầu nhập hàng.
 */
public class RequestListRow {

    private final String code;
    private final String createdDate;
    private final int itemCount;
    private final String status;

    public RequestListRow(String code, String createdDate, int itemCount, String status) {
        this.code = code;
        this.createdDate = createdDate;
        this.itemCount = itemCount;
        this.status = status;
    }

    public String getCode() { return code; }
    public String getCreatedDate() { return createdDate; }
    public int getItemCount() { return itemCount; }
    public String getStatus() { return status; }
}

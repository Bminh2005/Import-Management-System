package com.app.modules.sales.request.entity;

/**
 * Mặt hàng bị từ chối / hủy trong một yêu cầu nhập hàng.
 * Bất biến - chỉ dùng để hiển thị ở panel "Mặt hàng Bị từ chối / Hủy".
 */
public class RejectedItem {

    private final String code;
    private final String name;
    private final int quantity;
    private final String unit;
    /** "overseas" = Đặt hàng Quốc tế từ chối, "user" = Người dùng hủy. */
    private final String rejectedBy;
    private final String reason;
    private final String rejectedDate;

    public RejectedItem(String code, String name, int quantity, String unit,
                        String rejectedBy, String reason, String rejectedDate) {
        this.code = code;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.rejectedBy = rejectedBy;
        this.reason = reason;
        this.rejectedDate = rejectedDate;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public String getRejectedBy() { return rejectedBy; }
    public String getReason() { return reason; }
    public String getRejectedDate() { return rejectedDate; }
}

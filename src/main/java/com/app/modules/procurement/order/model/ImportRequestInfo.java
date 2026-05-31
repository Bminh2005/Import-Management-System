package com.app.modules.procurement.order.model;

public class ImportRequestInfo {
    private long requestId;
    private long userId;
    private String requesterName;
    private String desiredDate;

    public ImportRequestInfo() {
    }

    public ImportRequestInfo(long requestId, long userId, String requesterName, String desiredDate) {
        this.requestId = requestId;
        this.userId = userId;
        this.requesterName = requesterName;
        this.desiredDate = desiredDate;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getDesiredDate() {
        return desiredDate;
    }

    public void setDesiredDate(String desiredDate) {
        this.desiredDate = desiredDate;
    }
}

package com.app.modules.warehouse.dashboard.dto;

public class WarehouseDashboardSummary {
    private int pendingInboundCount;
    private int processingCount;
    private int importedThisMonthCount;
    private int mismatchCount;

    public WarehouseDashboardSummary() {
    }

    public WarehouseDashboardSummary(int pendingInboundCount, int processingCount,
                                     int importedThisMonthCount, int mismatchCount) {
        this.pendingInboundCount = pendingInboundCount;
        this.processingCount = processingCount;
        this.importedThisMonthCount = importedThisMonthCount;
        this.mismatchCount = mismatchCount;
    }

    public int getPendingInboundCount() {
        return pendingInboundCount;
    }

    public void setPendingInboundCount(int pendingInboundCount) {
        this.pendingInboundCount = pendingInboundCount;
    }

    public int getProcessingCount() {
        return processingCount;
    }

    public void setProcessingCount(int processingCount) {
        this.processingCount = processingCount;
    }

    public int getImportedThisMonthCount() {
        return importedThisMonthCount;
    }

    public void setImportedThisMonthCount(int importedThisMonthCount) {
        this.importedThisMonthCount = importedThisMonthCount;
    }

    public int getMismatchCount() {
        return mismatchCount;
    }

    public void setMismatchCount(int mismatchCount) {
        this.mismatchCount = mismatchCount;
    }
}

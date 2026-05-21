package com.app.modules.site.catalog.dto;

/** Dữ liệu một card Site trên lưới danh sách. */
public class SiteCardDTO {
  private String siteId;
  private String code;
  private String name;
  private String warehouse;
  private String location;
  private String statusLabel;
  private boolean active;
  private String inventoryText;

  public String getSiteId() {
    return siteId;
  }

  public void setSiteId(String siteId) {
    this.siteId = siteId;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getWarehouse() {
    return warehouse;
  }

  public void setWarehouse(String warehouse) {
    this.warehouse = warehouse;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getStatusLabel() {
    return statusLabel;
  }

  public void setStatusLabel(String statusLabel) {
    this.statusLabel = statusLabel;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public String getInventoryText() {
    return inventoryText;
  }

  public void setInventoryText(String inventoryText) {
    this.inventoryText = inventoryText;
  }
}

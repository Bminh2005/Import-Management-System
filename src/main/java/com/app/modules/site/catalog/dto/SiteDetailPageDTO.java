package com.app.modules.site.catalog.dto;

import java.util.ArrayList;
import java.util.List;

/** Dữ liệu hiển thị màn Chi tiết Site. */
public class SiteDetailPageDTO {
  private String siteId;
  private String pageTitle;
  private String pageSubtitle = "Thông tin chi tiết và danh sách mặt hàng có sẵn";
  private String statusLabel;
  private String statusStyleClass;
  private String shippingCostText;
  private String deliveryText;
  private String ratingText;
  private String totalOrdersText;
  private String availableItemsText;
  private String inventoryValueText;
  private String totalItemsSummary;
  private final List<SiteInfoRowDTO> infoRows = new ArrayList<>();
  private final List<SiteCategorySectionDTO> categorySections = new ArrayList<>();

  public String getSiteId() {
    return siteId;
  }

  public void setSiteId(String siteId) {
    this.siteId = siteId;
  }

  public String getPageTitle() {
    return pageTitle;
  }

  public void setPageTitle(String pageTitle) {
    this.pageTitle = pageTitle;
  }

  public String getPageSubtitle() {
    return pageSubtitle;
  }

  public void setPageSubtitle(String pageSubtitle) {
    this.pageSubtitle = pageSubtitle;
  }

  public String getStatusLabel() {
    return statusLabel;
  }

  public void setStatusLabel(String statusLabel) {
    this.statusLabel = statusLabel;
  }

  public String getStatusStyleClass() {
    return statusStyleClass;
  }

  public void setStatusStyleClass(String statusStyleClass) {
    this.statusStyleClass = statusStyleClass;
  }

  public String getShippingCostText() {
    return shippingCostText;
  }

  public void setShippingCostText(String shippingCostText) {
    this.shippingCostText = shippingCostText;
  }

  public String getDeliveryText() {
    return deliveryText;
  }

  public void setDeliveryText(String deliveryText) {
    this.deliveryText = deliveryText;
  }

  public String getRatingText() {
    return ratingText;
  }

  public void setRatingText(String ratingText) {
    this.ratingText = ratingText;
  }

  public String getTotalOrdersText() {
    return totalOrdersText;
  }

  public void setTotalOrdersText(String totalOrdersText) {
    this.totalOrdersText = totalOrdersText;
  }

  public String getAvailableItemsText() {
    return availableItemsText;
  }

  public void setAvailableItemsText(String availableItemsText) {
    this.availableItemsText = availableItemsText;
  }

  public String getInventoryValueText() {
    return inventoryValueText;
  }

  public void setInventoryValueText(String inventoryValueText) {
    this.inventoryValueText = inventoryValueText;
  }

  public String getTotalItemsSummary() {
    return totalItemsSummary;
  }

  public void setTotalItemsSummary(String totalItemsSummary) {
    this.totalItemsSummary = totalItemsSummary;
  }

  public List<SiteInfoRowDTO> getInfoRows() {
    return infoRows;
  }

  public List<SiteCategorySectionDTO> getCategorySections() {
    return categorySections;
  }
}

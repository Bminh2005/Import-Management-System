package com.app.modules.site.catalog.dto;

/** Một dòng trong bảng mặt hàng theo danh mục. */
public class SiteItemRowDTO {
  private String code;
  private String name;
  private String quantityText;
  private String unitPriceText;
  private String lastUpdated;

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

  public String getQuantityText() {
    return quantityText;
  }

  public void setQuantityText(String quantityText) {
    this.quantityText = quantityText;
  }

  public String getUnitPriceText() {
    return unitPriceText;
  }

  public void setUnitPriceText(String unitPriceText) {
    this.unitPriceText = unitPriceText;
  }

  public String getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(String lastUpdated) {
    this.lastUpdated = lastUpdated;
  }
}

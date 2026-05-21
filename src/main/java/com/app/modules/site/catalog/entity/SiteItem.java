package com.app.modules.site.catalog.entity;

public class SiteItem {
  private final String code;
  private final String name;
  private final String category;
  private final int availableQuantity;
  private final String unit;
  private final long unitPrice;
  private final String lastUpdated;

  public SiteItem(
      String code,
      String name,
      String category,
      int availableQuantity,
      String unit,
      long unitPrice,
      String lastUpdated) {
    this.code = code;
    this.name = name;
    this.category = category;
    this.availableQuantity = availableQuantity;
    this.unit = unit;
    this.unitPrice = unitPrice;
    this.lastUpdated = lastUpdated;
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public String getCategory() {
    return category;
  }

  public int getAvailableQuantity() {
    return availableQuantity;
  }

  public String getUnit() {
    return unit;
  }

  public long getUnitPrice() {
    return unitPrice;
  }

  public String getLastUpdated() {
    return lastUpdated;
  }
}

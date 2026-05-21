package com.app.modules.site.catalog.dto;

import java.util.ArrayList;
import java.util.List;

/** Nhóm mặt hàng theo danh mục trên Chi tiết Site. */
public class SiteCategorySectionDTO {
  private String categoryName;
  private String itemCountText;
  private final List<SiteItemRowDTO> items = new ArrayList<>();

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public String getItemCountText() {
    return itemCountText;
  }

  public void setItemCountText(String itemCountText) {
    this.itemCountText = itemCountText;
  }

  public List<SiteItemRowDTO> getItems() {
    return items;
  }
}

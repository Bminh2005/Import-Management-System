package com.app.modules.procurement.product.dto;

import java.util.ArrayList;
import java.util.List;

/** Dữ liệu form Thêm mặt hàng. */
public class AddMerchandiseDTO {
  private String dialogTitle = "Thêm Mặt hàng Mới";
  private String codeHint = "Tự động tạo (VD: MH005)";
  private String name = "";
  private String unit = "";
  private String category = "";
  private String price = "";
  private String supplier = "";
  private String description = "";
  private final List<String> categoryOptions = new ArrayList<>();

  public String getDialogTitle() {
    return dialogTitle;
  }

  public void setDialogTitle(String dialogTitle) {
    this.dialogTitle = dialogTitle;
  }

  public String getCodeHint() {
    return codeHint;
  }

  public void setCodeHint(String codeHint) {
    this.codeHint = codeHint;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public String getSupplier() {
    return supplier;
  }

  public void setSupplier(String supplier) {
    this.supplier = supplier;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<String> getCategoryOptions() {
    return categoryOptions;
  }

  public void setCategoryOptions(List<String> options) {
    categoryOptions.clear();
    categoryOptions.addAll(options);
  }

  public void clearForm() {
    name = "";
    unit = "";
    category = "";
    price = "";
    supplier = "";
    description = "";
  }
}

package com.app.common.dto;

/** Một chip lọc (danh mục, trạng thái, …). */
public class FilterChipDTO {
  private String value;
  private String label;
  private String count;
  private boolean selected;

  public FilterChipDTO(String value, String label, String count, boolean selected) {
    this.value = value;
    this.label = label;
    this.count = count;
    this.selected = selected;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getCount() {
    return count;
  }

  public void setCount(String count) {
    this.count = count;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public String getDisplayText() {
    return label + " (" + count + ")";
  }
}

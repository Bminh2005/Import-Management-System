package com.app.modules.site.catalog.dto;

/** Một dòng thông tin trên màn Chi tiết Site. */
public class SiteInfoRowDTO {
  private String icon;
  private String label;
  private String primaryText;
  private String secondaryText;
  private boolean linkStyle;
  private boolean showSeparatorAfter;

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getPrimaryText() {
    return primaryText;
  }

  public void setPrimaryText(String primaryText) {
    this.primaryText = primaryText;
  }

  public String getSecondaryText() {
    return secondaryText;
  }

  public void setSecondaryText(String secondaryText) {
    this.secondaryText = secondaryText;
  }

  public boolean isLinkStyle() {
    return linkStyle;
  }

  public void setLinkStyle(boolean linkStyle) {
    this.linkStyle = linkStyle;
  }

  public boolean isShowSeparatorAfter() {
    return showSeparatorAfter;
  }

  public void setShowSeparatorAfter(boolean showSeparatorAfter) {
    this.showSeparatorAfter = showSeparatorAfter;
  }
}

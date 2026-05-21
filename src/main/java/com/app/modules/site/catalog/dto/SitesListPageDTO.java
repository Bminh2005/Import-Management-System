package com.app.modules.site.catalog.dto;

/** Dữ liệu hiển thị màn Danh sách Site. */
public class SitesListPageDTO {
  private String pageTitle = "Danh sách Site";
  private String pageSubtitle = "Quản lý danh sách các site cung cấp hàng hóa";
  private String searchPrompt = "Tìm kiếm theo mã site, tên, địa điểm...";
  private String searchQuery = "";
  private boolean emptyVisible;

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

  public String getSearchPrompt() {
    return searchPrompt;
  }

  public void setSearchPrompt(String searchPrompt) {
    this.searchPrompt = searchPrompt;
  }

  public String getSearchQuery() {
    return searchQuery;
  }

  public void setSearchQuery(String searchQuery) {
    this.searchQuery = searchQuery;
  }

  public boolean isEmptyVisible() {
    return emptyVisible;
  }

  public void setEmptyVisible(boolean emptyVisible) {
    this.emptyVisible = emptyVisible;
  }
}

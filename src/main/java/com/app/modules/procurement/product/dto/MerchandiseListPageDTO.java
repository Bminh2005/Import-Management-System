package com.app.modules.procurement.product.dto;

/** Dữ liệu hiển thị màn Quản lý Mặt hàng. */
public class MerchandiseListPageDTO {
  private String pageTitle = "Quản lý Mặt hàng";
  private String pageSubtitle = "Danh sách các mặt hàng trong hệ thống";
  private String searchPrompt = "Tìm kiếm theo mã hàng hoặc tên...";
  private String searchQuery = "";
  private String paginationText = "Hiển thị 1-4 trong 24 mặt hàng";
  private String addButtonText = "+  Thêm Mặt hàng Mới";

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

  public String getPaginationText() {
    return paginationText;
  }

  public void setPaginationText(String paginationText) {
    this.paginationText = paginationText;
  }

  public String getAddButtonText() {
    return addButtonText;
  }

  public void setAddButtonText(String addButtonText) {
    this.addButtonText = addButtonText;
  }
}

package com.app.common.ui.navigation;

/** Điều hướng giữa các màn hình chính. */
public interface NavigationHandler {
  void showMerchandiseList();

  void showSitesList();

  void showSiteDetail(String siteId);
}

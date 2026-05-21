package com.app.Ioms;

import javafx.application.Application;

/**
 * Launcher cho {@link MainApp} — màn chính gồm Quản lý Mặt hàng và Danh sách Site.
 *
 * Cách chạy:
 * - IDE: right-click lớp này → Run.
 * - Terminal: mvn javafx:run
 */
public class MainLauncher {
  public static void main(String[] args) {
    Application.launch(MainApp.class, args);
  }
}

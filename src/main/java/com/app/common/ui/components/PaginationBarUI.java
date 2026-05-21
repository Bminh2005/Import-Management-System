package com.app.common.ui.components;

import com.app.common.util.ActionLog;
import com.app.common.util.FxmlUiHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/** Thanh phân trang tái sử dụng. */
public class PaginationBarUI extends HBox {

  @FXML private Label summaryLabel;
  @FXML private Button prevButton;
  @FXML private Button page1Button;
  @FXML private Button page2Button;
  @FXML private Button nextButton;

  public PaginationBarUI() {
    FxmlUiHelper.loadSelf(this, "PaginationBar.fxml");
    prevButton.setOnAction(e -> onPrev());
    page1Button.setOnAction(e -> onPage(1));
    page2Button.setOnAction(e -> onPage(2));
    nextButton.setOnAction(e -> onNext());
  }

  public void setSummaryText(String text) {
    summaryLabel.setText(text);
  }

  private void onPrev() {
    ActionLog.stub("Phân trang: Trang trước");
  }

  private void onNext() {
    ActionLog.stub("Phân trang: Trang sau");
  }

  private void onPage(int page) {
    ActionLog.stub("Phân trang: Chuyển tới trang " + page);
  }
}

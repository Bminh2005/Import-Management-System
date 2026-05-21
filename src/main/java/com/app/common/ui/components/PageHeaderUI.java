package com.app.common.ui.components;

import com.app.common.util.FxmlUiHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/** Tiêu đề trang + nút hành động tùy chọn. */
public class PageHeaderUI extends HBox {

  @FXML private Label titleLabel;
  @FXML private Label subtitleLabel;
  @FXML private Button actionButton;
  @FXML private HBox actionContainer;

  public PageHeaderUI() {
    FxmlUiHelper.loadSelf(this, "PageHeader.fxml");
    hideAction();
  }

  public void setTitle(String title) {
    titleLabel.setText(title);
  }

  public void setSubtitle(String subtitle) {
    subtitleLabel.setText(subtitle);
  }

  public void setActionButton(String text, Runnable onAction) {
    actionButton.setText(text);
    actionButton.setVisible(true);
    actionButton.setManaged(true);
    actionContainer.setVisible(true);
    actionContainer.setManaged(true);
    actionButton.setOnAction(e -> onAction.run());
  }

  public void hideAction() {
    actionButton.setVisible(false);
    actionButton.setManaged(false);
    actionContainer.setVisible(false);
    actionContainer.setManaged(false);
  }
}

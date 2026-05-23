package com.app.modules.site.catalog.ui.components;

import com.app.common.util.FxmlUiHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;

/** Một dòng thông tin Site (địa chỉ, liên hệ, …). */
public class SiteInfoRowUI extends VBox {

  @FXML private Label iconLabel;
  @FXML private Label fieldLabel;
  @FXML private Label primaryLabel;
  @FXML private Label secondaryLabel;
  @FXML private Separator rowSeparator;

  public SiteInfoRowUI() {
    FxmlUiHelper.loadSelf(this, "SiteInfoRow.fxml");
    FxmlUiHelper.addStylesheet(this, "site-info-row.css");
  }

  public void bind(
      String icon,
      String label,
      String primary,
      String secondary,
      boolean linkStyle,
      boolean showSeparator) {
    iconLabel.setText(icon);
    fieldLabel.setText(label);
    primaryLabel.setText(primary);
    primaryLabel.getStyleClass().removeAll("info-value", "link-text");
    primaryLabel.getStyleClass().add(linkStyle ? "link-text" : "info-value");

    if (secondary != null && !secondary.isBlank()) {
      secondaryLabel.setText(secondary);
      secondaryLabel.setVisible(true);
      secondaryLabel.setManaged(true);
    } else {
      secondaryLabel.setVisible(false);
      secondaryLabel.setManaged(false);
    }

    rowSeparator.setVisible(showSeparator);
    rowSeparator.setManaged(showSeparator);
  }
}

package com.app.modules.site.catalog.ui.components;

import com.app.common.util.FxmlUiHelper;
import com.app.modules.site.catalog.dto.SiteInfoRowDTO;
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

  public void bind(SiteInfoRowDTO vm) {
    iconLabel.setText(vm.getIcon());
    fieldLabel.setText(vm.getLabel());
    primaryLabel.setText(vm.getPrimaryText());
    primaryLabel.getStyleClass().removeAll("info-value", "link-text");
    primaryLabel.getStyleClass().add(vm.isLinkStyle() ? "link-text" : "info-value");

    if (vm.getSecondaryText() != null && !vm.getSecondaryText().isBlank()) {
      secondaryLabel.setText(vm.getSecondaryText());
      secondaryLabel.setVisible(true);
      secondaryLabel.setManaged(true);
    } else {
      secondaryLabel.setVisible(false);
      secondaryLabel.setManaged(false);
    }

    rowSeparator.setVisible(vm.isShowSeparatorAfter());
    rowSeparator.setManaged(vm.isShowSeparatorAfter());
  }
}

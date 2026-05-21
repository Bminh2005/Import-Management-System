package com.app.modules.site.catalog.ui.components;

import com.app.common.util.ActionLog;
import com.app.common.util.FxmlUiHelper;
import com.app.modules.site.catalog.dto.SiteCardDTO;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/** Card Site trên lưới danh sách. */
public class SiteCardUI extends VBox {

  @FXML private Label codeLabel;
  @FXML private Label nameLabel;
  @FXML private Label statusLabel;
  @FXML private Label warehouseLabel;
  @FXML private Label locationLabel;
  @FXML private Label inventoryLabel;
  @FXML private Button viewDetailButton;

  private String siteId;
  private Consumer<String> onViewDetail;

  public SiteCardUI() {
    FxmlUiHelper.loadSelf(this, "SiteCard.fxml");
    FxmlUiHelper.addStylesheet(this, "site-card.css");
    viewDetailButton.setOnAction(e -> onViewDetailClick());
  }

  public void bind(SiteCardDTO vm) {
    siteId = vm.getSiteId();
    codeLabel.setText(vm.getCode());
    nameLabel.setText(vm.getName());
    warehouseLabel.setText(vm.getWarehouse());
    locationLabel.setText(vm.getLocation());
    inventoryLabel.setText(vm.getInventoryText());
    statusLabel.setText(vm.getStatusLabel());
    statusLabel.getStyleClass().removeAll("badge-site-active", "badge-site-inactive");
    statusLabel
        .getStyleClass()
        .add(vm.isActive() ? "badge-site-active" : "badge-site-inactive");
  }

  public void setOnViewDetail(Consumer<String> onViewDetail) {
    this.onViewDetail = onViewDetail;
  }

  private void onViewDetailClick() {
    ActionLog.stub("Danh sách Site: Xem chi tiết site " + siteId);
    if (onViewDetail != null) {
      onViewDetail.accept(siteId);
    }
  }
}

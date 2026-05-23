package com.app.modules.site.catalog.ui.components;

import com.app.common.util.FxmlUiHelper;
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

  public void bind(
      String siteId,
      String code,
      String name,
      String warehouse,
      String location,
      String inventoryText,
      boolean active,
      String statusLabelText) {
    this.siteId = siteId;
    codeLabel.setText(code);
    nameLabel.setText(name);
    warehouseLabel.setText(warehouse);
    locationLabel.setText(location);
    inventoryLabel.setText(inventoryText);
    statusLabel.setText(statusLabelText);
    statusLabel.getStyleClass().removeAll("badge-site-active", "badge-site-inactive");
    statusLabel.getStyleClass().add(active ? "badge-site-active" : "badge-site-inactive");
  }

  public void setOnViewDetail(Consumer<String> onViewDetail) {
    this.onViewDetail = onViewDetail;
  }

  private void onViewDetailClick() {
    if (onViewDetail != null) {
      onViewDetail.accept(siteId);
    }
  }
}

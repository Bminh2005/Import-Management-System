package com.app.modules.procurement.order.ui;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.app.modules.procurement.order.model.RequestDetailItem;
import com.app.modules.procurement.order.model.SiteAllocationEntry;
import com.app.modules.procurement.order.model.SiteInventoryInfo;
import com.app.modules.procurement.order.service.SiteOrderService;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SiteAllocationDialogController implements Initializable {

    @FXML private Label lblDialogTitle;
    @FXML private Label lblMerchandiseName;
    @FXML private Label lblRequired;
    @FXML private Label lblAllocated;
    @FXML private Label lblRemaining;
    @FXML private ProgressBar progressBar;
    @FXML private Label lblSiteCount;
    @FXML private VBox siteCardsContainer;
    @FXML private Button btnCancel;
    @FXML private Button btnSave;

    private RequestDetailItem currentItem;
    private LocalDate desiredDate;
    private List<SiteInventoryInfo> availableSites = new ArrayList<>();
    private final Map<Long, TextField> quantityFields = new HashMap<>();
    private Consumer<List<SiteAllocationEntry>> onSave;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Objects.requireNonNull(lblDialogTitle);
        Objects.requireNonNull(lblMerchandiseName);
        Objects.requireNonNull(lblRequired);
        Objects.requireNonNull(lblAllocated);
        Objects.requireNonNull(lblRemaining);
        Objects.requireNonNull(progressBar);
        Objects.requireNonNull(lblSiteCount);
        Objects.requireNonNull(siteCardsContainer);
        Objects.requireNonNull(btnCancel);
        Objects.requireNonNull(btnSave);
    }

    public void setRequestData(RequestDetailItem item,
                               LocalDate desiredDate,
                               List<SiteInventoryInfo> sites,
                               List<SiteAllocationEntry> existingAllocations,
                               Consumer<List<SiteAllocationEntry>> onSave) {
        this.currentItem = Objects.requireNonNull(item);
        this.desiredDate = desiredDate;
        this.availableSites = List.copyOf(sites);
        this.onSave = Objects.requireNonNull(onSave);

        lblDialogTitle.setText("Phân bổ Site cho MH" + item.getMerchandiseDetailId());
        lblMerchandiseName.setText(item.getMerchandiseName());

        Map<Long, Long> prefill = new HashMap<>();
        if (existingAllocations != null) {
            for (SiteAllocationEntry allocation : existingAllocations) {
                prefill.put(allocation.getSiteId(), allocation.getQuantity());
            }
        }
        renderSiteCards(prefill);
        updateSummary();
    }

    private void renderSiteCards(Map<Long, Long> prefill) {
        siteCardsContainer.getChildren().clear();
        quantityFields.clear();
        lblSiteCount.setText("Tìm thấy " + availableSites.size() + " Site có sẵn");

        if (availableSites.isEmpty()) {
            Label empty = new Label("Không có Site nào đủ tồn kho và giao kịp ngày "
                    + SiteOrderService.formatDate(desiredDate) + ".");
            empty.getStyleClass().add("empty-site-msg");
            empty.setWrapText(true);
            siteCardsContainer.getChildren().add(empty);
            return;
        }

        for (SiteInventoryInfo site : availableSites) {
            siteCardsContainer.getChildren().add(buildSiteCard(site, prefill));
        }
    }

    private VBox buildSiteCard(SiteInventoryInfo site, Map<Long, Long> prefill) {
        Label scoreBadge = new Label("Điểm: " + site.getScore() + "/100");
        scoreBadge.getStyleClass().add("score-badge");

        Label name = new Label(site.getSiteName());
        name.getStyleClass().add("site-name");

        Label location = new Label("📍 " + nullToDash(site.getSiteAddress()));
        location.getStyleClass().add("info-label");

        Label stock = new Label("TỒN KHO: " + site.getAvailableQuantity() + " " + currentItem.getUnit());
        stock.getStyleClass().add("info-label");

        Label price = new Label("ĐƠN GIÁ: " + formatMoney(site.getPrice()));
        price.getStyleClass().add("info-label");

        Label ship = new Label("🚢 Tàu · Giao dự kiến: "
                + SiteOrderService.formatDate(site.getEstimatedDelivery()));
        ship.getStyleClass().add("info-label-highlight");

        TextField quantityField = new TextField();
        quantityField.setPromptText("0");
        quantityField.getStyleClass().add("qty-input");
        long existingValue = prefill.getOrDefault(site.getSiteId(), 0L);
        if (existingValue > 0) {
            quantityField.setText(String.valueOf(existingValue));
        }
        quantityField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                quantityField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            updateSummary();
        });

        Label qtyLabel = new Label("Số lượng phân bổ:");
        qtyLabel.getStyleClass().add("qty-label");
        Label maxLabel = new Label("/ " + site.getAvailableQuantity());
        maxLabel.getStyleClass().add("qty-max");

        HBox qtyRow = new HBox(8, qtyLabel, quantityField, maxLabel);
        qtyRow.setAlignment(Pos.CENTER_LEFT);

        HBox header = new HBox(12, name, new VBox(), scoreBadge);
        header.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(header.getChildren().get(1), Priority.ALWAYS);

        VBox card = new VBox(10, header, location, stock, price, ship, qtyRow);
        card.getStyleClass().add("site-card");
        card.setPadding(new Insets(16));
        quantityFields.put(site.getSiteId(), quantityField);
        return card;
    }

    private void updateSummary() {
        long required = currentItem.getRequiredQuantity();
        long allocated = quantityFields.values().stream()
                .mapToLong(field -> parseLong(field.getText()))
                .sum();
        long remaining = required - allocated;
        lblRequired.setText(required + " " + currentItem.getUnit());
        lblAllocated.setText(allocated + " " + currentItem.getUnit());
        lblRemaining.setText(Math.max(0, remaining) + " " + currentItem.getUnit());
        double progress = required == 0 ? 0 : Math.min(1.0, (double) allocated / required);
        progressBar.setProgress(progress);
    }

    @FXML
    private void handleClose() {
        closeWindow();
    }

    @FXML
    private void handleSave() {
        long required = currentItem.getRequiredQuantity();
        long allocated = quantityFields.values().stream()
                .mapToLong(field -> parseLong(field.getText()))
                .sum();

        if (allocated <= 0) {
            SiteOrderUiAlerts.warn("Chưa chọn Site",
                    "Vui lòng nhập số lượng phân bổ cho ít nhất một Site.");
            return;
        }
        if (allocated > required) {
            SiteOrderUiAlerts.warn("Phân bổ thừa",
                    "Tổng số lượng phân bổ (" + allocated + ") vượt quá yêu cầu (" + required + ").");
            return;
        }
        if (allocated < required) {
            SiteOrderUiAlerts.warn("Phân bổ chưa đủ",
                    "Bạn mới phân bổ " + allocated + "/" + required + ". "
                            + "Vui lòng phân bổ đủ số lượng trước khi lưu.");
            return;
        }

        List<SiteAllocationEntry> allocations = new ArrayList<>();
        for (SiteInventoryInfo site : availableSites) {
            long quantity = parseLong(quantityFields.get(site.getSiteId()).getText());
            if (quantity <= 0) {
                continue;
            }
            if (quantity > site.getAvailableQuantity()) {
                SiteOrderUiAlerts.warn("Vượt tồn kho",
                        "Site " + site.getSiteName() + " chỉ còn " + site.getAvailableQuantity() + ".");
                return;
            }
            SiteAllocationEntry entry = new SiteAllocationEntry();
            entry.setSiteId(site.getSiteId());
            entry.setSiteName(site.getSiteName());
            entry.setMerchandiseDetailId(currentItem.getMerchandiseDetailId());
            entry.setMerchandiseName(currentItem.getMerchandiseName());
            entry.setUnit(currentItem.getUnit());
            entry.setQuantity(quantity);
            entry.setPrice(site.getPrice());
            entry.setAvailableQuantity(site.getAvailableQuantity());
            allocations.add(entry);
        }

        onSave.accept(allocations);
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    private long parseLong(String value) {
        if (value == null || value.isBlank()) {
            return 0L;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            return 0L;
        }
    }

    private String formatMoney(double value) {
        return String.format("%,.0f đ", value).replace(',', '.');
    }

    private static String nullToDash(String value) {
        return value == null || value.isBlank() ? "—" : value;
    }
}

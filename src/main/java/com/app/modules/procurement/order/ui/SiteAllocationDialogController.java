package com.app.modules.procurement.order.ui;

import java.net.URL;
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

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    @FXML private VBox siteCardsContainer;
    @FXML private Button btnCancel;
    @FXML private Button btnSave;

    private RequestDetailItem currentItem;
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
        Objects.requireNonNull(siteCardsContainer);
        Objects.requireNonNull(btnCancel);
        Objects.requireNonNull(btnSave);
    }

    public void setRequestData(RequestDetailItem item,
                               List<SiteInventoryInfo> sites,
                               List<SiteAllocationEntry> existingAllocations,
                               Consumer<List<SiteAllocationEntry>> onSave) {
        this.currentItem = Objects.requireNonNull(item);
        this.availableSites = List.copyOf(sites);
        this.onSave = Objects.requireNonNull(onSave);

        lblDialogTitle.setText("Phân bổ Site cho " + item.getMerchandiseName());
        lblMerchandiseName.setText(item.getMerchandiseName());
        lblRequired.setText("Số lượng yêu cầu: " + item.getRequiredQuantity() + " " + item.getUnit());

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
        for (SiteInventoryInfo site : availableSites) {
            Label name = new Label(site.getSiteName());
            name.getStyleClass().add("section-title");
            Label detail = new Label("Còn: " + site.getAvailableQuantity() + " " + currentItem.getUnit()
                    + " · Giá: " + formatMoney(site.getPrice()));
            detail.getStyleClass().add("info-label");
            TextField quantityField = new TextField();
            quantityField.setPromptText("0");
            quantityField.setPrefWidth(88);
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

            HBox row = new HBox(12, new VBox(4, name, detail), quantityField);
            row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            HBox.setHgrow(row.getChildren().get(0), Priority.ALWAYS);
            row.getStyleClass().add("card");
            row.setPadding(new javafx.geometry.Insets(14));
            siteCardsContainer.getChildren().add(row);
            quantityFields.put(site.getSiteId(), quantityField);
        }
    }

    private void updateSummary() {
        long allocated = quantityFields.entrySet().stream()
                .mapToLong(entry -> parseLong(entry.getValue().getText()))
                .sum();
        long remaining = currentItem.getRequiredQuantity() - allocated;
        lblAllocated.setText("Đã phân bổ: " + allocated);
        lblRemaining.setText("Còn lại: " + Math.max(0, remaining));
    }

    @FXML
    private void handleClose() {
        closeWindow();
    }

    @FXML
    private void handleSave() {
        long allocated = quantityFields.entrySet().stream()
                .mapToLong(entry -> parseLong(entry.getValue().getText()))
                .sum();

        if (allocated <= 0) {
            System.out.println("Vui lòng nhập số lượng phân bổ lớn hơn 0.");
            return;
        }
        if (allocated > currentItem.getRequiredQuantity()) {
            System.out.println("Số lượng phân bổ không thể lớn hơn yêu cầu.");
            return;
        }

        List<SiteAllocationEntry> allocations = new ArrayList<>();
        for (SiteInventoryInfo site : availableSites) {
            long quantity = parseLong(quantityFields.get(site.getSiteId()).getText());
            if (quantity <= 0) {
                continue;
            }
            if (quantity > site.getAvailableQuantity()) {
                System.out.println("Số lượng phân bổ cho site " + site.getSiteName() + " vượt quá tồn kho.");
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
}

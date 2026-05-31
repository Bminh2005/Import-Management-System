package com.app.modules.procurement.order.ui;

import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import com.app.modules.procurement.order.model.ImportRequestInfo;
import com.app.modules.procurement.order.model.SiteAllocationEntry;
import com.app.modules.procurement.order.service.SiteOrderService;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SiteOrderConfirmDialogController implements Initializable {

    @FXML private Label lblDialogTitle;
    @FXML private Label lblDialogSubtitle;
    @FXML private Label lblStatLabel1;
    @FXML private Label lblStatValue1;
    @FXML private Label lblOrderCount;
    @FXML private Label lblTotalValue;
    @FXML private VBox orderCardsContainer;
    @FXML private Button btnBack;
    @FXML private Button btnConfirm;

    private final SiteOrderService service = new SiteOrderService();
    private ImportRequestInfo requestInfo;
    private Map<Long, List<SiteAllocationEntry>> allocationsBySite = new LinkedHashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Objects.requireNonNull(lblDialogTitle);
        Objects.requireNonNull(lblDialogSubtitle);
        Objects.requireNonNull(lblStatLabel1);
        Objects.requireNonNull(lblStatValue1);
        Objects.requireNonNull(lblOrderCount);
        Objects.requireNonNull(lblTotalValue);
        Objects.requireNonNull(orderCardsContainer);
        Objects.requireNonNull(btnBack);
        Objects.requireNonNull(btnConfirm);
    }

    public void setData(ImportRequestInfo requestInfo,
                        Map<Long, List<SiteAllocationEntry>> allocationsByItem) {
        this.requestInfo = Objects.requireNonNull(requestInfo);
        this.allocationsBySite = groupAllocationsBySite(allocationsByItem);
        renderPreview();
    }

    private Map<Long, List<SiteAllocationEntry>> groupAllocationsBySite(
            Map<Long, List<SiteAllocationEntry>> allocationsByItem) {
        Map<Long, List<SiteAllocationEntry>> siteMap = new LinkedHashMap<>();
        for (List<SiteAllocationEntry> allocations : allocationsByItem.values()) {
            for (SiteAllocationEntry entry : allocations) {
                siteMap.computeIfAbsent(entry.getSiteId(), key -> new ArrayList<>())
                        .add(entry);
            }
        }
        return siteMap;
    }

    private void renderPreview() {
        int orderCount = allocationsBySite.size();
        double totalValue = allocationsBySite.values().stream()
                .flatMap(List::stream)
                .mapToDouble(entry -> entry.getQuantity() * entry.getPrice())
                .sum();

        lblStatValue1.setText("REQ-" + requestInfo.getRequestId());
        lblOrderCount.setText(orderCount + " đơn");
        lblTotalValue.setText(formatMoney(totalValue));
        btnConfirm.setText("✓ Xác nhận & Tạo " + orderCount + " đơn mới");

        orderCardsContainer.getChildren().clear();
        for (List<SiteAllocationEntry> siteAllocations : allocationsBySite.values()) {
            orderCardsContainer.getChildren().add(buildSiteCard(siteAllocations));
        }
    }

    private VBox buildSiteCard(List<SiteAllocationEntry> siteAllocations) {
        String siteName = siteAllocations.get(0).getSiteName();
        long siteQuantity = siteAllocations.stream().mapToLong(SiteAllocationEntry::getQuantity).sum();
        double siteAmount = siteAllocations.stream().mapToDouble(entry -> entry.getQuantity() * entry.getPrice()).sum();

        Label title = new Label(siteName);
        title.getStyleClass().add("section-title");
        Label summary = new Label(siteQuantity + " mặt hàng · " + formatMoney(siteAmount));
        summary.getStyleClass().add("info-label");

        VBox itemRows = new VBox(6);
        for (SiteAllocationEntry entry : siteAllocations) {
            Label row = new Label(entry.getMerchandiseName() + " — "
                    + entry.getQuantity() + " " + entry.getUnit()
                    + " x " + formatMoney(entry.getPrice()));
            row.getStyleClass().add("info-label");
            itemRows.getChildren().add(row);
        }

        VBox card = new VBox(10, title, summary, itemRows);
        card.getStyleClass().addAll("card", "order-preview-card");
        card.setPadding(new javafx.geometry.Insets(16));
        return card;
    }

    @FXML
    private void handleBack() {
        closeWindow();
    }

    @FXML
    private void handleConfirm() {
        LocalDate expectedDate = parseDesiredDate(requestInfo.getDesiredDate());
        int created = service.createOrdersFromAllocation(
                requestInfo.getRequestId(),
                requestInfo.getUserId(),
                expectedDate,
                allocationsBySite);
        System.out.println("Đã tạo " + created + " đơn hàng mới cho yêu cầu " + requestInfo.getRequestId());
        closeWindow();
    }

    @FXML
    private void handleClose() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();
    }

    private LocalDate parseDesiredDate(String text) {
        try {
            return LocalDate.parse(text);
        } catch (DateTimeParseException ignored) {
            return LocalDate.now().plusDays(5);
        }
    }

    private String formatMoney(double value) {
        return NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN")).format(value) + " đ";
    }
}

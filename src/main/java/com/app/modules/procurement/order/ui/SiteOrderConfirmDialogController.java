package com.app.modules.procurement.order.ui;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import com.app.modules.procurement.order.model.ImportRequestInfo;
import com.app.modules.procurement.order.model.ReallocationResult;
import com.app.modules.procurement.order.model.SiteAllocationEntry;
import com.app.modules.procurement.order.model.SiteOrder;
import com.app.modules.procurement.order.service.SiteOrderService;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SiteOrderConfirmDialogController implements Initializable {

    @FXML private Label lblDialogTitle;
    @FXML private Label lblDialogSubtitle;
    @FXML private Label lblStatValue1;
    @FXML private Label lblOrderCount;
    @FXML private Label lblTotalValue;
    @FXML private Label lblSiteExplain;
    @FXML private VBox orderCardsContainer;
    @FXML private Button btnBack;
    @FXML private Button btnConfirm;

    private final SiteOrderService service = SiteOrderNavigator.service();
    private SiteOrder sourceOrder;
    private ImportRequestInfo requestInfo;
    private Scene ownerScene;
    private Map<Long, List<SiteAllocationEntry>> allocationsBySite = new LinkedHashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Objects.requireNonNull(lblDialogTitle);
        Objects.requireNonNull(lblDialogSubtitle);
        Objects.requireNonNull(lblStatValue1);
        Objects.requireNonNull(lblOrderCount);
        Objects.requireNonNull(lblTotalValue);
        Objects.requireNonNull(lblSiteExplain);
        Objects.requireNonNull(orderCardsContainer);
        Objects.requireNonNull(btnBack);
        Objects.requireNonNull(btnConfirm);
    }

    public void setData(SiteOrder sourceOrder,
                        ImportRequestInfo requestInfo,
                        Map<Long, List<SiteAllocationEntry>> allocationsByItem,
                        Scene ownerScene) {
        this.sourceOrder = Objects.requireNonNull(sourceOrder);
        this.requestInfo = Objects.requireNonNull(requestInfo);
        this.ownerScene = ownerScene;
        this.allocationsBySite = groupAllocationsBySite(allocationsByItem);
        renderPreview();
    }

    private Map<Long, List<SiteAllocationEntry>> groupAllocationsBySite(
            Map<Long, List<SiteAllocationEntry>> allocationsByItem) {
        Map<Long, List<SiteAllocationEntry>> siteMap = new LinkedHashMap<>();
        for (List<SiteAllocationEntry> allocations : allocationsByItem.values()) {
            for (SiteAllocationEntry entry : allocations) {
                siteMap.computeIfAbsent(entry.getSiteId(), key -> new ArrayList<>()).add(entry);
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

        lblStatValue1.setText(SiteOrderService.formatOrderCode(sourceOrder.getId()));
        lblOrderCount.setText(orderCount + " đơn");
        lblTotalValue.setText(formatMoney(totalValue));
        lblSiteExplain.setText("Mỗi Site sẽ nhận 1 đơn mới ở trạng thái Chưa xử lý. "
                + "Đơn bị hủy sẽ được thay thế và xóa khỏi danh sách đơn đặt hàng.");
        btnConfirm.setText("Xác nhận và tạo " + orderCount + " đơn mới");

        orderCardsContainer.getChildren().clear();
        int idx = 1;
        for (List<SiteAllocationEntry> siteAllocations : allocationsBySite.values()) {
            orderCardsContainer.getChildren().add(buildSiteCard(idx++, siteAllocations));
        }
    }

    private VBox buildSiteCard(int index, List<SiteAllocationEntry> siteAllocations) {
        String siteName = siteAllocations.get(0).getSiteName();
        double siteAmount = siteAllocations.stream()
                .mapToDouble(entry -> entry.getQuantity() * entry.getPrice()).sum();

        Label title = new Label("Đơn #" + index + " -> " + siteName);
        title.getStyleClass().add("preview-order-title");
        Label summary = new Label(formatMoney(siteAmount));
        summary.getStyleClass().add("preview-order-total");

        VBox itemRows = new VBox(6);
        for (SiteAllocationEntry entry : siteAllocations) {
            Label row = new Label(entry.getMerchandiseName() + " - "
                    + entry.getQuantity() + " " + entry.getUnit()
                    + " x " + formatMoney(entry.getPrice()));
            row.getStyleClass().add("info-label");
            row.setWrapText(true);
            itemRows.getChildren().add(row);
        }

        VBox card = new VBox(8, title, summary, itemRows);
        card.getStyleClass().addAll("card", "order-preview-card");
        card.setPadding(new Insets(14));
        return card;
    }

    @FXML
    private void handleBack() {
        closeWindow();
    }

    @FXML
    private void handleConfirm() {
        btnConfirm.setDisable(true);
        try {
            ReallocationResult result = service.finalizeReallocation(
                    sourceOrder, requestInfo, allocationsBySite);
            closeWindow();
            SiteOrderUiAlerts.info("Tạo đơn thành công",
                    "Đã tạo " + result.getCount()
                            + " đơn đặt hàng mới ở trạng thái Chưa xử lý và xóa đơn bị hủy "
                            + SiteOrderService.formatOrderCode(sourceOrder.getId()) + ".");
            if (ownerScene != null) {
                SiteOrderNavigator.showSuccess(ownerScene, result, sourceOrder.getId());
            }
        } catch (RuntimeException exception) {
            btnConfirm.setDisable(false);
            SiteOrderUiAlerts.warn("Không thể tạo đơn",
                    "Dữ liệu chưa được lưu. Vui lòng kiểm tra kết nối DB và thử lại.");
        }
    }

    @FXML
    private void handleClose() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();
    }

    private String formatMoney(double value) {
        return NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN")).format(value) + " đ";
    }
}

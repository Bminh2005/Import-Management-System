package com.app.modules.procurement.order.ui;

import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.app.modules.procurement.order.model.ImportRequestInfo;
import com.app.modules.procurement.order.model.OrderStatus;
import com.app.modules.procurement.order.model.ReallocationResult;
import com.app.modules.procurement.order.model.RequestDetailItem;
import com.app.modules.procurement.order.model.SiteAllocationEntry;
import com.app.modules.procurement.order.model.SiteInventoryInfo;
import com.app.modules.procurement.order.model.SiteOrder;
import com.app.modules.procurement.order.model.SiteOrderItem;
import com.app.modules.procurement.order.service.SiteOrderService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SiteOrderReallocationController implements Initializable {

    @FXML private Button btnBack;
    @FXML private Button btnPreviewTop;
    @FXML private Label lblTitle;
    @FXML private Label lblDesiredDate;
    @FXML private Label lblProgress;
    @FXML private ProgressBar progressBar;
    @FXML private Label lblCancelledSite;
    @FXML private Label lblCancelReason;
    @FXML private Label lblTotalItems;
    @FXML private Label lblAllocated;
    @FXML private Label lblOrderer;
    @FXML private VBox itemCardsContainer;
    @FXML private Label lblEmptyState;
    @FXML private HBox footerBar;
    @FXML private Button btnCancel;
    @FXML private Button btnPreview;

    private final SiteOrderService service = SiteOrderNavigator.service();
    private SiteOrder currentOrder;
    private ImportRequestInfo requestInfo;
    private LocalDate desiredDate;
    private List<RequestDetailItem> reallocationLines = new ArrayList<>();
    private final Map<Long, List<SiteAllocationEntry>> allocationsByItem = new LinkedHashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        footerBar.setVisible(false);
        footerBar.setManaged(false);
        btnPreviewTop.setVisible(false);
        btnPreviewTop.setManaged(false);
        validateInjectedFields();
    }

    private void validateInjectedFields() {
        Objects.requireNonNull(btnBack, "btnBack is null");
        Objects.requireNonNull(btnPreviewTop, "btnPreviewTop is null");
        Objects.requireNonNull(lblTitle, "lblTitle is null");
        Objects.requireNonNull(lblDesiredDate, "lblDesiredDate is null");
        Objects.requireNonNull(lblProgress, "lblProgress is null");
        Objects.requireNonNull(progressBar, "progressBar is null");
        Objects.requireNonNull(lblCancelledSite, "lblCancelledSite is null");
        Objects.requireNonNull(lblCancelReason, "lblCancelReason is null");
        Objects.requireNonNull(lblTotalItems, "lblTotalItems is null");
        Objects.requireNonNull(lblAllocated, "lblAllocated is null");
        Objects.requireNonNull(lblOrderer, "lblOrderer is null");
        Objects.requireNonNull(itemCardsContainer, "itemCardsContainer is null");
        Objects.requireNonNull(lblEmptyState, "lblEmptyState is null");
        Objects.requireNonNull(footerBar, "footerBar is null");
        Objects.requireNonNull(btnCancel, "btnCancel is null");
        Objects.requireNonNull(btnPreview, "btnPreview is null");
    }

    public void loadOrder(long orderId) {
        currentOrder = service.getOrderById(orderId);
        if (currentOrder == null) {
            return;
        }
        allocationsByItem.clear();
        requestInfo = service.resolveRequestContext(currentOrder);
        desiredDate = service.resolveDesiredDate(requestInfo, currentOrder);
        reallocationLines = service.getReallocationLineItems(currentOrder);
        renderView();
    }

    private void renderView() {
        lblTitle.setText("Phân bổ lại " + SiteOrderService.formatOrderCode(currentOrder.getId()));
        lblOrderer.setText(currentOrder.getOrdererName());
        lblDesiredDate.setText("Ngày nhận mong muốn: " + SiteOrderService.formatDate(desiredDate));
        lblCancelledSite.setText("Đơn hàng bị hủy bởi: " + currentOrder.getSiteName());

        String refusedMsg = currentOrder.getItems().stream()
                .map(SiteOrderItem::getRefusedReason)
                .filter(r -> r != null && !r.isBlank())
                .findFirst()
                .orElse("Site đã từ chối đơn hàng.");
        lblCancelReason.setText(refusedMsg);

        if (currentOrder.getStatus() != OrderStatus.REFUSED) {
            lblEmptyState.setText("Chỉ đơn bị hủy mới được phân bổ lại.");
            lblEmptyState.setVisible(true);
            lblEmptyState.setManaged(true);
            itemCardsContainer.setVisible(false);
            itemCardsContainer.setManaged(false);
            lblTotalItems.setText("0");
            refreshSummary();
            return;
        }

        boolean hasLines = !reallocationLines.isEmpty();
        lblEmptyState.setVisible(!hasLines);
        lblEmptyState.setManaged(!hasLines);
        itemCardsContainer.setVisible(hasLines);
        itemCardsContainer.setManaged(hasLines);
        lblTotalItems.setText(String.valueOf(reallocationLines.size()));
        renderItemCards();
        refreshSummary();
    }

    private void renderItemCards() {
        itemCardsContainer.getChildren().clear();
        int index = 1;
        for (RequestDetailItem item : reallocationLines) {
            itemCardsContainer.getChildren().add(buildItemCard(index++, item));
        }
    }

    private VBox buildItemCard(int index, RequestDetailItem item) {
        List<SiteAllocationEntry> entries = allocationsByItem.getOrDefault(
                item.getMerchandiseDetailId(), List.of());
        long allocated = entries.stream().mapToLong(SiteAllocationEntry::getQuantity).sum();
        long required = item.getRequiredQuantity();
        double itemProgress = required == 0 ? 0 : Math.min(1.0, (double) allocated / required);
        boolean fullyAllocated = required > 0 && allocated >= required;

        Label indexLabel = new Label("#" + index);
        indexLabel.getStyleClass().add("item-index");

        Label itemLabel = new Label(item.getMerchandiseName());
        itemLabel.getStyleClass().add("item-card-title");

        Label codeBadge = new Label("MH" + item.getMerchandiseDetailId());
        codeBadge.getStyleClass().add("code-badge");

        String statusText = fullyAllocated ? "Đã phân bổ"
                : (allocated > 0 ? "Đang phân bổ" : "Chưa phân bổ");
        Label statusBadge = new Label(statusText);
        statusBadge.getStyleClass().add(fullyAllocated ? "badge-done"
                : (allocated > 0 ? "badge-blue" : "badge-pending"));

        ProgressBar itemBar = new ProgressBar(itemProgress);
        itemBar.getStyleClass().add("item-progress-mini");
        itemBar.setPrefWidth(72);
        itemBar.setMaxWidth(72);
        itemBar.setPrefHeight(6);
        itemBar.setMaxHeight(6);

        Label pctLabel = new Label(Math.round(itemProgress * 100) + "%");
        pctLabel.getStyleClass().add("item-pct-mini");

        HBox statusGroup = new HBox(8, statusBadge, itemBar, pctLabel);
        statusGroup.setAlignment(Pos.CENTER_RIGHT);

        Button allocateButton = new Button("Phân bổ Site");
        allocateButton.getStyleClass().addAll("btn-action", "btn-primary");
        allocateButton.setOnAction(evt -> openAllocationDialog(item));

        Region spacer = new Region();
        HBox topRow = new HBox(12, indexLabel, itemLabel, codeBadge, spacer, statusGroup, allocateButton);
        topRow.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label requiredLabel = new Label("🎯 " + required + " " + item.getUnit());
        requiredLabel.getStyleClass().add("info-label");

        Label allocatedLabel = new Label("Đã phân bổ: " + allocated + "/" + required + " " + item.getUnit());
        allocatedLabel.getStyleClass().add("info-value");

        VBox card = new VBox(10, topRow, requiredLabel, allocatedLabel);
        if (!entries.isEmpty()) {
            VBox allocationLines = new VBox(6);
            allocationLines.getStyleClass().add("allocation-lines");
            for (SiteAllocationEntry entry : entries) {
                Label line = new Label("📍 " + entry.getSiteName()
                        + " · " + entry.getQuantity() + " " + item.getUnit());
                line.getStyleClass().add("allocation-line");
                line.setWrapText(true);
                allocationLines.getChildren().add(line);
            }
            card.getChildren().add(allocationLines);
        }

        card.getStyleClass().addAll("card", "item-card");
        card.setPadding(new Insets(16));
        return card;
    }

    private void openAllocationDialog(RequestDetailItem item) {
        try {
            List<SiteInventoryInfo> sites = service.getEligibleSitesForReallocation(
                    item.getMerchandiseDetailId(), item.getRequiredQuantity(), desiredDate);
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SiteAllocationDialog.fxml"));
            
            // Nếu dùng Spring Boot, bỏ comment dòng dưới và gọi context:
            // loader.setControllerFactory(ApplicationContextHolder.getContext()::getBean);

            Parent root = loader.load();
            SiteAllocationDialogController controller = loader.getController();
            
            List<SiteAllocationEntry> previous = allocationsByItem.getOrDefault(
                    item.getMerchandiseDetailId(), List.of());
            controller.setRequestData(item, desiredDate, sites, previous, saveAllocations(item));
            
            Stage dialog = new Stage();
            dialog.initOwner(btnBack.getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(root));
            dialog.setTitle("Phân bổ Site");
            dialog.showAndWait();
        } catch (Exception exception) {
            exception.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi hệ thống");
            alert.setHeaderText(null);
            alert.setContentText("Không thể mở hộp thoại phân bổ: " + exception.getMessage());
            alert.showAndWait();
        }
    }

    private Consumer<List<SiteAllocationEntry>> saveAllocations(RequestDetailItem item) {
        return allocations -> {
            if (allocations == null || allocations.isEmpty()) {
                allocationsByItem.remove(item.getMerchandiseDetailId());
            } else {
                allocationsByItem.put(item.getMerchandiseDetailId(), allocations);
            }
            renderItemCards();
            refreshSummary();
        };
    }

    private void refreshSummary() {
        long totalAllocated = allocationsByItem.values().stream()
                .flatMap(List::stream)
                .mapToLong(SiteAllocationEntry::getQuantity).sum();
        long totalRequired = reallocationLines.stream().mapToLong(RequestDetailItem::getRequiredQuantity).sum();
        lblAllocated.setText(totalAllocated + " / " + totalRequired);
        double progress = totalRequired == 0 ? 0 : (double) totalAllocated / totalRequired;
        progressBar.setProgress(Math.min(1, progress));
        lblProgress.setText(String.format("Tiến độ %d%%", Math.round(progress * 100)));

        boolean showFooter = totalAllocated > 0 && currentOrder.getStatus() == OrderStatus.REFUSED;
        footerBar.setVisible(showFooter);
        footerBar.setManaged(showFooter);
        btnPreviewTop.setVisible(showFooter);
        btnPreviewTop.setManaged(showFooter);
    }

    @FXML
    private void handleBack() {
        SiteOrderNavigator.showList(btnBack.getScene());
    }

    @FXML
    private void handleCancel() {
        SiteOrderNavigator.showList(btnCancel.getScene());
    }

    @FXML
    private void handleOpenConfirmDialog() {
        openConfirmDialog();
    }

    @FXML
    private void handleOpenConfirmDialogTop() {
        openConfirmDialog();
    }

    private void openConfirmDialog() {
        if (allocationsByItem.isEmpty() || currentOrder == null) {
            SiteOrderUiAlerts.warn("Chưa phân bổ", "Vui lòng phân bổ ít nhất một mặt hàng.");
            return;
        }
        
        long totalRequired = reallocationLines.stream().mapToLong(RequestDetailItem::getRequiredQuantity).sum();
        long totalAllocated = allocationsByItem.values().stream()
                .flatMap(List::stream).mapToLong(SiteAllocationEntry::getQuantity).sum();
                
        if (totalAllocated < totalRequired) {
            SiteOrderUiAlerts.warn("Phân bổ chưa đủ",
                    "Cần phân bổ đủ " + totalRequired + " trước khi xác nhận. Hiện tại: " + totalAllocated + ".");
            return;
        }
        
        for (RequestDetailItem line : reallocationLines) {
            long allocated = allocationsByItem.getOrDefault(line.getMerchandiseDetailId(), List.of())
                    .stream().mapToLong(SiteAllocationEntry::getQuantity).sum();
            if (allocated != line.getRequiredQuantity()) {
                SiteOrderUiAlerts.warn("Phân bổ chưa đủ",
                        "Mặt hàng " + line.getMerchandiseName() + " chưa được phân bổ đủ.");
                return;
            }
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SiteOrderConfirmDialog.fxml"));
            
            // Nếu dùng Spring Boot, bỏ comment dòng dưới và gọi context:
            // loader.setControllerFactory(ApplicationContextHolder.getContext()::getBean);

            Parent root = loader.load();
            SiteOrderConfirmDialogController controller = loader.getController();
            Scene ownerScene = btnPreview.getScene();
            
            controller.setData(currentOrder, requestInfo, allocationsByItem, ownerScene);
            
            Stage dialog = new Stage();
            dialog.initOwner(ownerScene.getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(root));
            dialog.setTitle("Xác nhận đơn phân bổ");
            dialog.showAndWait();
        } catch (Exception exception) {
            exception.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi hệ thống");
            alert.setHeaderText(null);
            alert.setContentText("Không thể mở hộp thoại xác nhận: " + exception.getMessage());
            alert.showAndWait();
        }
    }
}
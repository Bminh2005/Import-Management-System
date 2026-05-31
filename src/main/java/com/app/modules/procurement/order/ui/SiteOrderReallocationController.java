package com.app.modules.procurement.order.ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.app.modules.procurement.order.model.ImportRequestInfo;
import com.app.modules.procurement.order.model.RequestDetailItem;
import com.app.modules.procurement.order.model.SiteAllocationEntry;
import com.app.modules.procurement.order.model.SiteInventoryInfo;
import com.app.modules.procurement.order.model.SiteOrder;
import com.app.modules.procurement.order.service.SiteOrderService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SiteOrderReallocationController implements Initializable {

    @FXML private Button btnBack;
    @FXML private Label lblTitle;
    @FXML private Label lblProgress;
    @FXML private ProgressBar progressBar;
    @FXML private Label lblCancelledSite;
    @FXML private Label lblCancelReason;
    @FXML private Label lblTotalItems;
    @FXML private Label lblAllocated;
    @FXML private Label lblOrderer;
    @FXML private VBox itemCardsContainer;
    @FXML private HBox footerBar;
    @FXML private Button btnCancel;
    @FXML private Button btnPreview;

    private final SiteOrderService service = new SiteOrderService();
    private SiteOrder currentOrder;
    private ImportRequestInfo requestInfo;
    private List<RequestDetailItem> requestItems = new ArrayList<>();
    private final Map<Long, List<SiteAllocationEntry>> allocationsByItem = new LinkedHashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        footerBar.setVisible(false);
        footerBar.setManaged(false);
        validateInjectedFields();
    }

    private void validateInjectedFields() {
        Objects.requireNonNull(btnBack);
        Objects.requireNonNull(lblTitle);
        Objects.requireNonNull(lblProgress);
        Objects.requireNonNull(progressBar);
        Objects.requireNonNull(lblCancelledSite);
        Objects.requireNonNull(lblCancelReason);
        Objects.requireNonNull(lblTotalItems);
        Objects.requireNonNull(lblAllocated);
        Objects.requireNonNull(lblOrderer);
        Objects.requireNonNull(itemCardsContainer);
        Objects.requireNonNull(footerBar);
        Objects.requireNonNull(btnCancel);
        Objects.requireNonNull(btnPreview);
    }

    public void loadOrder(long orderId) {
        currentOrder = service.getOrderById(orderId);
        if (currentOrder == null) {
            return;
        }
        requestInfo = service.getRequestInfo(currentOrder.getRequestId());
        requestItems = service.getRequestDetails(currentOrder.getRequestId());
        renderView();
    }

    private void renderView() {
        lblTitle.setText("Phân bổ lại Đơn #" + currentOrder.getId());
        lblOrderer.setText(currentOrder.getOrdererName());
        lblCancelledSite.setText("Đơn hàng bị hủy bởi: " + currentOrder.getSiteId() + " – " + currentOrder.getSiteName());
        lblCancelReason.setText("Hãy chọn site mới và phân bổ lại các mặt hàng.");

        lblTotalItems.setText(String.valueOf(requestItems.size()));
        renderItemCards();
        refreshSummary();
    }

    private void renderItemCards() {
        itemCardsContainer.getChildren().clear();
        for (RequestDetailItem item : requestItems) {
            VBox card = buildItemCard(item);
            itemCardsContainer.getChildren().add(card);
        }
    }

    private VBox buildItemCard(RequestDetailItem item) {
        long allocated = allocationsByItem.getOrDefault(item.getMerchandiseDetailId(), List.of())
                .stream().mapToLong(SiteAllocationEntry::getQuantity).sum();
        Label itemLabel = new Label(item.getMerchandiseName());
        itemLabel.getStyleClass().add("section-title");

        Label requiredLabel = new Label("Số lượng yêu cầu: " + item.getRequiredQuantity() + " " + item.getUnit());
        requiredLabel.getStyleClass().add("info-label");

        Label allocatedLabel = new Label("Đã phân bổ: " + allocated + " / " + item.getRequiredQuantity());
        allocatedLabel.getStyleClass().add("info-value");

        Button allocateButton = new Button("Phân bổ");
        allocateButton.getStyleClass().add("btn-primary");
        allocateButton.setOnAction(evt -> openAllocationDialog(item));

        HBox topRow = new HBox(18, itemLabel, allocateButton);
        topRow.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        HBox.setHgrow(itemLabel, Priority.ALWAYS);

        VBox card = new VBox(10, topRow, requiredLabel, allocatedLabel);
        card.getStyleClass().addAll("card", "item-card");
        card.setPadding(new javafx.geometry.Insets(16));
        return card;
    }

    private void openAllocationDialog(RequestDetailItem item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SiteAllocationDialog.fxml"));
            Parent root = loader.load();
            SiteAllocationDialogController controller = loader.getController();
            List<SiteInventoryInfo> availableSites = service.getAvailableSitesForItem(item.getMerchandiseDetailId());
            List<SiteAllocationEntry> previous = allocationsByItem.getOrDefault(item.getMerchandiseDetailId(), List.of());
            controller.setRequestData(item, availableSites, previous, allocations -> {
                if (allocations == null || allocations.isEmpty()) {
                    allocationsByItem.remove(item.getMerchandiseDetailId());
                } else {
                    allocationsByItem.put(item.getMerchandiseDetailId(), allocations);
                }
                renderItemCards();
                refreshSummary();
            });
            Stage dialog = new Stage();
            dialog.initOwner(btnBack.getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(root));
            dialog.setTitle("Phân bổ site cho " + item.getMerchandiseName());
            dialog.showAndWait();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void refreshSummary() {
        long totalAllocated = allocationsByItem.values().stream()
                .flatMap(List::stream)
                .mapToLong(SiteAllocationEntry::getQuantity).sum();
        long totalRequired = requestItems.stream().mapToLong(RequestDetailItem::getRequiredQuantity).sum();
        lblAllocated.setText(totalAllocated + " / " + totalRequired);
        double progress = totalRequired == 0 ? 0 : (double) totalAllocated / totalRequired;
        progressBar.setProgress(Math.min(1, progress));
        lblProgress.setText(String.format("Tiến độ %d%%", Math.round(progress * 100)));

        boolean showFooter = totalAllocated > 0;
        footerBar.setVisible(showFooter);
        footerBar.setManaged(showFooter);
    }

    @FXML
    private void handleBack() {
        navigateToList();
    }

    @FXML
    private void handleCancel() {
        navigateToList();
    }

    @FXML
    private void handleOpenConfirmDialog() {
        if (allocationsByItem.isEmpty()) {
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SiteOrderConfirmDialog.fxml"));
            Parent root = loader.load();
            SiteOrderConfirmDialogController controller = loader.getController();
            controller.setData(requestInfo, allocationsByItem);
            Stage dialog = new Stage();
            dialog.initOwner(btnPreview.getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(root));
            dialog.setTitle("Xác nhận đơn phân bổ");
            dialog.showAndWait();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void navigateToList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SiteOrderListView.fxml"));
            Parent root = loader.load();
            Scene scene = btnBack.getScene();
            scene.setRoot(root);
            ((Stage) scene.getWindow()).setTitle("Đơn Đặt Hàng - Danh sách");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

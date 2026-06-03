package com.app.modules.procurement.order.ui;

import java.net.URL;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import com.app.modules.procurement.order.model.OrderStatus;
import com.app.modules.procurement.order.model.SiteOrder;
import com.app.modules.procurement.order.model.SiteOrderItem;
import com.app.modules.procurement.order.service.SiteOrderService;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

public class SiteOrderDetailController implements Initializable {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML private Button btnBack;
    @FXML private Label lblTitle;
    @FXML private Label lblStatusBadge;
    @FXML private Label lblOrderer;
    @FXML private Label lblSite;
    @FXML private Label lblOrderCreated;
    @FXML private Label lblTotalValue;

    @FXML private HBox alertRefused;
    @FXML private Label lblRefusedReason;

    @FXML private Label lblItemCount;
    @FXML private TableView<SiteOrderItem> itemTable;
    @FXML private TableColumn<SiteOrderItem, String> colItemCode;
    @FXML private TableColumn<SiteOrderItem, String> colItemName;
    @FXML private TableColumn<SiteOrderItem, String> colQty;
    @FXML private TableColumn<SiteOrderItem, String> colUnit;
    @FXML private TableColumn<SiteOrderItem, String> colUnitPrice;
    @FXML private TableColumn<SiteOrderItem, String> colSubtotal;

    @FXML private Label lblSiteCode;
    @FXML private Label lblSiteName;
    @FXML private Label lblSiteAddress;
    @FXML private Label lblShipDays;
    @FXML private Label lblAirDays;

    @FXML private Button btnReallocate;

    private final SiteOrderService service = SiteOrderNavigator.service();
    private SiteOrder currentOrder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alertRefused.setVisible(false);
        alertRefused.setManaged(false);
        validateInjectedFields();
        setupTableColumns();
    }

    private void validateInjectedFields() {
        Objects.requireNonNull(btnBack);
        Objects.requireNonNull(lblTitle);
        Objects.requireNonNull(lblStatusBadge);
        Objects.requireNonNull(lblOrderer);
        Objects.requireNonNull(lblSite);
        Objects.requireNonNull(lblOrderCreated);
        Objects.requireNonNull(lblTotalValue);
        Objects.requireNonNull(alertRefused);
        Objects.requireNonNull(lblRefusedReason);
        Objects.requireNonNull(lblItemCount);
        Objects.requireNonNull(itemTable);
        Objects.requireNonNull(colItemCode);
        Objects.requireNonNull(colItemName);
        Objects.requireNonNull(colQty);
        Objects.requireNonNull(colUnit);
        Objects.requireNonNull(colUnitPrice);
        Objects.requireNonNull(colSubtotal);
        Objects.requireNonNull(lblSiteCode);
        Objects.requireNonNull(lblSiteName);
        Objects.requireNonNull(lblSiteAddress);
        Objects.requireNonNull(lblShipDays);
        Objects.requireNonNull(lblAirDays);
        Objects.requireNonNull(btnReallocate);
    }

    private void setupTableColumns() {
        colItemCode.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper("MH" + cell.getValue().getMerchandiseDetailId()));
        colItemName.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(cell.getValue().getMerchandiseName()));
        colQty.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(String.valueOf(cell.getValue().getQuantity())));
        colUnit.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(nullToEmpty(cell.getValue().getUnit())));
        colUnitPrice.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(formatMoney(cell.getValue().getPrice())));
        colSubtotal.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(formatMoney(cell.getValue().getPrice() * cell.getValue().getQuantity())));
    }

    public void loadOrder(long orderId) {
        currentOrder = service.getOrderById(orderId);
        if (currentOrder == null) {
            return;
        }
        renderOrder();
    }

    private void renderOrder() {
        lblTitle.setText("Chi tiết Đơn Đặt Hàng " + SiteOrderService.formatOrderCode(currentOrder.getId()));
        lblOrderer.setText(currentOrder.getOrdererName());
        lblSite.setText(currentOrder.getSiteName());
        lblOrderCreated.setText(currentOrder.getCreatedAt() == null
                ? "—"
                : currentOrder.getCreatedAt().format(DATE_FMT));

        double totalValue = currentOrder.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getPrice())
                .sum();
        lblTotalValue.setText(formatMoney(totalValue));

        setStatusDisplay(currentOrder.getStatus());
        boolean canReallocate = currentOrder.getStatus() == OrderStatus.REFUSED
                && !currentOrder.getItems().isEmpty();
        btnReallocate.setVisible(canReallocate);
        btnReallocate.setManaged(canReallocate);

        renderItems(currentOrder.getItems());
        renderSiteInfo();
    }

    private void setStatusDisplay(OrderStatus status) {
        lblStatusBadge.setText(formatStatus(status));
        lblStatusBadge.getStyleClass().removeAll("badge-yellow", "badge-green", "badge-red", "badge-blue");
        lblStatusBadge.getStyleClass().add(switch (status) {
            case ACCEPTED -> "badge-green";
            case REFUSED -> "badge-red";
            case PROCESSING -> "badge-blue";
            default -> "badge-yellow";
        });
        if (status == OrderStatus.REFUSED) {
            alertRefused.setVisible(true);
            alertRefused.setManaged(true);
            lblRefusedReason.setText(resolveRefusedMessage(currentOrder.getItems()));
        } else {
            alertRefused.setVisible(false);
            alertRefused.setManaged(false);
        }
    }

    private static String resolveRefusedMessage(List<SiteOrderItem> items) {
        return items.stream()
                .map(SiteOrderItem::getRefusedReason)
                .filter(reason -> reason != null && !reason.isBlank())
                .findFirst()
                .orElse("Site đã từ chối đơn hàng.");
    }

    private void renderItems(List<SiteOrderItem> items) {
        itemTable.setItems(FXCollections.observableArrayList(items));
        lblItemCount.setText(items.size() + " mặt hàng");
    }

    private void renderSiteInfo() {
        lblSiteCode.setText("SITE" + String.format("%02d", currentOrder.getSiteId()));
        lblSiteName.setText(nullToDash(currentOrder.getSiteName()));
        lblSiteAddress.setText(nullToDash(currentOrder.getSiteAddress()));
        lblShipDays.setText(currentOrder.getDeliveryByShip() > 0
                ? currentOrder.getDeliveryByShip() + " ngày" : "—");
        lblAirDays.setText(currentOrder.getDeliveryByAir() > 0
                ? currentOrder.getDeliveryByAir() + " ngày" : "—");
    }

    private String formatStatus(OrderStatus status) {
        return switch (status) {
            case ACCEPTED -> "Đã xử lý";
            case REFUSED -> "Bị hủy";
            case PROCESSING -> "Đang xử lý";
            default -> "Chưa xử lý";
        };
    }

    private String formatMoney(double value) {
        return NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN")).format(value) + " đ";
    }

    private static String nullToDash(String value) {
        return value == null || value.isBlank() ? "—" : value;
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    @FXML
    private void handleBack() {
        SiteOrderNavigator.showList(btnBack.getScene());
    }

    @FXML
    private void handleReallocate() {
        if (currentOrder == null || currentOrder.getStatus() != OrderStatus.REFUSED) {
            return;
        }
        SiteOrderNavigator.showReallocation(btnReallocate.getScene(), currentOrder.getId());
    }
}

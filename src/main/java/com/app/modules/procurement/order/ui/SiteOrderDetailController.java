package com.app.modules.procurement.order.ui;

import java.net.URL;
import java.text.NumberFormat;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SiteOrderDetailController implements Initializable {

    @FXML private Button btnBack;
    @FXML private Label lblTitle;
    @FXML private Label lblStatusBadge;
    @FXML private Label lblOrderer;
    @FXML private Label lblSite;
    @FXML private Label lblCreatedAt;
    @FXML private Label lblTotalValue;

    @FXML private HBox alertRefused;
    @FXML private Label lblRefusedReason;

    @FXML private Label lblItemCount;
    @FXML private TableView<SiteOrderItem> itemTable;
    @FXML private TableColumn<SiteOrderItem, String> colItemCode;
    @FXML private TableColumn<SiteOrderItem, String> colItemName;
    @FXML private TableColumn<SiteOrderItem, String> colQty;
    @FXML private TableColumn<SiteOrderItem, String> colUnitPrice;
    @FXML private TableColumn<SiteOrderItem, String> colSubtotal;

    @FXML private Label lblSiteCode;
    @FXML private Label lblSiteName;
    @FXML private Label lblSiteAddress;
    @FXML private Label lblShipDays;
    @FXML private Label lblAirDays;

    @FXML private Button btnBackList;
    @FXML private Button btnReallocate;

    private final SiteOrderService service = new SiteOrderService();
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
        Objects.requireNonNull(lblCreatedAt);
        Objects.requireNonNull(lblTotalValue);
        Objects.requireNonNull(alertRefused);
        Objects.requireNonNull(lblRefusedReason);
        Objects.requireNonNull(lblItemCount);
        Objects.requireNonNull(itemTable);
        Objects.requireNonNull(colItemCode);
        Objects.requireNonNull(colItemName);
        Objects.requireNonNull(colQty);
        Objects.requireNonNull(colUnitPrice);
        Objects.requireNonNull(colSubtotal);
        Objects.requireNonNull(lblSiteCode);
        Objects.requireNonNull(lblSiteName);
        Objects.requireNonNull(lblSiteAddress);
        Objects.requireNonNull(lblShipDays);
        Objects.requireNonNull(lblAirDays);
        Objects.requireNonNull(btnBackList);
        Objects.requireNonNull(btnReallocate);
    }

    private void setupTableColumns() {
        colItemCode.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(String.valueOf(cell.getValue().getMerchandiseDetailId())));
        colItemName.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(cell.getValue().getMerchandiseName()));
        colQty.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(String.valueOf(cell.getValue().getQuantity())));
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
        lblTitle.setText("Chi tiết Đơn Đặt Hàng #" + currentOrder.getId());
        lblOrderer.setText(currentOrder.getOrdererName());
        lblSite.setText(currentOrder.getSiteId() + " – " + currentOrder.getSiteName());
        lblCreatedAt.setText(currentOrder.getExpectedDeliveryDate() == null
                ? "-"
                : currentOrder.getExpectedDeliveryDate().toString());
        double totalValue = currentOrder.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getPrice())
                .sum();
        lblTotalValue.setText(formatMoney(totalValue));

        setStatusDisplay(currentOrder.getStatus());
        renderItems(currentOrder.getItems());
        renderSiteInfo();
    }

    private void setStatusDisplay(OrderStatus status) {
        lblStatusBadge.setText(formatStatus(status));
        lblStatusBadge.getStyleClass().removeAll("badge-yellow", "badge-green", "badge-red", "badge-blue");
        String styleClass = switch (status) {
            case ACCEPTED -> "badge-green";
            case REFUSED -> "badge-red";
            case PROCESSING -> "badge-blue";
            default -> "badge-yellow";
        };
        lblStatusBadge.getStyleClass().add(styleClass);
        if (status == OrderStatus.REFUSED) {
            alertRefused.setVisible(true);
            alertRefused.setManaged(true);
            lblRefusedReason.setText("Đơn hàng đã bị hủy và cần phân bổ lại.");
        } else {
            alertRefused.setVisible(false);
            alertRefused.setManaged(false);
        }
    }

    private void renderItems(List<SiteOrderItem> items) {
        itemTable.setItems(FXCollections.observableArrayList(items));
        lblItemCount.setText(items.size() + " mặt hàng");
    }

    private void renderSiteInfo() {
        lblSiteCode.setText(String.valueOf(currentOrder.getSiteId()));
        lblSiteName.setText(currentOrder.getSiteName());
        lblSiteAddress.setText("-");
        lblShipDays.setText("-");
        lblAirDays.setText("-");
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

    @FXML
    private void handleBack() {
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

    @FXML
    private void handleReallocate() {
        if (currentOrder == null) {
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SiteOrderReallocationView.fxml"));
            Parent root = loader.load();
            SiteOrderReallocationController controller = loader.getController();
            controller.loadOrder(currentOrder.getId());
            Scene scene = btnReallocate.getScene();
            scene.setRoot(root);
            ((Stage) scene.getWindow()).setTitle("Đơn Đặt Hàng - Phân bổ lại");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

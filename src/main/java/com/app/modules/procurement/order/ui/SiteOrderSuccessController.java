package com.app.modules.procurement.order.ui;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import com.app.modules.procurement.order.model.ReallocationResult;
import com.app.modules.procurement.order.service.SiteOrderService;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SiteOrderSuccessController implements Initializable {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML private Label lblHeadline;
    @FXML private Label lblSubtext;
    @FXML private Label lblNewOrderCount;
    @FXML private Label lblProcessDate;
    @FXML private Label lblStatus;
    @FXML private Label lblOrderIds;
    @FXML private Label lblRemovedOrder;
    @FXML private Button btnHome;
    @FXML private Button btnList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Objects.requireNonNull(lblHeadline);
        Objects.requireNonNull(lblSubtext);
        Objects.requireNonNull(lblNewOrderCount);
        Objects.requireNonNull(lblProcessDate);
        Objects.requireNonNull(lblStatus);
        Objects.requireNonNull(lblOrderIds);
        Objects.requireNonNull(lblRemovedOrder);
        Objects.requireNonNull(btnHome);
        Objects.requireNonNull(btnList);
    }

    public void setResult(ReallocationResult result, long removedOrderId) {
        List<Long> ids = result.getCreatedOrderIds();
        lblNewOrderCount.setText(String.valueOf(ids.size()));
        lblProcessDate.setText(LocalDate.now().format(DATE_FMT));
        lblStatus.setText("Chưa xử lý");
        lblRemovedOrder.setText("Đơn bị hủy " + SiteOrderService.formatOrderCode(removedOrderId)
                + " đã được xóa khỏi danh sách.");
        if (ids.isEmpty()) {
            lblOrderIds.setText("Không có đơn mới được tạo.");
            return;
        }

        List<String> siteNames = result.getCreatedOrderSiteNames();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            if (i > 0) {
                builder.append('\n');
            }
            String site = i < siteNames.size() ? siteNames.get(i) : "Site";
            builder.append(SiteOrderService.formatOrderCode(ids.get(i)))
                    .append(" -> gửi tới ")
                    .append(site);
        }
        lblOrderIds.setText(builder.toString());
    }

    @FXML
    private void handleHome() {
        SiteOrderNavigator.showList(btnHome.getScene());
    }

    @FXML
    private void handleList() {
        SiteOrderNavigator.showList(btnList.getScene());
    }
}

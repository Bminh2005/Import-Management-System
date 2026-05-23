package com.app.Ioms.ui.orders;

import com.app.Ioms.domain.Order;
import com.app.Ioms.navigation.Router;
import com.app.Ioms.service.AllocationService;
import com.app.Ioms.service.OrderService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class ConfirmOrdersView extends BorderPane {
package com.app.Ioms.ui.orders;

import com.app.Ioms.domain.Order;
import com.app.Ioms.navigation.Router;
import com.app.Ioms.service.AllocationService;
import com.app.Ioms.service.OrderService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class ConfirmOrdersView extends BorderPane {

    @FXML
    private Label lblOrderId;

    @FXML
    private TextArea txtSummary;

    @FXML
    private Button btnSend;

    private final OrderService orderService = OrderService.getInstance();
    private final AllocationService allocationService = new AllocationService();
    private final AllocationResult result;

    public ConfirmOrdersView(AllocationResult result) {
        this.result = result;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("confirm-orders.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Cannot load confirm-orders.fxml", e);
        }
        getStylesheets().add(Objects.requireNonNull(getClass().getResource("confirm-orders.css")).toExternalForm());
    }

    @FXML
    private void initialize() {
        if (result == null) {
            lblOrderId.setText("Chưa có phân bổ trong phiên này");
            txtSummary.setText("Hãy thực hiện phân bổ từ màn hình 'Phân bổ'.");
            btnSend.setDisable(true);
            return;
        }
        Order order = result.getOrder();
        lblOrderId.setText(order.getId());
        StringBuilder sb = new StringBuilder();
        sb.append("Xác nhận phân bổ cho đơn: ").append(order.getId()).append("\n");
        for (Map.Entry<String, String> e : result.getAllocation().entrySet()) {
            sb.append(" - SKU ").append(e.getKey()).append(" -> Site ").append(e.getValue()).append("\n");
        }
        txtSummary.setText(sb.toString());
        btnSend.setOnAction(e -> {
            allocationService.reserve(order, result.getAllocation());
            orderService.markProcessing(order);
            new Alert(Alert.AlertType.INFORMATION, "Đã gửi đơn. Trạng thái: ĐANG XỬ LÝ.").showAndWait();
            Router.goToOrdersList();
        });
    }
}
    @FXML
    private Label lblOrderId;

    @FXML
    private TextArea txtSummary;

    @FXML
    private Button btnSend;

    private final OrderService orderService = OrderService.getInstance();
    private final AllocationService allocationService = new AllocationService();
    private final AllocationResult result;

    public ConfirmOrdersView(AllocationResult result) {
        this.result = result;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("confirm-orders.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Cannot load confirm-orders.fxml", e);
        }
        getStylesheets().add(Objects.requireNonNull(getClass().getResource("confirm-orders.css")).toExternalForm());
    }

    @FXML
    private void initialize() {
        if (result == null) {
            lblOrderId.setText("Chưa có phân bổ trong phiên này");
            txtSummary.setText("Hãy thực hiện phân bổ từ màn hình 'Phân bổ'.");
            btnSend.setDisable(true);
            return;
        }
        Order order = result.getOrder();
        lblOrderId.setText(order.getId());
        StringBuilder sb = new StringBuilder();
        sb.append("Xác nhận phân bổ cho đơn: ").append(order.getId()).append("\n");
        for (Map.Entry<String, String> e : result.getAllocation().entrySet()) {
            sb.append(" - SKU ").append(e.getKey()).append(" -> Site ").append(e.getValue()).append("\n");
        }
        txtSummary.setText(sb.toString());
        btnSend.setOnAction(e -> {
            // giữ chỗ tồn kho và cập nhật trạng thái
            allocationService.reserve(order, result.getAllocation());
            orderService.markProcessing(order);
            new Alert(Alert.AlertType.INFORMATION, "Đã gửi đơn. Trạng thái: ĐANG XỬ LÝ.").showAndWait();
            Router.goToOrdersList();
        });
    }
}

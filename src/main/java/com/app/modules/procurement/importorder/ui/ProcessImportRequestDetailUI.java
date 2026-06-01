package com.app.modules.procurement.importorder.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.app.modules.procurement.importorder.dto.AllocationProposalDTO;
import com.app.modules.procurement.importorder.repository.ImportOrderRepository;
import com.app.modules.procurement.importorder.service.ImportOrderService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * UI Class cho màn "Xử lý yêu cầu cụ thể".
 * Mỗi mặt hàng được phân bổ ra các site có tồn kho; người dùng có thể để hệ thống
 * tự đề xuất ("Tự động phân bổ") hoặc tự nhập số lượng cho từng site rồi xác nhận.
 */
public class ProcessImportRequestDetailUI extends ScrollPane {

    @FXML private Label titleLabel;
    @FXML private Label statusBadge;
    @FXML private Label requestCodeLabel;
    @FXML private Label createdDateLabel;
    @FXML private Label createdByLabel;
    @FXML private Label itemCountLabel;
    @FXML private Label messageLabel;

    @FXML private VBox itemsList;

    private final ImportOrderService service = new ImportOrderService();
    private long currentRequestId;
    private ImportRequestDetail current;
    private List<AllocationProposalDTO> options = new ArrayList<>();
    private final List<OptionRow> rowControls = new ArrayList<>();

    public ProcessImportRequestDetailUI() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ProcessImportRequestDetailPage.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Nạp dữ liệu yêu cầu theo mã (chính là id trong DB).
     */
    public void loadRequest(String requestCode) {
        try {
            long reqId = Long.parseLong(requestCode.trim());
            this.currentRequestId = reqId;

            ImportOrderRepository.RequestSummary reqSummary = service.getRequestById(reqId);
            if (reqSummary == null) {
                System.out.println("Không tìm thấy yêu cầu: " + requestCode);
                return;
            }

            List<ImportRequestItemRow> items = new ArrayList<>();
            for (ImportOrderRepository.RequestDetailItem item : service.getRequestDetails(reqId)) {
                items.add(new ImportRequestItemRow(
                        item.merchandiseName(),
                        item.quantity(),
                        item.unit(),
                        reqSummary.desiredDate(),
                        item.merchandiseDetailId()
                ));
            }

            this.current = new ImportRequestDetail(
                    reqSummary.code(),
                    reqSummary.createdDate(),
                    reqSummary.createdBy(),
                    reqSummary.status(),
                    items
            );

            this.options = service.getAllocationOptions(reqId);

            render();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void render() {
        if (current == null) {
            return;
        }

        titleLabel.setText("Xử lý yêu cầu: " + current.code());
        requestCodeLabel.setText(current.code());
        createdDateLabel.setText(current.createdDate());
        createdByLabel.setText(current.createdBy());

        statusBadge.setText(getStatusLabel(current.status()));
        statusBadge.getStyleClass().setAll("status-badge", getStatusStyleClass(current.status()));

        itemCountLabel.setText(current.items().size() + " mặt hàng");

        itemsList.getChildren().clear();
        rowControls.clear();
        for (ImportRequestItemRow item : current.items()) {
            itemsList.getChildren().add(buildItemBlock(item));
        }
    }

    private VBox buildItemBlock(ImportRequestItemRow item) {
        VBox block = new VBox(8);
        block.getStyleClass().add("item-row");

        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);

        Label name = new Label(item.name());
        name.getStyleClass().add("item-name");
        name.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(name, Priority.ALWAYS);

        Label requested = new Label("Yêu cầu: " + String.format("%,d", item.quantity()) + " " + item.unit());
        requested.getStyleClass().add("item-muted");

        Label needDate = new Label("Ngày cần: "
                + (item.requiredDate() == null || item.requiredDate().isBlank() ? "—" : item.requiredDate()));
        needDate.getStyleClass().add("item-muted");

        header.getChildren().addAll(name, requested, needDate);
        block.getChildren().add(header);

        List<AllocationProposalDTO> opts = optionsForItem(item.merchandiseDetailId());
        if (opts.isEmpty()) {
            Label none = new Label("Không có site nào còn tồn kho cho mặt hàng này");
            none.setStyle("-fx-text-fill:#ef4444; -fx-font-weight:bold;");
            block.getChildren().add(none);
            return block;
        }

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(6);
        addColumn(grid, 28);
        addColumn(grid, 16);
        addColumn(grid, 20);
        addColumn(grid, 18);
        addColumn(grid, 18);

        grid.add(headerCell("SITE"), 0, 0);
        grid.add(headerCell("GIAO HÀNG"), 1, 0);
        grid.add(headerCell("NGÀY NHẬN"), 2, 0);
        grid.add(headerCell("TỒN KHO"), 3, 0);
        grid.add(headerCell("SỐ LƯỢNG PHÂN BỔ"), 4, 0);

        int r = 1;
        for (AllocationProposalDTO opt : opts) {
            grid.add(new Label(opt.getSiteName()), 0, r);
            grid.add(new Label(opt.getDeliveryType().equals("SHIP") ? "Đường biển" : "Đường hàng không"), 1, r);
            grid.add(new Label(opt.getExpectedDeliveryDate()), 2, r);
            grid.add(new Label(String.format("%,d", opt.getAvailableQuantity()) + " " + item.unit()), 3, r);

            TextField qtyField = new TextField(opt.getAllocatedQuantity() > 0 ? String.valueOf(opt.getAllocatedQuantity()) : "");
            qtyField.setPromptText("0");
            qtyField.setPrefWidth(120);
            grid.add(qtyField, 4, r);

            rowControls.add(new OptionRow(opt, qtyField));
            r++;
        }

        block.getChildren().add(grid);
        return block;
    }

    private Label headerCell(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("table-header-label");
        return label;
    }

    private List<AllocationProposalDTO> optionsForItem(long mdId) {
        List<AllocationProposalDTO> result = new ArrayList<>();
        if (options == null) return result;
        for (AllocationProposalDTO opt : options) {
            if (opt.getMerchandiseDetailId() == mdId) {
                result.add(opt);
            }
        }
        return result;
    }

    private void addColumn(GridPane grid, double percent) {
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(percent);
        grid.getColumnConstraints().add(column);
    }

    private void showMessage(String text, boolean isError) {
        messageLabel.setText(text);
        messageLabel.setStyle("-fx-font-weight:bold; -fx-text-fill:" + (isError ? "#ef4444" : "#10b981") + ";");
    }

    private String getStatusLabel(String status) {
        return switch (status) {
            case "allocated", "processed" -> "Đã phân bổ";
            case "rejected" -> "Đã từ chối";
            case "processing" -> "Đang xử lý";
            default -> "Chờ xử lý";
        };
    }

    private String getStatusStyleClass(String status) {
        return switch (status) {
            case "allocated", "processed" -> "status-allocated";
            case "rejected" -> "status-rejected";
            default -> "status-pending";
        };
    }

    @FXML
    private void onAuto() {
        if (current == null) {
            return;
        }
        options = service.getAllocationOptions(currentRequestId);
        render();
        showMessage("Đã phân bổ tự động theo đề xuất của hệ thống. Bạn có thể chỉnh tay số lượng nếu cần.", false);
    }

    @FXML
    private void onConfirmAllocation() {
        if (current == null) {
            return;
        }

        List<AllocationProposalDTO> chosen = new ArrayList<>();
        for (OptionRow row : rowControls) {
            String text = row.field().getText() == null ? "" : row.field().getText().trim();
            if (text.isEmpty()) {
                continue;
            }
            int qty;
            try {
                qty = Integer.parseInt(text);
            } catch (NumberFormatException e) {
                showMessage("Số lượng không hợp lệ ở site " + row.option().getSiteName() + ".", true);
                return;
            }
            if (qty < 0) {
                showMessage("Số lượng không được âm (site " + row.option().getSiteName() + ").", true);
                return;
            }
            if (qty == 0) {
                continue;
            }
            if (qty > row.option().getAvailableQuantity()) {
                showMessage("Site " + row.option().getSiteName() + " chỉ còn "
                        + row.option().getAvailableQuantity() + " " + row.option().getUnit()
                        + ", không đủ để phân bổ " + qty + ".", true);
                return;
            }
            row.option().setAllocatedQuantity(qty);
            chosen.add(row.option());
        }

        if (chosen.isEmpty()) {
            showMessage("Chưa nhập số lượng phân bổ cho site nào.", true);
            return;
        }

        long currentUserId = 3;
        boolean success = service.confirmAllocation(currentRequestId, chosen, currentUserId);
        if (success) {
            current = current.withStatus("processed");
            options = service.getAllocationOptions(currentRequestId); // làm mới tồn kho sau khi trừ
            render();
            showMessage("Phân bổ thành công! Đơn hàng đã được tạo và tồn kho đã cập nhật.", false);
        } else {
            showMessage("Phân bổ thất bại (lỗi ghi Database). Vui lòng kiểm tra lại tồn kho.", true);
        }
    }

    @FXML
    private void onReject() {
        if (current == null) {
            return;
        }
        current = current.withStatus("rejected");
        render();
        showMessage("Đã đánh dấu từ chối yêu cầu.", true);
    }

    @FXML
    private void onBack() {
        ProcessImportRequestsUI root = new ProcessImportRequestsUI();

        Scene scene = itemsList.getScene();
        if (scene != null) {
            scene.setRoot(root);
            ((Stage) scene.getWindow()).setTitle(
                    "Hệ thống Quản lý Nhập khẩu - Xử lý Yêu cầu nhập hàng");
        }
    }

    private record OptionRow(AllocationProposalDTO option, TextField field) {
    }

    private record ImportRequestDetail(
            String code,
            String createdDate,
            String createdBy,
            String status,
            List<ImportRequestItemRow> items
    ) {
        private ImportRequestDetail withStatus(String newStatus) {
            return new ImportRequestDetail(code, createdDate, createdBy, newStatus, items);
        }
    }

    private record ImportRequestItemRow(
            String name,
            int quantity,
            String unit,
            String requiredDate,
            long merchandiseDetailId
    ) {
    }
}

package com.app.modules.procurement.importorder.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.app.modules.procurement.importorder.dto.AllocationProposalDTO;
import com.app.modules.procurement.importorder.service.ImportOrderService;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * UI Class cho màn "Xử lý yêu cầu cụ thể".
 * Kế thừa ScrollPane và nạp FXML thông qua fx:root.
 */
public class ProcessImportRequestDetailUI extends ScrollPane {

    @FXML private Label titleLabel;
    @FXML private Label statusBadge;
    @FXML private Label requestCodeLabel;
    @FXML private Label createdDateLabel;
    @FXML private Label createdByLabel;
    @FXML private Label priorityBadge;
    @FXML private Label itemCountLabel;
    @FXML private Label createdTimelineLabel;

    @FXML private VBox itemsList;

    @FXML private ComboBox<String> siteComboBox;
    @FXML private ComboBox<String> supplierComboBox;
    @FXML private TextField expectedOrderDateField;
    @FXML private TextArea noteArea;

    private final ImportOrderService service = new ImportOrderService();
    private List<AllocationProposalDTO> currentProposals = new ArrayList<>();
    private long currentRequestId;
    private ImportRequestDetail current;

    public ProcessImportRequestDetailUI() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ProcessImportRequestDetailPage.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setupFormOptions();
    }

    private void setupFormOptions() {
        siteComboBox.getItems().setAll(
                "Amazon US",
                "Alibaba CN",
                "Rakuten JP",
                "Shopee SG",
                "eBay Global"
        );

        supplierComboBox.getItems().setAll(
                "Global Supplier Co.",
                "Asia Import Partner",
                "US Wholesale Hub",
                "China Manufacturing Group",
                "Japan Trading Service"
        );

        expectedOrderDateField.setText(LocalDate.now().plusDays(2).toString());
    }

    /**
     * Nạp dữ liệu yêu cầu theo mã.
     */
    public void loadRequest(String requestCode) {
        try {
            long reqId = Long.parseLong(requestCode.replace("REQ-", "").replace("REQ", ""));
            this.currentRequestId = reqId;
            com.app.modules.procurement.importorder.repository.ImportOrderRepository.RequestSummary reqSummary = service.getRequestById(reqId);
            if (reqSummary == null) {
                System.out.println("Không tìm thấy yêu cầu: " + requestCode);
                return;
            }

            List<com.app.modules.procurement.importorder.repository.ImportOrderRepository.RequestDetailItem> dbItems = service.getRequestDetails(reqId);
            List<ImportRequestItemRow> items = new ArrayList<>();
            for (com.app.modules.procurement.importorder.repository.ImportOrderRepository.RequestDetailItem item : dbItems) {
                items.add(new ImportRequestItemRow(
                        "MD-" + item.merchandiseDetailId(),
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
                    reqSummary.priority(),
                    reqSummary.status(),
                    items
            );

            this.currentProposals = service.proposeAllocation(reqId);

            if (!currentProposals.isEmpty()) {
                siteComboBox.getSelectionModel().select(currentProposals.get(0).getSiteName());
                supplierComboBox.getSelectionModel().select("Global Supplier Co.");
            }

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
        createdTimelineLabel.setText(current.createdDate());

        statusBadge.setText(getStatusLabel(current.status()));
        statusBadge.getStyleClass().setAll(
                "status-badge",
                getStatusStyleClass(current.status())
        );

        priorityBadge.setText(getPriorityLabel(current.priority()));
        priorityBadge.getStyleClass().setAll(
                "priority-badge",
                getPriorityStyleClass(current.priority())
        );

        itemCountLabel.setText(current.items().size() + " mặt hàng");

        itemsList.getChildren().clear();
        for (ImportRequestItemRow item : current.items()) {
            itemsList.getChildren().add(buildItemRow(item));
        }
    }

    private GridPane buildItemRow(ImportRequestItemRow item) {
        GridPane row = new GridPane();
        row.getStyleClass().add("item-row");
        row.setHgap(12);
        row.setVgap(4);
        applyItemColumns(row);

        Label codeLabel = new Label(item.code());
        codeLabel.getStyleClass().add("item-code");
        row.add(codeLabel, 0, 0);

        Label nameLabel = new Label(item.name());
        nameLabel.getStyleClass().add("item-name");
        row.add(nameLabel, 1, 0);

        Label quantityLabel = new Label(String.format("%,d", item.quantity()));
        quantityLabel.getStyleClass().add("item-quantity");
        row.add(quantityLabel, 2, 0);

        Label unitLabel = new Label(item.unit());
        unitLabel.getStyleClass().add("item-muted");
        row.add(unitLabel, 3, 0);

        Label dateLabel = new Label(item.requiredDate());
        dateLabel.getStyleClass().add("item-muted");
        row.add(dateLabel, 4, 0);
        GridPane.setHalignment(dateLabel, HPos.LEFT);

        AllocationProposalDTO prop = findProposalForMerchandise(item.merchandiseDetailId());
        if (prop != null) {
            String deliveryLabelText = prop.getDeliveryType().equals("SHIP") ? "Đường biển (Tối ưu)" : "Đường hàng không";
            Label propLabel = new Label("Đề xuất phân bổ: " + prop.getSiteName() + " (" + deliveryLabelText + ") | SL: " + prop.getAllocatedQuantity() + " | Dự kiến giao: " + prop.getExpectedDeliveryDate());
            propLabel.setStyle("-fx-text-fill: #10b981; -fx-font-weight: bold; -fx-padding: 2 0 0 0;");
            row.add(propLabel, 1, 1, 4, 1);
        } else {
            Label propLabel = new Label("Đề xuất phân bổ: Không tìm thấy site phù hợp có tồn kho");
            propLabel.setStyle("-fx-text-fill: #ef4444; -fx-font-weight: bold; -fx-padding: 2 0 0 0;");
            row.add(propLabel, 1, 1, 4, 1);
        }

        return row;
    }

    private AllocationProposalDTO findProposalForMerchandise(long mdId) {
        if (currentProposals == null) return null;
        for (AllocationProposalDTO prop : currentProposals) {
            if (prop.getMerchandiseDetailId() == mdId) {
                return prop;
            }
        }
        return null;
    }

    private void applyItemColumns(GridPane grid) {
        addColumn(grid, 16);
        addColumn(grid, 34);
        addColumn(grid, 16);
        addColumn(grid, 16);
        addColumn(grid, 18);
    }

    private void addColumn(GridPane grid, double percent) {
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(percent);
        grid.getColumnConstraints().add(column);
    }

    private String getStatusLabel(String status) {
        return switch (status) {
            case "allocated" -> "Đã phân bổ";
            case "rejected" -> "Đã từ chối";
            default -> "Chờ xử lý";
        };
    }

    private String getStatusStyleClass(String status) {
        return switch (status) {
            case "allocated" -> "status-allocated";
            case "rejected" -> "status-rejected";
            default -> "status-pending";
        };
    }

    private String getPriorityLabel(String priority) {
        return switch (priority) {
            case "medium" -> "Trung bình";
            case "low" -> "Thấp";
            default -> "Cao";
        };
    }

    private String getPriorityStyleClass(String priority) {
        return switch (priority) {
            case "medium" -> "priority-medium";
            case "low" -> "priority-low";
            default -> "priority-high";
        };
    }

    @FXML
    private void onBack() {
        System.out.println("Nội dung chức năng: Quay lại màn Xử lý Yêu cầu nhập hàng");

        ProcessImportRequestsUI root = new ProcessImportRequestsUI();

        Scene scene = itemsList.getScene();
        if (scene != null) {
            scene.setRoot(root);
            ((Stage) scene.getWindow()).setTitle(
                    "Hệ thống Quản lý Nhập khẩu - Xử lý Yêu cầu nhập hàng");
        }
    }

    @FXML
    private void onReject() {
        if (current == null) {
            return;
        }

        String note = noteArea.getText() == null ? "" : noteArea.getText().trim();

        System.out.println("Nội dung chức năng: Từ chối yêu cầu "
                + current.code()
                + ", ghi chú: "
                + note);

        current = current.withStatus("rejected");
        render();
    }

    @FXML
    private void onSaveDraft() {
        if (current == null) {
            return;
        }

        System.out.println("Nội dung chức năng: Lưu nháp xử lý yêu cầu "
                + current.code()
                + ", site: "
                + siteComboBox.getValue()
                + ", supplier: "
                + supplierComboBox.getValue());
    }

    @FXML
    private void onConfirmAllocation() {
        if (current == null) {
            return;
        }

        if (currentProposals == null || currentProposals.isEmpty()) {
            System.out.println("Nội dung chức năng: Không có đề xuất phân bổ nào hợp lệ.");
            return;
        }

        System.out.println("Nội dung chức năng: Xác nhận phân bổ yêu cầu "
                + current.code()
                + " lưu vào Database...");

        long currentUserId = 3; 
        boolean success = service.confirmAllocation(currentRequestId, currentProposals, currentUserId);
        if (success) {
            System.out.println("Phân bổ thành công!");
            current = current.withStatus("allocated");
            render();
        } else {
            System.out.println("Phân bổ thất bại (Lỗi Database)!");
        }
    }

    private record ImportRequestDetail(
            String code,
            String createdDate,
            String createdBy,
            String priority,
            String status,
            List<ImportRequestItemRow> items
    ) {
        private ImportRequestDetail withStatus(String newStatus) {
            return new ImportRequestDetail(
                    code,
                    createdDate,
                    createdBy,
                    priority,
                    newStatus,
                    items
            );
        }
    }

    private record ImportRequestItemRow(
            String code,
            String name,
            int quantity,
            String unit,
            String requiredDate,
            long merchandiseDetailId
    ) {
    }
}

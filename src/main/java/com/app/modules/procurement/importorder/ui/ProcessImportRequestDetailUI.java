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
        this.current = buildMockRequest(requestCode);
        render();
    }

    private ImportRequestDetail buildMockRequest(String requestCode) {
        String createdBy = switch (requestCode) {
            case "REQ-2024-005" -> "Trần Thị Bình";
            case "REQ-2024-006" -> "Lê Quốc Huy";
            case "REQ-2024-007" -> "Phạm Minh Châu";
            case "REQ-2024-008" -> "Hoàng Hải Nam";
            default -> "Nguyễn Văn An";
        };

        String createdDate = switch (requestCode) {
            case "REQ-2024-006" -> "2024-05-09";
            case "REQ-2024-007" -> "2024-05-08";
            case "REQ-2024-008" -> "2024-05-07";
            default -> "2024-05-10";
        };

        String priority = switch (requestCode) {
            case "REQ-2024-007" -> "low";
            case "REQ-2024-005", "REQ-2024-008" -> "medium";
            default -> "high";
        };

        List<ImportRequestItemRow> items = new ArrayList<>();
        items.add(new ImportRequestItemRow(
                "MH-001",
                "Tai nghe Bluetooth chống ồn",
                120,
                "cái",
                "2024-05-20"
        ));
        items.add(new ImportRequestItemRow(
                "MH-002",
                "Bàn phím cơ RGB",
                80,
                "cái",
                "2024-05-22"
        ));
        items.add(new ImportRequestItemRow(
                "MH-003",
                "Chuột gaming không dây",
                150,
                "cái",
                "2024-05-25"
        ));
        items.add(new ImportRequestItemRow(
                "MH-004",
                "Màn hình 27 inch 2K",
                40,
                "cái",
                "2024-05-28"
        ));

        return new ImportRequestDetail(
                requestCode,
                createdDate,
                createdBy,
                priority,
                "pending",
                items
        );
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

        return row;
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

        String selectedSite = siteComboBox.getValue();
        String selectedSupplier = supplierComboBox.getValue();
        String expectedDate = expectedOrderDateField.getText();

        if (selectedSite == null || selectedSite.isBlank()) {
            System.out.println("Nội dung chức năng: Vui lòng chọn site nhận đơn");
            return;
        }

        if (selectedSupplier == null || selectedSupplier.isBlank()) {
            System.out.println("Nội dung chức năng: Vui lòng chọn nhà cung cấp");
            return;
        }

        System.out.println("Nội dung chức năng: Xác nhận phân bổ yêu cầu "
                + current.code()
                + " đến site "
                + selectedSite
                + ", nhà cung cấp "
                + selectedSupplier
                + ", ngày dự kiến "
                + expectedDate);

        current = current.withStatus("allocated");
        render();
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
            String requiredDate
    ) {
    }
}

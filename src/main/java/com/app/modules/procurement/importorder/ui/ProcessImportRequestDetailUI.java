package com.app.modules.procurement.importorder.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.app.database.manager.DatabaseManager;
import com.app.modules.procurement.importorder.dto.AllocationProposalDTO;
import com.app.modules.procurement.importorder.service.ImportOrderService;
import com.app.modules.procurement.importorder.repository.ImportOrderRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * UI Class cho màn "Xử lý yêu cầu cụ thể".
 * Kế thừa StackPane để hỗ trợ hiện Dialog Overlay.
 */
public class ProcessImportRequestDetailUI extends StackPane {

    @FXML private StackPane pageRoot;
    @FXML private Label titleLabel;
    @FXML private Label priorityBadge;
    @FXML private ProgressBar overallProgressBarTop;
    @FXML private Label overallProgressLabelTop;
    
    @FXML private Label totalItemsLabel;
    @FXML private Label fullyAllocatedLabel;
    @FXML private Label partiallyAllocatedLabel;
    @FXML private Label unallocatedLabel;

    @FXML private VBox itemsList;

    @FXML private ProgressBar overallProgressBarSide;
    @FXML private Label overallProgressLabelSide;
    @FXML private Label totalItemsSummaryLabel;
    @FXML private Label fullyAllocatedSummaryLabel;
    @FXML private Label partiallyAllocatedSummaryLabel;
    @FXML private Label unallocatedSummaryLabel;
    @FXML private Label selectedSitesCountLabel;

    @FXML private StackPane dialogOverlay;

    private final ImportOrderService service = new ImportOrderService();
    private final ImportOrderRepository repository = new ImportOrderRepository();
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
    }

    /**
     * Nạp dữ liệu yêu cầu theo mã.
     */
    public void loadRequest(String requestCode) {
        try {
            long reqId = Long.parseLong(requestCode.replace("REQ-", "").replace("REQ", ""));
            this.currentRequestId = reqId;
            ImportOrderRepository.RequestSummary reqSummary = service.getRequestById(reqId);
            if (reqSummary == null) {
                System.out.println("Không tìm thấy yêu cầu: " + requestCode);
                return;
            }

            List<ImportOrderRepository.RequestDetailItem> dbItems = service.getRequestDetails(reqId);
            List<ImportRequestItemRow> items = new ArrayList<>();
            for (ImportOrderRepository.RequestDetailItem item : dbItems) {
                items.add(new ImportRequestItemRow(
                        "MH" + String.format("%03d", item.merchandiseDetailId()),
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

            render();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void render() {
        if (current == null) {
            return;
        }

        titleLabel.setText("Xử lý Yêu cầu: " + current.code());
        
        // Priority label and class
        priorityBadge.setText("Độ ưu tiên: " + getPriorityLabel(current.priority()));
        priorityBadge.getStyleClass().setAll(
                "priority-badge",
                getPriorityStyleClass(current.priority())
        );

        // Stats calculation
        int totalItems = current.items().size();
        int fullyAllocated = 0;
        int partiallyAllocated = 0;
        int unallocated = 0;

        for (ImportRequestItemRow item : current.items()) {
            int allocated = 0;
            for (AllocationProposalDTO prop : currentProposals) {
                if (prop.getMerchandiseDetailId() == item.merchandiseDetailId()) {
                    allocated += prop.getAllocatedQuantity();
                }
            }

            if (allocated >= item.quantity()) {
                fullyAllocated++;
            } else if (allocated > 0) {
                partiallyAllocated++;
            } else {
                unallocated++;
            }
        }

        // Set top stats labels
        totalItemsLabel.setText(String.valueOf(totalItems));
        fullyAllocatedLabel.setText(String.valueOf(fullyAllocated));
        partiallyAllocatedLabel.setText(String.valueOf(partiallyAllocated));
        unallocatedLabel.setText(String.valueOf(unallocated));

        // Set side stats labels
        totalItemsSummaryLabel.setText(String.valueOf(totalItems));
        fullyAllocatedSummaryLabel.setText(String.valueOf(fullyAllocated));
        partiallyAllocatedSummaryLabel.setText(String.valueOf(partiallyAllocated));
        unallocatedSummaryLabel.setText(String.valueOf(unallocated));

        // Progress percentage
        int progress = totalItems > 0 ? (fullyAllocated * 100) / totalItems : 0;
        overallProgressLabelTop.setText(progress + "%");
        overallProgressBarTop.setProgress(progress / 100.0);
        overallProgressLabelSide.setText(progress + "%");
        overallProgressBarSide.setProgress(progress / 100.0);

        // Unique Sites Count
        Set<Long> uniqueSiteIds = new HashSet<>();
        for (AllocationProposalDTO prop : currentProposals) {
            if (prop.getAllocatedQuantity() > 0) {
                uniqueSiteIds.add(prop.getSiteId());
            }
        }
        selectedSitesCountLabel.setText(uniqueSiteIds.size() + " Site");

        // Render item cards
        itemsList.getChildren().clear();
        for (int i = 0; i < current.items().size(); i++) {
            itemsList.getChildren().add(buildItemCard(current.items().get(i), i + 1));
        }
    }

    private VBox buildItemCard(ImportRequestItemRow item, int index) {
        VBox card = new VBox();
        card.getStyleClass().add("item-card");
        card.setSpacing(0);

        // Card Header
        HBox header = new HBox();
        header.getStyleClass().add("item-card-header");
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(10);

        Label idxLabel = new Label("#" + index);
        idxLabel.getStyleClass().add("item-index-label");

        Label nameLabel = new Label(item.name());
        nameLabel.getStyleClass().add("item-name-bold");

        Label codeTag = new Label(item.code());
        codeTag.getStyleClass().add("item-code-tag");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Calculate allocated quantity
        int allocated = 0;
        for (AllocationProposalDTO prop : currentProposals) {
            if (prop.getMerchandiseDetailId() == item.merchandiseDetailId()) {
                allocated += prop.getAllocatedQuantity();
            }
        }

        Label statusTag = new Label();
        if (allocated >= item.quantity()) {
            statusTag.setText("✔ Đã phân bổ đủ");
            statusTag.getStyleClass().setAll("status-badge", "status-allocated");
        } else if (allocated > 0) {
            statusTag.setText("⏳ Phân bổ 1 phần");
            statusTag.getStyleClass().setAll("status-badge", "status-pending");
        } else {
            statusTag.setText("⚠ Chưa phân bổ");
            statusTag.getStyleClass().setAll("status-badge", "status-rejected");
        }

        header.getChildren().addAll(idxLabel, nameLabel, codeTag, spacer, statusTag);

        // Card Body
        VBox body = new VBox();
        body.getStyleClass().add("item-card-body");
        body.setSpacing(14);

        // Meta info (Yêu cầu, ngày nhận)
        HBox metaBox = new HBox();
        metaBox.setSpacing(24);
        metaBox.setAlignment(Pos.CENTER_LEFT);

        Label reqInfo = new Label();
        reqInfo.getStyleClass().add("item-meta-label");
        reqInfo.setGraphic(new Label("📦 "));
        reqInfo.setText("Yêu cầu: ");
        Label reqVal = new Label(item.quantity() + " " + item.unit());
        reqVal.getStyleClass().add("item-meta-value");
        HBox reqContainer = new HBox(reqInfo, reqVal);
        reqContainer.setAlignment(Pos.CENTER_LEFT);

        Label dateInfo = new Label();
        dateInfo.getStyleClass().add("item-meta-label");
        dateInfo.setGraphic(new Label("⏳ "));
        dateInfo.setText("Ngày nhận: ");
        Label dateVal = new Label(item.requiredDate());
        dateVal.getStyleClass().add("item-meta-value");
        HBox dateContainer = new HBox(dateInfo, dateVal);
        dateContainer.setAlignment(Pos.CENTER_LEFT);

        metaBox.getChildren().addAll(reqContainer, dateContainer);

        // Progress section
        VBox progressBox = new VBox();
        progressBox.setSpacing(6);

        HBox progHeader = new HBox();
        progHeader.setAlignment(Pos.CENTER_LEFT);

        Label progText = new Label("Đã phân bổ: " + allocated + "/" + item.quantity() + " " + item.unit());
        progText.getStyleClass().add("item-progress-text");

        Region progSpacer = new Region();
        HBox.setHgrow(progSpacer, Priority.ALWAYS);

        int pct = item.quantity() > 0 ? (allocated * 100) / item.quantity() : 0;
        Label pctText = new Label(pct + "%");
        pctText.getStyleClass().add("item-progress-percent");

        progHeader.getChildren().addAll(progText, progSpacer, pctText);

        ProgressBar bar = new ProgressBar(allocated * 1.0 / item.quantity());
        bar.setMaxWidth(Double.MAX_VALUE);
        if (allocated >= item.quantity()) {
            bar.getStyleClass().setAll("progress-bar", "progress-bar-green");
        } else if (allocated > 0) {
            bar.getStyleClass().setAll("progress-bar", "progress-bar-yellow");
        } else {
            bar.getStyleClass().setAll("progress-bar", "progress-bar-red");
        }

        progressBox.getChildren().addAll(progHeader, bar);

        // Allocations list box
        VBox allocationsBox = new VBox();
        allocationsBox.setSpacing(8);

        List<AllocationProposalDTO> itemAllocations = new ArrayList<>();
        for (AllocationProposalDTO prop : currentProposals) {
            if (prop.getMerchandiseDetailId() == item.merchandiseDetailId() && prop.getAllocatedQuantity() > 0) {
                itemAllocations.add(prop);
            }
        }

        if (itemAllocations.isEmpty()) {
            VBox noBox = new VBox();
            noBox.getStyleClass().add("no-allocations-box");
            noBox.setAlignment(Pos.CENTER);
            Label noTxt = new Label("Chưa có phân bổ nào cho mặt hàng này");
            noTxt.getStyleClass().add("no-allocations-text");
            noBox.getChildren().add(noTxt);
            allocationsBox.getChildren().add(noBox);
        } else {
            Label allocTitle = new Label("Đã phân bổ cho " + itemAllocations.size() + " Site:");
            allocTitle.getStyleClass().add("allocations-title");
            allocationsBox.getChildren().add(allocTitle);

            for (AllocationProposalDTO alloc : itemAllocations) {
                HBox row = new HBox();
                row.getStyleClass().add("allocation-row");
                row.setAlignment(Pos.CENTER_LEFT);

                Label sName = new Label(alloc.getSiteName());
                sName.getStyleClass().add("allocation-site-name");

                Region rSpacer = new Region();
                HBox.setHgrow(rSpacer, Priority.ALWAYS);

                Label sQty = new Label(alloc.getAllocatedQuantity() + " " + item.unit());
                sQty.getStyleClass().add("allocation-qty");

                row.getChildren().addAll(sName, rSpacer, sQty);
                allocationsBox.getChildren().add(row);
            }
        }

        // Action Button
        Button allocateBtn = new Button();
        allocateBtn.getStyleClass().add("allocate-btn-blue");
        allocateBtn.setMaxWidth(Double.MAX_VALUE);
        allocateBtn.setText("Phân bổ Site");
        allocateBtn.setOnAction(e -> openAllocationDialog(item));

        body.getChildren().addAll(metaBox, progressBox, allocationsBox, allocateBtn);
        card.getChildren().addAll(header, body);

        return card;
    }

    private void openAllocationDialog(ImportRequestItemRow item) {
        dialogOverlay.getChildren().clear();
        
        VBox dialogCard = new VBox();
        dialogCard.getStyleClass().add("dialog-card");
        dialogCard.setSpacing(0);

        // Header
        HBox header = new HBox();
        header.getStyleClass().add("dialog-header");
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(20);

        VBox titleBox = new VBox();
        titleBox.setSpacing(4);
        Label title = new Label("Phân bổ Site");
        title.getStyleClass().add("dialog-title-bold");
        Label subtitle = new Label(item.name());
        subtitle.getStyleClass().add("dialog-subtitle-light");
        titleBox.getChildren().addAll(title, subtitle);

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        Button closeBtn = new Button("×");
        closeBtn.getStyleClass().add("dialog-close-btn");
        closeBtn.setOnAction(e -> closeAllocationDialog());

        header.getChildren().addAll(titleBox, headerSpacer, closeBtn);

        // Stats summary at the top of dialog body
        HBox statsSummary = new HBox();
        statsSummary.setSpacing(14);
        statsSummary.setPadding(new Insets(16, 20, 0, 20));

        VBox stat1 = new VBox();
        stat1.getStyleClass().add("dialog-header-stat-box");
        stat1.setStyle("-fx-background-color: #EFF6FF; -fx-border-color: #BFDBFE; -fx-border-width: 1px; -fx-background-radius: 8px; -fx-border-radius: 8px;");
        Label stat1Title = new Label("YÊU CẦU");
        stat1Title.getStyleClass().add("dialog-header-stat-title");
        stat1Title.setStyle("-fx-text-fill: #1E3A8A;");
        Label stat1Val = new Label(item.quantity() + " " + item.unit());
        stat1Val.getStyleClass().add("dialog-header-stat-value");
        stat1Val.setStyle("-fx-text-fill: #2563EB; -fx-font-size: 16px;");
        stat1.getChildren().addAll(stat1Title, stat1Val);
        HBox.setHgrow(stat1, Priority.ALWAYS);

        VBox stat2 = new VBox();
        stat2.getStyleClass().add("dialog-header-stat-box");
        stat2.setStyle("-fx-background-color: #F0FDF4; -fx-border-color: #BBF7D0; -fx-border-width: 1px; -fx-background-radius: 8px; -fx-border-radius: 8px;");
        Label stat2Title = new Label("ĐÃ PHÂN BỔ");
        stat2Title.getStyleClass().add("dialog-header-stat-title");
        stat2Title.setStyle("-fx-text-fill: #14532D;");
        
        int currentAlloc = 0;
        for (AllocationProposalDTO prop : currentProposals) {
            if (prop.getMerchandiseDetailId() == item.merchandiseDetailId()) {
                currentAlloc += prop.getAllocatedQuantity();
            }
        }
        Label stat2Val = new Label(currentAlloc + " " + item.unit());
        stat2Val.getStyleClass().add("dialog-header-stat-value");
        stat2Val.setStyle("-fx-text-fill: #16A34A; -fx-font-size: 16px;");
        stat2.getChildren().addAll(stat2Title, stat2Val);
        HBox.setHgrow(stat2, Priority.ALWAYS);

        VBox stat3 = new VBox();
        stat3.getStyleClass().add("dialog-header-stat-box");
        stat3.setStyle("-fx-background-color: #FEF2F2; -fx-border-color: #FCA5A5; -fx-border-width: 1px; -fx-background-radius: 8px; -fx-border-radius: 8px;");
        Label stat3Title = new Label("CÒN LẠI");
        stat3Title.getStyleClass().add("dialog-header-stat-title");
        stat3Title.setStyle("-fx-text-fill: #7F1D1D;");
        Label stat3Val = new Label(Math.max(0, item.quantity() - currentAlloc) + " " + item.unit());
        stat3Val.getStyleClass().add("dialog-header-stat-value");
        stat3Val.setStyle("-fx-text-fill: #DC2626; -fx-font-size: 16px;");
        stat3.getChildren().addAll(stat3Title, stat3Val);
        HBox.setHgrow(stat3, Priority.ALWAYS);

        statsSummary.getChildren().addAll(stat1, stat2, stat3);

        // Body ScrollPane
        ScrollPane bodyScroll = new ScrollPane();
        bodyScroll.setFitToWidth(true);
        bodyScroll.getStyleClass().add("screen-scroll");
        VBox.setVgrow(bodyScroll, Priority.ALWAYS);

        VBox body = new VBox();
        body.getStyleClass().add("dialog-body");
        body.setSpacing(14);
        bodyScroll.setContent(body);

        List<ImportOrderRepository.SiteInventoryInfo> sites = repository.getSitesInventory(item.merchandiseDetailId());

        Label bodyTitle = new Label("Tìm thấy " + sites.size() + " Site có sẵn");
        bodyTitle.getStyleClass().add("dialog-body-title");
        Label bodySubtitle = new Label("Chọn số lượng cần phân bổ cho mỗi Site");
        bodySubtitle.getStyleClass().add("dialog-body-subtitle");
        body.getChildren().addAll(new VBox(2, bodyTitle, bodySubtitle));

        List<TextField> qtyFields = new ArrayList<>();

        for (ImportOrderRepository.SiteInventoryInfo site : sites) {
            int allocatedVal = 0;
            for (AllocationProposalDTO prop : currentProposals) {
                if (prop.getMerchandiseDetailId() == item.merchandiseDetailId() && prop.getSiteId() == site.siteId()) {
                    allocatedVal = prop.getAllocatedQuantity();
                }
            }

            VBox siteCard = new VBox();
            siteCard.getStyleClass().add("site-allocation-card");
            if (allocatedVal > 0) {
                siteCard.getStyleClass().add("site-allocation-card-selected");
            }
            siteCard.setSpacing(10);

            // Card Header
            HBox scHeader = new HBox();
            scHeader.setAlignment(Pos.CENTER_LEFT);
            scHeader.setSpacing(10);

            Label scName = new Label(site.siteName());
            scName.getStyleClass().add("site-name-bold");

            int score = 100 - (int) site.siteDistance();
            Label scScore = new Label(score + "/100");
            scScore.getStyleClass().add("site-score-badge");

            Region scSpacer = new Region();
            HBox.setHgrow(scSpacer, Priority.ALWAYS);

            Label scSelected = new Label("✔ Đã chọn");
            scSelected.getStyleClass().add("site-selected-badge");
            scSelected.setVisible(allocatedVal > 0);

            scHeader.getChildren().addAll(scName, scScore, scSpacer, scSelected);

            // Card Details Grid
            GridPane grid = new GridPane();
            grid.setHgap(20);
            grid.setVgap(6);

            SiteExtraInfo extra = getSiteExtraInfo(site.siteId());

            grid.add(pair("Địa chỉ", extra.address()), 0, 0);
            grid.add(pair("Người liên hệ", extra.moreInfo()), 1, 0);
            
            long shippingCost = (long) (site.siteDistance() * 20000);
            grid.add(pair("Phí vận chuyển", String.format("%,d VND", shippingCost)), 0, 1);

            LocalDate today = LocalDate.now();
            LocalDate desiredLocalDate = LocalDate.now().plusDays(30);
            try {
                desiredLocalDate = LocalDate.parse(item.requiredDate());
            } catch (Exception ex) {}
            boolean shipOk = today.plusDays(site.deliveryByShip()).isBefore(desiredLocalDate) || today.plusDays(site.deliveryByShip()).isEqual(desiredLocalDate);
            long deliveryDays = shipOk ? site.deliveryByShip() : site.deliveryByAir();
            grid.add(pair("Giao hàng dự kiến", today.plusDays(deliveryDays).toString() + " (" + (shipOk ? "Đường biển" : "Hàng không") + ")"), 1, 1);

            // Stock details
            HBox stockBox = new HBox(8);
            stockBox.setAlignment(Pos.CENTER_LEFT);
            Label stockLbl = new Label("Tồn kho tại Site:");
            stockLbl.getStyleClass().add("site-stock-level-label");
            Label stockVal = new Label(site.availableQuantity() + " " + item.unit() + "  |  Đơn giá: " + String.format("%,.0f VND", site.price()));
            stockVal.getStyleClass().add("site-stock-level-val");
            stockBox.getChildren().addAll(stockLbl, stockVal);

            // Qty Input row
            HBox inputRow = new HBox(12);
            inputRow.setAlignment(Pos.CENTER_LEFT);
            Label inputLbl = new Label("Số lượng phân bổ từ Site này:");
            inputLbl.getStyleClass().add("detail-label-muted");
            inputLbl.setStyle("-fx-font-weight: bold;");

            Region inputSpacer = new Region();
            HBox.setHgrow(inputSpacer, Priority.ALWAYS);

            Button minusBtn = new Button("-");
            minusBtn.getStyleClass().add("qty-btn-circle");

            TextField qtyField = new TextField(String.valueOf(allocatedVal));
            qtyField.getStyleClass().add("qty-input-field");
            qtyFields.add(qtyField);

            Button plusBtn = new Button("+");
            plusBtn.getStyleClass().add("qty-btn-circle");

            Label maxLbl = new Label("/ " + site.availableQuantity() + " " + item.unit());
            maxLbl.getStyleClass().add("detail-label-muted");

            HBox qtyContainer = new HBox(6, minusBtn, qtyField, plusBtn, maxLbl);
            qtyContainer.setAlignment(Pos.CENTER_LEFT);

            inputRow.getChildren().addAll(inputLbl, inputSpacer, qtyContainer);

            // Listeners for buttons and text changes
            final int maxStock = site.availableQuantity();
            minusBtn.setOnAction(e -> {
                try {
                    int val = Integer.parseInt(qtyField.getText().trim());
                    if (val > 0) {
                        qtyField.setText(String.valueOf(val - 1));
                    }
                } catch (NumberFormatException ex) {
                    qtyField.setText("0");
                }
            });

            plusBtn.setOnAction(e -> {
                try {
                    int val = Integer.parseInt(qtyField.getText().trim());
                    if (val < maxStock) {
                        qtyField.setText(String.valueOf(val + 1));
                    }
                } catch (NumberFormatException ex) {
                    qtyField.setText("0");
                }
            });

            qtyField.textProperty().addListener((obs, oldV, newV) -> {
                String clean = newV.replaceAll("[^0-9]", "");
                if (!newV.equals(clean)) {
                    qtyField.setText(clean);
                    return;
                }
                int val = 0;
                if (!clean.isEmpty()) {
                    try {
                        val = Integer.parseInt(clean);
                        if (val > maxStock) {
                            val = maxStock;
                            qtyField.setText(String.valueOf(val));
                        }
                    } catch (NumberFormatException ex) {
                        qtyField.setText("0");
                    }
                }

                // Update selected styles and badges
                scSelected.setVisible(val > 0);
                if (val > 0) {
                    if (!siteCard.getStyleClass().contains("site-allocation-card-selected")) {
                        siteCard.getStyleClass().add("site-allocation-card-selected");
                    }
                } else {
                    siteCard.getStyleClass().remove("site-allocation-card-selected");
                }

                // Update dynamic stats header
                updateDialogStats(stat2Val, stat3Val, item.quantity(), qtyFields, item.unit());
            });

            siteCard.getChildren().addAll(scHeader, grid, stockBox, inputRow);
            body.getChildren().add(siteCard);
        }

        // Footer
        HBox footer = new HBox();
        footer.getStyleClass().add("dialog-footer");
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setSpacing(12);

        Button cancelBtn = new Button("Hủy");
        cancelBtn.getStyleClass().add("cancel-btn-outline");
        cancelBtn.setOnAction(e -> closeAllocationDialog());

        Button saveBtn = new Button("Lưu phân bổ");
        saveBtn.getStyleClass().add("save-btn-blue");
        saveBtn.setOnAction(e -> {
            // Update Proposals in memory
            // 1. Remove old proposals for this item
            currentProposals.removeIf(prop -> prop.getMerchandiseDetailId() == item.merchandiseDetailId());

            // 2. Add new ones
            for (int i = 0; i < sites.size(); i++) {
                ImportOrderRepository.SiteInventoryInfo site = sites.get(i);
                TextField f = qtyFields.get(i);
                int qty = 0;
                try {
                    qty = Integer.parseInt(f.getText().trim());
                } catch (NumberFormatException ex) {}

                if (qty > 0) {
                    LocalDate today = LocalDate.now();
                    LocalDate desiredLocalDate = LocalDate.now().plusDays(30);
                    try {
                        desiredLocalDate = LocalDate.parse(item.requiredDate());
                    } catch (Exception ex) {}
                    boolean shipOk = today.plusDays(site.deliveryByShip()).isBefore(desiredLocalDate) || today.plusDays(site.deliveryByShip()).isEqual(desiredLocalDate);
                    String delivery = shipOk ? "SHIP" : "AIR";
                    long deliveryDays = shipOk ? site.deliveryByShip() : site.deliveryByAir();
                    String expectedDate = today.plusDays(deliveryDays).toString();

                    currentProposals.add(new AllocationProposalDTO(
                            site.siteId(),
                            site.siteName(),
                            item.merchandiseDetailId(),
                            item.name(),
                            item.unit(),
                            qty,
                            delivery,
                            expectedDate,
                            site.price()
                    ));
                }
            }

            // Re-render main detail screen
            render();
            closeAllocationDialog();
        });

        footer.getChildren().addAll(cancelBtn, saveBtn);

        dialogCard.getChildren().addAll(header, statsSummary, bodyScroll, footer);
        dialogOverlay.getChildren().setAll(dialogCard);

        dialogOverlay.setVisible(true);
        dialogOverlay.setManaged(true);
    }

    private void updateDialogStats(Label allocValLabel, Label remValLabel, int itemQty, List<TextField> qtyFields, String unit) {
        int totalAlloc = 0;
        for (TextField f : qtyFields) {
            try {
                String txt = f.getText().trim();
                if (!txt.isEmpty()) {
                    totalAlloc += Integer.parseInt(txt);
                }
            } catch (NumberFormatException e) {
                // Ignore
            }
        }
        allocValLabel.setText(totalAlloc + " " + unit);
        remValLabel.setText(Math.max(0, itemQty - totalAlloc) + " " + unit);
    }

    private void closeAllocationDialog() {
        dialogOverlay.getChildren().clear();
        dialogOverlay.setVisible(false);
        dialogOverlay.setManaged(false);
    }

    private VBox pair(String label, String value) {
        Label l = new Label(label + ": ");
        l.getStyleClass().add("detail-label-muted");
        Label v = new Label(value);
        v.getStyleClass().add("detail-value-dark");
        HBox box = new HBox(l, v);
        box.setAlignment(Pos.CENTER_LEFT);
        return new VBox(box);
    }

    private SiteExtraInfo getSiteExtraInfo(long siteId) {
        String sql = "SELECT site_address, more_info FROM \"Site\" WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, siteId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String addr = rs.getString("site_address");
                    String info = rs.getString("more_info");
                    return new SiteExtraInfo(
                        addr != null ? addr : "Không có thông tin địa chỉ",
                        info != null ? info : "Không có thông tin liên hệ"
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new SiteExtraInfo("Không xác định", "Không có thông tin");
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

        Scene scene = this.getScene();
        if (scene != null) {
            javafx.scene.Parent parentRoot = scene.getRoot();
            if (parentRoot instanceof com.app.common.ui.MainLayoutUI) {
                ((com.app.common.ui.MainLayoutUI) parentRoot).setPage(root);
            } else {
                scene.setRoot(root);
            }
            ((Stage) scene.getWindow()).setTitle(
                    "Hệ thống Quản lý Nhập khẩu - Xử lý Yêu cầu nhập hàng");
        }
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
            
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Thành công");
            alert.setHeaderText(null);
            alert.setContentText("Đã gửi đơn hàng thành công đến các Site!");
            alert.showAndWait();
            
            onBack();
        } else {
            System.out.println("Phân bổ thất bại (Lỗi Database)!");
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Có lỗi xảy ra khi tạo đơn hàng. Vui lòng kiểm tra lại kết nối Database!");
            alert.showAndWait();
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

    private record SiteExtraInfo(String address, String moreInfo) {}
}

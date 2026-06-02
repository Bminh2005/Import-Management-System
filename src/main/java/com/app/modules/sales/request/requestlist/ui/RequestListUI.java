package com.app.modules.sales.request.requestlist.ui;

import com.app.common.util.FxmlUiHelper;
import com.app.common.util.StatusStyle;
import com.app.modules.sales.request.requestdetail.dto.RequestListRow;
import com.app.modules.sales.request.requestdetail.service.RequestService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Danh sách yêu cầu nhập hàng — dữ liệu từ Supabase (ImportRequest).
 */
public class RequestListUI extends VBox {

    private static final int PAGE_SIZE = 5;

    @FXML private TextField txtSearch;
    @FXML private HBox filterBar;
    @FXML private TableView<RequestListRow> tableRequests;
    @FXML private TableColumn<RequestListRow, String> colId;
    @FXML private TableColumn<RequestListRow, String> colDate;
    @FXML private TableColumn<RequestListRow, Integer> colQuantity;
    @FXML private TableColumn<RequestListRow, String> colStatus;
    @FXML private TableColumn<RequestListRow, Void> colActions;
    @FXML private Label paginationInfoLabel;
    @FXML private Button btnPrev;
    @FXML private Button btnNext;
    @FXML private HBox pageButtonsBox;

    private final RequestService service;
    private ObservableList<RequestListRow> allRows = FXCollections.observableArrayList();
    private final ToggleGroup filterGroup = new ToggleGroup();
    /** null = tất cả; còn lại: draft, pending, processing, completed, cancelled */
    private String statusFilter = null;
    private int currentPage = 0;

    private static final String ACTION_ICON_COLOR = "#475569";

    private Consumer<String> onViewDetail;
    private Consumer<String> onEditRequest;

    public RequestListUI() {
        this(new RequestService());
    }

    public RequestListUI(RequestService service) {
        this.service = service;
        FxmlUiHelper.loadSelf(this, "/com/app/modules/sales/request/ui/RequestListPage.fxml");
        setupTable();
        txtSearch.textProperty().addListener((o, a, b) -> {
            currentPage = 0;
            refreshView();
        });
        reload();
    }

    public void setOnViewDetail(Consumer<String> callback) {
        this.onViewDetail = callback;
    }

    public void setOnEditRequest(Consumer<String> callback) {
        this.onEditRequest = callback;
    }

    public void reload() {
        try {
            allRows.setAll(service.listRequests());
            rebuildFilterPills();
            currentPage = 0;
            refreshView();
        } catch (Exception ex) {
            allRows.clear();
            rebuildFilterPills();
            refreshView();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Danh sách yêu cầu");
            alert.setHeaderText("Không tải được dữ liệu");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }

    private void rebuildFilterPills() {
        filterBar.getChildren().clear();
        Map<String, Integer> counts = countByStatus();

        addFilterPill("Tất cả", null, counts.get("all"), true);
        addFilterPill("Bản nháp", "draft", counts.get("draft"), false);
        addFilterPill("Chờ xử lý", "pending", counts.get("pending"), false);
        addFilterPill("Đang xử lý", "processing", counts.get("processing"), false);
        addFilterPill("Hoàn thành", "completed", counts.get("completed"), false);
        addFilterPill("Đã hủy", "cancelled", counts.get("cancelled"), false);
    }

    private Map<String, Integer> countByStatus() {
        Map<String, Integer> counts = new LinkedHashMap<>();
        counts.put("all", allRows.size());
        counts.put("draft", 0);
        counts.put("pending", 0);
        counts.put("processing", 0);
        counts.put("completed", 0);
        counts.put("cancelled", 0);
        for (RequestListRow row : allRows) {
            String s = row.getStatus();
            if (counts.containsKey(s)) {
                counts.merge(s, 1, Integer::sum);
            }
        }
        return counts;
    }

    private void addFilterPill(String label, String statusToken, int count, boolean selected) {
        ToggleButton pill = new ToggleButton(label + " (" + count + ")");
        pill.getStyleClass().add("filter-pill");
        pill.setToggleGroup(filterGroup);
        pill.setSelected(selected);
        pill.setUserData(statusToken);
        pill.setOnAction(e -> {
            statusFilter = (String) pill.getUserData();
            currentPage = 0;
            refreshView();
        });
        filterBar.getChildren().add(pill);
        if (selected) {
            statusFilter = statusToken;
        }
    }

    private List<RequestListRow> getFilteredRows() {
        String q = txtSearch.getText() == null ? "" : txtSearch.getText().trim().toLowerCase();
        List<RequestListRow> result = new ArrayList<>();
        for (RequestListRow row : allRows) {
            if (statusFilter != null && !statusFilter.equals(row.getStatus())) {
                continue;
            }
            if (!q.isEmpty() && !row.getCode().toLowerCase().contains(q)) {
                continue;
            }
            result.add(row);
        }
        return result;
    }

    private void refreshView() {
        List<RequestListRow> filtered = getFilteredRows();
        int total = filtered.size();
        int totalPages = total == 0 ? 1 : (int) Math.ceil((double) total / PAGE_SIZE);
        if (currentPage >= totalPages) {
            currentPage = Math.max(0, totalPages - 1);
        }
        int from = currentPage * PAGE_SIZE;
        int to = Math.min(from + PAGE_SIZE, total);

        if (total == 0) {
            tableRequests.setItems(FXCollections.observableArrayList());
            paginationInfoLabel.setText("Không có yêu cầu phù hợp");
        } else {
            tableRequests.setItems(FXCollections.observableArrayList(filtered.subList(from, to)));
            paginationInfoLabel.setText(
                    "Hiển thị " + (from + 1) + "–" + to + " trong " + total + " yêu cầu");
        }

        btnPrev.setDisable(currentPage <= 0);
        btnNext.setDisable(currentPage >= totalPages - 1 || total == 0);
        rebuildPageButtons(totalPages);
    }

    private void rebuildPageButtons(int totalPages) {
        pageButtonsBox.getChildren().clear();
        for (int i = 0; i < totalPages; i++) {
            final int pageIndex = i;
            Button pageBtn = new Button(String.valueOf(i + 1));
            pageBtn.getStyleClass().add("btn-page");
            if (i == currentPage) {
                pageBtn.getStyleClass().add("page-active");
            }
            pageBtn.setOnAction(e -> {
                currentPage = pageIndex;
                refreshView();
            });
            pageButtonsBox.getChildren().add(pageBtn);
        }
    }

    @FXML
    private void onPrevPage() {
        if (currentPage > 0) {
            currentPage--;
            refreshView();
        }
    }

    @FXML
    private void onNextPage() {
        currentPage++;
        refreshView();
    }

    private void setupTable() {
        colId.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCode()));
        colDate.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCreatedDate()));
        colQuantity.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getItemCount()));
        colQuantity.setCellFactory(col -> new TableCell<RequestListRow, Integer>() {
            private final Label badge = new Label();

            {
                badge.getStyleClass().add("quantity-badge");
                setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Integer count, boolean empty) {
                super.updateItem(count, empty);
                if (empty || count == null) {
                    setGraphic(null);
                } else {
                    badge.setText(String.valueOf(count));
                    setGraphic(badge);
                }
            }
        });
        colStatus.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getStatus()));
        colStatus.setCellFactory(StatusStyle.badgeCellFactory(StatusStyle::requestStatusLabel));

        colActions.getStyleClass().add("actions-column-header");
        colActions.setStyle("-fx-alignment: CENTER;");
        colActions.setCellFactory(col -> new TableCell<>() {
            private final Button viewBtn = iconButton(eyePath(), ACTION_ICON_COLOR);
            private final Button editBtn = iconButton(editPath(), ACTION_ICON_COLOR);
            private final HBox box = new HBox(6);

            {
                getStyleClass().add("actions-cell");
                viewBtn.getStyleClass().add("icon-btn");
                editBtn.getStyleClass().add("icon-btn");
                box.setAlignment(Pos.CENTER);
                Tooltip.install(viewBtn, new Tooltip("Xem chi tiết yêu cầu"));
                Tooltip.install(editBtn, new Tooltip("Chỉnh sửa yêu cầu"));
                viewBtn.setOnAction(e -> {
                    RequestListRow row = getTableView().getItems().get(getIndex());
                    if (row != null) {
                        openDetail(row.getCode());
                    }
                });
                editBtn.setOnAction(e -> {
                    RequestListRow row = getTableView().getItems().get(getIndex());
                    if (row != null) {
                        openEdit(row.getCode());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }
                RequestListRow row = getTableView().getItems().get(getIndex());
                box.getChildren().clear();
                if (row != null) {
                    box.getChildren().add(viewBtn);
                    if (!"completed".equals(row.getStatus())) {
                        box.getChildren().add(editBtn);
                    }
                }
                setGraphic(box);
                setAlignment(Pos.CENTER);
            }
        });

        tableRequests.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                RequestListRow row = tableRequests.getSelectionModel().getSelectedItem();
                if (row != null) {
                    openDetail(row.getCode());
                }
            }
        });
    }

    private void openDetail(String code) {
        if (onViewDetail != null) {
            onViewDetail.accept(code);
        }
    }

    private void openEdit(String code) {
        if (onEditRequest != null) {
            onEditRequest.accept(code);
        }
    }

    private static Button iconButton(String svg, String fill) {
        SVGPath icon = new SVGPath();
        icon.setContent(svg);
        icon.setStyle("-fx-fill: " + fill + ";");
        icon.setScaleX(0.85);
        icon.setScaleY(0.85);
        Button btn = new Button();
        btn.setGraphic(icon);
        return btn;
    }

    private static String eyePath() {
        return "M12 4.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5"
                + "C21.27 7.61 17 4.5 12 4.5zM12 17a5 5 0 1 1 0-10 5 5 0 0 1 0 10zm0-8a3 3 0 1 0 0 6 3 3 0 0 0 0-6z";
    }

    /** Icon chỉnh sửa (ô vuông + bút) — cùng tông màu với icon mắt. */
    private static String editPath() {
        return "M4 4h6v2H6v10h10v-4h2v6H4V4zm12.5-1.5l1.5 1.5-9 9-2.5.5.5-2.5 9-9 1.5 1.5 2.12-2.12z";
    }

    @FXML
    private void onCreateRequest() {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Tạo yêu cầu");
        info.setHeaderText(null);
        info.setContentText("Chức năng tạo yêu cầu mới sẽ triển khai ở use case riêng.");
        info.showAndWait();
    }
}

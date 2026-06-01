package com.app.modules.procurement.importorder.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.app.modules.procurement.importorder.service.ImportOrderService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * UI Class cho màn "Xử lý Yêu cầu nhập hàng".
 * Kế thừa ScrollPane và nạp FXML thông qua fx:root.
 */
public class ProcessImportRequestsUI extends ScrollPane {

    @FXML private Label pendingCountLabel;
    @FXML private Label highPriorityCountLabel;
    @FXML private Label totalItemsLabel;
    @FXML private Label availableSitesLabel;
    @FXML private Label resultCountLabel;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> priorityFilter;
    @FXML private ComboBox<String> sortFilter;

    @FXML private VBox requestsList;
    @FXML private Pane emptyContainer;

    private final ImportOrderService service = new ImportOrderService();
    private final List<PendingImportRequestRow> allRequests = new ArrayList<>();

    public ProcessImportRequestsUI() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ProcessImportRequestsPage.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setupFilters();
        loadData();
        renderSummary();
        renderList();
    }

    private void setupFilters() {
        priorityFilter.getItems().setAll(
                "Tất cả",
                "Ưu tiên cao",
                "Ưu tiên trung bình",
                "Ưu tiên thấp"
        );
        priorityFilter.getSelectionModel().select("Tất cả");

        sortFilter.getItems().setAll(
                "Mới nhất",
                "Cũ nhất",
                "Ưu tiên cao trước",
                "Nhiều mặt hàng trước"
        );
        sortFilter.getSelectionModel().select("Mới nhất");

        searchField.textProperty().addListener((obs, oldValue, newValue) -> renderList());
        priorityFilter.valueProperty().addListener((obs, oldValue, newValue) -> renderList());
        sortFilter.valueProperty().addListener((obs, oldValue, newValue) -> renderList());
    }

    private void loadData() {
        allRequests.clear();
        List<com.app.modules.procurement.importorder.repository.ImportOrderRepository.RequestSummary> dbRequests = service.getPendingRequests();
        for (com.app.modules.procurement.importorder.repository.ImportOrderRepository.RequestSummary r : dbRequests) {
            allRequests.add(new PendingImportRequestRow(
                    r.code(),
                    r.createdDate(),
                    r.createdBy(),
                    r.itemCount(),
                    r.priority(),
                    r.status()
            ));
        }
    }

    private void renderSummary() {
        long highPriorityCount = allRequests.stream()
                .filter(request -> "high".equals(request.priority()))
                .count();

        int totalItems = allRequests.stream()
                .mapToInt(PendingImportRequestRow::itemCount)
                .sum();

        pendingCountLabel.setText(String.valueOf(allRequests.size()));
        highPriorityCountLabel.setText(String.valueOf(highPriorityCount));
        totalItemsLabel.setText(String.valueOf(totalItems));
        availableSitesLabel.setText("18");
    }

    private void renderList() {
        requestsList.getChildren().clear();

        List<PendingImportRequestRow> filtered = applyFilters();
        resultCountLabel.setText(filtered.size() + " yêu cầu");

        boolean hasResult = !filtered.isEmpty();
        requestsList.setVisible(hasResult);
        requestsList.setManaged(hasResult);
        emptyContainer.setVisible(!hasResult);
        emptyContainer.setManaged(!hasResult);

        for (PendingImportRequestRow request : filtered) {
            requestsList.getChildren().add(buildRequestRow(request));
        }
    }

    private List<PendingImportRequestRow> applyFilters() {
        String keyword = searchField.getText() == null
                ? ""
                : searchField.getText().trim().toLowerCase();

        String selectedPriority = priorityFilter.getValue() == null
                ? "Tất cả"
                : priorityFilter.getValue();

        List<PendingImportRequestRow> result = new ArrayList<>();

        for (PendingImportRequestRow request : allRequests) {
            boolean matchKeyword = keyword.isEmpty()
                    || request.code().toLowerCase().contains(keyword)
                    || request.createdBy().toLowerCase().contains(keyword);

            boolean matchPriority = switch (selectedPriority) {
                case "Ưu tiên cao" -> "high".equals(request.priority());
                case "Ưu tiên trung bình" -> "medium".equals(request.priority());
                case "Ưu tiên thấp" -> "low".equals(request.priority());
                default -> true;
            };

            if (matchKeyword && matchPriority) {
                result.add(request);
            }
        }

        sortRequests(result);
        return result;
    }

    private void sortRequests(List<PendingImportRequestRow> requests) {
        String sort = sortFilter.getValue() == null ? "Mới nhất" : sortFilter.getValue();

        switch (sort) {
            case "Cũ nhất" -> requests.sort(Comparator.comparing(PendingImportRequestRow::createdDate));
            case "Ưu tiên cao trước" -> requests.sort(
                    Comparator.comparingInt(this::priorityRank));
            case "Nhiều mặt hàng trước" -> requests.sort(
                    Comparator.comparingInt(PendingImportRequestRow::itemCount).reversed());
            default -> requests.sort(
                    Comparator.comparing(PendingImportRequestRow::createdDate).reversed());
        }
    }

    private int priorityRank(PendingImportRequestRow request) {
        return switch (request.priority()) {
            case "high" -> 1;
            case "medium" -> 2;
            case "low" -> 3;
            default -> 4;
        };
    }

    private GridPane buildRequestRow(PendingImportRequestRow request) {
        GridPane row = new GridPane();
        row.getStyleClass().add("request-row");
        row.setHgap(12);
        applyTableColumns(row);

        Label codeLabel = new Label(request.code());
        codeLabel.getStyleClass().add("request-code");
        row.add(codeLabel, 0, 0);

        Label dateLabel = new Label(request.createdDate());
        dateLabel.getStyleClass().add("request-date");
        row.add(dateLabel, 1, 0);

        Label userLabel = new Label(request.createdBy());
        userLabel.getStyleClass().add("request-user");
        row.add(userLabel, 2, 0);

        Label itemCountLabel = new Label(request.itemCount() + " mặt hàng");
        itemCountLabel.getStyleClass().add("request-items");
        row.add(itemCountLabel, 3, 0);

        Label priorityBadge = new Label(getPriorityLabel(request.priority()));
        priorityBadge.getStyleClass().addAll("badge", getPriorityStyleClass(request.priority()));
        HBox priorityBox = new HBox(priorityBadge);
        priorityBox.setAlignment(Pos.CENTER_LEFT);
        row.add(priorityBox, 4, 0);

        Label statusBadge = new Label(getStatusLabel(request.status()));
        statusBadge.getStyleClass().addAll("badge", getStatusStyleClass(request.status()));
        HBox statusBox = new HBox(statusBadge);
        statusBox.setAlignment(Pos.CENTER_LEFT);
        row.add(statusBox, 5, 0);

        Button processButton = new Button("Xử lý");
        processButton.getStyleClass().add("process-button");
        processButton.setOnAction(event -> onProcessRequest(request.code()));

        HBox actionBox = new HBox(processButton);
        actionBox.setAlignment(Pos.CENTER_RIGHT);
        row.add(actionBox, 6, 0);
        GridPane.setHalignment(actionBox, HPos.RIGHT);

        return row;
    }

    private void applyTableColumns(GridPane grid) {
        addColumn(grid, 17);
        addColumn(grid, 16);
        addColumn(grid, 15);
        addColumn(grid, 13);
        addColumn(grid, 14);
        addColumn(grid, 13);
        addColumn(grid, 12);
    }

    private void addColumn(GridPane grid, double percent) {
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(percent);
        grid.getColumnConstraints().add(column);
    }

    private String getPriorityLabel(String priority) {
        return switch (priority) {
            case "high" -> "Cao";
            case "medium" -> "Trung bình";
            case "low" -> "Thấp";
            default -> "Không rõ";
        };
    }

    private String getPriorityStyleClass(String priority) {
        return switch (priority) {
            case "high" -> "priority-high";
            case "medium" -> "priority-medium";
            case "low" -> "priority-low";
            default -> "priority-low";
        };
    }

    private String getStatusLabel(String status) {
        return switch (status) {
            case "pending" -> "Chờ xử lý";
            case "reviewing" -> "Đang xem";
            default -> "Không rõ";
        };
    }

    private String getStatusStyleClass(String status) {
        return switch (status) {
            case "reviewing" -> "status-reviewing";
            default -> "status-pending";
        };
    }

    @FXML
    private void onRefresh() {
        System.out.println("Nội dung chức năng: Làm mới danh sách yêu cầu nhập hàng");
        loadData();
        renderSummary();
        renderList();
    }

    @FXML
    private void onBack() {
        System.out.println("Nội dung chức năng: Quay lại Trang Chủ - Bộ phận Đặt hàng Quốc tế");

        ProcurementDashboardUI root = new ProcurementDashboardUI();

        Scene scene = requestsList.getScene();
        if (scene != null) {
            scene.setRoot(root);
            ((Stage) scene.getWindow()).setTitle(
                    "Hệ thống Quản lý Nhập khẩu - Đặt hàng Quốc tế");
        }
    }

    private void onProcessRequest(String requestCode) {
        System.out.println("Nội dung chức năng: Mở màn xử lý yêu cầu nhập hàng " + requestCode);

        ProcessImportRequestDetailUI root = new ProcessImportRequestDetailUI();
        root.loadRequest(requestCode);

        Scene scene = requestsList.getScene();
        if (scene != null) {
            scene.setRoot(root);
            ((Stage) scene.getWindow()).setTitle(
                    "Hệ thống Quản lý Nhập khẩu - Xử lý yêu cầu " + requestCode);
        }
    }

    private record PendingImportRequestRow(
            String code,
            String createdDate,
            String createdBy,
            int itemCount,
            String priority,
            String status
    ) {
    }
}

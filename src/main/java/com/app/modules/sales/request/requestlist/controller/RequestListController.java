package com.app.modules.sales.request.requestlist.controller;

import com.app.modules.sales.request.requestlist.dto.RequestListRow;
import com.app.modules.sales.request.requestlist.service.RequestListService;
import com.app.modules.sales.request.requestlist.ui.RequestListAlertController;
import com.app.modules.sales.request.requestlist.ui.RequestListUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Controller cho màn danh sách yêu cầu nhập hàng.
 */
public class RequestListController {

    private static final int PAGE_SIZE = 5;

    private final RequestListUI view;
    private final RequestListService service;
    private final RequestListTableController tableController;
    private final RequestListAlertController alertController;

    private final ObservableList<RequestListRow> allRows = FXCollections.observableArrayList();
    private final ToggleGroup filterGroup = new ToggleGroup();

    private String statusFilter = null;
    private int currentPage = 0;

    private Consumer<String> onViewDetail;
    private Consumer<String> onEditRequest;
    private Runnable onCreateRequest;

    public RequestListController() {
        this(new RequestListUI(), new RequestListService(), new RequestListAlertController());
    }

    public RequestListController(RequestListUI view, RequestListService service,
                                 RequestListAlertController alertController) {
        this.view = view;
        this.service = service;
        this.alertController = alertController;
        this.tableController = new RequestListTableController(view);
        init();
    }

    private void init() {
        tableController.setupTable(this::openDetail, this::openEdit);
        view.setSearchListener(() -> {
            currentPage = 0;
            refreshView();
        });
        view.setPrevPageHandler(this::onPrevPage);
        view.setNextPageHandler(this::onNextPage);
        view.setCreateRequestHandler(this::handleCreateRequest);
        reload();
    }

    public RequestListUI getView() {
        return view;
    }

    public void setOnViewDetail(Consumer<String> callback) {
        this.onViewDetail = callback;
    }

    public void setOnEditRequest(Consumer<String> callback) {
        this.onEditRequest = callback;
    }

    public void setOnCreateRequest(Runnable callback) {
        this.onCreateRequest = callback;
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
            alertController.showLoadError(view.getSceneWindow(), ex);
        }
    }

    private void rebuildFilterPills() {
        view.getFilterBar().getChildren().clear();
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
        view.getFilterBar().getChildren().add(pill);
        if (selected) {
            statusFilter = statusToken;
        }
    }

    private List<RequestListRow> getFilteredRows() {
        String q = view.getSearchText();
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
            view.setTableItems(FXCollections.observableArrayList());
            view.setPaginationInfo("Không có yêu cầu phù hợp");
        } else {
            view.setTableItems(FXCollections.observableArrayList(filtered.subList(from, to)));
            view.setPaginationInfo("Hiển thị " + (from + 1) + "–" + to + " trong " + total + " yêu cầu");
        }

        view.setPrevDisabled(currentPage <= 0);
        view.setNextDisabled(currentPage >= totalPages - 1 || total == 0);
        rebuildPageButtons(totalPages);
    }

    private void rebuildPageButtons(int totalPages) {
        view.getPageButtonsBox().getChildren().clear();
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
            view.getPageButtonsBox().getChildren().add(pageBtn);
        }
    }

    private void onPrevPage() {
        if (currentPage > 0) {
            currentPage--;
            refreshView();
        }
    }

    private void onNextPage() {
        currentPage++;
        refreshView();
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

    private void handleCreateRequest() {
        if (onCreateRequest != null) {
            onCreateRequest.run();
        }
    }
}

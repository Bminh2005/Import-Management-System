package com.app.modules.sales.request.requestlist.ui;

import com.app.common.util.FxmlUiHelper;
import com.app.modules.sales.request.requestlist.dto.RequestListRow;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

/**
 * View cho màn danh sách yêu cầu nhập hàng.
 * Logic điều phối nằm ở {@link com.app.modules.sales.request.requestlist.controller.RequestListController}.
 */
public class RequestListUI extends VBox {

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
    @FXML private Button btnCreateRequest;

    private Runnable prevPageHandler;
    private Runnable nextPageHandler;

    public RequestListUI() {
        FxmlUiHelper.loadSelf(this, "/com/app/modules/sales/request/ui/RequestListPage.fxml");
    }

    @FXML
    private void onPrevPage() {
        if (prevPageHandler != null) {
            prevPageHandler.run();
        }
    }

    @FXML
    private void onNextPage() {
        if (nextPageHandler != null) {
            nextPageHandler.run();
        }
    }

    public Window getSceneWindow() {
        return getScene() != null ? getScene().getWindow() : null;
    }

    public String getSearchText() {
        return txtSearch.getText() == null ? "" : txtSearch.getText().trim().toLowerCase();
    }

    public void setSearchListener(Runnable onChange) {
        txtSearch.textProperty().addListener((o, a, b) -> onChange.run());
    }

    public void setPrevPageHandler(Runnable handler) {
        this.prevPageHandler = handler;
    }

    public void setNextPageHandler(Runnable handler) {
        this.nextPageHandler = handler;
    }

    public void setCreateRequestHandler(Runnable handler) {
        if (btnCreateRequest != null) {
            btnCreateRequest.setOnAction(e -> handler.run());
        }
    }

    public void setTableItems(ObservableList<RequestListRow> items) {
        tableRequests.setItems(items);
    }

    public void setPaginationInfo(String text) {
        paginationInfoLabel.setText(text);
    }

    public void setPrevDisabled(boolean disabled) {
        btnPrev.setDisable(disabled);
    }

    public void setNextDisabled(boolean disabled) {
        btnNext.setDisable(disabled);
    }

    public TableView<RequestListRow> getTableRequests() { return tableRequests; }
    public TableColumn<RequestListRow, String> getColId() { return colId; }
    public TableColumn<RequestListRow, String> getColDate() { return colDate; }
    public TableColumn<RequestListRow, Integer> getColQuantity() { return colQuantity; }
    public TableColumn<RequestListRow, String> getColStatus() { return colStatus; }
    public TableColumn<RequestListRow, Void> getColActions() { return colActions; }
    public HBox getFilterBar() { return filterBar; }
    public HBox getPageButtonsBox() { return pageButtonsBox; }
}

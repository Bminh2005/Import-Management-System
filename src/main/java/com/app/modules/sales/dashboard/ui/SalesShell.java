package com.app.modules.sales.dashboard.ui;

import com.app.common.ui.MainLayoutUI;
import com.app.modules.procurement.product.ui.MerchandiseListUI;
import com.app.modules.sales.request.createrequest.controller.CreateRequestController;
import com.app.modules.sales.request.createrequest.ui.CreateImportRequestUI;
import com.app.modules.sales.request.editrequest.ui.EditRequestController;
import com.app.modules.sales.request.editrequest.ui.EditRequestUI;
import com.app.modules.sales.request.requestdetail.controller.RequestDetailController;
import com.app.modules.sales.request.requestlist.controller.RequestListController;
import javafx.scene.control.Alert;

/**
 * Điều hướng sidebar Bộ phận Bán hàng → đổi trang trong {@link MainLayoutUI}.
 */
public class SalesShell {

    private final MainLayoutUI layout;
    private final SalesSidebar sidebar;
    private RequestListController requestListController;

    public SalesShell(MainLayoutUI layout) {
        this.layout = layout;
        this.sidebar = layout.getSalesSidebar();
        sidebar.bindNavigation(
                this::showDashboard,
                this::showProductList,
                this::showRequestList);
    }

    public void showDashboard() {
        SalesDashboardUI dashboard = new SalesDashboardUI();
        dashboard.setActionOnCreateNewRequest(this::showCreateRequest);
        dashboard.setActionOnViewAllRequests(this::showRequestList);
        dashboard.setActionOnAddNewProduct(this::showProductList);
        layout.setPage(dashboard);
    }

    /** Màn quản lý mặt hàng (procurement/product). */
    public void showProductList() {
        sidebar.selectMenu(sidebar.getProductManagerItem());
        layout.setPage(new MerchandiseListUI());
    }

    public void showRequestList() {
        sidebar.selectMenu(sidebar.getImportRequestItem());
        requestListController = new RequestListController();
        requestListController.setOnViewDetail(this::showRequestDetail);
        requestListController.setOnEditRequest(this::showRequestEdit);
        requestListController.setOnCreateRequest(this::showCreateRequest);
        layout.setPage(requestListController.getView());
    }

    /** Màn tạo yêu cầu nhập hàng mới (createrequest). */
    public void showCreateRequest() {
        sidebar.selectMenu(sidebar.getImportRequestItem());
        CreateRequestController controller = new CreateRequestController();
        CreateImportRequestUI view = controller.getView();
        view.setCancelButtonAction(this::showRequestList);
        controller.setOnCreated(this::showRequestList);
        layout.setPage(view);
    }

    /** @return false nếu không tải được yêu cầu từ DB */
    public boolean showRequestDetail(String code) {
        sidebar.selectMenu(sidebar.getImportRequestItem());
        try {
            RequestDetailController controller = new RequestDetailController();
            controller.setOnBack(this::showRequestList);
            controller.setOnEdit(this::showRequestEdit);
            if (!controller.loadRequest(code)) {
                return false;
            }
            layout.setPage(controller.getView());
            return true;
        } catch (RuntimeException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xem chi tiết yêu cầu");
            alert.setHeaderText("Không mở được màn chi tiết: " + code);
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
            return false;
        }
    }

    /** Màn chỉnh sửa yêu cầu — mở từ icon chỉnh sửa trên danh sách. */
    public void showRequestEdit(String code) {
        sidebar.selectMenu(sidebar.getImportRequestItem());
        EditRequestUI edit = new EditRequestUI();
        new EditRequestController(edit);
        edit.setOnBack(this::showRequestList);
        edit.setOnSaved(savedCode -> showRequestList());
        layout.setPage(edit);
        try {
            edit.loadRequest(code);
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Chỉnh sửa yêu cầu");
            alert.setHeaderText("Không tải được yêu cầu " + code);
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
            showRequestList();
        }
    }
}

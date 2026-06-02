package com.app.modules.sales.dashboard.ui;

import com.app.common.ui.MainLayoutUI;
import com.app.modules.sales.request.editrequest.ui.EditRequestUI;
import com.app.modules.sales.request.requestdetail.ui.RequestDetailUI;
import com.app.modules.sales.request.requestlist.ui.RequestListUI;
import javafx.scene.control.Alert;

/**
 * Điều hướng sidebar Bộ phận Bán hàng → đổi trang trong {@link MainLayoutUI}.
 */
public class SalesShell {

    private final MainLayoutUI layout;
    private final SalesSidebar sidebar;

    public SalesShell(MainLayoutUI layout) {
        this.layout = layout;
        this.sidebar = layout.getSalesSidebar();
        sidebar.bindNavigation(
                this::showDashboard,
                this::showProductsPlaceholder,
                this::showRequestList);
    }

    public void showDashboard() {
        layout.setPage(new SalesDashboardUI());
    }

    public void showProductsPlaceholder() {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Quản lý Mặt hàng");
        info.setHeaderText(null);
        info.setContentText("Màn quản lý mặt hàng (module procurement/product) chưa gắn vào sidebar Sales.");
        info.showAndWait();
    }

    public void showRequestList() {
        sidebar.selectMenu(sidebar.getImportRequestItem());
        RequestListUI list = new RequestListUI();
        list.setOnViewDetail(this::showRequestDetail);
        list.setOnEditRequest(this::showRequestEdit);
        layout.setPage(list);
    }

    /** @return false nếu không tải được yêu cầu từ DB */
    public boolean showRequestDetail(String code) {
        sidebar.selectMenu(sidebar.getImportRequestItem());
        RequestDetailUI detail = new RequestDetailUI();
        detail.setOnBack(this::showRequestList);
        layout.setPage(detail);
        if (!detail.loadRequest(code)) {
            showRequestList();
            return false;
        }
        return true;
    }

    /** Màn chỉnh sửa yêu cầu — mở từ icon chỉnh sửa trên danh sách. */
    public void showRequestEdit(String code) {
        sidebar.selectMenu(sidebar.getImportRequestItem());
        EditRequestUI edit = new EditRequestUI();
        edit.setOnBack(() -> showRequestDetail(code));
        edit.setOnSaved(savedCode -> showRequestDetail(savedCode));
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

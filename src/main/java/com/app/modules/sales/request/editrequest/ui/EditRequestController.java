package com.app.modules.sales.request.editrequest.ui;

import com.app.modules.sales.request.editrequest.dto.RequestResponse;
import com.app.modules.sales.request.editrequest.dto.UpdateRequestDTO;
import com.app.modules.sales.request.editrequest.service.RequestService;
import com.app.modules.sales.request.entity.RequestItem;
import java.util.List;

/**
 * Controller chính cho feature Chỉnh sửa Yêu cầu Nhập hàng (EditRequest).
 * Điều phối giữa View (EditRequestUI) và Service, ủy quyền cấu hình bảng cho
 * EditRequestTableController và các hộp thoại cho EditRequestAlertController (SRP).
 */
public class EditRequestController {

    private final EditRequestUI view;
    private final RequestService service;
    private final EditRequestTableController tableController;
    private final EditRequestAlertController alertController;

    public EditRequestController() {
        this(null, new RequestService(), new EditRequestAlertController());
    }

    public EditRequestController(EditRequestUI view) {
        this(view, new RequestService(), new EditRequestAlertController());
    }

    public EditRequestController(EditRequestUI view, RequestService service, EditRequestAlertController alertController) {
        this.view = view;
        this.service = service;
        this.alertController = alertController;
        this.tableController = new EditRequestTableController(view, this::handleDeleteItem);
        if (this.view != null) {
            this.view.setController(this);
            bindViewActions();
            // Khởi chạy thiết lập bảng ban đầu
            this.tableController.setupTables(this.view.isEditable());
        }
    }

    private void bindViewActions() {
        view.setActionForSaveButton(this::handleSave);
        view.setActionForAddItemButton(this::handleAddItem);
        view.setActionForBackButton(this::handleBack);
    }

    /**
     * Tải chi tiết yêu cầu và cập nhật View.
     */
    public void loadRequest(String code) {
        try {
            RequestResponse response = service.getRequestDetail(code);
            view.renderRequest(response);
            // Cập nhật lại trạng thái các bảng theo cờ editable mới
            tableController.setupTables(view.isEditable());
            if (!view.isEditable()) {
                alertController.showCompletedAlert(view.getSceneWindow(), response.getCode(), response.getStatus());
            }
        } catch (Exception e) {
            alertController.showWarning(view.getSceneWindow(), "Không thể tải yêu cầu", e.getMessage());
        }
    }

    /**
     * Xử lý sự kiện Lưu thay đổi.
     */
    private void handleSave() {
        if (!view.hasCurrentRequest()) return;
        if (!view.isEditable()) {
            alertController.showWarning(view.getSceneWindow(), "Không thể chỉnh sửa",
                    "Yêu cầu " + view.getRequestCode() + " không còn ở trạng thái PENDING, không thể lưu thay đổi.");
            return;
        }

        UpdateRequestDTO dto = new UpdateRequestDTO(
                view.getRequestCode(),
                view.getCurrentRequestItemsList()
        );

        try {
            RequestResponse updated = service.updateRequest(dto);
            view.renderRequest(updated);
            tableController.setupTables(view.isEditable());
            view.setDirty(false);
            alertController.showInfo(view.getSceneWindow(), "Đã lưu thay đổi yêu cầu " + updated.getCode());
            if (view.getOnSaved() != null) {
                view.getOnSaved().accept(updated.getCode());
            }
        } catch (IllegalStateException ex) {
            alertController.showWarning(view.getSceneWindow(), "Lỗi khi lưu", ex.getMessage());
        }
    }

    /**
     * Xử lý sự kiện Thêm mặt hàng.
     */
    private void handleAddItem() {
        if (!view.hasCurrentRequest() || !view.isEditable()) return;
        javafx.stage.Window owner = view.getSceneWindow();
        SelectProductDialogUI.show(owner, view.getRequestCode(), this, product ->
                EnterItemInfoDialogUI.show(owner, product, newItem -> {
                    view.addRequestItem(newItem);
                    System.out.println("Nội dung chức năng: Đã thêm mặt hàng " + newItem.getCode());
                }));
    }

    /**
     * Xử lý sự kiện Xóa mặt hàng khỏi yêu cầu.
     */
    private void handleDeleteItem(RequestItem item) {
        if (!view.hasCurrentRequest() || !view.isEditable()) return;
        if (alertController.confirmDelete(view.getSceneWindow(), item.getCode())) {
            view.removeRequestItem(item);
            System.out.println("Nội dung chức năng: Xóa mặt hàng " + item.getCode());
        }
    }

    /**
     * Xử lý sự kiện nhấn nút Quay lại.
     */
    private void handleBack() {
        if (view.isDirty()) {
            if (!alertController.confirmBack(view.getSceneWindow())) {
                return;
            }
        }
        System.out.println("Nội dung chức năng: Quay lại "
                + (view.hasCurrentRequest() ? view.getRequestCode() : ""));
        if (view.getOnBack() != null) {
            view.getOnBack().run();
        }
    }

    /** Danh sách sản phẩm có thể thêm vào yêu cầu (dùng cho dialog chọn mặt hàng). */
    public List<RequestItem> getAvailableProducts(String requestCode) {
        return service.listAvailableProducts(requestCode);
    }
}

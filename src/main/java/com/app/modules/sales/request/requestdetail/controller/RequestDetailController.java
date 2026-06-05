package com.app.modules.sales.request.requestdetail.controller;

import com.app.modules.sales.request.requestdetail.dto.RequestResponse;
import com.app.modules.sales.request.requestdetail.service.RequestService;
import com.app.modules.sales.request.requestdetail.ui.OrderDetailDialogUI;
import com.app.modules.sales.request.requestdetail.ui.RequestDetailAlertController;
import com.app.modules.sales.request.requestdetail.ui.RequestDetailTableController;
import com.app.modules.sales.request.requestdetail.ui.RequestDetailUI;

import java.util.function.Consumer;

/**
 * Controller cho màn "Xem chi tiết Yêu cầu Nhập hàng".
 */
public class RequestDetailController {

    private final RequestDetailUI view;
    private final RequestService service;
    private final RequestDetailTableController tableController;
    private final RequestDetailAlertController alertController;

    private Runnable onBack;
    private Consumer<String> onEdit;

    public RequestDetailController() {
        this(new RequestDetailUI(), new RequestService(), new RequestDetailAlertController());
    }

    public RequestDetailController(RequestDetailUI view, RequestService service,
                                   RequestDetailAlertController alertController) {
        this.view = view;
        this.service = service;
        this.alertController = alertController;
        this.tableController = new RequestDetailTableController(view);
        init();
    }

    private void init() {
        view.setOnBack(this::handleBack);
        view.setOnEdit(this::handleEdit);
        tableController.setupTables(this::handleViewOrder);
    }

    public RequestDetailUI getView() {
        return view;
    }

    public void setOnBack(Runnable callback) {
        this.onBack = callback;
    }

    public void setOnEdit(Consumer<String> callback) {
        this.onEdit = callback;
    }

    public boolean loadRequest(String code) {
        try {
            RequestResponse response = service.getRequestDetail(code);
            view.display(response);
            return true;
        } catch (Exception ex) {
            view.clearCurrentRequestCode();
            alertController.showLoadError(
                    view.getScene() != null ? view.getScene().getWindow() : null, code, ex);
            return false;
        }
    }

    private void handleBack() {
        if (onBack != null) {
            onBack.run();
            return;
        }
        System.out.println("Nội dung chức năng: Quay lại danh sách yêu cầu "
                + view.getCurrentRequestCode());
    }

    private void handleEdit(String code) {
        if (onEdit != null) {
            onEdit.accept(code);
            return;
        }
        System.out.println("Nội dung chức năng: Chỉnh sửa yêu cầu " + code);
    }

    private void handleViewOrder(String orderCode) {
        System.out.println("Nội dung chức năng: Xem chi tiết đơn hàng " + orderCode);
        OrderDetailDialogUI.show(view.getSceneWindow(), orderCode);
    }
}

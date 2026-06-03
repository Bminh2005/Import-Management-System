package com.app.modules.procurement.order.ui;

import com.app.modules.procurement.order.model.ReallocationResult;
import com.app.modules.procurement.order.service.SiteOrderService;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Caches the list view and shares one {@link SiteOrderService} to reduce lag on navigation.
 */
public final class SiteOrderNavigator {

    private static final SiteOrderService SERVICE = new SiteOrderService();

    private static Parent listRoot;
    private static SiteOrderListController listController;

    private SiteOrderNavigator() {
    }

    public static SiteOrderService service() {
        return SERVICE;
    }

    public static void showList(Scene scene) {
        try {
            ensureListLoaded();
            listController.refresh();
            scene.setRoot(listRoot);
            stageTitle(scene, "Đơn đặt hàng");
        } catch (Exception e) {
            throw new RuntimeException("Không mở được danh sách đơn", e);
        }
    }

    public static void showDetail(Scene scene, long orderId) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SiteOrderNavigator.class.getResource("SiteOrderDetailView.fxml"));
            Parent root = loader.load();
            ((SiteOrderDetailController) loader.getController()).loadOrder(orderId);
            scene.setRoot(root);
            stageTitle(scene, "Chi tiết đơn #" + orderId);
        } catch (Exception e) {
            throw new RuntimeException("Không mở được chi tiết đơn", e);
        }
    }

    public static void showReallocation(Scene scene, long orderId) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SiteOrderNavigator.class.getResource("SiteOrderReallocationView.fxml"));
            Parent root = loader.load();
            ((SiteOrderReallocationController) loader.getController()).loadOrder(orderId);
            scene.setRoot(root);
            stageTitle(scene, "Phân bổ lại đơn #" + orderId);
        } catch (Exception e) {
            throw new RuntimeException("Không mở được màn phân bổ lại", e);
        }
    }

    public static void showSuccess(Scene scene, ReallocationResult result, long removedOrderId) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SiteOrderNavigator.class.getResource("SiteOrderSuccessView.fxml"));
            Parent root = loader.load();
            ((SiteOrderSuccessController) loader.getController()).setResult(result, removedOrderId);
            ensureListLoaded();
            listController.refresh();
            scene.setRoot(root);
            stageTitle(scene, "Gửi đơn thành công");
        } catch (Exception e) {
            throw new RuntimeException("Không mở được màn thành công", e);
        }
    }

    private static void ensureListLoaded() throws Exception {
        if (listRoot == null) {
            FXMLLoader loader = new FXMLLoader(
                    SiteOrderNavigator.class.getResource("SiteOrderListView.fxml"));
            listRoot = loader.load();
            listController = loader.getController();
        }
    }

    private static void stageTitle(Scene scene, String title) {
        Stage stage = (Stage) scene.getWindow();
        if (stage != null) {
            stage.setTitle(title);
        }
    }
}

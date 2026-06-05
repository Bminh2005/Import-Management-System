package com.app.modules.sales.request.editrequest.ui;

import com.app.common.util.FxmlUiHelper;
import com.app.modules.sales.request.entity.RequestItem;
import com.app.modules.sales.request.editrequest.ui.common.ProductCardUI;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.List;
import java.util.function.Consumer;

/**
 * Popup "Chọn mặt hàng" — danh sách sản phẩm có thể thêm vào yêu cầu.
 * Mỗi item là 1 card có nút "Chọn"; khi click sẽ callback ra ngoài
 * để mở popup tiếp theo (nhập SL + ngày nhận).
 *
 * Mở qua {@link #show(Window, String, Consumer)}.
 */
public class SelectProductDialogUI extends VBox {

    @FXML private VBox productList;

    private final EditRequestController controller;
    private Stage stage;
    private Consumer<RequestItem> onPicked;

    public SelectProductDialogUI() {
        this(new EditRequestController());
    }

    public SelectProductDialogUI(EditRequestController controller) {
        this.controller = controller;
        FxmlUiHelper.loadSelf(this, "SelectProductDialogPage.fxml");
    }

    /** Tiện ích: mở popup, callback nhận RequestItem đã chọn (chưa có SL/ngày). */
    public static void show(Window owner, String currentRequestCode,
                            Consumer<RequestItem> onPicked) {
        show(owner, currentRequestCode, new EditRequestController(), onPicked);
    }

    /** Tiện ích với Provider: mở popup, callback nhận RequestItem đã chọn. */
    public static void show(Window owner, String currentRequestCode, EditRequestController controller,
                            Consumer<RequestItem> onPicked) {
        SelectProductDialogUI dialog = new SelectProductDialogUI(controller);
        dialog.onPicked = onPicked;
        dialog.loadProducts(currentRequestCode);

        Stage stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Chọn mặt hàng");
        Scene scene = new Scene(dialog);
        scene.setFill(null);
        stage.setScene(scene);
        dialog.stage = stage;
        stage.showAndWait();
    }

    private void loadProducts(String requestCode) {
        productList.getChildren().clear();
        List<RequestItem> products = controller.getAvailableProducts(requestCode);
        if (products.isEmpty()) {
            Label empty = new Label("Tất cả sản phẩm đã có trong yêu cầu.");
            empty.getStyleClass().add("empty-state");
            empty.setMaxWidth(Double.MAX_VALUE);
            empty.setAlignment(Pos.CENTER);
            productList.getChildren().add(empty);
            return;
        }
        for (RequestItem p : products) {
            productList.getChildren().add(new ProductCardUI(p, () -> {
                if (onPicked != null) onPicked.accept(p);
                if (stage != null) stage.close();
            }));
        }
    }

    @FXML
    private void onClose() {
        if (stage != null) stage.close();
    }
}

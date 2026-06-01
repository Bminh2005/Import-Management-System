package com.app.modules.sales.request.createrequest.ui;

import com.app.common.ui.components.ToastNotification;
import com.app.common.util.FormatUtil;
import com.app.modules.sales.request.createrequest.ui.components.ActionTableCell;
import com.app.modules.sales.request.createrequest.ui.components.CreateQuantityTableCell;
import com.app.modules.sales.request.createrequest.ui.components.DesiredDateTableCell;
import com.app.modules.sales.request.createrequest.ui.model.CreateImportItemModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;

public class CreateImportRequestUI extends VBox {
    @FXML
    private Button addMerchandiseButton;
    @FXML
    private Button createButton;
    @FXML
    private Button cancelButton;
    @FXML private TableView<CreateImportItemModel> tableItems;
    @FXML private TableColumn<CreateImportItemModel, String> colMaHang;
    @FXML private TableColumn<CreateImportItemModel, String> colTenHang;
    @FXML private TableColumn<CreateImportItemModel, Integer> colSoLuong;
    @FXML private TableColumn<CreateImportItemModel, String> colDonVi;
    @FXML private TableColumn<CreateImportItemModel, LocalDate> colNgayNhan;
    @FXML private TableColumn<CreateImportItemModel, Void> colThaoTac;
    @FXML private TableColumn<CreateImportItemModel, Double> colGiaThamKhao;
    ObservableList<CreateImportItemModel> availableProducts;
    public CreateImportRequestUI() {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/app/modules/sales/request/ui/CreateImportRequest.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        colMaHang.setCellValueFactory(cellData -> cellData.getValue().itemCodeProperty());
        colTenHang.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
        colDonVi.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
        colSoLuong.setCellFactory(column -> new CreateQuantityTableCell());
        colNgayNhan.setCellFactory(column -> new DesiredDateTableCell());
        colGiaThamKhao.setCellValueFactory(cellData -> cellData.getValue().referencePriceProperty().asObject());
        colGiaThamKhao.setCellFactory(column -> new TableCell<CreateImportItemModel, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    // Sử dụng hàm CurrencyUtils bạn đã tạo
                    setText(FormatUtil.formatPrice(price));
                    setAlignment(Pos.CENTER_RIGHT); // <--- CĂN LỀ PHẢI Ở ĐÂY
                }
            }
        });
        colThaoTac.setCellFactory(column -> new ActionTableCell());
//        javafx.collections.ObservableList<CreateImportItemModel> testData = javafx.collections.FXCollections.observableArrayList(
//                new CreateImportItemModel("MH001", "Laptop Dell XPS 13", 1, "cái", null),
//                new CreateImportItemModel("MH003", "Chuột Logitech MX Master 3", 1, "cái", null),
//                new CreateImportItemModel("MH001", "Laptop Dell XPS 13", 1, "cái", null),
//                new CreateImportItemModel("MH002", "Bàn phím cơ Keychron K2", 1, "cái", null)
//        );
//
//        // Đổ danh sách dữ liệu mẫu này lên TableView
//        tableItems.setItems(testData);
        setAddMerchandiseButtonAction(new Runnable() {
            @Override
            public void run() {
                showSelectMerchandisePopup();
            }
        });
        setCreateButtonAction(new Runnable() {
            @Override
            public void run() {
                System.out.println("Create button clicked");
            }
        });

    }

    public void setAddMerchandiseButtonAction(Runnable r){
        addMerchandiseButton.setOnAction(e ->{
            r.run();
        });
    }
    public void setCreateButtonAction(Runnable r){
        createButton.setOnAction(e ->{
            try {
//                if (tableItems.getItems().isEmpty()) {
//                    showToastNotification("Vui lòng thêm ít nhất một mặt hàng!", false);
//                    return;
//                }
                r.run();
            } catch (Exception ex) {
                throw new RuntimeException("Lỗi hệ thống: " + ex.getMessage(), ex);
            }
        });
    }
    public void setCancelButtonAction(Runnable r){
        cancelButton.setOnAction(e ->{
            r.run();
        });
    }
    private void showSelectMerchandisePopup() {
        try {
            FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/app/modules/sales/request/ui/SelectMerchandiseDialog.fxml"));
            Parent root = loader.load();
            SelectMerchandiseController dialogController = loader.getController();
            // Nhận lại một LOẠT các hàng đã được chọn và đẩy vào bảng chính
            dialogController.initData(availableProducts, selectedList -> {
                for (CreateImportItemModel selectedItem : selectedList) {
                    // Tạo bản sao mới add vào TableView chính
                    CreateImportItemModel newItem = new CreateImportItemModel(
                            selectedItem.getMerchandiseDetailId(),
                            selectedItem.getItemCode(),
                            selectedItem.getItemName(),
                            1, // Mặc định số lượng nhập ban đầu là 1
                            selectedItem.getUnit(),
                            selectedItem.getReferencePrice(),
                            null
                    );
                    tableItems.getItems().add(newItem); // Đẩy vào bảng
                }
            });

            // Hiển thị Popup
            javafx.stage.Stage popupStage = new javafx.stage.Stage();
            popupStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            popupStage.initStyle(javafx.stage.StageStyle.TRANSPARENT);

            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
            popupStage.setScene(scene);
            popupStage.centerOnScreen();
            popupStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setAvailableProducts(ObservableList<CreateImportItemModel> availableProducts) {
        this.availableProducts = availableProducts;
    }

    public void showToastNotification(String message, boolean isSuccess) {
        ToastNotification.show(this.getScene().getWindow(), message, isSuccess);
    }

    public ObservableList<CreateImportItemModel> getItems() {
        return tableItems.getItems();
    }

}

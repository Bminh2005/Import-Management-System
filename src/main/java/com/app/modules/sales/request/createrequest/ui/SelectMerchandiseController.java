package com.app.modules.sales.request.createrequest.ui;

import com.app.modules.sales.request.createrequest.ui.components.MerchandiseCardCell;
import com.app.modules.sales.request.createrequest.ui.model.CreateImportItemModel;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SelectMerchandiseController {

    @FXML private ListView<CreateImportItemModel> lvMerchandise;
    @FXML private TextField txtSearch; // Khai báo ô tìm kiếm mới
    @FXML private Button btnXClose;
    @FXML private Button btnClose;
    @FXML private Button btnSubmit;

    private Consumer<List<CreateImportItemModel>> onSubmitHandler;
    private ObservableList<CreateImportItemModel> masterList;
    private FilteredList<CreateImportItemModel> filteredList; // Đối tượng hỗ trợ lọc dữ liệu

    @FXML
    public void initialize() {
        btnXClose.setOnAction(e -> closeWindow());
        btnClose.setOnAction(e -> closeWindow());

        btnSubmit.setOnAction(e -> {
            if (onSubmitHandler != null && masterList != null) {
                // Quét bộ lọc trên masterList gốc để gom đủ các mục được chọn kể cả khi đang bị ẩn bởi search
                List<CreateImportItemModel> selectedItems = masterList.stream()
                        .filter(CreateImportItemModel::isSelected)
                        .collect(Collectors.toList());

                onSubmitHandler.accept(selectedItems);
            }
            closeWindow();
        });

        // TÍNH NĂNG SEARCH TỰ ĐỘNG: Lắng nghe sự kiện người dùng gõ chữ
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (filteredList == null) return;

            filteredList.setPredicate(item -> {
                // Nếu thanh tìm kiếm trống, hiển thị toàn bộ danh sách
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase().trim();

                // Lọc theo Tên sản phẩm HOẶC Mã sản phẩm
                boolean matchName = item.getItemName() != null && item.getItemName().toLowerCase().contains(lowerCaseFilter);
                boolean matchCode = item.getItemCode() != null && item.getItemCode().toLowerCase().contains(lowerCaseFilter);

                return matchName || matchCode;
            });
        });
    }

    public void initData(ObservableList<CreateImportItemModel> allProducts, Consumer<List<CreateImportItemModel>> onSubmitHandler) {
        this.onSubmitHandler = onSubmitHandler;
        this.masterList = allProducts;

        // Reset trạng thái chọn về false mỗi lần mở
        this.masterList.forEach(item -> item.setSelected(false));

        // Khởi tạo FilteredList bọc ngoài masterList
        this.filteredList = new FilteredList<>(this.masterList, p -> true);

        // Đổ danh sách đã bọc bộ lọc lên ListView thay vì danh sách gốc
        lvMerchandise.setItems(this.filteredList);
        lvMerchandise.setCellFactory(lv -> new MerchandiseCardCell());

        // Tự động focus vào thanh tìm kiếm ngay khi mở popup
        txtSearch.requestFocus();
    }

    private void closeWindow() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
}
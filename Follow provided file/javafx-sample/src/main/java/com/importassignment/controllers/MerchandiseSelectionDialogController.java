package com.importassignment.controllers;

import com.importassignment.models.Merchandise;
import com.importassignment.services.MockDataService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controller for Merchandise Selection Dialog
 * Allows users to search, filter, and select merchandise with details preview
 */
public class MerchandiseSelectionDialogController implements Initializable {

    @FXML private TextField searchField;
    @FXML private ComboBox<String> categoryFilter;
    @FXML private Label resultCountLabel;

    @FXML private TableView<Merchandise> merchandiseTable;
    @FXML private TableColumn<Merchandise, String> codeColumn;
    @FXML private TableColumn<Merchandise, String> nameColumn;
    @FXML private TableColumn<Merchandise, String> categoryColumn;
    @FXML private TableColumn<Merchandise, String> unitColumn;
    @FXML private TableColumn<Merchandise, String> priceColumn;

    @FXML private VBox detailsPanel;
    @FXML private Label detailCodeLabel;
    @FXML private Label detailNameLabel;
    @FXML private Label detailCategoryLabel;
    @FXML private Label detailUnitLabel;
    @FXML private Label detailPriceLabel;
    @FXML private Label detailSupplierLabel;
    @FXML private Label detailDescriptionLabel;

    @FXML private Button selectButton;

    private ResourceBundle resources;
    private MockDataService dataService;
    private FilteredList<Merchandise> filteredData;
    private Merchandise selectedMerchandise;
    private MerchandiseSelectionCallback callback;

    // Callback interface
    public interface MerchandiseSelectionCallback {
        void onMerchandiseSelected(Merchandise merchandise);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        this.dataService = MockDataService.getInstance();

        setupTable();
        setupFilters();
        loadData();
        detailsPanel.setVisible(false);
        selectButton.setDisable(true);
    }

    public void setCallback(MerchandiseSelectionCallback callback) {
        this.callback = callback;
    }

    private void setupTable() {
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Add info button column
        TableColumn<Merchandise, Void> infoColumn = new TableColumn<>("");
        infoColumn.setPrefWidth(50);
        infoColumn.setCellFactory(column -> new TableCell<>() {
            private final Button infoButton = new Button("ℹ️");

            {
                infoButton.setStyle("-fx-cursor: hand; -fx-background-color: transparent;");
                infoButton.setOnAction(event -> {
                    Merchandise item = getTableView().getItems().get(getIndex());
                    showDetails(item);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : infoButton);
            }
        });

        // Add select button column
        TableColumn<Merchandise, Void> selectColumn = new TableColumn<>("");
        selectColumn.setPrefWidth(100);
        selectColumn.setCellFactory(column -> new TableCell<>() {
            private final Button selectBtn = new Button("Chọn");

            {
                selectBtn.setStyle(
                    "-fx-background-color: #2563EB; " +
                    "-fx-text-fill: white; " +
                    "-fx-cursor: hand; " +
                    "-fx-padding: 8 16; " +
                    "-fx-border-radius: 6; " +
                    "-fx-background-radius: 6;"
                );
                selectBtn.setOnAction(event -> {
                    Merchandise item = getTableView().getItems().get(getIndex());
                    handleSelect(item);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : selectBtn);
            }
        });

        merchandiseTable.getColumns().addAll(infoColumn, selectColumn);

        // Row selection
        merchandiseTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                selectedMerchandise = newVal;
                selectButton.setDisable(newVal == null);
                if (newVal != null) {
                    showDetails(newVal);
                }
            }
        );

        // Double-click to select
        merchandiseTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && selectedMerchandise != null) {
                handleSelect(selectedMerchandise);
            }
        });
    }

    private void setupFilters() {
        // Setup category filter
        Set<String> categories = dataService.getActiveMerchandise()
            .stream()
            .map(Merchandise::getCategory)
            .collect(Collectors.toSet());

        ObservableList<String> categoryList = FXCollections.observableArrayList();
        categoryList.add("Tất cả danh mục");
        categoryList.addAll(categories);
        categoryFilter.setItems(categoryList);
        categoryFilter.setValue("Tất cả danh mục");

        // Search listener
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            applyFilters();
        });

        // Category filter listener
        categoryFilter.valueProperty().addListener((observable, oldValue, newValue) -> {
            applyFilters();
        });
    }

    private void applyFilters() {
        filteredData.setPredicate(merchandise -> {
            // Search filter
            String searchText = searchField.getText();
            boolean matchesSearch = true;
            if (searchText != null && !searchText.isEmpty()) {
                String lowerCaseFilter = searchText.toLowerCase();
                matchesSearch = merchandise.getCode().toLowerCase().contains(lowerCaseFilter) ||
                               merchandise.getName().toLowerCase().contains(lowerCaseFilter);
            }

            // Category filter
            String selectedCategory = categoryFilter.getValue();
            boolean matchesCategory = selectedCategory == null ||
                                     selectedCategory.equals("Tất cả danh mục") ||
                                     merchandise.getCategory().equals(selectedCategory);

            return matchesSearch && matchesCategory;
        });

        updateResultCount();
    }

    private void loadData() {
        filteredData = new FilteredList<>(dataService.getActiveMerchandise(), p -> true);
        merchandiseTable.setItems(filteredData);
        updateResultCount();
    }

    private void updateResultCount() {
        int total = dataService.getActiveMerchandise().size();
        int showing = filteredData.size();
        resultCountLabel.setText(
            String.format("Hiển thị %d trong %d mặt hàng", showing, total)
        );
    }

    private void showDetails(Merchandise merchandise) {
        detailsPanel.setVisible(true);
        detailsPanel.setManaged(true);

        detailCodeLabel.setText(merchandise.getCode());
        detailNameLabel.setText(merchandise.getName());
        detailCategoryLabel.setText(merchandise.getCategory());
        detailUnitLabel.setText(merchandise.getUnit());
        detailPriceLabel.setText(merchandise.getPrice());
        detailSupplierLabel.setText(merchandise.getSupplier());
        detailDescriptionLabel.setText(merchandise.getDescription());
    }

    @FXML
    private void onSelect() {
        if (selectedMerchandise != null) {
            handleSelect(selectedMerchandise);
        }
    }

    private void handleSelect(Merchandise merchandise) {
        if (callback != null) {
            callback.onMerchandiseSelected(merchandise);
        }
        closeDialog();
    }

    @FXML
    private void onCancel() {
        closeDialog();
    }

    @FXML
    private void onCloseDetails() {
        detailsPanel.setVisible(false);
        detailsPanel.setManaged(false);
    }

    private void closeDialog() {
        Stage stage = (Stage) merchandiseTable.getScene().getWindow();
        stage.close();
    }
}

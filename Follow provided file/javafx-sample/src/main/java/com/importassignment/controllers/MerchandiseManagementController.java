package com.importassignment.controllers;

import com.importassignment.models.Merchandise;
import com.importassignment.services.MockDataService;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for Merchandise Management
 */
public class MerchandiseManagementController implements Initializable {

    @FXML private TextField searchField;
    @FXML private TableView<Merchandise> merchandiseTable;
    @FXML private TableColumn<Merchandise, String> codeColumn;
    @FXML private TableColumn<Merchandise, String> nameColumn;
    @FXML private TableColumn<Merchandise, String> unitColumn;
    @FXML private TableColumn<Merchandise, String> categoryColumn;
    @FXML private TableColumn<Merchandise, String> statusColumn;

    @FXML private VBox detailsPanel;
    @FXML private Label detailCodeLabel;
    @FXML private Label detailNameLabel;
    @FXML private Label detailUnitLabel;
    @FXML private Label detailCategoryLabel;
    @FXML private Label detailPriceLabel;
    @FXML private Label detailSupplierLabel;
    @FXML private Label detailDescriptionLabel;

    private ResourceBundle resources;
    private MockDataService dataService;
    private FilteredList<Merchandise> filteredData;
    private Merchandise selectedMerchandise;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        this.dataService = MockDataService.getInstance();

        setupTable();
        setupSearch();
        loadData();
        detailsPanel.setVisible(false);
    }

    private void setupTable() {
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Status column with badges
        statusColumn.setCellFactory(column -> new TableCell<Merchandise, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status.equals("active") ? "Hoạt động" : "Ngừng");
                    if (status.equals("active")) {
                        setStyle("-fx-background-color: #D1FAE5; -fx-text-fill: #065F46; " +
                               "-fx-padding: 6 12; -fx-border-radius: 12; -fx-background-radius: 12;");
                    } else {
                        setStyle("-fx-background-color: #F3F4F6; -fx-text-fill: #1F2937; " +
                               "-fx-padding: 6 12; -fx-border-radius: 12; -fx-background-radius: 12;");
                    }
                }
            }
        });

        // Add action column
        TableColumn<Merchandise, Void> actionColumn = new TableColumn<>("Thao tác");
        actionColumn.setCellFactory(column -> new TableCell<>() {
            private final Button viewButton = new Button("👁");
            private final Button editButton = new Button("✏");
            private final Button deleteButton = new Button("🗑");

            {
                viewButton.setOnAction(event -> {
                    Merchandise item = getTableView().getItems().get(getIndex());
                    showDetails(item);
                });

                editButton.setOnAction(event -> {
                    Merchandise item = getTableView().getItems().get(getIndex());
                    handleEdit(item);
                });

                deleteButton.setOnAction(event -> {
                    Merchandise item = getTableView().getItems().get(getIndex());
                    handleDelete(item);
                });

                viewButton.setStyle("-fx-cursor: hand;");
                editButton.setStyle("-fx-cursor: hand;");
                deleteButton.setStyle("-fx-cursor: hand;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    javafx.scene.layout.HBox buttons = new javafx.scene.layout.HBox(5);
                    buttons.getChildren().addAll(viewButton, editButton, deleteButton);
                    setGraphic(buttons);
                }
            }
        });

        merchandiseTable.getColumns().add(actionColumn);

        // Row selection
        merchandiseTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null) {
                    showDetails(newVal);
                }
            }
        );
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(merchandise -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return merchandise.getCode().toLowerCase().contains(lowerCaseFilter) ||
                       merchandise.getName().toLowerCase().contains(lowerCaseFilter) ||
                       merchandise.getCategory().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }

    private void loadData() {
        filteredData = new FilteredList<>(dataService.getAllMerchandise(), p -> true);
        merchandiseTable.setItems(filteredData);
    }

    private void showDetails(Merchandise merchandise) {
        selectedMerchandise = merchandise;
        detailsPanel.setVisible(true);
        detailsPanel.setManaged(true);

        detailCodeLabel.setText(merchandise.getCode());
        detailNameLabel.setText(merchandise.getName());
        detailUnitLabel.setText(merchandise.getUnit());
        detailCategoryLabel.setText(merchandise.getCategory());
        detailPriceLabel.setText(merchandise.getPrice());
        detailSupplierLabel.setText(merchandise.getSupplier());
        detailDescriptionLabel.setText(merchandise.getDescription());
    }

    @FXML
    private void onAddNew() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/AddMerchandiseDialog.fxml"),
                resources
            );
            Parent root = loader.load();

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Thêm Mặt hàng Mới");
            dialog.setScene(new Scene(root));
            dialog.showAndWait();

            // Refresh table after dialog closes
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleEdit(Merchandise merchandise) {
        System.out.println("Edit: " + merchandise.getName());
        // Show edit dialog
    }

    private void handleDelete(Merchandise merchandise) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText("Bạn có chắc chắn muốn xóa mặt hàng này?");
        alert.setContentText(merchandise.getCode() + " - " + merchandise.getName());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            dataService.removeMerchandise(merchandise);
            loadData();
            detailsPanel.setVisible(false);
        }
    }

    @FXML
    private void onCloseDetails() {
        detailsPanel.setVisible(false);
        detailsPanel.setManaged(false);
        merchandiseTable.getSelectionModel().clearSelection();
    }
}

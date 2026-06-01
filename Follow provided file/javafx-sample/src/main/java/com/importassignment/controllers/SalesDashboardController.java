package com.importassignment.controllers;

import com.importassignment.MainApp;
import com.importassignment.models.ImportRequest;
import com.importassignment.services.MockDataService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for Sales Dashboard
 */
public class SalesDashboardController implements Initializable {

    @FXML private Label totalMerchandiseLabel;
    @FXML private Label pendingRequestsLabel;
    @FXML private Label thisMonthLabel;
    @FXML private Label approvedLabel;

    @FXML private TableView<ImportRequest> recentRequestsTable;
    @FXML private TableColumn<ImportRequest, String> codeColumn;
    @FXML private TableColumn<ImportRequest, String> dateColumn;
    @FXML private TableColumn<ImportRequest, Integer> itemCountColumn;
    @FXML private TableColumn<ImportRequest, String> statusColumn;

    @FXML private Button createRequestButton;
    @FXML private Button addMerchandiseButton;
    @FXML private Button viewAllButton;

    @FXML private StackPane contentContainer;

    private ResourceBundle resources;
    private MockDataService dataService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        this.dataService = MockDataService.getInstance();

        setupMetrics();
        setupTable();
        loadRecentRequests();
    }

    private void setupMetrics() {
        // Mock metrics data
        totalMerchandiseLabel.setText("48");
        pendingRequestsLabel.setText("12");
        thisMonthLabel.setText("28");
        approvedLabel.setText("24");
    }

    private void setupTable() {
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        itemCountColumn.setCellValueFactory(new PropertyValueFactory<>("itemCount"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Custom cell factory for status column with colored badges
        statusColumn.setCellFactory(column -> new TableCell<ImportRequest, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(getStatusText(status));
                    setStyle(getStatusStyle(status));
                }
            }
        });

        // Add action column with "View Details" button
        TableColumn<ImportRequest, Void> actionColumn = new TableColumn<>("Thao tác");
        actionColumn.setCellFactory(column -> new TableCell<>() {
            private final Button viewButton = new Button("Xem chi tiết →");

            {
                viewButton.setStyle(
                    "-fx-background-color: transparent; " +
                    "-fx-text-fill: #2563EB; " +
                    "-fx-cursor: hand; " +
                    "-fx-font-weight: bold;"
                );
                viewButton.setOnAction(event -> {
                    ImportRequest request = getTableView().getItems().get(getIndex());
                    handleViewRequest(request);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : viewButton);
            }
        });

        recentRequestsTable.getColumns().add(actionColumn);
    }

    private void loadRecentRequests() {
        recentRequestsTable.setItems(dataService.getAllImportRequests());
    }

    private String getStatusText(String status) {
        switch (status) {
            case "pending": return "Chờ xử lý";
            case "processing": return "Đang xử lý";
            case "completed": return "Hoàn thành";
            case "draft": return "Bản nháp";
            default: return status;
        }
    }

    private String getStatusStyle(String status) {
        String baseStyle = "-fx-padding: 6 12; -fx-border-radius: 12; -fx-background-radius: 12; -fx-font-weight: bold;";
        switch (status) {
            case "pending":
                return baseStyle + "-fx-background-color: #FEF3C7; -fx-text-fill: #92400E;";
            case "processing":
                return baseStyle + "-fx-background-color: #DBEAFE; -fx-text-fill: #1E40AF;";
            case "completed":
                return baseStyle + "-fx-background-color: #D1FAE5; -fx-text-fill: #065F46;";
            case "draft":
                return baseStyle + "-fx-background-color: #F3F4F6; -fx-text-fill: #1F2937;";
            default:
                return baseStyle;
        }
    }

    @FXML
    private void onCreateRequest() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/CreateImportRequestDialog.fxml"),
                resources
            );
            Parent dialog = loader.load();
            // Show dialog logic here
            System.out.println("Create Import Request clicked");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onAddMerchandise() {
        try {
            loadView("/fxml/MerchandiseManagement.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onViewAll() {
        try {
            loadView("/fxml/ImportRequestsView.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleViewRequest(ImportRequest request) {
        System.out.println("View request: " + request.getCode());
        // Show request details dialog
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource(fxmlPath),
                resources
            );
            Parent view = loader.load();
            contentContainer.getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

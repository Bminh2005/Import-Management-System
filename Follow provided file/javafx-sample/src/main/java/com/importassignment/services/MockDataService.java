package com.importassignment.services;

import com.importassignment.models.Merchandise;
import com.importassignment.models.ImportRequest;
import com.importassignment.models.RequestItem;
import com.importassignment.models.Order;
import com.importassignment.models.OrderItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

/**
 * Mock Data Service - Provides sample data for testing
 */
public class MockDataService {

    private static MockDataService instance;
    private final ObservableList<Merchandise> merchandiseList;
    private final ObservableList<ImportRequest> importRequestList;
    private final ObservableList<Order> orderList;

    private MockDataService() {
        merchandiseList = FXCollections.observableArrayList();
        importRequestList = FXCollections.observableArrayList();
        orderList = FXCollections.observableArrayList();
        initializeMockData();
    }

    public static MockDataService getInstance() {
        if (instance == null) {
            instance = new MockDataService();
        }
        return instance;
    }

    private void initializeMockData() {
        // Mock Merchandise data
        merchandiseList.addAll(
            new Merchandise(1, "MH001", "Laptop Dell XPS 13", "cái", "Điện tử", "active",
                "25,000,000 VND", "Dell Vietnam",
                "Laptop cao cấp, màn hình 13.3 inch, CPU Intel Core i7 thế hệ 12"),

            new Merchandise(2, "MH002", "Bàn phím cơ Keychron K2", "cái", "Phụ kiện", "active",
                "2,500,000 VND", "Keychron",
                "Bàn phím cơ Bluetooth, switch Gateron Brown, tương thích Mac/Windows"),

            new Merchandise(3, "MH003", "Chuột Logitech MX Master 3", "cái", "Phụ kiện", "active",
                "2,200,000 VND", "Logitech",
                "Chuột không dây cao cấp, cảm biến 4000 DPI, pin 70 ngày"),

            new Merchandise(4, "MH004", "Màn hình LG 27 inch 4K", "cái", "Điện tử", "active",
                "8,500,000 VND", "LG Electronics",
                "Màn hình 27 inch UHD 4K, IPS, HDR10, USB-C"),

            new Merchandise(5, "MH005", "Tai nghe Sony WH-1000XM4", "cái", "Điện tử", "inactive",
                "7,500,000 VND", "Sony Vietnam",
                "Tai nghe chống ồn chủ động, Bluetooth 5.0, pin 30 giờ"),

            new Merchandise(6, "MH006", "USB-C Hub Anker 7-in-1", "cái", "Phụ kiện", "active",
                "850,000 VND", "Anker",
                "Hub đa năng: HDMI, USB 3.0, SD card, Ethernet"),

            new Merchandise(7, "MH007", "Webcam Logitech C920", "cái", "Phụ kiện", "active",
                "1,800,000 VND", "Logitech",
                "Webcam Full HD 1080p, tự động lấy nét, micro stereo"),

            new Merchandise(8, "MH008", "SSD Samsung 980 Pro 1TB", "cái", "Linh kiện", "active",
                "3,200,000 VND", "Samsung",
                "SSD NVMe M.2, tốc độ đọc 7000 MB/s, PCIe 4.0"),

            new Merchandise(9, "MH009", "RAM Corsair Vengeance 32GB", "bộ", "Linh kiện", "active",
                "3,500,000 VND", "Corsair",
                "RAM DDR4 3200MHz, 2x16GB, tản nhiệt RGB"),

            new Merchandise(10, "MH010", "Đế tản nhiệt laptop", "cái", "Phụ kiện", "active",
                "450,000 VND", "Cooler Master",
                "Đế laptop 6 quạt, điều chỉnh độ cao, LED RGB")
        );

        // Mock Import Request data with items
        ImportRequest req1 = new ImportRequest(1, "REQ-2024-001", "2024-05-08", 3, "pending");
        req1.addItem(new RequestItem(1, merchandiseList.get(0), 2, LocalDate.of(2024, 5, 15)));
        req1.addItem(new RequestItem(2, merchandiseList.get(1), 3, LocalDate.of(2024, 5, 16)));
        req1.addItem(new RequestItem(3, merchandiseList.get(2), 1, LocalDate.of(2024, 5, 15)));

        ImportRequest req2 = new ImportRequest(2, "REQ-2024-002", "2024-05-07", 5, "processing");
        req2.addItem(new RequestItem(4, merchandiseList.get(3), 1, LocalDate.of(2024, 5, 20)));
        req2.addItem(new RequestItem(5, merchandiseList.get(6), 2, LocalDate.of(2024, 5, 20)));
        req2.addItem(new RequestItem(6, merchandiseList.get(7), 1, LocalDate.of(2024, 5, 22)));

        ImportRequest req3 = new ImportRequest(3, "REQ-2024-003", "2024-05-06", 2, "completed");
        req3.addItem(new RequestItem(7, merchandiseList.get(0), 2, LocalDate.of(2024, 5, 15)));
        req3.addItem(new RequestItem(8, merchandiseList.get(1), 3, LocalDate.of(2024, 5, 16)));
        req3.addItem(new RequestItem(9, merchandiseList.get(2), 1, LocalDate.of(2024, 5, 15)));

        ImportRequest req4 = new ImportRequest(4, "REQ-2024-004", "2024-05-05", 4, "draft");
        req4.addItem(new RequestItem(10, merchandiseList.get(4), 2, LocalDate.of(2024, 5, 18)));
        req4.addItem(new RequestItem(11, merchandiseList.get(5), 5, LocalDate.of(2024, 5, 19)));
        req4.addItem(new RequestItem(12, merchandiseList.get(8), 2, LocalDate.of(2024, 5, 20)));
        req4.addItem(new RequestItem(13, merchandiseList.get(9), 3, LocalDate.of(2024, 5, 18)));

        importRequestList.addAll(req1, req2, req3, req4);

        // Mock Order data - orders created from processed import requests
        // Order for REQ-2024-003 (completed)
        Order order1 = new Order("ORD-2024-001", "REQ-2024-003",
            LocalDate.of(2024, 5, 6), "completed", "Site A - Hanoi");
        order1.addItem(new OrderItem(merchandiseList.get(0), 2, 25000000, LocalDate.of(2024, 5, 15)));
        order1.addItem(new OrderItem(merchandiseList.get(2), 1, 2200000, LocalDate.of(2024, 5, 15)));

        Order order2 = new Order("ORD-2024-002", "REQ-2024-003",
            LocalDate.of(2024, 5, 6), "completed", "Site B - HCMC");
        order2.addItem(new OrderItem(merchandiseList.get(1), 3, 2500000, LocalDate.of(2024, 5, 16)));

        // Order for REQ-2024-002 (processing)
        Order order3 = new Order("ORD-2024-003", "REQ-2024-002",
            LocalDate.of(2024, 5, 7), "processing", "Site A - Hanoi");
        order3.addItem(new OrderItem(merchandiseList.get(3), 1, 8500000, LocalDate.of(2024, 5, 20)));
        order3.addItem(new OrderItem(merchandiseList.get(6), 2, 1800000, LocalDate.of(2024, 5, 20)));

        Order order4 = new Order("ORD-2024-004", "REQ-2024-002",
            LocalDate.of(2024, 5, 7), "processing", "Site C - Da Nang");
        order4.addItem(new OrderItem(merchandiseList.get(7), 1, 3200000, LocalDate.of(2024, 5, 22)));

        orderList.addAll(order1, order2, order3, order4);
    }

    public ObservableList<Merchandise> getAllMerchandise() {
        return merchandiseList;
    }

    public ObservableList<Merchandise> getActiveMerchandise() {
        return merchandiseList.filtered(Merchandise::isActive);
    }

    public ObservableList<ImportRequest> getAllImportRequests() {
        return importRequestList;
    }

    public void addMerchandise(Merchandise merchandise) {
        merchandiseList.add(merchandise);
    }

    public void removeMerchandise(Merchandise merchandise) {
        merchandiseList.remove(merchandise);
    }

    public void addImportRequest(ImportRequest request) {
        importRequestList.add(request);
    }

    public void updateImportRequest(ImportRequest request) {
        // Update logic here
    }

    public String generateNextMerchandiseCode() {
        int maxId = merchandiseList.stream()
            .mapToInt(m -> {
                try {
                    return Integer.parseInt(m.getCode().substring(2));
                } catch (Exception e) {
                    return 0;
                }
            })
            .max()
            .orElse(0);
        return String.format("MH%03d", maxId + 1);
    }

    public String generateNextRequestCode() {
        int maxId = importRequestList.stream()
            .mapToInt(r -> {
                try {
                    String[] parts = r.getCode().split("-");
                    return Integer.parseInt(parts[parts.length - 1]);
                } catch (Exception e) {
                    return 0;
                }
            })
            .max()
            .orElse(0);
        return String.format("REQ-2024-%03d", maxId + 1);
    }

    public ObservableList<Order> getAllOrders() {
        return orderList;
    }

    public ObservableList<Order> getOrdersByRequestCode(String requestCode) {
        return orderList.filtered(order -> order.getImportRequestCode().equals(requestCode));
    }

    public void addOrder(Order order) {
        orderList.add(order);
    }
}

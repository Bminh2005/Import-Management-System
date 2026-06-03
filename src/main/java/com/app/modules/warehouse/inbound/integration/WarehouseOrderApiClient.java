package com.app.modules.warehouse.inbound.integration;

import com.app.modules.warehouse.inbound.dto.InboundOrderItemResponse;
import com.app.modules.warehouse.inbound.dto.InboundOrderResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class WarehouseOrderApiClient {
    private static final String DEFAULT_ORDERS_ENDPOINT =
            "https://warehousesystem-wi8b.onrender.com/api/orders";
    private static final DateTimeFormatter VI_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final HttpClient httpClient;
    private final String ordersEndpoint;

    public WarehouseOrderApiClient() {
        this(HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(6))
                .build(), resolveOrdersEndpoint());
    }

    WarehouseOrderApiClient(HttpClient httpClient, String ordersEndpoint) {
        this.httpClient = httpClient;
        this.ordersEndpoint = ordersEndpoint == null ? "" : ordersEndpoint.trim();
    }

    public WarehouseOrderSyncResult postOrder(InboundOrderResponse order, List<InboundOrderItemResponse> items) {
        if (ordersEndpoint.isBlank()) {
            return WarehouseOrderSyncResult.skipped("Chưa cấu hình endpoint đồng bộ hệ thống kho.");
        }
        if (order == null || items == null || items.isEmpty()) {
            return WarehouseOrderSyncResult.skipped("Không có dữ liệu đơn nhập kho để đồng bộ.");
        }

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ordersEndpoint))
                    .timeout(Duration.ofSeconds(15))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(buildPayload(order, items)))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            if (statusCode >= 200 && statusCode < 300) {
                return WarehouseOrderSyncResult.success(statusCode, "Đã đồng bộ đơn sang hệ thống kho riêng.");
            }
            return WarehouseOrderSyncResult.failure(statusCode,
                    "Hệ thống kho riêng trả về HTTP " + statusCode + ".");
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            return WarehouseOrderSyncResult.failure(0, "Đồng bộ hệ thống kho bị gián đoạn.");
        } catch (IllegalArgumentException | IOException exception) {
            return WarehouseOrderSyncResult.failure(0,
                    "Không thể gọi API hệ thống kho riêng: " + exception.getMessage());
        }
    }

    private static String resolveOrdersEndpoint() {
        String endpointFromProperty = System.getProperty("warehouse.api.orders.url");
        if (endpointFromProperty != null && !endpointFromProperty.isBlank()) {
            return endpointFromProperty;
        }

        String endpointFromEnv = System.getenv("WAREHOUSE_API_ORDERS_URL");
        if (endpointFromEnv != null && !endpointFromEnv.isBlank()) {
            return endpointFromEnv;
        }
        return DEFAULT_ORDERS_ENDPOINT;
    }

    private String buildPayload(InboundOrderResponse order, List<InboundOrderItemResponse> items) {
        StringBuilder json = new StringBuilder();
        json.append('{');
        appendField(json, "code", order.getOrderCode());
        json.append(',');
        appendField(json, "receiptDate", resolveReceiptDate(order));
        json.append(",\"items\":[");
        for (int index = 0; index < items.size(); index++) {
            if (index > 0) {
                json.append(',');
            }
            appendItem(json, items.get(index));
        }
        json.append("]}");
        return json.toString();
    }

    private void appendItem(StringBuilder json, InboundOrderItemResponse item) {
        json.append('{');
        appendField(json, "productCode", item.getProductCode());
        json.append(',');
        appendField(json, "productName", item.getProductName());
        json.append(',');
        appendNumberField(json, "qtyOrdered", item.getOrderedQuantity());
        json.append(',');
        appendNumberField(json, "qtyReceived", item.getActualQuantity());

        String reason = item.getDiscrepancyReason();
        if (item.hasMismatch() && reason != null && !reason.isBlank()) {
            json.append(',');
            appendField(json, "reason", reason.trim());
        }
        json.append('}');
    }

    private String resolveReceiptDate(InboundOrderResponse order) {
        String receivedDate = normalizeDate(order.getReceivedDate());
        if (!receivedDate.isBlank()) {
            return receivedDate;
        }

        String expectedDate = normalizeDate(order.getExpectedDate());
        if (!expectedDate.isBlank()) {
            return expectedDate;
        }
        return LocalDate.now().toString();
    }

    private String normalizeDate(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }

        String trimmed = value.trim();
        try {
            return LocalDate.parse(trimmed, DateTimeFormatter.ISO_LOCAL_DATE).toString();
        } catch (DateTimeParseException ignored) {
            // Fall through to the Vietnamese UI date format.
        }

        try {
            return LocalDate.parse(trimmed, VI_DATE_FORMATTER).toString();
        } catch (DateTimeParseException ignored) {
            return trimmed;
        }
    }

    private void appendField(StringBuilder json, String fieldName, String value) {
        json.append('"')
                .append(fieldName)
                .append("\":\"")
                .append(escape(value))
                .append('"');
    }

    private void appendNumberField(StringBuilder json, String fieldName, int value) {
        json.append('"')
                .append(fieldName)
                .append("\":")
                .append(value);
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }

        StringBuilder escaped = new StringBuilder(value.length() + 8);
        for (int index = 0; index < value.length(); index++) {
            char character = value.charAt(index);
            switch (character) {
                case '"' -> escaped.append("\\\"");
                case '\\' -> escaped.append("\\\\");
                case '\b' -> escaped.append("\\b");
                case '\f' -> escaped.append("\\f");
                case '\n' -> escaped.append("\\n");
                case '\r' -> escaped.append("\\r");
                case '\t' -> escaped.append("\\t");
                default -> {
                    if (character < 0x20) {
                        escaped.append(String.format("\\u%04x", (int) character));
                    } else {
                        escaped.append(character);
                    }
                }
            }
        }
        return escaped.toString();
    }
}

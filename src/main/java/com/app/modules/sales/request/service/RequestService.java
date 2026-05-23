package com.app.modules.sales.request.service;

import com.app.modules.sales.request.dto.RequestResponse;
import com.app.modules.sales.request.dto.UpdateRequestDTO;
import com.app.modules.sales.request.entity.Request;
import com.app.modules.sales.request.entity.RequestItem;
import com.app.modules.sales.request.repository.RequestRepository;

import java.util.NoSuchElementException;

/**
 * Service cho Request: chứa business logic và điều phối workflow
 * giữa UI và Repository theo đúng quy ước trong README.
 *
 * UI chỉ gọi service, service gọi repository — không bao giờ
 * UI chạm thẳng repository hoặc viết SQL.
 */
public class RequestService {

    private final RequestRepository repository;

    public RequestService() {
        this(new RequestRepository());
    }

    public RequestService(RequestRepository repository) {
        this.repository = repository;
    }

    /** Lấy chi tiết yêu cầu nhập hàng theo mã, dùng cho 2 màn hình Xem / Sửa. */
    public RequestResponse getRequestDetail(String code) {
        Request request = repository.findById(code)
                .orElseThrow(() -> new NoSuchElementException(
                        "Không tìm thấy yêu cầu " + code));
        return new RequestResponse(request);
    }

    /**
     * Lưu thay đổi từ màn hình Chỉnh sửa.
     * Áp danh sách item mới (đã thêm/xóa/sửa) vào entity rồi ghi xuống repository.
     */
    public RequestResponse updateRequest(UpdateRequestDTO dto) {
        Request request = repository.findById(dto.getCode())
                .orElseThrow(() -> new NoSuchElementException(
                        "Không tìm thấy yêu cầu " + dto.getCode()));

        request.getItems().setAll(dto.getItems());
        repository.save(request);
        return new RequestResponse(request);
    }

    /** Cập nhật số lượng / ngày nhận cho 1 mặt hàng trong yêu cầu. */
    public void updateItemQuantity(String code, String itemCode, int newQuantity) {
        repository.findById(code).ifPresent(req ->
                req.getItems().stream()
                        .filter(i -> i.getCode().equals(itemCode))
                        .findFirst()
                        .ifPresent(i -> i.setQuantity(newQuantity)));
    }

    public void updateItemDeliveryDate(String code, String itemCode, String date) {
        repository.findById(code).ifPresent(req ->
                req.getItems().stream()
                        .filter(i -> i.getCode().equals(itemCode))
                        .findFirst()
                        .ifPresent(i -> i.setDeliveryDate(date)));
    }

    /** Xóa 1 mặt hàng khỏi yêu cầu. */
    public void removeItem(String code, String itemCode) {
        repository.findById(code).ifPresent(req ->
                req.getItems().removeIf(i -> i.getCode().equals(itemCode)));
    }

    /** Thêm 1 mặt hàng vào yêu cầu (gọi từ popup "Thêm mặt hàng"). */
    public void addItem(String code, RequestItem item) {
        repository.findById(code).ifPresent(req -> req.getItems().add(item));
    }

    /** Hủy toàn bộ yêu cầu kèm lý do (đổi trạng thái sang cancelled). */
    public void cancelRequest(String code, String reason) {
        repository.findById(code).ifPresent(req -> {
            req.setStatus("cancelled");
            repository.save(req);
            System.out.println("[RequestService] Đã hủy yêu cầu " + code
                    + " - Lý do: " + reason);
        });
    }
}

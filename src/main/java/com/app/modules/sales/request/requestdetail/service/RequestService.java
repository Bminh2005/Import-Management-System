package com.app.modules.sales.request.requestdetail.service;

import com.app.modules.sales.request.requestdetail.dto.OrderDetailResponse;
import com.app.modules.sales.request.requestdetail.dto.RequestResponse;
import com.app.modules.sales.request.requestdetail.dto.UpdateRequestDTO;
import com.app.modules.sales.request.entity.OrderItem;
import com.app.modules.sales.request.entity.RelatedOrder;
import com.app.modules.sales.request.entity.Request;
import com.app.modules.sales.request.entity.RequestItem;
import com.app.modules.sales.request.requestdetail.repository.RequestRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Service cho Request: chứa business logic và điều phối workflow
 * giữa UI và Repository. Theo quy ước README: UI chỉ gọi service,
 * service gọi repository - không bao giờ UI chạm thẳng repository.
 */
public class RequestService {

    private final RequestRepository repository;

    public RequestService() {
        this(new RequestRepository());
    }

    public RequestService(RequestRepository repository) {
        this.repository = repository;
    }

    /** Lấy chi tiết yêu cầu nhập hàng (cho 2 màn Xem / Sửa). */
    public RequestResponse getRequestDetail(String code) {
        Request request = repository.findById(code)
                .orElseThrow(() -> new NoSuchElementException(
                        "Không tìm thấy yêu cầu " + code));
        return new RequestResponse(request);
    }

    /**
     * Lưu thay đổi từ màn Chỉnh sửa: đồng bộ toàn bộ danh sách item
     * trong DTO xuống DB theo kiểu upsert.
     */
    public RequestResponse updateRequest(UpdateRequestDTO dto) {
        Request request = repository.findById(dto.getCode())
                .orElseThrow(() -> new NoSuchElementException(
                        "Không tìm thấy yêu cầu " + dto.getCode()));

        Set<String> keep = new HashSet<>();
        int pos = 0;
        for (RequestItem item : dto.getItems()) {
            repository.insertItem(dto.getCode(), item, pos++);
            keep.add(item.getCode());
        }
        for (RequestItem old : request.getItems()) {
            if (!keep.contains(old.getCode())) {
                repository.deleteItem(dto.getCode(), old.getCode());
            }
        }

        // Ngày nhận lưu ở cấp yêu cầu (schema chỉ có 1 desired_date / yêu cầu).
        for (RequestItem item : dto.getItems()) {
            String date = item.getDeliveryDate();
            if (date != null && !date.isBlank()) {
                repository.updateItemDeliveryDate(dto.getCode(), item.getCode(), date);
                break;
            }
        }

        Request fresh = repository.findById(dto.getCode())
                .orElseThrow(() -> new NoSuchElementException(
                        "Không tìm thấy yêu cầu " + dto.getCode()));
        return new RequestResponse(fresh);
    }

    public void updateItemQuantity(String code, String itemCode, int newQuantity) {
        repository.updateItemQuantity(code, itemCode, newQuantity);
    }

    public void updateItemDeliveryDate(String code, String itemCode, String date) {
        repository.updateItemDeliveryDate(code, itemCode, date);
    }

    public void removeItem(String code, String itemCode) {
        repository.deleteItem(code, itemCode);
    }

    /**
     * Danh sách sản phẩm khả dụng cho yêu cầu hiện tại:
     * master list (tất cả sản phẩm từng có) trừ các mã đã có trong
     * yêu cầu hiện tại.
     */
    public List<RequestItem> listAvailableProducts(String currentRequestCode) {
        Set<String> excluded = new HashSet<>();
        repository.findById(currentRequestCode).ifPresent(req ->
                req.getItems().forEach(i -> excluded.add(i.getCode())));
        List<RequestItem> result = new ArrayList<>();
        for (RequestItem p : repository.findAllProducts()) {
            if (!excluded.contains(p.getCode())) result.add(p);
        }
        return result;
    }

    /** Thêm 1 mặt hàng vào yêu cầu (gọi từ popup "Thêm mặt hàng"). */
    public void addItem(String code, RequestItem item) {
        Request request = repository.findById(code)
                .orElseThrow(() -> new NoSuchElementException(
                        "Không tìm thấy yêu cầu " + code));
        int position = request.getItems().size();
        repository.insertItem(code, item, position);
    }

    /**
     * Lấy chi tiết 1 đơn hàng quốc tế (cho popup "Chi tiết Đơn hàng").
     * DB hiện chưa có bảng order_items: items được dựng từ
     * item_count mặt hàng đầu của yêu cầu gốc.
     */
    public OrderDetailResponse getOrderDetail(String orderCode) {
        RequestRepository.RelatedOrderWithRequest wrap = repository.findRelatedOrder(orderCode)
                .orElseThrow(() -> new NoSuchElementException(
                        "Không tìm thấy đơn hàng " + orderCode));

        RelatedOrder order = wrap.getOrder();
        List<RequestItem> requestItems = repository.findRequestItems(wrap.getRequestCode());

        List<OrderItem> items = new ArrayList<>();
        int limit = Math.min(order.getItemCount(), requestItems.size());
        for (int i = 0; i < limit; i++) {
            RequestItem src = requestItems.get(i);
            items.add(new OrderItem(src.getCode(), src.getName(),
                    src.getQuantity(), src.getUnit()));
        }

        return new OrderDetailResponse(
                order.getCode(), order.getOrderDate(), order.getStatus(),
                wrap.getRequestCode(), order.getSite(), items);
    }
}

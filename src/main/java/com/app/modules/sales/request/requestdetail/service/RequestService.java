package com.app.modules.sales.request.requestdetail.service;

import com.app.modules.sales.request.requestdetail.dto.OrderDetailResponse;
import com.app.modules.sales.request.requestdetail.dto.RequestResponse;
import com.app.modules.sales.request.entity.OrderItem;
import com.app.modules.sales.request.entity.RelatedOrder;
import com.app.modules.sales.request.entity.Request;
import com.app.modules.sales.request.entity.RequestItem;
import com.app.modules.sales.request.requestdetail.repository.RequestRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service cho màn xem chi tiết yêu cầu nhập hàng.
 */
public class RequestService {

    private final RequestRepository repository;

    public RequestService() {
        this(new RequestRepository());
    }

    public RequestService(RequestRepository repository) {
        this.repository = repository;
    }

    public RequestResponse getRequestDetail(String code) {
        Request request = repository.findById(code)
                .orElseThrow(() -> new NoSuchElementException(
                        "Không tìm thấy yêu cầu " + code));
        return new RequestResponse(request);
    }

    /** Lấy chi tiết 1 đơn hàng quốc tế (popup "Chi tiết Đơn hàng"). */
    public OrderDetailResponse getOrderDetail(String orderCode) {
        RequestRepository.RelatedOrderWithRequest wrap = repository.findRelatedOrder(orderCode)
                .orElseThrow(() -> new NoSuchElementException(
                        "Không tìm thấy đơn hàng " + orderCode));

        RelatedOrder order = wrap.getOrder();
        List<OrderItem> items = repository.findOrderItems(orderCode);
        if (items.isEmpty()) {
            List<RequestItem> requestItems = repository.findRequestItems(wrap.getRequestCode());
            int limit = Math.min(order.getItemCount(), requestItems.size());
            for (int i = 0; i < limit; i++) {
                RequestItem src = requestItems.get(i);
                items.add(new OrderItem(src.getCode(), src.getName(),
                        src.getQuantity(), src.getUnit()));
            }
        }

        return new OrderDetailResponse(
                order.getCode(), order.getOrderDate(), order.getStatus(),
                wrap.getRequestCode(), order.getSite(), items);
    }
}

package com.app.modules.sales.request.editrequest.service;

import com.app.modules.sales.request.editrequest.dto.RequestResponse;
import com.app.modules.sales.request.editrequest.dto.UpdateRequestDTO;
import com.app.modules.sales.request.entity.Request;
import com.app.modules.sales.request.entity.RequestItem;
import com.app.modules.sales.request.editrequest.repository.RequestRepository;

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

    /** Lấy chi tiết yêu cầu nhập hàng. */
    public RequestResponse getRequestDetail(String code) {
        return new RequestResponse(findRequestOrThrow(code));
    }

    /**
     * Lưu thay đổi từ màn Chỉnh sửa: đồng bộ toàn bộ danh sách item
     * trong DTO xuống DB theo kiểu upsert. Chỉ cho phép khi yêu cầu PENDING.
     */
    public RequestResponse updateRequest(UpdateRequestDTO dto) {
        Request request = findRequestOrThrow(dto.getCode());
        if (!"pending".equals(request.getStatus())) {
            throw new IllegalStateException("Yêu cầu " + dto.getCode()
                    + " đang ở trạng thái " + request.getStatus() + ", không thể chỉnh sửa.");
        }

        Set<String> keep = new HashSet<>();
        int pos = 0;
        for (RequestItem item : dto.getItems()) {
            repository.insertItem(dto.getCode(), item, pos++);
            repository.updateItemDeliveryDate(dto.getCode(), item.getCode(), item.getDeliveryDate());
            keep.add(item.getCode());
        }
        for (RequestItem old : request.getItems()) {
            if (!keep.contains(old.getCode())) {
                repository.deleteItem(dto.getCode(), old.getCode());
            }
        }

        return new RequestResponse(findRequestOrThrow(dto.getCode()));
    }

    /**
     * Danh sách sản phẩm khả dụng cho yêu cầu hiện tại:
     * master list trừ các mã đã có trong yêu cầu.
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

    private Request findRequestOrThrow(String code) {
        return repository.findById(code)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy yêu cầu " + code));
    }
}

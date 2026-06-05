package com.app.modules.sales.request.requestlist.service;

import com.app.modules.sales.request.requestlist.dto.RequestListRow;
import com.app.modules.sales.request.requestlist.repository.RequestListRepository;

import java.util.List;

/**
 * Service cho màn danh sách yêu cầu nhập hàng.
 */
public class RequestListService {

    private final RequestListRepository repository;

    public RequestListService() {
        this(new RequestListRepository());
    }

    public RequestListService(RequestListRepository repository) {
        this.repository = repository;
    }

    public List<RequestListRow> listRequests() {
        return repository.findAllSummaries();
    }
}

package com.app.modules.sales.request.createrequest.repository;

import com.app.common.entity.RequestDetail;
import com.app.modules.sales.request.createrequest.dto.MerchandiseDTO;

import java.util.List;

public interface ICreateRequestRepository {
    public List<MerchandiseDTO> getAllMerchandise();
    public long createRequest(List<RequestDetail> merchandiseList);
}

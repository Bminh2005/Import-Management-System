package com.app.modules.sales.request.createrequest.repository;

import com.app.common.entity.RequestDetail;
import com.app.modules.sales.request.createrequest.dto.MerchandiseDTO;

import java.util.List;

public class MySQLCreateRequestRepository implements ICreateRequestRepository{
    public List<MerchandiseDTO> getAllMerchandise(){
        return null;
    }

    @Override
    public long createRequest(List<RequestDetail> merchandiseList) {
        return 0;
    }
}

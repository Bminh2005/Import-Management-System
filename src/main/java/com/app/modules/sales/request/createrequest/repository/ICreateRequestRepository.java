package com.app.modules.sales.request.createrequest.repository;

import com.app.modules.sales.request.createrequest.dto.MerchandiseDTO;

import java.util.List;

public interface ICreateRequestRepository {
    public List<MerchandiseDTO> getAllMerchandise();
}

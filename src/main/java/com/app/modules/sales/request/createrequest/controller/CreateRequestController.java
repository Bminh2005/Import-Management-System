package com.app.modules.sales.request.createrequest.controller;

import com.app.common.entity.Merchandise;
import com.app.modules.sales.request.createrequest.dto.MerchandiseDTO;
import com.app.modules.sales.request.createrequest.mapper.MerchandiseMapper;
import com.app.modules.sales.request.createrequest.service.CreateRequestService;
import com.app.modules.sales.request.createrequest.ui.CreateImportRequestUI;
import com.app.modules.sales.request.createrequest.ui.model.CreateImportItemModel;
import javafx.collections.ObservableList;

import java.util.List;

public class CreateRequestController {
    private CreateImportRequestUI view;
    private CreateRequestService service;
    public CreateRequestController() {
        this.view = new CreateImportRequestUI();
        this.service = new CreateRequestService();
        init();
    }
    private void init(){
        List<MerchandiseDTO> allMerchandise = service.fetchMerchandiseList();
        ObservableList<CreateImportItemModel> models = MerchandiseMapper.mapToCreateImportItemModel(allMerchandise);
        view.setAvailableProducts(models);
    }
    public CreateImportRequestUI getView() {
        return view;
    }

    public Merchandise getAllMerchandises(){
        return null;
    }
}

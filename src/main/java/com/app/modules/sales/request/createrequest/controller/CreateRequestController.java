package com.app.modules.sales.request.createrequest.controller;

import com.app.common.entity.Merchandise;
import com.app.common.exception.BusinessException;
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
    private Runnable onCreated;
    public CreateRequestController() {
        this.view = new CreateImportRequestUI();
        this.service = new CreateRequestService();
        init();
    }
    private void init(){
        try{
            List<MerchandiseDTO> allMerchandise = service.fetchMerchandiseList();
            ObservableList<CreateImportItemModel> models = MerchandiseMapper.mapToCreateImportItemModel(allMerchandise);
            view.setAvailableProducts(models);
        }
        catch (BusinessException e){
            this.view.showToastNotification(e.getMessage(),false);
        }
        try{
            view.setCreateButtonAction(this::SubmitCreateRequest);
        } catch (RuntimeException e) {
            view.showToastNotification(e.getMessage(),false);
            e.getCause().printStackTrace();
        }
    }

    public CreateImportRequestUI getView() {
        return view;
    }

    public void setOnCreated(Runnable callback) {
        this.onCreated = callback;
    }

    private void SubmitCreateRequest(){
        try{
            ObservableList<CreateImportItemModel> list = view.getItems();
            long id = service.saveNewRequest(list);
            view.showToastNotification(String.format("%s%d","Đã tạo yêu cầu thành công! ID: ",id), true);
            if (onCreated != null) {
                onCreated.run();
            }
        }catch (BusinessException e){
            view.showToastNotification(e.getMessage(),false);
        }
    }
}

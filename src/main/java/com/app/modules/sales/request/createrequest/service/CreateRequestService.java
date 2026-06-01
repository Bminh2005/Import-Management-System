package com.app.modules.sales.request.createrequest.service;

import com.app.common.entity.RequestDetail;
import com.app.common.exception.BusinessException;
import com.app.common.exception.DatabaseOperationException;
import com.app.modules.sales.request.createrequest.dto.MerchandiseDTO;
import com.app.modules.sales.request.createrequest.factory.CreateRequestRepositoryFactory;
import com.app.modules.sales.request.createrequest.repository.ICreateRequestRepository;
import com.app.modules.sales.request.createrequest.ui.model.CreateImportItemModel;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class CreateRequestService {

        public List<MerchandiseDTO> fetchMerchandiseList(){
            try{
                ICreateRequestRepository dao = CreateRequestRepositoryFactory.getRepository();
                return dao.getAllMerchandise();
            }
            catch (DatabaseOperationException e){
                if(e.getMessage().equals("Failed connection with database")){
                    System.out.println("Failed connection with database");
                    throw new BusinessException("The system is currently down. Please try again later.");
                }
                else{
                    throw new BusinessException("Something went wrong. Please try again later.");
                }
            }
            catch (NullPointerException e){
                throw new BusinessException("Error: DAO in CreateRequestService class is null! " +
                        "Please contact the system developer to solve this issue.");
            }

        }

        public long saveNewRequest(ObservableList<CreateImportItemModel> merchandiseList){
            if(merchandiseList.isEmpty()){
                throw new BusinessException("Bạn cần chọn ít nhất một mặt hàng!");
            }
            List<RequestDetail> list = new ArrayList<>();
            for(CreateImportItemModel item: merchandiseList){
                RequestDetail requestDetail = new RequestDetail(item.getMerchandiseDetailId(), item.getQuantity());
                requestDetail.setDesired_date(item.getExpectedDate());
                list.add(requestDetail);
            }
            try{
                ICreateRequestRepository dao = CreateRequestRepositoryFactory.getRepository();
                long requestId = dao.createRequest(list);
                System.out.println("Bạn vừa tạo yêu cầu ID: "+ requestId);
                return requestId;
            }
            catch (DatabaseOperationException e){
                if(e.getMessage().equals("Failed connection with database")){
//                    System.out.println("Failed connection with database");
                    throw new BusinessException("Chưa thể kết nối với hệ thống. Kiểm tra kết nối rồi thử lại");
                }
                else{
                    throw new BusinessException("Something went wrong. Please try again later.");
                }
            }
            catch (NullPointerException e){
                throw new BusinessException("Error: DAO in CreateRequestService class is null! " +
                        "Please contact the system developer to solve this issue.");
            }
        }
}

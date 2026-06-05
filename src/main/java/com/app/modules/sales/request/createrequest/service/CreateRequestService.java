package com.app.modules.sales.request.createrequest.service;

import com.app.common.entity.RequestDetail;
import com.app.common.exception.BusinessException;
import com.app.common.exception.DatabaseOperationException;
import com.app.modules.sales.request.createrequest.dto.MerchandiseDTO;
import com.app.modules.sales.request.createrequest.factory.CreateRequestRepositoryFactory;
import com.app.modules.sales.request.createrequest.repository.ICreateRequestRepository;
import com.app.modules.sales.request.createrequest.ui.model.CreateImportItemModel;
import javafx.collections.ObservableList;

import java.time.LocalDate;
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

    public void validateImportItem(CreateImportItemModel item) {
        // Nhánh 1: Kiểm tra object null
        if (item == null) {
            throw new BusinessException("Dữ liệu mặt hàng không được để trống.");
        }

        // Nhánh 2: Kiểm tra ID mặt hàng
        if (item.getMerchandiseDetailId() <= 0) {
            throw new BusinessException("Mã mặt hàng không hợp lệ.");
        }

        // Nhánh 3: Kiểm tra biên dưới của số lượng (Black-box Boundary Value)
        if (item.getQuantity() <= 0) {
            throw new BusinessException("Số lượng nhập phải lớn hơn 0.");
        }

        // Nhánh 4: Kiểm tra null đối với ngày tháng
        if (item.getExpectedDate() == null) {
            throw new BusinessException("Ngày mong muốn nhận hàng không được để trống.");
        }

        // Nhánh 5: Kiểm tra logic thời gian (phải từ ngày mai trở đi)
        if (!item.getExpectedDate().isAfter(LocalDate.now())) {
            throw new BusinessException("Ngày mong muốn nhận hàng phải từ ngày mai trở đi.");
        }
    }

    public long saveNewRequest(ObservableList<CreateImportItemModel> merchandiseList){
        if(merchandiseList.isEmpty()){
            throw new BusinessException("Bạn cần chọn ít nhất một mặt hàng!");
        }
        List<RequestDetail> list = new ArrayList<>();
        for(CreateImportItemModel item: merchandiseList){
            validateImportItem(item);
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

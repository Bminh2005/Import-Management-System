package com.app.modules.sales.request.createrequest.service;

import com.app.common.exception.BusinessException;
import com.app.common.exception.DatabaseOperationException;
import com.app.modules.sales.request.createrequest.dto.MerchandiseDTO;
import com.app.modules.sales.request.createrequest.factory.CreateRequestRepositoryFactory;
import com.app.modules.sales.request.createrequest.repository.ICreateRequestRepository;

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
}

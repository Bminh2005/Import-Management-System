package com.app.auth.login.service;

import com.app.auth.login.dto.SignupDTO;
import com.app.auth.login.repository.SignupRepository;
import com.app.common.entity.User;
import com.app.common.exception.BusinessException;
import com.app.common.exception.DatabaseOperationException;

public class SignupService {
    private final SignupRepository signupRepository = new SignupRepository();
    public User signup(SignupDTO info){
        try{
          User userInfo = new User(info.getUsername(),info.getPassword(),info.getRole(),info.getFullname());
          User newUser =  signupRepository.save(userInfo);
          return newUser;
        } catch (DatabaseOperationException e){
            e.printStackTrace();
            throw new BusinessException("Xảy ra lỗi khi đăng ký. Vui lòng thử lại!");
        }
    }
}

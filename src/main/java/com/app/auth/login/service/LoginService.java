package com.app.auth.login.service;

import com.app.auth.login.dto.LoginDTO;
import com.app.auth.login.repository.LoginRepository;
import com.app.common.entity.User;
import com.app.common.exception.BusinessException;
import com.app.common.exception.DatabaseOperationException;

public class LoginService {
    private final LoginRepository loginRepository = new LoginRepository();
    public User login(LoginDTO loginDTO) {
        if(loginDTO.getUsername() == null){
            throw new BusinessException("Bạn cần điền đầy đủ username và password");
        }
        else if(loginDTO.getUsername().length() == 0){
            throw new BusinessException("Bạn cần điền đầy đủ username và password");
        }
        if(loginDTO.getPassword() == null){
            throw new BusinessException("Bạn cần điền đầy đủ username và password");
        }
        else if(loginDTO.getPassword().length() == 0){
            throw new BusinessException("Bạn cần điền đầy đủ username và password");
        }
        if(loginDTO.getRole() == null){
            throw new BusinessException("Bạn cần chọn vai trò trước khi đăng nhập");
        }
        else if(loginDTO.getRole().length() == 0) {
            throw new BusinessException("Bạn cần chọn vai trò trước khi đăng nhập");
        }

        User user = null;
        try{
            user = loginRepository.checkAccount(loginDTO.getUsername(), loginDTO.getPassword(), loginDTO.getRole());
            if(user == null){
                throw new BusinessException("Thông tin đăng nhập sai hoặc kết nối chưa ổn định");
            }
        } catch (DatabaseOperationException e){
            throw new BusinessException("Database error: " + e.getMessage());
        }
        return user;
    }
}

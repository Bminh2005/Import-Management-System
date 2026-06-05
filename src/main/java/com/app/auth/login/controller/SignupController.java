package com.app.auth.login.controller;

import com.app.auth.login.dto.SignupDTO;
import com.app.auth.login.service.SignupService;
import com.app.auth.login.ui.SignupUI;
import com.app.common.exception.BusinessException;
import com.app.common.navigator.Navigator;
import javafx.scene.Parent;

public class SignupController {
    private SignupUI view;
    private SignupService service;

    public SignupController() {
        view = new SignupUI();
        service = new SignupService();
        view.setSignupButtonAction(this::signupHandle);
        view.setOnActionForLoginLink(()->{
            Navigator.getInstance().goBack();
        });
    }
    public SignupUI getView(){
        return view;
    }
    private void signupHandle(){
        SignupDTO info = new SignupDTO(view.getUsername(), view.getPassword(), view.getRole(), view.getFullName());
        try{
            service.signup(info);
            view.showToastNotification("Đã đăng ký thành công", true);
            Navigator.getInstance().goBack();
        } catch (BusinessException e){
            view.showToastNotification(e.getMessage(), false);
        }
    }

}

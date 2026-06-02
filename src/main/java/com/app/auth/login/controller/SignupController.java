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
    private LoginController loginController;
    public SignupController(LoginController loginController) {
        service = new SignupService();
        view = new SignupUI();
        this.loginController = loginController;
        view.setSignupButtonAction(()->{
            signupHandle();
            Navigator.getInstance().goBack();
        });
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
        } catch (BusinessException e){
            view.showToastNotification(e.getMessage(), false);
        }
    }

}

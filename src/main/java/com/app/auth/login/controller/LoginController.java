package com.app.auth.login.controller;

import com.app.auth.login.dto.LoginDTO;
import com.app.auth.login.service.LoginService;
import com.app.auth.login.ui.LoginUI;
import com.app.auth.security.CurrentUserSession;
import com.app.common.entity.User;
import com.app.common.exception.BusinessException;
import com.app.common.navigator.Navigator;
import com.app.modules.sales.dashboard.ui.SalesMainUI;
import javafx.scene.Parent;

public class LoginController {
    private LoginUI loginUI;
    private LoginService loginService;
    private SignupController signupController;
    public LoginController() {
        loginUI = new LoginUI();
        loginService = new LoginService();

        loginUI.setLoginButtonAction(this::loginHandle);
        loginUI.setOnActionForSignupLink(()->{
            this.signupController = new SignupController(this);
            Navigator.getInstance().navigateTo(signupController.getView());
        });
    }
    public LoginController(SignupController signupController) {
        loginUI = new LoginUI();
        loginService = new LoginService();
        this.signupController = signupController;
        loginUI.setLoginButtonAction(this::loginHandle);
        loginUI.setOnActionForSignupLink(()->{

        });
    }

    public LoginUI getLoginUI() {
        return loginUI;
    }

    public LoginService getLoginService() {
        return loginService;
    }

    public void loginHandle(){
        LoginDTO loginInfo = new LoginDTO(loginUI.getUsername(), loginUI.getPassword(), loginUI.getRole());
        try {
            User user = loginService.login(loginInfo);
            if (user == null) {
                loginUI.showToastNotification("Đăng nhập thất bại!", false);
            } else {
                CurrentUserSession.login(user.getId(), loginInfo.getUsername(), loginInfo.getRole(), user.getFullname());
                loginUI.showToastNotification("Đăng nhập thành công UserID: " + CurrentUserSession.getCurrentUserId(), true);
                if ("SALES".equalsIgnoreCase(loginInfo.getRole())) {
                    Navigator.getInstance().navigateTo(new SalesMainUI());
                }
            }
        } catch (BusinessException e) {
            loginUI.showToastNotification(e.getMessage(), false);
        }
    }

    public SignupController getSignupController() {
        return signupController;
    }
}

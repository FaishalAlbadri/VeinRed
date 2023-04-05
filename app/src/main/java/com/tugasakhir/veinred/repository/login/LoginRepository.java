package com.tugasakhir.veinred.repository.login;

import org.jetbrains.annotations.NotNull;

public class LoginRepository implements LoginDataResource {

    private LoginDataResource loginDataResource;

    public LoginRepository(LoginDataResource loginDataResource) {
        this.loginDataResource = loginDataResource;
    }

    @Override
    public void getLoginResult(String user_email, String user_password, @NotNull LoginGetCallback loginGetCallback) {
        loginDataResource.getLoginResult(user_email, user_password, loginGetCallback);
    }
}

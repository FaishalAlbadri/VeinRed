package com.tugasakhir.veinred.presenter.login;

import com.tugasakhir.veinred.data.user.UserItem;
import com.tugasakhir.veinred.repository.login.LoginDataResource;
import com.tugasakhir.veinred.repository.login.LoginRepository;

import java.util.List;

public class LoginPresenter implements LoginContract.loginPresenter {

    private LoginRepository loginRepository;
    private LoginContract.loginView loginView;

    public LoginPresenter(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    @Override
    public void getDataLogin(String user_email, String user_password) {
        loginRepository.getLoginResult(user_email, user_password, new LoginDataResource.LoginGetCallback() {
            @Override
            public void onSuccessLogin(List<UserItem> userItems) {
                loginView.onSuccessLogin(userItems);
            }

            @Override
            public void onErrorLogin(String msg) {
                loginView.onErrorLogin(msg);
            }
        });
    }

    @Override
    public void onAttachView(LoginContract.loginView view) {
        loginView = view;
    }

    @Override
    public void onDettachView() {

    }
}

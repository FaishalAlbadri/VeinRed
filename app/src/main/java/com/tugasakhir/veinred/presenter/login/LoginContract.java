package com.tugasakhir.veinred.presenter.login;

import com.tugasakhir.veinred.base.BasePresenter;

public class LoginContract {

    public interface loginView {

        void onSuccessLogin(String id_user);

        void onErrorLogin(String msg);
    }

    public interface loginPresenter extends BasePresenter<loginView> {

        void getDataLogin(String user_email, String user_password);

    }

}

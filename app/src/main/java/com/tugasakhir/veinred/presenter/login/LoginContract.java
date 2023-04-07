package com.tugasakhir.veinred.presenter.login;

import com.tugasakhir.veinred.base.BasePresenter;
import com.tugasakhir.veinred.data.user.UserItem;

import java.util.List;

public class LoginContract {

    public interface loginView {

        void onSuccessLogin(List<UserItem> userItems);

        void onErrorLogin(String msg);
    }

    public interface loginPresenter extends BasePresenter<loginView> {

        void getDataLogin(String user_email, String user_password);

    }

}

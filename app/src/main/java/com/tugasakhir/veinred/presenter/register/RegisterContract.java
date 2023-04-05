package com.tugasakhir.veinred.presenter.register;


import com.tugasakhir.veinred.base.BasePresenter;

public class RegisterContract {

    public interface registerView {

        void onSuccessRegister();

        void onErrorRegister(String msg);
    }

    public interface registerPresenter extends BasePresenter<registerView> {

        void getDataRegister(String user_name, String user_email, String user_password);

    }

}

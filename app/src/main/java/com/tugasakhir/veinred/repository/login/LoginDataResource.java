package com.tugasakhir.veinred.repository.login;

import androidx.annotation.NonNull;

public interface LoginDataResource {

    void getLoginResult(String user_email, String user_password, @NonNull LoginGetCallback loginGetCallback);

    interface LoginGetCallback {

        void onSuccessLogin(String id_user);

        void onErrorLogin(String msg);
    }
}

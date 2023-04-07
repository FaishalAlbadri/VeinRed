package com.tugasakhir.veinred.repository.login;

import androidx.annotation.NonNull;

import com.tugasakhir.veinred.data.user.UserItem;

import java.util.List;

public interface LoginDataResource {

    void getLoginResult(String user_email, String user_password, @NonNull LoginGetCallback loginGetCallback);

    interface LoginGetCallback {

        void onSuccessLogin(List<UserItem> userItems);

        void onErrorLogin(String msg);
    }
}

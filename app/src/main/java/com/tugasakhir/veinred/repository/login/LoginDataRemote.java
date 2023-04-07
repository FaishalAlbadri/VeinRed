package com.tugasakhir.veinred.repository.login;

import android.content.Context;

import com.tugasakhir.veinred.api.APIClient;
import com.tugasakhir.veinred.api.APIInterface;
import com.tugasakhir.veinred.api.Server;
import com.tugasakhir.veinred.data.BaseResponse;
import com.tugasakhir.veinred.data.user.UserItem;
import com.tugasakhir.veinred.data.user.UserResponse;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginDataRemote implements LoginDataResource {

    private APIInterface apiInterface;

    public LoginDataRemote(Context context) {
        apiInterface = APIClient.getRetrofit(context).create(APIInterface.class);
    }

    @Override
    public void getLoginResult(String user_email, String user_password, @NotNull LoginGetCallback loginGetCallback) {

        final Call<UserResponse> userResponseCall = apiInterface.login(user_email, user_password);
        userResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                try {
                    if (response.body().getMsg().equals("Berhasil")) {
                        loginGetCallback.onSuccessLogin(response.body().getUser());
                    } else {
                        loginGetCallback.onErrorLogin(response.body().getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                loginGetCallback.onErrorLogin(Server.CHECK_INTERNET_CONNECTION);
            }
        });
    }
}

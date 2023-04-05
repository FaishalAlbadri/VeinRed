package com.tugasakhir.veinred.repository.register;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tugasakhir.veinred.api.APIClient;
import com.tugasakhir.veinred.api.APIInterface;
import com.tugasakhir.veinred.api.Server;
import com.tugasakhir.veinred.data.BaseResponse;
import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterDataRemote implements RegisterDataResource {

    private APIInterface apiInterface;

    public RegisterDataRemote(Context context) {
        apiInterface = APIClient.getRetrofit(context).create(APIInterface.class);
    }

    @Override
    public void getRegisterResult(String user_name, String user_email, String user_password, @NonNull @NotNull RegisterGetCallback registerGetCallback) {

        final Call<BaseResponse> baseResponseCall = apiInterface.register(user_name, user_email, user_password);
        baseResponseCall.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                try {
                    if (response.body().getMsg().equals("Berhasil")) {
                        registerGetCallback.onSuccessRegister();
                    } else {
                        registerGetCallback.onErrorRegister(response.body().getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                registerGetCallback.onErrorRegister(Server.CHECK_INTERNET_CONNECTION);
            }
        });
    }
}

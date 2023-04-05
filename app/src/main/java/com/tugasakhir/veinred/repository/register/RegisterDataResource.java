package com.tugasakhir.veinred.repository.register;

import androidx.annotation.NonNull;

public interface RegisterDataResource {

    void getRegisterResult(String user_name, String user_email, String user_password, @NonNull RegisterGetCallback registerGetCallback);

    interface RegisterGetCallback {

        void onSuccessRegister();

        void onErrorRegister(String msg);
    }
}

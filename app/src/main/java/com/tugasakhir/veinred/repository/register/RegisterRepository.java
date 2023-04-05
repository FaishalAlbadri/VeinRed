package com.tugasakhir.veinred.repository.register;

import androidx.annotation.NonNull;

public class RegisterRepository implements RegisterDataResource {

    private RegisterDataResource registerDataResource;

    public RegisterRepository(RegisterDataResource registerDataResource) {
        this.registerDataResource = registerDataResource;
    }

    @Override
    public void getRegisterResult(String user_name, String user_email, String user_password,@NonNull RegisterGetCallback registerGetCallback) {
        registerDataResource.getRegisterResult(user_name, user_email, user_password, registerGetCallback);
    }
}

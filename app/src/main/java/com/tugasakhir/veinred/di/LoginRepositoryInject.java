package com.tugasakhir.veinred.di;

import android.content.Context;

import com.tugasakhir.veinred.repository.login.LoginDataRemote;
import com.tugasakhir.veinred.repository.login.LoginRepository;

public class LoginRepositoryInject {

    public static LoginRepository provideToRepository(Context context) {
        return new LoginRepository(new LoginDataRemote(context));
    }

}

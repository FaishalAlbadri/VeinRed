package com.tugasakhir.veinred.di;

import android.content.Context;

import com.tugasakhir.veinred.repository.register.RegisterDataRemote;
import com.tugasakhir.veinred.repository.register.RegisterRepository;


public class RegisterRepositoryInject {
    public static RegisterRepository provideToRepository(Context context) {
        return new RegisterRepository(new RegisterDataRemote(context));
    }
}

package com.tugasakhir.veinred.presenter.register;

import com.tugasakhir.veinred.repository.register.RegisterDataResource;
import com.tugasakhir.veinred.repository.register.RegisterRepository;

public class RegisterPresenter implements RegisterContract.registerPresenter {

    private RegisterRepository registerRepository;
    private RegisterContract.registerView registerView;

    public RegisterPresenter(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    @Override
    public void getDataRegister(String user_name, String user_email, String user_password) {
        registerRepository.getRegisterResult(user_name, user_email, user_password, new RegisterDataResource.RegisterGetCallback() {
            @Override
            public void onSuccessRegister() {
                registerView.onSuccessRegister();
            }

            @Override
            public void onErrorRegister(String msg) {
                registerView.onErrorRegister(msg);
            }
        });
    }

    @Override
    public void onAttachView(RegisterContract.registerView view) {
        registerView = view;
    }

    @Override
    public void onDettachView() {

    }
}

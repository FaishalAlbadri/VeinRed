package com.tugasakhir.veinred.base;

public interface BasePresenter<T> {

    void onAttachView(T view);

    void onDettachView();

}

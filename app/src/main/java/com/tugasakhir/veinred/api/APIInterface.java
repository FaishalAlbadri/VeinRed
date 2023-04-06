package com.tugasakhir.veinred.api;

import com.tugasakhir.veinred.data.BaseResponse;
import com.tugasakhir.veinred.data.news.NewsResponse;
import com.tugasakhir.veinred.data.user.UserResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIInterface {

    @FormUrlEncoded
    @POST("user/register")
    Call<BaseResponse> register(
            @Field("user_name") String name,
            @Field("user_email") String email,
            @Field("user_password") String pass
    );

    @FormUrlEncoded
    @POST("user/login")
    Call<UserResponse> login(
            @Field("user_email") String email,
            @Field("user_password") String pass
    );

    @FormUrlEncoded
    @POST("news/get")
    Call<NewsResponse> news(
            @Field("status") String status,
            @Field("id_user") String id_user
    );
}

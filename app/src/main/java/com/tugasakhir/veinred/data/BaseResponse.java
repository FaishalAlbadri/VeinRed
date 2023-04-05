package com.tugasakhir.veinred.data;

import com.google.gson.annotations.SerializedName;

public class BaseResponse {

    @SerializedName("msg")
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "msg='" + msg + '\'' +
                '}';
    }
}

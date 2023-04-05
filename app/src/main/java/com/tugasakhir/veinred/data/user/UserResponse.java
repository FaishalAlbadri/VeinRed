package com.tugasakhir.veinred.data.user;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class UserResponse{

	@SerializedName("msg")
	private String msg;

	@SerializedName("user")
	private List<UserItem> user;

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setUser(List<UserItem> user){
		this.user = user;
	}

	public List<UserItem> getUser(){
		return user;
	}

	@Override
 	public String toString(){
		return 
			"UserResponse{" + 
			"msg = '" + msg + '\'' + 
			",user = '" + user + '\'' + 
			"}";
		}
}
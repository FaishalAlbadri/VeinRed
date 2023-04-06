package com.tugasakhir.veinred.data.news;

import com.google.gson.annotations.SerializedName;

public class UserItem{

	@SerializedName("user_name")
	private String userName;

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getUserName(){
		return userName;
	}

	@Override
 	public String toString(){
		return 
			"UserItem{" + 
			"user_name = '" + userName + '\'' + 
			"}";
		}
}
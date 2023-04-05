package com.tugasakhir.veinred.data.user;

import com.google.gson.annotations.SerializedName;

public class UserItem{

	@SerializedName("user_email")
	private String userEmail;

	@SerializedName("user_password")
	private String userPassword;

	@SerializedName("user_image")
	private String userImage;

	@SerializedName("user_name")
	private String userName;

	@SerializedName("id_user")
	private String idUser;

	@SerializedName("user_create")
	private String userCreate;

	public void setUserEmail(String userEmail){
		this.userEmail = userEmail;
	}

	public String getUserEmail(){
		return userEmail;
	}

	public void setUserPassword(String userPassword){
		this.userPassword = userPassword;
	}

	public String getUserPassword(){
		return userPassword;
	}

	public void setUserImage(String userImage){
		this.userImage = userImage;
	}

	public String getUserImage(){
		return userImage;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getUserName(){
		return userName;
	}

	public void setIdUser(String idUser){
		this.idUser = idUser;
	}

	public String getIdUser(){
		return idUser;
	}

	public void setUserCreate(String userCreate){
		this.userCreate = userCreate;
	}

	public String getUserCreate(){
		return userCreate;
	}

	@Override
 	public String toString(){
		return 
			"UserItem{" + 
			"user_email = '" + userEmail + '\'' + 
			",user_password = '" + userPassword + '\'' + 
			",user_image = '" + userImage + '\'' + 
			",user_name = '" + userName + '\'' + 
			",id_user = '" + idUser + '\'' + 
			",user_create = '" + userCreate + '\'' + 
			"}";
		}
}
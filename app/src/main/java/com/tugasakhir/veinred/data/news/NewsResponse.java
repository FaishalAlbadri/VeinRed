package com.tugasakhir.veinred.data.news;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class NewsResponse{

	@SerializedName("news")
	private List<NewsItem> news;

	@SerializedName("msg")
	private String msg;

	@SerializedName("user")
	private List<UserItem> user;

	public void setNews(List<NewsItem> news){
		this.news = news;
	}

	public List<NewsItem> getNews(){
		return news;
	}

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
			"NewsResponse{" + 
			"news = '" + news + '\'' + 
			",msg = '" + msg + '\'' + 
			",user = '" + user + '\'' + 
			"}";
		}
}
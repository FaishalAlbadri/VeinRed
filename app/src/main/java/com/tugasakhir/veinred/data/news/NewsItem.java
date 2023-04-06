package com.tugasakhir.veinred.data.news;

import com.google.gson.annotations.SerializedName;

public class NewsItem{

	@SerializedName("news_create")
	private String newsCreate;

	@SerializedName("id_news")
	private String idNews;

	@SerializedName("news_desc")
	private String newsDesc;

	@SerializedName("news_judul")
	private String newsJudul;

	@SerializedName("news_img")
	private String newsImg;

	public void setNewsCreate(String newsCreate){
		this.newsCreate = newsCreate;
	}

	public String getNewsCreate(){
		return newsCreate;
	}

	public void setIdNews(String idNews){
		this.idNews = idNews;
	}

	public String getIdNews(){
		return idNews;
	}

	public void setNewsDesc(String newsDesc){
		this.newsDesc = newsDesc;
	}

	public String getNewsDesc(){
		return newsDesc;
	}

	public void setNewsJudul(String newsJudul){
		this.newsJudul = newsJudul;
	}

	public String getNewsJudul(){
		return newsJudul;
	}

	public void setNewsImg(String newsImg){
		this.newsImg = newsImg;
	}

	public String getNewsImg(){
		return newsImg;
	}

	@Override
 	public String toString(){
		return 
			"NewsItem{" + 
			"news_create = '" + newsCreate + '\'' + 
			",id_news = '" + idNews + '\'' + 
			",news_desc = '" + newsDesc + '\'' + 
			",news_judul = '" + newsJudul + '\'' + 
			",news_img = '" + newsImg + '\'' + 
			"}";
		}
}
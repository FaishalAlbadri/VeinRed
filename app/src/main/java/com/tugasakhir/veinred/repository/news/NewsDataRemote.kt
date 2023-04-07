package com.tugasakhir.veinred.repository.news;

import android.content.Context
import com.tugasakhir.veinred.api.APIClient
import com.tugasakhir.veinred.api.APIInterface
import com.tugasakhir.veinred.api.Server
import com.tugasakhir.veinred.data.news.NewsItem
import com.tugasakhir.veinred.data.news.NewsResponse
import com.tugasakhir.veinred.util.session.SessionUser
import com.tugasakhir.veinred.util.session.User

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsDataRemote : NewsDataResource {

    private var apiInterface: APIInterface
    private lateinit var newsResponseCall: Call<NewsResponse>

    constructor(context: Context) {
        apiInterface = APIClient.getRetrofit(context)!!.create(APIInterface::class.java)
        SessionUser.getInstance(context).setDataUser()
    }

    override fun news(status: String, newsCallback: NewsDataResource.NewsCallback) {
        newsResponseCall = apiInterface.news(status, User.getInstance().id_user)
        newsResponseCall.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                try {
                    if (response.body()!!.msg.equals("Berhasil")) {
                        val newsResponse: NewsResponse = response.body()!!
                        val newsItem: List<NewsItem> = newsResponse.news
                        newsCallback.onSuccessNews(newsItem, response.body()!!.user.get(0).userName, response.body()!!.msg)

                    } else {
                        newsCallback.onErrorNews(response.body()!!.user.get(0).userName, response.body()!!.msg)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                newsCallback.onErrorNews("Tidak Ada Koneksi Internet", Server.CHECK_INTERNET_CONNECTION)
            }

        })
    }
}
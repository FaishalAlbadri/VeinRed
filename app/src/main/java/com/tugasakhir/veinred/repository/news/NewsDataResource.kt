package com.tugasakhir.veinred.repository.news;

import androidx.annotation.NonNull
import com.tugasakhir.veinred.data.news.NewsItem

interface NewsDataResource {

    fun news(status: String, @NonNull newsCallback: NewsCallback)

    interface NewsCallback {
        fun onSuccessNews(newsListItem: List<NewsItem>, username: String, msg: String)
        fun onErrorNews(username: String, msg: String)
    }
}
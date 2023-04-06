package com.tugasakhir.veinred.presenter.news;

import com.tugasakhir.veinred.base.BasePresenter
import com.tugasakhir.veinred.data.news.NewsItem

class NewsContract {

    interface newsView {
        fun onSuccessNews(newsListItem: List<NewsItem>, username: String, msg: String)
        fun onErrorNews(msg: String)
    }

    interface newsPresenter: BasePresenter<newsView> {
        fun news(status: String)
    }
}
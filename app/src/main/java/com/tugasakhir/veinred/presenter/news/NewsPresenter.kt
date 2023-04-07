package com.tugasakhir.veinred.presenter.news;

import com.tugasakhir.veinred.data.news.NewsItem
import com.tugasakhir.veinred.repository.news.NewsDataResource
import com.tugasakhir.veinred.repository.news.NewsRepository

class NewsPresenter : NewsContract.newsPresenter {

    private var newsRepository: NewsRepository
    private lateinit var newsView: NewsContract.newsView

    constructor(newsRepository: NewsRepository) {
        this.newsRepository = newsRepository
    }

    override fun news(status: String) {
        newsRepository.news(status, object: NewsDataResource.NewsCallback {
            override fun onSuccessNews(newsListItem: List<NewsItem>, username: String, msg: String) {
                newsView.onSuccessNews(newsListItem, username, msg)
            }

            override fun onErrorNews(username: String, msg: String) {
                newsView.onErrorNews(username, msg)
            }

        })
    }

    override fun onAttachView(view: NewsContract.newsView) {
        newsView = view
    }

    override fun onDettachView() {
    }
}
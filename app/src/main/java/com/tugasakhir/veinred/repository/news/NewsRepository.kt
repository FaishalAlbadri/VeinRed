package com.tugasakhir.veinred.repository.news;

class NewsRepository: NewsDataResource {

    private var newsDataResource: NewsDataResource

    constructor(newsDataResource: NewsDataResource) {
        this.newsDataResource = newsDataResource
    }

    override fun news(status: String, newsCallback: NewsDataResource.NewsCallback) {
        newsDataResource.news(status, newsCallback)
    }
}
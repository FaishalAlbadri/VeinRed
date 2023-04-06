package com.tugasakhir.veinred.di;

import android.content.Context;
import com.tugasakhir.veinred.repository.news.NewsDataRemote;
import com.tugasakhir.veinred.repository.news.NewsRepository;

public class NewsRepositoryInject {

    public static NewsRepository provideToRepository(Context context) {
        return new NewsRepository(new NewsDataRemote(context));
    }
}

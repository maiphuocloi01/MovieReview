package com.cnjava.moviereview.repository;

import com.cnjava.moviereview.api.TranslateApi;
import com.cnjava.moviereview.model.Translate;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class Repository {

    private final TranslateApi apiService;

    @Inject
    public Repository(TranslateApi apiService) {
        this.apiService = apiService;
    }

    public Single<Translate> translateText(String text) {
        return apiService.translateText(text);
    }
}

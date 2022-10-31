package com.cnjava.moviereview.api;

import static com.cnjava.moviereview.util.Constants.API_KEY_TRANSLATE;

import com.cnjava.moviereview.model.Translate;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TranslateApi {

    @POST("translate?key=" + API_KEY_TRANSLATE + "&lang=en-vi")
    @FormUrlEncoded
    Single<Translate> translateText(@Field("text") String text);
}

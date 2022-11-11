package com.cnjava.moviereview.di;

import static com.cnjava.moviereview.util.Constants.BASE_URL_GUEST;
import static com.cnjava.moviereview.util.Constants.BASE_URL_TRANSLATE;
import static com.cnjava.moviereview.util.Constants.BASE_URL_USER;

import com.cnjava.moviereview.data.AccountService;
import com.cnjava.moviereview.data.MovieService;
import com.cnjava.moviereview.data.TranslateService;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {

    @Singleton
    @Provides
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        return new HttpLoggingInterceptor();
    }

    @Singleton
    @Provides
    OkHttpClient.Builder provideOkHttpClient1(HttpLoggingInterceptor interceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
    }

    @Singleton
    @Provides
    @Named("TRANSLATE")
    Retrofit provideRetrofit1(OkHttpClient.Builder client) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL_TRANSLATE)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    @Named("MOVIE")
    Retrofit provideRetrofit2(OkHttpClient.Builder client) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL_GUEST)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    @Named("ACCOUNT")
    Retrofit provideRetrofit3(OkHttpClient.Builder client) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL_USER)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    TranslateService provideTranslateApi(@Named("TRANSLATE") Retrofit retrofit) {
        return retrofit.create(TranslateService.class);
    }

    @Provides
    @Singleton
    MovieService provideMovieService(@Named("MOVIE") Retrofit retrofit) {
        return retrofit.create(MovieService.class);
    }

    @Provides
    @Singleton
    AccountService provideAccountService(@Named("ACCOUNT") Retrofit retrofit) {
        return retrofit.create(AccountService.class);
    }

}

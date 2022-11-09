package com.cnjava.moviereview.view.fragment.result;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cnjava.moviereview.model.Actor;
import com.cnjava.moviereview.model.Collection;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.model.Video;
import com.cnjava.moviereview.repository.AccountRepository;
import com.cnjava.moviereview.repository.MovieRepository;
import com.cnjava.moviereview.view.fragment.home.HomeViewModel;
import com.cnjava.moviereview.viewmodel.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;

@HiltViewModel
public class ResultViewModel extends BaseViewModel {
    private static final String TAG = HomeViewModel.class.getSimpleName();

    private final MovieRepository movieRepository;
    private final AccountRepository accountRepository;

    @Inject
    public ResultViewModel(MovieRepository movieRepository, AccountRepository accountRepository) {
        this.movieRepository = movieRepository;
        this.accountRepository = accountRepository;
    }

    private final MutableLiveData<Movie> popularMovieLD = new MutableLiveData<>();
    private final MutableLiveData<Movie> nowPlayingMovieLD = new MutableLiveData<>();
    private final MutableLiveData<Movie> upcomingMovieLD = new MutableLiveData<>();
    private final MutableLiveData<Movie> topRatedMovieLD = new MutableLiveData<>();
    private final MutableLiveData<Movie> recommendationLD = new MutableLiveData<>();
    private final MutableLiveData<Actor> actorLD = new MutableLiveData<>();
    private final MutableLiveData<Video> videoLD = new MutableLiveData<>();
    private final MutableLiveData<Collection> collectionLD = new MutableLiveData<>();

    public LiveData<Movie> popularMovieLD() {
        return popularMovieLD;
    }

    public LiveData<Movie> nowPlayingMovieLD() {
        return nowPlayingMovieLD;
    }

    public LiveData<Movie> upcomingMovieLD() {
        return upcomingMovieLD;
    }

    public LiveData<Movie> topRatedMovieLD() {
        return topRatedMovieLD;
    }

    public LiveData<Actor> actorLD() {
        return actorLD;
    }

    public LiveData<Movie> recommendationLD() {
        return recommendationLD;
    }

    public LiveData<Video> videoLD() {
        return videoLD;
    }

    public LiveData<Collection> collectionLD() {
        return collectionLD;
    }


    public void getPopularMovie() {
        mLiveDataIsLoading.setValue(true);
        movieRepository.getPopularMovie().subscribe(new CustomSingleObserver<Movie>() {
            @Override
            public void onSuccess(@NonNull Movie movie) {
                popularMovieLD.setValue(movie);
                mLiveDataIsLoading.setValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.setValue(false);
            }
        });
    }

    public void getNowPlayingMovie() {
        movieRepository.getNowPlayingMovie().subscribe(new CustomSingleObserver<Movie>() {
            @Override
            public void onSuccess(@NonNull Movie movie) {
                nowPlayingMovieLD.setValue(movie);
                mLiveDataIsLoading.setValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.setValue(false);
            }
        });
    }

    public void getUpComingMovie() {
        movieRepository.getUpComingMovie().subscribe(new CustomSingleObserver<Movie>() {
            @Override
            public void onSuccess(@NonNull Movie movie) {
                upcomingMovieLD.setValue(movie);
                mLiveDataIsLoading.setValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.setValue(false);
            }
        });
    }

    public void getTopRatedMovie() {
        movieRepository.getTopRatedMovie().subscribe(new CustomSingleObserver<Movie>() {
            @Override
            public void onSuccess(@NonNull Movie movie) {
                topRatedMovieLD.setValue(movie);
                mLiveDataIsLoading.setValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.setValue(false);
            }
        });
    }

    public void getCast(int id) {
        movieRepository.getCast(id).subscribe(new CustomSingleObserver<Actor>() {
            @Override
            public void onSuccess(@NonNull Actor actor) {
                actorLD.setValue(actor);
                mLiveDataIsLoading.setValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.setValue(false);
            }
        });
    }

    public void getRecommendation(int id) {
        movieRepository.getRecommendation(id).subscribe(new CustomSingleObserver<Movie>() {
            @Override
            public void onSuccess(@NonNull Movie movie) {
                recommendationLD.setValue(movie);
                mLiveDataIsLoading.setValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.setValue(false);
            }
        });
    }

    public void getVideo(int id) {
        movieRepository.getVideo(id).subscribe(new CustomSingleObserver<Video>() {
            @Override
            public void onSuccess(@NonNull Video video) {
                videoLD.setValue(video);
                mLiveDataIsLoading.setValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.setValue(false);
            }
        });
    }

    public void getCollection(int collectionId) {
        movieRepository.getCollection(collectionId).subscribe(new CustomSingleObserver<Collection>() {
            @Override
            public void onSuccess(@NonNull Collection collection) {
                collectionLD.setValue(collection);
                mLiveDataIsLoading.setValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.setValue(false);
            }
        });
    }
}

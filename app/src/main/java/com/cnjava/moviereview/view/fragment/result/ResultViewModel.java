package com.cnjava.moviereview.view.fragment.result;


import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;

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
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import kotlinx.coroutines.CoroutineScope;

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

    private final MutableLiveData<PagingData<Movie.Result>> nowPlayingMovieLD = new MutableLiveData<>();
    private final MutableLiveData<PagingData<Movie.Result>> upcomingMovieLD = new MutableLiveData<>();

    public LiveData<PagingData<Movie.Result>> upcomingMovieLD() {
        return upcomingMovieLD;
    }

    private final MutableLiveData<PagingData<Movie.Result>> movieByCategoryLD = new MutableLiveData<>();

    public LiveData<PagingData<Movie.Result>> movieByCategoryLD() {
        return movieByCategoryLD;
    }

    private final MutableLiveData<PagingData<Movie.Result>> topRatedMovieLD = new MutableLiveData<>();
    private final MutableLiveData<Movie> recommendationLD = new MutableLiveData<>();
    private final MutableLiveData<Actor> actorLD = new MutableLiveData<>();
    private final MutableLiveData<Video> videoLD = new MutableLiveData<>();
    private final MutableLiveData<Collection> collectionLD = new MutableLiveData<>();


    public LiveData<PagingData<Movie.Result>> nowPlayingMovieLD() {
        return nowPlayingMovieLD;
    }


    public LiveData<PagingData<Movie.Result>> topRatedMovieLD() {
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


    /*public void getUpComingMovie() {
        mLiveDataIsLoading.setValue(true);
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
    }*/

    @SuppressLint("UnsafeOptInUsageWarning")
    public void getUpComingMovie() {
        mLiveDataIsLoading.postValue(true);
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        mMainCompDisposable.add(
                PagingRx.cachedIn(movieRepository.getUpComingMoviePaging(), viewModelScope)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(resultPagingData -> {
                            upcomingMovieLD.postValue(resultPagingData);
                            mLiveDataIsLoading.postValue(false);
                        })
        );
    }

    @SuppressLint("UnsafeOptInUsageWarning")
    public void getNowPlayingMoviePaging() {
        mLiveDataIsLoading.postValue(true);
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        mMainCompDisposable.add(
                PagingRx.cachedIn(movieRepository.getNowPlayingMoviePaging(), viewModelScope)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(resultPagingData -> {
                            nowPlayingMovieLD.postValue(resultPagingData);
                            mLiveDataIsLoading.postValue(false);
                        })
        );
    }

    @SuppressLint("UnsafeOptInUsageWarning")
    public void getTopRatedMovie() {
        mLiveDataIsLoading.postValue(true);
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        mMainCompDisposable.add(
                PagingRx.cachedIn(movieRepository.getTopRatedMoviePaging(), viewModelScope)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(resultPagingData -> {
                            topRatedMovieLD.postValue(resultPagingData);
                            mLiveDataIsLoading.postValue(false);
                        })
        );
    }

    @SuppressLint("UnsafeOptInUsageWarning")
    public void getMovieByCategory(String query) {
        mLiveDataIsLoading.postValue(true);
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        mMainCompDisposable.add(
                PagingRx.cachedIn(movieRepository.getMovieByCategory(query), viewModelScope)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(resultPagingData -> {
                            movieByCategoryLD.postValue(resultPagingData);
                            mLiveDataIsLoading.postValue(false);
                        })
        );
    }

    /*public void getTopRatedMovie() {
        mLiveDataIsLoading.setValue(true);
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
    }*/

    public void getCast(int id) {
        mLiveDataIsLoading.setValue(true);
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
        mLiveDataIsLoading.setValue(true);
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


    public void getCollection(int collectionId) {
        mLiveDataIsLoading.setValue(true);
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

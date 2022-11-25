package com.cnjava.moviereview.view.fragment.search;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;

import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.model.MovieName;
import com.cnjava.moviereview.repository.MovieRepository;
import com.cnjava.moviereview.util.cutom.SingleLiveEvent;
import com.cnjava.moviereview.viewmodel.BaseViewModel;

import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import kotlinx.coroutines.CoroutineScope;

@HiltViewModel
public class SearchViewModel extends BaseViewModel {

    private static final String TAG = SearchViewModel.class.getName();

    private final MovieRepository movieRepository;
    private SavedStateHandle state;

    private final MutableLiveData<Movie> trendingWeekLD = new MutableLiveData<>();
    private final MutableLiveData<Movie> trendingDayLD = new MutableLiveData<>();
    private final MutableLiveData<MovieName> movieNameLD = new MutableLiveData<>();
    private final SingleLiveEvent<Movie> movieResultLD = new SingleLiveEvent<>();
    private final MutableLiveData<PagingData<Movie.Result>> movieResultLD2 = new MutableLiveData<>();
    private String textSearch = "";

    public String getTextSearch() {
        return textSearch;
    }

    public void setTextSearch(String textSearch) {
        this.textSearch = textSearch;
    }

    public LiveData<Movie> trendingWeekLD() {
        return trendingWeekLD;
    }

    public LiveData<Movie> trendingDayLD() {
        return trendingDayLD;
    }

    public LiveData<MovieName> movieNameLD() {
        return movieNameLD;
    }

    /*public SingleLiveEvent<Movie> movieResultLD() {
        return movieResultLD;
    }*/

    public LiveData<PagingData<Movie.Result>> movieResultLD2() {
        return movieResultLD2;
    }

    @Inject
    public SearchViewModel(MovieRepository movieRepository, SavedStateHandle savedStateHandle) {
        this.movieRepository = movieRepository;
        this.state = savedStateHandle;
    }

    public void getTrending(String timeWindow) {
        movieRepository.getTrending(timeWindow).subscribe(new CustomSingleObserver<Movie>() {
            @Override
            public void onSuccess(@NonNull Movie movie) {
                if (timeWindow.equals("day")) {
                    trendingDayLD.postValue(movie);
                } else if (timeWindow.equals("week")) {
                    trendingWeekLD.postValue(movie);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.postValue(false);
            }
        });
    }

    public void autoCompleteSearch(String searchView) {
        movieRepository.autoCompleteSearch(searchView)
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribe(new Observer<MovieName>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mMainCompDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull MovieName movieName) {
                        movieNameLD.postValue(movieName);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                        mLiveDataIsLoading.postValue(false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void searchMovie(String text) {
        movieRepository.searchMovie(text).subscribe(new CustomSingleObserver<Movie>() {
            @Override
            public void onSuccess(@NonNull Movie movie) {
                movieResultLD.postValue(movie);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.postValue(false);
            }
        });

    }

    @SuppressLint("UnsafeOptInUsageWarning")
    public Flowable<PagingData<Movie.Result>> searchMoviePaging2() {
        //mLiveDataIsLoading.postValue(true);
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        return PagingRx.cachedIn(movieRepository.searchMoviePaging(textSearch), viewModelScope);
    }

    @SuppressLint("UnsafeOptInUsageWarning")
    public void searchMoviePaging(String text) {
        mLiveDataIsLoading.postValue(true);
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        mMainCompDisposable.add(
                PagingRx.cachedIn(movieRepository.searchMoviePaging(text), viewModelScope)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(resultPagingData -> {
                            movieResultLD2.postValue(resultPagingData);
                            mLiveDataIsLoading.postValue(false);
                        })
        );
    }

}

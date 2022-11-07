package com.cnjava.moviereview.view.fragment.search;

import static com.cnjava.moviereview.util.StringConvert.removeDiacriticalMarks;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.repository.MovieRepository;
import com.cnjava.moviereview.viewmodel.BaseViewModel;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class SearchViewModel extends BaseViewModel {

    private static final String TAG = SearchViewModel.class.getSimpleName();

    private final MovieRepository movieRepository;

    private final MutableLiveData<Movie> trendingWeekLD = new MutableLiveData<>();
    private final MutableLiveData<Movie> trendingDayLD = new MutableLiveData<>();
    private final MutableLiveData<List<String>> movieNamesLD = new MutableLiveData<>();

    public LiveData<Movie> trendingWeekLD() {
        return trendingWeekLD;
    }

    public LiveData<Movie> trendingDayLD() {
        return trendingDayLD;
    }

    public LiveData<List<String>> movieNamesLD() {
        return movieNamesLD;
    }

    @Inject
    public SearchViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void getTrending(String timeWindow) {
        mLiveDataIsLoading.setValue(true);
        movieRepository.getTrending(timeWindow).subscribe(new CustomObserver<Movie>() {
            @Override
            public void onSuccess(@NonNull Movie movie) {
                if (timeWindow.equals("day")) {
                    trendingDayLD.setValue(movie);
                } else if (timeWindow.equals("week")) {
                    trendingWeekLD.setValue(movie);
                }
                mLiveDataIsLoading.setValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.setValue(false);
            }
        });
    }

    public void autoCompleteSearchFromJson(List<String> movieNames, String nameSearch) {
        Single<List<String>> result = Observable.fromStream(movieNames
                .stream()
                .filter(name -> removeDiacriticalMarks(name).toLowerCase().contains(removeDiacriticalMarks(nameSearch).toLowerCase()))
                .limit(4)
        ).collect(Collectors.toList());
        result.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<List<String>>() {
                    @Override
                    public void onSuccess(@NonNull List<String> strings) {
                        movieNamesLD.setValue(strings);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }
}

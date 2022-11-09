package com.cnjava.moviereview.view.fragment.detailmovie;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cnjava.moviereview.model.Actor;
import com.cnjava.moviereview.model.Collection;
import com.cnjava.moviereview.model.Favorite;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.model.MovieDetail;
import com.cnjava.moviereview.model.Review;
import com.cnjava.moviereview.model.Social;
import com.cnjava.moviereview.model.User;
import com.cnjava.moviereview.model.Video;
import com.cnjava.moviereview.repository.AccountRepository;
import com.cnjava.moviereview.repository.MovieRepository;
import com.cnjava.moviereview.viewmodel.BaseViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;


@HiltViewModel
public class DetailViewModel extends BaseViewModel {
    private static final String TAG = "DetailViewModel";

    private final MovieRepository movieRepository;
    private final AccountRepository accountRepository;

    @Inject
    public DetailViewModel(MovieRepository movieRepository, AccountRepository accountRepository) {
        this.movieRepository = movieRepository;
        this.accountRepository = accountRepository;
    }

    private final MutableLiveData<MovieDetail> movieDetailLD = new MutableLiveData<>();
    private final MutableLiveData<Actor> actorLD = new MutableLiveData<>();
    private final MutableLiveData<Movie> recommendationLD = new MutableLiveData<>();
    private final MutableLiveData<Video> videoLD = new MutableLiveData<>();
    private final MutableLiveData<Social> socialLD = new MutableLiveData<>();
    private final MutableLiveData<Collection> collectionLD = new MutableLiveData<>();
    private final MutableLiveData<List<Favorite>> myFavoriteLD = new MutableLiveData<>();
    private final MutableLiveData<List<Review>> movieReviewLD = new MutableLiveData<>();
    private final MutableLiveData<User> yourProfileLD = new MutableLiveData<>();

    public LiveData<MovieDetail> movieDetailLD() {
        return movieDetailLD;
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

    public LiveData<Social> socialLD() {
        return socialLD;
    }

    public LiveData<Collection> collectionLD() {
        return collectionLD;
    }

    public LiveData<List<Favorite>> myFavoriteLD() {
        return myFavoriteLD;
    }

    public LiveData<List<Review>> movieReviewLD() {
        return movieReviewLD;
    }

    public LiveData<User> yourProfileLD() {
        return yourProfileLD;
    }

    public void getMovieDetail(int id) {
        mLiveDataIsLoading.setValue(true);
        movieRepository.getMovieDetail(id).subscribe(new CustomSingleObserver<MovieDetail>() {
            @Override
            public void onSuccess(@NonNull MovieDetail movie) {
                movieDetailLD.setValue(movie);
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

    public void getSocial(int id) {
        movieRepository.getSocial(id).subscribe(new CustomSingleObserver<Social>() {
            @Override
            public void onSuccess(@NonNull Social social) {
                socialLD.setValue(social);
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

    public void getMyFavorite(String token) {
        accountRepository.getMyFavorite(token).subscribe(new CustomSingleObserver<List<Favorite>>() {
            @Override
            public void onSuccess(@NonNull List<Favorite> favorites) {
                myFavoriteLD.setValue(favorites);
                mLiveDataIsLoading.setValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.setValue(false);
            }
        });
    }


    private final MutableLiveData<Favorite> addFavoriteLD = new MutableLiveData<>();

    public LiveData<Favorite> addFavoriteLD() {
        return addFavoriteLD;
    }

    public void getReviewByMovieId(String movieId) {
        accountRepository.getReviewByMovieId(movieId).subscribe(new CustomSingleObserver<List<Review>>() {
            @Override
            public void onSuccess(@NonNull List<Review> reviews) {
                movieReviewLD.setValue(reviews);
                mLiveDataIsLoading.setValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.setValue(false);
            }
        });
    }

    public void addFavorite(Favorite.MovieFavorite movie, String token) {
        accountRepository.addFavorite(movie, token).subscribe(new CustomSingleObserver<Favorite>() {
            @Override
            public void onSuccess(@NonNull Favorite favorite) {
                addFavoriteLD.setValue(favorite);
                mLiveDataIsLoading.setValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.setValue(false);
            }
        });
    }

    public void deleteFavorite(String movieId, String token) {
        accountRepository.deleteFavorite(movieId, token).subscribe(new CustomCompletableObserver() {
            @Override
            public void onComplete() {
                mLiveDataIsLoading.setValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.setValue(false);
            }
        });
    }

    public void deleteReview(String reviewId, String token) {
        accountRepository.deleteReview(reviewId, token).subscribe(new CustomCompletableObserver() {
            @Override
            public void onComplete() {
                mLiveDataIsLoading.setValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.setValue(false);
            }
        });
    }

    public void dislikeReview(String reviewId, String token) {
        accountRepository.dislikeReview(reviewId, token).subscribe(new CustomCompletableObserver() {
            @Override
            public void onComplete() {
                mLiveDataIsLoading.setValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.setValue(false);
            }
        });
    }

    public void likeReview(String reviewId, String token) {
        accountRepository.likeReview(reviewId, token).subscribe(new CustomCompletableObserver() {
            @Override
            public void onComplete() {
                mLiveDataIsLoading.setValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.setValue(false);
            }
        });
    }

    public void getYourProfile(String token) {
        mLiveDataIsLoading.setValue(true);
        accountRepository.getMyProfile(token).subscribe(new CustomSingleObserver<User>() {
            @Override
            public void onSuccess(@NonNull User user) {
                yourProfileLD.setValue(user);
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


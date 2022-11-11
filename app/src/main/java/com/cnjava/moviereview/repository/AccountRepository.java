package com.cnjava.moviereview.repository;

import com.cnjava.moviereview.data.AccountService;
import com.cnjava.moviereview.model.Favorite;
import com.cnjava.moviereview.model.Review;
import com.cnjava.moviereview.model.Statistic;
import com.cnjava.moviereview.model.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class AccountRepository {
    private final AccountService accountService;

    @Inject
    public AccountRepository(AccountService accountService) {
        this.accountService = accountService;
    }

    public Single<User> getMyProfile(String token) {
        return subscribe(accountService.getMyProfile("Bearer " + token));
    }

    public Single<ResponseBody> uploadImage(String token, MultipartBody.Part part, RequestBody requestBody) {
        return subscribe(accountService.uploadImage("Bearer " + token, part, requestBody));
    }

    public Single<Review> addReview(Review review, String token) {
        return subscribe(accountService.addReview(review, "Bearer " + token));
    }

    public Single<Statistic> getMyStatistics(String token) {
        return subscribe(accountService.getMyStatistics("Bearer " + token));
    }

    public Single<List<Review>> getReviewByUserId(String userId) {
        return subscribe(accountService.getReviewByUserId(userId));
    }

    public Single<List<Favorite>> getMyFavorite(String token) {
        return subscribe(accountService.getMyFavorite("Bearer " + token));
    }

    public Single<Review> getReviewsByUserIdAndMovieId(String userId, String movieId) {
        return subscribe(accountService.getReviewsByUserIdAndMovieIdLD(userId, movieId));
    }

    public Single<Favorite> addFavorite(Favorite.MovieFavorite movie, String token) {
        return subscribe(accountService.addFavorite(movie, "Bearer " + token));
    }

    public Completable deleteFavorite(String movieId, String token) {
        return subscribeCompletable(accountService.deleteFavorite(movieId, "Bearer " + token));
    }

    public Completable deleteReview(String reviewId, String token) {
        return subscribeCompletable(accountService.deleteFavorite(reviewId, "Bearer " + token));
    }

    public Completable dislikeReview(String reviewId, String token) {
        return subscribeCompletable(accountService.dislikeReview(reviewId, "Bearer " + token));
    }

    public Completable likeReview(String reviewId, String token) {
        return subscribeCompletable(accountService.likeReview(reviewId, "Bearer " + token));
    }

    public Single<List<Review>> getReviewByMovieId(String movieId) {
        return subscribe(accountService.getReviewByMovieId(movieId));
    }

    public Single<Void> updateProfile(User user, String token) {
        return subscribe(accountService.updateProfile(user, "Bearer " + token));
    }

    /*public Single<ResponseBody> uploadImageAccount(MultipartBody.Part parts, RequestBody someData) {
        return subscribe(accountService.uploadImage(parts, someData));
    }*/

    private <T> Single<T> subscribe(Single<T> single) {
        return single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private <T> Completable subscribeCompletable(Completable completable) {
        return completable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}

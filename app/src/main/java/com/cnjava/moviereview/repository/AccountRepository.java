package com.cnjava.moviereview.repository;

import com.cnjava.moviereview.data.AccountService;
import com.cnjava.moviereview.model.Favorite;
import com.cnjava.moviereview.model.Review;
import com.cnjava.moviereview.model.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AccountRepository {
    private final AccountService accountService;

    @Inject
    public AccountRepository(AccountService accountService) {
        this.accountService = accountService;
    }

    public Single<User> getMyProfile(String token){
        return subscribe(accountService.getMyProfile("Bearer " + token));
    }

    public Single<List<Favorite>> getMyFavorite(String token){
        return subscribe(accountService.getMyFavorite("Bearer " + token));
    }

    public Single<List<Review>> getReviewByMovieId(String movieId){
        return subscribe(accountService.getReviewByMovieId(movieId));
    }

    private <T> Single<T> subscribe(Single<T> single) {
        return single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}

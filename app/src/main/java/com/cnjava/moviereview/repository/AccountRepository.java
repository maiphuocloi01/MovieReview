package com.cnjava.moviereview.repository;

import com.cnjava.moviereview.data.AccountService;
import com.cnjava.moviereview.model.User;

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

    public Single<User> getYourProfile(String token){
        return subscribe(accountService.getYourProfile("Bearer " + token));
    }

    private <T> Single<T> subscribe(Single<T> single) {
        return single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}

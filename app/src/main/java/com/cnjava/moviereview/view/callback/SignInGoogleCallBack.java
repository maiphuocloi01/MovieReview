package com.cnjava.moviereview.view.callback;

import com.google.firebase.auth.FirebaseUser;

public interface SignInGoogleCallBack {
    void loginSuccess(FirebaseUser data);
    void loginError(String message);
}

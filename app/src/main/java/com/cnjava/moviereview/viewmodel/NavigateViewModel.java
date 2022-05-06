package com.cnjava.moviereview.viewmodel;

import androidx.fragment.app.Fragment;

public class NavigateViewModel extends BaseViewModel {

    private Fragment fragment;

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public Fragment getFragment() {
        return fragment;
    }
}

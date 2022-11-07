package com.cnjava.moviereview.view.callback;

import androidx.fragment.app.Fragment;

public interface OnMainCallBack {
    void showFragment(String tag, Object data, boolean isBack, int anim);

    void replaceFragment(String tag, Object data, boolean isBack, int anim);

    void backToPrev();

    void reloadFragment(Fragment tag);

    void clearBackStack();

    Fragment getBackStack();
}

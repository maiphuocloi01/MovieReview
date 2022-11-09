package com.cnjava.moviereview.view.callback;

import androidx.fragment.app.Fragment;

public interface OnMainCallBack {
    void addFragment(String tag, Object data, boolean isBack, int anim);

    void replaceFragment(String tag, Object data, boolean isBack, int anim);

    void backToPrev();

    void reloadFragment(Fragment tag);

    void reloadFragmentByTag(String tag);

    void clearBackStack();

    Fragment getBackStack();

    void reloadAllBackStack();
}

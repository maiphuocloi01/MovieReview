package com.cnjava.moviereview.view.callback;

public interface OnMainCallBack {
    void showFragment(String tag, Object data, boolean isBack, int anim);
    void replaceFragment(String tag, Object data, boolean isBack, int anim);
    void backToPrev();
    void reloadFragment(String tag);
    void clearBackStack();
}

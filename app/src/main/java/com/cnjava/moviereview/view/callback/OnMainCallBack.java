package com.cnjava.moviereview.view.callback;

public interface OnMainCallBack {
    void showFragment(String tag, Object data, boolean isBack);
    void backToPrev();
}

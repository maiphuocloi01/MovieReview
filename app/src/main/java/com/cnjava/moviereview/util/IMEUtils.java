package com.cnjava.moviereview.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class IMEUtils {

    //Chuyển đổi trạng thái hiển thị / ẩn bàn phím
    public static void toggleSoftInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * Use only from Activities, don't use from Fragment (with getActivity) or from Dialog/DialogFragment
     */
    public static boolean hideSoftInput(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            return imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
        return false;
    }
//    public static void hideSoftInput(Activity activity) {
//        if (activity.getCurrentFocus() != null) {
//            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
//            activity.getWindow().getDecorView();
//        }
//    }


    /**
     * Use everywhere except from Activity (Custom View, Fragment, Dialogs, DialogFragments).
     */
    public static void showSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }


    /**
     * Use everywhere except from Activity (Custom View, Fragment, Dialogs, DialogFragments).
     */
    public static void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //Check if the keyboard is active
    public static boolean isActive(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();
    }
}

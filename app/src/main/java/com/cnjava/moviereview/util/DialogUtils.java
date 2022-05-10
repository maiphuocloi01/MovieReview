package com.cnjava.moviereview.util;

import android.app.ProgressDialog;
import android.content.Context;

import com.cnjava.moviereview.R;


public class DialogUtils {

    private static ProgressDialog progressDialog;

    public static void showLoadingDialog(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.custom_progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public static void hideLoadingDialog(){
        progressDialog.dismiss();
    }
}

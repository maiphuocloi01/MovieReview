package com.cnjava.moviereview.util.cutom;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cnjava.moviereview.util.ViewUtils;

public class ExpandableTextView {

    private static final String READ_MORE = "Read more";
    private static final String READ_LESS = "Read less";

    public static void onChange(TextView textBody, TextView buttonReadMore, int maxLine) {
        buttonReadMore.setOnClickListener(view -> {
            if (buttonReadMore.getText().equals(READ_MORE)) {
                textBody.setMaxLines(Integer.MAX_VALUE);
                textBody.setEllipsize(null);;
                buttonReadMore.setText(READ_LESS);
            } else {
                textBody.setMaxLines(maxLine);
                textBody.setEllipsize(TextUtils.TruncateAt.END);
                buttonReadMore.setText(READ_MORE);
            }
        });
        textBody.setOnClickListener(view -> {
            if (buttonReadMore.getText().equals(READ_MORE)) {
                textBody.setMaxLines(Integer.MAX_VALUE);
                textBody.setEllipsize(null);;
                buttonReadMore.setText(READ_LESS);
            } else {
                textBody.setMaxLines(maxLine);
                textBody.setEllipsize(TextUtils.TruncateAt.END);
                buttonReadMore.setText(READ_MORE);
            }
        });
    }

    public static void isEllipsized(TextView textView, int maxLine, TextView textButton) {
        double textPixelLength = textView.getPaint().measureText(textView.getText().toString());
        double numberOfLines = Math.ceil((textPixelLength / textView.getMeasuredWidth()));
        //return textView.getLineHeight() * numberOfLines >= textView.getMeasuredHeight();
        if(numberOfLines <= maxLine){
            ViewUtils.gone(textButton);
            textView.setEnabled(false);
        }
    }
}

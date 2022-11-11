package com.cnjava.moviereview.util.cutom;

import android.view.View;
import android.widget.TextView;

public class ExpandableTextView {

    private static final String READ_MORE = "Read more";
    private static final String READ_LESS = "Read less";

    public static void onChange(TextView textBody, TextView buttonReadMore, int maxLine) {
        buttonReadMore.setOnClickListener(view -> {
            if (buttonReadMore.getText().equals(READ_MORE)) {
                textBody.setMaxLines(0);
                buttonReadMore.setText(READ_LESS);
            } else {
                textBody.setMaxLines(maxLine);
                buttonReadMore.setText(READ_MORE);
            }
        });
        textBody.setOnClickListener(view -> {
            if (buttonReadMore.getText().equals(READ_MORE)) {
                textBody.setMaxLines(0);
                buttonReadMore.setText(READ_LESS);
            } else {
                textBody.setMaxLines(maxLine);
                buttonReadMore.setText(READ_MORE);
            }
        });
    }

    public static boolean isEllipsized(TextView textView) {
        double textPixelLength = textView.getPaint().measureText(textView.getText().toString());
        double numberOfLines = Math.ceil((textPixelLength / textView.getMeasuredWidth()));
        return textView.getLineHeight() * numberOfLines > textView.getMeasuredHeight();
    }
}

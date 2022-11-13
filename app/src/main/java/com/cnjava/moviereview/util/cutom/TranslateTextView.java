package com.cnjava.moviereview.util.cutom;

import android.widget.TextView;

public class TranslateTextView {

    private static final String READ_MORE = "Translate review";
    private static final String READ_LESS = "Original review";

    public static void onChange(TextView textBody, TextView buttonReadMore, String origin, String translate) {
        buttonReadMore.setOnClickListener(view -> {
            if (buttonReadMore.getText().equals(READ_MORE)) {
                textBody.setText(translate);
                buttonReadMore.setText(READ_LESS);
            } else {
                textBody.setText(origin);
                buttonReadMore.setText(READ_MORE);
            }
        });
    }
}

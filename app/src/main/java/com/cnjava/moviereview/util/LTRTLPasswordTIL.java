package com.cnjava.moviereview.util;

import android.content.Context;
import android.graphics.PorterDuff;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.TextViewCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Field;
import com.cnjava.moviereview.R;

public class LTRTLPasswordTIL extends TextInputLayout {

    public static final int RANK_WEAK = 4;
    public static final int RANK_STRONG = 5;

    private boolean legend = false;

    private Context context;

    public LTRTLPasswordTIL(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        this.getEditText().addTextChangedListener(passwordStrengthChecker);
    }

    @Override
    public void setErrorEnabled(boolean enabled) {
        super.setErrorEnabled(enabled);
        if (!enabled) {
            return;
        }

        try {
            Field errorViewField = TextInputLayout.class.getDeclaredField("mErrorView");
            errorViewField.setAccessible(true);
            TextView errorView = (TextView) errorViewField.get(this);
            if (errorView != null) {
                if (isLegend()) {
                    errorView.setGravity(Gravity.END);
                    LinearLayout.LayoutParams params =
                            new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMarginEnd(convertToDP(10));
                    params.gravity = Gravity.END;
                    errorView.setLayoutParams(params);
                } else {
                    errorView.setGravity(Gravity.START);
                    LinearLayout.LayoutParams params =
                            new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.START;
                    errorView.setLayoutParams(params);
                }
            }
        } catch (Exception e) {
            // At least log what went wrong
            e.printStackTrace();
        }

    }

    public boolean isLegend() {
        return legend;
    }

    public void setLegend(boolean legend) {
        this.legend = legend;
    }

    private int convertToDP(int px) {
        return (int) (px * context.getResources().getDisplayMetrics().density);
    }

    private TextWatcher passwordStrengthChecker = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            passwordStrengthChecker(editable.toString(), LTRTLPasswordTIL.this);
        }
    };

    public int passwordStrengthChecker(Object sourceObject, View indicatorView) {
        StringBuilder sourceText = new StringBuilder();

        if (sourceObject != null && sourceObject instanceof TextView) {
            sourceText.append(((TextView) sourceObject).getText().toString().trim());
        }
        if (sourceObject != null && sourceObject instanceof EditText) {
            sourceText.append(((EditText) sourceObject).getText().toString().trim());
        }

        if (sourceObject != null && sourceObject instanceof TextInputEditText) {
            sourceText.append(((TextInputEditText) sourceObject).getText().toString().trim());
        }

        if (sourceObject != null && sourceObject instanceof LTRTLPasswordTIL) {
            sourceText.append(((LTRTLPasswordTIL) sourceObject).getEditText().getText().toString().trim());
        }

        if (sourceObject != null && sourceObject instanceof String) {
            sourceText.append(sourceObject.toString().trim());
        }

        if (!sourceText.toString().isEmpty()) {
            int rank = passwordRanking(sourceText), errorTextApp = -1, errorTextId = -1, color = -1;
            if (rank < RANK_WEAK) {
                errorTextApp = R.style.AppTheme_TextErrorAppearance;
                errorTextId = R.string.password_weak;
                color = R.color.colorWarning;
            } else if (rank >= RANK_STRONG) {
                errorTextApp = R.style.AppTheme_PasswordStrongAppearance;
                errorTextId = R.string.password_strong;
                color = R.color.colorGreen;
            }

            if (errorTextApp != -1 && errorTextId != -1) {
                if (indicatorView != null && indicatorView instanceof TextView) {
                    ((TextView) indicatorView).setText(errorTextId);
                    TextViewCompat.setTextAppearance((TextView) indicatorView, errorTextApp);
                }

                if (indicatorView != null && indicatorView instanceof TextInputLayout) {
                    Context context = indicatorView.getContext();
                    ((TextInputLayout) indicatorView).setErrorEnabled(true);
                    ((TextInputLayout) indicatorView).setError(context.getString(errorTextId));
                    ((TextInputLayout) indicatorView).setErrorTextAppearance(errorTextApp);
                    ((TextInputLayout) indicatorView).getEditText().getBackground()
                            .setColorFilter(ResourcesCompat.getColor(
                                    context.getResources(), color, null), PorterDuff.Mode.SRC_ATOP);
                }

                if (indicatorView != null && indicatorView instanceof LTRTLPasswordTIL) {
                    Context context = indicatorView.getContext();
                    ((LTRTLPasswordTIL) indicatorView).setLegend(true);
                    ((LTRTLPasswordTIL) indicatorView).setErrorEnabled(true);
                    ((LTRTLPasswordTIL) indicatorView).setError(context.getString(errorTextId));
                    ((LTRTLPasswordTIL) indicatorView).setErrorTextAppearance(errorTextApp);
                    ((LTRTLPasswordTIL) indicatorView).getEditText().getBackground()
                            .setColorFilter(ResourcesCompat.getColor(
                                    context.getResources(), color, null), PorterDuff.Mode.SRC_ATOP);
                }
            }
            sourceText.replace(0, sourceText.length(), ""); // Clear the string builder
            sourceText.setLength(0); // Clear the string builder
            return rank;
        } else {
            sourceText.setLength(0); // Clear the string builder
            return -1;
        }
    }

    public static int passwordRanking(StringBuilder textToVerify) throws IllegalArgumentException {

        if (textToVerify == null) {
            throw new IllegalArgumentException();
        }
        int strength = -1;

        boolean containsUppercase = !textToVerify.equals(textToVerify.toString().toLowerCase());
        boolean containsLowercase = !textToVerify.equals(textToVerify.toString().toUpperCase());

        if (containsUppercase && containsLowercase) {
            strength += 2;
        } else {
            strength--;
        }

        int numDigits = getNumberDigits(textToVerify);
        if (numDigits > 0 && numDigits != textToVerify.length()) {
            strength++;
        }

        int numSymbols = getSymbols(textToVerify);
        if (numSymbols > 0 && numSymbols != textToVerify.length()) {
            strength++;
        }

        if (textToVerify.length() >= 8) {
            strength += 2;
        }
        return strength;
    }

    public static int getNumberDigits(StringBuilder inString) {
        if (isEmpty(inString)) {
            return 0;
        }
        int numDigits = 0;
        int length = inString.length();
        for (int i = 0; i < length; i++) {
            if (Character.isDigit(inString.charAt(i))) {
                numDigits++;
            }
        }
        return numDigits;
    }

    public static int getSymbols(StringBuilder inString) {
        if (isEmpty(inString)) {
            return 0;
        }
        int numDigits = 0;
        int length = inString.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isLetterOrDigit(inString.charAt(i))) {
                numDigits++;
            }
        }
        return numDigits;
    }

    public static boolean isEmpty(StringBuilder inString) {
        return inString == null || inString.length() == 0;
    }
}

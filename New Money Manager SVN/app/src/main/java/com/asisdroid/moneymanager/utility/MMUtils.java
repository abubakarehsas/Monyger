package com.asisdroid.moneymanager.utility;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by ashishkumarpolai on 7/14/2017.
 */

public class MMUtils {

    private static Animation fadeInAnime;

    public static void startAnimate(View animationView, int anime, Context instance)
    {
        fadeInAnime = AnimationUtils.loadAnimation(instance, anime);
        fadeInAnime.setFillAfter(true);
        animationView.startAnimation(fadeInAnime);
        fadeInAnime = null;
    }

    public static void hideKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

}

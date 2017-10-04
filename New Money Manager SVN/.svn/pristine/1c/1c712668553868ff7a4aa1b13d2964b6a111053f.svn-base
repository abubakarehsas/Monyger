
package com.asisdroid.moneymanager.utility;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FontUtils {

    private static FontUtils utils;
    private static Typeface GothamBold;
    private static Typeface GothamLight;
    private static Typeface GothamMedium;
    private static Typeface GothamXLight;
    private static Typeface GothamBook;

    public static final int GothamHTF_Bold = 101;
    public static final int GothamHTF_Light = 102;
    public static final int GothamHTF_Medium = 103;
    public static final int GothamHTF_XLight = 104;
    public static final int GothamHTF_Book = 105;

    public static FontUtils getInstance(Context context) {
        if (utils == null) {
            utils = new FontUtils(context);
        }
        return utils;
    }

    private FontUtils(Context context) {
        GothamBold = Typeface.createFromAsset(context.getAssets(),
                "GothamHTF-Bold.otf");
        GothamLight = Typeface.createFromAsset(context.getAssets(),
                "GothamHTF-Light.otf");
        GothamMedium = Typeface.createFromAsset(context.getAssets(),
                "GothamHTF-Medium.otf");
        GothamXLight = Typeface.createFromAsset(context.getAssets(),
                "GothamHTF-XLight.otf");
        GothamBook = Typeface.createFromAsset(context.getAssets(),
                "GothamHTF-Book.otf");

    }

    /* setting font for textview */
    public void setFont(TextView textView, int font) {
        switch (font) {
            case GothamHTF_Bold:
                textView.setTypeface(GothamBold);
                break;
            case GothamHTF_Light:
                textView.setTypeface(GothamLight);
                break;
            case GothamHTF_Medium:
                textView.setTypeface(GothamMedium);
                break;
            case GothamHTF_XLight:
                textView.setTypeface(GothamXLight);
                break;
            case GothamHTF_Book:
                textView.setTypeface(GothamBook);
                break;

        }
    }

    //setting font for button

	public void setFont(Button button, int font) {
		switch (font) {
            case GothamHTF_Medium:
                button.setTypeface(GothamMedium);
                break;
            case GothamHTF_Book:
                button.setTypeface(GothamBook);
                break;
		}
	}


    //setting font for Edittext

    public void setFont(EditText editText, int font) {
        switch (font) {
            case GothamHTF_Medium:
                editText.setTypeface(GothamMedium);
                break;
            case GothamHTF_Light:
                editText.setTypeface(GothamLight);
                break;
        }
    }



}

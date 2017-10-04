package com.asisdroid.moneymanager.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ashishkumarpolai on 7/14/2017.
 */

public class MoneyManagerPreferences {
    private static MoneyManagerPreferences sCommonPref;
    public SharedPreferences mPreference;
    private Context mContext;

    private String CURRENT_DATE_VIEW_FOR_THE_DATAS = "currentdateview";

    public static MoneyManagerPreferences getInstance(Context context) {
        if (sCommonPref == null) {
            sCommonPref = new MoneyManagerPreferences(context);
        }
        return sCommonPref;
    }

    public MoneyManagerPreferences(Context context) {
        mContext = context;
        mPreference = mContext.getSharedPreferences("NissanConnect_Preferences",
                Context.MODE_PRIVATE);
    }

    public void clearPreference() {
        mPreference.edit().clear().commit();
    }

    public String getCurrentDateView() {
        return mPreference.getString(CURRENT_DATE_VIEW_FOR_THE_DATAS, "");
    }

    public void setCurrentDateView(String currentView) {

        mPreference.edit().putString(CURRENT_DATE_VIEW_FOR_THE_DATAS, currentView)
                .commit();
    }
}

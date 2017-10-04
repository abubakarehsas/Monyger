package com.asisdroid.moneymanager.Dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

/**
 * Created by ashishkumarpolai on 7/14/2017.
 * CAN BE USED FOR ALL DIALOGS
 */

public class DialogDatepickerFragment extends DialogFragment {

    public static DialogDatepickerFragment getInstance(){

        //  BELOW CODES FOR PASSING THE PARAMETERS HERE FROM CALLING FRAGMENTS
     /*   Bundle b = new Bundle();
        b.putString("", message);

        DialogDatepickerFragment dialogDatepickerFragment = new DialogDatepickerFragment();
        dialogDatepickerFragment.setArguments(b);*/


        return new DialogDatepickerFragment();
    }


    private DatePickerDialog myDatepickerBox;
    private DatePickerDialog.OnDateSetListener datepickerListener;
    private Calendar cal = Calendar.getInstance();

    /*
    //PASS SOME VARIABLE FROM THE ACTIVITY

    public void setCal(Calendar cal){
        this.cal = cal;
    }*/


   //WILL BE CALLED ONLY ON SHOW DIALOG
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR,-10);

        myDatepickerBox = new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), cal
                .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        myDatepickerBox.getDatePicker().setMinDate(c.getTimeInMillis());
        myDatepickerBox.getDatePicker().setMaxDate(System.currentTimeMillis());
        myDatepickerBox.setCustomTitle(null);
        return myDatepickerBox;
    }


}

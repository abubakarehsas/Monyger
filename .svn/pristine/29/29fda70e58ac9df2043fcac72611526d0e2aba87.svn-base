package com.asisdroid.moneymanager.Dialog;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.asisdroid.moneymanager.AddAccFragment;
import com.asisdroid.moneymanager.MainActivity;
import com.asisdroid.moneymanager.R;
import com.asisdroid.moneymanager.database.CategoryDBAdapter;
import com.asisdroid.moneymanager.database.CategoryIncomeDBAdapter;
import com.asisdroid.moneymanager.utility.MMUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ashishkumarpolai on 7/14/2017.
 * CAN BE USED FOR ALL DIALOGS
 */

public class DialogRemainderFragment extends DialogFragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private LayoutInflater inflater;
    private View addRemainderView;

    private AlertDialog alertDialog;
    private  AlertDialog.Builder alertDialogBuilder;

    private TextView txtDailyAlert, txtWeeklyAlert, txtMonthlyAlert, txtNoAlert;

    private String currentSelectedAlert = "noalert", currentSelectedAlertTime = "", remainderTime = "";

    private Spinner selectPeriod;
    private TimePicker selectTimePicker;
    private Button saveRemainder;

    List<String> typeofpayments = new ArrayList<String>();
    private RelativeLayout timeDialogLayout;
    ArrayAdapter<String> dataAdapter;

    private boolean isSaveClicked = false;

    private int mShortAnimationDuration, remainderPeriod = 0;

    private int  timeHourSelected, timeMinSelected;

    //INTERFACE CREATED TO INTERACT WITH THE PARENT FARGMENT OR ANY OTHER FRAGMENT OR ACTIVITY
    public interface onRemainderDialogClosed{
        //ABSTRACT METHOD TO BE CALLED FROM THE IMPLEMENTED FRAGMENT
        public void onRemainderDClosed(String targetAlertView, String targetDate);
    }
    //INTERFACE OBJECT CREATED FOR GIVING THE INSTANCE OF THE INTERFACE FOR ANY OTHER FRAGMENT OR ACTIVITY
    public static onRemainderDialogClosed dialogCloseListener;

    public static DialogRemainderFragment getInstance(MainActivity mainInstance){
        //ALLOCATING THE IMPLEMENTING FRAGMENT TO THE INTERFACE'S OBJECT i.e. CREATING INTERFACE FOR THE FRAGMENT
        dialogCloseListener = (onRemainderDialogClosed) mainInstance;
        DialogRemainderFragment addcatgfragment = new DialogRemainderFragment();
        return addcatgfragment;
     }

    /*
    //PASS SOME VARIABLE FROM THE ACTIVITY

    public void setCal(Calendar cal){
        this.cal = cal;
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //WILL BE CALLED ONLY ON SHOW DIALOG
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        inflater = getActivity().getLayoutInflater();
        addRemainderView = inflater.inflate(R.layout.remainder_dialog_layout, null);
        alertDialogBuilder.setView(addRemainderView);

        alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        return alertDialog;
    }

//CALCULATOR CODES

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initAddCategoryUI();
        initAddCatgEvents();
    }

     @TargetApi(Build.VERSION_CODES.M)
     public void initAddCategoryUI()
    {
        txtDailyAlert = (TextView) addRemainderView.findViewById(R.id.alertType1);
        txtWeeklyAlert = (TextView) addRemainderView.findViewById(R.id.alertType2);
        txtMonthlyAlert = (TextView) addRemainderView.findViewById(R.id.alertType3);
        txtNoAlert = (TextView) addRemainderView.findViewById(R.id.noAlert);

        saveRemainder = (Button) addRemainderView.findViewById(R.id.saveRemainder);

        txtDailyAlert.setTag(R.drawable.category_gradient_border);
        txtWeeklyAlert.setTag(R.drawable.category_gradient_border);
        txtMonthlyAlert.setTag(R.drawable.category_gradient_border);
        txtNoAlert.setTag(R.drawable.category_gradient_border);

        txtDailyAlert.setOnClickListener(this);
        txtWeeklyAlert.setOnClickListener(this);
        txtMonthlyAlert.setOnClickListener(this);
        txtNoAlert.setOnClickListener(this);
        saveRemainder.setOnClickListener(this);

        // Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        selectPeriod = (Spinner) addRemainderView.findViewById(R.id.selectPeriod);
        selectTimePicker = (TimePicker) addRemainderView.findViewById(R.id.selectTime);

        timeDialogLayout = (RelativeLayout) addRemainderView.findViewById(R.id.timepickAlertLayout);

        currentSelectedAlert = getActivity().getSharedPreferences("remainderData", Context.MODE_PRIVATE).getString("alertType","noalert");
        currentSelectedAlertTime = getActivity().getSharedPreferences("remainderData", Context.MODE_PRIVATE).getString("alertTime","");


        //SETTING THE CURRENT SELECTED REMAINDER
        if(currentSelectedAlert.equalsIgnoreCase("noalert")){
            selectNoAlertButton();
            timeDialogLayout.setVisibility(View.GONE);
        }
        else if(currentSelectedAlert.equalsIgnoreCase("daily")){
             selectUnselectFunctions("daily",txtDailyAlert, txtWeeklyAlert, txtMonthlyAlert, R.drawable.weekly_alert, R.drawable.monthly_alert);
            selectPeriod.setVisibility(View.GONE);
            timeDialogLayout.setVisibility(View.VISIBLE);
            checkNoAlertButton();
        }
        else if(currentSelectedAlert.equalsIgnoreCase("weekly")){
            selectUnselectFunctions("weekly",txtWeeklyAlert, txtDailyAlert, txtMonthlyAlert,  R.drawable.daily_alert, R.drawable.monthly_alert);
            timeDialogLayout.setVisibility(View.VISIBLE);
            selectPeriod.setVisibility(View.VISIBLE);
            checkNoAlertButton();
            //Setting remainder times for WEEKLY
            typeofpayments.clear();
            typeofpayments.add("FRIDAY");
            typeofpayments.add("SATURDAY");

        }
        else if(currentSelectedAlert.equalsIgnoreCase("monthly")){
            selectUnselectFunctions("monthly",txtMonthlyAlert, txtWeeklyAlert, txtDailyAlert, R.drawable.weekly_alert, R.drawable.daily_alert);
            timeDialogLayout.setVisibility(View.VISIBLE);
            selectPeriod.setVisibility(View.VISIBLE);
            checkNoAlertButton();

            setMonthlySelectSpinner();
        }

        // Creating adapter for spinner
        dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.whitetxt_spinner_textview, typeofpayments);
        selectPeriod.setAdapter(dataAdapter);

        //SETTING THE LAST SELECTED
        try {
            remainderPeriod = Integer.parseInt(currentSelectedAlertTime.split(":")[0]);
            if(currentSelectedAlert.equalsIgnoreCase("monthly") || currentSelectedAlert.equalsIgnoreCase("weekly")) {
                selectPeriod.setSelection(remainderPeriod, true);
            }
            remainderTime = currentSelectedAlertTime.split(":")[1]+":"+currentSelectedAlertTime.split(":")[2];

                int currentApiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1){
                    selectTimePicker.setHour(Integer.parseInt(currentSelectedAlertTime.split(":")[1]));
                    selectTimePicker.setMinute(Integer.parseInt(currentSelectedAlertTime.split(":")[2]));
                } else {
                    selectTimePicker.setCurrentHour(Integer.parseInt(currentSelectedAlertTime.split(":")[1]));
                    selectTimePicker.setCurrentMinute(Integer.parseInt(currentSelectedAlertTime.split(":")[2]));
                }
        }
        catch(Exception e){
        }


    }

    public void initAddCatgEvents()
    {
        selectTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                //remainderTime = hourOfDay+":"+minute;
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(isSaveClicked) {
            //CALLING THE ABSTRACT METHOD OF THE INTERFACE
            dialogCloseListener.onRemainderDClosed(currentSelectedAlert, currentSelectedAlertTime);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.alertType1:
                 selectUnselectFunctions("daily",txtDailyAlert, txtWeeklyAlert, txtMonthlyAlert, R.drawable.weekly_alert, R.drawable.monthly_alert);
                checkNoAlertButton();
                selectPeriod.setVisibility(View.GONE);
                selectPeriod.animate()
                        .alpha(0f)
                        .setDuration(mShortAnimationDuration);
                timeDialogLayout.setVisibility(View.VISIBLE);
                timeDialogLayout.animate()
                        .alpha(1f)
                        .setDuration(mShortAnimationDuration);
                MMUtils.startAnimate(timeDialogLayout,R.anim.shake,getActivity());
                Toast.makeText(getActivity(), "Please, set your time.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.alertType2:
                selectUnselectFunctions("weekly",txtWeeklyAlert, txtDailyAlert, txtMonthlyAlert,  R.drawable.daily_alert, R.drawable.monthly_alert);
                checkNoAlertButton();
                timeDialogLayout.animate()
                        .alpha(1f)
                        .setDuration(mShortAnimationDuration);
                selectPeriod.animate()
                        .alpha(1f)
                        .setDuration(0);
                selectPeriod.setVisibility(View.VISIBLE);
                timeDialogLayout.setVisibility(View.VISIBLE);
                //Setting remainder times for WEEKLY
                typeofpayments.clear();
                typeofpayments.add("FRIDAY");
                typeofpayments.add("SATURDAY");
                dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.whitetxt_spinner_textview, typeofpayments);
                selectPeriod.setAdapter(dataAdapter);
                MMUtils.startAnimate(timeDialogLayout,R.anim.shake,getActivity());
                Toast.makeText(getActivity(), "Please, set your day & time.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.alertType3:
                selectUnselectFunctions("monthly",txtMonthlyAlert, txtWeeklyAlert, txtDailyAlert, R.drawable.weekly_alert, R.drawable.daily_alert);
                checkNoAlertButton();
                selectPeriod.animate()
                        .alpha(1f)
                        .setDuration(0);
                timeDialogLayout.animate()
                        .alpha(1f)
                        .setDuration(mShortAnimationDuration);
                selectPeriod.setVisibility(View.VISIBLE);
                timeDialogLayout.setVisibility(View.VISIBLE);

                setMonthlySelectSpinner();

                dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.whitetxt_spinner_textview, typeofpayments);
                selectPeriod.setAdapter(dataAdapter);
                MMUtils.startAnimate(timeDialogLayout,R.anim.shake,getActivity());
                Toast.makeText(getActivity(), "Please, set your date & time.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.noAlert:
                selectNoAlertButton();
                timeDialogLayout.setVisibility(View.GONE);
                timeDialogLayout.animate()
                        .alpha(0f)
                        .setDuration(mShortAnimationDuration);
            break;

            case R.id.saveRemainder:
                isSaveClicked = true;
                int currentApiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1){
                    remainderTime = selectTimePicker.getHour() + ":" +  selectTimePicker.getMinute();
                } else {
                    remainderTime = selectTimePicker.getCurrentHour() + ":" +  selectTimePicker.getCurrentMinute();
                }
                remainderPeriod = selectPeriod.getSelectedItemPosition();
                currentSelectedAlertTime = remainderPeriod+":"+remainderTime;
                 this.dismiss();
                break;

            default:
                break;
        }
    }

    public void selectUnselectFunctions(String currentAlertType, TextView activeTv, TextView unactiveTv1, TextView unactiveTv2 , int unactive1Drwble, int unactive2Drwble){
        if(activeTv.getTag().equals(R.drawable.category_gradient_border)) {
            currentSelectedAlert = currentAlertType;
            MMUtils.startAnimate(activeTv, R.anim.shake, addRemainderView.getContext());
            activeTv.setBackground(getResources().getDrawable(R.drawable.category_selected_border_gradient));
            activeTv.setTag(R.drawable.category_selected_border_gradient);
            activeTv.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.selected_alert), null, null);
        }
        if(unactiveTv1.getTag().equals(R.drawable.category_selected_border_gradient)) {
            unactiveTv1.setTag(R.drawable.category_gradient_border);
            unactiveTv1.setBackground(getResources().getDrawable(R.drawable.category_gradient_border));
            unactiveTv1.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(unactive1Drwble), null, null);
        }
        if(unactiveTv2.getTag().equals(R.drawable.category_selected_border_gradient)) {
            unactiveTv2.setTag(R.drawable.category_gradient_border);
            unactiveTv2.setBackground(getResources().getDrawable(R.drawable.category_gradient_border));
            unactiveTv2.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(unactive2Drwble), null, null);
        }
    }

    public void checkNoAlertButton(){
        if(txtNoAlert.getTag().equals(R.drawable.calculator_red_buttons_gradient)){
            txtNoAlert.setBackground(getResources().getDrawable(R.drawable.category_gradient_border));
            txtNoAlert.setTag(R.drawable.category_gradient_border);
        }
    }

    public void selectNoAlertButton(){
        if(txtNoAlert.getTag().equals(R.drawable.category_gradient_border)) {
            currentSelectedAlert = "noalert";
            MMUtils.startAnimate(txtNoAlert, R.anim.blink, addRemainderView.getContext());
            txtNoAlert.setBackground(getResources().getDrawable(R.drawable.calculator_red_buttons_gradient));
            txtNoAlert.setTag(R.drawable.calculator_red_buttons_gradient);
        }
        if(txtWeeklyAlert.getTag().equals(R.drawable.category_selected_border_gradient)) {
            txtWeeklyAlert.setTag(R.drawable.category_gradient_border);
            txtWeeklyAlert.setBackground(getResources().getDrawable(R.drawable.category_gradient_border));
            txtWeeklyAlert.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.weekly_alert), null, null);
        }
        if(txtDailyAlert.getTag().equals(R.drawable.category_selected_border_gradient)) {
            txtDailyAlert.setTag(R.drawable.category_gradient_border);
            txtDailyAlert.setBackground(getResources().getDrawable(R.drawable.category_gradient_border));
            txtDailyAlert.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.daily_alert), null, null);
        }
        if(txtMonthlyAlert.getTag().equals(R.drawable.category_selected_border_gradient)) {
            txtMonthlyAlert.setTag(R.drawable.category_gradient_border);
            txtMonthlyAlert.setBackground(getResources().getDrawable(R.drawable.category_gradient_border));
            txtMonthlyAlert.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.monthly_alert), null, null);
        }
    }

    public void setMonthlySelectSpinner()
    {
        int data = 28;
        //Setting remainder times for MONTHLY
        typeofpayments.clear();
        typeofpayments.add("Last day of every month");
        for(int x=0;x<4;x++){
            typeofpayments.add(data+"th of every month");
            data--;
        }
    }
}

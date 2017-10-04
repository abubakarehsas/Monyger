package com.asisdroid.moneymanager.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.asisdroid.moneymanager.MainActivity;
import com.asisdroid.moneymanager.R;

/**
 * Created by ashishkumarpolai on 7/14/2017.
 * CAN BE USED FOR ALL DIALOGS
 */

public class DialogRemindMeFragment extends DialogFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private LayoutInflater inflater;
    private View remindMeView;

    private AlertDialog alertDialog;
    private  AlertDialog.Builder alertDialogBuilder;

    private SharedPreferences remindMePreference;

    private boolean isRemindme;

    private Button btn_Ok, btn_Cancel;

    //INTERFACE CREATED TO INTERACT WITH THE PARENT FARGMENT OR ANY OTHER FRAGMENT OR ACTIVITY
    public interface onRemindMeDialogClosedListener{
        //ABSTRACT METHOD TO BE CALLED FROM THE IMPLEMENTED FRAGMENT
        public void onRemindMeDialogClosed(boolean isRemindMe);
    }
    //INTERFACE OBJECT CREATED FOR GIVING THE INSTANCE OF THE INTERFACE FOR ANY OTHER FRAGMENT OR ACTIVITY
    public static DialogRemindMeFragment.onRemindMeDialogClosedListener remindMeDialogCloseListener;

    public static DialogRemindMeFragment getInstance(MainActivity mainInstance){
        //ALLOCATING THE IMPLEMENTING FRAGMENT TO THE INTERFACE'S OBJECT i.e. CREATING INTERFACE FOR THE FRAGMENT
        remindMeDialogCloseListener = (onRemindMeDialogClosedListener) mainInstance;
        DialogRemindMeFragment addcatgfragment = new DialogRemindMeFragment();
        return addcatgfragment;
     }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //WILL BE CALLED ONLY ON SHOW DIALOG
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        inflater = getActivity().getLayoutInflater();
        remindMeView = inflater.inflate(R.layout.remindme_dialog_layout, null);
        alertDialogBuilder.setView(remindMeView);

        alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }

//CALCULATOR CODES

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initAddCategoryUI();
        initAddCatgEvents();
    }

    public void initAddCategoryUI()
    {
        remindMePreference = getActivity().getSharedPreferences("remindMeData", Context.MODE_PRIVATE);

        btn_Ok = (Button) remindMeView.findViewById(R.id.btnRemindMeOk);
        btn_Cancel = (Button) remindMeView.findViewById(R.id.btnRemindMeCancel);

        isRemindme = remindMePreference.getBoolean("isRemindMe", false);
    }

    public void initAddCatgEvents()
    {
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRemindme = false;
                dismiss();
            }
        });

        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRemindme = true;
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        //CALLING THE ABSTRACT METHOD OF THE INTERFACE
        remindMeDialogCloseListener.onRemindMeDialogClosed(isRemindme);
    }
}

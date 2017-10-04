package com.asisdroid.moneymanager.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asisdroid.moneymanager.AddAccFragment;
import com.asisdroid.moneymanager.R;
import com.asisdroid.moneymanager.database.CategoryDBAdapter;
import com.asisdroid.moneymanager.database.CategoryIncomeDBAdapter;
import com.asisdroid.moneymanager.utility.MMUtils;

import java.util.ArrayList;

/**
 * Created by ashishkumarpolai on 7/14/2017.
 * CAN BE USED FOR ALL DIALOGS
 */

public class DialogAddCatgFragment extends DialogFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private LayoutInflater inflater;
    private View addCatgView;

    public CategoryDBAdapter catgExpenseDbInDialog;
    public CategoryIncomeDBAdapter catgIncomeDbInDialog;
    public ArrayList<Integer> myECategoriesUseListinDialog, myICategoriesUseListinDialog;

    private AlertDialog alertDialog;
    private  AlertDialog.Builder alertDialogBuilder;

    private EditText edttxtNewCatg;
    private TextView txtHeading, txtNewCatg, txtError;
    private Button btnAdd, btnCancel;

    private boolean isESuccessfullySavedOrNot = false, isISuccessfullySavedOrNot = false;

    private String searchedCatg, catgType;

    //INTERFACE CREATED TO INTERACT WITH THE PARENT FARGMENT OR ANY OTHER FRAGMENT OR ACTIVITY
    public interface OnDialogClosedListener{
        //ABSTRACT METHOD TO BE CALLED FROM THE IMPLEMENTED FRAGMENT
        public void onDialogClosed(String catgType);
    }
    //INTERFACE OBJECT CREATED FOR GIVING THE INSTANCE OF THE INTERFACE FOR ANY OTHER FRAGMENT OR ACTIVITY
    public static OnDialogClosedListener dialogCloseListener;

    public static DialogAddCatgFragment getInstance(AddAccFragment fragment, String searchedCatgeory, String catgType){
        //ALLOCATING THE IMPLEMENTING FRAGMENT TO THE INTERFACE'S OBJECT i.e. CREATING INTERFACE FOR THE FRAGMENT
        dialogCloseListener = (OnDialogClosedListener) fragment;
        DialogAddCatgFragment addcatgfragment = new DialogAddCatgFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, searchedCatgeory);
        args.putString(ARG_PARAM2, catgType);
        addcatgfragment.setArguments(args);
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
        if (getArguments() != null) {
            searchedCatg = getArguments().getString(ARG_PARAM1);
            catgType = getArguments().getString(ARG_PARAM2);
        }
    }

    //WILL BE CALLED ONLY ON SHOW DIALOG
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        inflater = getActivity().getLayoutInflater();
        addCatgView = inflater.inflate(R.layout.addcatgeory_dialog_layout, null);
        alertDialogBuilder.setView(addCatgView);

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
        edttxtNewCatg = (EditText) addCatgView.findViewById(R.id.edttxtNewCatg);

        txtHeading = (TextView) addCatgView.findViewById(R.id.txt_AddCatgHeading);
        txtNewCatg = (TextView) addCatgView.findViewById(R.id.catgCustom);
        txtError = (TextView) addCatgView.findViewById(R.id.txtErr);

        btnAdd = (Button) addCatgView.findViewById(R.id.btnAddCatg);
        btnCancel = (Button) addCatgView.findViewById(R.id.btnCancelAddCatg);

        if(searchedCatg.equalsIgnoreCase("")) {
            txtNewCatg.setText("Custom");
        }
        else{
            txtNewCatg.setText(searchedCatg);
            edttxtNewCatg.setText(searchedCatg);
        }

        if(catgType == "expense") {
            txtHeading.setText("Add your category for Expense");
        }
        else if(catgType == "income"){
            txtHeading.setText("Add your category for Income");
        }

        catgExpenseDbInDialog = new CategoryDBAdapter(getActivity());
        catgExpenseDbInDialog = catgExpenseDbInDialog.open();

        catgIncomeDbInDialog = new CategoryIncomeDBAdapter(getActivity());
        catgIncomeDbInDialog = catgIncomeDbInDialog.open();

        myECategoriesUseListinDialog = catgExpenseDbInDialog.getAllCatgUse();
        myICategoriesUseListinDialog = catgIncomeDbInDialog.getAllCatgUse();

        if(edttxtNewCatg.getText().toString().equalsIgnoreCase("")) {
            btnAdd.setEnabled(false);
            btnAdd.setTextColor(getResources().getColor(R.color.DarkGrey));
        }

        // Toast.makeText(getActivity(), "maxuse="+myECategoriesUseListinDialog, Toast.LENGTH_SHORT).show();

    }

    public void initAddCatgEvents()
    {
        edttxtNewCatg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                txtNewCatg.setText(edttxtNewCatg.getText().toString());
                txtError.setVisibility(View.GONE);
                if(edttxtNewCatg.getText().toString().equalsIgnoreCase("")) {
                    btnAdd.setEnabled(false);
                    btnAdd.setTextColor(getResources().getColor(R.color.DarkGrey));
                }
                else{
                    btnAdd.setEnabled(true);
                    btnAdd.setTextColor(getResources().getColor(R.color.White));
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(edttxtNewCatg.getText().toString().equalsIgnoreCase(""))) {
                   if(catgType.equalsIgnoreCase("expense")) {
                      isESuccessfullySavedOrNot = catgExpenseDbInDialog.insertNewCategory(edttxtNewCatg.getText().toString(), String.valueOf(myECategoriesUseListinDialog.get(0) + 1), "yes", "custom");
                      //CHECKING IF CATG TYPE IS ALREADY THERE IN DATABASE
                      if (isESuccessfullySavedOrNot == true) {
                          MMUtils.hideKeyboard(getActivity(),edttxtNewCatg);
                          alertDialog.dismiss();
                          Toast.makeText(getActivity(), edttxtNewCatg.getText().toString() + " Category added successfully!", Toast.LENGTH_SHORT).show();
                      } else {
                          txtError.setVisibility(View.VISIBLE);
                      }
                   }
                   else if(catgType.equalsIgnoreCase("income")){
                       isISuccessfullySavedOrNot = catgIncomeDbInDialog.insertNewCategory(edttxtNewCatg.getText().toString(), String.valueOf(myICategoriesUseListinDialog.get(0) + 1), "yes", "custom");
                       //CHECKING IF CATG TYPE IS ALREADY THERE IN DATABASE
                       if (isISuccessfullySavedOrNot == true) {
                           MMUtils.hideKeyboard(getActivity(),edttxtNewCatg);
                           alertDialog.dismiss();
                           Toast.makeText(getActivity(), edttxtNewCatg.getText().toString() + " Category added successfully!", Toast.LENGTH_SHORT).show();
                       } else {
                           txtError.setVisibility(View.VISIBLE);
                       }
                   }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        //CALLING THE ABSTRACT METHOD OF THE INTERFACE
        dialogCloseListener.onDialogClosed(catgType);
    }
}

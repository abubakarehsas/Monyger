package com.asisdroid.moneymanager.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.asisdroid.moneymanager.AddAccFragment;
import com.asisdroid.moneymanager.R;
import com.asisdroid.moneymanager.database.CategoryDBAdapter;
import com.asisdroid.moneymanager.database.CategoryIncomeDBAdapter;
import com.asisdroid.moneymanager.utility.MMUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ashishkumarpolai on 7/14/2017.
 * CAN BE USED FOR ALL DIALOGS
 */

public class DialogAllCatgFragment extends DialogFragment implements  View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private LayoutInflater inflater;
    private View allCatgView;

    public CategoryDBAdapter catgExpenseDbInAllDialog;
    public CategoryIncomeDBAdapter catgIncomeDbInAllDialog;
    public ArrayList<String> myAllCategoriesENameList, myAllCategoriesEImgList,
            myAllCategoriesINameList, myAllCategoriesIImgList, myTempAllCatgNameLsit;

    private AlertDialog alertDialog;
    private  AlertDialog.Builder alertDialogBuilder;

    private String searchedCatg, catgType, selectedExpenseCatg = "", selectedIncomeCatg = "", prevSelectedCatg = "";

    private SpannableString ss;

    //CATEGORY TEXTVIEWS
    private TextView catgE1,catgE2,catgE3,catgE4,catgE5,catgE6,catgE7,catgE8,catgE9,catgE10,catgE11,
            catgE12,catgE13,catgE14,catgE15,catgE16,catgE17,catgE18,catgE19,catgE20,catgE21,
            catgE22,catgE23,catgE24,catgE25,catgE26,catgE27,catgE28, catgESave, catgEDelete,
            catgI1,catgI2,catgI3,catgI4,catgI5,catgI6,catgI7,catgI8, catgI9,catgI10,catgI11,
            catgI12,catgI13,catgI14,catgI15,catgI16,catgI17,catgI18,catgI19,catgI20,catgI21,
            catgI22,catgI23,catgI24,catgI25,catgI26,catgI27,catgI28,catgISave, catgIDelete;

    private HashMap<String, Integer> catgAllDrawableHashmap = new HashMap<>();
    private HashMap<Integer, Integer> catgAllETextViewsHashmap = new HashMap<>();
    private HashMap<String, String> catgAllEFullNamesHashmap = new HashMap<>();

    private HashMap<Integer, Integer> catgAllITextViewsHashmap = new HashMap<>();
    private HashMap<String, String> catgAllIFullNamesHashmap = new HashMap<>();

    private TextView catgAllECommonTextview, errorNoCatgeory, catgAllHeadingTextview,
            catgAllICommonTextview, errorNoCatgeoryIncome, catgAllHeadingTextviewIncome;

    private EditText edttxt_SearchAllCategoryExpense, edttxt_SearchAllCategoryIncome;

    private ScrollView scrollview_AllExpenseCatgs, scrollview_AllIncomeCatgs;

    private RelativeLayout expenseLayouts, incomeLayouts;

    private boolean isSaveClicked = false, isDeleteActive = false;


    //INTERFACE CREATED TO INTERACT WITH THE PARENT FARGMENT OR ANY OTHER FRAGMENT OR ACTIVITY
    public interface OnAllCatgDialogClosedListener{
        //ABSTRACT METHOD TO BE CALLED FROM THE IMPLEMENTED FRAGMENT
        public void onAllCatgDialogClosed(String selectedCatg, String catgSentOncloseType);
    }
    //INTERFACE OBJECT CREATED FOR GIVING THE INSTANCE OF THE INTERFACE FOR ANY OTHER FRAGMENT OR ACTIVITY
    public static OnAllCatgDialogClosedListener dialogAllCatgCloseListener;

    public static DialogAllCatgFragment getInstance(AddAccFragment fragment, String searchedCatgeory, String catgType, String selectedCatg){
        //ALLOCATING THE IMPLEMENTING FRAGMENT TO THE INTERFACE'S OBJECT i.e. CREATING INTERFACE FOR THE FRAGMENT
        dialogAllCatgCloseListener = (OnAllCatgDialogClosedListener) fragment;
        DialogAllCatgFragment addcatgfragment = new DialogAllCatgFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, searchedCatgeory);
        args.putString(ARG_PARAM2, catgType);
        args.putString(ARG_PARAM3, selectedCatg);
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
            prevSelectedCatg = getArguments().getString(ARG_PARAM3);
        }
    }

    //WILL BE CALLED ONLY ON SHOW DIALOG
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        inflater = getActivity().getLayoutInflater();
        allCatgView = inflater.inflate(R.layout.all_categories, null);
        alertDialogBuilder.setView(allCatgView);

        alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }

//CALCULATOR CODES

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initAllCategoryUI();
        initAllCatgEvents();
        initAllHashmapsForCategories();
        initAllCatgeoryDBDatas();
    }

    public void initAllCategoryUI() {
        if (catgType.equalsIgnoreCase("expense")) {
            expenseLayouts = (RelativeLayout) allCatgView.findViewById(R.id.tableAllCategoryExpense);
            expenseLayouts.setVisibility(View.VISIBLE);
            edttxt_SearchAllCategoryExpense = (EditText) allCatgView.findViewById(R.id.searchAllCategoryExpense);
            errorNoCatgeory = (TextView) allCatgView.findViewById(R.id.txtErrNocatg);

            scrollview_AllExpenseCatgs = (ScrollView) allCatgView.findViewById(R.id.expenseVisibleCategoryLayout);

            catgAllHeadingTextview = (TextView) allCatgView.findViewById(R.id.txt_expenseAllCatgHead);

            //CATEGGORY INITS
            catgE1 = (TextView) allCatgView.findViewById(R.id.catgE1);
            catgE2 = (TextView) allCatgView.findViewById(R.id.catgE2);
            catgE3 = (TextView) allCatgView.findViewById(R.id.catgE3);
            catgE4 = (TextView) allCatgView.findViewById(R.id.catgE4);
            catgE5 = (TextView) allCatgView.findViewById(R.id.catgE5);
            catgE6 = (TextView) allCatgView.findViewById(R.id.catgE6);
            catgE7 = (TextView) allCatgView.findViewById(R.id.catgE7);
            catgE8 = (TextView) allCatgView.findViewById(R.id.catgE8);
            catgE9 = (TextView) allCatgView.findViewById(R.id.catgE9);
            catgE10 = (TextView) allCatgView.findViewById(R.id.catgE10);
            catgE11 = (TextView) allCatgView.findViewById(R.id.catgE11);
            catgE12 = (TextView) allCatgView.findViewById(R.id.catgE12);
            catgE13 = (TextView) allCatgView.findViewById(R.id.catgE13);
            catgE14 = (TextView) allCatgView.findViewById(R.id.catgE14);
            catgE15 = (TextView) allCatgView.findViewById(R.id.catgE15);
            catgE16 = (TextView) allCatgView.findViewById(R.id.catgE16);
            catgE17 = (TextView) allCatgView.findViewById(R.id.catgE17);
            catgE18 = (TextView) allCatgView.findViewById(R.id.catgE18);
            catgE19 = (TextView) allCatgView.findViewById(R.id.catgE19);
            catgE20 = (TextView) allCatgView.findViewById(R.id.catgE20);
            catgE21 = (TextView) allCatgView.findViewById(R.id.catgE21);
            catgE22 = (TextView) allCatgView.findViewById(R.id.catgE22);
            catgE23 = (TextView) allCatgView.findViewById(R.id.catgE23);
            catgE24 = (TextView) allCatgView.findViewById(R.id.catgE24);
            catgE25 = (TextView) allCatgView.findViewById(R.id.catgE25);
            catgE26 = (TextView) allCatgView.findViewById(R.id.catgE26);
            catgE27 = (TextView) allCatgView.findViewById(R.id.catgE27);
            catgE28 = (TextView) allCatgView.findViewById(R.id.catgE28);
            catgESave = (TextView) allCatgView.findViewById(R.id.catgESelect);
            catgEDelete = (TextView) allCatgView.findViewById(R.id.catgEDelete);
        }
        else {
            incomeLayouts = (RelativeLayout) allCatgView.findViewById(R.id.tableAllCategoryIncome);
            incomeLayouts.setVisibility(View.VISIBLE);
            edttxt_SearchAllCategoryIncome = (EditText) allCatgView.findViewById(R.id.searchAllCategoryIncome);
            errorNoCatgeoryIncome = (TextView) allCatgView.findViewById(R.id.txtErrNocatgIncome);

            scrollview_AllIncomeCatgs = (ScrollView) allCatgView.findViewById(R.id.incomeVisibleCategoryLayout);

            catgAllHeadingTextviewIncome = (TextView) allCatgView.findViewById(R.id.txt_expenseAllCatgHeadIncome);

            //CATEGGORY INITS
            catgI1 = (TextView) allCatgView.findViewById(R.id.catgI1);
            catgI2 = (TextView) allCatgView.findViewById(R.id.catgI2);
            catgI3 = (TextView) allCatgView.findViewById(R.id.catgI3);
            catgI4 = (TextView) allCatgView.findViewById(R.id.catgI4);
            catgI5 = (TextView) allCatgView.findViewById(R.id.catgI5);
            catgI6 = (TextView) allCatgView.findViewById(R.id.catgI6);
            catgI7 = (TextView) allCatgView.findViewById(R.id.catgI7);
            catgI8 = (TextView) allCatgView.findViewById(R.id.catgI8);
            catgI9 = (TextView) allCatgView.findViewById(R.id.catgI9);
            catgI10 = (TextView) allCatgView.findViewById(R.id.catgI10);
            catgI11 = (TextView) allCatgView.findViewById(R.id.catgI11);
            catgI12 = (TextView) allCatgView.findViewById(R.id.catgI12);
            catgI13 = (TextView) allCatgView.findViewById(R.id.catgI13);
            catgI14 = (TextView) allCatgView.findViewById(R.id.catgI14);
            catgI15 = (TextView) allCatgView.findViewById(R.id.catgI15);
            catgI16 = (TextView) allCatgView.findViewById(R.id.catgI16);
            catgI17 = (TextView) allCatgView.findViewById(R.id.catgI17);
            catgI18 = (TextView) allCatgView.findViewById(R.id.catgI18);
            catgI19 = (TextView) allCatgView.findViewById(R.id.catgI19);
            catgI20 = (TextView) allCatgView.findViewById(R.id.catgI20);
            catgI21 = (TextView) allCatgView.findViewById(R.id.catgI21);
            catgI22 = (TextView) allCatgView.findViewById(R.id.catgI22);
            catgI23 = (TextView) allCatgView.findViewById(R.id.catgI23);
            catgI24 = (TextView) allCatgView.findViewById(R.id.catgI24);
            catgI25 = (TextView) allCatgView.findViewById(R.id.catgI25);
            catgI26 = (TextView) allCatgView.findViewById(R.id.catgI26);
            catgI27 = (TextView) allCatgView.findViewById(R.id.catgI27);
            catgI28 = (TextView) allCatgView.findViewById(R.id.catgI28);
            catgISave = (TextView) allCatgView.findViewById(R.id.catgISelect);
            catgIDelete = (TextView) allCatgView.findViewById(R.id.catgIDelete);
        }
    }

    public void initAllCatgEvents()
    {
        //SET EVENTS CLICK ON CATEGORIES
      if (catgType.equalsIgnoreCase("expense")) {
          catgE1.setOnClickListener(this);
          catgE2.setOnClickListener(this);
          catgE3.setOnClickListener(this);
          catgE4.setOnClickListener(this);
          catgE5.setOnClickListener(this);
          catgE6.setOnClickListener(this);
          catgE7.setOnClickListener(this);
          catgE8.setOnClickListener(this);
          catgE9.setOnClickListener(this);
          catgE10.setOnClickListener(this);
          catgE11.setOnClickListener(this);
          catgE12.setOnClickListener(this);
          catgE13.setOnClickListener(this);
          catgE14.setOnClickListener(this);
          catgE15.setOnClickListener(this);
          catgE16.setOnClickListener(this);
          catgE17.setOnClickListener(this);
          catgE18.setOnClickListener(this);
          catgE19.setOnClickListener(this);
          catgE20.setOnClickListener(this);
          catgE21.setOnClickListener(this);
          catgE22.setOnClickListener(this);
          catgE23.setOnClickListener(this);
          catgE24.setOnClickListener(this);
          catgE25.setOnClickListener(this);
          catgE26.setOnClickListener(this);
          catgE27.setOnClickListener(this);
          catgE28.setOnClickListener(this);
          catgESave.setOnClickListener(this);
          catgEDelete.setOnClickListener(this);

          edttxt_SearchAllCategoryExpense.addTextChangedListener(new TextWatcher() {
              @Override
              public void beforeTextChanged(CharSequence s, int start, int count, int after) {

              }

              @Override
              public void onTextChanged(CharSequence s, int start, int before, int count) {

              }

              @Override
              public void afterTextChanged(Editable s) {
                  searchAllECategoriesAndShow(catgExpenseDbInAllDialog.getAllMatchedCategoriesName(edttxt_SearchAllCategoryExpense.getText().toString().trim()), catgExpenseDbInAllDialog.getAllMatchedCategoriesImageName(edttxt_SearchAllCategoryExpense.getText().toString().trim()));
              }
          });
      }
      else{
          catgI1.setOnClickListener(this);
          catgI2.setOnClickListener(this);
          catgI3.setOnClickListener(this);
          catgI4.setOnClickListener(this);
          catgI5.setOnClickListener(this);
          catgI6.setOnClickListener(this);
          catgI7.setOnClickListener(this);
          catgI8.setOnClickListener(this);
          catgI9.setOnClickListener(this);
          catgI10.setOnClickListener(this);
          catgI11.setOnClickListener(this);
          catgI12.setOnClickListener(this);
          catgI13.setOnClickListener(this);
          catgI14.setOnClickListener(this);
          catgI15.setOnClickListener(this);
          catgI16.setOnClickListener(this);
          catgI17.setOnClickListener(this);
          catgI18.setOnClickListener(this);
          catgI19.setOnClickListener(this);
          catgI20.setOnClickListener(this);
          catgI21.setOnClickListener(this);
          catgI22.setOnClickListener(this);
          catgI23.setOnClickListener(this);
          catgI24.setOnClickListener(this);
          catgI25.setOnClickListener(this);
          catgI26.setOnClickListener(this);
          catgI27.setOnClickListener(this);
          catgI28.setOnClickListener(this);
          catgISave.setOnClickListener(this);
          catgIDelete.setOnClickListener(this);

          edttxt_SearchAllCategoryIncome.addTextChangedListener(new TextWatcher() {
              @Override
              public void beforeTextChanged(CharSequence s, int start, int count, int after) {

              }

              @Override
              public void onTextChanged(CharSequence s, int start, int before, int count) {

              }

              @Override
              public void afterTextChanged(Editable s) {
                  searchAllICategoriesAndShow(catgIncomeDbInAllDialog.getAllMatchedCategoriesName(edttxt_SearchAllCategoryIncome.getText().toString()), catgIncomeDbInAllDialog.getAllMatchedCategoriesImageName(edttxt_SearchAllCategoryIncome.getText().toString()));
              }
          });
      }
    }

    public void initAllCatgeoryDBDatas()
    {
        //STORING THE CATGEORIES IN DATABASE
         if(catgType.equalsIgnoreCase("expense")) {
           catgExpenseDbInAllDialog = new CategoryDBAdapter(getActivity());
           catgExpenseDbInAllDialog = catgExpenseDbInAllDialog.open();
           myAllCategoriesENameList = catgExpenseDbInAllDialog.getAllCategoriesName();
           myAllCategoriesEImgList = catgExpenseDbInAllDialog.getAllCategoriesImageName();
           selectedExpenseCatg = prevSelectedCatg;
           int catgeoryNameSize = (myAllCategoriesENameList.size());
           for (int x = 1; x <= catgeoryNameSize; x++) {
               if (x > 28) {
                   break; //CHECKING THE EXCEEDING LIMIT OF AVAILABLE CATEGORIES in UI
               }
               catgAllECommonTextview = (TextView) allCatgView.findViewById(catgAllETextViewsHashmap.get(x));
               if (myAllCategoriesENameList.get(x - 1).equalsIgnoreCase(prevSelectedCatg)) {
                   catgAllECommonTextview.setBackground(getResources().getDrawable(R.drawable.category_selected_border_gradient));
                   ss = new SpannableString("Choosen expense catgeory - " + selectedExpenseCatg);
                   ss.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 27, ss.length(), 0);
                   catgAllHeadingTextview.setText(ss);
               } else {
                   catgAllECommonTextview.setBackground(getResources().getDrawable(R.drawable.category_gradient_border));
               }
               //MAINTAININGN THE CONSTANT LENGHT OF THE CATGS
               if (myAllCategoriesENameList.get(x - 1).toString().length() > 5) {
                   myAllCategoriesENameList.set(x - 1, myAllCategoriesENameList.get(x - 1).substring(0, 4) + "..");
               } else {
                   myAllCategoriesENameList.set(x - 1, myAllCategoriesENameList.get(x - 1));
               }
               catgAllECommonTextview.setText(myAllCategoriesENameList.get(x - 1));
               catgAllECommonTextview.setCompoundDrawablesWithIntrinsicBounds(0, (catgAllDrawableHashmap.get(myAllCategoriesEImgList.get(x - 1))), 0, 0);
               if (myAllCategoriesEImgList.get(x - 1).toString().equalsIgnoreCase("custom")) {
                   catgAllECommonTextview.setTag("custom");
               } else {
                   catgAllECommonTextview.setTag("defaults");
               }
               catgAllECommonTextview.setVisibility(View.VISIBLE);
           }
       }
       else{
             catgIncomeDbInAllDialog = new CategoryIncomeDBAdapter(getActivity());
             catgIncomeDbInAllDialog = catgIncomeDbInAllDialog.open();
             myAllCategoriesINameList = catgIncomeDbInAllDialog.getAllCategoriesName();
             myAllCategoriesIImgList = catgIncomeDbInAllDialog.getAllCategoriesImageName();
             selectedIncomeCatg = prevSelectedCatg;
             int catgeoryNameSize = (myAllCategoriesINameList.size());
             for (int x = 1; x <= catgeoryNameSize; x++) {
                 if (x > 28) {
                     break; //CHECKING THE EXCEEDING LIMIT OF AVAILABLE CATEGORIES in UI
                 }
                 catgAllICommonTextview = (TextView) allCatgView.findViewById(catgAllITextViewsHashmap.get(x));
                 if (myAllCategoriesINameList.get(x - 1).equalsIgnoreCase(prevSelectedCatg)) {
                     catgAllICommonTextview.setBackground(getResources().getDrawable(R.drawable.category_selected_border_gradient));
                     ss = new SpannableString("Choosen income catgeory - " + selectedIncomeCatg);
                     ss.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 26, ss.length(), 0);
                     catgAllHeadingTextviewIncome.setText(ss);
                 } else {
                     catgAllICommonTextview.setBackground(getResources().getDrawable(R.drawable.category_gradient_border));
                 }
                 //MAINTAININGN THE CONSTANT LENGHT OF THE CATGS
                 if (myAllCategoriesINameList.get(x - 1).toString().length() > 5) {
                     myAllCategoriesINameList.set(x - 1, myAllCategoriesINameList.get(x - 1).substring(0, 4) + "..");
                 } else {
                     myAllCategoriesINameList.set(x - 1, myAllCategoriesINameList.get(x - 1));
                 }
                 catgAllICommonTextview.setText(myAllCategoriesINameList.get(x - 1));
                 catgAllICommonTextview.setCompoundDrawablesWithIntrinsicBounds(0, (catgAllDrawableHashmap.get(myAllCategoriesIImgList.get(x - 1))), 0, 0);
                 if (myAllCategoriesIImgList.get(x - 1).toString().equalsIgnoreCase("custom")) {
                     catgAllICommonTextview.setTag("custom");
                 } else {
                     catgAllICommonTextview.setTag("defaults");
                 }
                 catgAllICommonTextview.setVisibility(View.VISIBLE);
             }
         }
    }

    private void refreshAllExpenseCatg()
    {
        myAllCategoriesENameList = catgExpenseDbInAllDialog.getAllCategoriesName();
        myAllCategoriesEImgList = catgExpenseDbInAllDialog.getAllCategoriesImageName();
        int catgeoryNameSize = (myAllCategoriesENameList.size());
        for(int x=1;x<=catgeoryNameSize;x++) {
            if(x>28) {
                break; //CHECKING THE EXCEEDING LIMIT OF AVAILABLE CATEGORIES in UI
            }
            catgAllECommonTextview = (TextView) allCatgView.findViewById(catgAllETextViewsHashmap.get(x));
            if(myAllCategoriesENameList.get(x-1).equalsIgnoreCase(selectedExpenseCatg)) {
                catgAllECommonTextview.setBackground(getResources().getDrawable(R.drawable.category_selected_border_gradient));
                ss = new SpannableString("Choosen expense catgeory - "+selectedExpenseCatg);
                ss.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 27, ss.length(), 0);
                catgAllHeadingTextview.setText(ss);
            }
            else {
                catgAllECommonTextview.setBackground(getResources().getDrawable(R.drawable.category_gradient_border));
            }
            //MAINTAININGN THE CONSTANT LENGHT OF THE CATGS
            if(myAllCategoriesENameList.get(x-1).toString().length()>5){
                myAllCategoriesENameList.set(x-1,myAllCategoriesENameList.get(x-1).substring(0,4)+"..");
            }
            else{
                myAllCategoriesENameList.set(x-1,myAllCategoriesENameList.get(x-1));
            }
            catgAllECommonTextview.setText(myAllCategoriesENameList.get(x-1));
            catgAllECommonTextview.setCompoundDrawablesWithIntrinsicBounds(0,(catgAllDrawableHashmap.get(myAllCategoriesEImgList.get(x-1))),0,0);
            if(catgAllECommonTextview.getVisibility()==View.INVISIBLE){
                break;
            }
        }
    }

    private void initAllHashmapsForCategories()
    {
        //SETTING HASHMAP FOR CATEGORIES DRAWABLE FOR DYNAMICALLY GETTING THE DRAWABLES ACCORDING TO THE DATABASE DATAS
        catgAllDrawableHashmap.put("food",R.drawable.catg_food);
        catgAllDrawableHashmap.put("transport",R.drawable.catg_transport);
        catgAllDrawableHashmap.put("bill",R.drawable.catg_bill);
        catgAllDrawableHashmap.put("carandbike",R.drawable.catg_carandbike);
        catgAllDrawableHashmap.put("clothes",R.drawable.catg_clothes);
        catgAllDrawableHashmap.put("eatout",R.drawable.catg_eatout);
        catgAllDrawableHashmap.put("custom",R.drawable.catg_custom);
        catgAllDrawableHashmap.put("phone",R.drawable.catg_phone);
        catgAllDrawableHashmap.put("entertainment",R.drawable.catg_entertainment);
        catgAllDrawableHashmap.put("rental",R.drawable.catg_rental);
        catgAllDrawableHashmap.put("gift",R.drawable.catg_gift);
        catgAllDrawableHashmap.put("health",R.drawable.catg_health);
        catgAllDrawableHashmap.put("pet",R.drawable.catg_pet);
        catgAllDrawableHashmap.put("electronics",R.drawable.catg_electronics);
        catgAllDrawableHashmap.put("sports",R.drawable.catg_sports);
        catgAllDrawableHashmap.put("saving",R.drawable.catg_saving);
        catgAllDrawableHashmap.put("tax",R.drawable.catg_tax);
        catgAllDrawableHashmap.put("salary",R.drawable.catg_salary);
        catgAllDrawableHashmap.put("homerent",R.drawable.catg_homerent);

        //SETTING HASHMAP FOR CATEGORIES DRAWABLE FOR DYNAMICALLY GETTING THE DRAWABLES ACCORDING TO THE DATABASE DATAS
        if(catgType.equalsIgnoreCase("expense")) {
            catgAllETextViewsHashmap.put(1, R.id.catgE1);
            catgAllETextViewsHashmap.put(2, R.id.catgE2);
            catgAllETextViewsHashmap.put(3, R.id.catgE3);
            catgAllETextViewsHashmap.put(4, R.id.catgE4);
            catgAllETextViewsHashmap.put(5, R.id.catgE5);
            catgAllETextViewsHashmap.put(6, R.id.catgE6);
            catgAllETextViewsHashmap.put(7, R.id.catgE7);
            catgAllETextViewsHashmap.put(8, R.id.catgE8);
            catgAllETextViewsHashmap.put(9, R.id.catgE9);
            catgAllETextViewsHashmap.put(10, R.id.catgE10);
            catgAllETextViewsHashmap.put(11, R.id.catgE11);
            catgAllETextViewsHashmap.put(12, R.id.catgE12);
            catgAllETextViewsHashmap.put(13, R.id.catgE13);
            catgAllETextViewsHashmap.put(14, R.id.catgE14);
            catgAllETextViewsHashmap.put(15, R.id.catgE15);
            catgAllETextViewsHashmap.put(16, R.id.catgE16);
            catgAllETextViewsHashmap.put(17, R.id.catgE17);
            catgAllETextViewsHashmap.put(18, R.id.catgE18);
            catgAllETextViewsHashmap.put(19, R.id.catgE19);
            catgAllETextViewsHashmap.put(20, R.id.catgE20);
            catgAllETextViewsHashmap.put(21, R.id.catgE21);
            catgAllETextViewsHashmap.put(22, R.id.catgE22);
            catgAllETextViewsHashmap.put(23, R.id.catgE23);
            catgAllETextViewsHashmap.put(24, R.id.catgE24);
            catgAllETextViewsHashmap.put(25, R.id.catgE25);
            catgAllETextViewsHashmap.put(26, R.id.catgE26);
            catgAllETextViewsHashmap.put(27, R.id.catgE27);
            catgAllETextViewsHashmap.put(28, R.id.catgE28);

            //CREATING HASHMAP FOR MAPPING THE SHORT NAMES WITH THE FULL NAMES
            catgExpenseDbInAllDialog = new CategoryDBAdapter(getActivity());
            catgExpenseDbInAllDialog = catgExpenseDbInAllDialog.open();
            myTempAllCatgNameLsit = catgExpenseDbInAllDialog.getAllCategoriesName();
            int tempCatgSize = myTempAllCatgNameLsit.size();
            for (int x = 0; x < tempCatgSize; x++) {
                if (myTempAllCatgNameLsit.get(x).toString().length() > 5) {
                    catgAllEFullNamesHashmap.put(myTempAllCatgNameLsit.get(x).substring(0, 4) + "..", myTempAllCatgNameLsit.get(x));
                } else {
                    catgAllEFullNamesHashmap.put(myTempAllCatgNameLsit.get(x), myTempAllCatgNameLsit.get(x));
                }
            }
        }
        else{
            catgAllITextViewsHashmap.put(1, R.id.catgI1);
            catgAllITextViewsHashmap.put(2, R.id.catgI2);
            catgAllITextViewsHashmap.put(3, R.id.catgI3);
            catgAllITextViewsHashmap.put(4, R.id.catgI4);
            catgAllITextViewsHashmap.put(5, R.id.catgI5);
            catgAllITextViewsHashmap.put(6, R.id.catgI6);
            catgAllITextViewsHashmap.put(7, R.id.catgI7);
            catgAllITextViewsHashmap.put(8, R.id.catgI8);
            catgAllITextViewsHashmap.put(9, R.id.catgI9);
            catgAllITextViewsHashmap.put(10, R.id.catgI10);
            catgAllITextViewsHashmap.put(11, R.id.catgI11);
            catgAllITextViewsHashmap.put(12, R.id.catgI12);
            catgAllITextViewsHashmap.put(13, R.id.catgI13);
            catgAllITextViewsHashmap.put(14, R.id.catgI14);
            catgAllITextViewsHashmap.put(15, R.id.catgI15);
            catgAllITextViewsHashmap.put(16, R.id.catgI16);
            catgAllITextViewsHashmap.put(17, R.id.catgI17);
            catgAllITextViewsHashmap.put(18, R.id.catgI18);
            catgAllITextViewsHashmap.put(19, R.id.catgI19);
            catgAllITextViewsHashmap.put(20, R.id.catgI20);
            catgAllITextViewsHashmap.put(21, R.id.catgI21);
            catgAllITextViewsHashmap.put(22, R.id.catgI22);
            catgAllITextViewsHashmap.put(23, R.id.catgI23);
            catgAllITextViewsHashmap.put(24, R.id.catgI24);
            catgAllITextViewsHashmap.put(25, R.id.catgI25);
            catgAllITextViewsHashmap.put(26, R.id.catgI26);
            catgAllITextViewsHashmap.put(27, R.id.catgI27);
            catgAllITextViewsHashmap.put(28, R.id.catgI28);

            //CREATING HASHMAP FOR MAPPING THE SHORT NAMES WITH THE FULL NAMES
            catgIncomeDbInAllDialog = new CategoryIncomeDBAdapter(getActivity());
            catgIncomeDbInAllDialog = catgIncomeDbInAllDialog.open();
            myTempAllCatgNameLsit = catgIncomeDbInAllDialog.getAllCategoriesName();
            int tempCatgSize = myTempAllCatgNameLsit.size();
            for (int x = 0; x < tempCatgSize; x++) {
                if (myTempAllCatgNameLsit.get(x).toString().length() > 5) {
                    catgAllIFullNamesHashmap.put(myTempAllCatgNameLsit.get(x).substring(0, 4) + "..", myTempAllCatgNameLsit.get(x));
                } else {
                    catgAllIFullNamesHashmap.put(myTempAllCatgNameLsit.get(x), myTempAllCatgNameLsit.get(x));
                }
            }
        }
    }

    public void searchAllECategoriesAndShow(ArrayList<String> name, ArrayList<String> imagename)
    {
        myAllCategoriesENameList = name;
        myAllCategoriesEImgList = imagename;
        isDeleteActive = false;
        catgEDelete.setBackground(getResources().getDrawable(R.drawable.category_gradient_border_primary));
        if(myAllCategoriesENameList.size()!=0) {
           // scrollViewAddAcct.smoothScrollTo(0,catgEAdd.getBottom());
            int allCatgNameSize = catgExpenseDbInAllDialog.getAllCategoriesName().size();
            for(int x=1;x<=(allCatgNameSize+1);x++) {
                catgAllECommonTextview = (TextView) allCatgView.findViewById(catgAllETextViewsHashmap.get(x));

                if((x)>myAllCategoriesEImgList.size()) {
                    catgAllECommonTextview.setVisibility(View.INVISIBLE);
                    continue;
                }
                catgAllECommonTextview.setTextColor(getResources().getColor(R.color.Black));
                //HIGHLIGHTING THE SELECTED CATGEORY ON SEARCH TOO
                if(myAllCategoriesENameList.get(x-1).equalsIgnoreCase(selectedExpenseCatg)){
                    catgAllECommonTextview.setBackground(getResources().getDrawable(R.drawable.category_selected_border_gradient));
                }
                else {
                    catgAllECommonTextview.setBackground(getResources().getDrawable(R.drawable.category_gradient_border));
                }
                //MAINTAININGN THE CONSTANT LENGHT OF THE CATGS
                if(myAllCategoriesENameList.get(x-1).toString().length()>5){
                    myAllCategoriesENameList.set(x-1,myAllCategoriesENameList.get(x-1).substring(0,4)+"..");
                }
                else{
                    myAllCategoriesENameList.set(x-1,myAllCategoriesENameList.get(x-1));
                }
                catgAllECommonTextview.setText(myAllCategoriesENameList.get(x-1));
                catgAllECommonTextview.setCompoundDrawablesWithIntrinsicBounds(0,(catgAllDrawableHashmap.get(myAllCategoriesEImgList.get(x-1))),0,0);
                if(myAllCategoriesEImgList.get(x-1).toString().equalsIgnoreCase("custom")){
                    catgAllECommonTextview.setTag("custom");
                }
                else{
                    catgAllECommonTextview.setTag("defaults");
                }
                if(catgAllECommonTextview.getVisibility() == View.INVISIBLE) {
                    catgAllECommonTextview.setVisibility(View.VISIBLE);
                }
            }
            scrollview_AllExpenseCatgs.setVisibility(View.VISIBLE);
            errorNoCatgeory.setVisibility(View.INVISIBLE);
        }
        else {
            //scrollViewAddAcct.smoothScrollTo(0,expenseCatgLayout.getBottom());
            scrollview_AllExpenseCatgs.setVisibility(View.INVISIBLE);
            errorNoCatgeory.setVisibility(View.VISIBLE);
           //MMUtils.startAnimate(catgEAdd, R.anim.mutiplezoominout, getActivity());
        }
    }

    public void searchAllICategoriesAndShow(ArrayList<String> name, ArrayList<String> imagename)
    {
        myAllCategoriesINameList = name;
        myAllCategoriesIImgList = imagename;
        isDeleteActive = false;
        catgIDelete.setBackground(getResources().getDrawable(R.drawable.category_gradient_border_primary));
        if(myAllCategoriesINameList.size()!=0) {
            // scrollViewAddAcct.smoothScrollTo(0,catgEAdd.getBottom());
            int allCatgNameSize = catgIncomeDbInAllDialog.getAllCategoriesName().size();
             for(int x=1;x<=(allCatgNameSize+1);x++) {
                catgAllICommonTextview = (TextView) allCatgView.findViewById(catgAllITextViewsHashmap.get(x));

                if((x)>myAllCategoriesIImgList.size()) {
                    catgAllICommonTextview.setVisibility(View.INVISIBLE);
                    continue;
                }
                catgAllICommonTextview.setTextColor(getResources().getColor(R.color.Black));
                //HIGHLIGHTING THE SELECTED CATGEORY ON SEARCH TOO
                if(myAllCategoriesINameList.get(x-1).equalsIgnoreCase(selectedIncomeCatg)){
                    catgAllICommonTextview.setBackground(getResources().getDrawable(R.drawable.category_selected_border_gradient));
                }
                else {
                    catgAllICommonTextview.setBackground(getResources().getDrawable(R.drawable.category_gradient_border));
                }
                //MAINTAININGN THE CONSTANT LENGHT OF THE CATGS
                if(myAllCategoriesINameList.get(x-1).toString().length()>5){
                    myAllCategoriesINameList.set(x-1,myAllCategoriesINameList.get(x-1).substring(0,4)+"..");
                }
                else{
                    myAllCategoriesINameList.set(x-1,myAllCategoriesINameList.get(x-1));
                }
                catgAllICommonTextview.setText(myAllCategoriesINameList.get(x-1));
                catgAllICommonTextview.setCompoundDrawablesWithIntrinsicBounds(0,(catgAllDrawableHashmap.get(myAllCategoriesIImgList.get(x-1))),0,0);
                if(myAllCategoriesIImgList.get(x-1).toString().equalsIgnoreCase("custom")){
                    catgAllICommonTextview.setTag("custom");
                }
                else{
                    catgAllICommonTextview.setTag("defaults");
                }
                if(catgAllICommonTextview.getVisibility() == View.INVISIBLE) {
                    catgAllICommonTextview.setVisibility(View.VISIBLE);
                }
            }
            scrollview_AllIncomeCatgs.setVisibility(View.VISIBLE);
            errorNoCatgeoryIncome.setVisibility(View.INVISIBLE);
        }
        else {
            //scrollViewAddAcct.smoothScrollTo(0,expenseCatgLayout.getBottom());
            scrollview_AllIncomeCatgs.setVisibility(View.INVISIBLE);
            errorNoCatgeoryIncome.setVisibility(View.VISIBLE);
            //MMUtils.startAnimate(catgEAdd, R.anim.mutiplezoominout, getActivity());
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if(isSaveClicked){
            //CALLING THE ABSTRACT METHOD OF THE INTERFACE
            if(catgType.equalsIgnoreCase("expense")) {
                dialogAllCatgCloseListener.onAllCatgDialogClosed(selectedExpenseCatg, catgType);
            }
            else{
                dialogAllCatgCloseListener.onAllCatgDialogClosed(selectedIncomeCatg, catgType);
            }
            isSaveClicked = false;
        }
        else{ //SENDING BLANK SELECTED CATEGORY JUST FOR THE REFRESH
            dialogAllCatgCloseListener.onAllCatgDialogClosed("",catgType);
        }
    }

    public void categoryEAllCommonClickEventMethod(int index) {
        MMUtils.hideKeyboard(getActivity(),edttxt_SearchAllCategoryExpense);
        catgAllECommonTextview = (TextView) allCatgView.findViewById(catgAllETextViewsHashmap.get(index));

        //FOR NORMAL CLICK EVENTS
        if(isDeleteActive == false) {
            myAllCategoriesENameList = catgExpenseDbInAllDialog.getAllCategoriesName();
            selectedExpenseCatg = catgAllEFullNamesHashmap.get(catgAllECommonTextview.getText().toString());
            ss = new SpannableString("Choosen expense catgeory - " + selectedExpenseCatg);
            ss.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 27, ss.length(), 0);
            catgAllHeadingTextview.setText(ss);
            catgAllHeadingTextview.setShadowLayer(10, 5, 5, getResources().getColor(R.color.DarkSeaGreen));
            MMUtils.startAnimate(catgAllHeadingTextview, R.anim.blink, allCatgView.getContext());
            int allCatgnamesSize = (myAllCategoriesENameList.size());
            for (int x = 1; x <= allCatgnamesSize; x++) {
                catgAllECommonTextview = (TextView) allCatgView.findViewById(catgAllETextViewsHashmap.get(x));
                if (x == index) {
                    MMUtils.startAnimate(catgAllECommonTextview, R.anim.blink, allCatgView.getContext());
                    catgAllECommonTextview.setBackground(getResources().getDrawable(R.drawable.category_selected_border_gradient));
                } else {
                    catgAllECommonTextview.setBackground(getResources().getDrawable(R.drawable.category_gradient_border));
                }
            }
        }
        else{//FOR DELETE CLICK EVNT
            if((!(catgAllEFullNamesHashmap.get(catgAllECommonTextview.getText().toString()).equalsIgnoreCase(selectedExpenseCatg))) && catgAllECommonTextview.getTag().toString().equalsIgnoreCase("custom")){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setMessage("Are you sure to delete your custom category - "+catgAllEFullNamesHashmap.get(catgAllECommonTextview.getText().toString())+" ?");
                        alertDialogBuilder.setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        catgExpenseDbInAllDialog.deleteCustomCategory(catgAllEFullNamesHashmap.get(catgAllECommonTextview.getText().toString()));
                                        catgAllECommonTextview.setTextColor(getResources().getColor(R.color.Black));
                                        searchAllECategoriesAndShow(catgExpenseDbInAllDialog.getAllMatchedCategoriesName(edttxt_SearchAllCategoryExpense.getText().toString()),catgExpenseDbInAllDialog.getAllMatchedCategoriesImageName(edttxt_SearchAllCategoryExpense.getText().toString()));
                                    }
                                });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
            else {
                Toast.makeText(getActivity(), "Please select highlighted category to delete!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void categoryIAllCommonClickEventMethod(int index) {
        MMUtils.hideKeyboard(getActivity(),edttxt_SearchAllCategoryIncome);
        catgAllICommonTextview = (TextView) allCatgView.findViewById(catgAllITextViewsHashmap.get(index));

        //FOR NORMAL CLICK EVENTS
        if(isDeleteActive == false) {
            myAllCategoriesINameList = catgIncomeDbInAllDialog.getAllCategoriesName();
            selectedIncomeCatg = catgAllIFullNamesHashmap.get(catgAllICommonTextview.getText().toString());
            ss = new SpannableString("Choosen income catgeory - " + selectedIncomeCatg);
            ss.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 26, ss.length(), 0);
            catgAllHeadingTextviewIncome.setText(ss);
            catgAllHeadingTextviewIncome.setShadowLayer(10, 5, 5, getResources().getColor(R.color.DarkSeaGreen));
            MMUtils.startAnimate(catgAllHeadingTextviewIncome, R.anim.blink, allCatgView.getContext());
            int allCatgnamesSize = (myAllCategoriesINameList.size());
            for (int x = 1; x <= allCatgnamesSize; x++) {
                catgAllICommonTextview = (TextView) allCatgView.findViewById(catgAllITextViewsHashmap.get(x));
                if (x == index) {
                    MMUtils.startAnimate(catgAllICommonTextview, R.anim.blink, allCatgView.getContext());
                    catgAllICommonTextview.setBackground(getResources().getDrawable(R.drawable.category_selected_border_gradient));
                } else {
                    catgAllICommonTextview.setBackground(getResources().getDrawable(R.drawable.category_gradient_border));
                }
            }
        }
        else{//FOR DELETE CLICK EVNT
            if((!(catgAllIFullNamesHashmap.get(catgAllICommonTextview.getText().toString()).equalsIgnoreCase(selectedIncomeCatg))) && catgAllICommonTextview.getTag().toString().equalsIgnoreCase("custom")){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setMessage("Are you sure to delete your custom category - "+catgAllIFullNamesHashmap.get(catgAllICommonTextview.getText().toString())+" ?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                catgIncomeDbInAllDialog.deleteCustomCategory(catgAllIFullNamesHashmap.get(catgAllICommonTextview.getText().toString()));
                                catgAllICommonTextview.setTextColor(getResources().getColor(R.color.Black));
                                searchAllICategoriesAndShow(catgIncomeDbInAllDialog.getAllMatchedCategoriesName(edttxt_SearchAllCategoryIncome.getText().toString()),catgIncomeDbInAllDialog.getAllMatchedCategoriesImageName(edttxt_SearchAllCategoryIncome.getText().toString()));
                            }
                        });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
            else {
                Toast.makeText(getActivity(), "Please select highlighted category to delete!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.catgE1:
                categoryEAllCommonClickEventMethod(1);
                break;

            case R.id.catgE2:
                categoryEAllCommonClickEventMethod(2);
                break;

            case R.id.catgE3:
                categoryEAllCommonClickEventMethod(3);
                break;

            case R.id.catgE4:
                categoryEAllCommonClickEventMethod(4);
                break;

            case R.id.catgE5:
                categoryEAllCommonClickEventMethod(5);
                break;

            case R.id.catgE6:
                categoryEAllCommonClickEventMethod(6);
                break;

            case R.id.catgE7:
                categoryEAllCommonClickEventMethod(7);
                break;

            case R.id.catgE8:
                categoryEAllCommonClickEventMethod(8);
                break;

            case R.id.catgE9:
                categoryEAllCommonClickEventMethod(9);
                break;

            case R.id.catgE10:
                categoryEAllCommonClickEventMethod(10);
                break;

            case R.id.catgE11:
                categoryEAllCommonClickEventMethod(11);
                break;

            case R.id.catgE12:
                categoryEAllCommonClickEventMethod(12);
                break;

            case R.id.catgE13:
                categoryEAllCommonClickEventMethod(13);
                break;

            case R.id.catgE14:
                categoryEAllCommonClickEventMethod(14);
                break;

            case R.id.catgE15:
                categoryEAllCommonClickEventMethod(15);
                break;

            case R.id.catgE16:
                categoryEAllCommonClickEventMethod(16);
                break;

            case R.id.catgE17:
                categoryEAllCommonClickEventMethod(17);
                break;

            case R.id.catgE18:
                categoryEAllCommonClickEventMethod(18);
                break;

            case R.id.catgE19:
                categoryEAllCommonClickEventMethod(19);
                break;

            case R.id.catgE20:
                categoryEAllCommonClickEventMethod(20);
                break;

            case R.id.catgE21:
                categoryEAllCommonClickEventMethod(21);
                break;

            case R.id.catgE22:
                categoryEAllCommonClickEventMethod(22);
                break;

            case R.id.catgE23:
                categoryEAllCommonClickEventMethod(23);
                break;

            case R.id.catgE24:
                categoryEAllCommonClickEventMethod(24);
                break;

            case R.id.catgE25:
                categoryEAllCommonClickEventMethod(25);
                break;

            case R.id.catgE26:
                categoryEAllCommonClickEventMethod(26);
                break;

            case R.id.catgE27:
                categoryEAllCommonClickEventMethod(27);
                break;

            case R.id.catgE28:
                categoryEAllCommonClickEventMethod(28);
                break;


            case R.id.catgESelect:
                   isSaveClicked = true;
                   alertDialog.dismiss();
                break;

            case R.id.catgEDelete:
                if(isDeleteActive == false){
                    int customCatgCount = 0;
                    int customSelectedCatgCount = 0;
                    for(int x=1;x<=28;x++) {
                        catgAllECommonTextview = (TextView) allCatgView.findViewById(catgAllETextViewsHashmap.get(x));
                        if(catgAllECommonTextview.getVisibility()==View.VISIBLE){
                             if(catgAllECommonTextview.getTag().toString().equalsIgnoreCase("custom")){
                                 customCatgCount++;
                                 if((!(catgAllEFullNamesHashmap.get(catgAllECommonTextview.getText().toString()).equalsIgnoreCase(selectedExpenseCatg)))) {
                                     catgAllECommonTextview.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.catg_delete, 0, 0);
                                     catgAllECommonTextview.setBackground(getResources().getDrawable(R.drawable.category_gradient_red_border));
                                     catgAllECommonTextview.setTextColor(getResources().getColor(R.color.White));
                                 }
                                 else{
                                     customSelectedCatgCount = 1;
                                 }
                             }

                        }
                        else{
                            break;
                        }
                    }

                    if(customCatgCount==0){
                        Toast.makeText(getActivity(), "No custom category found to delete!", Toast.LENGTH_SHORT).show();
                    }
                    else if((customCatgCount-customSelectedCatgCount)>0) {
                        isDeleteActive = true;
                        catgEDelete.setBackground(getResources().getDrawable(R.drawable.category_gradient_red_border));
                    }
                    else {
                        Toast.makeText(getActivity(), "Please unselect your custom catgeory to delete!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    isDeleteActive = false;
                    catgEDelete.setBackground(getResources().getDrawable(R.drawable.category_gradient_border_primary));
                    searchAllECategoriesAndShow(catgExpenseDbInAllDialog.getAllMatchedCategoriesName(edttxt_SearchAllCategoryExpense.getText().toString()),catgExpenseDbInAllDialog.getAllMatchedCategoriesImageName(edttxt_SearchAllCategoryExpense.getText().toString()));
                }
                break;

            case R.id.catgI1:
                categoryIAllCommonClickEventMethod(1);
                break;

            case R.id.catgI2:
                categoryIAllCommonClickEventMethod(2);
                break;

            case R.id.catgI3:
                categoryIAllCommonClickEventMethod(3);
                break;

            case R.id.catgI4:
                categoryIAllCommonClickEventMethod(4);
                break;

            case R.id.catgI5:
                categoryIAllCommonClickEventMethod(5);
                break;

            case R.id.catgI6:
                categoryIAllCommonClickEventMethod(6);
                break;

            case R.id.catgI7:
                categoryIAllCommonClickEventMethod(7);
                break;

            case R.id.catgI8:
                categoryIAllCommonClickEventMethod(8);
                break;

            case R.id.catgI9:
                categoryIAllCommonClickEventMethod(9);
                break;

            case R.id.catgI10:
                categoryIAllCommonClickEventMethod(10);
                break;

            case R.id.catgI11:
                categoryIAllCommonClickEventMethod(11);
                break;

            case R.id.catgI12:
                categoryIAllCommonClickEventMethod(12);
                break;

            case R.id.catgI13:
                categoryIAllCommonClickEventMethod(13);
                break;

            case R.id.catgI14:
                categoryIAllCommonClickEventMethod(14);
                break;

            case R.id.catgI15:
                categoryIAllCommonClickEventMethod(15);
                break;

            case R.id.catgI16:
                categoryIAllCommonClickEventMethod(16);
                break;

            case R.id.catgI17:
                categoryIAllCommonClickEventMethod(17);
                break;

            case R.id.catgI18:
                categoryIAllCommonClickEventMethod(18);
                break;

            case R.id.catgI19:
                categoryIAllCommonClickEventMethod(19);
                break;

            case R.id.catgI20:
                categoryIAllCommonClickEventMethod(20);
                break;

            case R.id.catgI21:
                categoryIAllCommonClickEventMethod(21);
                break;

            case R.id.catgI22:
                categoryIAllCommonClickEventMethod(22);
                break;

            case R.id.catgI23:
                categoryIAllCommonClickEventMethod(23);
                break;

            case R.id.catgI24:
                categoryIAllCommonClickEventMethod(24);
                break;

            case R.id.catgI25:
                categoryIAllCommonClickEventMethod(25);
                break;

            case R.id.catgI26:
                categoryIAllCommonClickEventMethod(26);
                break;

            case R.id.catgI27:
                categoryIAllCommonClickEventMethod(27);
                break;

            case R.id.catgI28:
                categoryIAllCommonClickEventMethod(28);
                break;


            case R.id.catgISelect:
                isSaveClicked = true;
                alertDialog.dismiss();
                break;

            case R.id.catgIDelete:
                if(isDeleteActive == false){
                    int customCatgCount = 0;
                    int customSelectedCatgCount = 0;
                    for(int x=1;x<=28;x++) {
                        catgAllICommonTextview = (TextView) allCatgView.findViewById(catgAllITextViewsHashmap.get(x));
                        if(catgAllICommonTextview.getVisibility()==View.VISIBLE){
                            if(catgAllICommonTextview.getTag().toString().equalsIgnoreCase("custom")){
                                customCatgCount++;
                                if((!(catgAllIFullNamesHashmap.get(catgAllICommonTextview.getText().toString()).equalsIgnoreCase(selectedIncomeCatg)))) {
                                    catgAllICommonTextview.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.catg_delete, 0, 0);
                                    catgAllICommonTextview.setBackground(getResources().getDrawable(R.drawable.category_gradient_red_border));
                                    catgAllICommonTextview.setTextColor(getResources().getColor(R.color.White));
                                }
                                else{
                                    customSelectedCatgCount = 1;
                                }
                            }

                        }
                        else{
                            break;
                        }
                    }

                    if(customCatgCount==0){
                        Toast.makeText(getActivity(), "No custom category found to delete!", Toast.LENGTH_SHORT).show();
                    }
                    else if((customCatgCount-customSelectedCatgCount)>0) {
                        isDeleteActive = true;
                        catgIDelete.setBackground(getResources().getDrawable(R.drawable.category_gradient_red_border));
                    }
                    else {
                        Toast.makeText(getActivity(), "Please unselect your custom catgeory to delete!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    isDeleteActive = false;
                    catgIDelete.setBackground(getResources().getDrawable(R.drawable.category_gradient_border_primary));
                    searchAllICategoriesAndShow(catgIncomeDbInAllDialog.getAllMatchedCategoriesName(edttxt_SearchAllCategoryIncome.getText().toString()),catgIncomeDbInAllDialog.getAllMatchedCategoriesImageName(edttxt_SearchAllCategoryIncome.getText().toString()));
                }
                break;

            default:
                break;
        }
    }
}

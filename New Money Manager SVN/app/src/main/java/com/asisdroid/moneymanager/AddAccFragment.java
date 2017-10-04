package com.asisdroid.moneymanager;

import android.app.DatePickerDialog;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asisdroid.moneymanager.Dialog.DialogAddCatgFragment;
import com.asisdroid.moneymanager.Dialog.DialogAllCatgFragment;
import com.asisdroid.moneymanager.Dialog.DialogCalculatorFragment;
import com.asisdroid.moneymanager.Dialog.DialogDatepickerFragment;
import com.asisdroid.moneymanager.database.CategoryDBAdapter;
import com.asisdroid.moneymanager.database.CategoryIncomeDBAdapter;
import com.asisdroid.moneymanager.database.ExpenseAccountDBAdapter;
import com.asisdroid.moneymanager.database.IncomeAccountDBAdapter;
import com.asisdroid.moneymanager.utility.FontUtils;
import com.asisdroid.moneymanager.utility.MMUtils;
import com.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import com.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import com.co.deanwild.materialshowcaseview.ShowcaseConfig;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.hkjkjkkjkjkk
 * Activities that contain this fragment must implement the
 * {@link AddAccFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddAccFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddAccFragment extends Fragment implements DialogAddCatgFragment.OnDialogClosedListener, View.OnClickListener, DialogAllCatgFragment.OnAllCatgDialogClosedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";
    private static final String ARG_IS_FRESH = "param8";

    private static final String SHOWCASE_ID_FRAGMENT = "firdst timef adhd9dd";

    // TODO: Rename and change types of parameters
    private String addAccType;
    private Calendar calInstance, mcurrentDate;
    private TextView  txtSpinner, errAmount, catgECommonTextview, catgICommonTextview, errorNoCatgeory,errorNoCatgeoryIncome, catgEAdd,catgIAdd, txtExpenseCatgHeader, txtIncomeCatgHeader ;
    private View fragView;
    private Spinner spinnerChoice, spinnerTypeOfPayment;
    private FontUtils fontUtils;
    private EditText edtxtAmount, edtxtDate, edtxtNotes, edtxtSearchCategoryExpense, edtxtSearchCategoryIncome;
    private static DatePickerDialog mDatePicker;
    private static int mYear, mMonth, mDay;
    private SimpleDateFormat formater;
    private RelativeLayout incomeCatgLayout, expenseCatgLayout, expenseVisibleCatgLayout, incomeVisibleCatgLayout;
    private ScrollView scrollViewAddAcct;
    private ImageView calculatorAddAccnt;

    private String selectedDate = "";

    public CategoryDBAdapter catgExpenseDb;
    public CategoryIncomeDBAdapter catgIncomeDb;
    private ExpenseAccountDBAdapter addExpenseDataDb;
    private IncomeAccountDBAdapter addIncomeDataDb;
    public ArrayList<String> myCategoriesENameList, myCategoriesEImgNameList, myCategoriesINameList, myCategoriesIImgNameList;

    private LayoutInflater calculatorInflater;
    private View calculatorView;

    private SpannableString ss;

    private static String AccType, paymentType, paymentTypeSelected, amountSelected, shortNotesSelected, catgSelected, dateSelected ;
    private OnFragmentInteractionListener mListener;

    private String selectedCategoryNameExpense = "", selectedCategoryNameIncome = "";

    //CATEGORY TEXTVIEWS
    private TextView catgE1,catgE2,catgE3,catgE4,catgE5,catgE6,catgE7,catgE8,
            catgI1,catgI2,catgI3,catgI4,catgI5,catgI6,catgI7,catgI8, catgEMore, catgIMore;

    private HashMap<String, Integer> catgDrawableHashmap = new HashMap<>();
    private HashMap<Integer, Integer> catgETextViewsHashmap = new HashMap<>();
    private HashMap<Integer, Integer> catgITextViewsHashmap = new HashMap<>();

    private boolean isNew = true; //FOR DIFFERENTIATING BETWEEN CREATING ACCOUNT AND EDITING ACCOUNT

    public SharedPreferences  showFragmentTips ;

    public AddAccFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param addAccType Parameter 1.
     * @param calGetInstance Parameter 2.
     * @return A new instance of fragment AddAccFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddAccFragment newInstance(String addAccType, Calendar calGetInstance) {
        AddAccFragment fragment = new AddAccFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, addAccType);
        args.putSerializable(ARG_PARAM2, calGetInstance);
        args.putBoolean(ARG_IS_FRESH, true);
        fragment.setArguments(args);
        return fragment;
    }

    public static AddAccFragment newInstanceFromDataScreen(String addAccType, Calendar calGetInstance,String amount, String paymentType, String shortNote, String catg) {
        AddAccFragment fragment = new AddAccFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, addAccType);
        args.putSerializable(ARG_PARAM2, calGetInstance);
        args.putString(ARG_PARAM3, amount);
        args.putString(ARG_PARAM4, paymentType);
        args.putString(ARG_PARAM5, shortNote);
        args.putString(ARG_PARAM6, catg);
        args.putBoolean(ARG_IS_FRESH, false);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            addAccType = getArguments().getString(ARG_PARAM1);
            calInstance = (Calendar) getArguments().getSerializable(ARG_PARAM2);
            amountSelected = getArguments().getString(ARG_PARAM3);
            paymentTypeSelected = getArguments().getString(ARG_PARAM4);
            shortNotesSelected = getArguments().getString(ARG_PARAM5);
            catgSelected = getArguments().getString(ARG_PARAM6);
            isNew = getArguments().getBoolean(ARG_IS_FRESH);
        }
        //CALLED HERE SO THAT IT IS CALLED ONLY ONCE
        initHashmapsForCategories();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragView = inflater.inflate(R.layout.fragment_add_acc, container, false);
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return fragView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
       super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initFragmentUI();
        initCatgeoryDBDatas();
        initFragmentEvents();
        setFontsInFragment();

        if(!isNew){
            setTheDatasInField();
        }
         super.onActivityCreated(savedInstanceState);
    }

    public void initFragmentUI()
    {
        showFragmentTips = getActivity().getSharedPreferences("tempfrag", Context.MODE_PRIVATE);
        View spinnerLayoutView = getLayoutInflater(new Bundle()).inflate(R.layout.spinner_textview, null);
        txtSpinner =(TextView) spinnerLayoutView.findViewById(R.id.txtSpinner);

        errorNoCatgeory= (TextView) fragView.findViewById(R.id.txtErrNocatg);
        errorNoCatgeoryIncome= (TextView) fragView.findViewById(R.id.txtErrNocatgIncome);
        catgEAdd = (TextView) fragView.findViewById(R.id.catgEAdd);
        catgIAdd = (TextView) fragView.findViewById(R.id.catgIAdd);
        txtExpenseCatgHeader = (TextView) fragView.findViewById(R.id.txt_expenseHead);
        txtIncomeCatgHeader = (TextView) fragView.findViewById(R.id.txt_incomeHead);

        //CATEGGORY INITS
        catgE1 = (TextView) fragView.findViewById(R.id.catgE1);
        catgE2 = (TextView) fragView.findViewById(R.id.catgE2);
        catgE3 = (TextView) fragView.findViewById(R.id.catgE3);
        catgE4 = (TextView) fragView.findViewById(R.id.catgE4);
        catgE5 = (TextView) fragView.findViewById(R.id.catgE5);
        catgE6 = (TextView) fragView.findViewById(R.id.catgE6);
        catgE7 = (TextView) fragView.findViewById(R.id.catgE7);
        catgE8 = (TextView) fragView.findViewById(R.id.catgE8);
        catgEMore = (TextView) fragView.findViewById(R.id.catgEMore);

        catgI1 = (TextView) fragView.findViewById(R.id.catgI1);
        catgI2 = (TextView) fragView.findViewById(R.id.catgI2);
        catgI3 = (TextView) fragView.findViewById(R.id.catgI3);
        catgI4 = (TextView) fragView.findViewById(R.id.catgI4);
        catgI5 = (TextView) fragView.findViewById(R.id.catgI5);
        catgI6 = (TextView) fragView.findViewById(R.id.catgI6);
        catgI7 = (TextView) fragView.findViewById(R.id.catgI7);
        catgI8 = (TextView) fragView.findViewById(R.id.catgI8);
        catgIMore = (TextView) fragView.findViewById(R.id.catgIMore);

        errAmount = (TextView) fragView.findViewById(R.id.errAmount);

        edtxtAmount = (EditText) fragView.findViewById(R.id.edtxtAmount);
        edtxtDate = (EditText) fragView.findViewById(R.id.edtxtDate);
        edtxtNotes = (EditText) fragView.findViewById(R.id.edtxtNotes);
        edtxtSearchCategoryExpense = (EditText) fragView.findViewById(R.id.searchCategoryExpense) ;
        edtxtSearchCategoryIncome = (EditText) fragView.findViewById(R.id.searchCategoryIncome) ;

        //Category layouts initialize
        incomeCatgLayout = (RelativeLayout) fragView.findViewById(R.id.tableCategoryIncome);
        expenseCatgLayout = (RelativeLayout) fragView.findViewById(R.id.tableCategoryExpense);
        expenseVisibleCatgLayout = (RelativeLayout) fragView.findViewById(R.id.expenseVisibleCategoryLayout);
        incomeVisibleCatgLayout = (RelativeLayout) fragView.findViewById(R.id.incomeVisibleCategoryLayout);

        scrollViewAddAcct = (ScrollView) fragView.findViewById(R.id.scrollViewAddAcct);

        calculatorAddAccnt = (ImageView) fragView.findViewById(R.id.calculatorAddAccnt);
        calculatorAddAccnt.bringToFront();

        //Setting the default selected date
        formater = new SimpleDateFormat("dd MMMM, yyyy");
        edtxtDate.setText(formater.format(calInstance.getTime()));

        // Spinner element
        spinnerChoice = (Spinner) fragView.findViewById(R.id.optChoice);
        spinnerTypeOfPayment = (Spinner) fragView.findViewById(R.id.spnTypeOfPayment);

        mcurrentDate=Calendar.getInstance();

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Add Income");
        categories.add("Add Expense");

        List<String> updateData = new ArrayList<String>();
        updateData.add("Update Income");
        updateData.add("Update Expense");

        List<String> typeofpayments = new ArrayList<String>();
        typeofpayments.add("Cash");
        typeofpayments.add("Card");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_textview, categories);
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(), R.layout.innerpage_spinner_textview, typeofpayments);
        ArrayAdapter<String> dataAdapterUpdate = new ArrayAdapter<String>(getActivity(), R.layout.spinner_textview, updateData);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.spinner_textview);
        dataAdapter1.setDropDownViewResource(R.layout.innerpage_spinner_textview);
        dataAdapterUpdate.setDropDownViewResource(R.layout.spinner_textview);

        // attaching data adapter to spinner
        if(isNew) {
            spinnerChoice.setAdapter(dataAdapter);
        }
        else {
            spinnerChoice.setAdapter(dataAdapterUpdate);
            spinnerChoice.setEnabled(false);
        }
        spinnerTypeOfPayment.setAdapter(dataAdapter1);

        spinnerTypeOfPayment.setSelection(0);
        paymentType = "cash";

        //Validate according to the choice of income or expense
        if(addAccType.equalsIgnoreCase("credit")) {
            spinnerChoice.setSelection(0);
            incomeCatgLayout.setVisibility(View.VISIBLE);
            spinnerTypeOfPayment.setVisibility(View.GONE);
        }
        else {
            spinnerChoice.setSelection(1);
            expenseCatgLayout.setVisibility(View.VISIBLE);
            spinnerTypeOfPayment.setVisibility(View.VISIBLE);
        }
        AccType = addAccType;

            if(showFragmentTips.getAll().size()==0) {
                SharedPreferences.Editor editor = showFragmentTips.edit();
                editor.putBoolean("isShown", true);
                editor.commit();

                presentShowcaseViewfragment();
            }

    }

    public void initCatgeoryDBDatas()
    {
        //STORING THE CATGEORIES IN DATABASE
        catgExpenseDb = new CategoryDBAdapter(getActivity());
        catgExpenseDb = catgExpenseDb.open();
        catgIncomeDb = new CategoryIncomeDBAdapter(getActivity());
        catgIncomeDb = catgIncomeDb.open();

        myCategoriesENameList = catgExpenseDb.getAllCategoriesName();
        myCategoriesINameList = catgIncomeDb.getAllCategoriesName();

        //JUST FOR ONCE SAVING THE DEFAULT EXPENSE CATEGORIES
        if(myCategoriesENameList.size() == 0) {
            catgExpenseDb.insertNewCategory("Food","1","no","food");
            catgExpenseDb.insertNewCategory("Travel","1","no","transport");
            catgExpenseDb.insertNewCategory("Bill","1","no","bill");
            catgExpenseDb.insertNewCategory("Car and bike","1","no","carandbike");
            catgExpenseDb.insertNewCategory("Cloth","1","no","clothes");
            catgExpenseDb.insertNewCategory("Eat Out","0","no","eatout");
            catgExpenseDb.insertNewCategory("Phone","1","no","phone");
            catgExpenseDb.insertNewCategory("Entertain","1","no","entertainment");
            catgExpenseDb.insertNewCategory("Rent","1","no","rental");
            catgExpenseDb.insertNewCategory("Gift","0","no","gift");
            catgExpenseDb.insertNewCategory("Health","0","no","health");
            catgExpenseDb.insertNewCategory("Pet","0","no","pet");
            catgExpenseDb.insertNewCategory("Sports","0","no","sports");
            catgExpenseDb.insertNewCategory("Electronics","0","no","electronics");
            catgExpenseDb.insertNewCategory("Saving","0","no","saving");
            catgExpenseDb.insertNewCategory("Income Tax","0","no","tax");
        }
        if(myCategoriesINameList.size() == 0) {
            catgIncomeDb.insertNewCategory("Salary","1","no","salary");
            catgIncomeDb.insertNewCategory("Home Rent","1","no","homerent");
        }

        myCategoriesENameList = catgExpenseDb.getAllCategoriesName();
        myCategoriesEImgNameList = catgExpenseDb.getAllCategoriesImageName();
        for(int x=1;x<=8;x++) {
            catgECommonTextview = (TextView) fragView.findViewById(catgETextViewsHashmap.get(x));
            catgECommonTextview.setText(myCategoriesENameList.get(x-1));
            catgECommonTextview.setCompoundDrawablesWithIntrinsicBounds(0,(catgDrawableHashmap.get(myCategoriesEImgNameList.get(x-1))),0,0);
        }

        myCategoriesINameList = catgIncomeDb.getAllCategoriesName();
        myCategoriesIImgNameList = catgIncomeDb.getAllCategoriesImageName();
        for(int x=1;x<=8;x++) {
            catgICommonTextview = (TextView) fragView.findViewById(catgITextViewsHashmap.get(x));
            if (x<=myCategoriesINameList.size()) {
                catgICommonTextview.setText(myCategoriesINameList.get(x - 1));
                catgICommonTextview.setCompoundDrawablesWithIntrinsicBounds(0, (catgDrawableHashmap.get(myCategoriesIImgNameList.get(x - 1))), 0, 0);
            }
            else{
                catgICommonTextview.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void refreshExpenseCatg()
    {
        edtxtSearchCategoryExpense.setText("");
        myCategoriesENameList = catgExpenseDb.getAllCategoriesName();
        myCategoriesEImgNameList = catgExpenseDb.getAllCategoriesImageName();
        for(int x=1;x<=8;x++) {
            catgECommonTextview = (TextView) fragView.findViewById(catgETextViewsHashmap.get(x));
            catgECommonTextview.setText(myCategoriesENameList.get(x-1));
            catgECommonTextview.setCompoundDrawablesWithIntrinsicBounds(0,(catgDrawableHashmap.get(myCategoriesEImgNameList.get(x-1))),0,0);
        }
    }

    private void refreshIncomeCatg()
    {
        edtxtSearchCategoryIncome.setText("");
        myCategoriesINameList = catgIncomeDb.getAllCategoriesName();
        myCategoriesIImgNameList = catgIncomeDb.getAllCategoriesImageName();
        for(int x=1;x<=8;x++) {
            catgICommonTextview = (TextView) fragView.findViewById(catgITextViewsHashmap.get(x));
            if (x<=myCategoriesINameList.size()) {
                catgICommonTextview.setText(myCategoriesINameList.get(x - 1));
                catgICommonTextview.setCompoundDrawablesWithIntrinsicBounds(0, (catgDrawableHashmap.get(myCategoriesIImgNameList.get(x - 1))), 0, 0);
            }
            else{
                catgICommonTextview.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void initHashmapsForCategories()
    {
        //SETTING HASHMAP FOR CATEGORIES DRAWABLE FOR DYNAMICALLY GETTING THE DRAWABLES ACCORDING TO THE DATABASE DATAS
        catgDrawableHashmap.put("food",R.drawable.catg_food);
        catgDrawableHashmap.put("transport",R.drawable.catg_transport);
        catgDrawableHashmap.put("bill",R.drawable.catg_bill);
        catgDrawableHashmap.put("carandbike",R.drawable.catg_carandbike);
        catgDrawableHashmap.put("clothes",R.drawable.catg_clothes);
        catgDrawableHashmap.put("eatout",R.drawable.catg_eatout);
        catgDrawableHashmap.put("custom",R.drawable.catg_custom);
        catgDrawableHashmap.put("phone",R.drawable.catg_phone);
        catgDrawableHashmap.put("entertainment",R.drawable.catg_entertainment);
        catgDrawableHashmap.put("rental",R.drawable.catg_rental);
        catgDrawableHashmap.put("gift",R.drawable.catg_gift);
        catgDrawableHashmap.put("health",R.drawable.catg_health);
        catgDrawableHashmap.put("pet",R.drawable.catg_pet);
        catgDrawableHashmap.put("electronics",R.drawable.catg_electronics);
        catgDrawableHashmap.put("sports",R.drawable.catg_sports);
        catgDrawableHashmap.put("saving",R.drawable.catg_saving);
        catgDrawableHashmap.put("tax",R.drawable.catg_tax);
        catgDrawableHashmap.put("salary",R.drawable.catg_salary);
        catgDrawableHashmap.put("homerent",R.drawable.catg_homerent);

        //SETTING HASHMAP FOR CATEGORIES DRAWABLE FOR DYNAMICALLY GETTING THE DRAWABLES ACCORDING TO THE DATABASE DATAS
        catgETextViewsHashmap.put(1,R.id.catgE1);
        catgETextViewsHashmap.put(2,R.id.catgE2);
        catgETextViewsHashmap.put(3,R.id.catgE3);
        catgETextViewsHashmap.put(4,R.id.catgE4);
        catgETextViewsHashmap.put(5,R.id.catgE5);
        catgETextViewsHashmap.put(6,R.id.catgE6);
        catgETextViewsHashmap.put(7,R.id.catgE7);
        catgETextViewsHashmap.put(8,R.id.catgE8);

        catgITextViewsHashmap.put(1,R.id.catgI1);
        catgITextViewsHashmap.put(2,R.id.catgI2);
        catgITextViewsHashmap.put(3,R.id.catgI3);
        catgITextViewsHashmap.put(4,R.id.catgI4);
        catgITextViewsHashmap.put(5,R.id.catgI5);
        catgITextViewsHashmap.put(6,R.id.catgI6);
        catgITextViewsHashmap.put(7,R.id.catgI7);
        catgITextViewsHashmap.put(8,R.id.catgI8);
    }

    public void searchECategoriesAndShow(ArrayList<String> name, ArrayList<String> imagename)
    {
        myCategoriesENameList = name;
        myCategoriesEImgNameList = imagename;
        if(myCategoriesENameList.size()!=0) {
            scrollViewAddAcct.smoothScrollTo(0,catgEAdd.getBottom());
            for(int x=1;x<=8;x++) {
                catgECommonTextview = (TextView) fragView.findViewById(catgETextViewsHashmap.get(x));
                catgECommonTextview.setBackground(getResources().getDrawable(R.drawable.category_gradient_border));

                if((x)>myCategoriesEImgNameList.size()) {
                   catgECommonTextview.setVisibility(View.INVISIBLE);
                    continue;
                }

                //HIGHLIGHTING THE SELECTED CATGEORY ON SEARCH TOO
                if(myCategoriesENameList.get(x-1).equalsIgnoreCase(selectedCategoryNameExpense)){
                    catgECommonTextview.setBackground(getResources().getDrawable(R.drawable.category_selected_border_gradient));
                }
                else {
                    catgECommonTextview.setBackground(getResources().getDrawable(R.drawable.category_gradient_border));
                }
                catgECommonTextview.setText(myCategoriesENameList.get(x-1));
                catgECommonTextview.setCompoundDrawablesWithIntrinsicBounds(0,(catgDrawableHashmap.get(myCategoriesEImgNameList.get(x-1))),0,0);
                if(catgECommonTextview.getVisibility() == View.INVISIBLE) {
                    catgECommonTextview.setVisibility(View.VISIBLE);
                }
            }
            expenseVisibleCatgLayout.setVisibility(View.VISIBLE);
            errorNoCatgeory.setVisibility(View.INVISIBLE);
        }
        else {
            scrollViewAddAcct.smoothScrollTo(0,expenseCatgLayout.getBottom());
            expenseVisibleCatgLayout.setVisibility(View.INVISIBLE);
            errorNoCatgeory.setVisibility(View.VISIBLE);
            MMUtils.startAnimate(catgEAdd, R.anim.mutiplezoominout, getActivity());
        }
    }

    public void searchICategoriesAndShow(ArrayList<String> namei, ArrayList<String> imagenamei)
    {
        myCategoriesINameList = namei;
        myCategoriesIImgNameList = imagenamei;
        if(myCategoriesINameList.size()!=0) {
            scrollViewAddAcct.smoothScrollTo(0,catgIAdd.getBottom());
            for(int x=1;x<=8;x++) {
                catgICommonTextview = (TextView) fragView.findViewById(catgITextViewsHashmap.get(x));
                catgICommonTextview.setBackground(getResources().getDrawable(R.drawable.category_gradient_border));

                if((x)>myCategoriesIImgNameList.size()) {
                    catgICommonTextview.setVisibility(View.INVISIBLE);
                    continue;
                }

                //HIGHLIGHTING THE SELECTED CATGEORY ON SEARCH TOO
                if(myCategoriesINameList.get(x-1).equalsIgnoreCase(selectedCategoryNameIncome)){
                    catgICommonTextview.setBackground(getResources().getDrawable(R.drawable.category_selected_border_gradient));
                }
                else {
                    catgICommonTextview.setBackground(getResources().getDrawable(R.drawable.category_gradient_border));
                }
                catgICommonTextview.setText(myCategoriesINameList.get(x-1));
                catgICommonTextview.setCompoundDrawablesWithIntrinsicBounds(0,(catgDrawableHashmap.get(myCategoriesIImgNameList.get(x-1))),0,0);
                if(catgICommonTextview.getVisibility() == View.INVISIBLE) {
                    catgICommonTextview.setVisibility(View.VISIBLE);
                }
            }
            incomeVisibleCatgLayout.setVisibility(View.VISIBLE);
            errorNoCatgeoryIncome.setVisibility(View.INVISIBLE);
        }
        else {
            scrollViewAddAcct.smoothScrollTo(0,incomeCatgLayout.getBottom());
            incomeVisibleCatgLayout.setVisibility(View.INVISIBLE);
            errorNoCatgeoryIncome.setVisibility(View.VISIBLE);
            MMUtils.startAnimate(catgIAdd, R.anim.mutiplezoominout, getActivity());
        }
    }

    public void setFontsInFragment()
    {
       // fontUtils.setFont(txtSpinner, FontUtils.GothamHTF_Medium);
    }

    public void initFragmentEvents()
    {
        // Spinner click listener
        spinnerChoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position == 0) {
                    AccType = "credit";
                    incomeCatgLayout.setVisibility(View.VISIBLE);
                    expenseCatgLayout.setVisibility(View.GONE);
                    spinnerTypeOfPayment.setVisibility(View.GONE);
                }
                else {
                    AccType = "debit";
                    expenseCatgLayout.setVisibility(View.VISIBLE);
                    incomeCatgLayout.setVisibility(View.GONE);
                    spinnerTypeOfPayment.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerTypeOfPayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position == 0) {
                    paymentType = "cash";
                }
                else {
                    paymentType = "card";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edtxtAmount.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (edtxtAmount.getRight() - edtxtAmount.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        edtxtAmount.setText("");
                        return true;
                    }
                }
                return false;
            }
        });

        edtxtAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).closeTheExpenseDateMenu();
            }
        });

        edtxtAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focusedOrNot) {
                if(focusedOrNot) {
                    MMUtils.startAnimate(calculatorAddAccnt, R.anim.mutiplezoominout, getActivity());
                }
            }
        });

        edtxtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).closeTheExpenseDateMenu();
                MMUtils.hideKeyboard(getActivity(),edtxtAmount);
                 //OPENING DATEPICKER
                mYear=mcurrentDate.get(Calendar.YEAR);
                mMonth=mcurrentDate.get(Calendar.MONTH);
                mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);
                mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        mcurrentDate.set(Calendar.YEAR, selectedyear);
                        mcurrentDate.set(Calendar.MONTH,selectedmonth);
                        mcurrentDate.set(Calendar.DAY_OF_MONTH,selectedday);
                        selectedDate=selectedyear+"-"+String.format("%02d", selectedmonth+1)+"-"+String.format("%02d", selectedday);
                        edtxtDate.setText(formater.format(mcurrentDate.getTime()));
                    }
                },mYear, mMonth, mDay);

                Calendar c = Calendar.getInstance();
                c.add(Calendar.YEAR,-10);

                mDatePicker.getDatePicker().setMinDate(c.getTimeInMillis());
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.setTitle(null);
                mDatePicker.show();
            }
        });

        edtxtNotes.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                ((MainActivity)getActivity()).closeTheExpenseDateMenu();
            }
        });

        calculatorAddAccnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).closeTheExpenseDateMenu();
                //OPENING CALCULATOR
                DialogCalculatorFragment dialogCalculatorFragment = DialogCalculatorFragment.getInstance(fragView);
                dialogCalculatorFragment.show(getActivity().getSupportFragmentManager(), "");

            }
        });

        edtxtSearchCategoryExpense.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Toast.makeText(getActivity(), "data="+edtxtSearchCategoryExpense.getText().toString()+"----"+catgExpenseDb.getAllMatchedCategoriesName(edtxtSearchCategoryExpense.getText().toString()), Toast.LENGTH_SHORT).show();
                searchECategoriesAndShow(catgExpenseDb.getAllMatchedCategoriesName(edtxtSearchCategoryExpense.getText().toString().trim()),catgExpenseDb.getAllMatchedCategoriesImageName(edtxtSearchCategoryExpense.getText().toString().trim()));
            }
        });

        edtxtSearchCategoryIncome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                 searchICategoriesAndShow(catgIncomeDb.getAllMatchedCategoriesName(edtxtSearchCategoryIncome.getText().toString().trim()),catgIncomeDb.getAllMatchedCategoriesImageName(edtxtSearchCategoryIncome.getText().toString().trim()));
            }
        });

        edtxtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                 errAmount.setVisibility(View.GONE);
                 edtxtAmount.setBackground(getResources().getDrawable(R.drawable.gradient_edittext_border));
            }
        });

        //SET EVENTS CLICK ON CATEGORIES
        catgE1.setOnClickListener(this);
        catgE2.setOnClickListener(this);
        catgE3.setOnClickListener(this);
        catgE4.setOnClickListener(this);
        catgE5.setOnClickListener(this);
        catgE6.setOnClickListener(this);
        catgE7.setOnClickListener(this);
        catgE8.setOnClickListener(this);
        catgEMore.setOnClickListener(this);
        catgEAdd.setOnClickListener(this);

        catgI1.setOnClickListener(this);
        catgI2.setOnClickListener(this);
        catgI3.setOnClickListener(this);
        catgI4.setOnClickListener(this);
        catgI5.setOnClickListener(this);
        catgI6.setOnClickListener(this);
        catgI7.setOnClickListener(this);
        catgI8.setOnClickListener(this);
        catgIMore.setOnClickListener(this);
        catgIAdd.setOnClickListener(this);
    }

    public void onFragmentDateSelected()
    {
        //Toast.makeText(getActivity(), "dasdsadas", Toast.LENGTH_SHORT).show();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //OVERRIDEN CLASS FROM DialogAddCatgFragment.OnDialogClosedListener  FOR ADD CATEGORY DIALOG CLOSE EVENT
    @Override
    public void onDialogClosed(String catgType) {
        if(catgType.equalsIgnoreCase("expense")){refreshExpenseCatg();}
        else if(catgType.equalsIgnoreCase("income")){refreshIncomeCatg();}
     }

    //OVERRIDEN CLASS FROM DialogAllCatgFragment.OnAllCatgDialogClosedListener  FOR ALL CATEGORY DIALOG CLOSE EVENT
    @Override
    public void onAllCatgDialogClosed(String selectedCatgFromAll, String catgType) {
          if(selectedCatgFromAll!= null && (!(selectedCatgFromAll.equalsIgnoreCase("")))){
               MMUtils.startAnimate(txtExpenseCatgHeader,R.anim.blink,fragView.getContext());
               if(catgType.equalsIgnoreCase("expense")){
                  selectedCategoryNameExpense = selectedCatgFromAll;
                  ss = new SpannableString("Choosen expense catgeory - "+selectedCategoryNameExpense);
                  ss.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 27, ss.length(), 0);
                  txtExpenseCatgHeader.setText(ss);
                   refreshExpenseCatg();
                   txtExpenseCatgHeader.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                   expenseCatgLayout.setBackground(getResources().getDrawable(R.drawable.gradient_edittext_border));
                   MMUtils.startAnimate(txtExpenseCatgHeader,R.anim.blink,getActivity());
              }
              else
              if(catgType.equalsIgnoreCase("income")){
                  selectedCategoryNameIncome = selectedCatgFromAll;
                  ss = new SpannableString("Choosen income catgeory - "+selectedCategoryNameIncome);
                  ss.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 26, ss.length(), 0);
                  txtIncomeCatgHeader.setText(ss);
                  refreshIncomeCatg();
                  txtIncomeCatgHeader.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                  incomeCatgLayout.setBackground(getResources().getDrawable(R.drawable.gradient_edittext_border));
                  MMUtils.startAnimate(txtIncomeCatgHeader,R.anim.blink,getActivity());
              }

          }
          else{
              if(catgType.equalsIgnoreCase("expense")) {
                  refreshExpenseCatg();
              }
              else{
                  refreshIncomeCatg();
              }
          }

    }

     //SAVING THE ACC DATAS
     public void onSaveAccData()
     {
         String amount, date, shortnotes, paymenttype;
         amount = edtxtAmount.getText().toString().trim();
         if(selectedDate.equalsIgnoreCase("") || selectedDate.equalsIgnoreCase(null)) {
             SimpleDateFormat temp  = new SimpleDateFormat("yyyy-MM-dd");
             date = temp.format(calInstance.getTime());
         }
         else{
             date = selectedDate;
         }
         shortnotes = edtxtNotes.getText().toString().trim();
         paymenttype = spinnerTypeOfPayment.getSelectedItem().toString().trim();

         boolean isErrorNot = false;

         if(amount.equalsIgnoreCase("") || Integer.parseInt(amount)==0){
             isErrorNot = true;
             errAmount.setVisibility(View.VISIBLE);
             edtxtAmount.setBackground(getResources().getDrawable(R.drawable.gradient_edittext_error_border));
             edtxtAmount.requestFocus();
         }
         else{
             errAmount.setVisibility(View.GONE);
             edtxtAmount.setBackground(getResources().getDrawable(R.drawable.gradient_edittext_border));
         }

         //FOR INCOME
         if(AccType.equalsIgnoreCase("credit")){
             if(selectedCategoryNameIncome.equalsIgnoreCase("")){
                 txtIncomeCatgHeader.setTextColor(getResources().getColor(R.color.Red));
                 incomeCatgLayout.setBackground(getResources().getDrawable(R.drawable.gradient_edittext_error_border));
                 if(isErrorNot==false){
                     isErrorNot = true;
                     scrollViewAddAcct.smoothScrollTo(0,catgEAdd.getBottom());
                     MMUtils.hideKeyboard(getActivity(),edtxtAmount);
                 }
             }
             else{
                 txtIncomeCatgHeader.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                 incomeCatgLayout.setBackground(getResources().getDrawable(R.drawable.gradient_edittext_border));
             }

             //CHEKCING FOR ERRORS THERE OR NOT AND SAVE DATA TO ACCOUNT
             if(!isErrorNot) {
                 addIncomeDataDb = new IncomeAccountDBAdapter(getActivity());
                 addIncomeDataDb = addIncomeDataDb.open();
                 if(isNew) {
                     addIncomeDataDb.insertAccData(amount, date, shortnotes, selectedCategoryNameIncome);
                     Toast.makeText(getActivity(), "Your income data saved successfully!", Toast.LENGTH_SHORT).show();
                 }
                 else{
                     boolean updated = addIncomeDataDb.updateAccData(amount, date, shortnotes, selectedCategoryNameIncome,amountSelected,dateSelected,shortNotesSelected,catgSelected);
                     if(updated){
                         Toast.makeText(getActivity(), "Your income data updated successfully!", Toast.LENGTH_SHORT).show();
                     }
                 }
                 catgIncomeDb.updateTheCatgNoOfUse(selectedCategoryNameIncome,String.valueOf(catgIncomeDb.getTheCatgUse(selectedCategoryNameIncome)+1));
                 Intent intent = getActivity().getIntent();
                 getActivity().finish();
                 startActivity(intent);
             }
         }
         else {   //FOR EXPENSE
             if(selectedCategoryNameExpense.equalsIgnoreCase("")){
                 txtExpenseCatgHeader.setTextColor(getResources().getColor(R.color.Red));
                 expenseCatgLayout.setBackground(getResources().getDrawable(R.drawable.gradient_edittext_error_border));
                 if(isErrorNot==false) {
                     isErrorNot = true;
                     scrollViewAddAcct.smoothScrollTo(0, catgEAdd.getBottom());
                     MMUtils.hideKeyboard(getActivity(),edtxtAmount);
                 }
             }
             else{
                 txtExpenseCatgHeader.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                 expenseCatgLayout.setBackground(getResources().getDrawable(R.drawable.gradient_edittext_border));
             }
             //CHEKCING FOR ERRORS THERE OR NOT AND SAVE DATA TO ACCOUNT
             if(!isErrorNot) {
                 addExpenseDataDb = new ExpenseAccountDBAdapter(getActivity());
                 addExpenseDataDb = addExpenseDataDb.open();
                 if(isNew){ //FOR FRESH DATA ADDITION
                     addExpenseDataDb.insertAccData(amount,date,shortnotes,paymenttype,selectedCategoryNameExpense);
                     Toast.makeText(getActivity(), "Your expense data saved successfully!", Toast.LENGTH_SHORT).show();
                 }
                 else{  //FOR EDITED DATA
                     boolean updated =addExpenseDataDb.updateAccData(amount,date,shortnotes,paymenttype,selectedCategoryNameExpense,amountSelected,dateSelected,shortNotesSelected,paymentTypeSelected,catgSelected);
                     if(updated){
                         Toast.makeText(getActivity(), "Your expense data updated successfully!", Toast.LENGTH_SHORT).show();
                     }
                 }
                 catgExpenseDb.updateTheCatgNoOfUse(selectedCategoryNameExpense,String.valueOf(catgExpenseDb.getTheCatgUse(selectedCategoryNameExpense)+1));
                 Intent intent = getActivity().getIntent();
                 getActivity().finish();
                 startActivity(intent);
             }
         }
     }

     //OVERRIDEN CLASS FROM View.OnClickListener . ESPECIALLY FOR CATEGORY BUTTONS
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.catgE1:
                categoryECommonClickEventMethod(1);
                break;

            case R.id.catgE2:
                categoryECommonClickEventMethod(2);
                break;

            case R.id.catgE3:
                categoryECommonClickEventMethod(3);
                break;

            case R.id.catgE4:
                categoryECommonClickEventMethod(4);
                break;

            case R.id.catgE5:
                categoryECommonClickEventMethod(5);
                break;

            case R.id.catgE6:
                categoryECommonClickEventMethod(6);
                break;

            case R.id.catgE7:
                categoryECommonClickEventMethod(7);
                break;

            case R.id.catgE8:
                categoryECommonClickEventMethod(8);
                break;

            case R.id.catgEMore:
                DialogAllCatgFragment dialogAllCatgFragment = DialogAllCatgFragment.getInstance(AddAccFragment.this,edtxtSearchCategoryExpense.getText().toString(),"expense", selectedCategoryNameExpense);
                dialogAllCatgFragment.show(getActivity().getSupportFragmentManager(), "");
                break;

            case R.id.catgEAdd:
                DialogAddCatgFragment dialogAddCatgFragment = DialogAddCatgFragment.getInstance(AddAccFragment.this,edtxtSearchCategoryExpense.getText().toString(),"expense");
                dialogAddCatgFragment.show(getActivity().getSupportFragmentManager(), "");
                break;

            case R.id.catgI1:
                categoryICommonClickEventMethod(1);
                break;

            case R.id.catgI2:
                categoryICommonClickEventMethod(2);
                break;

            case R.id.catgI3:
                categoryICommonClickEventMethod(3);
                break;

            case R.id.catgI4:
                categoryICommonClickEventMethod(4);
                break;

            case R.id.catgI5:
                categoryICommonClickEventMethod(5);
                break;

            case R.id.catgI6:
                categoryICommonClickEventMethod(6);
                break;

            case R.id.catgI7:
                categoryICommonClickEventMethod(7);
                break;

            case R.id.catgI8:
                categoryICommonClickEventMethod(8);
                break;

            case R.id.catgIMore:
                DialogAllCatgFragment dialogAllCatgFragmentI = DialogAllCatgFragment.getInstance(AddAccFragment.this,edtxtSearchCategoryIncome.getText().toString(),"income", selectedCategoryNameIncome);
                dialogAllCatgFragmentI.show(getActivity().getSupportFragmentManager(), "");
                break;

            case R.id.catgIAdd:
                DialogAddCatgFragment dialogAddCatgFragmentIncome = DialogAddCatgFragment.getInstance(AddAccFragment.this,edtxtSearchCategoryExpense.getText().toString(),"income");
                dialogAddCatgFragmentIncome.show(getActivity().getSupportFragmentManager(), "");
                break;

            default:
                break;
        }
    }

    //CATEGORY CLICK EVENT MODULE for expense and income
    private void categoryECommonClickEventMethod(int index)
    {
        MMUtils.hideKeyboard(getActivity(),edtxtSearchCategoryExpense);
        catgECommonTextview = (TextView) fragView.findViewById(catgETextViewsHashmap.get(index));
        selectedCategoryNameExpense = catgECommonTextview.getText().toString();
        ss = new SpannableString("Choosen expense catgeory - "+selectedCategoryNameExpense);
        ss.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 27, ss.length(), 0);
        txtExpenseCatgHeader.setText(ss);
        txtExpenseCatgHeader.setShadowLayer(10,5,5,getResources().getColor(R.color.DarkSeaGreen));
        txtExpenseCatgHeader.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        expenseCatgLayout.setBackground(getResources().getDrawable(R.drawable.gradient_edittext_border));
        MMUtils.startAnimate(txtExpenseCatgHeader,R.anim.blink,fragView.getContext());
        MMUtils.startAnimate(catgECommonTextview,R.anim.blink,fragView.getContext());
        for(int x=1;x<=8;x++) {
            catgECommonTextview = (TextView) fragView.findViewById(catgETextViewsHashmap.get(x));
            if(x==index) {
                catgECommonTextview.setBackground(getResources().getDrawable(R.drawable.category_selected_border_gradient));
            }
            else{
                catgECommonTextview.setBackground(getResources().getDrawable(R.drawable.category_gradient_border));
            }
        }
    }

    private void categoryICommonClickEventMethod(int index)
    {
        catgICommonTextview = (TextView) fragView.findViewById(catgITextViewsHashmap.get(index));
        selectedCategoryNameIncome = catgICommonTextview.getText().toString();
        ss = new SpannableString("Choosen income catgeory - "+selectedCategoryNameIncome);
        ss.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 26, ss.length(), 0);
        txtIncomeCatgHeader.setText(ss);
        txtIncomeCatgHeader.setShadowLayer(10,5,5,getResources().getColor(R.color.DarkSeaGreen));
        txtIncomeCatgHeader.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        incomeCatgLayout.setBackground(getResources().getDrawable(R.drawable.gradient_edittext_border));
        MMUtils.startAnimate(txtIncomeCatgHeader,R.anim.blink,fragView.getContext());
        MMUtils.startAnimate(catgICommonTextview,R.anim.blink,fragView.getContext());
        for(int x=1;x<=8;x++) {
            catgICommonTextview = (TextView) fragView.findViewById(catgITextViewsHashmap.get(x));
            if(x==index) {
                catgICommonTextview.setBackground(getResources().getDrawable(R.drawable.category_selected_border_gradient));
            }
            else{
                catgICommonTextview.setBackground(getResources().getDrawable(R.drawable.category_gradient_border));
            }
        }
    }

    private void setTheDatasInField(){
        String commonCatgText;
        SimpleDateFormat temp  = new SimpleDateFormat("yyyy-MM-dd");
        dateSelected = temp.format(calInstance.getTime());
        edtxtAmount.setText(amountSelected);
        edtxtNotes.setText(shortNotesSelected);
        if(paymentTypeSelected.equalsIgnoreCase("cash")) {
            spinnerTypeOfPayment.setSelection(0);
        }else{
            spinnerTypeOfPayment.setSelection(1);
        }
        if(addAccType.equalsIgnoreCase("credit")) {
            selectedCategoryNameIncome = catgSelected;
            spinnerChoice.setSelection(0);
            ss = new SpannableString("Choosen income catgeory - "+selectedCategoryNameIncome);
            ss.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 26, ss.length(), 0);
            txtIncomeCatgHeader.setText(ss);
            txtIncomeCatgHeader.setShadowLayer(10,5,5,getResources().getColor(R.color.DarkSeaGreen));
            txtIncomeCatgHeader.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            incomeCatgLayout.setBackground(getResources().getDrawable(R.drawable.gradient_edittext_border));
            MMUtils.startAnimate(txtExpenseCatgHeader,R.anim.blink,fragView.getContext());
            for(int x=1;x<=8;x++) {
                catgICommonTextview = (TextView) fragView.findViewById(catgITextViewsHashmap.get(x));
                commonCatgText = catgICommonTextview.getText().toString();
                if(commonCatgText.equalsIgnoreCase(selectedCategoryNameIncome)) {
                    catgICommonTextview.setBackground(getResources().getDrawable(R.drawable.category_selected_border_gradient));
                }
                else{
                    catgICommonTextview.setBackground(getResources().getDrawable(R.drawable.category_gradient_border));
                }
            }
        }
        else {
            selectedCategoryNameExpense = catgSelected;
            spinnerChoice.setSelection(1);
            ss = new SpannableString("Choosen expense catgeory - "+selectedCategoryNameExpense);
            ss.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 27, ss.length(), 0);
            txtExpenseCatgHeader.setText(ss);
            txtExpenseCatgHeader.setShadowLayer(10,5,5,getResources().getColor(R.color.DarkSeaGreen));
            txtExpenseCatgHeader.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            MMUtils.startAnimate(txtExpenseCatgHeader,R.anim.blink,fragView.getContext());
            for(int x=1;x<=8;x++) {
                catgECommonTextview = (TextView) fragView.findViewById(catgETextViewsHashmap.get(x));
                commonCatgText = catgECommonTextview.getText().toString();
                if(commonCatgText.equalsIgnoreCase(selectedCategoryNameExpense)) {
                    catgECommonTextview.setBackground(getResources().getDrawable(R.drawable.category_selected_border_gradient));
                }
                else{
                    catgECommonTextview.setBackground(getResources().getDrawable(R.drawable.category_gradient_border));
                }
            }
        }
    }

    //FIRST TIME SHOWCASE VIEW TIPS
    private void presentShowcaseViewfragment() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity(), SHOWCASE_ID_FRAGMENT);

        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
            @Override
            public void onShow(MaterialShowcaseView itemView, int position) {
                switch (position) {
                    case 0: {
                        MMUtils.startAnimate(fragView.findViewById(R.id.AccTypeLayout),R.anim.shake,getActivity());
                        break;
                    }

                    case 1: {
                        MMUtils.startAnimate(calculatorAddAccnt,R.anim.shake,getActivity());
                        break;
                    }

                    case 2: {
                        MMUtils.startAnimate(expenseCatgLayout,R.anim.shake,getActivity());
                        break;
                    }

                    default: {
                        break;
                    }
                }
            }
        });

        sequence.setOnItemDismissedListener(new MaterialShowcaseSequence.OnSequenceItemDismissedListener() {
            @Override
            public void onDismiss(MaterialShowcaseView itemView, int position) {
                if(position==6){
                    //mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
            }
        });

        sequence.setConfig(config);

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(getActivity())
                        .setTarget(fragView.findViewById(R.id.AccTypeLayout))
                        .setDismissText("GOT IT")
                        .setTitleText("ADD YOUR DATA")
                        .setDismissStyle(Typeface.SANS_SERIF)
                        .setContentText("Tap on it to switch between add expense and income directly.")
                        .withCircleShape()
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(getActivity())
                        .setTarget(fragView.findViewById(R.id.edtxtDate))
                        .setTitleText("IN APP CALCULATOR")
                        .setDismissStyle(Typeface.SANS_SERIF)
                        .setDismissText("GOT IT")
                        .setContentText("Need some calculations to do! Tap on our calculator.")
                        .withoutShape()
                        .build()
        );

        View tempView;
        if(AccType.equalsIgnoreCase("credit")){
            tempView = incomeCatgLayout;
        }
        else{
            tempView = expenseCatgLayout;
        }
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(getActivity())
                        .setTarget(tempView)
                        .setTitleText("Select, search or add your own category here.")
                        .setDismissStyle(Typeface.SANS_SERIF)
                        .setDismissText("GOT IT")
                        .setContentText("See all your details for this date. Tap on the next and previous arrows to see the respective date or week or month or year data's.")
                        .withCircleShape()
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(getActivity())
                        .setTarget(tempView)
                        .setTitleText("SAVE YOUR DATA")
                        .setDismissStyle(Typeface.SANS_SERIF)
                        .setDismissText("GOT IT")
                        .setContentText("Click on the save button at the top-right corner of the menu bar.")
                        .withoutShape()
                        .build()
        );

        sequence.start();
    }
}

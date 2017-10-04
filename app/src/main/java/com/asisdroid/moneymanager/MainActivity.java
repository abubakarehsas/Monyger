package com.asisdroid.moneymanager;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.asisdroid.moneymanager.Dialog.DialogAddCatgFragment;
import com.asisdroid.moneymanager.Dialog.DialogDatepickerFragment;
import com.asisdroid.moneymanager.Dialog.DialogRemainderFragment;
import com.asisdroid.moneymanager.Dialog.DialogRemindMeFragment;
import com.asisdroid.moneymanager.database.ExpenseAccountDBAdapter;
import com.asisdroid.moneymanager.database.IncomeAccountDBAdapter;
import com.asisdroid.moneymanager.utility.Alarmreceiver;
import com.asisdroid.moneymanager.utility.FontUtils;
import com.asisdroid.moneymanager.utility.MMUtils;
import com.asisdroid.moneymanager.utility.MoneyManagerPreferences;
import com.asisdroid.moneymanager.utility.RemindMeReceiver;
import com.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import com.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import com.co.deanwild.materialshowcaseview.ShowcaseConfig;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends DemoBase implements  OnChartValueSelectedListener, DatePickerDialog.OnDateSetListener, AddAccFragment.OnFragmentInteractionListener, DialogRemainderFragment.onRemainderDialogClosed, DialogRemindMeFragment.onRemindMeDialogClosedListener {

    private static final String SLIDINGTAG = "AndroidSlidingUpPanel";
    private static final String SHOWCASE_ID = "first time opendfdasd";

    private TextView txtCredit, txtDebit,txtCreditMin, txtDebitMin, txtDate, txtCalendar, txtDay, txtMonth, txtWeek, txtYear;
    private ImageView btnBalanceLeft, btnBalanceRight, connectorLeft, connectorRight;
    private TextView btnBalanceMid, expenseDetailsTabClick, incomeDetailsTabClick, expenseDetailsTabClickSelector,incomeDetailsTabClickSelector, noDataErrs, groupBySpinnerLabel;
    private FontUtils fontUtils;
    private PieChart mChart;
    private HorizontalBarChart mBarChart;
    private Toolbar toolbar;
    private static Calendar cal;
    private ImageView nextData, prevData;
    public static  MainActivity mainActivityInstance;
    private LinearLayout expenseDateLayout;
    private LinearLayout bottomLayout, bottomLayoutMin;
    private int mShortAnimationDuration;
    private RelativeLayout slidingBalanceLayout, mainLayout, subLayout, chartViewLayout;
    public static boolean isMenuVisibleOrNot = false;
    private Animation closeMenuAnime, openMenuAnime, fadeInAnime, sliderAnime;
    private boolean isNextBlocked = true;

    private Spinner sortingSpinner;

    private IncomeAccountDBAdapter incomeAccDb;
    private ExpenseAccountDBAdapter expenseAccDb;

    private int chartClickedCatgIndex = -1;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    public AddAccFragment myFragment;

    private String dataDateStart, dataDateEnd;

    private ArrayList<String> incomeDataArray, expenseDataArray,  dateOfTransIArray,shortNotesIArray, catgIArray,
             dateOfTransEArray,shortNotesEArray, paymentTypeEArray, catgEArray,catgChartArray, catgChartIArray,
             dateGroupEArray, dateGroupIArray, amountGroupEArray, amountGroupIArray;

    private ArrayList<Integer> amountIArray, amountEArray, amountChartArray;
    private int totalIncome = 0, totalExpense = 0;

    private HashMap<String, Integer> catgAllDrawableHashmap = new HashMap<>();

    ListViewCustomAdapter listAdapter;
    ExpandableListView listOfBalance;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    int totalMonthlyIncome = 0, totalMonthlyExpense = 0;

    private Menu mainMenu;

    private static final String currentViewPreference = "MyPrefs" ;
    private static final String currentView = "calndrView" ;
    private static final String DayView = "day" ;
    private static final String MonthView = "month" ;
    private static final String YearView = "year" ;
    private static final String DateView = "date" ;
    private static final String WeekView = "week" ;
    public SharedPreferences currentCalndrDisplay, showTipsMain, showFragmentTips, remainderTypePreference, remindMePreference;
    SharedPreferences.Editor editor;

    private String currentDetailsView = "expense";
    private int currentSortingOption = 0;

     protected SlidingUpPanelLayout mLayout;

    private MoneyManagerPreferences myPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivityInstance = this;
        myPreference = MoneyManagerPreferences.getInstance(mainActivityInstance);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //Setting Action bar string and style
        SpannableString s = new SpannableString(" Monyger");
        s.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, s.length(), 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(s);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setSubtitle(" Track & Spend Smarter");
        //getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        //Calling all initializing functions
        initMyUI();
        getDbDatas();
        //initBarCharts();
        setFonts();
        setMyEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mainMenu = menu;
        if(subLayout.getVisibility() == View.VISIBLE) {
            mainMenu.findItem(R.id.otheroptions).setVisible(false);
            mainMenu.findItem(R.id.moreoptions).setVisible(false);
            mainMenu.findItem(R.id.saveData).setVisible(true);
            mainMenu.findItem(R.id.remainders).setVisible(false);
        }
        else {
            mainMenu.findItem(R.id.otheroptions).setVisible(true);
            mainMenu.findItem(R.id.moreoptions).setVisible(true);
            mainMenu.findItem(R.id.saveData).setVisible(false);
            mainMenu.findItem(R.id.remainders).setVisible(true);
        }
        //SETTING ALERT OR NO ALERT ICON
        if(remainderTypePreference.getString("alertType","noalert").equalsIgnoreCase("noalert")){
            mainMenu.findItem(R.id.remainders).setIcon(R.mipmap.alertoff);
        }
        else{
            mainMenu.findItem(R.id.remainders).setIcon(R.mipmap.alerton);
        }

        if(remindMePreference.getBoolean("isRemindMe",false)){ //FOR REMIND ME TRUE
            mainMenu.findItem(R.id.addexpenseremainder).setChecked(true);
        }
        else{
            mainMenu.findItem(R.id.addexpenseremainder).setChecked(false);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.moreoptions: {
                if(isMenuVisibleOrNot==true)
                {
                    closeTheExpenseDateMenu();
                }
                else
                {
                    isMenuVisibleOrNot = true;
                    if(openMenuAnime == null)
                    {openMenuAnime = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.movedown);}
                    openMenuAnime.setFillAfter(true);
                    expenseDateLayout.startAnimation(openMenuAnime);
                    openMenuAnime.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            expenseDateLayout.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });

                    //Making the calendar options inactive if it is active
                    if(isActive(txtCalendar))
                    {
                        setInActive(txtCalendar);
                    }

                    openMenuAnime = null;
                }

                break;
            }
            case R.id.remainders : {
                //CALL HERE THE REMAINDER FUNCTION
                DialogRemainderFragment dialogAddCatgFragment = DialogRemainderFragment.getInstance(mainActivityInstance);
                dialogAddCatgFragment.show(mainActivityInstance.getSupportFragmentManager(), "");
                break;
            }
            case R.id.saveData : {
                //CALL HERE THE FRAGMENT FUNCTION
                if(myFragment!=null){
                    myFragment.onSaveAccData();
                }
                break;
            }
            case R.id.addexpenseremainder : {
                if(remindMePreference.getBoolean("isRemindMe",false)==false) { //if remind me is OFF
                    DialogRemindMeFragment dialogRemindMeFragment = DialogRemindMeFragment.getInstance(mainActivityInstance);
                    dialogRemindMeFragment.show(mainActivityInstance.getSupportFragmentManager(), "");
                }
                else{  //if remind me is ON - uncheck and cancel the alarm manager here
                    mainMenu.findItem(R.id.addexpenseremainder).setChecked(false);
                    editor = remindMePreference.edit();
                    editor.putBoolean("isRemindMe", false);
                    editor.commit();
                    Intent intentRemindMe = new Intent(MainActivity.this, RemindMeReceiver.class);
                    PendingIntent remindmePendingIntent = PendingIntent.getBroadcast(MainActivity.this, 1, intentRemindMe, PendingIntent.FLAG_CANCEL_CURRENT);
                    AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarm.cancel(remindmePendingIntent);
                    Toast.makeText(mainActivityInstance, "You will not be reminded to add expense anymore!", Toast.LENGTH_LONG).show();
                }
                break;
            }
            case R.id.help : {
                Intent helpIntent = new Intent(mainActivityInstance,HelpActiivty.class);
                startActivity(helpIntent);
                break;
            }
        }
        return true;
    }

    private void setData(int count, float range) {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        float percentage;
        Drawable tempDrawable;
        int tempIndex;
        if(currentDetailsView.equalsIgnoreCase("expense")) {
            amountChartArray.clear();

            if (amountEArray.size() > 0) {
                catgChartArray.clear();
                for (int i = 0; i < amountEArray.size(); i++) {
                    tempIndex = catgChartArray.indexOf(catgEArray.get(i));
                    if (tempIndex >= 0) {
                        amountChartArray.set(tempIndex, amountChartArray.get(tempIndex) + amountEArray.get(i));
                    } else {
                        amountChartArray.add(amountEArray.get(i));
                        catgChartArray.add(catgEArray.get(i));
                    }
                }
            }
            // NOTE: The order of the entries when being added to the entries array determines their position around the center of
            // the chart.
            if (amountChartArray.size() > 0) {
                for (int i = 0; i < amountChartArray.size(); i++) {
                    percentage = (((float) amountChartArray.get(i) / totalExpense) * 100);
                    try {
                        tempDrawable = getResources().getDrawable(catgAllDrawableHashmap.get(catgChartArray.get(i)));
                    } catch (NullPointerException e) {
                        tempDrawable = getResources().getDrawable(R.drawable.catg_custom);
                    }
                    entries.add(new PieEntry(percentage, catgChartArray.get(i), tempDrawable));
                }
            } else {
                entries.add(new PieEntry(100.0f,
                        "No records",
                        getResources().getDrawable(R.mipmap.norecords)));
            }

            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setDrawIcons(true);
            dataSet.setSliceSpace(3f);
            dataSet.setIconsOffset(new MPPointF(0, 40));
            dataSet.setSelectionShift(5f);

            // add a lot of colors
            ArrayList<Integer> colors = new ArrayList<Integer>();
            int[] COLORFUL_COLORS = {
                    Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0),
                    Color.rgb(106, 150, 31), Color.rgb(179, 100, 53), Color.rgb(64, 89, 128), Color.rgb(149, 165, 124), Color.rgb(217, 184, 162),
                    Color.rgb(191, 134, 134), Color.rgb(179, 48, 80)
            };
            for (int c : COLORFUL_COLORS)
                colors.add(c);

            colors.add(ColorTemplate.getHoloBlue());

            dataSet.setColors(colors);
            //dataSet.setSelectionShift(0f);

            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.BLACK);
            data.setValueTypeface(gothamBold);
            mChart.setData(data);

            // undo all highlights
            mChart.highlightValues(null);
            mChart.invalidate();
        }
        int xtemp = (totalIncome-totalExpense);
        //SETTING THE BALANCE
        SpannableString ssss = new SpannableString("Balance = ₹ "+xtemp+"\nPer day Expensable-balance = ₹ "+(totalMonthlyIncome-totalMonthlyExpense)/((cal.getActualMaximum(Calendar.DAY_OF_MONTH)-cal.get(Calendar.DAY_OF_MONTH))+1));
        ssss.setSpan(new RelativeSizeSpan(1.1f), 0, 12+(String.valueOf(xtemp).length()), 0);
        ssss.setSpan(Typeface.BOLD_ITALIC, 0, 12+(String.valueOf(xtemp).length()), 0);
         ssss.setSpan(new RelativeSizeSpan(0.8f), 13+(String.valueOf(xtemp).length()), ssss.length(), 0);
        ssss.setSpan(new StyleSpan(Typeface.ITALIC), 13+(String.valueOf(xtemp).length()), ssss.length(), 0);
        ssss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.LightGrey)), 13+(String.valueOf(xtemp).length()), ssss.length(), 0);
        btnBalanceMid.setText(ssss);
        mChart.setCenterText(generateCenterSpannableText());
        totalExpense = 0;
        totalIncome = 0;
        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        //listOfBalance.setAdapter(new ListViewCustomAdapter(this, your_array_list,prgmImages));
        // preparing list data
        prepareListData();

          // Listview Group click listener
        listOfBalance.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                if(isMenuVisibleOrNot == true) {
                    closeTheExpenseDateMenu();
                }
                return false;
            }
        });

        // Listview Group expanded listener
        listOfBalance.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        // Listview Group collasped listener
        listOfBalance.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });

        // Listview on child click listener
        listOfBalance.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        final int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                if(isMenuVisibleOrNot == true) {
                    closeTheExpenseDateMenu();
                }
                final String todo[] = {"Edit your data","Delete your data"};
                final String category;
                final String[] splitDataArr = listDataChild.get(
                        listDataHeader.get(groupPosition)).get(
                        childPosition).split("-@-");
                String tempData;
                final String amount = splitDataArr[1];
                final String date = splitDataArr[0];
                final String paymentType,shortNotes;
                if(currentSortingOption==0){ //FOR CATEGORY GROUPING
                    category = listDataHeader.get(groupPosition).split(" ")[0]; //SPLITTING FOR GETTING THE CORRECT CATEGORY
                }
                else{
                    category = splitDataArr[4];
                }
                try{
                   tempData =  splitDataArr[2];
                }
                catch (ArrayIndexOutOfBoundsException e){
                    tempData = "";
                }
                paymentType = tempData;
               try{
                    tempData = splitDataArr[3];
                }
                catch (ArrayIndexOutOfBoundsException e){
                    tempData = "";
                }
                shortNotes = tempData;

                /*Toast.makeText(MainActivity.this, ""+listDataChild.get(
                        listDataHeader.get(groupPosition)).get(
                        childPosition), Toast.LENGTH_SHORT).show();*/

                AlertDialog.Builder editDelData = new AlertDialog.Builder(mainActivityInstance);
                editDelData.setItems(todo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) { //EDIT THE ACCOUNT DATA OPTION
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            Date tempSelectedDate = new Date();
                            try {
                                tempSelectedDate = df.parse(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Calendar currntCaldrInst = Calendar.getInstance();
                            currntCaldrInst.setTimeInMillis(tempSelectedDate.getTime());

                            fragmentManager = getSupportFragmentManager();
                            fragmentTransaction = fragmentManager.beginTransaction();

                            if(currentDetailsView.equalsIgnoreCase("expense")){
                                myFragment = AddAccFragment.newInstanceFromDataScreen("debit",currntCaldrInst,amount,paymentType,shortNotes,category);
                            }
                            else{
                                myFragment = AddAccFragment.newInstanceFromDataScreen("credit",currntCaldrInst,amount,paymentType,shortNotes,category);
                            }
                            fragmentTransaction.replace(R.id.subLayout,myFragment);
                            fragmentTransaction.commit();
                            mainLayout.setVisibility(View.GONE);
                            subLayout.setVisibility(View.VISIBLE);
                            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        }
                        else{ //DELETE THE ACCOUNT DATA OPTION
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mainActivityInstance);
                            String alertMsg = "Are you sure to delete your <b>"+currentDetailsView+"</b> data of Amount : <b>₹ "+amount+" </b>( <i>Catg : "+category +"</i> ) ?";
                            alertDialogBuilder.setMessage(Html.fromHtml(alertMsg));
                            alertDialogBuilder.setPositiveButton("yes",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            boolean successDel;
                                            if(currentDetailsView.equalsIgnoreCase("expense")){
                                                 successDel = expenseAccDb.deleteAccData(amount,category,date,paymentType,shortNotes);
                                            }
                                            else{
                                                 successDel = incomeAccDb.deleteAccData(amount,category,date,shortNotes);
                                            }
                                            //CHECKING FOR SUCCESSFULL DELETION AND REFRESHING THE DATAS
                                            if(successDel){
                                                Toast.makeText(MainActivity.this, "Successfully deleted!", Toast.LENGTH_SHORT).show();
                                                retrieveAccDatas(dataDateStart,dataDateEnd);
                                                setData(0, 0);
                                                //prepareListData();
                                            }
                                            else{
                                                Toast.makeText(MainActivity.this, "Sorry! Some problem in deleting.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                            alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    }
                });
                editDelData.create();
                editDelData.show();
                return false;
            }
        });
    }

    private SpannableString generateCenterSpannableText() {
        SpannableString s = new SpannableString("Expense - ₹ "+totalExpense+"\nIncome - ₹ "+totalIncome);
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 7, 0);
        int tempval = 11+(String.valueOf(totalExpense)).length() + 1;
        s.setSpan(new RelativeSizeSpan(1.7f), tempval, tempval+7, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - ((String.valueOf(totalIncome)).length()+4), s.length(), 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), 10, 10+(String.valueOf(totalExpense)).length()+2, 0);
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimaryDark)), s.length() - ((String.valueOf(totalIncome)).length()+4), s.length(), 0);
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimaryDark)), 10, 10+(String.valueOf(totalExpense)).length()+2, 0);
        return s;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null) return;
        if(catgEArray.size()>0) {
            currentDetailsView = "expense";
            expenseDetailsTabClick.setTextColor(getResources().getColor(R.color.White));
            incomeDetailsTabClick.setTextColor(getResources().getColor(R.color.LightSeaGreen));
            expenseDetailsTabClickSelector.setBackgroundColor(getResources().getColor(R.color.White));
            incomeDetailsTabClickSelector.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        else if(catgIArray.size()>0){
            currentDetailsView = "income";
            expenseDetailsTabClick.setTextColor(getResources().getColor(R.color.LightSeaGreen));
            incomeDetailsTabClick.setTextColor(getResources().getColor(R.color.White));
            expenseDetailsTabClickSelector.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            incomeDetailsTabClickSelector.setBackgroundColor(getResources().getColor(R.color.White));
        }
            chartClickedCatgIndex = Math.round(h.getX()); //Setting the index of the selected chart
            mChart.spin(1000, mChart.getRotationAngle(), mChart.getRotationAngle() + 360, Easing.EasingOption.EaseInBounce);
            prepareListData();
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        if(isMenuVisibleOrNot == true)
        {
            closeTheExpenseDateMenu();
            //Log.i("VAL SELECTED", "Value: " + e.getY() + ", index: " + h.getX() + ", DataSet index: " + h.getDataSetIndex());
        }
    }

    @Override
    public void onNothingSelected() {
        //Log.i("PieChart", "nothing selected");
    }

    public String getCurrentDateAndTime(int nextVal, boolean resetCalendar)
    {
        if(resetCalendar == true)
        {
            cal = Calendar.getInstance();
        }
        String mydate;
        SimpleDateFormat formater = new SimpleDateFormat("dd MMMM, EEEE");
        cal.add(Calendar.DATE, nextVal);
        return formater.format(cal.getTime());
    }

    public String getCurrentMonth(int nextVal, boolean resetCalendar)
    {
        if(resetCalendar == true)
        {
            cal = Calendar.getInstance();
        }
        String mydate;
        SimpleDateFormat formater = new SimpleDateFormat("MMMM yyyy");
        cal.add(Calendar.MONTH, nextVal);
        return formater.format(cal.getTime());
    }

    public String getCurrentYear(int nextVal, boolean resetCalendar)
    {
        if(resetCalendar == true)
        {
            cal = Calendar.getInstance();
        }
        String mydate;
        SimpleDateFormat formater = new SimpleDateFormat("yyyy");
        cal.add(Calendar.YEAR, nextVal);
        return "YEAR - "+formater.format(cal.getTime());
    }

    public String getCurrentWeekAndDate(int nextVal,boolean resetCalendar)
    {
        if(resetCalendar == true)
        {
            cal = Calendar.getInstance();
        }
        SimpleDateFormat formater = new SimpleDateFormat("dd MMM, yyyy");
        cal.add(Calendar.WEEK_OF_YEAR,nextVal);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        String week = "Week "+ (cal.get(Calendar.WEEK_OF_YEAR))+"\n("+formater.format(cal.getTime());
        cal.add(Calendar.DATE, 6);
        week+=" - "+formater.format(cal.getTime())+")";
        cal.add(Calendar.DATE, -6);
        return week;
    }

    //INITIALIZE ALL UI ELEMENTS
    public void initMyUI() {
        showTipsMain = getSharedPreferences("temp", Context.MODE_PRIVATE);
        showFragmentTips = getSharedPreferences("tempfrag", Context.MODE_PRIVATE);
        remainderTypePreference = getSharedPreferences("remainderData", Context.MODE_PRIVATE);
        remindMePreference = getSharedPreferences("remindMeData", Context.MODE_PRIVATE);

        txtCredit = (TextView) findViewById(R.id.textCredit);
        txtDebit = (TextView) findViewById(R.id.textDebit);
        txtCreditMin = (TextView) findViewById(R.id.textCreditMin);
        txtDebitMin = (TextView) findViewById(R.id.textDebitMin);
        txtDate = (TextView) findViewById(R.id.textDate);

        //Calendar View Options
        txtCalendar = (TextView) findViewById(R.id.textCalendar);
        txtDay = (TextView) findViewById(R.id.textDay);
        txtMonth = (TextView) findViewById(R.id.textMonth);
        txtWeek = (TextView) findViewById(R.id.textWeek);
        txtYear = (TextView) findViewById(R.id.textYear);

        groupBySpinnerLabel = (TextView) findViewById(R.id.txtLabel);

        btnBalanceLeft = (ImageView) findViewById(R.id.btnBalanceLeft);
        btnBalanceRight = (ImageView) findViewById(R.id.btnBalanceRight);
        btnBalanceMid = (TextView) findViewById(R.id.txtBalance);

        expenseDetailsTabClick = (TextView) findViewById(R.id.expenseDetailsTab);
        incomeDetailsTabClick = (TextView) findViewById(R.id.incomeDetailsTab);
        expenseDetailsTabClickSelector = (TextView) findViewById(R.id.expenseDetailsTabSelector);
        incomeDetailsTabClickSelector = (TextView) findViewById(R.id.incomeDetailsTabSelector);
        noDataErrs = (TextView) findViewById(R.id.noDataErr);

        final String [] spinnerArray = {"Category name","Amount of transaction","Date of Transaction"};
        sortingSpinner = (Spinner) findViewById(R.id.spinnerSort);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.accnt_spinner_textview, spinnerArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(R.layout.accnt_spinner_textview);
        sortingSpinner.setAdapter(spinnerArrayAdapter);
        sortingSpinner.setSelection(0);

        //Setting the background and text color of the selected calendar view
        //Storing the slected user value in app
        if(myPreference.getCurrentDateView().equalsIgnoreCase("year")){
            setActive(txtYear);
            txtDate.setText(getCurrentYear(0,true));
        }
        else if(myPreference.getCurrentDateView().equalsIgnoreCase("month")){
            setActive(txtMonth);
            txtDate.setText(getCurrentMonth(0,true));
        }
        else if(myPreference.getCurrentDateView().equalsIgnoreCase("week")){
            setActive(txtWeek);
            txtDate.setText(getCurrentWeekAndDate(0,true));
        }
        else {
            if(myPreference.getCurrentDateView().equalsIgnoreCase("")){
                myPreference.setCurrentDateView(DayView);
            }
            setActive(txtDay);
            txtDate.setText(getCurrentDateAndTime(0,true));
        }


        nextData = (ImageView) findViewById(R.id.nextMonth);
        prevData = (ImageView) findViewById(R.id.prevMonth);
        connectorLeft = (ImageView) findViewById(R.id.connectorLeft);
        connectorRight = (ImageView) findViewById(R.id.connectorRight);

        cal= Calendar.getInstance();

        fontUtils = FontUtils.getInstance(mainActivityInstance);

        expenseDateLayout = (LinearLayout) findViewById(R.id.expenseDateLayout);
        expenseDateLayout.bringToFront();

        slidingBalanceLayout = (RelativeLayout) findViewById(R.id.layoutBalance);
        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        subLayout = (RelativeLayout) findViewById(R.id.subLayout);
        chartViewLayout = (RelativeLayout) findViewById(R.id.chartShow);

        bottomLayout = (LinearLayout) findViewById(R.id.bottomLayout);
        bottomLayoutMin = (LinearLayout) findViewById(R.id.bottomLayoutMin);

        // Initially hide the content view.
        bottomLayoutMin.setVisibility(View.GONE);

        // Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        listOfBalance = (ExpandableListView) findViewById(R.id.slidingView);

         MMUtils.startAnimate(btnBalanceMid,R.anim.animateslider,mainActivityInstance);

        if(showTipsMain.getAll().size()==0) {
            editor = showTipsMain.edit();
            editor.putBoolean("isShown", true);
            editor.commit();

            presentShowcaseView(); //Startup show tips for the first time
        }

        //SETTING DEFAULT ALERT TYPE
        if(remainderTypePreference.getAll().size()==0){
            editor = remainderTypePreference.edit();
            editor.putString("alertType", "noalert");
            editor.putString("alertTime", "");
            editor.commit();
        }

        //SETTING DEFAULT REMIND ME TYPE
        if(remindMePreference.getAll().size()==0){
            editor = remindMePreference.edit();
            editor.putBoolean("isRemindMe", false);
            editor.commit();
        }
      }

    //STORE THE TYPE OF CALENDAR VIEW IN SHARED PREFERENCES
    public void setCurrentCalendarView(String currentViewData)
    {
        String prevView = myPreference.getCurrentDateView();
        if(prevView != currentViewData) {
            if(prevView.equalsIgnoreCase(DayView) || prevView.equalsIgnoreCase("")) {
                setInActive(txtDay);
            }
            else
            if(prevView.equalsIgnoreCase(MonthView)) {
                setInActive(txtMonth);
            }
            else
            if(prevView.equalsIgnoreCase(YearView)) {
                setInActive(txtYear);
            }
            else
            if(prevView.equalsIgnoreCase(WeekView)) {
                setInActive(txtWeek);
            }
            else
            if(prevView.equalsIgnoreCase(DateView)) {
                setInActive(txtCalendar);
            }

            myPreference.setCurrentDateView(currentViewData);

            //For removing the default calendar view i.e. day , in case it is not removed due to slow execution issues
            if(isActive(txtDay)) {
                setInActive(txtDay);
            }

            if(currentViewData.equalsIgnoreCase(DayView)) {
                setActive(txtDay);
            }
            else
            if(currentViewData.equalsIgnoreCase(MonthView)) {
                setActive(txtMonth);
            }
            else
            if(currentViewData.equalsIgnoreCase(YearView)) {
                setActive(txtYear);
            }
            else
            if(currentViewData.equalsIgnoreCase(WeekView)) {
                setActive(txtWeek);
            }
            else
            if(currentViewData.equalsIgnoreCase(DateView)) {
                setActive(txtCalendar);
            }
        }
    }

    //SET THE VIEW ACTIVE BY CHANGING THE BACKGROUND AND TEXT COLOR
    public void setActive(TextView targetActive)
    {
        targetActive.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        targetActive.setTextColor(getResources().getColor(R.color.White));
    }

    //CHECK THE VIEW IS ACTIVE OR NOT
    public boolean isActive(TextView targetCheck)
    {
        if(targetCheck.getCurrentTextColor() == getResources().getColor(R.color.White))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //SET THE VIEW NON-ACTIVE BY CHANGING THE BACKGROUND AND TEXT COLOR
    public void setInActive(TextView targetInactive)
    {
        targetInactive.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        targetInactive.setTextColor(getResources().getColor(R.color.Black));
    }

    //CHECK THE SELECTED DATE REACHED CURRENT DATE OR NOT
    public void hasNotReachedCurrentDate()
    {
        if(myPreference.getCurrentDateView().equalsIgnoreCase(DayView)) {
            SimpleDateFormat formatr = new SimpleDateFormat("ddMMyyyy");
            String formattedSelectedDate = formatr.format(cal.getTime()).toString();
            String formattedCurrentDate = formatr.format(Calendar.getInstance().getTime()).toString();
            if(Integer.parseInt(formattedSelectedDate.substring(2)) >= Integer.parseInt(formattedCurrentDate.substring(2))) {
                if(Integer.parseInt(formattedSelectedDate.substring(2)) == Integer.parseInt(formattedCurrentDate.substring(2))) {
                    if(Integer.parseInt(formattedSelectedDate.substring(0,2)) >= Integer.parseInt(formattedCurrentDate.substring(0,2))) {
                        blockNextButton();
                    }
                    else
                    {
                        unblockNextButton();
                    }
                }
                else
                {
                    blockNextButton();
                }
            }
            else {
                unblockNextButton();
            }
        }
        else
        if(myPreference.getCurrentDateView().equalsIgnoreCase(WeekView)) {
            if(cal.get(Calendar.WEEK_OF_YEAR) >= Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)) {
                blockNextButton();
            }
            else {
                unblockNextButton();
            }
        }
        else
        if(myPreference.getCurrentDateView().equalsIgnoreCase(MonthView)) {
            if(cal.get(Calendar.MONTH) >= Calendar.getInstance().get(Calendar.MONTH)) {
                blockNextButton();
            }
            else {
                unblockNextButton();
            }
        }
        else
        if(myPreference.getCurrentDateView().equalsIgnoreCase(YearView)) {
            if(cal.get(Calendar.YEAR) >= Calendar.getInstance().get(Calendar.YEAR)) {
                blockNextButton();
            }
            else {
                unblockNextButton();
            }
        }
    }

    //BLOCKING AND UNBLOCKING NEXT BUTTON
    public void blockNextButton()
    {
        isNextBlocked = true;
        nextData.setImageResource(R.mipmap.nextblock);
    }

    public void unblockNextButton()
    {
        isNextBlocked = false;
        nextData.setImageResource(R.mipmap.next);
    }

    //INITIALIZE ALL PIE CHART ELEMENTS
    public void intiCharts() {
        mChart = (PieChart) findViewById(R.id.chart1);
        mChart.setUsePercentValues(false);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);
        mChart.setDragDecelerationFrictionCoef(0.95f);
        mChart.setCenterTextTypeface(gothamLight);
        mChart.setCenterText(generateCenterSpannableText());
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);
        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);
        mChart.setHoleRadius(58f);        mChart.setTransparentCircleRadius(61f);
        mChart.setDrawCenterText(true);
        mChart.setRotationAngle(0);

        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);

        setData(4, 100);

        mChart.animateY(1400, Easing.EasingOption.EaseInCubic);
        mChart.getLegend().setEnabled(false);

        // entry label styling
        mChart.setEntryLabelColor(Color.BLACK);
        mChart.setEntryLabelTypeface(gothamMedium);
        mChart.setEntryLabelTextSize(12f);
    }

        //SET FONTS TO THE TEXT
    public void setFonts() {
        fontUtils.setFont(txtCredit, FontUtils.GothamHTF_Bold);
        fontUtils.setFont(txtDebit, FontUtils.GothamHTF_Bold);
        //fontUtils.setFont(btnBalanceMid, FontUtils.GothamHTF_Medium);
        fontUtils.setFont(txtDate, FontUtils.GothamHTF_Medium);

        fontUtils.setFont(txtCalendar, FontUtils.GothamHTF_Light);
        fontUtils.setFont(txtDay, FontUtils.GothamHTF_Light);
        fontUtils.setFont(txtWeek, FontUtils.GothamHTF_Light);
        fontUtils.setFont(txtMonth, FontUtils.GothamHTF_Light);
        fontUtils.setFont(txtYear, FontUtils.GothamHTF_Light);
    }

    //SET ALL EVENTS ON THE ELEMENTS
    public void setMyEvents()
    {
        txtCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(isMenuVisibleOrNot==false) {
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    myFragment = AddAccFragment.newInstance("Credit",cal);
                    fragmentTransaction.replace(R.id.subLayout,myFragment);
                    fragmentTransaction.commit();
                    mainLayout.setVisibility(View.GONE);
                    subLayout.setVisibility(View.VISIBLE);
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    closeTheExpenseDateMenu();
                /*}
                else {
                    closeTheExpenseDateMenu();
                }*/
            }
        });

        txtCreditMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(isMenuVisibleOrNot==false) {
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                myFragment = AddAccFragment.newInstance("Credit",cal);
                fragmentTransaction.replace(R.id.subLayout,myFragment);
                fragmentTransaction.commit();
                mainLayout.setVisibility(View.GONE);
                subLayout.setVisibility(View.VISIBLE);
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                closeTheExpenseDateMenu();
                /*}
                else {
                    closeTheExpenseDateMenu();
                }*/
            }
        });

        txtDebit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // if(isMenuVisibleOrNot==false) {
                    //Pass no of parameters accordingly
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    myFragment = AddAccFragment.newInstance("Debit",cal);
                    fragmentTransaction.replace(R.id.subLayout,myFragment);
                    fragmentTransaction.commit();
                    mainLayout.setVisibility(View.GONE);
                    subLayout.setVisibility(View.VISIBLE);
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    closeTheExpenseDateMenu();
                /* }
                else{
                    closeTheExpenseDateMenu();
                }*/

            }
        });

        txtDebitMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if(isMenuVisibleOrNot==false) {
                //Pass no of parameters accordingly
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                myFragment = AddAccFragment.newInstance("Debit",cal);
                fragmentTransaction.replace(R.id.subLayout,myFragment);
                fragmentTransaction.commit();
                mainLayout.setVisibility(View.GONE);
                subLayout.setVisibility(View.VISIBLE);
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                closeTheExpenseDateMenu();
                /* }
                else{
                    closeTheExpenseDateMenu();
                }*/

            }
        });

        nextData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMenuVisibleOrNot==false && isNextBlocked==false) {
                    mChart.spin(1000, mChart.getRotationAngle(), mChart.getRotationAngle() + 360, Easing.EasingOption
                            .EaseInCubic);
                    MMUtils.startAnimate(txtDate,R.anim.dateanimate,mainActivityInstance);
                    MMUtils.startAnimate(connectorLeft,R.anim.dateanimate,mainActivityInstance);
                    MMUtils.startAnimate(connectorRight,R.anim.dateanimate,mainActivityInstance);
                    if(myPreference.getCurrentDateView().equalsIgnoreCase(DayView) || myPreference.getCurrentDateView().equalsIgnoreCase(DayView)) {
                        txtDate.setText(getCurrentDateAndTime(1,false));
                        getDbDatas();
                    }
                    else
                    if(myPreference.getCurrentDateView().equalsIgnoreCase(WeekView))
                    {
                        txtDate.setText(getCurrentWeekAndDate(1,false));
                        getDbDatas();
                    }
                    else
                    if(myPreference.getCurrentDateView().equalsIgnoreCase(MonthView))
                    {
                        txtDate.setText(getCurrentMonth(1,false));
                        getDbDatas();
                    }
                    else
                    if(myPreference.getCurrentDateView().equalsIgnoreCase(YearView))
                    {
                        txtDate.setText(getCurrentYear(1,false));
                        getDbDatas();
                    }
                    hasNotReachedCurrentDate();
                }
            }
        });

        prevData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMenuVisibleOrNot==false) {
                    mChart.spin(1000, mChart.getRotationAngle(), mChart.getRotationAngle() - 360, Easing.EasingOption
                            .EaseInCubic);
                    MMUtils.startAnimate(txtDate,R.anim.dateanimate,mainActivityInstance);
                    MMUtils.startAnimate(connectorLeft,R.anim.dateanimate,mainActivityInstance);
                    MMUtils.startAnimate(connectorRight,R.anim.dateanimate,mainActivityInstance);
                    if(myPreference.getCurrentDateView().equalsIgnoreCase(DayView) || myPreference.getCurrentDateView().equalsIgnoreCase(DayView)) {
                        txtDate.setText(getCurrentDateAndTime( -1,false));
                        getDbDatas();
                    }
                    else
                    if(myPreference.getCurrentDateView().equalsIgnoreCase(WeekView))
                    {
                        txtDate.setText(getCurrentWeekAndDate(-1,false));
                        getDbDatas();
                    }
                    else
                    if(myPreference.getCurrentDateView().equalsIgnoreCase(MonthView))
                    {
                        txtDate.setText(getCurrentMonth(-1,false));
                        getDbDatas();
                    }
                    else
                    if(myPreference.getCurrentDateView().equalsIgnoreCase(YearView))
                    {
                        txtDate.setText(getCurrentYear(-1,false));
                        getDbDatas();
                    }
                    hasNotReachedCurrentDate();
                }
            }
        });

        txtCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActive(txtCalendar);
                closeTheExpenseDateMenu();

                //OPENING DATEPICKER
                DialogDatepickerFragment dialogDatepickerFragment = DialogDatepickerFragment.getInstance();
                dialogDatepickerFragment.show(getSupportFragmentManager(), "");
            }
        });

        txtYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //BLOCKING THE EXECUTION IF THE VIEW IS ALREADY ACTIVE
                if(!(isActive(txtYear))) {
                    setCurrentCalendarView(YearView);
                    subLayout.setVisibility(View.GONE);
                    mainLayout.setVisibility(View.VISIBLE);
                    txtDate.setText(getCurrentYear(0,true));
                    hasNotReachedCurrentDate();
                    MMUtils.startAnimate(txtDate,R.anim.dateanimate,mainActivityInstance);
                    MMUtils.startAnimate(connectorLeft,R.anim.dateanimate,mainActivityInstance);
                    MMUtils.startAnimate(connectorRight,R.anim.dateanimate,mainActivityInstance);
                    getDbDatas();
                }
                closeTheExpenseDateMenu();
            }
        });

        txtMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(isActive(txtMonth))) {
                    setCurrentCalendarView(MonthView);
                    subLayout.setVisibility(View.GONE);
                    mainLayout.setVisibility(View.VISIBLE);
                    txtDate.setText(getCurrentMonth(0, true));
                    hasNotReachedCurrentDate();
                    MMUtils.startAnimate(txtDate,R.anim.dateanimate,mainActivityInstance);
                    MMUtils.startAnimate(connectorLeft,R.anim.dateanimate,mainActivityInstance);
                    MMUtils.startAnimate(connectorRight,R.anim.dateanimate,mainActivityInstance);
                    getDbDatas();
                }
                closeTheExpenseDateMenu();
            }
        });

        txtWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(isActive(txtWeek))) {
                    setCurrentCalendarView(WeekView);
                    subLayout.setVisibility(View.GONE);
                    mainLayout.setVisibility(View.VISIBLE);
                    txtDate.setText(getCurrentWeekAndDate(0, true));
                    hasNotReachedCurrentDate();
                    MMUtils.startAnimate(txtDate,R.anim.dateanimate,mainActivityInstance);
                    MMUtils.startAnimate(connectorLeft,R.anim.dateanimate,mainActivityInstance);
                    MMUtils.startAnimate(connectorRight,R.anim.dateanimate,mainActivityInstance);
                    getDbDatas();
                }
                closeTheExpenseDateMenu();
            }
        });

        txtDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(isActive(txtDay))) {
                    setCurrentCalendarView(DayView);
                    subLayout.setVisibility(View.GONE);
                    mainLayout.setVisibility(View.VISIBLE);
                    txtDate.setText(getCurrentDateAndTime(0, true));
                    hasNotReachedCurrentDate();
                    MMUtils.startAnimate(txtDate,R.anim.dateanimate,mainActivityInstance);
                    MMUtils.startAnimate(connectorLeft,R.anim.dateanimate,mainActivityInstance);
                    MMUtils.startAnimate(connectorRight,R.anim.dateanimate,mainActivityInstance);
                    getDbDatas();
                }
                closeTheExpenseDateMenu();
            }
        });

        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                //Log.i(SLIDINGTAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
               // Log.i(SLIDINGTAG, "onPanelStateChanged " + newState);
                if(isMenuVisibleOrNot == true) {
                    closeTheExpenseDateMenu();
                }
                if(newState == SlidingUpPanelLayout.PanelState.EXPANDED) {

                   // Set the content view to 0% opacity but visible, so that it is visible
                    // (but fully transparent) during the animation.
                    mChart.highlightValues(null);
                    bottomLayout.setAlpha(1f);
                    bottomLayoutMin.setAlpha(0f);
                    bottomLayoutMin.setVisibility(View.VISIBLE);
                    bottomLayoutMin.animate()
                            .alpha(1f)
                            .setDuration(mShortAnimationDuration)
                            .setListener(null);

                    bottomLayout.setVisibility(View.GONE);
                    bottomLayout.animate()
                            .alpha(0f)
                            .setDuration(mShortAnimationDuration);

                    MMUtils.startAnimate(btnBalanceMid,R.anim.animateslider,mainActivityInstance);
                 }
                else
                if(newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    bottomLayout.setAlpha(0f);
                    bottomLayout.setVisibility(View.VISIBLE);
                    bottomLayout.animate()
                            .alpha(1f)
                            .setDuration(mShortAnimationDuration)
                            .setListener(null);

                    bottomLayoutMin.animate()
                            .alpha(0f)
                            .setDuration(mShortAnimationDuration);
                    bottomLayoutMin.setVisibility(View.GONE);
                    }
            }
        });

        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        listOfBalance.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(mainActivityInstance, "onItemClick", Toast.LENGTH_SHORT).show();
                closeTheExpenseDateMenu();
            }
        });

        expenseDetailsTabClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentDetailsView.equalsIgnoreCase("income")) {
                    currentDetailsView = "expense";
                    expenseDetailsTabClick.setTextColor(getResources().getColor(R.color.White));
                    incomeDetailsTabClick.setTextColor(getResources().getColor(R.color.LightSeaGreen));
                    expenseDetailsTabClickSelector.setBackgroundColor(getResources().getColor(R.color.White));
                    incomeDetailsTabClickSelector.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    prepareListData();
                }
            }
        });

        incomeDetailsTabClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentDetailsView.equalsIgnoreCase("expense")) {
                    currentDetailsView = "income";
                    expenseDetailsTabClick.setTextColor(getResources().getColor(R.color.LightSeaGreen));
                    incomeDetailsTabClick.setTextColor(getResources().getColor(R.color.White));
                    expenseDetailsTabClickSelector.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    incomeDetailsTabClickSelector.setBackgroundColor(getResources().getColor(R.color.White));
                    prepareListData();
                }
            }
        });

        listOfBalance.setOnTouchListener(new OnSwipeTouchListener(mainActivityInstance) {
             public void onSwipeRight() {
                if(currentDetailsView.equalsIgnoreCase("income")){
                    currentDetailsView = "expense";
                    expenseDetailsTabClick.setTextColor(getResources().getColor(R.color.White));
                    incomeDetailsTabClick.setTextColor(getResources().getColor(R.color.LightSeaGreen));
                    expenseDetailsTabClickSelector.setBackgroundColor(getResources().getColor(R.color.White));
                    incomeDetailsTabClickSelector.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    prepareListData();
                }
            }
            public void onSwipeLeft() {
                if(currentDetailsView.equalsIgnoreCase("expense")){
                    currentDetailsView = "income";
                    expenseDetailsTabClick.setTextColor(getResources().getColor(R.color.LightSeaGreen));
                    incomeDetailsTabClick.setTextColor(getResources().getColor(R.color.White));
                    expenseDetailsTabClickSelector.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    incomeDetailsTabClickSelector.setBackgroundColor(getResources().getColor(R.color.White));
                    prepareListData();
                }
            }
        });

        sortingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED){
                   currentSortingOption = position;
                    prepareListData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // TO CLOSE THE UPPER EXPENSE DATE MENU
    public void closeTheExpenseDateMenu()
    {
        isMenuVisibleOrNot = false;
        if(closeMenuAnime == null)
        {closeMenuAnime = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.moveup);}
        closeMenuAnime.setFillAfter(true);
        expenseDateLayout.startAnimation(closeMenuAnime);
        closeMenuAnime.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                expenseDateLayout.setTop(400);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        closeMenuAnime = null;
        expenseDateLayout.setVisibility(View.GONE);
    }

    //Datepicker date selected
    public void onChangeDatepickerDialogDate(int year, int monthOfYear, int dayOfMonth)
    {
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear  );
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        setCurrentCalendarView(DayView);
        subLayout.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);
        txtDate.setText(getCurrentDateAndTime(0,false));
        MMUtils.startAnimate(txtDate,R.anim.dateanimate,mainActivityInstance);
        getDbDatas();
        //Checking for Next Button validation
        hasNotReachedCurrentDate();
    }

    @Override
    public void onBackPressed() {
        if(subLayout.getVisibility() == View.VISIBLE)
        {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            AddAccFragment myFragment = new AddAccFragment();
            fragmentManager.popBackStack();
            fragmentTransaction.remove(myFragment);
            fragmentTransaction.commit();
            subLayout.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);
            mainMenu.findItem(R.id.moreoptions).setVisible(true);
            mainMenu.findItem(R.id.saveData).setVisible(false);
            mainMenu.findItem(R.id.remainders).setVisible(true);
            mainMenu.findItem(R.id.otheroptions).setVisible(true);
            if(isMenuVisibleOrNot == true) {
                closeTheExpenseDateMenu();
            }
        }
        else
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else
        if(isMenuVisibleOrNot == true) {
            closeTheExpenseDateMenu();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        // TODO Auto-generated method stub
        // Toast.makeText(mainActivityInstance, "date="+dayOfMonth+"/"+monthOfYear+"/"+year, Toast.LENGTH_SHORT).show();
        if(mainLayout.getVisibility() == View.VISIBLE) {
            onChangeDatepickerDialogDate(year,monthOfYear,dayOfMonth);
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    //GETTING ALL ACCOUNT DATAS AND STORING IN RESPECTIVE VARIABLES
    public void getDbDatas() {
        incomeAccDb = new IncomeAccountDBAdapter(this);
        incomeAccDb = incomeAccDb.open();

        expenseAccDb = new ExpenseAccountDBAdapter(this);
        expenseAccDb = expenseAccDb.open();

        if(catgAllDrawableHashmap.size()==0) {
            catgAllDrawableHashmap.put("Food", R.drawable.catg_food);
            catgAllDrawableHashmap.put("Travel", R.drawable.catg_transport);
            catgAllDrawableHashmap.put("Bill", R.drawable.catg_bill);
            catgAllDrawableHashmap.put("Car and bike", R.drawable.catg_carandbike);
            catgAllDrawableHashmap.put("Cloth", R.drawable.catg_clothes);
            catgAllDrawableHashmap.put("Eat Out", R.drawable.catg_eatout);
            catgAllDrawableHashmap.put("Phone", R.drawable.catg_phone);
            catgAllDrawableHashmap.put("Entertain", R.drawable.catg_entertainment);
            catgAllDrawableHashmap.put("Rent", R.drawable.catg_rental);
            catgAllDrawableHashmap.put("Gift", R.drawable.catg_gift);
            catgAllDrawableHashmap.put("Health", R.drawable.catg_health);
            catgAllDrawableHashmap.put("Pet", R.drawable.catg_pet);
            catgAllDrawableHashmap.put("Electronics", R.drawable.catg_electronics);
            catgAllDrawableHashmap.put("Sports", R.drawable.catg_sports);
            catgAllDrawableHashmap.put("Saving", R.drawable.catg_saving);
            catgAllDrawableHashmap.put("Income Tax", R.drawable.catg_tax);
            catgAllDrawableHashmap.put("Salary", R.drawable.catg_salary);
            catgAllDrawableHashmap.put("Home Rent", R.drawable.catg_homerent);
        }

        String currntView = myPreference.getCurrentDateView();

         amountIArray = new ArrayList<Integer>();
        dateOfTransIArray = new ArrayList<String>();
        shortNotesIArray = new ArrayList<String>();
        catgIArray = new ArrayList<String>();

        amountEArray = new ArrayList<Integer>();
        dateOfTransEArray = new ArrayList<String>();
        shortNotesEArray = new ArrayList<String>();
        paymentTypeEArray = new ArrayList<String>();
        catgEArray = new ArrayList<String>();

        catgChartArray = new ArrayList<String>();
        catgChartIArray = new ArrayList<String>();
        dateGroupEArray = new ArrayList<String>();
        dateGroupIArray = new ArrayList<String>();
        amountGroupEArray = new ArrayList<String>();
        amountGroupIArray = new ArrayList<String>();
        amountChartArray = new ArrayList<Integer>();

        SimpleDateFormat dateformater;
        String date,datelast;



        //FOR DIFFERENT TIME LIMITS
        switch (currntView){

            case "year":
                dateformater = new SimpleDateFormat("yyyy-MM-dd");
                date = dateformater.format(cal.getTime());
                date = date.substring(0,5)+"01-01";
                datelast = date.substring(0,5)+"12-31";
                //Toast.makeText(mainActivityInstance, ""+date+"--"+datelast, Toast.LENGTH_SHORT).show();
                retrieveAccDatas(date,datelast);
                break;

            case "week":
                dateformater = new SimpleDateFormat("yyyy-MM-dd");
                cal.set(Calendar.DAY_OF_WEEK,cal.getFirstDayOfWeek());
                Calendar tempInst = cal;
                date = dateformater.format(cal.getTime());
                tempInst.add(Calendar.DAY_OF_YEAR,6);
                datelast = dateformater.format(tempInst.getTime());
                tempInst.add(Calendar.DAY_OF_YEAR,-6);
                retrieveAccDatas(date,datelast);
                break;

            case "month":
                dateformater = new SimpleDateFormat("yyyy-MM-dd");
                date = dateformater.format(cal.getTime());
                date=date.substring(0,(date.length()-2))+"01";
                datelast=date.substring(0,(date.length()-2))+String.valueOf(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                //Toast.makeText(mainActivityInstance, ""+date+"---"+datelast, Toast.LENGTH_SHORT).show();
                retrieveAccDatas(date,datelast);
            break;

            case "day":
                dateformater = new SimpleDateFormat("yyyy-MM-dd");
                date = dateformater.format(cal.getTime());
                //Toast.makeText(mainActivityInstance, ""+date+"---"+currntView+"---"+ cal.getActualMaximum(Calendar.DAY_OF_MONTH), Toast.LENGTH_SHORT).show();
                retrieveAccDatas(date,date);
            break;

            default:  //THIS ALWAYS GIVES DATAS FOR THE CURRENT DATE
                Toast.makeText(mainActivityInstance, "Something went wrong!", Toast.LENGTH_SHORT).show();
            break;
        }
        intiCharts();
    }

    public void retrieveAccDatas(String dateStart, String dateEnd){
        String [] tempIDatas, tempEDatas;
        dataDateStart = dateStart;
        dataDateEnd = dateEnd;

        expenseDataArray = expenseAccDb.getAccountDetails(dateStart,dateEnd);
        incomeDataArray = incomeAccDb.getAccountDetails(dateStart, dateEnd);

        if(isMenuVisibleOrNot == true) {
            closeTheExpenseDateMenu();
        }

        amountIArray.clear();
        totalIncome = 0;
        dateOfTransIArray.clear();
        shortNotesIArray.clear();
        catgIArray.clear();
        if(incomeDataArray.size()>0) {
            for(int x=0;x<incomeDataArray.size();x++){
                tempIDatas = (incomeDataArray.get(x)).split("-#-");
                amountIArray.add(x,Integer.parseInt(tempIDatas[0]));
                totalIncome+=amountIArray.get(x);
                dateOfTransIArray.add(x,tempIDatas[1]);
                shortNotesIArray.add(x,tempIDatas[2]);
                catgIArray.add(x,tempIDatas[3]);
            }
        }

        amountEArray.clear();
        totalExpense = 0;
        dateOfTransEArray.clear();
        shortNotesEArray.clear();
        catgEArray.clear();
        if(expenseDataArray.size()>0){
            for(int x=0;x<expenseDataArray.size();x++){
                tempEDatas = expenseDataArray.get(x).split("-#-");
               amountEArray.add(x,Integer.parseInt(tempEDatas[0]));
                totalExpense+=amountEArray.get(x);
                dateOfTransEArray.add(x,tempEDatas[1]);
                shortNotesEArray.add(x,tempEDatas[2]);
                paymentTypeEArray.add(x,tempEDatas[3]);
                catgEArray.add(x, tempEDatas[4]);
            }
        }

        //GETTING TOTAL MONTH EXPEMSE AND INCOME
        totalMonthlyExpense = 0;
        totalMonthlyIncome = 0;
        SimpleDateFormat tempdateformater = new SimpleDateFormat("yyyy-MM-dd");
        String datestrt = tempdateformater.format(cal.getTime());
        datestrt=datestrt.substring(0,(datestrt.length()-2))+"01";
        String dateend=datestrt.substring(0,(datestrt.length()-2))+String.valueOf(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        expenseDataArray = expenseAccDb.getAccountDetails(datestrt,dateend);
        incomeDataArray = incomeAccDb.getAccountDetails(datestrt, dateend);
         if(expenseDataArray.size()>0){
            for(int x=0;x<expenseDataArray.size();x++){
                tempEDatas = expenseDataArray.get(x).split("-#-");
                totalMonthlyExpense+=Integer.parseInt(tempEDatas[0]);
            }
        }
        if(incomeDataArray.size()>0) {
            for(int x=0;x<incomeDataArray.size();x++){
                tempIDatas = (incomeDataArray.get(x)).split("-#-");
                totalMonthlyIncome+=Integer.parseInt(tempIDatas[0]);
           }
        }

        /*if(totalMonthlyExpense>totalMonthlyIncome){

        }*/
    }

    /*
	 * Preparing the list data
	 */
    private void prepareListData() {
        ArrayList<String> tempDateOfTranscEArray = dateOfTransEArray;
        ArrayList<String> tempDateOfTranscIArray = dateOfTransIArray;
        ArrayList<String> finalDateOfTranscEArray = new ArrayList<String>();
        ArrayList<String> finalDateOfTranscIArray = new ArrayList<String>();
        SimpleDateFormat formater = new SimpleDateFormat("dd MMM, yyyy");

        if(isMenuVisibleOrNot == true) {
            closeTheExpenseDateMenu();
        }

         if(currentDetailsView.equalsIgnoreCase("expense")){ //EXPENSE
             if(catgEArray.size()>5){
                 mLayout.setScrollableView(listOfBalance);
             }
             else{
                 mLayout.setScrollableView(null);
             }

             if(currentSortingOption == 0) { //FOR CATEGORY GROUPING
                 int tempIndex;
                 catgChartArray.clear();
                 amountChartArray.clear();
                 if(catgEArray.size()>0){
                     for (int i = 0; i < amountEArray.size() ; i++) {
                         tempIndex =catgChartArray.indexOf(catgEArray.get(i));
                         if(tempIndex>=0){
                             amountChartArray.set(tempIndex,amountChartArray.get(tempIndex)+amountEArray.get(i));
                         }
                         else{
                             amountChartArray.add(amountEArray.get(i));
                             catgChartArray.add(catgEArray.get(i));
                         }
                     }
                 }
                 listDataHeader = catgChartArray;
             }
             else if(currentSortingOption == 1){ //FOR AMOUNT GROUPING
                  if(amountEArray.size()>0){
                      int tempAccnt, frstCount = 0, secCount =0, thrdCount = 0, fourthCount = 0;
                      for (int y = 0; y < amountEArray.size(); y++) {
                          tempAccnt = amountEArray.get(y);
                          if (tempAccnt<=500) {
                              frstCount++;
                          }
                          else if (tempAccnt>500 && tempAccnt <= 1000) {
                              secCount++;
                          }
                          else if (tempAccnt>1000 && tempAccnt <= 5000) {
                              thrdCount++;
                          }
                          else if (tempAccnt>5000) {
                              fourthCount++;
                          }
                      }
                      dateGroupEArray.clear();
                      if(frstCount>0) {
                          dateGroupEArray.add("< ₹ 500");
                      }
                      if(secCount>0) {
                          dateGroupEArray.add("₹ 501 - ₹ 1000");
                      }
                      if(thrdCount>0) {
                          dateGroupEArray.add("₹ 1001 - ₹ 5000");
                      }
                      if(fourthCount > 0) {
                          dateGroupEArray.add("> ₹ 5000");
                      }
                 }
                 listDataHeader = dateGroupEArray;
             }
             else if(currentSortingOption == 2){ //FOR DATE GROUPING
                 int tempIndex;
                 if(amountEArray.size()>0){
                     dateGroupEArray.clear();
                     for (int i = 0; i < amountEArray.size() ; i++) {
                         tempIndex =dateGroupEArray.indexOf(dateOfTransEArray.get(i));
                         if(tempIndex>=0){}
                         else{
                             dateGroupEArray.add(dateOfTransEArray.get(i));
                         }
                     }
                 }


                 for(int y = 0; y<tempDateOfTranscEArray.size();y++){
                     DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                     Date tempDate = new Date();
                     try {
                         tempDate = df.parse(tempDateOfTranscEArray.get(y));
                         finalDateOfTranscEArray.add(formater.format(tempDate));
                     } catch (ParseException e) {
                         e.printStackTrace();
                     }
                 }

                 ArrayList<Date> tempDateArray = new ArrayList<Date>();
                 for(int x=0;x<dateGroupEArray.size();x++){ //FOR DATE SORTING
                     DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                     Date tempDate = new Date();
                     try {
                         tempDate = df.parse(dateGroupEArray.get(x));
                         tempDateArray.add(tempDate);
                     } catch (ParseException e) {
                         e.printStackTrace();
                     }
                 }
                 Collections.sort(tempDateArray); //Sorting the data's by date
                 dateGroupEArray.clear();
                 for(int y=(tempDateArray.size()-1);y>=0;y--){
                     dateGroupEArray.add(formater.format(tempDateArray.get(y)));
                 }

                 listDataHeader = dateGroupEArray;
             }
        }
        else{  //INCOME

             if(catgIArray.size()>5){
                 mLayout.setScrollableView(listOfBalance);
             }
             else{
                 mLayout.setScrollableView(null);
             }

             if(currentSortingOption == 0) { //FOR CATEGORY GROUPING
                 int tempIndex;
                 catgChartIArray.clear();
                 if(catgIArray.size()>0){
                      for (int i = 0; i < catgIArray.size() ; i++) {
                         tempIndex =catgChartIArray.indexOf(catgIArray.get(i));
                         if(tempIndex>=0){}
                         else{
                             catgChartIArray.add(catgIArray.get(i));
                         }
                     }
                 }
                 listDataHeader = catgChartIArray;
             }
             else if(currentSortingOption == 1){ //FOR AMOUNT GROUPING
                 if(amountIArray.size()>0){
                       int tempAccnt, frstCount = 0, secCount =0, thrdCount = 0, fourthCount = 0;
                     for (int y = 0; y < amountIArray.size(); y++) {
                         tempAccnt = amountIArray.get(y);
                         if (tempAccnt<=500) {
                             frstCount++;
                         }
                         else if (tempAccnt>500 && tempAccnt <= 1000) {
                             secCount++;
                         }
                         else if (tempAccnt>1000 && tempAccnt <= 5000) {
                             thrdCount++;
                         }
                         else if (tempAccnt>5000) {
                             fourthCount++;
                         }
                     }
                     dateGroupIArray.clear();
                     if(frstCount>0) {
                         dateGroupIArray.add("< ₹ 500");
                     }
                     if(secCount>0) {
                         dateGroupIArray.add("₹ 501 - ₹ 1000");
                     }
                     if(thrdCount>0) {
                         dateGroupIArray.add("₹ 1001 - ₹ 5000");
                     }
                     if(fourthCount > 0) {
                         dateGroupIArray.add("> ₹ 5000");
                     }
                 }
                 listDataHeader = dateGroupIArray;
             }
             else if(currentSortingOption == 2){ //FOR DATE GROUPING
                 int tempIndex;
                 if(amountIArray.size()>0){
                     dateGroupIArray.clear();
                     for (int i = 0; i < amountIArray.size() ; i++) {
                         tempIndex =dateGroupIArray.indexOf(dateOfTransIArray.get(i));
                         if(tempIndex>=0){}
                         else{
                             dateGroupIArray.add(dateOfTransIArray.get(i));
                         }
                     }
                 }

                 for(int y = 0; y<tempDateOfTranscIArray.size();y++){
                     DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                     Date tempDate = new Date();
                     try {
                         tempDate = df.parse(tempDateOfTranscIArray.get(y));
                         finalDateOfTranscIArray.add(formater.format(tempDate));
                     } catch (ParseException e) {
                         e.printStackTrace();
                     }
                 }

                 ArrayList<Date> tempDateArray = new ArrayList<Date>();

                 for(int x=0;x<dateGroupIArray.size();x++){ //FOR DATE SORTING
                     DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                     Date tempDate = new Date();
                     try {
                         tempDate = df.parse(dateGroupIArray.get(x));
                         tempDateArray.add(tempDate);
                     } catch (ParseException e) {
                         e.printStackTrace();
                     }
                 }
                 Collections.sort(tempDateArray);
                 dateGroupIArray.clear();
                 for(int y=(tempDateArray.size()-1);y>=0;y--){
                     dateGroupIArray.add(formater.format(tempDateArray.get(y)));
                 }

                 listDataHeader = dateGroupIArray;
             }
        }
        listDataChild = new HashMap<String, List<String>>();
        if(listDataHeader.size()==0){
            sortingSpinner.setVisibility(View.GONE);
            groupBySpinnerLabel.setVisibility(View.GONE);
           if(currentDetailsView.equalsIgnoreCase("expense")){
                noDataErrs.setText("To add expense details, tap at\n [ Add Expense ] at the bottom of your screen.");
            }
            else {
                noDataErrs.setText("To add income details, tap at\n [ Add Income ] at the bottom of your screen");
            }
            noDataErrs.setVisibility(View.VISIBLE);
        }
        else{
            sortingSpinner.setVisibility(View.VISIBLE);
            groupBySpinnerLabel.setVisibility(View.VISIBLE);
            noDataErrs.setVisibility(View.GONE);
        }

        int totalCategoryAmount = 0; //SAVING EACH CATGEORY TOTAL AMOUNTS
        if(currentDetailsView.equalsIgnoreCase("expense")){  //EXPENSE
            if(currentSortingOption == 0) {  //FOR CATEGORY GROUPING
                for (int x = 0; x < listDataHeader.size(); x++) {
                    List<String> txtChildData = new ArrayList<String>();
                    totalCategoryAmount = 0;
                    for (int y = 0; y < catgEArray.size(); y++) {
                        if (catgEArray.get(y).equalsIgnoreCase(listDataHeader.get(x))) {
                            txtChildData.add(String.valueOf(dateOfTransEArray.get(y) + "-@-" + amountEArray.get(y) + "-@-" + paymentTypeEArray.get(y) + "-@-" + shortNotesEArray.get(y)));
                            totalCategoryAmount+=amountEArray.get(y);
                        }
                    }
                    listDataHeader.set(x,listDataHeader.get(x)+" (Total - ₹ "+totalCategoryAmount+")");
                    listDataChild.put(listDataHeader.get(x), txtChildData);
                    //Toast.makeText(mainActivityInstance, ""+listDataChild.get(listDataHeader.get(x)), Toast.LENGTH_SHORT).show();
                }
            }
            else if(currentSortingOption == 1){ //FOR AMOUNT GROUPING
                int tempAccnt;
                for (int x = 0; x < listDataHeader.size(); x++) {
                    List<String> txtChildData = new ArrayList<String>();
                    totalCategoryAmount = 0;
                    for (int y = 0; y < amountEArray.size(); y++) {
                        tempAccnt = amountEArray.get(y);
                        if (tempAccnt<=500 && listDataHeader.get(x).contains("< ₹ 500")) {
                            txtChildData.add(String.valueOf(dateOfTransEArray.get(y) + "-@-" + amountEArray.get(y) + "-@-" + paymentTypeEArray.get(y) + "-@-" + shortNotesEArray.get(y)+ "-@-" + catgEArray.get(y)));
                            totalCategoryAmount+=amountEArray.get(y);
                        }
                        else if (tempAccnt>500 && tempAccnt <= 1000 && listDataHeader.get(x).contains("₹ 501 - ₹ 1000")) {
                            txtChildData.add(String.valueOf(dateOfTransEArray.get(y) + "-@-" + amountEArray.get(y) + "-@-" + paymentTypeEArray.get(y) + "-@-" + shortNotesEArray.get(y)+ "-@-" + catgEArray.get(y)));
                            totalCategoryAmount+=amountEArray.get(y);
                        }
                        else if (tempAccnt>1000 && tempAccnt <= 5000 && listDataHeader.get(x).contains("₹ 1001 - ₹ 5000")) {
                            txtChildData.add(String.valueOf(dateOfTransEArray.get(y) + "-@-" + amountEArray.get(y) + "-@-" + paymentTypeEArray.get(y) + "-@-" + shortNotesEArray.get(y)+ "-@-" + catgEArray.get(y)));
                            totalCategoryAmount+=amountEArray.get(y);
                        }
                        else if (tempAccnt>5000 && listDataHeader.get(x).contains("> ₹ 5000")) {
                            txtChildData.add(String.valueOf(dateOfTransEArray.get(y) + "-@-" + amountEArray.get(y) + "-@-" + paymentTypeEArray.get(y) + "-@-" + shortNotesEArray.get(y)+ "-@-" + catgEArray.get(y)));
                            totalCategoryAmount+=amountEArray.get(y);
                        }
                    }
                    listDataHeader.set(x,listDataHeader.get(x)+" (Total - ₹ "+totalCategoryAmount+")");
                    listDataChild.put(listDataHeader.get(x), txtChildData);
                    //Toast.makeText(mainActivityInstance, ""+listDataChild.get(listDataHeader.get(x)), Toast.LENGTH_SHORT).show();
                }
            }
            else if(currentSortingOption == 2){  //FOR DATE GROUPING
                for (int x = 0; x < listDataHeader.size(); x++) {
                    List<String> txtChildData = new ArrayList<String>();
                    totalCategoryAmount = 0;
                    for (int y = 0; y < dateOfTransEArray.size(); y++) {
                        if (finalDateOfTranscEArray.get(y).equalsIgnoreCase(listDataHeader.get(x))) {
                            txtChildData.add(String.valueOf(dateOfTransEArray.get(y) + "-@-" + amountEArray.get(y) + "-@-" + paymentTypeEArray.get(y) + "-@-" + shortNotesEArray.get(y)+ "-@-" + catgEArray.get(y)));
                            totalCategoryAmount+=amountEArray.get(y);
                        }
                    }
                    listDataHeader.set(x,listDataHeader.get(x)+" (Total - ₹ "+totalCategoryAmount+")");
                    listDataChild.put(listDataHeader.get(x), txtChildData);
                    //Toast.makeText(mainActivityInstance, ""+listDataChild.get(listDataHeader.get(x)), Toast.LENGTH_SHORT).show();
                }
            }
        }
        else{   //INCOME
            if(currentSortingOption == 0) {  //FOR CATEGORY GROUPING
                for (int x = 0; x < listDataHeader.size(); x++) {
                    List<String> txtChildData = new ArrayList<String>();
                    totalCategoryAmount = 0;
                    for (int y = 0; y < catgIArray.size(); y++) {
                        if (catgIArray.get(y).equalsIgnoreCase(listDataHeader.get(x))) {
                            txtChildData.add(String.valueOf(dateOfTransIArray.get(y) + "-@-" + amountIArray.get(y) + "-@-" + "" + "-@-" + shortNotesIArray.get(y)));
                            totalCategoryAmount+=amountIArray.get(y);
                        }
                    }
                    listDataHeader.set(x,listDataHeader.get(x)+" (Total - ₹ "+totalCategoryAmount+")");
                    listDataChild.put(listDataHeader.get(x), txtChildData);
                }
            }
            else if(currentSortingOption == 1){ //FOR AMOUNT GROUPING
                int tempAccnt;
                for (int x = 0; x < listDataHeader.size(); x++) {
                    List<String> txtChildData = new ArrayList<String>();
                    totalCategoryAmount = 0;
                    for (int y = 0; y < amountIArray.size(); y++) {
                        tempAccnt = amountIArray.get(y);
                        if (tempAccnt<=500 && listDataHeader.get(x).contains("< ₹ 500")) {
                            txtChildData.add(String.valueOf(dateOfTransIArray.get(y) + "-@-" + amountIArray.get(y) + "-@-" + "" + "-@-" + shortNotesIArray.get(y)+ "-@-" + catgIArray.get(y)));
                            totalCategoryAmount+=amountIArray.get(y);
                        }
                        else if (tempAccnt>500 && tempAccnt <= 1000 && listDataHeader.get(x).contains("₹ 501 - ₹ 1000")) {
                            txtChildData.add(String.valueOf(dateOfTransIArray.get(y) + "-@-" + amountIArray.get(y) + "-@-" + "" + "-@-" + shortNotesIArray.get(y)+ "-@-" + catgIArray.get(y)));
                            totalCategoryAmount+=amountIArray.get(y);
                        }
                        else if (tempAccnt>1000 && tempAccnt <= 5000 && listDataHeader.get(x).contains("₹ 1001 - ₹ 5000")) {
                            txtChildData.add(String.valueOf(dateOfTransIArray.get(y) + "-@-" + amountIArray.get(y) + "-@-" + "" + "-@-" + shortNotesIArray.get(y)+ "-@-" + catgIArray.get(y)));
                            totalCategoryAmount+=amountIArray.get(y);
                        }
                        else if (tempAccnt>5000 && listDataHeader.get(x).contains("> ₹ 5000")) {
                            txtChildData.add(String.valueOf(dateOfTransIArray.get(y) + "-@-" + amountIArray.get(y) + "-@-" + "" + "-@-" + shortNotesIArray.get(y)+ "-@-" + catgIArray.get(y)));
                            totalCategoryAmount+=amountIArray.get(y);
                        }
                    }
                    listDataHeader.set(x,listDataHeader.get(x)+" (Total - ₹ "+totalCategoryAmount+")");
                    listDataChild.put(listDataHeader.get(x), txtChildData);
                     //Toast.makeText(mainActivityInstance, ""+listDataChild.get(listDataHeader.get(x)), Toast.LENGTH_SHORT).show();
                }
            }
            else if(currentSortingOption == 2){  //FOR DATE GROUPING
                for (int x = 0; x < listDataHeader.size(); x++) {
                    List<String> txtChildData = new ArrayList<String>();
                    totalCategoryAmount = 0;
                    for (int y = 0; y < dateOfTransIArray.size(); y++) {
                        if (finalDateOfTranscIArray.get(y).equalsIgnoreCase(listDataHeader.get(x))) {
                            txtChildData.add(String.valueOf(dateOfTransIArray.get(y) + "-@-" + amountIArray.get(y) + "-@-" + "" + "-@-" + shortNotesIArray.get(y)+ "-@-" + catgIArray.get(y)));
                            totalCategoryAmount+=amountIArray.get(y);
                        }
                    }
                    listDataHeader.set(x,listDataHeader.get(x)+" (Total - ₹ "+totalCategoryAmount+")");
                    listDataChild.put(listDataHeader.get(x), txtChildData);
                }
            }
        }

            listAdapter = new ListViewCustomAdapter(this, listDataHeader, listDataChild, currentSortingOption);
            // setting list adapter
            listOfBalance.setAdapter(listAdapter);

           //CHECKING AND OPENING THE RESPECTIVE CATEGORY DATA IN ACCOUNT DATAS
            if (chartClickedCatgIndex >= 0 && catgEArray.size()>0 && currentDetailsView.equalsIgnoreCase("expense") && currentSortingOption==0) {
                listOfBalance.expandGroup(chartClickedCatgIndex, true);
            }
    }

    //FIRST TIME SHOWCASE VIEW TIPS
    private void presentShowcaseView() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);

        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
            @Override
            public void onShow(MaterialShowcaseView itemView, int position) {
               switch (position) {
                    case 0: {
                        break;
                    }

                    case 1: {
                        MMUtils.startAnimate(bottomLayout,R.anim.shake,mainActivityInstance);
                        break;
                    }

                    case 2: {
                        MMUtils.startAnimate(chartViewLayout,R.anim.shake,mainActivityInstance);
                        break;
                    }

                    case 3: {
                        MMUtils.startAnimate(findViewById(R.id.dateLayout),R.anim.shake,mainActivityInstance);
                        break;
                    }

                    case 4: {
                        expenseDateLayout.setVisibility(View.VISIBLE);
                        MMUtils.startAnimate(expenseDateLayout,R.anim.shake,mainActivityInstance);
                        break;
                    }

                    case 5: {
                        closeTheExpenseDateMenu();
                        MMUtils.startAnimate(btnBalanceMid,R.anim.shake,mainActivityInstance);
                        break;
                    }

                   case 6: {
                       mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                       MMUtils.startAnimate(expenseDetailsTabClick,R.anim.shake,mainActivityInstance);
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
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
            }
        });

        sequence.setConfig(config);

        sequence.addSequenceItem(toolbar, "Let's get to know your Money Manager!", "START");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(findViewById(R.id.dateLayout))
                        .setTitleText("YOUR CURRENT DATA VIEW PERIOD")
                        .setDismissStyle(Typeface.SANS_SERIF)
                        .setDismissText("GOT IT")
                        .setContentText("See all your details for this date. Tap on the next and previous arrows to see the respective date or week or month or year data's.")
                        .withCircleShape()
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(expenseDateLayout)
                        .setTitleText("CHANGE YOUR DATA VIEW PERIOD")
                        .setDismissStyle(Typeface.SANS_SERIF)
                        .setDismissText("GOT IT")
                        .setContentText("Tap on the menu-item to see your expense data's for a month OR a year OR a week OR select a date from the calendar.")
                        .withCircleShape()
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(btnBalanceMid)
                        .setTitleText("YOUR DATA IN DETAILS")
                        .setDismissStyle(Typeface.SANS_SERIF)
                        .setDismissText("GOT IT")
                        .setContentText("Tap on this to see all your expense and income full details of the data's shown in chart.")
                        .withCircleShape()
                        .build()
        );

        sequence.start();
    }

    @Override
    public void onRemainderDClosed(String alertCatg, String alertTime) {
        int remainderHour, remainderMin, spinnerIndex;
        remainderHour = Integer.parseInt(alertTime.split(":")[1]);
        remainderMin = Integer.parseInt(alertTime.split(":")[2]);
        spinnerIndex = Integer.parseInt(alertTime.split(":")[0]);

      //  Toast.makeText(mainActivityInstance, ""+spinnerIndex+"--"+remainderHour+"--"+remainderMin, Toast.LENGTH_SHORT).show();

        editor = remainderTypePreference.edit();
        editor.putString("alertType", alertCatg);
        editor.putString("alertTime", alertTime);
        editor.commit();
        if(remainderTypePreference.getString("alertType","noalert").equalsIgnoreCase("noalert")){
              mainMenu.findItem(R.id.remainders).setIcon(R.mipmap.alertoff);
        }
        else{
            mainMenu.findItem(R.id.remainders).setIcon(R.mipmap.alerton);
        }

        if(alertCatg.equalsIgnoreCase("noalert")){
            Intent intent = new Intent(MainActivity.this, Alarmreceiver.class);
            PendingIntent pintent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarm.cancel(pintent);
        }
        else {
            //CALLING THE ALARM MANAGER FOR REPEATED NOTIFICATIONS
            Intent intent = new Intent(MainActivity.this, Alarmreceiver.class);
           // intent.putExtra("type","day");

            cal.set(Calendar.HOUR_OF_DAY,remainderHour);
            cal.set(Calendar.MINUTE,remainderMin);
            cal.set(Calendar.SECOND,0);
            PendingIntent pintent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if(alertCatg.equalsIgnoreCase("weekly")){  //SETTING REPEATING ALARM FOR WEEK
                if(spinnerIndex==0){
                    cal.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
                }
                else{
                    cal.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
                }
                alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pintent);
            }
            else if(alertCatg.equalsIgnoreCase("daily")){   //SETTING REPEATING ALARM FOR day
                alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintent);
            }
            else if(alertCatg.equalsIgnoreCase("monthly")){  //SETTING REPEATING ALARM FOR MONTH
                  int currentMonth = cal.get(Calendar.MONTH);
                  int currentYear = cal.get(Calendar.YEAR);

                if(spinnerIndex == 0){
                    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                }
                else if(spinnerIndex == 1){
                    cal.set(Calendar.DAY_OF_MONTH,28);
                }
                else if(spinnerIndex == 2){
                    cal.set(Calendar.DAY_OF_MONTH,27);
                }
                else if(spinnerIndex == 3){
                    cal.set(Calendar.DAY_OF_MONTH,26);
                }
                else{
                    cal.set(Calendar.DAY_OF_MONTH,25);
                }

                if (currentMonth == Calendar.JANUARY || currentMonth == Calendar.MARCH || currentMonth == Calendar.MAY || currentMonth == Calendar.JULY
                        || currentMonth == Calendar.AUGUST || currentMonth == Calendar.OCTOBER || currentMonth == Calendar.DECEMBER){
                    alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 31, pintent);
                }
                if (currentMonth == Calendar.APRIL || currentMonth == Calendar.JUNE || currentMonth == Calendar.SEPTEMBER
                        || currentMonth == Calendar.NOVEMBER){
                    alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 30, pintent);
                }


                if  (currentMonth == Calendar.FEBRUARY){//for feburary month)
                    GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
                    if(cal.isLeapYear(currentYear)){//for leap year feburary month
                        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 29, pintent);
                    }
                    else{ //for non leap year feburary month
                        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 28, pintent);
                    }
                }
            }
        }
   }

    @Override
    public void onRemindMeDialogClosed(boolean isRemindMe) {
        mainMenu.findItem(R.id.addexpenseremainder).setChecked(isRemindMe);
        editor = remindMePreference.edit();
        editor.putBoolean("isRemindMe", isRemindMe);
        editor.commit();
        if(isRemindMe) { //Add alarm-manger here
            Intent intentRemindMe = new Intent(MainActivity.this, RemindMeReceiver.class);
            Calendar remindMeTempCal = Calendar.getInstance();
            remindMeTempCal.set(Calendar.HOUR_OF_DAY,22);
            remindMeTempCal.set(Calendar.MINUTE,0);
            remindMeTempCal.set(Calendar.SECOND,0);
            PendingIntent remindmepintent = PendingIntent.getBroadcast(MainActivity.this, 1, intentRemindMe, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, remindMeTempCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY , remindmepintent);
        }
    }
}
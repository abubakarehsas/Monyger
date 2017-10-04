package com.asisdroid.moneymanager;

import android.support.v7.app.ActionBar;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.aakira.expandablelayout.ExpandableRelativeLayout;

import java.util.HashMap;

public class HelpActiivty extends AppCompatActivity {

    private  ExpandableRelativeLayout tempCommonLayout, expandableLayout1, expandableLayout2, expandableLayout3, expandableLayout4, expandableLayout5, expandableLayout6, expandableLayout7, expandableLayout8, expandableLayout9, expandableLayout10;
    private ScrollView mainScroll;
    final private HashMap<Integer,Integer> allLayouts = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_actiivty);
        ActionBar actionBar = getSupportActionBar();
        //Setting Action bar string and style
        SpannableString s = new SpannableString("Monyger - Help Me !");
        s.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, s.length(), 0);
        actionBar.setTitle(s);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mainScroll = (ScrollView) findViewById(R.id.helpMainScrollView);

        allLayouts.put(1,R.id.expandableLayout1);
        allLayouts.put(2,R.id.expandableLayout2);
        allLayouts.put(3,R.id.expandableLayout3);
        allLayouts.put(4,R.id.expandableLayout4);
        allLayouts.put(5,R.id.expandableLayout5);
        allLayouts.put(6,R.id.expandableLayout6);
        allLayouts.put(7,R.id.expandableLayout7);
        allLayouts.put(8,R.id.expandableLayout8);
        allLayouts.put(9,R.id.expandableLayout9);
        allLayouts.put(10,R.id.expandableLayout10);
    }

    public void expandableButton1(View view) {
        expandableLayout1 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout1);
        expandableLayout1.toggle();
        collapseAll(1);
    }

    public void expandableButton2(View view) {
        expandableLayout2 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout2);
        expandableLayout2.toggle(); // toggle expand and collapse
        collapseAll(2);
    }

    public void expandableButton3(View view) {
        expandableLayout3 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout3);
        expandableLayout3.toggle(); // toggle expand and collapse
        collapseAll(3);
    }

    public void expandableButton4(View view) {
        expandableLayout4 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout4);
        expandableLayout4.toggle(); // toggle expand and collapse
        collapseAll(4);
    }

    public void expandableButton5(View view) {
        expandableLayout5 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout5);
        expandableLayout5.toggle(); // toggle expand and collapse
        collapseAll(5);
    }

    public void expandableButton6(View view) {
        expandableLayout6 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout6);
        expandableLayout6.toggle(); // toggle expand and collapse
        collapseAll(6);
    }

    public void expandableButton7(View view) {
        expandableLayout7 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout7);
        expandableLayout7.toggle(); // toggle expand and collapse
        collapseAll(7);
    }

    public void expandableButton8(View view) {
        expandableLayout8 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout8);
        expandableLayout8.toggle(); // toggle expand and collapse
        collapseAll(8);
    }

    public void expandableButton9(View view) {
        expandableLayout9 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout9);
        expandableLayout9.toggle(); // toggle expand and collapse
        collapseAll(9);
    }
    public void expandableButton10(View view) {
        expandableLayout10 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout10);
        expandableLayout10.toggle(); // toggle expand and collapse
        collapseAll(10);
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    //TO COLLAPSE ALL REMIANIGN LAYOUTS
    public void collapseAll(int index){
        for(int c=1;c<=10;c++) {
            if(c==index){
                continue;
            }
            tempCommonLayout = (ExpandableRelativeLayout) findViewById(allLayouts.get(c));
            if(tempCommonLayout.isExpanded())
            tempCommonLayout.collapse();
        }
    }
}

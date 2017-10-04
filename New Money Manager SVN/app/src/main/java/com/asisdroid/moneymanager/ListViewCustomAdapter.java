package com.asisdroid.moneymanager;

/**
 * Created by ashishkumarpolai on 8/18/2017.
 */

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ListViewCustomAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    private HashMap<String,Integer> catgAllDrawableHashmap = new HashMap<String, Integer>();
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private int _groupOption = 0;

    public ListViewCustomAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData, int groupOptionData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild= listChildData;
        this._groupOption = groupOptionData;

        if(_groupOption==0) {  //REQUIRED ONLY FOR CATEGORY GROUPISM
            if (catgAllDrawableHashmap.size() == 0) {
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
                catgAllDrawableHashmap.put("custom", R.drawable.catg_custom);
            }
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        String childText = String.valueOf(getChild(groupPosition, childPosition));
        String childAmount = childText.split("-@-")[1];
        String childDate = childText.split("-@-")[0];

        String childPayment, childShortNotes;
        try {
            childPayment = childText.split("-@-")[2];
        } catch (ArrayIndexOutOfBoundsException e) {
            childPayment = "";
        }
        try {
            childShortNotes = childText.split("-@-")[3];
        } catch (ArrayIndexOutOfBoundsException e) {
            childShortNotes = "";
        }

        //Toast.makeText(_context, ""+groupPosition+"--"+_listDataHeader.get(groupPosition)+"---"+_listDataChild.get("Travel"), Toast.LENGTH_SHORT).show();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }
        TextView txtDateOfTransac = (TextView) convertView
                .findViewById(R.id.lblListItem);

        TextView txtAmount = (TextView) convertView
                .findViewById(R.id.txtAmount);

        TextView txtPayment = (TextView) convertView
                .findViewById(R.id.txtPaymentType);

        TextView txtShortNotes = (TextView) convertView
                .findViewById(R.id.txtShortNotes);


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = new Date();
        try {
            startDate = df.parse(childDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat formater = new SimpleDateFormat("dd MMM, yyyy");
        childDate = formater.format(startDate);
        txtDateOfTransac.setText(childDate);

        if(_groupOption == 1 || _groupOption==2){ //AMOUNT GROUP
            String childCatgType;
            childCatgType = childText.split("-@-")[4];
            TextView txtCatg = (TextView) convertView
                    .findViewById(R.id.txtCatg);
            txtCatg.setText("Category - "+childCatgType);
            txtCatg.setVisibility(View.VISIBLE);
        }


         txtAmount.setText("Amount - ₹ "+childAmount);

        if(childPayment.equalsIgnoreCase("")) {
            txtPayment.setVisibility(View.GONE);
        }
        else{
            txtPayment.setText("Payment Type - " + childPayment);
        }

        if(childShortNotes.equalsIgnoreCase("")) {
            txtShortNotes.setVisibility(View.GONE);
        }
        else{
            txtShortNotes.setText("Note - "+childShortNotes);
            txtShortNotes.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        try{
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .size();
        }
        catch (Exception e){
            return -1;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        SpannableString ssss = new SpannableString(headerTitle.toUpperCase());
         ssss.setSpan(Typeface.BOLD, 0, headerTitle.indexOf(" (Total"), 0);
        ssss.setSpan(new RelativeSizeSpan(0.7f), (headerTitle.indexOf(" (Total")+1), ssss.length(), 0);
        ssss.setSpan(new StyleSpan(Typeface.ITALIC), (headerTitle.indexOf(" (Total")+1), ssss.length(), 0);
        lblListHeader.setTypeface(null, Typeface.BOLD);
       // ssss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.LightGrey)), 13+(String.valueOf(xtemp).length()), ssss.length(), 0);
        lblListHeader.setText(ssss);

        if(_groupOption==0) { ////REQUIRED ONLY FOR CATEGORY GROUPISM
            try {
                lblListHeader.setCompoundDrawablesWithIntrinsicBounds(null, null, MoneyManagerApplication.getInstance().getResources().getDrawable(catgAllDrawableHashmap.get(headerTitle.substring(0,headerTitle.indexOf(" (Total")))), null);
            } catch (NullPointerException e) {
                lblListHeader.setCompoundDrawablesWithIntrinsicBounds(null, null, MoneyManagerApplication.getInstance().getResources().getDrawable(R.drawable.catg_custom), null);
            }
        }
        else if(_groupOption==1) { ////REQUIRED ONLY FOR AMOUNT GROUPISM
            lblListHeader.setCompoundDrawablesWithIntrinsicBounds(null, null, MoneyManagerApplication.getInstance().getResources().getDrawable(R.drawable.cash_head), null);
        }
        else if(_groupOption==2) { ////REQUIRED ONLY FOR DATE GROUPISM
            lblListHeader.setCompoundDrawablesWithIntrinsicBounds(null, null, MoneyManagerApplication.getInstance().getResources().getDrawable(R.drawable.calndr_group), null);
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}

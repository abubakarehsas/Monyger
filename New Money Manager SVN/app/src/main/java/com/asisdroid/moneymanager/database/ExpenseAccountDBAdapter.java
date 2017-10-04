package com.asisdroid.moneymanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class ExpenseAccountDBAdapter extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "ExpenseAccountDetails.db";
    static final int DATABASE_VERSION = 1;
    public static final int NAME_COLUMN = 1;
    // TODO: Create public field for each column in your table.
    // SQL Statement to create a new database.
    static final String DATABASE_CREATE = "create table " + "EXPENSE_ACCOUNT_DETAILS" +
            "(AMOUNT text, DATE text, SHORTNOTES text, PAYMENTTYPE text, CATEGORY text)";
    // Variable to hold the database instance
    public SQLiteDatabase db;
    // Context of the application using the database.
    // Database open/upgrade helper

    public ExpenseAccountDBAdapter(Context _context) {
        super(_context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS EXPENSE_ACCOUNT_DETAILS");
        onCreate(db);
    }

    public ExpenseAccountDBAdapter open() throws SQLException {
        db = this.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    public void insertAccData(String amount, String date, String shortnotes, String paymenttype, String catgtype) {

        ContentValues newValues = new ContentValues();
        // Assign values for each row.
        newValues.put("AMOUNT", amount);
        newValues.put("DATE", date);
        newValues.put("SHORTNOTES", shortnotes);
        newValues.put("PAYMENTTYPE", paymenttype);
        newValues.put("CATEGORY", catgtype);
        // Insert the row into your table
        db.insert("EXPENSE_ACCOUNT_DETAILS", null, newValues);
        ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
    }

    public boolean updateAccData(String amount, String date, String shortnotes, String paymenttype, String catgtype,String amountSelected,String dateSelected,String shortNotesSelected,String paymentTypeSelected,String catgSelected) {
        String []whereArgs =  {amountSelected,dateSelected,catgSelected,paymentTypeSelected,shortNotesSelected};
        ContentValues newValues = new ContentValues();
        // Assign values for each row.
        newValues.put("AMOUNT", amount);
        newValues.put("DATE", date);
        newValues.put("SHORTNOTES", shortnotes);
        newValues.put("PAYMENTTYPE", paymenttype);
        newValues.put("CATEGORY", catgtype);
        // Insert the row into your table
       int x= db.update("EXPENSE_ACCOUNT_DETAILS", newValues, "AMOUNT = ? AND DATE = ? AND CATEGORY = ? AND PAYMENTTYPE = ? AND SHORTNOTES = ?",whereArgs);
        if(x==1){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean deleteAccData(String amount, String category, String date, String paymenttype, String shortNotes) {
        String []whereArgs =  {amount,date,category,paymenttype, shortNotes};
        int numberOFEntriesDeleted = db.delete("EXPENSE_ACCOUNT_DETAILS", "AMOUNT = ? AND DATE = ? AND CATEGORY = ? AND PAYMENTTYPE = ? AND SHORTNOTES = ?",whereArgs);
        // Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        if(numberOFEntriesDeleted==1){
            return true;
        }
        else {
            return false;
        }
    }

    public ArrayList<String> getAccountDetails(String date1, String date2) {
        ArrayList<String> array_list = new ArrayList<String>();
        Cursor res = db.query("EXPENSE_ACCOUNT_DETAILS", new String[]{"AMOUNT","DATE","SHORTNOTES","PAYMENTTYPE","CATEGORY"}, "Date(DATE) >= Date('"+date1+"') AND Date(DATE) <= Date('"+date2+"')", null, null, null, "CATEGORY ASC");
        if( res != null && res.moveToFirst() ) {
            while (res.isAfterLast() == false) {
                array_list.add(res.getString(0)+"-#-"+res.getString(1)+"-#-"+res.getString(2)+"-#-"+res.getString(3)+"-#-"+res.getString(4));
                res.moveToNext();
            }}
        return array_list;
    }
}


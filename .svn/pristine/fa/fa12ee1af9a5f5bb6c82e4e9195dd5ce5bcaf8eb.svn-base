package com.asisdroid.moneymanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class CategoryIncomeDBAdapter extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "moneymanagerincomecategory.db";
    static final int DATABASE_VERSION = 1;
    public static final int NAME_COLUMN = 1;
    // TODO: Create public field for each column in your table.
    // SQL Statement to create a new database.
    static final String DATABASE_CREATE = "create table " + "CATEGORY_INCOME_DETAILS" +
            "( NAME text, NOOFUSE text, FAVORNOT text, IMAGENAME text)";
    // Variable to hold the database instance
    public SQLiteDatabase db;
    // Context of the application using the database.
    // Database open/upgrade helper

    public CategoryIncomeDBAdapter(Context _context) {
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
        db.execSQL("DROP TABLE IF EXISTS CATEGORY_INCOME_DETAILS");
        onCreate(db);
    }

    public CategoryIncomeDBAdapter open() throws SQLException {
        db = this.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    public boolean insertNewCategory(String name, String noofuse, String favornot, String imagename) {
        Cursor res;
        if(name.length()<4) {
            res = db.query("CATEGORY_INCOME_DETAILS", new String[]{"NAME"}, "upper(NAME) = ?", new String[]{name.toUpperCase()}, null, null, null);
        }
        else {
            res = db.query("CATEGORY_INCOME_DETAILS", new String[]{"NAME"}, "upper(NAME) like ?",new String[] {name.toUpperCase().substring(0,4)+"%"}, null, null, null);
        }
        if(!(res != null && res.moveToFirst() )){
            ContentValues newValues = new ContentValues();
            // Assign values for each row.
            newValues.put("NAME", name);
            newValues.put("NOOFUSE", noofuse);
            newValues.put("FAVORNOT", favornot);
            newValues.put("IMAGENAME", imagename);
            // Insert the row into your table
            db.insert("CATEGORY_INCOME_DETAILS", null, newValues);
            return true;
        }
        else{
            return false;
        }

        ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();

    }

    public int deleteAllCategories() {
        int numberOFEntriesDeleted = db.delete("CATEGORY_INCOME_DETAILS", null, null);
        // Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;
    }


    public ArrayList<String> getAllCategoriesName() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();

        Cursor res = db.query("CATEGORY_INCOME_DETAILS", new String[]{"NAME"}, null, null, null, null, "NOOFUSE DESC");
        if( res != null && res.moveToFirst() ) {
            while (res.isAfterLast() == false) {
                array_list.add(res.getString(res.getColumnIndex("NAME")).toString());
                res.moveToNext();
            }
        }
        return array_list;
    }

    public ArrayList<Integer> getAllCatgUse() {
        ArrayList<Integer> array_list = new ArrayList<Integer>();

        //hp = new HashMap();

        Cursor res = db.query("CATEGORY_INCOME_DETAILS", new String[]{"NOOFUSE"}, null, null, null, null, "NOOFUSE DESC");
         if( res != null && res.moveToFirst() ) {
           for(int x=0;x<2;x++) {
                array_list.add(res.getInt(res.getColumnIndex("NOOFUSE")));
                res.moveToNext();
            }}
        return array_list;
    }

    public int getTheCatgUse(String catgName) {
        int array_list = 0;

        //hp = new HashMap();

        Cursor res = db.query("CATEGORY_INCOME_DETAILS", new String[]{"NOOFUSE"}, "NAME = ?", new String[] { catgName }, null, null, "NOOFUSE DESC");
        if( res != null && res.moveToFirst() ) {
            array_list = res.getInt(res.getColumnIndex("NOOFUSE"));
        }
        return array_list;
    }

    public int updateTheCatgNoOfUse(String catgName, String newNoOfUse) {
        ArrayList<Integer> array_list = new ArrayList<Integer>();

        //hp = new HashMap();
        ContentValues data=new ContentValues();
        data.put("NOOFUSE",newNoOfUse);
        return db.update("CATEGORY_INCOME_DETAILS",data ,"NAME = ?",new String[] { catgName });
    }

    public ArrayList<String> getAllCategoriesImageName() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();

        Cursor res = db.query("CATEGORY_INCOME_DETAILS", new String[]{"IMAGENAME"}, null, null, null, null, "NOOFUSE DESC");
        String giveortake;
        if( res != null && res.moveToFirst() ) {
            while (res.isAfterLast() == false) {
                array_list.add(res.getString(res.getColumnIndex("IMAGENAME")).toString());
                res.moveToNext();
            }}
        return array_list;
    }

    public ArrayList<String> getAllMatchedCategoriesName(String matchCatg) {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();

        Cursor res = db.query("CATEGORY_INCOME_DETAILS", new String[]{"NAME"}, "NAME like ?",  new String[] { "%"+matchCatg+"%" }, null, null, "NOOFUSE DESC");
        String giveortake;
        if( res != null && res.moveToFirst() ) {
            while (res.isAfterLast() == false) {
                array_list.add(res.getString(res.getColumnIndex("NAME")).toString());
                res.moveToNext();
            }}
        return array_list;
    }

    public ArrayList<String> getAllMatchedCategoriesImageName(String matchCatg) {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();

        Cursor res = db.query("CATEGORY_INCOME_DETAILS", new String[]{"IMAGENAME"}, "NAME like ?",  new String[] { "%"+matchCatg+"%" }, null, null, "NOOFUSE DESC");
        String giveortake;
        if( res != null && res.moveToFirst() ) {
            while (res.isAfterLast() == false) {
                array_list.add(res.getString(res.getColumnIndex("IMAGENAME")).toString());
                res.moveToNext();
            }}
        return array_list;
    }

    public void deleteCustomCategory(String deleteCatg){
        db.delete("CATEGORY_INCOME_DETAILS", "NAME = ?",  new String[] { deleteCatg });
    }

}


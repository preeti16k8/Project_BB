package com.kre8tives.bareboneneww.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kre8tives.bareboneneww.Model.Orderpreviewmodel;

import java.util.ArrayList;
import java.util.HashMap;

import static com.kre8tives.bareboneneww.Database.Constants.ROW_ID;
import static com.kre8tives.bareboneneww.Database.Constants.TB_NAME;

/**
 * Created by user on 5/6/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }
    //TABLE CREATION
    @Override
    public void onCreate(SQLiteDatabase db) {
        try
        {
            db.execSQL(Constants.CREATE_TB);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    //TABLE UPGRADE
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ Constants.TB_NAME);
        onCreate(db);
    }
    public int getItemCount() {
        String countQuery = "SELECT  * FROM " + TB_NAME;
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public ArrayList<Orderpreviewmodel> getOrderList()

    {

        String selectQuery = "SELECT  * FROM " + TB_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<Orderpreviewmodel> InvoiceList = new ArrayList<Orderpreviewmodel>();
        if (cursor.moveToFirst())
        {
            do {
                Orderpreviewmodel list = new Orderpreviewmodel();
                list.setOid(cursor.getString(0));
                list.setOname(cursor.getString(1));
                list.setOprice(cursor.getString(2));
                list.setOquantity(cursor.getString(3));
                list.setOprice(cursor.getString(4));
                list.setOposition(cursor.getString(5));

                InvoiceList.add(list);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return InvoiceList;
    }

    public ArrayList<Orderpreviewmodel> getOrderById(String id)

    {
        String selectQuery = "SELECT * FROM " + TB_NAME + " WHERE "
                + ROW_ID + " = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<Orderpreviewmodel> InvoiceList = new ArrayList<Orderpreviewmodel>();
        if (cursor.moveToFirst())
        {
            do {
                Orderpreviewmodel list = new Orderpreviewmodel();
                list.setOid(cursor.getString(0));
                list.setOname(cursor.getString(1));
                list.setOposition(cursor.getString(2));
                list.setOquantity(cursor.getString(3));
                list.setOprice(cursor.getString(4));

                InvoiceList.add(list);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return InvoiceList;
    }

    public String composeJSONfromSQLite() {
        ArrayList<HashMap<String, String>> offlineList;
        offlineList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM TB_NAME ";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("zip", cursor.getString(1));
                map.put("phone", cursor.getString(2));
                map.put("uid", cursor.getString(3));
                offlineList.add(map);

            } while (cursor.moveToNext());
        }
        database.close();
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(offlineList);
    }
}
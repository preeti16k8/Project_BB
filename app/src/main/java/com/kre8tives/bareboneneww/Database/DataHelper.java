package com.kre8tives.bareboneneww.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Administrator on 1/25/2017.
 */

public class DataHelper extends SQLiteOpenHelper {

    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();


    static String DATABASE_NAME = "BAREBONES";
    static String USER_TABLE = "user";
    static String PROFILE_TABLE = "profile";
    static String CURRENT_STORE_TABLE = "current_store";
    static String CART_TABLE = "cart";

    static String Item_Id = "item_id";
    static String Item_Name = "item_name";
    static String Quantity = "quantity";
    static String Price = "price";

    static String Name = "name";
    static String UserId = "userid";
    static String StoreID= "storeId";
    static String PhoneNo = "phoneNo";
    static String Password = "password";
    static String User_Name = "user_name";
    static String ID = "id";
    static String Email = "email";
    static String Image_Url = "image_url";
    static String DOB="dob";
    static String Level="level";

    static String CREATE_ADMIN_TABLE = "";
//    static String CREATE_PROFILE_TABLE = "";
    static String CREATE_CURRENT_STORE_TABLE="";
    static String CREATE_CART_TABLE = "";


    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        CREATE_ADMIN_TABLE = "CREATE TABLE " + USER_TABLE + "(" + User_Name + " TEXT PRIMARY KEY, " + UserId + " VARCHAR, " + Email + " VARCHAR, " + DOB + " VARCHAR, " + Image_Url + " VARCHAR, " + Level + " VARCHAR, " + PhoneNo + " VARCHAR " + ");";
        CREATE_CURRENT_STORE_TABLE = "CREATE TABLE " + CURRENT_STORE_TABLE + "(" + StoreID + " VARCHAR PRIMARY KEY," + UserId + " VARCHAR "+");";

        db.execSQL(CREATE_ADMIN_TABLE);
        db.execSQL(CREATE_CURRENT_STORE_TABLE);


    }




    public void adduser(Context mContext, String userid, String userName, String userEmail, String userImage, String userDob, String userPhone, String userLevel)

    {

        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(User_Name, userName);
            values.put(UserId, userid);
            values.put(Email, userEmail);
            values.put(Image_Url, userImage);
            values.put(PhoneNo,userPhone);
            values.put(DOB,userDob);
            values.put(Level,userLevel);


            db.insertWithOnConflict(USER_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            db.close();

        } catch (Exception e) {
            Log.e("Insertion exception", "cannot insert", e);
        }
    }


    public void addStore(Context mContext, String storeID,String userId)

    {

        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(StoreID, storeID);
            values.put(UserId, userId);


            db.insertWithOnConflict(CURRENT_STORE_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            db.close();

        } catch (Exception e) {
            Log.e("Insertion exception", "cannot insert", e);
        }
    }

    //	Delete Sample

    public void deleteUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(USER_TABLE, null, null);
        db.close();
    }

    public void deleteStore() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CURRENT_STORE_TABLE, null, null);
        db.close();
    }

    public int getUserCount() {
        String countQuery = "SELECT  * FROM " + USER_TABLE;
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getStoreCount() {
        String countQuery = "SELECT  * FROM " + CURRENT_STORE_TABLE;
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }
    public Cursor getuser() {
        String selectuser = "SELECT * FROM " + USER_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectuser, null);


        return cursor;
    }
    public Cursor getstore() {
        String selectuser = "SELECT * FROM " + CURRENT_STORE_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectuser, null);


        return cursor;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (newVersion > oldVersion) {
            // on upgrade drop older tables
            db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
//            db.execSQL("DROP TABLE IF EXISTS " + ORDER_TABLE);

            // create new tables
            onCreate(db);

        }
    }
}
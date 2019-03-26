package com.kre8tives.bareboneneww.Orders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

/**
 * Created by Ashet on 21-10-2017.
 */

public class RetrofitDataBase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION =1;

    // Database Name
    private static final String DATABASE_NAME = "TotalOrders";


    // Database table name
    private static final String TABLE_ORDERS = "allOrders";

    private static final String TABLE_ITEM = "items";
    private static final String ORDER_ID_ITEM = "order_id";
    private static final String ITEM_PRICE = "item_Price";
    private static final String ITEM_NAME = "item_name";
    private static final String ITEM_QUANTITY = "item_quantity";
    private static final String ITEM_TOTAL = "item_total";


    private static final String CREATE_ITEM=
            "CREATE TABLE " + TABLE_ITEM + " (" +
                    ORDER_ID_ITEM+ " TEXT,"+
                    ITEM_NAME + " TEXT," +
                    ITEM_PRICE+ " TEXT,"+
                    ITEM_QUANTITY+ " TEXT," +
                    ITEM_TOTAL + " TEXT)";




    // Table Columns names
    private static final String ORDER_ID = "order_id";
    private static final String CUSTOMER_ID = "customer_id";
    private static final String ORDER_AMOUNT = "order_amount";
    private static final String ORDER_DATE = "order_date";
    private static final String ORDER_STATUS = "order_status";

    private static final String CREATE=
            "CREATE TABLE " + TABLE_ORDERS + " (" +
                    ORDER_ID+ " TEXT,"+
                    CUSTOMER_ID + " TEXT," +
                    ORDER_AMOUNT+ " TEXT,"+
                    ORDER_DATE + " TEXT," +
                    ORDER_STATUS + " TEXT)";


    public RetrofitDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE);
        sqLiteDatabase.execSQL(CREATE_ITEM);
        Log.d("Data","Database_Created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +TABLE_ORDERS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +TABLE_ITEM);
        onCreate(sqLiteDatabase);

    }
    //Orders
    public void dropTableORders(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_ORDERS);
    }
    public void deleteAllRecords()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_ORDERS);
    }

    public Cursor getListItem() {
        String selectQuery = "SELECT  * FROM " + TABLE_ORDERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery , null);

        return cursor;
    }
    public void addOrder(Orders orders) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
       values.put(ORDER_ID, orders.getOrderId());
        values.put(CUSTOMER_ID, orders.getCustomerId());
        values.put(ORDER_DATE,orders.getOrderDate()); // Contact Name
       values.put(ORDER_AMOUNT, orders.getOrderAmount());
        values.put(ORDER_STATUS, orders.getOrderStatus());

        // Inserting Row
        db.insert(TABLE_ORDERS, null, values);
        db.close(); // Closing database connection
    }
    public Cursor getStatus(){
        String selectQuery = "SELECT order_status FROM " + TABLE_ORDERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery , null);

        return cursor;
    }
    //Item
    public void addOrderIem(Orders order) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        List<Item> items=order.getItems();
        for(int i=0;i<items.size();i++){
        values.put(ORDER_ID_ITEM, order.getOrderId());
        values.put(ITEM_NAME,items.get(i).getItemName());
        values.put(ITEM_PRICE,items.get(i).getItemPrice()); // Contact Name
        values.put(ITEM_QUANTITY, items.get(i).getItemQuantity());
        values.put(ITEM_TOTAL,items.get(i).getItemTotal());
        db.insert(TABLE_ITEM, null, values);}

        // Inserting Row

        db.close(); // Closing database connection
    }
    public void deleteAllItems()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_ITEM);
    }
}

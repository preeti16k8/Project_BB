package com.kre8tives.bareboneneww.Orders;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;


import com.kre8tives.bareboneneww.MyApplication;

import com.kre8tives.bareboneneww.Util.Utilities;


import java.util.List;

/**
 * Created by Ashet on 23-10-2017.
 */

public class OrdersDataHelper  {

    private static List<Orders> ordersList;
    private static RetrofitDataBase dbHelper;



    public static void createOrders(List<Orders> orders){
        dbHelper=new RetrofitDataBase(MyApplication.getInstance());
        for (int i=0;i<orders.size();i++){
            dbHelper.addOrder(orders.get(i));
        }
        dbHelper.close();

    }
    public static void deleteRecordsAll(){
        dbHelper=new RetrofitDataBase(MyApplication.getInstance());
        dbHelper.deleteAllRecords();
        dbHelper.close();
    }

    public static void logOrders(){


        dbHelper=new RetrofitDataBase(MyApplication.getInstance());
        Cursor cursor=dbHelper.getListItem();
        String cursors=
                DatabaseUtils.dumpCursorToString(cursor);
        Log.d("data",cursors);
        dbHelper.close();
    }
    public static void getstatus(){
        dbHelper=new RetrofitDataBase(MyApplication.getInstance());
        Cursor cursor=dbHelper.getStatus();
        int total=cursor.getCount();
        Log.d("bill_printed",""+total);
        int newcounter=0;
        if (cursor.moveToFirst()){
            do{
                String data = cursor.getString(cursor.getColumnIndex("order_status"));
                if(data.equals("bill_printed")){

                 newcounter++;

                }
                if(total==newcounter){
                    Log.d("counter","all orders settled");
                    Utilities.removeKey("qrcode",MyApplication.getInstance());
                    Utilities.removeKey("orderid",MyApplication.getInstance());



                }
            }while(cursor.moveToNext());
        }

        String cursors=
                DatabaseUtils.dumpCursorToString(cursor);
        Log.d("status",cursors);

        dbHelper.close();
    }
    //items
    public static void createItems(List<Orders> orders){
        dbHelper=new RetrofitDataBase(MyApplication.getInstance());
        for (int i=0;i<orders.size();i++){
            dbHelper.addOrderIem(orders.get(i));
        }
        dbHelper.close();
    }
    public static void deleteItemsAll(){
        dbHelper=new RetrofitDataBase(MyApplication.getInstance());
        dbHelper.deleteAllItems();
        dbHelper.close();
    }



}

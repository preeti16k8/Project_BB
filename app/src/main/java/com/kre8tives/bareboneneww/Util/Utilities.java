package com.kre8tives.bareboneneww.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.kre8tives.bareboneneww.helper.ConnectionReceiver;
import com.kre8tives.bareboneneww.MyApplication;


import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Ashet on 08-10-2017.
 */

public class Utilities {
    static String pref_orderid=null,pref_qrcode=null;

    public static final String PREF_ORDERID_WITHQR = "order and qrcode";
    public static final String PREF_USER = "user";



    public static void setPref(String key,String value,Context context){

        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_ORDERID_WITHQR, MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
        //editor.apply();
    }

    public static void removeKey(String string,Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_ORDERID_WITHQR, MODE_PRIVATE).edit();
        editor.remove(string);
        editor.commit();

    }
    public static void clearPrefOrderid(String string, Context context){

        SharedPreferences preferences =context.getSharedPreferences(string, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();

    }

    public static String getPref(String key, Context context){
        SharedPreferences prefs =context.getSharedPreferences(PREF_ORDERID_WITHQR, MODE_PRIVATE);
        String restoredText = prefs.getString(key, null);
        return restoredText;
    }
    public static Boolean checkConnection() {

        boolean isConnected = ConnectionReceiver.isConnected();
        if(!isConnected) {


            return false;

        }else {
            return true;
        }

    }
    public static void logPref(){
        //Loging shared pref
        Context context= MyApplication.getInstance();
        SharedPreferences pref =context.getSharedPreferences(PREF_ORDERID_WITHQR, MODE_PRIVATE);
        Map<String, ?> allEntries = pref.getAll();
        if(allEntries!=null){
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            }
        }
        //

    }
}

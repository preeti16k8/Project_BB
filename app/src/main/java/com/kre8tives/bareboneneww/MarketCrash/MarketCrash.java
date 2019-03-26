package com.kre8tives.bareboneneww.MarketCrash;


import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.kre8tives.bareboneneww.Activity.MainActivity;
import com.kre8tives.bareboneneww.R;
import com.kre8tives.bareboneneww.Util.ServerConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.kre8tives.bareboneneww.Fragments.FoodSettingsFragment.storeid;
import static com.kre8tives.bareboneneww.Util.Utilities.getPref;
import static com.kre8tives.bareboneneww.Util.Utilities.setPref;


public class MarketCrash extends IntentService {

    private final static String TAG = "MarketCrashService";
    public static String CRASH = "http://barebonesbar.com/bbapi/notification_api.php";

    public static final String CRASHBROAD = "your_package_name.countdown_br";
    Intent bi = new Intent(CRASHBROAD);
    String responseCrash;
    JSONArray crashArray;
    public static long total;
    String flag="flase";
    public static String crashFlag="false";

    String crashOn= "Market crash is on";
    String crashOff= "Market crash is off";


    public MarketCrash() {
        super("MarketCrash");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Starting...");


    }

    @Override
    protected void onHandleIntent(Intent intent) {


        postClient();
    }

    public void postClient() {
        HttpURLConnection conn ;
        try {

            URL url = new URL(CRASH);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            Uri.Builder builder = new Uri.Builder().
                    appendQueryParameter("store_id",storeid);



            String query = builder.build().getEncodedQuery();
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
            responseCrash = ServerConnection.getData(conn);
            Log.i(TAG, responseCrash);
            Log.d("marketcannnrsh",responseCrash);
            crashArray = new JSONArray(responseCrash);
            JSONObject jobject = crashArray.getJSONObject(0);
             crashFlag=jobject.getString("market_crash");

            crashFlag="true";
            Log.d("crashvaluee",crashFlag);
            String check=getPref("dont",getApplicationContext());
            if(crashFlag=="true"&& check==null){

                notifyuser(crashOn);

                setPref("dont","true",getApplicationContext());
                setPref("endtime","12:25:44",getApplicationContext());
                setPref("marketcrash","on",getApplicationContext());
                calculateTime();
                /*Intent mIntent = new Intent(getApplicationContext(), CounterService.class);
                Utilities.setPref("counter",""+total,getApplicationContext());
                startService(mIntent);
                Log.i(TAG, "Started service");*/


                
            }else{

                Log.i(TAG,"Market is off");
                setPref("marketcrash","off",getApplicationContext());
            }

        }catch (Exception e){
            Log.i(TAG,e.getMessage());
        }
    }

    private void stopalaramservice() {
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, MarketCrash.class);
        PendingIntent pending = PendingIntent.getService(this, 0, alarmIntent, 0);

        alarmManager.cancel(pending);
    }



    private void calculateTime() {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String currenttime=sdf.format(cal.getTime());
            Date currentTime=sdf.parse(currenttime);
            String endtime = "22:30";
            Date endTime = sdf.parse(endtime);
            total=endTime.getTime()-currentTime.getTime();
            //total=60000;



        }catch (Exception e){

            total=0;
        }
    }

    private void notifyuser(String msg) {
        Context ctx=getApplicationContext();
        Intent intent = new Intent(ctx, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(ctx);

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_bar)
                .setTicker("BareBones")
                .setContentTitle("BareBones")
                .setContentText(msg)
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setContentInfo("Info");


        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Destroyed...");
    }
}
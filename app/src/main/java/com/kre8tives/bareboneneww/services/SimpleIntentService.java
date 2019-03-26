package com.kre8tives.bareboneneww.services;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.kre8tives.bareboneneww.R;
import com.kre8tives.bareboneneww.Util.ServerConnection;
import com.kre8tives.bareboneneww.Util.Utilities;
import com.kre8tives.bareboneneww.Fragments.NewCurrentOrderFragment;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.kre8tives.bareboneneww.Util.Utilities.PREF_ORDERID_WITHQR;

public class SimpleIntentService extends IntentService {
    public static final String PARAM_IN_MSG = "imsg";
    public static final String PARAM_OUT_MSG = "omsg";
    String responsestatus,status,extramain;
    Integer timeout;
    private final Handler handler = new Handler();
    private static int cnf=0;

    public SimpleIntentService() {
        super("SimpleIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {


            postClient();
    }

    private void notifyuser(String msg) {
        Context ctx = getApplicationContext();
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(ctx, SimpleIntentService.class);
        PendingIntent pIntent = PendingIntent.getActivity(ctx, 0, intent, 0);
        Notification noti = new NotificationCompat.Builder(ctx)
                .setContentTitle("BareBones")
                .setTicker("Hearty365")
                .setAutoCancel(true)
                .setContentText(msg).setSmallIcon(R.mipmap.new_ic_launch)
                .setContentIntent(pIntent)
                .setSound(soundUri)
                .build();

        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(1, noti);
    }

    public void postClient() {

        HttpURLConnection conn = null;
        try {

            URL url = new URL(NewCurrentOrderFragment.BRAND_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("customer_id", Utilities.getPref("userid",getApplicationContext()))
                    .appendQueryParameter("store_id", Utilities.getPref("storeid",getApplicationContext()))
                    .appendQueryParameter("qr_code", Utilities.getPref("qrcode",getApplicationContext()))
                    .appendQueryParameter("order_id", Utilities.getPref("current",getApplicationContext()))
                    .appendQueryParameter("current_status", "New");
            String query = builder.build().getEncodedQuery();
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
            responsestatus = ServerConnection.getData(conn);
            Log.d("Cart Json", responsestatus);

            if(responsestatus.contains("Cart is Empty")){
                //Utilities.clearPrefOrderid(PREF_ORDERID_WITHQR,getApplication());
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent alarmIntent = new Intent(this, SimpleIntentService.class);
                PendingIntent pending = PendingIntent.getService(this, 0, alarmIntent, 0);
                alarmManager.cancel(pending);

            }

            JSONObject wholeObject = new JSONObject(responsestatus);
            status = wholeObject.getString("order_status");

            if(status.equals("confirm")&& cnf==0){
                cnf=1;
                notifyuser("Your Order has been Confirmed");
            }else if(status.equals("settled")) {
                Utilities.clearPrefOrderid(PREF_ORDERID_WITHQR,getApplication());
                notifyuser("Your Order has been Settled");
                cnf=0;
            }else if(status.equals("rejected")) {
                Utilities.clearPrefOrderid(PREF_ORDERID_WITHQR,getApplication());
                notifyuser("Your Order has been Rejected.Please try again!");
                cnf=0;
            }
            else if(status.equals("bill_printed")&&cnf==0) {
                Utilities.clearPrefOrderid(PREF_ORDERID_WITHQR,getApplication());
                notifyuser("Bill is printed for your Order!");
                cnf=0;
            }



        } catch (Exception e) {
            timeout = 1;
        } finally {
            {
                if (conn != null) {
                    conn.disconnect();

                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
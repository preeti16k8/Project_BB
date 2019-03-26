package com.kre8tives.bareboneneww.MarketCrash;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import com.kre8tives.bareboneneww.Util.Utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.kre8tives.bareboneneww.Util.Utilities.removeKey;

public class CounterService extends Service {

    private final static String TAG = "CounterService";

    public static final String COUNTDOWN_BR = "your_package_name.countdown_br";
    Intent bi = new Intent(COUNTDOWN_BR);

    CountDownTimer cdt = null;
    long total;

    @Override
    public void onCreate() {
        super.onCreate();


        Log.i(TAG, "Starting timer...");
       String count= Utilities.getPref("counter",getApplicationContext());
       //CALCULATE TIME HERE
        String endtime= Utilities.getPref("endtime",getApplicationContext());
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String currenttime=sdf.format(cal.getTime());
            Date currentTime=sdf.parse(currenttime);
            Date endTime = sdf.parse(endtime);
            total=endTime.getTime()-currentTime.getTime();
            Log.d(TAG,""+total);
        }catch (Exception e){


        }



        cdt = new CountDownTimer(/*total*/120000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                Log.i(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000);
                bi.putExtra("countdown", millisUntilFinished);
                sendBroadcast(bi);
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "Timer finished");
                removeKey("dont",getApplicationContext());
                removeKey("endtime",getApplicationContext());
                removeKey("marketcrash",getApplicationContext());


            }
        };

        cdt.start();

    }

    @Override
    public void onDestroy() {

        cdt.cancel();
        Log.i(TAG, "Timer cancelled");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent,flags,startId);

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
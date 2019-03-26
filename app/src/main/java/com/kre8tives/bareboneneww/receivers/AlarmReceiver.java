package com.kre8tives.bareboneneww.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kre8tives.bareboneneww.services.BackgroundService;

/**
 * Created by Preeti on 31-03-2018.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent background=new Intent(context, BackgroundService.class);
        context.startService(background);
    }
}

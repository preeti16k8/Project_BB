package com.kre8tives.bareboneneww.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import static com.kre8tives.bareboneneww.Activity.VerifyActivity.otpText;

/**
 * Created by user on 6/17/2017.
 */

public  class SmsReceiver extends BroadcastReceiver {

    private final String TAG = VerifyActivity.SmsReceiver.class.getSimpleName();
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        SmsMessage currentSMS;
        String message;


        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                if (pdusObj != null) {

                    for (Object aObject : pdusObj) {

                        currentSMS = getIncomingMessage(aObject, bundle);

                        String senderNo = currentSMS.getDisplayOriginatingAddress();

                        message = currentSMS.getDisplayMessageBody();
                        String verificationCode = getVerificationCode(message);
                        otpText.setText(verificationCode);
                    }
                    this.abortBroadcast();

                }
            }

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }

    }


    private SmsMessage getIncomingMessage(Object aObject, Bundle bundle) {
        SmsMessage currentSMS;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String format = bundle.getString("format");
            currentSMS = SmsMessage.createFromPdu((byte[]) aObject, format);
        } else {
            currentSMS = SmsMessage.createFromPdu((byte[]) aObject);
        }
        return currentSMS;
    }




    public String getVerificationCode(String message) {

        //String st="Your mobile verification code 123456";
        String st = message;
        String st1 = st.replaceAll("\\D+", "");
        Log.d("otpmessageextract", st1);
        Log.d("otpmessage", st);
        return st1;
    }


}


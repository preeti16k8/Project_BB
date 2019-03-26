package com.kre8tives.bareboneneww.services;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.app.NotificationCompat;

import com.kre8tives.bareboneneww.Activity.MainActivity;
import com.kre8tives.bareboneneww.R;


public class MySimpleService extends IntentService {
	public static final int NOTIF_ID = 56;
	long timestamp;
	Context mContext;
	public static int marketcrashtoastmsg=0;
	
	// Must create a default constructor
	public MySimpleService() {
		super("simple-service");
	}
   
	// This describes what will happen when service is triggered
	@Override
	protected void onHandleIntent(Intent intent) {
		timestamp =  System.currentTimeMillis();
	    // Extract additional values from the bundle
	    String val = intent.getStringExtra("foo");
		// Extract the receiver passed into the service
	    ResultReceiver rec = intent.getParcelableExtra("receiver");
	    // Sleep a bit first
	   // sleep(3000);
	    // Send result to activity


	   // sendResultValue(rec, val);


		// Let's also create notification
	    createNotification(val);
	}

	// Send result to activity using ResultReceiver
	private void sendResultValue(ResultReceiver rec, String val) {
		// To send a message to the Activity, create a pass a Bundle
	    Bundle bundle = new Bundle();
	    bundle.putString("resultValue", "My Result Value. You Passed in: " + val + " with timestamp: " + timestamp);
	    // Here we call send passing a resultCode and the bundle of extras
	    rec.send(Activity.RESULT_OK, bundle);
	}
	
	// Construct compatible notification
	private void createNotification(String val) {
		// Construct pending intent to serve as action for notification item
		Intent intent = new Intent(this, MainActivity.class);
		marketcrashtoastmsg=1;
		//Toast.makeText(getApplicationContext(),"Hey,Market Crash is on.Hurry Up !Now You can buy liquor at more discounted price",Toast.LENGTH_LONG).show();
/*
		CategoryFragment categoryFragment=new CategoryFragment();
		FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.content_frame, categoryFragment);
		fragmentTransaction.addToBackStack(categoryFragment.getClass().getName());
		fragmentTransaction.commit();*/
		/*Bundle args = new Bundle();
		args.putString("categoryId", brand.getCategoryId());
		args.putString("brandId",brand.getId());
		categoryFragment.setArguments(args);*/


		intent.putExtra("message", "Launched via notification with message: " + val + " and timestamp " + timestamp);


		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
		// Create notification
	//	String longText = "Intent service has a new message with: " + val + " and a timestamp of: " + timestamp;
		String longText="Hey,Market Crash is on.Hurry Up !Now You can buy liquor at more discounted price";
		Notification noti =
		        new NotificationCompat.Builder(this)
		        .setSmallIcon(R.mipmap.ic_launcher)
		        .setContentTitle("Alert!")
		        .setDefaults(Notification.DEFAULT_SOUND)
		        .setContentText("Market Crash is On")
		        .setStyle(new NotificationCompat.BigTextStyle().bigText(longText))
		        .setContentIntent(pIntent)
		        .build();
		
		// Hide the notification after its selected
		noti.flags |= Notification.FLAG_AUTO_CANCEL;
		
		NotificationManager mNotificationManager =
			    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(NOTIF_ID, noti);
	}
	
	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
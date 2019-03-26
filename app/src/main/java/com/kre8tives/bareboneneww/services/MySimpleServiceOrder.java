/*
package com.kre8tives.bareboneabcdpl.services;
import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import MainActivity;
import ExpaOrders;
import Item;
import Orders;
import com.kre8tives.bareboneabcdpl.R;
import ApiClient;
import ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static MainActivity.userid;
import static SelectStoreAdapter.storeId;
import static Utilities.setPref;


public class MySimpleServiceOrder extends IntentService {
    public static final int NOTIF_ID = 56;
    long timestamp;
    public static int confirmordertoast=0;
    Context mContext;
 String qrcode;

    public static int counter=0;
    public static int counterb=0;
    public static int counterc=0;
    public static int counterr=0;
    public static String LatestStatus="";
    public static boolean status=false;
    public static List<Orders> historyPojoListM;
    private Handler handler;

    // Must create a default constructor
    public MySimpleServiceOrder() {
        super("simple-service");
    }

    // This describes what will happen when service is triggered
    @Override
    protected void onHandleIntent(Intent intent) {

        if(intent!=null){
            synchronized (this){
                try {
                    wait(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                       // postClient1();
                        Toast.makeText(MySimpleServiceOrder.this, "HHHHHIIIIIII", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }

        timestamp =  System.currentTimeMillis();
        // Extract additional values from the bundle
        String val = intent.getStringExtra("foo");
        // Extract the receiver passed into the service
        ResultReceiver rec = intent.getParcelableExtra("receiver");
      //  postClient1();


       // createNotification(val);


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
        confirmordertoast=1;

        //Toast.makeText(getApplicationContext(),"Congratulation,Your Order has been Confirmed",Toast.LENGTH_LONG).show();
*/
/*
		CategoryFragment categoryFragment=new CategoryFragment();
		FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.content_frame, categoryFragment);
		fragmentTransaction.addToBackStack(categoryFragment.getClass().getName());
		fragmentTransaction.commit();*//*

		*/
/*Bundle args = new Bundle();
		args.putString("categoryId", brand.getCategoryId());
		args.putString("brandId",brand.getId());
		categoryFragment.setArguments(args);*//*


        intent.putExtra("message", "Launched via notification with message: " + val + " and timestamp " + timestamp);


        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        // Create notification
       // String longText = "Intent service has a new message with: " + val + " and a timestamp of: " + timestamp;
       // String longText="Congratulation,Your Order has been Confirmed";


        String longTextt="HHHHHHHHHHHHCongratulations,Your bill has been printed";
        Notification notii =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Alert!")
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setContentText("HHHHHHHHCongratulations,Your bill has been printed!")
                      */
/*  .setStyle(new NotificationCompat.BigTextStyle().bigText(longText))*//*

                        .setStyle(new NotificationCompat.BigTextStyle().bigText(longTextt))
                        .setContentIntent(pIntent)
                        .build();

        // Hide the notification after its selected
        notii.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(NOTIF_ID, notii);

      */
/*  if(toastmsgbillprinted==1){
            toastmsgbillprinted=0;
            String longText="Congratulations,Your bill has been printed";
            Notification noti =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("Alert!")
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setContentText("Congratulations,Your bill has been printed!")
                      *//*
*/
/*  .setStyle(new NotificationCompat.BigTextStyle().bigText(longText))*//*
*/
/*
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

        if(toastmsgsettled==1){
            toastmsgsettled=0;
            String longText="Congratulations,Your bill is settled";
            Notification noti =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("Alert!")
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setContentText("Congratulations,Your bill is settled!")
                      *//*
*/
/*  .setStyle(new NotificationCompat.BigTextStyle().bigText(longText))*//*
*/
/*
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
        if(toastmsgrejected==1){
            toastmsgrejected=0;
            String longText="Sorry, your order is rejected";
            Notification noti =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("Alert!")
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setContentText("Your order is rejected!")
                      *//*
*/
/*  .setStyle(new NotificationCompat.BigTextStyle().bigText(longText))*//*
*/
/*
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
        if(taostmsgconfirmed==1){
            taostmsgconfirmed=0;
            String longText="Sorry, your order is Confirmed";
            Notification noti =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("Alert!")
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setContentText("Your order is Confirmed!")
                      *//*
*/
/*  .setStyle(new NotificationCompat.BigTextStyle().bigText(longText))*//*
*/
/*
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(longText))
                            .setContentIntent(pIntent)
                            .build();

            // Hide the notification after its selected
            noti.flags |= Notification.FLAG_AUTO_CANCEL;
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // mId allows you to update the notification later on.
            mNotificationManager.notify(NOTIF_ID, noti);
        }*//*





    }




    private void postClient1() {

        //Retrofit
        ApiInterface apiService;
        apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<List<Orders>> call = apiService.getHistoryOrders(userid, storeId);
        call.enqueue(new Callback<List<Orders>>() {
            @Override
            public void onResponse(Call<List<Orders>> call, Response<List<Orders>> response) {
                int statusCode = response.code();
                Log.d("codeeeeeee", "" + statusCode);
                //  Log.d("responseeeesta", String.valueOf(response));
                List<Orders> ordersList = response.body();
                //  Log.d("responseboadyyy", String.valueOf(response.body()));
                historyPojoListM = ordersList;
                */
/*orderHistoryAdapter = new OrderHistoryAdapter(historyPojoList, getActivity());
                rv_orderhistory1.setAdapter(orderHistoryAdapter);*//*

                //test

                List<ExpaOrders> orders=new ArrayList<>();
                int count=0;
                int countb=0;
                int countc=0;
                int countr=0;
                for(int i=0;i<ordersList.size();i++){

                    List<Item> items=new ArrayList<>();
                    items.addAll(ordersList.get(i).getItems());
                    String input=ordersList.get(i).getOrderStatus();
                    LatestStatus=ordersList.get(0).getOrderStatus();
                    Log.d("latesttstatus",LatestStatus);
                    Log.d("arryasizee", String.valueOf(ordersList.size()));


                    if(input.equals("bill_printed")){
                        countb++;
                        status=true;
                        if(countb>counterb){
                            status=true;
                            counterb=countb;
                            showAlert("", "Your bill is printed" +new String(Character.toChars(0x1F61C)), "bill_printed");
                            //  toastmsgbillprinted=1;
                            toastmsgbillprinted=1;
                            String msg="Bill has been printed for your order";
                          //  showAlert("", "Your bill is printed" +new String(Character.toChars(0x1F61C)), "bill_printed");
                            sendNotification(msg);
                            // onStartAlarmOrder();


                            break;
                        }


                    }
                    else if (input.equals("settled")) {
                        count++;
                        if(count>counter){
                            status = false;
                            counter=count;
                            showAlert("", "Your bill is settled" +new String(Character.toChars(0x1F61C)), "bill_printed");
                            // toastmsgsettled=1;
                                String msg="Cheers! Your bill is settled . Thanks for visiting Barebone";
                               // showAlert("", "Your bill is settled" +new String(Character.toChars(0x1F61C)), "bill_printed");
                                sendNotification(msg);
                            qrcode = null;
                            setPref("qrcode", qrcode, getApplicationContext());

                            //  onStartAlarmOrder();
                            break;
                        }

                    }
                    else if (input.equals("rejected")) {
                        countr++;
                        if(countr>counterr){
                            //status = false;
                            counterr=countr;
                            showAlert("", "Sorry ,Your Order is rejected" +new String(Character.toChars(0x1F61C)), "bill_printed");
                            //  toastmsgrejected=1;
                            String msg="Your Order is rejected";
                           // showAlert("", "Sorry ,Your Order is rejected" +new String(Character.toChars(0x1F61C)), "bill_printed");
                            sendNotification(msg);
                            qrcode = null;
                            setPref("qrcode", qrcode, getApplicationContext());

                            // onStartAlarmOrder();
                            break;
                        }

                    }
                    else{
                        if (input.equals("confirmed")) {
                            countc++;
                            // status=false;
                            if(countc>counterc){
                                counterc=countc;
                                showAlert("", "Your Order is Confirmed" +new String(Character.toChars(0x1F61C)), "bill_printed");
                                //   taostmsgconfirmed=1;
                                String msg="Your Order is Confirmed";
                              // showAlert("", "Your Order is Confirmed" +new String(Character.toChars(0x1F61C)), "bill_printed");
                                sendNotification(msg);
                                //  onStartAlarmOrder();
                                break;
                            }

                        }

                    }



                    Log.d("statussscheck", String.valueOf(status));

                */
/*    if((LatestStatus.equals("bill_printed"))){
                        //status=true;
                    }

                    if((LatestStatus.equals("settled"))){
                        //status=false;
                    }*//*


                    String status=input.substring(0,1).toUpperCase()+input.substring(1);
                    ExpaOrders expaOrderer =new ExpaOrders(ordersList.get(i).getOrderId(),items
                            ,ordersList.get(i).getOrderDate()
                            ,status
                            ,ordersList.get(i).getOrderAmount(), R.drawable.icon_arrow);

                    orders.add(expaOrderer);
                }

              */
/*  OrdersAdapter adapter = new OrdersAdapter(orders);
                rv_orderhistory1.setAdapter(adapter);*//*



                //

                //   progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<List<Orders>> call, Throwable t) {

            }


        });


    }


    public void sendNotification(String msg) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this);

        //Create the intent that’ll fire when the user taps the notification//
        // showAlert("", "Please wait! You can not place order until your current bill is settled" +new String(Character.toChars(0x1F61C)), "bill_printed");

        //  Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse());
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        //  Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.androidauthority.com/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        mBuilder.setContentIntent(pendingIntent);

        mBuilder.setSmallIcon(R.mipmap.new_ic_launch);
        mBuilder.setContentTitle("My notification");
        mBuilder.setContentText(msg);

        NotificationManager mNotificationManager =

                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(001, mBuilder.build());
    }



    private void showAlert(String title,String msg,String type) {
        switch(type){
            case "bill_printed":
                new AwesomeErrorDialog(this)
                        .setTitle(msg)
                        .setColoredCircle(R.color.colorAccent)
                        .setDialogIconOnly(R.drawable.bare33)
                        .setDialogBodyBackgroundColor(R.color.white)
                        .setCancelable(true).setButtonText(getString(R.string.dialog_ok_button))
                        .setButtonBackgroundColor(R.color.colorAccent)
                        .setButtonText(getString(R.string.dialog_ok_button))
                        .setErrorButtonClick(new Closure() {
                            @Override
                            public void exec() {
                                // click

                            }
                        })
                        .show();
                break;

            case "success":
                new AwesomeSuccessDialog(getApplicationContext())
                        .setTitle(msg)
                        .setColoredCircle(R.color.colorAccent)

                        .setDialogBodyBackgroundColor(R.color.white)
                        .setDialogIconOnly(R.drawable.bare22)
                        .setCancelable(true)
                        .setPositiveButtonText("Place your order now")
                        .setPositiveButtonbackgroundColor(R.color.colorAccent)
                        .setPositiveButtonTextColor(R.color.white)

                        .setPositiveButtonClick(new Closure() {
                            @Override
                            public void exec() {
                                //click
                            }
                        })
                        .show();
                break;


        }

    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}*/

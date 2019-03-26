package com.kre8tives.bareboneneww.services;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.kre8tives.bareboneneww.ExpandableOrders.ExpaOrders;
import com.kre8tives.bareboneneww.Orders.Item;
import com.kre8tives.bareboneneww.Orders.Orders;
import com.kre8tives.bareboneneww.R;
import com.kre8tives.bareboneneww.Retrofit.ApiClient;
import com.kre8tives.bareboneneww.Retrofit.ApiInterface;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kre8tives.bareboneneww.Adapter.SelectStoreAdapter.storeId;
import static com.kre8tives.bareboneneww.Util.Utilities.setPref;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.kre8tives.bareboneneww.Activity.MainActivity;
import com.kre8tives.bareboneneww.Util.ServerConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.kre8tives.bareboneneww.Activity.MainActivity.userid;


/**
 * Created by Preeti on 31-03-2018.
 */

public class BackgroundService extends Service {
    private boolean isRunning;
    private Context context;
    private Thread backgroundThread;
    String userResponse="";
    Integer timeout = 0;

    public static int confirmordertoast=0;
    Context mContext;
    String qrcode;
    private final LocalBinder mBinder = new LocalBinder();

    protected Toast mToast;
    public static int counter=0;
    public static int counterb=0;
    public static int counterc=0;
    public static int counterr=0;
    public static String LatestStatus="";
    public static boolean status=false;
    public static List<Orders> historyPojoListM=new ArrayList<>();
    private Handler handler;
    public static ArrayList<String> statusarraylist=new ArrayList<>();
    public int i=0;


    public class LocalBinder extends Binder {
        public BackgroundService getService() {
            return BackgroundService.this;
        }
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate(){

       super.onCreate();

    }


    @Override
    public int onStartCommand(Intent intent,int flags,int startId){

        Toast.makeText(this,"service started",Toast.LENGTH_LONG).show();
        Log.d("servicesstrt","servicesstartedcheck");

          handler = new Handler();
          handler.post(new Runnable() {
              @Override
              public void run() {

               /* MyTask myTask = new MyTask();
                myTask.execute("http://barebonesbar.com/bbapi/orders_api.php");*/
                  getUser getss = new getUser();
                  getss.execute();
                  //Log.d("ivalueinsideloop", String.valueOf(i));
                  //postClient1();
              }
          });


        return android.app.Service.START_STICKY;
    }

    @Override
    public void onDestroy(){
        Toast.makeText(this,"service stopped",Toast.LENGTH_LONG).show();
        Log.d("helllleeestopee","stopped?");
        super.onDestroy();
    }


    private void postClient1() {
        //Retrofit

        Toast.makeText(this,"hiii i am in postclient method",Toast.LENGTH_LONG).show();
        Log.d("pstcliient","postclientnn");



        ApiInterface apiService;
        apiService =
                ApiClient.getClient().create(ApiInterface.class);
        //Call<List<Orders>> call = apiService.getHistoryOrders(userid, storeId);
        Call<List<Orders>> call = apiService.getHistoryOrders("618", "1");
        Log.d("useriddddd",userid);
        Log.d("storeidddddd",storeId);
        call.enqueue(new Callback<List<Orders>>() {
            @Override
            public void onResponse(Call<List<Orders>> call, Response<List<Orders>> response) {
                int statusCode = response.code();
                Log.d("codeeeeeee", "" + statusCode);
                List<Orders> ordersList = response.body();
                historyPojoListM = ordersList;
                List<ExpaOrders> orders=new ArrayList<>();
                int count=0;
                for(int i=0;i<ordersList.size();i++){

                    List<Item> items=new ArrayList<>();
                    items.addAll(ordersList.get(i).getItems());
                    String input=ordersList.get(i).getOrderStatus();
                    LatestStatus=ordersList.get(0).getOrderStatus();
                    Log.d("latesttstatus",LatestStatus);


                    if(input.equals("bill_printed")){
                        status=true;
                       // onStartAlarm();
                        String msg="Bill is printed!!!!!!!";
                        sendNotification(msg);
                        Log.d("Hstatussscheck", String.valueOf(status));
                        break;
                    }
                    else {

                        if (input.equals("settled")) {
                            count++;
                            if(count>counter){
                                status = false;
                              //  onStartAlarm();
                                counter=count;
                                String msg="Bill is settled!!!!!!!";
                                sendNotification(msg);
                                qrcode = null;
                                setPref("qrcode", qrcode, getApplicationContext());
                                break;
                            }


                        }

                    }


                }



            }

            @Override
            public void onFailure(Call<List<Orders>> call, Throwable t) {

            }


        });


    }

    private void doTheAutoRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new saveOrdersData1().execute();
                doTheAutoRefresh();
            }
        }, 1000);
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
        mBuilder.setContentTitle("Order Status!");
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




    public class saveOrdersData1 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          /*  progressDialog = new ProgressDialog(getActivity(),
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setMessage("Loading....");
            progressDialog.show();*/
            historyPojoListM = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... params) {
            postClient1();
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }
    }



    public class MyTask extends AsyncTask<String, Void, String>{
        //declare variables required
        URL myurl;
        HttpURLConnection connection;
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;
        String line; //for reading line by line from buffered reader
        StringBuilder result; //for accumulating all lines

        @Override
        protected String doInBackground(String... p1) {
            try {
                myurl = new URL(p1[0]); //we have prepared url - p1[0] contains url
                connection = (HttpURLConnection) myurl.openConnection(); //connection is established
                inputStream = connection.getInputStream(); // opens channel for reading
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                result = new StringBuilder(); // ADD
                line = bufferedReader.readLine(); //read first line
                while(line != null){
                    result.append(line); //pile up in string builder
                    line = bufferedReader.readLine(); //reads next line
                }
                //BY NOW ALL LINES OF BUFFERED READER ARE IN STRING BUILDER result
                //NOW result contains finally the HTML CODE coming from server
                return result.toString();
            } catch (MalformedURLException e) {
                //Toast.makeText(getActivity(), "IMPROPER URL", Toast.LENGTH_SHORT).show();
                Log.d("B33","IMPOROPER URL.."); //will be printed in logcat
                Log.d("B33","EXCEPTION .. "+e.getMessage()+e.getCause());
                e.printStackTrace();
            } catch (IOException e) {
                //Toast.makeText(getActivity(), "CHECK INTERNET", Toast.LENGTH_SHORT).show();
                Log.d("B33", "CHECK INTERNET");
                e.printStackTrace();
            } finally {
                //CLEAN IMPORTANT SYSTEM RESOURCES
                if(connection != null)
                    connection.disconnect();
                try {
                    if(inputStream != null)
                        inputStream.close();
                    if(inputStreamReader != null)
                        inputStreamReader.close();
                    if(bufferedReader != null)
                        bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            //NOW APPLY THAT DATA ONTO TEXTVIEW
            if(s != null) {
             //   tv.setText(Html.fromHtml(s));
                /*Log.d("HHHHH","GGGGG");
                Toast.makeText(BackgroundService.this,"HHHHHHHHHH",Toast.LENGTH_LONG).show();*/
            }
            else
              //  tv.setText("SOME THING WENT WRONG...NULL RETURN");
            super.onPostExecute(s);
        }
    }


    public class getUser extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            postClient();
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if (userResponse!=null){

                try {
                    JSONArray userArray = new JSONArray(userResponse);
                       Log.d("statusresponsee",userResponse);
                    Log.d("HHHHH","GGGGG");
                   // Toast.makeText(BackgroundService.this,"HHHHHHHHHH",Toast.LENGTH_LONG).show();

                    statusarraylist.clear();
                    Log.d("arrayliststastaftercle", String.valueOf(statusarraylist));
                    for (int i = 0; i < userArray.length(); i++) {
                        JSONObject jobject = userArray.getJSONObject(i);
                        String orderid = jobject.getString("order_id");
                        String customerid = jobject.getString("customer_id");
                        String orderamount=jobject.getString("order_amount");
                        String orderdate=jobject.getString("order_date");
                        String orderstatus=jobject.getString("order_status");
                        statusarraylist.add(orderstatus);
                    }
                    Log.d("arrayliststast", String.valueOf(statusarraylist));
                   boolean x=false;
                    int count=0;
                    int countb=0;
                    int countc=0;
                    int countr=0;
                    for(int j=0; j<statusarraylist.size();j++) {

                        String statusal = statusarraylist.get(j);

                        if (statusal.equals("bill_printed")) {
                            status = true;
                            x=true;
                          //  showAlert("", "Please wait! You can not place order until your current bill is settled" +new String(Character.toChars(0x1F61C)), "bill_printed");
                            // onStartAlarm();
                            countb++;

                            String msg = "Bill is printed!!!!!!!";
                           // sendNotification(msg);
                            Log.d("countbvalue", String.valueOf(countb));
                            Log.d("counterbvalue", String.valueOf(counterb));
                            if(countb>counterb){
                                counterb=countb;
                                Log.d("countbvaluein", String.valueOf(countb));
                                Log.d("counterbvaluein", String.valueOf(counterb));
                                sendNotification(msg);
                            }
                            Log.d("Hstatussscheck", String.valueOf(status));

                        }
                        if (statusal.equals("settled")) {
                                count++;
                                if(x==true){
                                    status=true;
                                }
                                else{
                                    status=false;
                                }
                                if (count > counter) {
                                  //  status = false;
                                    //  onStartAlarm();
                                    counter = count;
                                    String msg = "Your bill is settled.Thanks for visiting Barebone Bar!!!!!!!";
                                    sendNotification(msg);
                                    qrcode = null;
                                    setPref("qrcode", qrcode, getApplicationContext());

                                }
                            Log.d("Hsettledstatussscheck", String.valueOf(status));

                            }
                        if (statusal.equals("confirmed")) {
                            countc++;
                            if (countc > counterc) {
                                //status = false;
                                //  onStartAlarm();
                                counterc = countc;
                                String msg = "Your Order is Confirmed !!!!!!!";
                                sendNotification(msg);

                            }
                            if (statusal.equals("cancelled")) {
                                countr++;
                                if (countr > counterr) {
                                  //  status = false;
                                    //  onStartAlarm();
                                    counterr = countr;
                                    String msg ="Sorry,Your Order got cancelled!!!!!!!";
                                    sendNotification(msg);
                                    qrcode = null;
                                    setPref("qrcode", qrcode, getApplicationContext());

                                }

                            }

                        }


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }


    private void postClient()
    {

        HttpURLConnection conn = null;
        try {

            URL url = new URL("http://barebonesbar.com/bbapi/orders_api.php");
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("customer_id","618")
             .appendQueryParameter("store_id","1");


           /* Call<List<Orders>> call = apiService.getHistoryOrders("618", "1");
            Log.d("useriddddd",userid);
            Log.d("storeidddddd",storeId);*/

            String query = builder.build().getEncodedQuery();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
            userResponse = ServerConnection.getData(conn);

        }

        catch (Exception e) {
            timeout = 1;
        } finally {
            {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
    }
}

package com.kre8tives.bareboneneww.Activity;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kre8tives.bareboneneww.Adapter.SelectStoreAdapter;
import com.kre8tives.bareboneneww.Database.DataHelper;
import com.kre8tives.bareboneneww.Model.SelectStore;
import com.kre8tives.bareboneneww.R;
import com.kre8tives.bareboneneww.Util.ServerConnection;
import com.kre8tives.bareboneneww.Util.Utilities;
import com.kre8tives.bareboneneww.helper.ConnectionReceiver;
import com.kre8tives.bareboneneww.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.kre8tives.bareboneneww.Url.BASE_URL;

public class StoreActivity extends AppCompatActivity implements ConnectionReceiver.ConnectionReceiverListener {




    public static String STORE_URL =BASE_URL+"/"+"store_api.php";
    String storeApiResponseStr = "";
    public static String USER_DETAILS_URL =BASE_URL+"/"+"customer_oneapi.php";
    String custApiResponseStr="";


    public static String userid;
    String userName,userPhone,userImage,userLevel,userDob,userEmail;
    String MobileNumber;
    ImageView storeimage;
    Integer timeout = 0;
    List<SelectStore> selectStoreList;
    SelectStoreAdapter selectStoreAdapter;
    JSONArray storeArray;

    //For Enter Screen
    RecyclerView recyclerView;

    ProgressDialog progressDialog;
    ProgressBar progressBar;
    DataHelper dbHelper;
    ConnectionReceiver mybroadcast=new ConnectionReceiver();


    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //context=getApplicationContext();


        setContentView(R.layout.activity_store);
        


        selectStoreList=new ArrayList<>();
        storeimage=(ImageView)findViewById(R.id.selectstore_imgView);
        dbHelper = new DataHelper(this);
        recyclerView = (RecyclerView)findViewById(R.id.rv_selectstore);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MobileNumber=getIntent().getStringExtra("phone");
        progressDialog = new ProgressDialog(StoreActivity.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        checkConnection();
        //This needs to be handled
        Cursor cursor = dbHelper.getuser();//Getting Data from User Table
        lOGCursorData(cursor);

        cursor.close();

        //Get User Api call AsyncTask
        Log.d("Flow_in_splash","Get store Async called");

        new getUser().execute();


        //??
        if(getIntent().getBooleanExtra("EXIT", false)) {
            finish();
            Log.d("Finished","Finished Store activity");
        }
    }



    //This one is for checking internet connection

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(!isConnected) {

           Toast.makeText(getApplicationContext(), "Connection Lost.Please turn on wifi/cellular data.", Toast.LENGTH_SHORT).show();

            //throwAlertDialog();


        }else{
            new getStore().execute();

        }
    }
    private void throwAlertDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(false);
        alertBuilder.setTitle("Error");
        alertBuilder.setMessage("Check your connection and try again.");
        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onClick(DialogInterface dialog, int which) {
                if(Utilities.checkConnection()){

                    new getStore().execute();

                    }
                else{

                    throwAlertDialog();

                }

            }
        });
        AlertDialog alert = alertBuilder.create();
        alert.show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        registerReceiver(mybroadcast, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectionListener(this);
    }

    private void checkConnection() {

        boolean isConnected = ConnectionReceiver.isConnected();
        if(!isConnected) {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
    }}

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mybroadcast);

    }
    //This one is for checking internet connection Ends


    private void lOGCursorData(Cursor cursor) {
        if (cursor.moveToFirst()) {
            do {
                StringBuilder sb = new StringBuilder();
                int columnsQty = cursor.getColumnCount();
                for (int idx=0; idx<columnsQty; ++idx) {
                    sb.append(cursor.getString(idx));
                    if (idx < columnsQty - 1)
                        sb.append("; ");
                }
                Log.d("CursorData", String.format("Row: %d, Values: %s", cursor.getPosition(),
                        sb.toString()));
            } while (cursor.moveToNext());
        }
    }



    //Get Store API calls and Post Method
    public class getStore extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            //refresh_btn.setVisibility(View.GONE);
            selectStoreList = new ArrayList<SelectStore>();

            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            postData();
            return null;
        }
        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);


            if (storeApiResponseStr != null) {

                Log.d("storeresponsee",storeApiResponseStr);
                longInfo(storeApiResponseStr);
                try {
                    storeArray = new JSONArray(storeApiResponseStr);
                    for (int i = 0; i < storeArray.length(); i++) {
                        SelectStore selectStore = new SelectStore();
                        JSONObject jobject = storeArray.getJSONObject(i);
                        String Id = jobject.getString("store_id");
                        String Name = jobject.getString("store_name");
                        String Image =jobject.getString("store_image");
                        String imageUrl="http://barebonesbar.com/bbapi/upload/"+Image;
                       // Picasso.with(getApplicationContext()).load(imageUrl).into(storeimage) ;
                        selectStore.setName(Name);
                        selectStore.setId(Id);
                        selectStore.setImageUrl(imageUrl);
                        selectStoreList.add(selectStore);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }




                selectStoreAdapter = new SelectStoreAdapter(selectStoreList, StoreActivity.this);
                recyclerView.setAdapter(selectStoreAdapter);
                progressDialog.dismiss();


            }
            //Log.d("response",storeApiResponseStr);


        }
    }

    private void postData()
    {

        HttpURLConnection conn = null;
        try {
            URL url = new URL(STORE_URL);

            /*
            * Api JSON Check--http://barebonesbar.com/bbapi/store_api.php
            * Posts- 1. "customer_id"
            *
            * Response Null Array
            * */


            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("customer_id",userid);
            String query = builder.build().getEncodedQuery();
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
            storeApiResponseStr = ServerConnection.getData(conn);

            Log.d("userIddd",userid);
        }

        catch (Exception e) {
            timeout = 1;
            //throwAlertDialog();

        } finally {
            {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
    }

    public static void longInfo(String str) {
        if(str.length() > 4000) {
            Log.d("Full", str.substring(0, 4000));
            longInfo(str.substring(4000));
        } else
            Log.d("Full", str);
    }
    //Store API calls End

    //Customer api calls

    public class getUser extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Toast.makeText(getApplicationContext(),"Can put Progress bar",Toast.LENGTH_SHORT).show();

        }

        @Override
        protected String doInBackground(String... params) {
            postClient();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (custApiResponseStr!=null){

                try {
                    Log.d("custApiResponseStr", custApiResponseStr);
                    JSONArray userArray = new JSONArray(custApiResponseStr);
                    for (int i = 0; i < userArray.length(); i++) {
                        //SelectStore selectStore=new SelectStore();

                        //No data is Coming here
                        JSONObject jobject = userArray.getJSONObject(i);
                        userName = jobject.getString("customer_name");
                        Log.d("username",userName);
                        String image = jobject.getString("image");
                        //also used in store api post -userid
                        userid = jobject.getString("customer_id");
                        userPhone=jobject.getString("phone");
                        userDob=jobject.getString("dob");
                        userEmail=jobject.getString("email");
                        userLevel=jobject.getString("level");
                        userImage=BASE_URL+"/"+"upload/"+image;
                     dbHelper.adduser(StoreActivity.this, userid,userName, userEmail, userImage, userDob, userPhone, userLevel);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //new getStore().execute();

        }
    }

    private void postClient()
    {
        HttpURLConnection conn = null;

        try {


            URL url = new URL(USER_DETAILS_URL);
            /*
            * Api JSON Check--http://barebonesbar.com/bbapi/customer_oneapi.php
            * Posts- 1. "phone"
            *
            * Response {"success":0}
            * */
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(3000);
            conn.setConnectTimeout(3000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("phone",Utilities.getPref("phone",this));
            String query = builder.build().getEncodedQuery();
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
            custApiResponseStr = ServerConnection.getData(conn);
            Log.d("Myphone", getIntent().getStringExtra("phone"));
        }

        catch (Exception e) {
            timeout = 1;
            /*Snackbar snackbar;
            snackbar = Snackbar.make(this.findViewById(android.R.id.content), "Something went wrong.Try again!", Snackbar.LENGTH_SHORT);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(getResources().getColor(R.color.red));
            TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(getResources().getColor(R.color.white));
            snackbar.show();*/
        } finally {
            {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
    }
    //Customer Api calls ends
//App crash handler


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog!=null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
}



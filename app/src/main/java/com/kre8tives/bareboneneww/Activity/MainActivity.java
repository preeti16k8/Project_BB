package com.kre8tives.bareboneneww.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.gnzlt.AndroidVisionQRReader.QRActivity;
import com.kre8tives.bareboneneww.Database.DBAdapter;
import com.kre8tives.bareboneneww.Database.DataHelper;
import com.kre8tives.bareboneneww.ExpandableOrders.ExpaOrders;
import com.kre8tives.bareboneneww.ExpandableOrders.NewCurrentOrdersFragment;
import com.kre8tives.bareboneneww.Fragments.AboutFragment;
import com.kre8tives.bareboneneww.Fragments.DashBoardFragment;
import com.kre8tives.bareboneneww.Fragments.NewsFragment;
import com.kre8tives.bareboneneww.Fragments.OrderHistoryFragment;
import com.kre8tives.bareboneneww.Fragments.OrderPreviewFragment;
import com.kre8tives.bareboneneww.Fragments.RaiseDisputeFragment;
import com.kre8tives.bareboneneww.Fragments.TermsFragment;
import com.kre8tives.bareboneneww.MarketCrash.MarketCrash;
import com.kre8tives.bareboneneww.Orders.Item;
import com.kre8tives.bareboneneww.Orders.Orders;
import com.kre8tives.bareboneneww.R;
import com.kre8tives.bareboneneww.Retrofit.ApiClient;
import com.kre8tives.bareboneneww.Retrofit.ApiInterface;
import com.kre8tives.bareboneneww.Util.ServerConnection;
import com.kre8tives.bareboneneww.Util.Utilities;
import com.kre8tives.bareboneneww.receivers.MyAlarmReceiver;
import com.kre8tives.bareboneneww.receivers.MySimpleReceiver;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.kre8tives.bareboneneww.Adapter.SelectStoreAdapter.storeId;
import static com.kre8tives.bareboneneww.Url.BASE_URL;
import static com.kre8tives.bareboneneww.Util.Utilities.PREF_ORDERID_WITHQR;
import static com.kre8tives.bareboneneww.Util.Utilities.PREF_USER;
import static com.kre8tives.bareboneneww.Util.Utilities.setPref;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{


    public MySimpleReceiver receiverForSimple;
    private PendingIntent alarmPendingIntent;

    public static List<Orders> historyPojoListM;
    Toolbar toolbar;
    DataHelper db;
    String qrcode;
    DBAdapter dbAdapter;
    String userResponse="";
    private BroadcastReceiver receiver;
    private ActionBarDrawerToggle toggle;
    private TextView phoneTxt;
    private TextView nameTxt;
    public static String userid,username,userImage,userlevel,user_Phone;
    private ImageView imageView;
    public static String USER_DETAILS_URL =BASE_URL+"/"+"customer_oneapi.php";
    public static int counter=0;
    private Button btn_updateProfile;
    public static
    Integer timeout = 0;
    static boolean stopservice=false;
    AlarmManager alarmManager;
    public static String LatestStatus="";
    private final Handler handler = new Handler();
    public static boolean status=false;
    private Context context;
    public static int hi=0;
    // Integer counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Caching
        try {
            File httpCacheDir = new File(getApplication().getCacheDir(), "http");
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            HttpResponseCache.install(httpCacheDir, httpCacheSize);

        } catch (IOException e) {

        }
        //
        Toast.makeText(this, "Welcome to Barebones", Toast.LENGTH_SHORT).show();
        hi++;
        Log.d("hivalueeeinmain", String.valueOf(hi));


       /* this.context=this;
        Intent alarm=new Intent(this.context, AlarmReceiver.class);
        boolean alarmRunning=(PendingIntent.getBroadcast(this.context,0,alarm,PendingIntent.FLAG_NO_CREATE)!=null);
        if(alarmRunning==false){
            PendingIntent pendingIntent=PendingIntent.getBroadcast(this.context,0,alarm,0);
            AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),1000,pendingIntent);
        }
*/



      /* Intent intent=new Intent(MainActivity.this, BackgroundService.class);
       startService(intent);
*/
       // startService(new Intent(getBaseContext(), BackgroundService.class));


        //Orders Update
        // startService(new Intent(this,TotalOrdersService.class));
        //Crash Service
        //startService(new Intent(this,TotalOrdersService.class));
       // checkForCrash();



        db=new DataHelper(this);
        dbAdapter=new DBAdapter(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView)
                findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displaySelectedScreen(R.id.nav_dash);


        NavigationView navigatioView =
                (NavigationView)findViewById(R.id.nav_view);
        navigatioView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);

        nameTxt=header.findViewById(R.id.tv_drawername);
        imageView=header.findViewById(R.id.imageProfile);
        phoneTxt=header.findViewById(R.id.tv_drawerlevel);

        btn_updateProfile=(Button)header.findViewById(R.id.btn_updateProfile);

        btn_updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new
                        Intent(MainActivity.this,UpdateProfileActivity.class);
                intent.putExtra("phonemain",
                        getIntent().getStringExtra("phone"));
                startActivity(intent);

            }
        });

        db = new DataHelper(getApplicationContext());
        Cursor cursor = db.getuser();

        if (cursor.moveToFirst()) {
            userid=cursor.getString(1);
            username=cursor.getString(0);
            userImage=cursor.getString(4);
            userlevel=cursor.getString(5);
            user_Phone=cursor.getString(6);
        }
        //user id and store id store
        Utilities.setPref("storeid",storeId,this);
        Utilities.setPref("userid",userid,this);
        Utilities.logPref();
        //
        new getUser().execute();
      new saveOrdersData1().execute();
       doTheAutoRefresh();
       // setupServiceReceiver();

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


    public void onStartAlarm() {
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(),
                MyAlarmReceiver.class);
        intent.putExtra("receiver", receiverForSimple);
        // Create a PendingIntent to be triggered when the alarm goes off
        alarmPendingIntent = PendingIntent.getBroadcast(this,
                MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);


        long firstMillis = System.currentTimeMillis(); // first run of


        long intervalMillis = 3600000*24*365 ;
        AlarmManager alarm = (AlarmManager)
                this.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                firstMillis, intervalMillis, alarmPendingIntent);

    }

    public void onStopAlarm() {
        AlarmManager alarm = (AlarmManager)
                this.getSystemService(Context.ALARM_SERVICE);
        if (alarmPendingIntent != null) {
            alarm.cancel(alarmPendingIntent);
        }
    }

    // Setup the callback for when data is received from the service
    public void setupServiceReceiver() {
        receiverForSimple = new MySimpleReceiver(new Handler());
        receiverForSimple.setReceiver(new MySimpleReceiver.Receiver() {
            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == RESULT_OK) {
                    String resultValue = resultData.getString("resultValue");
                    Toast.makeText(MainActivity.this, resultValue,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void checkForCrash() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, MarketCrash.class);
        PendingIntent pending = PendingIntent.getService(this, 0, alarmIntent, 0);

        alarmManager.setRepeating(AlarmManager.RTC,
                System.currentTimeMillis(),1000*60
                , pending);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.invalidateOptionsMenu();
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                String qrCode;
                try {
                    qrCode = Utilities.getPref("qrcode", this);
                    qrCode = null;
                } catch (Exception e) {
                    qrCode = null;
                }


                /*    Snackbar snackbar;
                    snackbar = Snackbar.make(this.findViewById(android.R.id.content), "You have no current orders.", Snackbar.LENGTH_SHORT);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextSize(18);
                    textView.setTextColor(getResources().getColor(R.color.black));
                    snackbar.show();

                }else{*/

                NewCurrentOrdersFragment newCurrentOrderFragment = new NewCurrentOrdersFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, newCurrentOrderFragment);
                fragmentTransaction.addToBackStack(newCurrentOrderFragment.getClass().getName());
                fragmentTransaction.commit();


                return true;
        }


        return super.onOptionsItemSelected(item);

    }



    private void displaySelectedScreen(int id){

        Fragment fragment = null;

        switch (id){
            case R.id.nav_home:
                Intent storeintent=new Intent(this, StoreActivity.class);
                finish();
                storeintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(storeintent);
                break;
            case R.id.nav_change_table:
                String msg;
                String qr=Utilities.getPref("qrcode",this);
                String msg2;
                if(qr!=null){
                    String arr=qr.substring(5);
                    msg="You are on Table "+ arr +"\n"+" Are you sure you want \n to change your table?";
                    new AwesomeErrorDialog(this)
                            .setTitle(msg)


                            .setColoredCircle(R.color.colorAccent)
                            .setDialogIconOnly(R.drawable.bare22)
                            .setDialogBodyBackgroundColor(R.color.white)
                            .setCancelable(true).setButtonText(getString(R.string.dialog_ok_button))
                            .setButtonBackgroundColor(R.color.colorPrimaryDark)
                            .setButtonText(getString(R.string.dialog_ok_button))
                            .setErrorButtonClick(new Closure() {
                                @Override
                                public void exec() {
                                    Intent qrScanIntent = new Intent(MainActivity.this, QRActivity.class);
                                    startActivityForResult(qrScanIntent, OrderPreviewFragment.QR_REQUEST);
                                }
                            })

                            .show();


                }else {
                    msg="Please make your first order. \n Qr code not found";
                    new AwesomeErrorDialog(this)
                            .setTitle(msg)


                            .setColoredCircle(R.color.colorAccent)
                            .setDialogIconOnly(R.drawable.bare22)
                            .setDialogBodyBackgroundColor(R.color.white)
                            .setCancelable(true).setButtonText(getString(R.string.dialog_ok_button))
                            .setButtonBackgroundColor(R.color.colorPrimaryDark)
                            .setButtonText(getString(R.string.dialog_ok_button))
                            .setErrorButtonClick(new Closure() {
                                @Override
                                public void exec() {

                                }
                            })

                            .show();

                }



                break;

            case R.id.nav_dash:
                DashBoardFragment dashFrag= new DashBoardFragment();

                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,dashFrag,"dashHello").disallowAddToBackStack().commit();
                break;

            case R.id.nav_historyOrder:
                OrderHistoryFragment historyFragment= new OrderHistoryFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,historyFragment,"dashHello").disallowAddToBackStack().commit();
                break;

            case R.id.nav_news:
                //Utilities.clearPrefOrderid(PREF_ORDERID_WITHQR,getApplication());


                NewsFragment newsFragment = new NewsFragment();

                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,newsFragment,"newsHello").disallowAddToBackStack().commit();
                break;

            case R.id.nav_raise:
                RaiseDisputeFragment raiseDisputeFragment = new
                        RaiseDisputeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,raiseDisputeFragment,"raiseHello").disallowAddToBackStack().commit();
                break;
            case R.id.nav_facebook:
                Intent facebookAppIntent;
                try {
                    facebookAppIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.facebook.com/thebarebonesbar/"));
                    startActivity(facebookAppIntent);

                } catch (ActivityNotFoundException e) {
                    facebookAppIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.facebook.com/thebarebonesbar/"));
                    startActivity(facebookAppIntent);
                }
                break;
            case R.id.nav_twitter:
                Intent twitterAppIntent;
                try {
                    twitterAppIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://twitter.com/barebones100ft"));
                    startActivity(twitterAppIntent);
                } catch (ActivityNotFoundException e) {
                    twitterAppIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://twitter.com/barebones100ft"));
                    startActivity(twitterAppIntent);
                }
                break;

            case R.id.nav_rate:
                Intent playstoreAppIntent;
                try {
                    playstoreAppIntent = new
                            Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.kre8tives.bareboneabcdpl&hl=en"));
                    startActivity(playstoreAppIntent);

                } catch (ActivityNotFoundException e) {
                    playstoreAppIntent = new
                            Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.kre8tives.bareboneabcdpla"));
                    startActivity(playstoreAppIntent);
                }
                break;
            case R.id.nav_share:
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String sAux = "\nFor Android Phones\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=com.kre8tives.bareboneabcdpla \n\n";
                    sAux = sAux +"For Iphones\n\n"+ "itms://itunes.apple.com/us/app/barebones-bidding-app/id1257636561?ls=1&mt=8 \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);

                    startActivity(Intent.createChooser(i, "choose one"));

                } catch(Exception e) {
                    //e.toString();
                }
                break;
            case R.id.nav_about:
                AboutFragment aboutFragment = new AboutFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,aboutFragment,"aboutHello").disallowAddToBackStack().commit();
                break;
            case R.id.nav_terms:


                TermsFragment termsFragment = new TermsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,termsFragment,"termsHello").disallowAddToBackStack().commit();
                break;

            case R.id.nav_logout:
                db.deleteUser();
                db.deleteStore();
                dbAdapter.deleteitems();
                Utilities.clearPrefOrderid(PREF_ORDERID_WITHQR,getApplication());
                Utilities.clearPrefOrderid(PREF_USER,getApplication());


                Intent inte=new Intent(MainActivity.this,LoginActivity.class);
                inte.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|inte.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(inte);
        }

        if (fragment!=null){

            FragmentTransaction ft =
                    getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame,fragment,fragment.getTag());
            ft.commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());

        return true;
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
                List<Orders> ordersList = response.body();
                historyPojoListM = ordersList;
                /*orderHistoryAdapter = new OrderHistoryAdapter(historyPojoList, getActivity());
                rv_orderhistory1.setAdapter(orderHistoryAdapter);*/
                //test



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
                        break;
                    }
                    else {

                        if (input.equals("settled")) {
                            count++;
                            if(count>counter){
                                status = false;
                               // onStartAlarm();
                                counter=count;
                                qrcode = null;
                                setPref("qrcode", qrcode, getApplicationContext());
                                break;
                            }


                            //  status = false;

/*                            qrcode = null;
                            setPref("qrcode", qrcode, getApplicationContext());*/

                            // qrcode=null;
                        }

                    }

                    Log.d("statussscheck", String.valueOf(status));

                /*    if((LatestStatus.equals("bill_printed"))){
                        //status=true;
                    }

                    if((LatestStatus.equals("settled"))){
                        //status=false;
                    }*/

                    String status=input.substring(0,1).toUpperCase()+input.substring(1);
                    ExpaOrders expaOrderer =new ExpaOrders(ordersList.get(i).getOrderId(),items
                            ,ordersList.get(i).getOrderDate()
                            ,status
                            ,ordersList.get(i).getOrderAmount(), R.drawable.icon_arrow);

                    orders.add(expaOrderer);
                }

              /*  OrdersAdapter adapter = new OrdersAdapter(orders);
                rv_orderhistory1.setAdapter(adapter);*/


                //

                //   progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<List<Orders>> call, Throwable t) {

            }


        });


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

                Log.d("userrespmain",userResponse);
                    for (int i = 0; i < userArray.length(); i++) {

                        JSONObject jobject = userArray.getJSONObject(i);
                        String name = jobject.getString("customer_name");
                        String image = jobject.getString("image");
                        String id = jobject.getString("customer_id");
                        String phone=jobject.getString("phone");
                        String dob=jobject.getString("dob");
                        String email=jobject.getString("email");
                        String level=jobject.getString("level");
                        String imageUrl= BASE_URL+"/"+"upload/"+image;
                        nameTxt.setText(name);
                        Picasso.with(MainActivity.this)
                                .load(imageUrl)
                                .into(imageView) ;
                        phoneTxt.setText(level);
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

            URL url = new URL(USER_DETAILS_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("phone",user_Phone);

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


    @Override
    public void setTitle(final CharSequence title) {
        toolbar.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.super.setTitle(title);
            }
        }, 100);
    }
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        ////for service
        IntentFilter filter = new IntentFilter();
        filter.addAction("SOME_ACTION");


        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getStringExtra("null").equals("clear")) {
                    Toast.makeText(context, "Here stop alaram", Toast.LENGTH_SHORT).show();
                    stopservice=true;

                }
            }
        };


        registerReceiver(receiver, filter);
        Log.d("Main","Recvr registered");

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("qrservice", "started");

        String checkqr = "table";
        if (requestCode == OrderPreviewFragment.QR_REQUEST) {
            if (resultCode == RESULT_OK) {

                String qrData = data.getStringExtra(QRActivity.EXTRA_QR_RESULT);
                if (qrData != null && qrData.toLowerCase().contains(checkqr)) {
                    if (qrData.contains("{")) {
                        Toast.makeText(this, "Wrong QR Scanned", Toast.LENGTH_SHORT).show();
                    } else {
                        qrcode = qrData;
                        setPref("qrcode", qrcode, this);
                        try {

                            Toast.makeText(this, "Your table has been changed successfully!", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }

            } else {
                Toast.makeText(this, "Wrong QR ", Toast.LENGTH_SHORT).show();
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        Log.d("main","OnDestroy");
        db.close();
        super.onDestroy();
    }
}



package com.kre8tives.bareboneneww.MarketCrash;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kre8tives.bareboneneww.Fragments.BiddingFragment;
import com.kre8tives.bareboneneww.Fragments.BuyNowFragment;
import com.kre8tives.bareboneneww.Fragments.OrderPreviewFragment;
import com.kre8tives.bareboneneww.Model.LiquorImagePrice;
import com.kre8tives.bareboneneww.R;
import com.kre8tives.bareboneneww.Util.ServerConnection;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.kre8tives.bareboneneww.Adapter.FoodAdapter.Fid;
import static com.kre8tives.bareboneneww.Adapter.FoodAdapter.FidnewId;
import static com.kre8tives.bareboneneww.Adapter.FoodAdapter.Fname;
import static com.kre8tives.bareboneneww.Adapter.FoodAdapter.Fnewprice;
import static com.kre8tives.bareboneneww.Adapter.FoodAdapter.Fprice;
import static com.kre8tives.bareboneneww.Adapter.FoodAdapter.Fqty;
import static com.kre8tives.bareboneneww.Adapter.SelectStoreAdapter.storeId;
import static com.kre8tives.bareboneneww.MarketCrash.MarketCrash.total;
import static com.kre8tives.bareboneneww.Util.Utilities.getPref;

public class LiquorItemPriceFragment extends Fragment {
    public static String LIQUOR_API = "http://barebonesbar.com/bbapi/product_detailsapi.php";
   // public static String LIQUOR_API = "http://barebonesbar.com/bbapi/product_details_ios.php";
    Integer timeout = 0;
    private Context mContext;
    int minteger;
    String responseStr = "";
    List<LiquorImagePrice> liquorImagePriceList;


    //
    String categoryId,productId,ItemName;
    String storeid;
    ImageView refresh;
    TextView tv_liqname,tv_liqmaxprice,tv_liqminprice,buyNowmsg;
    Button btnbuynw,btnbidnow,btncrash;
    ImageView liqimg;
    String buyNow,max,min,name,bidacceptance,id,newid;
    LinearLayout ll_Prices,ll_stopwatch,ll_button;
    TextView tv_stopwatch;
    ProgressDialog progressDialog;

    CountDownTimer countDownTimer;
    long mills,avgtimeinterval;
    String date,starttime,endtime;
    Time mintime;
    Boolean marketcras=false;
    long difference;
    String diff;
    int Hours,Mins;
    private final Handler handler = new Handler();
    public static final String inputFormat = "HH:mm";


    String formattedDate;
    public LiquorItemPriceFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_liquor_item_price, container, false);
        liquorImagePriceList = new ArrayList<LiquorImagePrice>();
        storeid=storeId;

        //tollbar
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Liquor");


        tv_liqname=(TextView)view.findViewById(R.id.tv_liquorimgname);
        tv_liqmaxprice=(TextView)view.findViewById(R.id.tv_liquorimgPriceMax) ;
        tv_liqminprice=(TextView)view.findViewById(R.id.tv_liquorimgPriceMin) ;
        buyNowmsg=view.findViewById(R.id.buynowcrash);
        ll_button=view.findViewById(R.id.price_layout);
        btnbuynw=(Button)view.findViewById(R.id.btn_imgpricebuynw);
        btnbidnow=(Button)view.findViewById(R.id.btn_imgpricebidnw);


        liqimg=(ImageView)view.findViewById(R.id.imageView_ItmPrice) ;
        ll_Prices=(LinearLayout)view.findViewById(R.id.ll_Prices);

        ll_stopwatch=(LinearLayout)view.findViewById(R.id.ll_stopwatch);
        btncrash=view.findViewById(R.id.crash_price);
        tv_stopwatch=(TextView)view.findViewById(R.id.tv_stopwatch);
        ll_stopwatch.setVisibility(View.VISIBLE);
        tv_stopwatch.setVisibility(View.GONE);
        btncrash.setVisibility(View.GONE);
        buyNowmsg.setVisibility(View.GONE);

        ll_Prices.setVisibility(View.GONE);
        btnbidnow.setVisibility(View.GONE);
        btnbuynw.setVisibility(View.GONE);
        tv_liqmaxprice.setVisibility(View.GONE);
        tv_liqminprice.setVisibility(View.GONE);

        categoryId=getArguments().getString("categoryId");
        productId=getArguments().getString("ItemId");
        ItemName=getArguments().getString("ItemName");
        getActivity().setTitle(ItemName);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
         formattedDate = df.format(c.getTime());
        refresh=(ImageView)view.findViewById(R.id.refresh_itemprcimg);
        refresh.setVisibility(View.GONE);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeout=0;
                refresh.setVisibility(View.GONE);

                new getItem().execute();
            }
        });
        new getItem().execute();
        doTheAutoRefresh();
        btnbuynw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*BuyNowFragment buyNowFragment = new BuyNowFragment();
                Bundle b=new Bundle();
                b.putString("buyNowPrice",buyNow);
                b.putString("ItemName",name);
                b.putString("id",id);
                b.putString("newidbuy",newid);
                buyNowFragment.setArguments(b);
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, buyNowFragment);
                fragmentTransaction.addToBackStack(buyNowFragment.getClass().getName());
                fragmentTransaction.commit();*/
                //
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater=getActivity().getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                dialogBuilder.setView(dialogView);
                final TextView edt = (TextView) dialogView.findViewById(R.id.edit1);
                //Plus minus
                edt.setText("0");
                final Button incr=dialogView.findViewById(R.id.increase);
                final Button decr=dialogView.findViewById(R.id.decrease);
                dialogBuilder.setMessage("Please Enter Quantity");
                //
                incr.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        minteger = minteger + 1;
                        if(minteger>50){
                            edt.setText("0");
                            minteger=0;
                        }else{
                            edt.setText(""+minteger);}

                    }
                });
                decr.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        minteger = minteger - 1;
                        if(minteger<0){
                            edt.setText("0");
                            minteger=0;
                        }else{
                            edt.setText(""+minteger);}


                    }
                });
                //ends plus minus
                dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        if (Integer.parseInt(edt.getText().toString())==0){
                            Vibrator v = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                            // Vibrate for 500 milliseconds
                            v.vibrate(100);
                            Toast.makeText(getContext(), "Quantity can not be zero!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            float quantity = Float.parseFloat(edt.getText().toString());
                            float price = Float.parseFloat(buyNow);
                            float TotalPrice = quantity * price;
                            Fname = name;
                            Fprice = String.valueOf(TotalPrice);
                            Fnewprice= String.valueOf(price);
                            Fqty = edt.getText().toString();
                            Fid = id;
                            FidnewId = newid;
                            OrderPreviewFragment orderPreviewFragment = new OrderPreviewFragment();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame, orderPreviewFragment);
                            fragmentTransaction.addToBackStack(orderPreviewFragment.getClass().getName());
                            fragmentTransaction.commit();
                        }
                    }
                });

                AlertDialog b = dialogBuilder.create();
                b.show();

                //
            }
        });

        btnbidnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BiddingFragment biddingFragment=new BiddingFragment();
                Bundle bid=new Bundle();
                bid.putString("minPrice",min);
                bid.putString("buyNowPrice",buyNow);
                bid.putString("maxPrice",max);
                bid.putString("ItemName",name);
                bid.putString("acceptance",bidacceptance);
                bid.putString("id",id);
                bid.putString("newid",newid);
                biddingFragment.setArguments(bid);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, biddingFragment);
                fragmentTransaction.addToBackStack(biddingFragment.getClass().getName());
                fragmentTransaction.commit();
            }
        });
        btncrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyNowFragment buyNowFragment = new BuyNowFragment();
                Bundle b=new Bundle();
                b.putString("buyNowPrice",buyNow);
                b.putString("ItemName",name);
                b.putString("id",id);
                b.putString("newidbuy",newid);
                buyNowFragment.setArguments(b);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, buyNowFragment);
                fragmentTransaction.addToBackStack(buyNowFragment.getClass().getName());
                fragmentTransaction.commit();
            }
        });

        return view;
    }


    private void doTheAutoRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new getItem().execute();
            }
        }, 10000);
    }

    public class getItem extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            liquorImagePriceList=new ArrayList<LiquorImagePrice>();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            postItem();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if (timeout == 1) {
                refresh.setVisibility(View.VISIBLE);
            } else {
                refresh.setVisibility(View.GONE);
                if(responseStr!=null)
                {
                        progressDialog.dismiss();
                    try {
                        JSONObject wholeObject = new JSONObject(responseStr);
                        Log.d("getitem_market",responseStr);
                        JSONArray liquorArrayitem = wholeObject.getJSONArray("product");
                        for (int i = 0; i < liquorArrayitem.length(); i++) {
                            JSONObject jobjectitem = liquorArrayitem.getJSONObject(i);
                            id = jobjectitem.getString("id");//
                            newid = jobjectitem.getString("_id");
                            name = jobjectitem.getString("liquor_name");
                            max = jobjectitem.getString("max_price");
                            min = jobjectitem.getString("min_price");
                            buyNow = jobjectitem.getString("buy_now_price");
                            bidacceptance = jobjectitem.getString("bidacceptance_price");
                            String image = jobjectitem.getString("image");
                            String imageUrl = "http://barebonesbar.com/bbapi/upload/" + image;
                            Picasso.with(getActivity()).load(imageUrl).into(liqimg);
                            String getCrash = getPref("marketcrash", getContext());
                            //  String endtime=getPref("endtime",getContext());

                           /* setPref("endtime","12:25:44",getApplicationContext());
                            setPref("marketcrash","on",getApplicationContext());*/

                            tv_liqname.setText(name);
                            tv_liqminprice.setText(min);
                            tv_liqmaxprice.setText(max);
                            btnbuynw.setText("Buy now @" + buyNow);

                            tv_liqmaxprice.setVisibility(View.VISIBLE);
                            tv_liqminprice.setVisibility(View.VISIBLE);
                            ll_Prices.setVisibility(View.VISIBLE);
                            ll_stopwatch.setVisibility(View.GONE);
                            btnbuynw.setVisibility(View.VISIBLE);
                            btnbidnow.setVisibility(View.VISIBLE);
                            tv_liqmaxprice.setVisibility(View.VISIBLE);
                            tv_liqminprice.setVisibility(View.VISIBLE);
                            ll_Prices.setVisibility(View.VISIBLE);
                            ll_stopwatch.setVisibility(View.GONE);
                            btnbuynw.setVisibility(View.VISIBLE);
                            btnbidnow.setVisibility(View.VISIBLE);


                    }


                        JSONArray liquorArraycrash = wholeObject.getJSONArray("market_crash");
                        Log.d("liquorArraycrash",liquorArraycrash.toString());
                        for (int i = 0; i < liquorArraycrash.length(); i++) {
                            JSONObject jobjectcustomer = liquorArraycrash.getJSONObject(i);
                            marketcras = jobjectcustomer.getBoolean("market_crash");
                            date = jobjectcustomer.getString("date");
                            starttime = jobjectcustomer.getString("start_time");
                            endtime = jobjectcustomer.getString("end_time");
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            Date birthDate1 = sdf.parse(starttime);
                            Date birthDate2 = sdf.parse(endtime);
                            Date currenttime=sdf.parse(formattedDate);
                            Hours = (int) (mills/(1000 * 60 * 60));
                            Mins = (int) (mills/(1000*60)) % 60;
                            Log.d("currenttimeeee", String.valueOf(currenttime));
                            Log.d("birthdate22", String.valueOf(birthDate2));

                            if(marketcras==true){
                                mills = birthDate2.getTime()-currenttime.getTime();
                                timerstart();
                                ll_Prices.setVisibility(View.INVISIBLE);
                                ll_stopwatch.setVisibility(View.VISIBLE);
                            }
                            else{
                                ll_stopwatch.setVisibility(View.GONE);
                                ll_Prices.setVisibility(View.VISIBLE);
                            }
                        }






   /*                       //  if(getCrash!=null&&getCrash.equals("on")){
                            Log.d("crashflagliqvalue",crashFlag);
                            if(crashFlag.equals(false)){


                                //mills = endtime-currenttime.getTime();
                              //  timerstart();
                           Log.d("helloiammmhere","hhhhhhh");
                                updateGUI();
                                ll_Prices.setVisibility(View.INVISIBLE);
                                ll_stopwatch.setVisibility(View.VISIBLE);

                                ll_Prices.setVisibility(View.GONE);
                                btnbidnow.setVisibility(View.GONE);
                                btnbuynw.setVisibility(View.GONE);
                                tv_liqmaxprice.setVisibility(View.GONE);
                                tv_liqminprice.setVisibility(View.GONE);
                                //ll_stopwatch.setVisibility(View.VISIBLE);
                                btncrash.setText(buyNow);




                            }else{

                                tv_liqmaxprice.setVisibility(View.VISIBLE);
                                tv_liqminprice.setVisibility(View.VISIBLE);
                                ll_Prices.setVisibility(View.VISIBLE);
                                ll_stopwatch.setVisibility(View.GONE);
                                btnbuynw.setVisibility(View.VISIBLE);
                                btnbidnow.setVisibility(View.VISIBLE);}




                        }*/
                    
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }

            }
        }
    }


    private void postItem()
    {

        HttpURLConnection conn = null;
        try {

            URL url = new URL(LIQUOR_API);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("category_id",categoryId)
                    .appendQueryParameter("product_id",productId)
                    .appendQueryParameter("store_id",storeid);

            String query = builder.build().getEncodedQuery();
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
            responseStr = ServerConnection.getData(conn);
        }

        catch (Exception e) {
            timeout = 1;
            progressDialog.dismiss();
        } finally {
            {
                if (conn != null) {
                    conn.disconnect();
                }

            }
        }
    }


    public void timerstart(){
        countDownTimer=new CountDownTimer( mills,1000) {//adjust the milli seconds here
            @Override
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                        TimeUnit.MILLISECONDS.toMinutes(millis) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                        TimeUnit.MILLISECONDS.toSeconds(millis) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                String text = String.format(Locale.getDefault(), "Time Remaining %02d min: %02d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                tv_stopwatch.setText(hms);
            }
            @Override
            public void onFinish() {
                tv_stopwatch.setText("Done!");
                ll_Prices.setVisibility(View.VISIBLE);
                ll_stopwatch.setVisibility(View.INVISIBLE);
            }
        };
        countDownTimer.start();
    }

    //Market Crash Logic
    private BroadcastReceiver crash=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ll_Prices.setVisibility(View.INVISIBLE);
            ll_stopwatch.setVisibility(View.VISIBLE);
        }
    };

   /* private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGUI(intent); // or whatever method used to update your GUI fields
        }
    };
    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(br, new IntentFilter(CounterService.COUNTDOWN_BR));
        getActivity().registerReceiver(crash,new IntentFilter(MarketCrash.CRASHBROAD));
        Log.i("counter", "Registered broacast receiver");

    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(br);
        getActivity().unregisterReceiver(crash);
        Log.i("counter", "Unregistered broacast receiver");
    }
    @Override
    public void onStop() {
        try {
            getActivity().unregisterReceiver(br);
            getActivity().unregisterReceiver(crash);
        } catch (Exception e) {
            // Receiver was probably already stopped in onPause()
        }
        super.onStop();
    }*/
    private void updateGUI() {

          //  long total = intent.getLongExtra("countdown", 0);
            //Millis to Hrs

            String tv=String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(total),
                    TimeUnit.MILLISECONDS.toMinutes(total) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(total)),
                    TimeUnit.MILLISECONDS.toSeconds(total) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(total)));


            ll_stopwatch.setVisibility(View.VISIBLE);
            tv_stopwatch.setText(tv);

            Log.i("counter", "Countdown seconds remaining: " +  total / 1000);

    }








}

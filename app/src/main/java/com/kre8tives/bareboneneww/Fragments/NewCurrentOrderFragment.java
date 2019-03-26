package com.kre8tives.bareboneneww.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kre8tives.bareboneneww.Adapter.NewCurrentOrderAdapter;
import com.kre8tives.bareboneneww.Database.DBAdapter;
import com.kre8tives.bareboneneww.Model.NewCurrentOrder;
import com.kre8tives.bareboneneww.R;
import com.kre8tives.bareboneneww.Util.ServerConnection;
import com.kre8tives.bareboneneww.Util.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.kre8tives.bareboneneww.Activity.MainActivity.userid;
import static com.kre8tives.bareboneneww.Adapter.BiddingPreviewAdapter.biddingPreviewModelss;
import static com.kre8tives.bareboneneww.Adapter.BuyNowAdapter.buyNowModelss;
import static com.kre8tives.bareboneneww.Adapter.FoodPreviewAdapter.foodPreviewModelArrayList;
import static com.kre8tives.bareboneneww.Adapter.MixturePreviewAdapter.mixturePreviewModelss;
import static com.kre8tives.bareboneneww.Adapter.SelectStoreAdapter.storeId;


public class NewCurrentOrderFragment extends Fragment {
    public static String BRAND_URL = "http://barebonesbar.com/bbapi/cart_itemapi.php";
    Integer timeout = 0;
    String responseStr = "";
    private int delay;
    public static List<NewCurrentOrder> newCurrentOrderList;
    NewCurrentOrderAdapter newCurrentOrderAdapter;
    RecyclerView rv_newcurrentOrderitem;
    public static TextView tv_nwcurrentordergrandtotal;
    public final Handler handler = new Handler();
    DBAdapter db;
    FloatingActionButton floatingActionButton;
    ImageView refresh;
    public static String CorderId;
    TextView tv_nwcurrentStatus;
    public  static String itemstatuss="";
    String itemqty,itemprice;
    String orderamount;
    public static String orderid="";
   // public static boolean status=false;


    public NewCurrentOrderFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_current_order, container, false);
        getActivity().setTitle("Current Order");
        refresh = (ImageView) view.findViewById(R.id.nwrefreshbrand);
        tv_nwcurrentStatus=(TextView)view.findViewById(R.id.tv_nwcurrentStatus);
        rv_newcurrentOrderitem = (RecyclerView) view.findViewById(R.id.rv_newcurrentOrderitem);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        tv_nwcurrentordergrandtotal = (TextView) view.findViewById(R.id.tv_nwcurrentordergrandtotal);
        rv_newcurrentOrderitem.setLayoutManager(new LinearLayoutManager(getActivity()));

        delay=30000;

        if (foodPreviewModelArrayList != null && biddingPreviewModelss != null && buyNowModelss != null && mixturePreviewModelss != null) {
            foodPreviewModelArrayList.clear();
            biddingPreviewModelss.clear();
            buyNowModelss.clear();
            mixturePreviewModelss.clear();
        }
        //grandtotal.setText("0");
        CorderId = getArguments().getString("Preorderid");
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Please Select!");
                alertDialog.setMessage("What you want to add?");
                alertDialog.setPositiveButton("Add Liquor", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CategoryFragment categoryFragment = new CategoryFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, categoryFragment);
                        fragmentTransaction.addToBackStack(categoryFragment.getClass().getName());
                        fragmentTransaction.commit();
                    }
                });
                alertDialog.setNegativeButton("Add Food", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FoodCategoryFragment foodCategoryFragment = new FoodCategoryFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, foodCategoryFragment);
                        fragmentTransaction.addToBackStack(foodCategoryFragment.getClass().getName());
                        fragmentTransaction.commit();
                    }
                });
                alertDialog.setNeutralButton("Add Mixer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        NewMixerFragment mixerFragment = new NewMixerFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, mixerFragment);
                        fragmentTransaction.addToBackStack(mixerFragment.getClass().getName());
                        fragmentTransaction.commit();
                    }
                });

                alertDialog.show();
            }
        });

        refresh.setVisibility(View.GONE);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeout = 0;
                refresh.setVisibility(View.GONE);
                new getCurrentOrder().execute();
            }
        });
        return view;
    }



    public class getCurrentOrder extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            newCurrentOrderList = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... params) {
            postClient();
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("neworderresponse123",responseStr);


            if (timeout == 1) {
                refresh.setVisibility(View.VISIBLE);
                rv_newcurrentOrderitem.setVisibility(View.GONE);
            } else {
                rv_newcurrentOrderitem.setVisibility(View.VISIBLE);
                refresh.setVisibility(View.GONE);

                if (responseStr == null) {
                    refresh.setVisibility(View.VISIBLE);
                    rv_newcurrentOrderitem.setVisibility(View.GONE);
                } else {
                    try {

                        JSONObject wholeObject = new JSONObject(responseStr);
                        orderamount = wholeObject.getString("order_amount");
                        orderid=wholeObject.getString("order_id");
                        Utilities.setPref("order_id",orderid,getActivity());
                        itemstatuss = wholeObject.getString("order_status");
                        JSONArray textArray = wholeObject.getJSONArray("items");
                        for (int i = 0; i < textArray.length(); i++) {
                            NewCurrentOrder newCurrentOrder = new NewCurrentOrder();
                            JSONObject jobjectt = textArray.getJSONObject(i);
                            String itemid = jobjectt.getString("_id");
                            String itemname = jobjectt.getString("item_name");
                            itemqty = jobjectt.getString("item_quantity");
                            itemprice = jobjectt.getString("item_price");
                            String subtotal=jobjectt.getString("item_total");
                            newCurrentOrder.setCname(itemname);
                            newCurrentOrder.setCqty(itemqty);
                            newCurrentOrder.setCprice(itemprice);
                            newCurrentOrder.setCsubtotal(subtotal);
                            tv_nwcurrentStatus.setText(itemstatuss);
                            tv_nwcurrentordergrandtotal.setText(orderamount);
                            newCurrentOrderList.add(newCurrentOrder);

                            if((itemstatuss.equals("bill_printed"))){
                               // status=true;
                            }

                            if((itemstatuss.equals("settled"))){
                              //  status=false;
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                  if(newCurrentOrderList!=null){

                    newCurrentOrderAdapter = new NewCurrentOrderAdapter(newCurrentOrderList, getActivity());
                    rv_newcurrentOrderitem.setAdapter(newCurrentOrderAdapter);
                }
                else {
                      Toast.makeText(getActivity(), "No Current Orders", Toast.LENGTH_SHORT).show();
                  }

                }
            }
        }
    }





    public void postClient() {

        HttpURLConnection conn = null;
        try {

            URL url = new URL(BRAND_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("customer_id", userid)
                    .appendQueryParameter("store_id", storeId);
                    /*.appendQueryParameter("qr_code", Utilities.getPref("qrcode",getActivity()))
                    .appendQueryParameter("order_id", Utilities.getPref("orderid", getActivity()))
                    .appendQueryParameter("current_status", "New");*/
            Log.d("useridddd",userid);
            Log.d("storeeeeidd",storeId);
            Log.d("orderrrrrid",CorderId);
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
    protected void exitByBackKey() {
        DashBoardFragment mixerFragment = new DashBoardFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, mixerFragment);
        fragmentTransaction.addToBackStack(mixerFragment.getClass().getName());
        fragmentTransaction.commit();
    }

    @Override
    public void onStop() {

        super.onStop();
    }

    @Override
    public void onResume() {

        super.onResume();
        new getCurrentOrder().execute();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    exitByBackKey();
                    return true;
                }
                return false;
            }
        });

    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setVisible(false);
    }
    @Override
    public void onDestroy() {
        Log.d("Newcurrentorderfragment","on destroy");
        super.onDestroy();



    }

}

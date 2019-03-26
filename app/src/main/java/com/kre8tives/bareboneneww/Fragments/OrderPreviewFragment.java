package com.kre8tives.bareboneneww.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.gnzlt.AndroidVisionQRReader.QRActivity;
import com.inthecheesefactory.thecheeselibrary.fragment.support.v4.app.StatedFragment;
import com.kre8tives.bareboneneww.Adapter.BiddingPreviewAdapter;
import com.kre8tives.bareboneneww.Adapter.BuyNowAdapter;
import com.kre8tives.bareboneneww.Adapter.FoodPreviewAdapter;
import com.kre8tives.bareboneneww.Adapter.MixtureAdapter;
import com.kre8tives.bareboneneww.Adapter.MixturePreviewAdapter;
import com.kre8tives.bareboneneww.Adapter.NewMixtureAdapter;
import com.kre8tives.bareboneneww.Database.DBAdapter;
import com.kre8tives.bareboneneww.Database.DataHelper;
import com.kre8tives.bareboneneww.Database.getOrderJsonarray;
import com.kre8tives.bareboneneww.ExpandableOrders.NewCurrentOrdersFragment;
import com.kre8tives.bareboneneww.Model.BiddingPreviewModel;
import com.kre8tives.bareboneneww.Model.BuyNowModel;
import com.kre8tives.bareboneneww.Model.FoodPreviewModel;
import com.kre8tives.bareboneneww.Model.MixturePreviewModel;
import com.kre8tives.bareboneneww.MyApplication;
import com.kre8tives.bareboneneww.Orders.TotalOrdersService;
import com.kre8tives.bareboneneww.R;
import com.kre8tives.bareboneneww.Util.ServerConnection;
import com.kre8tives.bareboneneww.Util.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.kre8tives.bareboneneww.Activity.MainActivity.status;
import static com.kre8tives.bareboneneww.Activity.MainActivity.user_Phone;
import static com.kre8tives.bareboneneww.Activity.MainActivity.userid;
import static com.kre8tives.bareboneneww.Activity.MainActivity.username;
import static com.kre8tives.bareboneneww.Adapter.BiddingPreviewAdapter.biddingPreviewModelss;
import static com.kre8tives.bareboneneww.Adapter.BuyNowAdapter.buyNowModelss;
import static com.kre8tives.bareboneneww.Adapter.FoodAdapter.Fid;
import static com.kre8tives.bareboneneww.Adapter.FoodAdapter.FidnewId;
import static com.kre8tives.bareboneneww.Adapter.FoodAdapter.Fname;
import static com.kre8tives.bareboneneww.Adapter.FoodAdapter.Fnewprice;
import static com.kre8tives.bareboneneww.Adapter.FoodAdapter.Fprice;
import static com.kre8tives.bareboneneww.Adapter.FoodAdapter.Fqty;
import static com.kre8tives.bareboneneww.Adapter.FoodPreviewAdapter.foodPreviewModelArrayList;
import static com.kre8tives.bareboneneww.Adapter.MixtureAdapter.mname;
import static com.kre8tives.bareboneneww.Adapter.MixtureAdapter.mnewid;
import static com.kre8tives.bareboneneww.Adapter.MixtureAdapter.mnewprice;
import static com.kre8tives.bareboneneww.Adapter.MixtureAdapter.mprice;
import static com.kre8tives.bareboneneww.Adapter.MixtureAdapter.mquantity;
import static com.kre8tives.bareboneneww.Adapter.MixturePreviewAdapter.mixturePreviewModelss;
import static com.kre8tives.bareboneneww.Adapter.NewMixtureAdapter.newmixturenewid;
import static com.kre8tives.bareboneneww.Adapter.NewMixtureAdapter.newname;
import static com.kre8tives.bareboneneww.Adapter.NewMixtureAdapter.newprice;
import static com.kre8tives.bareboneneww.Adapter.NewMixtureAdapter.newquantity;
import static com.kre8tives.bareboneneww.Adapter.NewMixtureAdapter.newsinglemixtureprice;
import static com.kre8tives.bareboneneww.Adapter.SelectStoreAdapter.store_id;

import static com.kre8tives.bareboneneww.Util.Utilities.getPref;
import static com.kre8tives.bareboneneww.Util.Utilities.setPref;



//qr


public class OrderPreviewFragment extends StatedFragment {
    public static Integer timeout = 0;

    ProgressDialog progressDialog;
    RecyclerView rv1;
    BuyNowAdapter buyNowAdapter;
    MixturePreviewAdapter mixturePreviewAdapter;
    BiddingPreviewAdapter biddingPreviewAdapter;
    FoodPreviewAdapter foodPreviewAdapter;
    ArrayList<FoodPreviewModel> foodPreviewModels = new ArrayList<>();
    ArrayList<BuyNowModel> buyNowModels = new ArrayList<>();
    ArrayList<BiddingPreviewModel> biddingPreviewModels = new ArrayList<>();
    ArrayList<MixturePreviewModel> mixturePreviewModels = new ArrayList<>();
    ArrayList<Float> calcList = new ArrayList<>();
    public Button btn_confirm, btn_addmore;
    DataHelper dataHelper;
    public static DBAdapter db;
    public static String prefOrderid;
    String MixtureName, MixtureQuantity, MixturePrice, Mixtureid, Mixturenewprice;
    String NewMixtureName, NewMixtureQuantity, NewMixturePrice, NewMixtureid, NewMixtureSingleprice;
    String FoodName, FoodQuantity, FoodPrice, Foodid, FoodNewprice;
    ///For status

    public static TextView grandtotal;
    getOrderJsonarray polldata = new getOrderJsonarray();
    public static String updateResponse = "";

    public static String qrcode;
    public static JSONObject jsonObj;
    public static String iname, iquantity, iprice, isingleprice;
    Button btn_deletecart;
    public static String newId, foodnewid;
    public static int iid;
    public static String neworderid;
    String checknewid, checkname;
    public static final int QR_REQUEST = 1234;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().startService(new Intent(getActivity(),TotalOrdersService.class));
    }

    //This call is important
    public OrderPreviewFragment() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_order_preview, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Order Preview");
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //Check for orders

        qrcode = getPref("qrcode",getActivity());


        btn_deletecart = (Button) view.findViewById(R.id.btn_deletecart);
        grandtotal = (TextView) view.findViewById(R.id.txt_grandtotal);
        db = new DBAdapter(getActivity());
        dataHelper = new DataHelper(getActivity());
        MixtureName = mname;
        MixtureQuantity = mquantity;
        MixturePrice = mprice;
        Mixturenewprice = mnewprice;
        Mixtureid = mnewid;
        NewMixtureName = newname;
        NewMixtureQuantity = newquantity;
        NewMixturePrice = newprice;
        NewMixtureSingleprice = newsinglemixtureprice;
        NewMixtureid = newmixturenewid;
        FoodName = Fname;
        FoodQuantity = Fqty;
        FoodPrice = Fprice;
        FoodNewprice = Fnewprice;
        Foodid = Fid;
        foodnewid = FidnewId;
        db.openDB();
        if (MixtureName != null && MixtureQuantity != null && MixturePrice != null && Mixturenewprice != null) {
            db.add(Mixtureid, MixtureName, MixtureQuantity, MixturePrice, Mixturenewprice);
            mname = null;
            mquantity = null;
            mprice = null;
            MixtureAdapter.mid = null;
            mnewprice = null;
        }

        if (NewMixtureName != null && NewMixtureQuantity != null && NewMixturePrice != null && NewMixtureSingleprice != null) {
            db.add(NewMixtureid, NewMixtureName, NewMixtureQuantity, NewMixturePrice, NewMixtureSingleprice);
            newname = null;
            newquantity = null;
            newprice = null;
            NewMixtureAdapter.newid = null;
            newsinglemixtureprice = null;
        }

        if (FoodName != null && FoodQuantity != null && FoodPrice != null && FoodNewprice != null) {
            db.add(foodnewid, FoodName, FoodQuantity, FoodPrice, FoodNewprice);
            Fname = null;
            Fqty = null;
            Fprice = null;
            Fid = null;
            FidnewId = null;
            FoodNewprice = null;
        }
        buyNowModels.clear();
        mixturePreviewModels.clear();
        foodPreviewModels.clear();
        biddingPreviewModels.clear();
        Cursor c = db.getAllPlayers();
        while (c.moveToNext()) {
            int id = c.getInt(0);
            String newid = c.getString(1);
            String name = c.getString(2);
            String quantity = c.getString(3);
            String price = c.getString(4);
            String singleprice = c.getString(5);
            BuyNowModel p = new BuyNowModel(id, newid, name, quantity, price, singleprice);
            BiddingPreviewModel biddingPreviewModel = new BiddingPreviewModel(id, newid, name, quantity, price, singleprice);
            MixturePreviewModel previewModel = new MixturePreviewModel(id, newid, name, quantity, price, singleprice);
            FoodPreviewModel foodPreviewModel = new FoodPreviewModel(id, newid, name, quantity, price, singleprice);
            buyNowModels.add(p);
            biddingPreviewModels.add(biddingPreviewModel);
            mixturePreviewModels.add(previewModel);
            foodPreviewModels.add(foodPreviewModel);
            calcList.add(Float.parseFloat(price));
            float sum = 0;
            for (int i = 0; i < calcList.size(); i++) {
                sum = calcList.get(i) + sum;
            }


            grandtotal.setText(String.valueOf(sum));
        }
        rv1 = (RecyclerView) view.findViewById(R.id.bidConfirmRecycler);
        rv1.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv1.setItemAnimator(new DefaultItemAnimator());
        buyNowAdapter = new BuyNowAdapter(getActivity(), buyNowModels);
        biddingPreviewAdapter = new BiddingPreviewAdapter(getActivity(), biddingPreviewModels);
        mixturePreviewAdapter = new MixturePreviewAdapter(getActivity(), mixturePreviewModels);
        foodPreviewAdapter = new FoodPreviewAdapter(getActivity(), foodPreviewModels);
        rv1.setAdapter(buyNowAdapter);
        rv1.setAdapter(biddingPreviewAdapter);
        rv1.setAdapter(mixturePreviewAdapter);
        rv1.setAdapter(foodPreviewAdapter);
        btn_confirm = (Button) view.findViewById(R.id.bidConf_btn_order);
        btn_addmore = (Button) view.findViewById(R.id.bidConf_btn_addmore);
        //From Menu
        String menu = getArguments().getString("menu");
        if (menu != null && menu.equals("yes")) {
            btn_addmore.setVisibility(View.GONE);
            btn_confirm.setVisibility(View.GONE);
            btn_deletecart.setVisibility(View.GONE);
            toolbar.setTitle("From Menu");
        }
        //

        btn_addmore.setOnClickListener(new View.OnClickListener() {
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
   /*     btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.openDB();

                Cursor c = db.getAllPlayers();
                while (c.moveToNext()) {

                    checknewid = c.getString(1);
                    checkname = c.getString(2);
                }

                if (checkname == null && checknewid == null) {
                    Toast.makeText(getActivity(), "Cart is Empty.Please add items to proceed.", Toast.LENGTH_LONG).show();
                } else {
                    if(qrcode!=null){
                        new sendOrder().execute();
                    }else {
                    Intent qrScanIntent = new Intent(getActivity(), QRActivity.class);
                    startActivityForResult(qrScanIntent, QR_REQUEST);}

                }


            }
        });*/


        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.openDB();

                Cursor c = db.getAllPlayers();
                int abc = c.getCount();
                Log.d("cursor lenght", abc + "");
                while (c.moveToNext()) {

                    checknewid = c.getString(1);
                    checkname = c.getString(2);
                }

                if (checkname == null && checknewid == null) {
                    Toast.makeText(getActivity(), "Cart is Empty.Please add items to proceed.", Toast.LENGTH_LONG).show();
                } else {


                  /*  if ((qrcode ==null)|| (LatestStatus.equals("cancelled"))||LatestStatus.equals("settled")){
                        //Google
                        qrcode=null;
                        Intent qrScanIntent = new Intent(getActivity(), QRActivity.class);
                        startActivityForResult(qrScanIntent, QR_REQUEST);
*/

                    if((status==true)){    //ie bill printed

                        //Toast.makeText(getActivity(), "Please wait! You can not place order until your current bill is settled", Toast.LENGTH_SHORT).show();

                        showAlert("", "Please wait! You can not place order until your current bill is settled" +new String(Character.toChars(0x1F61C)), "bill_printed");
                    }


                   else {



                        if ((qrcode ==null)){
                            //Google
                            // qrcode=null;
                            Intent qrScanIntent = new Intent(getActivity(), QRActivity.class);
                            startActivityForResult(qrScanIntent, QR_REQUEST);

                        }
                        else{


                            new sendOrder().execute();
                        }


                    }
                }
            }
        });



        btn_deletecart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                // alertDialog.setTitle("Alert!");
                alertDialog.setMessage("Are You Sure,You want to Delete all items?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteitems();
                        foodPreviewModelArrayList.clear();
                        biddingPreviewModelss.clear();
                        buyNowModelss.clear();
                        mixturePreviewModelss.clear();
                        rv1.setAdapter(buyNowAdapter);
                        rv1.setAdapter(biddingPreviewAdapter);
                        rv1.setAdapter(mixturePreviewAdapter);
                        rv1.setAdapter(foodPreviewAdapter);
                        grandtotal.setText("0");
                    }

                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }

        });
        //getActivity().startService(new Intent(getActivity(),TotalOrdersService.class));

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("qrservice", "started");

        String checkqr = "table";
        if (requestCode == QR_REQUEST) {
            if (resultCode == RESULT_OK) {

                String qrData = data.getStringExtra(QRActivity.EXTRA_QR_RESULT);
                if (qrData != null && qrData.toLowerCase().contains(checkqr)) {
                    if (qrData.contains("{")) {
                        Toast.makeText(getActivity(), "Wrong QR Scanned", Toast.LENGTH_SHORT).show();
                    } else {
                        qrcode = qrData;
                        setPref("qrcode", qrcode, getActivity());
                        try {

                            Toast.makeText(getActivity(), "Sending your order", Toast.LENGTH_SHORT).show();
                            new sendOrder().execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }

            } else {
                Toast.makeText(getActivity(), "Wrong QR ", Toast.LENGTH_SHORT).show();
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showAlert(String title,String msg,String type) {
        switch(type){
            case "bill_printed":
                new AwesomeErrorDialog(getActivity())
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

          /*  case "success":
                new AwesomeSuccessDialog(getActivity())
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

*/
        }

    }


    public class sendOrder extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity(),
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Placing Your Order");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            postUpdate();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (updateResponse != null) {
                progressDialog.dismiss();
                try {
                    Log.d("newOrderResponse", updateResponse);
                    //Orders drop and getting new



                    //
                    //updateResponse="{\"order_id\":\"6245\",\"success\":1}";
                    JSONObject jsonObject = new JSONObject(updateResponse);
                    neworderid = jsonObject.getString("order_id");
                    Utilities.setPref("orderid", neworderid, MyApplication.getInstance());


                    Log.d("porderid", neworderid);
                    if (jsonObject.getString("success").equals("1")) {
                        //Fetch data
                        db.deleteitems();
                        db.closeDB();
                        foodPreviewModelArrayList.clear();
                        biddingPreviewModelss.clear();
                        buyNowModelss.clear();
                        mixturePreviewModelss.clear();
                        //
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                        alertDialog.setTitle("Below taxes are applicable!!");
                        alertDialog.setMessage("Taxes as applicable. Gst is only on food and mocktails.");
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Vibrator v = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                                // Vibrate for 500 milliseconds
                                v.vibrate(500);

                                NewCurrentOrdersFragment newCurrentOrderFragment = new NewCurrentOrdersFragment();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.content_frame, newCurrentOrderFragment);
                                fragmentTransaction.addToBackStack(newCurrentOrderFragment.getClass().getName());
                                fragmentTransaction.commit();
                            }
                        });
                        alertDialog.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Order Unsuccessfull", Toast.LENGTH_SHORT).show();


                }
            }
        }

    }

    private static void postUpdate() {
        HttpURLConnection conn = null;
        try {

            URL url = new URL("http://barebonesbar.com/bbapi/add_cartapi.php");
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            JSONObject sendorderarray = pollingJsonVal();
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("order_cart", sendorderarray.toString())
                    .appendQueryParameter("store_id",store_id);

            //.appendQueryParameter("order_id",neworderid);
            Log.d("OPordercartJSON", sendorderarray.toString());

            //Encoded..??
            String query = builder.build().getQuery();
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
            updateResponse = ServerConnection.getData(conn);
            Log.d("OPnewstatus", sendorderarray.toString());
            Log.d("OPupadte from send cart", updateResponse);

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

    public static JSONObject pollingJsonVal() {
        try {
            jsonObj = new JSONObject();
            JSONArray order_cart = new JSONArray();
            Cursor c = db.getAllPlayers();
            while (c.moveToNext()) {
                JSONObject jsonObjData = new JSONObject();
                iid = c.getInt(0);
                newId = c.getString(1);
                iname = c.getString(2);
                iquantity = c.getString(3);
                iprice = c.getString(4);
                isingleprice = c.getString(5);
                jsonObjData.put("customer_id", userid);
                jsonObjData.put("store_id","1");
                jsonObjData.put("item_id", iid);
                jsonObjData.put("_id", newId);
                jsonObjData.put("item_name", iname);
                jsonObjData.put("item_quantity", iquantity);
                jsonObjData.put("price", isingleprice);
                jsonObjData.put("qr_code", qrcode);
                jsonObjData.put("phone", user_Phone);
                jsonObjData.put("customer_name", username);
                jsonObjData.put("current_status", "New");
                jsonObjData.put("date", "21/10/17");
                jsonObjData.put("time", "15:36:29");
                order_cart.put(jsonObjData);
            }
            Log.d("OPcartitemname", iname);
            Log.d("cartitemid", String.valueOf(iid));
            Log.d("cartcusid", userid);
            Log.d("cartnewid", newId);
            Log.d("inewpricee", isingleprice);
            Log.d("cartcusname", username);
            Log.d("cartphone", user_Phone);
            Log.d("orderidsec", "");
            jsonObj.put("order_cart", order_cart);
            Log.d("cart", order_cart.toString());
        } catch (JSONException e) {
            Log.d("Json", "Polling data Exception" + e);
        }
        Log.d("Json", "JSON Object==" + jsonObj);
        return jsonObj;
    }


    protected void exitByBackKey() {
        AlertDialog alertbox = new AlertDialog.Builder(getActivity())
                .setMessage("Do you want to cancel the order?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        //new
                        db.deleteitems();
                        foodPreviewModelArrayList.clear();
                        biddingPreviewModelss.clear();
                        buyNowModelss.clear();
                        mixturePreviewModelss.clear();
                        //

                        DashBoardFragment mixerFragment = new DashBoardFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, mixerFragment);
                        fragmentTransaction.disallowAddToBackStack();
                        fragmentTransaction.commit();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }
                })
                .show();
    }


    @Override
    public void onResume() {
        super.onResume();
        //

        //
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    exitByBackKey();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
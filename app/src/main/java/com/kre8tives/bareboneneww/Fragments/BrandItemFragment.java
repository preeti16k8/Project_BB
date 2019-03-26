package com.kre8tives.bareboneneww.Fragments;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kre8tives.bareboneneww.Adapter.BrandItemAdapter;
import com.kre8tives.bareboneneww.Model.BrandItem;
import com.kre8tives.bareboneneww.Model.BrandItemCheck;
import com.kre8tives.bareboneneww.R;
import com.kre8tives.bareboneneww.Util.ItemOffsetDecoration;
import com.kre8tives.bareboneneww.Util.ServerConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.kre8tives.bareboneneww.Adapter.SelectStoreAdapter.storeId;
public class BrandItemFragment extends Fragment {
    public static String KEY_USER = "email", KEY_CATEGORY_ID = "id",
            BRAND_URL = "http://barebonesbar.com/bbapi/liquor_price_withtrigger.php";
    Integer timeout = 0;
    String responseStr = "";

    List<BrandItem> brandItemList;
    List<BrandItemCheck> brandItemChecksList;
    BrandItemAdapter brandItemAdapter;
    JSONArray brandArray;
    RecyclerView rv_branditem;
    String id;
    String categoryId,storeid,brandId,categoryname;
    ProgressDialog progressDialog;

    ImageView refresh;
    private Parcelable recyclerViewState;


    public BrandItemFragment() {

        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brand_item, container, false);
        brandItemList = new ArrayList<BrandItem>();
        brandItemChecksList=new ArrayList<BrandItemCheck>();
        storeid=storeId;
        categoryId=getArguments().getString("categoryid");
        categoryname=getArguments().getString("categoryname");


        getActivity().setTitle(categoryname);
        rv_branditem=(RecyclerView)view.findViewById(R.id.rv_branditem);
        rv_branditem.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_branditem.setVisibility(View.GONE);
        rv_branditem.setHasFixedSize(true);
        rv_branditem.setLayoutManager((new GridLayoutManager(getActivity(), 2)));

        recyclerViewState = rv_branditem.getLayoutManager().onSaveInstanceState();

// Restore state
        rv_branditem.getLayoutManager().onRestoreInstanceState(recyclerViewState);


        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.spacing);
        rv_branditem.addItemDecoration(itemDecoration);
        refresh=(ImageView)view.findViewById(R.id.refreshbrand);
        refresh.setVisibility(View.GONE);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeout=0;
                refresh.setVisibility(View.GONE);

                new getBrand1().execute();
            }
        });



        new getBrand1().execute();
        return view;

    }


    public class getBrand1 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            brandItemList=new ArrayList<BrandItem>();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

        }
        @Override
        protected String doInBackground(String... params) {
            postClient1();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if (timeout == 1) {
                progressDialog.dismiss();
                refresh.setVisibility(View.VISIBLE);
                rv_branditem.setVisibility(View.GONE);
            } else {
                rv_branditem.setVisibility(View.VISIBLE);
                refresh.setVisibility(View.GONE);

                if(responseStr==null)
                {
                    progressDialog.dismiss();
                    refresh.setVisibility(View.VISIBLE);
                    rv_branditem.setVisibility(View.GONE);
                }
                else {
                    try {
                        Log.d("Responsebranditem", responseStr);
                        brandArray = new JSONArray(responseStr);
                        for (int i = 0; i < brandArray.length(); i++) {
                            BrandItem brandItem = new BrandItem();
                            JSONObject jobject = brandArray.getJSONObject(i);
                            String name = jobject.getString("liquor_name");
                            String min = jobject.getString("min_price");
                            String max = jobject.getString("max_price");
                            String buyNow = jobject.getString("buy_now_price");
                            String stock = jobject.getString("liquor_stock");
                            String maxStock = jobject.getString("max_stock_limit");
//                            String category_ID=jobject.getString("category_id");
                            //String brand_ID=jobject.getString("brand_id");
                            String id=jobject.getString("id");
                            String bidacceptance=jobject.getString("bidacceptance_price");
                            String image = jobject.getString("image");
                           String newid=jobject.getString("_id");
                            Log.d("branditem_newid",newid);
                            brandItem.setIname(name);
                            brandItem.setBrandmaxprice(max);
                            brandItem.setBrandminprice(min);
                            brandItem.setIimageUrl( "http://barebonesbar.com/bbapi/upload/" + image);
                            brandItem.setIcategoryId(categoryId);
                            //brandItem.setIbrandId(brand_ID);
                            brandItem.setIid(id);
                            brandItem.setNewId(newid);
                            brandItemList.add(brandItem);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    brandItemAdapter = new BrandItemAdapter(brandItemList,getActivity());
                    rv_branditem.setAdapter(brandItemAdapter);
                    rv_branditem.setHasFixedSize(true);
                    brandItemAdapter.notifyDataSetChanged();
                    brandItemAdapter.notifyItemRangeChanged(0, brandItemAdapter.getItemCount());
                    progressDialog.dismiss();


                }

            }
        }
    }
    private void postClient1()
    {

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
                    .appendQueryParameter("category_id",categoryId)
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
            Log.d("brand_item_trigger",responseStr);
            Log.d("bicategoryid",categoryId);
            Log.d("bistoreid",storeid);
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


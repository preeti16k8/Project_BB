package com.kre8tives.bareboneneww.Fragments;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kre8tives.bareboneneww.Adapter.CategoryFoodAdapter;
import com.kre8tives.bareboneneww.Database.DataHelper;
import com.kre8tives.bareboneneww.Model.CategoryFood;
import com.kre8tives.bareboneneww.R;
import com.kre8tives.bareboneneww.Util.ItemOffsetDecoration;
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
import java.util.List;

import static com.kre8tives.bareboneneww.Adapter.SelectStoreAdapter.storeId;
import static com.kre8tives.bareboneneww.Url.BASE_URL;

/**
 * Created by Ashet on 05-10-2017.
 */

public class FoodCategoryFragment extends Fragment {

    public static String CATEGORY_FOOD_URL = BASE_URL + "/" + "food_categoryapi.php";
    Integer timeout = 0;
    String responseStrFC = "";
    public static List<CategoryFood> categoryFoodList;
    public static ArrayList<String> al;
    CategoryFoodAdapter categoryFoodAdapter;
    JSONArray categoryFoodArray;
    ProgressDialog progressDialog;
    RecyclerView recyclerView_fc;
    ImageView refresh;
    DataHelper dbHelper;
    String userid, username, id, storeid;
    public static String CateId;


    public FoodCategoryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_category, container, false);
        categoryFoodList = new ArrayList<>();
        al = new ArrayList<>();
        dbHelper = new DataHelper(getActivity());
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Food Category");
        //refresh
        refresh=(ImageView)view.findViewById(R.id.refrescategory);

        refresh.setVisibility(View.GONE);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeout=0;
                refresh.setVisibility(View.GONE);

                new getFoodCategoryask().execute();
            }
        });
        //



        dbHelper = new DataHelper(getActivity());
        Cursor cursor = dbHelper.getuser();
        recyclerView_fc = (RecyclerView) view.findViewById(R.id.categoryRecyclerViewfood);
        recyclerView_fc.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_fc.setHasFixedSize(true);
        recyclerView_fc.setLayoutManager((new GridLayoutManager(getActivity(), 2)));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.spacing);
        recyclerView_fc.addItemDecoration(itemDecoration);
        if (cursor.moveToFirst()) {
            username = cursor.getString(1);
            userid = cursor.getString(0);
        }
        storeid = storeId;
        if(Utilities.checkConnection()){

        new getFoodCategoryask().execute();}
        else{

            throwAlertDialog();

        }
        return view;

    }
     private void throwAlertDialog(){
         AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
         alertBuilder.setCancelable(false);
         alertBuilder.setTitle("Error");
         alertBuilder.setMessage("Check your connection and try again.");
         alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
             @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
             public void onClick(DialogInterface dialog, int which) {
                 DashBoardFragment dashFrag= new DashBoardFragment();
                 getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,dashFrag).disallowAddToBackStack().commit();

             }
         });
         AlertDialog alert = alertBuilder.create();
         alert.show();

     }

    //Async For Food Categories
    public class getFoodCategoryask extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            categoryFoodList = new ArrayList<>();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
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

            if (responseStrFC != null) {

                progressDialog.dismiss();

                longInfo(responseStrFC);

                Log.d("category", responseStrFC);
                try {
                    categoryFoodArray = new JSONArray(responseStrFC);

                    for (int i = 0; i < categoryFoodArray.length(); i++) {
                        CategoryFood categoryFood = new CategoryFood();
                        JSONObject jobject = categoryFoodArray.getJSONObject(i);
                        String Image = jobject.getString("category_image");
                        String Name = jobject.getString("category_name");
                        CateId = jobject.getString("category_id");
                        categoryFood.setFoodCatagoryImage("http://barebonesbar.com/bbapi/upload/" + Image);
                        categoryFood.setFoodCetagoryName(Name);
                        categoryFood.setFoodCategoryId(CateId);
                        categoryFoodList.add(categoryFood);
                        al.add(CateId);
                        Log.d("firstcategid", CateId);
                        Log.d("firstcategname", Name);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                categoryFoodAdapter = new CategoryFoodAdapter(categoryFoodList, getActivity());
                recyclerView_fc.setAdapter(categoryFoodAdapter);
            }else {
                progressDialog.dismiss();
                refresh.setVisibility(View.VISIBLE);
            }
        }
    }



    private void postData() {

        HttpURLConnection conn = null;
        try {

            URL url = new URL(CATEGORY_FOOD_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(2000);
            conn.setConnectTimeout(2000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("store_id", storeid);
            String query = builder.build().getEncodedQuery();
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
            responseStrFC = ServerConnection.getData(conn);
            Log.d("Response food",responseStrFC);
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

    public static void longInfo(String str) {
        if (str.length() > 4000) {
            Log.i("Full", str.substring(0, 4000));
            longInfo(str.substring(4000));
        } else
            Log.i("Full", str);
    }

    /*protected void exitByBackKey() {
        DashBoardFragment dashBoardFragment  = new DashBoardFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, dashBoardFragment);
       fragmentTransaction.disallowAddToBackStack();
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
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
    }*/
}





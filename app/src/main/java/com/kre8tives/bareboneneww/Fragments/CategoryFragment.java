package com.kre8tives.bareboneneww.Fragments;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kre8tives.bareboneneww.Adapter.CategoryAdapter;
import com.kre8tives.bareboneneww.Database.DataHelper;
import com.kre8tives.bareboneneww.Model.Category;

import com.kre8tives.bareboneneww.R;
import com.kre8tives.bareboneneww.Util.ItemOffsetDecoration;
import com.kre8tives.bareboneneww.Util.ServerConnection;
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
public class CategoryFragment extends Fragment {
    public static String KEY_USER = "username", KEY_CATEGORY_ID = "id",
            CATEGORY_URL = "http://barebonesbar.com/bbapi/liquorcategoryfetchapi.php";
    Integer timeout = 0;
    String responseStr = "";
    public static List<Category> categoryList;
    public static ArrayList<String> al;
    CategoryAdapter categoryAdapter;
    JSONArray categoryArray;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    ImageView refresh;

    DataHelper dbHelper;
    String userid, username, id, storeid;
    public static String CateId;
    public CategoryFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        categoryList = new ArrayList<Category>();
        al = new ArrayList<String>();
        dbHelper = new DataHelper(getActivity());

        dbHelper = new DataHelper(getActivity());
        refresh=(ImageView)view.findViewById(R.id.refrescategory);

        refresh.setVisibility(View.GONE);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeout=0;
                refresh.setVisibility(View.GONE);

                new getTask().execute();
            }
        });

        Cursor cursor = dbHelper.getuser();
        recyclerView = (RecyclerView) view.findViewById(R.id.categoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager((new GridLayoutManager(getActivity(), 2)));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.spacing);
        recyclerView.addItemDecoration(itemDecoration);
        if (cursor.moveToFirst()) {
            username = cursor.getString(1);
            userid = cursor.getString(0);
        }
        storeid = storeId;
        new getTask().execute();
        return view;
    }

    public class getTask extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            categoryList = new ArrayList<Category>();
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


            if (responseStr != null) {

                progressDialog.dismiss();


                longInfo(responseStr);

                Log.d("category", responseStr);

                try {
                    categoryArray = new JSONArray(responseStr);

                    for (int i = 0; i < categoryArray.length(); i++) {
                        Category category = new Category();
                        JSONObject jobject = categoryArray.getJSONObject(i);
                        String Image = jobject.getString("category_image");
                        String Name = jobject.getString("category_name");
                        CateId = jobject.getString("category_id");
                        category.setImageUrl("http://barebonesbar.com/bbapi/upload/" + Image);
                        category.setName(Name);
                        category.setId(CateId);
                        categoryList.add(category);
                        al.add(CateId);
                        Log.d("firstcategid", CateId);
                        Log.d("firstcategname", Name);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                categoryAdapter = new CategoryAdapter(categoryList, getActivity());
                recyclerView.setAdapter(categoryAdapter);



            }else {
                progressDialog.dismiss();
                refresh.setVisibility(View.VISIBLE);
            }
//
        }

    }


    private void postData() {

        HttpURLConnection conn = null;
        try {

            URL url = new URL(CATEGORY_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
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
    public static void longInfo(String str) {
        if (str.length() > 4000) {
            Log.i("Full", str.substring(0, 4000));
            longInfo(str.substring(4000));
        } else
            Log.i("Full", str);
    }


    /*protected void exitByBackKey() {
            DashBoardFragment mixerFragment = new DashBoardFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, mixerFragment);
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
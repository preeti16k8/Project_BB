package com.kre8tives.bareboneneww.Fragments;


import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.kre8tives.bareboneneww.Adapter.MixtureAdapter;
import com.kre8tives.bareboneneww.Database.DataHelper;
import com.kre8tives.bareboneneww.Model.Mixture;
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

/**
 * A simple {@link Fragment} subclass.
 */


public class MixerFragment extends Fragment {

    public static String KEY_USER = "email", KEY_CATEGORY_ID = "id", Mixture_URL = "http://barebonesbar.com/bbapi/mixerapi.php";
    Integer timeout = 0;
    String responseStr = "";
    List<Mixture> mixtureList;
    MixtureAdapter mixtureAdapter;
    JSONArray mixtureArray;
    RecyclerView rv_mixture;
    String id;
    String storeid;
    DataHelper dbHelper;
    ImageView refresh;
    ProgressDialog progressDialog;
    Button btn_skip;
    private final Handler handler = new Handler();

    public MixerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mixer, container, false);
        btn_skip = (Button) view.findViewById(R.id.btn_skip);
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderPreviewFragment b = new OrderPreviewFragment();
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, b);
                fragmentTransaction.addToBackStack(b.getClass().getName());
                fragmentTransaction.commit();
            }
        });


        mixtureList = new ArrayList<Mixture>();
//        dbHelper = new DataHelper(getActivity());
//        Cursor cursor = dbHelper.getuser();
//
//        if (cursor.moveToFirst()) {
//            storeid = cursor.getString(3);
//        }

        storeid = storeId;
        rv_mixture = (RecyclerView) view.findViewById(R.id.rv_mixturelist);
        rv_mixture.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_mixture.setHasFixedSize(true);
        rv_mixture.setLayoutManager((new GridLayoutManager(getActivity(), 2)));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.spacing);
        rv_mixture.addItemDecoration(itemDecoration);

        refresh = (ImageView) view.findViewById(R.id.refrescategory);
        refresh.setVisibility(View.GONE);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh.setVisibility(View.GONE);
                new getmixture().execute();
            }
        });


        new getmixture().execute();
        return view;
    }

    public class getmixture extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mixtureList = new ArrayList<Mixture>();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            postmixture();
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (responseStr != null) {
                progressDialog.dismiss();
                try {

                    mixtureArray = new JSONArray(responseStr);
                  Log.d("mixtureresp",responseStr);

                    for (int i = 0; i < mixtureArray.length(); i++) {
                        Mixture mixture = new Mixture();
                        JSONObject jobject = mixtureArray.getJSONObject(i);
                        String name = jobject.getString("mixer_name");
                        String price = jobject.getString("price");
                        String image = jobject.getString("image");
                        String stock = jobject.getString("stock");
                        String id = jobject.getString("id");
                        String newid = jobject.getString("_id");
                        String maxStock = jobject.getString("maximum_stock_limit");
                        mixture.setName(name);
                        mixture.setPrice(price);
                        mixture.setStock(stock);
                        mixture.setMaxStock(maxStock);
                        mixture.setNewid(newid);
                        mixture.setImageUrl("http://barebonesbar.com/bbapi/upload/" + image);
                        mixture.setId(id);

                        Log.d("mixernewid", newid);


                        mixtureList.add(mixture);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                mixtureAdapter = new MixtureAdapter(mixtureList, getActivity());
                rv_mixture.setAdapter(mixtureAdapter);

            } else {
                progressDialog.dismiss();
                refresh.setVisibility(View.VISIBLE);
            }

        }


        private void postmixture() {

            HttpURLConnection conn = null;
            try {

                URL url = new URL(Mixture_URL);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(3000);
                conn.setConnectTimeout(3000);
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

                Log.d("store", storeid);
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
    }
}






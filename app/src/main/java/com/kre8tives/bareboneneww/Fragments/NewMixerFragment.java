package com.kre8tives.bareboneneww.Fragments;
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
import android.widget.ProgressBar;

import com.kre8tives.bareboneneww.Adapter.NewMixtureAdapter;
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
import static com.kre8tives.bareboneneww.Url.BASE_URL;

public class NewMixerFragment extends Fragment {
    public static String KEY_USER = "email", KEY_CATEGORY_ID = "id", Mixture_URL = BASE_URL+"/"+"mixerapi.php";
    Integer timeout = 0;
    String responseStr = "";
    List<Mixture> mixtureList;
    NewMixtureAdapter mixtureAdapter;
    JSONArray mixtureArray;
    RecyclerView rv_mixture;
    String id;
    ProgressBar loader;
    String storeid;
    DataHelper dbHelper;
    ImageView refresh;
    Button btn_skip;
    private final Handler handler = new Handler();
    public NewMixerFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_new_mixer, container, false);
        new getmixture().execute();
        mixtureList = new ArrayList<Mixture>();
        storeid=storeId;
        rv_mixture=(RecyclerView)view.findViewById(R.id.rv_mixturelist);
        rv_mixture.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_mixture.setVisibility(View.GONE);
        rv_mixture.setHasFixedSize(true);
        rv_mixture.setLayoutManager((new GridLayoutManager(getActivity(), 2)));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.spacing);
        rv_mixture.addItemDecoration(itemDecoration);
        refresh=(ImageView)view.findViewById(R.id.refreshmixture);
        refresh.setVisibility(View.GONE);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeout=0;
                refresh.setVisibility(View.GONE);
                loader.setVisibility(View.VISIBLE);
                new getmixture().execute();
            }
        });
        loader=(ProgressBar)view.findViewById(R.id.mixtureLoader);
        loader.setVisibility(View.VISIBLE);
        return view;
    }


    public class getmixture extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mixtureList = new ArrayList<Mixture>();
        }

        @Override
        protected String doInBackground(String... params) {

            postmixture();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loader.setVisibility(View.GONE);
            if (timeout == 1) {
               /* refresh.setVisibility(View.VISIBLE);
                rv_mixture.setVisibility(View.GONE);*/
            } else {
                rv_mixture.setVisibility(View.VISIBLE);
                refresh.setVisibility(View.GONE);

                if (responseStr == null) {
                    refresh.setVisibility(View.VISIBLE);
                    rv_mixture.setVisibility(View.GONE);
                } else {

                    //From Liquor items

                    try {
                        Log.d("mixturerespnew",responseStr);
                        mixtureArray = new JSONArray(responseStr);
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
                            mixture.setImageUrl(BASE_URL + "/" + "upload/" + image);
                            mixture.setId(id);
                            mixture.setNewid(newid);
                            mixtureList.add(mixture);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mixtureAdapter = new NewMixtureAdapter(mixtureList, getActivity());
                    rv_mixture.setAdapter(mixtureAdapter);

                }

            }
        }
    }

    private void postmixture()
    {

        HttpURLConnection conn = null;
        try {

            URL url = new URL(Mixture_URL);


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

            Log.d("store",storeid);
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
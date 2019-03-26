package com.kre8tives.bareboneneww.Fragments;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kre8tives.bareboneneww.Adapter.NewsAdapter;
import com.kre8tives.bareboneneww.Model.News;
import com.kre8tives.bareboneneww.R;
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

/**
 * A simple {@link Fragment} subclass.
 */

public class NewsFragment extends Fragment {

    public static String KEY_USER = "username", KEY_TYPE = "type",
            ALLPROJECTS_URL ="http://barebonesbar.com/bbapi/news_api.php";
    Integer timeout = 0;
    String responseStr = "";
    List<News> newsList;
    NewsAdapter newsAdapter;
    JSONArray newsArray;
    TextView news,title;
    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.news_item, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("News");
        newsList=new ArrayList<News>();
        //getActivity().setTitle("News");
        news = (TextView) view.findViewById(R.id.tv_news);
        title=(TextView)view.findViewById(R.id.tv_title);
        new getNews().execute();
        return view;
    }

    public class getNews extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            newsList=new ArrayList<News>();
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

                longInfo(responseStr);

                try {
                    Log.d("newssssss",responseStr);
                    newsArray = new JSONArray(responseStr);
                    for (int i = 0; i < newsArray.length(); i++) {
                        JSONObject jobject = newsArray.getJSONObject(i);
                        String title1 = jobject.getString("title");
                        String new1 = jobject.getString("news");
                        news.setText(new1);
                       title.setText(title1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void postData()
    {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(ALLPROJECTS_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("store_id",storeId);
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
            Log.i("Full", str.substring(0, 4000));
            longInfo(str.substring(4000));
        } else
            Log.i("Full", str);
    }

    protected void exitByBackKey() {
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
    }
}

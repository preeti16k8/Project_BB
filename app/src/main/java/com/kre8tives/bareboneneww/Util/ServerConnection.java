package com.kre8tives.bareboneneww.Util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Preeti on 12-04-2017.
 */

@SuppressWarnings("deprecation")
public class ServerConnection {

    static JSONObject jObj = null;
    static String json = "";
    static Context mContext;

    public ServerConnection(Context context){
        mContext = context;
    }

    public static String getData(HttpURLConnection connection) {
        String result = null;
        StringBuffer sb = new StringBuffer();
        InputStream is = null;

        try {
            is = new BufferedInputStream(connection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            result = sb.toString();
        }
        catch (Exception e) {
            Log.d("TAG", "Error reading InputStream");
            result = null;
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (IOException e) {
                    Log.d("TAG", "Error closing InputStream");
                }
            }
        }

        return result;
    }


    @SuppressLint("LongLogTag")
    public static JSONArray getJSONArrayFromUrl(String urlink)
    {
        JSONArray array = new JSONArray();
        json="[{}]";

        HttpsURLConnection conn=null;

        try
        {

            URL url = new URL(urlink);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setReadTimeout(200000);
            conn.setConnectTimeout(20000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
            json = ServerConnection.getData(conn);
            array = new JSONArray(json);

        }


        catch (ConnectTimeoutException | SocketTimeoutException e)
        {
            Log.e("Slow Network  ", "" + e);
        }
        catch (UnsupportedEncodingException e) {
            Log.e("Login UnsupportedEncodingException  ", "" + e);
        }catch (IOException e)
        {
            Log.e("Login IOException  ", "" + e);
        }

        catch (Exception e)
        {
            Log.e("login server error ", "Error converting result " + e.toString());
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        // return JSON String

//		Log.d("Array",array.toString());
        return array;

    }

    @SuppressLint("LongLogTag")
    public static JSONArray getJSONArray(String urlink)
    {
        JSONArray array = new JSONArray();
        json="[{}]";
        try {
            array = new JSONArray("[{}]");
        } catch (JSONException e) {
            e.printStackTrace();
        }



        HttpsURLConnection conn=null;

        try
        {

            URL url = new URL(urlink);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setReadTimeout(200000);
            conn.setConnectTimeout(20000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
            json = ServerConnection.getData(conn);

        }

        catch (ConnectTimeoutException | SocketTimeoutException e)
        {
            Log.e("Slow Network  ", "" + e);
        }

        catch (UnsupportedEncodingException e)
        {
            Log.e("Login UnsupportedEncodingException  ", "" + e);
        }


        catch (IOException e)
        {
            Log.e("Login IOException  ", "" + e);
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        // try parse the string to a JSON object
        try
        {

            array = new JSONArray(json);
            // jObj = new JSONObject(json);
            Log.e("login server jObj ", "" + jObj);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        // return JSON String

//		Log.d("Array",array.toString());
        return array;

    }


    public static Boolean checkOnlineState(Context context) {
        Boolean yes =false;
        int timeout = 10000;

        ConnectivityManager CManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo NInfo = CManager.getActiveNetworkInfo();
        if (NInfo != null && NInfo.isConnectedOrConnecting()) {
            try {
                if (InetAddress.getByName("towness.co.in").isReachable(timeout))
                {
                    yes = true;
                }
                else
                {
                    yes =  false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return yes;
    }

    // Private class isNetworkAvailable
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}

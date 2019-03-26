package com.kre8tives.bareboneneww.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.kre8tives.bareboneneww.Activity.MainActivity;

import com.kre8tives.bareboneneww.Database.DataHelper;
import com.kre8tives.bareboneneww.Model.SelectStore;
import com.kre8tives.bareboneneww.R;
import com.kre8tives.bareboneneww.Util.ServerConnection;
import com.kre8tives.bareboneneww.Util.Utilities;


import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static com.kre8tives.bareboneneww.Url.BASE_URL;


public class SelectStoreAdapter extends RecyclerView.Adapter <SelectStoreAdapter.StoreViewholder> {
    private Context mContext;
    public static String storeId;
    DataHelper dbHelper;
    Integer timeout = 0;
    String success="";
    String message="";
    public static String store_id,imageurl;
    ImageView storeview;
    public static String customerid;
    public String responseStr = "",userresponse="";
    private List<SelectStore> selectStoreList;
    String phone;
    public SelectStoreAdapter(List<SelectStore> selectStoreList, Context context) {
        this.mContext = context;
        this.selectStoreList = selectStoreList;
    }
    @Override
    public StoreViewholder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.selectstore_item, null);
        StoreViewholder storeViewholder = new StoreViewholder(view);
        return storeViewholder;
    }
    @Override
    public void onBindViewHolder(StoreViewholder holder, int position) {
        dbHelper = new DataHelper(mContext);
        Cursor cursor = dbHelper.getuser();
        if (cursor.moveToFirst()) {
            phone=cursor.getString(6);
        }
        final SelectStore selectStore = selectStoreList.get(position);
        storeId = selectStore.getId();
        customerid=selectStore.getCustomerId();
        holder.enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utilities.checkConnection()){
             new sendStore(selectStore.getId()).execute();}
                else{
                    Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return selectStoreList.size();
    }

    //View holder class for view binding
    public static class StoreViewholder extends RecyclerView.ViewHolder {
        Button enterButton;
        View view;
        public StoreViewholder(View itemView) {
            super(itemView);
            view = itemView;
            //Log.d("Dynamic Image",imageurl);
            enterButton=(Button)itemView.findViewById(R.id.enter);


        }
    }





    public class sendStore extends AsyncTask<String, String, String> {
          String store_id;
          public sendStore(String id) {
              this.store_id=id;
          }
          @Override
          protected void onPreExecute() {

          }

          @Override
          protected String doInBackground(String... params) {
              return  postStore();
          }

          @Override
          protected void onPostExecute(String s) {
              super.onPostExecute(s);
              //super.onPreExecute();
              if (timeout == 1) {

              } else {

                  if(s==null)
                  {
                  }
                  else {
                      try {
                          Log.d("ResponseStore", s);
                          JSONObject jsonObject = new JSONObject(s);

                          if(jsonObject.getString("success").equals("1"))
                          {

                              Intent intent=new Intent(mContext,MainActivity.class);
                              intent.putExtra("store_id",storeId);
                              intent.putExtra("customer_id",customerid);
                              intent.putExtra("pphhoonnee",phone);
                              intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                              mContext.startActivity(intent);
                          }
                          else {
                              Toast.makeText(mContext,"Please Try Again",Toast.LENGTH_SHORT).show();
                          }

                      } catch (Exception e) {
                          e.printStackTrace();
                      }

                  }

              }
          }

          private String postStore() {

              HttpURLConnection conn = null;
              try {

                  URL url = new URL(BASE_URL+"/"+"customer_currentstoreapi.php");
                  conn = (HttpURLConnection) url.openConnection();
                  conn.setReadTimeout(5000);
                  conn.setConnectTimeout(50000);
                  conn.setRequestMethod("POST");
                  conn.setDoInput(true);
                  conn.setDoOutput(true);

                  Uri.Builder builder = new Uri.Builder()
                          .appendQueryParameter("store_id", "1")
                          .appendQueryParameter("phone",phone);

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

                  Log.d("currentstoreap",responseStr);




              } catch (Exception e) {
                  e.printStackTrace();
              } finally {
                  {
                      if (conn != null) {
                          conn.disconnect();
                      }
                  }
              }

              return responseStr;
          }
      }
    }




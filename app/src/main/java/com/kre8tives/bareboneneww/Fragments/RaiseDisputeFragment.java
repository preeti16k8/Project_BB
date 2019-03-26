package com.kre8tives.bareboneneww.Fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kre8tives.bareboneneww.R;
import com.kre8tives.bareboneneww.Util.ServerConnection;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.kre8tives.bareboneneww.Activity.MainActivity.userid;
import static com.kre8tives.bareboneneww.Adapter.SelectStoreAdapter.storeId;

/**
 * A simple {@link Fragment} subclass.
 */
public class RaiseDisputeFragment extends Fragment {
EditText feedbackEditText;
Button btn_submit,btn_call;
    String updateResponse="";
    Integer timeout = 0;
    String responseStr = "";
    String enteredtext;
    public RaiseDisputeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Raise Dispute");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View v=inflater.inflate(R.layout.fragment_raise_dispute, container, false);
        feedbackEditText=(EditText)v.findViewById(R.id.feedbackEditText) ;
        btn_submit=(Button)v.findViewById(R.id.btn_submit);
        btn_call=(Button)v.findViewById(R.id.btn_call);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enteredtext=feedbackEditText.getText().toString();
                if(enteredtext.equals("")){
                    Toast.makeText(getActivity(),"Please write your query",Toast.LENGTH_SHORT).show();
                }
                else{
                    new sendDispute().execute();
                }

            }
        });
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCall(v);

            }
        });
        return v;
    }
    public void onCall(View view) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:+919980070833"));
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE},   //request specific permission from user
                    10);
            return;
        }else {
            try{
                startActivity(callIntent);
            }
            catch (android.content.ActivityNotFoundException ex){
                Toast.makeText(getActivity(),"yourActivity is not founded",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Raise Dispute");
    }

    public class sendDispute extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            postUpdate();
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if(updateResponse!=null){

                try {
                    Log.d("DisputeResponse", updateResponse);
                    JSONObject jsonObject = new JSONObject(updateResponse);
                    if(jsonObject.getString("success").equals("1"))
                    {
                        Toast.makeText(getActivity(),"Your Complaint has been successfully submitted",Toast.LENGTH_SHORT).show();
                        feedbackEditText.setText("");


                    }
                    else {
                        Toast.makeText(getActivity(),"Please Try Again",Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else {
                Toast.makeText(getActivity(),"Please tttttttTry Again",Toast.LENGTH_SHORT).show();
            }

        }

    }
    private void postUpdate()
    {
        HttpURLConnection conn = null;
        try {

            URL url = new URL("http://barebonesbar.com/bbapi/feedback.php");
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            Uri.Builder builder = new Uri.Builder()

                    .appendQueryParameter("customer_id", userid)
             .appendQueryParameter("store_id",storeId)
             .appendQueryParameter("comments",enteredtext);

            String query = builder.build().getEncodedQuery();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
            updateResponse = ServerConnection.getData(conn);
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

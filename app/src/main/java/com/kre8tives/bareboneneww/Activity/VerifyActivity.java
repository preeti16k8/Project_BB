package com.kre8tives.bareboneneww.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kre8tives.bareboneneww.Database.DataHelper;
import com.kre8tives.bareboneneww.R;
import com.kre8tives.bareboneneww.Util.HttpService;
import com.kre8tives.bareboneneww.Util.ServerConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;


public class VerifyActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_RECEIVE_SMS = 123;
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    String mobile = "";
    String responseStr = "";
    String message = "Please enter correct otp";
    DataHelper dbHelper;
    String success = "";
    public static String otp = "";
    public static String phoneotp = "";
    public static Activity verifyActivity;
    Boolean Received = false;
    Integer timeout = 0;
    ProgressDialog progressDialog;
    String phNumber;
    Context context = this;
    public static EditText otpText;
    Button btn_verify,btn_resend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        dbHelper = new DataHelper(this);
        otpText = (EditText) findViewById(R.id.input_otp);
        btn_verify = (Button) findViewById(R.id.btn_verify);
        btn_resend=(Button)findViewById(R.id.btn_resend);
        phoneotp = getIntent().getStringExtra("otp");
        Log.d("phone from verify",phoneotp);
        phNumber = getIntent().getStringExtra("phone");
        btn_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoginPost().execute();
            }
        });
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              login();
                //Toast.makeText(VerifyActivity.this, "Please Enter Correct Otp.", Toast.LENGTH_SHORT).show();
            }
        });

        otpText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String otptext = otpText.getText().toString();
                if (!otptext.equals("")) {
                    login();
                    if (otptext.equals(getIntent().getStringExtra("otp")) || otptext.equals(otp)) {

                        Intent main = new Intent(VerifyActivity.this, SetProfile.class);
                        main.putExtra("phone", getIntent().getStringExtra("phone"));
                        startActivity(main);
                        finish();
                        Log.d("Verify_Activity","Finished");
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        final Handler ha = new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (!Received) {
                    verifyOtp();
                }
                ha.postDelayed(this, 10000);
            }
        }, 10000);
    }

    public void verifyOtp() {
        String otp = getIntent().getStringExtra("otp");
        if (!otp.isEmpty()) {
            Intent grapprIntent = new Intent(getApplicationContext(), HttpService.class);
            grapprIntent.putExtra("otp", otp);
            startService(grapprIntent);
        }
    }

    public void login() {
        if (!validatee()) {
            onLoginFailed();
            return;
        }

        mobile = otpText.getText().toString();

        new VerifyPost().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }


    public class LoginPost extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(VerifyActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            validaternw();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if (success.equals("1"))
            {
                onLoginSuccess();

            }
            else {
                Toast.makeText(VerifyActivity.this,message, Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void validaternw()

    {

        HttpURLConnection conn = null;
        try {
           // int responseCode = -1;

          //  URL url = new URL("http://barebonesbar.com/bbapi/send_otp.php");


            // URL url = new URL("http://www.sitesandflats.com/send_otp.php");
            URL url = new URL("http://www.barebonesbar.com/bbapi/send_otp.php");
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

/*
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // do your stuff herelater
                Toast.makeText(VerifyActivity.this,"Server is Down .Please Try again later",Toast.LENGTH_LONG).show();
            }
            else {
                // Show some error dialog or Toast message
            }*/

            Uri.Builder builder = new Uri.Builder()
                    //  .appendQueryParameter("type", "adduser")
                    //.appendQueryParameter("otp",mobileText.getText().toString())
                    .appendQueryParameter("phone", phNumber);

            Log.d("phone",phNumber);

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
            Log.d("Validation",responseStr);

            if(responseStr!=null)
            {
                JSONObject jsonObject = new JSONObject(responseStr);
//                message = jsonObject.getString("message");
                success = jsonObject.getString("success");
                otp = jsonObject.getString("otp");
            }

        }
        catch (SocketTimeoutException | UnknownHostException e)
        {
            timeout = 1;
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
    }


    public void onLoginSuccess()

    {

        Intent login = new Intent(VerifyActivity.this, VerifyActivity.class);
        login.putExtra("otp",phoneotp);
        login.putExtra("phone",phNumber);
        startActivity(login);
        finish();

    }


    public class VerifyPost extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(VerifyActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Processing...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            validater();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if (success.equals("1")) {
                onLoginSuccesss();
            } else {
                Toast.makeText(VerifyActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void validater()

    {

        HttpURLConnection conn = null;
        try {

         //   URL url = new URL("http://barebonesbar.com/bbapi/otp_verify.php");


            //URL url = new URL("http://www.sitesandflats.com/otp_verify.php");
            URL url = new URL("http://www.barebonesbar.com/bbapi/otp_verify.php");
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("otp", otpText.getText().toString())
                    .appendQueryParameter("phone", getIntent().getStringExtra("phone"));
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
            if (responseStr != null) {
                Log.d("response", responseStr);
                JSONObject jsonObject = new JSONObject(responseStr);
                success = jsonObject.getString("sucess");
                otp = jsonObject.getString("otp");
            }
        } catch (SocketTimeoutException | UnknownHostException e) {
            timeout = 1;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
       /* Log.d("otp", otp);
        otpText.setText(otp);
        Log.d("phone", getIntent().getStringExtra("phone"));*/
    }

    public void onLoginSuccesss() {
        Intent login = new Intent(VerifyActivity.this, SetProfile.class);
        login.putExtra("phone", getIntent().getStringExtra("phone"));

        startActivity(login);
        finish();
    }

    public void onLoginFailed() {
        otpText.setEnabled(true);
    }

    public boolean validatee() {
        boolean valid = true;

        String email = otpText.getText().toString();

        if (email.isEmpty() || !Patterns.PHONE.matcher(email).matches()) {
            otpText.setError("enter a valid phone number");
            valid = false;
        } else {
            otpText.setError(null);
        }
        return valid;
    }

    public static class SmsReceiver extends BroadcastReceiver {
        private final String TAG = SmsReceiver.class.getSimpleName();
        final SmsManager sms = SmsManager.getDefault();

        public void onReceive(Context context, Intent intent) {
            final Bundle bundle = intent.getExtras();
            SmsMessage currentSMS;
            String message;
            try {
                if (bundle != null) {
                    final Object[] pdusObj = (Object[]) bundle.get("pdus");
                    if (pdusObj != null) {
                        for (Object aObject : pdusObj) {
                            currentSMS = getIncomingMessage(aObject, bundle);
                            String senderNo = currentSMS.getDisplayOriginatingAddress();
                            message = currentSMS.getDisplayMessageBody();
                            String verificationCode = getVerificationCode(message);
                            otpText.setText(verificationCode);
                        }
                        this.abortBroadcast();
                    }
                }

            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver" + e);
            }
        }

        private SmsMessage getIncomingMessage(Object aObject, Bundle bundle) {
            SmsMessage currentSMS;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String format = bundle.getString("format");
                currentSMS = SmsMessage.createFromPdu((byte[]) aObject, format);
            } else {
                currentSMS = SmsMessage.createFromPdu((byte[]) aObject);
            }
            return currentSMS;
        }

        public String getVerificationCode(String message) {
            //String st="Your mobile verification code 123456";
            String st = message;
            String st1 = st.replaceAll("\\D+", "");
            Log.d("otpmessageextract", st1);
            Log.d("otpmessage", st);
            return st1;
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
       /* Intent intent=new Intent(VerifyActivity.this,LoginActivity.class);
        startActivity(intent);
        super.onBackPressed();*/
    }
}
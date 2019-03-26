package com.kre8tives.bareboneneww.Activity;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.kre8tives.bareboneneww.Database.DataHelper;
import com.kre8tives.bareboneneww.R;
import com.kre8tives.bareboneneww.Util.ServerConnection;
import com.kre8tives.bareboneneww.Util.Utilities;

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
import static com.kre8tives.bareboneneww.Util.ServerConnection.isNetworkAvailable;

public class LoginActivity extends AppCompatActivity
{
    private static final int PERMISSIONS_REQUEST_RECEIVE_SMS = 123;
    private static final int REQUEST_SETTINGS = 12345;
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    String phone = "";
    String responseStr = "";
    String message="";
    DataHelper dbHelper;
    String success="";
    String otp="";
    Integer timeout = 0;
    ProgressDialog progressDialog;
    Context context = this;
    EditText mobileText;
    Button loginButton;
    CheckBox checkbox_terms;
    TextView tv_termsConditions;



    @Override
    public void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DataHelper(this);
        mobileText = (EditText) findViewById(R.id.input_phone);
        loginButton = (Button) findViewById(R.id.btn_login);
        checkbox_terms=(CheckBox)findViewById(R.id.checkbox_terms);

        LayoutInflater li = getLayoutInflater();

        final View layout = li.inflate(R.layout.customtoast,
                (ViewGroup) findViewById(R.id.custom_toast_layout));

        tv_termsConditions=(TextView)findViewById(R.id.tv_termsConditions);
        tv_termsConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //By using our App you agree to be legally bound by these terms, which shall take effect immediately on your first use of our App.
                Toast.makeText(getApplicationContext(),"By using our App you agree to be legally bound by these terms, which shall take effect immediately on your first use of our App.",Toast.LENGTH_LONG).show();
             //   CustomDialogFragment customDialogFragment=new CustomDialogFragment();
             //   customDialogFragment.show(getSupportFragmentManager(),null);
            }
        });

        if (dbHelper.getUserCount() != 0) {
            Intent login = new Intent(LoginActivity.this, StoreActivity.class);
            login.putExtra("phone",phone);
            startActivity(login);
            finish();
            return;
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setEnabled(false);
                if(mobileText.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"please enter correct mobile number",Toast.LENGTH_SHORT).show();
                    loginButton.setEnabled(true);
                }
                else {
                    if (checkbox_terms.isChecked())
                    {
                       // checkPermission();
                        showAlert("", "Please Visit to Barebone Restuarant and connect with wifi to access this app" +new String(Character.toChars(0x1F61C)), "bill_printed");
                        loginButton.setEnabled(true);

                    } else {
                        loginButton.setEnabled(true);
                        //Please agree to Our terms &amp; Conditions!
                        Toast.makeText(getApplicationContext(),"Please agree to Our terms and Conditions!",Toast.LENGTH_SHORT).show();
                       /* Toast toast = new Toast(getApplicationContext());
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.setView(layout);//setting the view of custom toast layout
                        toast.show();
*/
                    }
                }


            }
        });

    }


    private boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }
        return false;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkPermission()
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.RECEIVE_SMS)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission is necessary");
                    alertBuilder.setMessage("Sms Receive permission is necessary to read one time password!!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.RECEIVE_SMS}, PERMISSIONS_REQUEST_RECEIVE_SMS);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.RECEIVE_SMS}, PERMISSIONS_REQUEST_RECEIVE_SMS);
                }
                return false;
            } else {
                loginnw();
                return true;
            }
        } else {
            loginnw();
            return true;
        }
    }

    public void runTask()
    {
        if(isNetworkAvailable(LoginActivity.this))

        {

            showAlert("", "Please Visit to Barebone Restuarant and connect with wifi to access this app" +new String(Character.toChars(0x1F61C)), "bill_printed");
            loginButton.setEnabled(true);
          //  new LoginPost().execute();
        }
        else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("No Internet");
            builder.setMessage("Internet is required. Please Retry.");

            builder.setNegativeButton("Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    startActivityForResult(intent,REQUEST_SETTINGS);
                }
            });

            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                    runTask();
                }
            });
            AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
            dialog.show();

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_RECEIVE_SMS:
                if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    checkPermission();
                }
                else {
                    loginnw();

                }
                break;
        }
    }


    public void loginnw()
    {
        Log.d(TAG, "Login");
        if (!validate()) {
            // onLoginFailedd();
            return;
        }else{
            runTask();
        }

        phone = mobileText.getText().toString();
        // new LoginPost().execute();
        // TODO: Implement your own authentication logic here.
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        // Disable going back to the MainActivity
        moveTaskToBack(true);

    }


    public class LoginPost extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this,
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
                showAlert("", "Please connect to wifi to access barebone app " +new String(Character.toChars(0x1F61C)), "bill_printed");
                // Toast.makeText(LoginActivity.this,"Please connect to wifi to access barebone app!", Toast.LENGTH_SHORT).show();
            }

        }

    }


    private void showAlert(String title,String msg,String type) {
        switch(type){
            case "bill_printed":
                new AwesomeErrorDialog(this)
                        .setTitle(msg)
                        .setColoredCircle(R.color.colorAccent)
                        .setDialogIconOnly(R.drawable.bare33)
                        .setDialogBodyBackgroundColor(R.color.white)
                        .setCancelable(true).setButtonText(getString(R.string.dialog_ok_button))
                        .setButtonBackgroundColor(R.color.colorAccent)
                        .setButtonText(getString(R.string.dialog_ok_button))
                        .setErrorButtonClick(new Closure() {
                            @Override
                            public void exec() {
                                // click

                            }
                        })
                        .show();
                break;

          /*  case "success":
                new AwesomeSuccessDialog(getActivity())
                        .setTitle(msg)
                        .setColoredCircle(R.color.colorAccent)

                        .setDialogBodyBackgroundColor(R.color.white)
                        .setDialogIconOnly(R.drawable.bare22)
                        .setCancelable(true)
                        .setPositiveButtonText("Place your order now")
                        .setPositiveButtonbackgroundColor(R.color.colorAccent)
                        .setPositiveButtonTextColor(R.color.white)

                        .setPositiveButtonClick(new Closure() {
                            @Override
                            public void exec() {
                                //click
                            }
                        })
                        .show();
                break;

*/
        }

    }


    private void validaternw()

    {

        HttpURLConnection conn = null;
        try {

            //int responseCode = -1;

            //URL url = new URL("http://www.barebonesbar.com/bbapi/send_otp.php");


            // URL url = new URL("http://www.sitesandflats.com/send_otp.php");
            URL url = new URL("http://www.barebonesbar.com/bbapi/send_otp.php");
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(200000);
            conn.setConnectTimeout(20000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);



            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("phone", phone);

            Log.d("phone",phone);

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
                Utilities.setPref("phone",phone,this);
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

        Intent login = new Intent(LoginActivity.this, VerifyActivity.class);
        login.putExtra("otp",otp);
        login.putExtra("phone",mobileText.getText().toString());
        Utilities.setPref("phone",mobileText.getText().toString(),this);
        startActivity(login);
        finish();

    }

    public void onLoginFailedd()

    {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    public boolean validate()

    {
        boolean valid = true;

        String email = mobileText.getText().toString();

        if (email.isEmpty() || !Patterns.PHONE.matcher(email).matches())

        {
            mobileText.setError("enter a valid phone number");
            valid = false;
        }
        else {
            mobileText.setError(null);
        }
        return valid;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}


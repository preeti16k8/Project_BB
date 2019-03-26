package com.kre8tives.bareboneneww.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kre8tives.bareboneneww.Adapter.CropingOptionAdapter;
import com.kre8tives.bareboneneww.Database.DataHelper;
import com.kre8tives.bareboneneww.Model.CropingOption;
import com.kre8tives.bareboneneww.R;
import com.kre8tives.bareboneneww.Util.AgeCalculation;
import com.kre8tives.bareboneneww.Util.PictUtil;
import com.kre8tives.bareboneneww.Util.ServerConnection;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import static com.kre8tives.bareboneneww.Url.BASE_URL;

import static com.kre8tives.bareboneneww.Util.PictUtil.getCacheFilename;
import static com.kre8tives.bareboneneww.Util.PictUtil.saveToCacheFile;
import static com.kre8tives.bareboneneww.Util.ServerConnection.isNetworkAvailable;


public class UpdateProfileActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private FloatingActionButton buttonSelect;
    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.
    private Button buttonContinue;

    private ImageView imageView;
    // and returned in the Activity's onRequestPermissionsResult()
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    List<String> listPermissionsNeeded;
    String responseStr = "";
    String userid;

    private EditText editTextName,editTextDOB,editTextEmail;
    private final static int REQUEST_PERMISSION_REQ_CODE = 34;
    private static final int CAMERA_CODE = 101, GALLERY_CODE = 201, CROPING_CODE = 301;
    private Uri mImageCaptureUri;
    private File outPutFile = null;
    Context context = this;
    private Bitmap bitmap;
    String GETUSER_URL = BASE_URL+"/"+"customer_dataapi.php";
    private Uri filePath;
    RequestQueue requestQueue;
    String message = "";
    DataHelper dbHelper;
    ProgressBar loading;
    ProgressDialog upload;
    private SimpleDateFormat dateFormat;
    DatePickerDialog datePickerDialog;
    static final int DATE_START_DIALOG_ID = 0;
    private int startYear=1970;
    private int startMonth=6;
    private int startDay=15;
    private AgeCalculation age = null;
    private TextView currentDate;
    private TextView birthDate;
    int calculated_age;
    String profilePic;
    String success="";
    private int PICK_IMAGE_REQUEST = 1;
    //Permision code that will be checked in the method onRequestPermissionsResult
    private int STORAGE_PERMISSION_CODE = 23;
    private int IMAGE_PERMISSION_CODE=21;

    private String UPLOAD_URL = BASE_URL+"/"+"customer_registerapi.php",  KEY_COUNTRYCODE = "country_code";

    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";
    private String KEY_PHONE = "phone";
    String name = "", dob="",email="";
    Bitmap photo;
    String mainPhone;
    public static int counterrenameupdate;
    public static String path="";
    String updateResponse="";
    Integer timeout = 0;
    boolean b=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        age=new AgeCalculation();
        //get the received intent
        Intent receivedIntent = getIntent();
        //get the action
        String receivedAction = receivedIntent.getAction();
        //find out what we are dealing with
        String receivedType = receivedIntent.getType();
        dateFormat=new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Calendar calendar=Calendar.getInstance();

          /*  public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
               // editTextDOB.setText(dateFormat.format(newDate.getTime()));
                calculateAge();
            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));*/

        currentDate=(TextView) findViewById(R.id.tv_currentDate);
        currentDate.setText( age.getCurrentDate());
        currentDate.setVisibility(View.INVISIBLE);
        birthDate=(TextView) findViewById(R.id.tv_birthDate);
        editTextName = (EditText) findViewById(R.id.editText_name);
        editTextDOB=(EditText)findViewById(R.id.editText_age);
        editTextDOB.setOnFocusChangeListener(this);
        editTextEmail=(EditText)findViewById(R.id.editText_email);

        // calculated_age= Integer.parseInt(age.getResult());
        listPermissionsNeeded = new ArrayList<>();
        dbHelper = new DataHelper(this);
        Cursor cursor=dbHelper.getuser();
        if (cursor.moveToFirst()) {

            mainPhone= cursor.getString(6);
        }


        //make sure it's an action and type we can handle
        try {
            if (receivedAction.equals(Intent.ACTION_ATTACH_DATA)) {
                if (receivedType.startsWith("image/")) {
                    handleSendImage(receivedIntent); // Handle single image being sent
                }
                //content is being shared
            }
        } catch (Exception ignored) {

        }


        buttonSelect = (FloatingActionButton) findViewById(R.id.buttonSelect);
        buttonContinue = (Button) findViewById(R.id.buttonContinue);

        imageView = (ImageView) findViewById(R.id.imageView);
        outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
        buttonSelect.setOnClickListener(this);
        buttonContinue.setOnClickListener(this);

        imageView.setVisibility(View.GONE);
        buttonSelect.setVisibility(View.GONE);
        buttonContinue.setVisibility(View.GONE);
        editTextName.setVisibility(View.GONE);
        editTextDOB.setVisibility(View.GONE);
        editTextEmail.setVisibility(View.GONE);
        editTextDOB.setVisibility(View.GONE);
        editTextEmail.setVisibility(View.GONE);

        loading = (ProgressBar)findViewById(R.id.progressloading);
        if(isNetworkAvailable(UpdateProfileActivity.this))

        {
            deleteCache(context);

        }

        new getSuccess().execute();
//        calculateAge();
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    void handleSendImage(Intent intent) {
        mImageCaptureUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (mImageCaptureUri != null) {
            // Update UI to reflect image being shared
            CropingIMG();

        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_START_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        startYear, startMonth, startDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            startYear=selectedYear;
            startMonth=selectedMonth;
            startDay=selectedDay;
            age.setDateOfBirth(startYear, startMonth, startDay);
            Calendar newDate = Calendar.getInstance();
            newDate.set(selectedYear, selectedMonth, selectedDay);
            editTextDOB.setText(dateFormat.format(newDate.getTime()));
//            editTextDOB.setText(""+selectedDay+":"+(startMonth+1)+":"+startYear);
        }
    };
    private void calculateAge()
    {
        age.calcualteYear();
        age.calcualteMonth();
        age.calcualteDay();
        //Toast.makeText(getBaseContext(), "click the resulted button"+age.getResult() , Toast.LENGTH_SHORT).show();
        //editTextDOB.setText(age.getResult());
    }

    private static boolean isValidEmail(String email) {
        return TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    /*
    * This is the method responsible for image upload
    * We need the full image path and the name for the image in this method
    * */
    public void uploadMultipart() {

        //If the app has not the permission then asking for the permission
        name = editTextName.getText().toString();
        email=editTextEmail.getText().toString();
        dob=editTextDOB.getText().toString();

        upload= ProgressDialog.show(this, "Processing", "Please wait...", false, false);
        upload.show();

        //Converting Bitmap to String
        for(int i=0;i<=1;i++){
            String rename=String.valueOf(counterrenameupdate++);
            saveToCacheFile(photo, "+91" + mainPhone +rename);
            //getting the actual path of the image
            //   final String path = getCacheFilename("+91" + userPhone);
            path = getCacheFilename("+91" + mainPhone +rename);

        }

      /*  saveToCacheFile(photo, "+91" + mainPhone);
        //getting the actual path of the image
        final String path = getCacheFilename("+91" + mainPhone);*/


//Uploading code
        try {
            final String uploadId = UUID.randomUUID().toString();

//Creating a multi part request
            new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                    .addFileToUpload(path,"image") //Adding file
                    .addParameter("customer_name", name)
                    .addParameter("phone", mainPhone)
                    .addParameter("email",email)
                    .addParameter("dob", dob)
                    //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(UploadInfo uploadInfo) {
                            Log.d("Uploading", uploadInfo.toString());
                        }

                        @Override
                        public void onError(UploadInfo uploadInfo, Exception exception) {
                            try {

                                Toast.makeText(UpdateProfileActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                            } catch (Exception ignored) {

                            }

                        }

                        @Override
                        public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
                            Log.i("UploadCompleted", String.format(Locale.getDefault(),
                                    "ID %1$s: completed in %2$ds at %3$.2f Kbit/s. Response code: %4$d, body:[%5$s]",
                                    uploadInfo.getUploadId(), uploadInfo.getElapsedTime() / 1000,
                                    uploadInfo.getUploadRate(), serverResponse.getHttpCode(),
                                    serverResponse.getBodyAsString()));
//                                Toast.makeText(SetProfile.this, serverResponse.getBodyAsString(),Toast.LENGTH_LONG).show();

                            File file = new File(getCacheFilename("+91" + mainPhone));
                            boolean delete = file.delete();
                            boolean deletepath = PictUtil.getSavePath().delete();
                            boolean deleteoutput = outPutFile.delete();
                            Intent main = new Intent(UpdateProfileActivity.this, MainActivity.class);

//               dbHelper.adduser(SetProfile.this, userid, name, userPhone, userPhone+"cache.png",dob,email);

                            main.putExtra("phone", getIntent().getStringExtra("phone"));
                            main.putExtra("customer_id",userid);
                            main.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                            upload.dismiss();
                            startActivity(main);
                            finish();
                            try {
                                JSONObject response = new JSONObject(serverResponse.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(UploadInfo uploadInfo) {

                        }
                    })
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    private void getuser(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GETUSER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        Log.d("Getupdateuserresponse",s);
                        try {
                            JSONArray jsonArray=new JSONArray(s);
                            for(int i=0;i<jsonArray.length();i++){

                                JSONObject resJson = jsonArray.getJSONObject(i);
                                userid=resJson.getString("customer_id");
                                String name = resJson.getString("customer_name");
                                String phone = resJson.getString("phone");
                                String age=resJson.getString("dob");
                                String mail=resJson.getString("email");

                                String imageurl = resJson.getString("image");
                                imageurl = imageurl.replaceAll(" ", "%20");

                                imageurl = BASE_URL+"/"+"upload/"+imageurl;


                                //Toast.makeText(MainActivity.this, imageurl, Toast.LENGTH_SHORT).show();

                                editTextName.setText(name);
                                editTextDOB.setText(age);
                                editTextEmail.setText(mail);
                                Picasso.with(UpdateProfileActivity.this)
                                        .load(imageurl)
                                        .placeholder(R.drawable.user)
                                        .into(new Target() {
                                            @Override
                                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                loading.setVisibility(View.GONE);


                                                imageView.setVisibility(View.VISIBLE);
                                                buttonSelect.setVisibility(View.VISIBLE);
                                                buttonContinue.setVisibility(View.VISIBLE);
                                                editTextName.setVisibility(View.VISIBLE);
                                                editTextDOB.setVisibility(View.VISIBLE);
                                                editTextEmail.setVisibility(View.VISIBLE);
                                                editTextDOB.setEnabled(true);

                                                imageView.setImageBitmap(bitmap);
                                                Log.d("Imageloading","loaded");
                                            }

                                            @Override
                                            public void onBitmapFailed(Drawable errorDrawable) {
                                                loading.setVisibility(View.GONE);
                                                imageView.setVisibility(View.VISIBLE);
                                                buttonSelect.setVisibility(View.VISIBLE);
                                                buttonContinue.setVisibility(View.VISIBLE);
                                                editTextName.setVisibility(View.VISIBLE);
                                                editTextDOB.setVisibility(View.VISIBLE);
                                                editTextEmail.setVisibility(View.VISIBLE);
                                                editTextDOB.setEnabled(false);

                                                Log.d("Imageloading","failed");
                                            }

                                            @Override
                                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                                                Log.d("Imageloading","prepared");
                                            }
                                        });
                            }


                        } catch (JSONException e) {

                            e.printStackTrace();
                        }

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        getuser();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                params.put(KEY_PHONE, mainPhone);
                params.put(KEY_COUNTRYCODE, "+91");

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        if (requestQueue == null) {requestQueue =  Volley.newRequestQueue(this);  }

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            int result;
            listPermissionsNeeded = new ArrayList<>();

            for (String p : PERMISSIONS) {

                result = ContextCompat.checkSelfPermission(context, p);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(p);
                }
            }

            if (!listPermissionsNeeded.isEmpty()) {

                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, listPermissionsNeeded.get(0))) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Permissions Required to Access Your Storage");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(UpdateProfileActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions(UpdateProfileActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
                }
                return false;
            } else {

                showFileChooser();

                return true;
            }

        } else {
            showFileChooser();

            return true;
        }
    }



    //We are calling this method to check the permission status
    private boolean isReadStorageAllowed() {
//Getting the permission status
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        }

//If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

//If permission is not granted returning false
        return false;
    }
    //Requesting permission
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
//If the user has denied the permission previously your code will come to this block
//Here you can explain why you need this permission
//Explain here why you need this permission
        }

//And finally ask for the permission
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }
    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

//Checking the request code of our request
        int result;
        listPermissionsNeeded = new ArrayList<>();

        for (String p : PERMISSIONS) {

            result = ContextCompat.checkSelfPermission(context, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }

        if (listPermissionsNeeded.size() == 0) {
            showFileChooser();
        }
        if (requestCode == STORAGE_PERMISSION_CODE) {

//If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

//Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
//Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == IMAGE_PERMISSION_CODE) {

//If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

//Displaying a toast
                Toast.makeText(this, "Permission granted now you can take photo", Toast.LENGTH_LONG).show();
            } else {
//Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you denied the permission", Toast.LENGTH_LONG).show();
            }

        }
    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }



    private void showFileChooser() {

        final CharSequence[] items = {"Choose from Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Capture Photo")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp1.jpg");
                    mImageCaptureUri = Uri.fromFile(f);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                    startActivityForResult(intent, CAMERA_CODE);

                } else if (items[item].equals("Choose from Gallery")) {



                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, GALLERY_CODE);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && null != data) {

            mImageCaptureUri = data.getData();

            CropingIMG();

        } else if (requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK) {

            System.out.println("Camera Image URI : "+mImageCaptureUri);
            CropingIMG();
        } else if (requestCode == CROPING_CODE) {

            try {
                if(outPutFile.exists()){

                    photo = decodeFile(outPutFile);
                    imageView.setImageBitmap(photo);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error while save image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void CropingIMG() {

        final ArrayList<CropingOption> cropOptions = new ArrayList<CropingOption>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities( intent, 0 );
        int size = list.size();
        if (size == 0) {
            Toast.makeText(this, "Cann't find image croping app", Toast.LENGTH_SHORT).show();
            return;
        } else {
            intent.setData(mImageCaptureUri);
            intent.putExtra("outputX", 512);
            intent.putExtra("outputY", 512);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);

            //TODO: don't use return-data tag because it's not return large image data and crash not given any message
            //intent.putExtra("return-data", true);

            //Create output file here
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPutFile));

            if (size == 1) {
                Intent i   = new Intent(intent);
                ResolveInfo res = (ResolveInfo) list.get(0);

                i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                startActivityForResult(i, CROPING_CODE);
            } else {
                for (ResolveInfo res : list) {
                    final CropingOption co = new CropingOption();

                    co.title  = getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon  = getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent= new Intent(intent);
                    co.appIntent.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                    cropOptions.add(co);
                }

                CropingOptionAdapter adapter = new CropingOptionAdapter(getApplicationContext(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose Croping App");
                builder.setCancelable(false);
                builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item ) {
                        startActivityForResult( cropOptions.get(item).appIntent, CROPING_CODE);
                    }
                });

                builder.setOnCancelListener( new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel( DialogInterface dialog ) {

                        if (mImageCaptureUri != null ) {
                            getContentResolver().delete(mImageCaptureUri, null, null );
                            mImageCaptureUri = null;
                        }
                    }
                } );

                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    private Bitmap decodeFile(File f) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 512;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (Exception e) {

            Toast.makeText(UpdateProfileActivity.this,"Error"+e, Toast.LENGTH_LONG).show();
        }
        return null;
    }

    @Override
    public void onClick(View v) {

        if(v == buttonSelect){
            b=true;
            //checkPermission();
            showFileChooser();


        }

        if(v == buttonContinue&&b==true){
            if(editTextName.getText().length()!=0&&editTextEmail.getText().length()!=0&&editTextDOB.getText().length()!=0){

                calculateAge();

                if(Integer.parseInt(age.getResult())>21) {

                    Log.d("calcAge", age.getResult());


                    /*listPermissionsNeeded = new ArrayList<>();
                    int result;
                    for (String p : PERMISSIONS) {

                        result = ContextCompat.checkSelfPermission(context, p);
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            listPermissionsNeeded.add(p);
                        }

                    }
                    if (listPermissionsNeeded.size() == 0) {
                        uploadMultipart();

                    }*/


                    uploadMultipart();


                } else {
                    Toast.makeText(this,"Oops you are not eligible,your age must be greater than 21 " , Toast.LENGTH_LONG).show();
                }

            }else{
                Toast.makeText(this,"All the fields are required!",Toast.LENGTH_SHORT).show();
            }
        }



        else if(v == buttonContinue&&b==false)
        {
            if(editTextName.getText().length()!=0&&editTextEmail.getText().length()!=0&&editTextDOB.getText().length()!=0){

                calculateAge();

                if(Integer.parseInt(age.getResult())>21) {

                    Log.d("calcAge", age.getResult());


                    uploadMultipart();



                } else {
                    Toast.makeText(this,"Oops you are not eligible,your age must be greater than 21 " , Toast.LENGTH_LONG).show();
                }

            }else{
                Toast.makeText(this,"All the fields are required!",Toast.LENGTH_SHORT).show();
            }
        }





    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if(hasFocus){
            if(v==editTextDOB){
                // datePickerDialog.show();
                showDialog(DATE_START_DIALOG_ID);
                editTextDOB.clearFocus();
            }
        }
    }


    public class noImage extends AsyncTask<String, String, String> {


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


            if (updateResponse != null) {

                try {
                    Log.d("UpdateDisputeResponse", updateResponse);
                    JSONObject jsonObject = new JSONObject(updateResponse);
                    if (jsonObject.getString("success").equals("1")) {
                        // Toast.makeText(getApplicationContext(), "Your Complaint has been successfully submitted", Toast.LENGTH_SHORT).show();
                        //   qrcode=null;
                        Intent in=new Intent(UpdateProfileActivity.this,MainActivity.class);
                        in.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(in);


                    } else {
                        //Toast.makeText(getApplicationContext(), "Please Try Again", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Please tttttttTry Again", Toast.LENGTH_SHORT).show();
            }

        }


        private void postUpdate() {

            HttpURLConnection conn = null;
            try {

                URL url = new URL(UPLOAD_URL);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);


                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("customer_name", name)
                        .appendQueryParameter("phone", mainPhone)
                        .appendQueryParameter("email", email)
                        .appendQueryParameter("dob", dob);

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



    public class getSuccess extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
//            loading = new ProgressDialog(SetProfile.this,
//                    R.style.AppTheme_Dark_Dialog);
//            progressDialog.setIndeterminate(true);
//            progressDialog.setMessage("Authenticating...");
//            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            validater();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            progressDialog.dismiss();
            if (success.equals("1"))
            {
                getuser();

            }
            else {
                loading.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                buttonSelect.setVisibility(View.VISIBLE);
                buttonContinue.setVisibility(View.VISIBLE);
                editTextName.setVisibility(View.VISIBLE);
                editTextDOB.setVisibility(View.VISIBLE);
                editTextEmail.setVisibility(View.VISIBLE);

            }

            Log.d("success",success);
        }
    }

    private void validater()

    {

        HttpsURLConnection conn = null;
        try {
           // int responseCode = -1;
            URL url = new URL(BASE_URL+"/"+"customer_checkapi.php");
            conn = (HttpsURLConnection) url.openConnection();
            conn.setReadTimeout(200000);
            conn.setConnectTimeout(20000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

/*

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // do your stuff herelater
                Toast.makeText(UpdateProfileActivity.this,"Server is Down .Please Try again later",Toast.LENGTH_LONG).show();
            }
            else {
                // Show some error dialog or Toast message
            }
*/

            Uri.Builder builder = new Uri.Builder()
                    //  .appendQueryParameter("type", "adduser")
                    //.appendQueryParameter("otp",mobileText.getText().toString())
                    .appendQueryParameter("phone",mainPhone);

            Log.d("mainnphone",  mainPhone);

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
//            Log.d("Validation",responseStr);

            if(responseStr!=null)
            {
                JSONObject jsonObject = new JSONObject(responseStr);
//                message = jsonObject.getString("message");
                success = jsonObject.getString("success");
//                otp = jsonObject.getString("otp");
            }

        }
        catch (SocketTimeoutException | UnknownHostException e)
        {
//            timeout = 1;
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
    }


}

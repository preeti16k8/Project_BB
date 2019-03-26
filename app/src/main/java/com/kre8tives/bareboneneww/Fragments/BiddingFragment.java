package com.kre8tives.bareboneneww.Fragments;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.text.Editable;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.kre8tives.bareboneneww.Database.DBAdapter;
import com.kre8tives.bareboneneww.R;
import com.kre8tives.bareboneneww.Util.Dialogs;
import com.kre8tives.bareboneneww.Util.ServerConnection;



import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


import static com.kre8tives.bareboneneww.Adapter.CategoryAdapter.checkbearid;
import static com.kre8tives.bareboneneww.Adapter.SelectStoreAdapter.storeId;
import static com.kre8tives.bareboneneww.Util.Dialogs.reject;


public class BiddingFragment extends Fragment {
    Button buyNowButton;
    List<String> answersaccept,answersreject;
    EditText bid_et_makeoffer;
    Spinner quantity;
    TextView minimumPrice;
    TextView buyNowPrice;
    int counter;
    TextView name;
    static TextView totalPrice;
    String min,buyNow,acceptancePrice,liquorName,id,newidd,maxprice;
    String offer;
    String updateResponse="";
    int accept;
    Integer timeout = 0;
    DBAdapter db;
    String quant;
    public static String LIQUORUPDATE_API= "http://barebonesbar.com/bbapi/current_bidapi.php";
    public BiddingFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bidding, container, false);

        //Dialogs
        db = new DBAdapter(getActivity());
        buyNowButton = (Button) v.findViewById(R.id.buyNowBidding);
        totalPrice=(TextView)v.findViewById(R.id.bid_txt_totalPrice);
        bid_et_makeoffer=(EditText)v.findViewById(R.id.bid_et_makeoffer);

        quantity = (Spinner) v.findViewById(R.id.categoryList);
        String[] items = new String[]{
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "11", "12", "13", "14","15","16","17","18","19","20",
                "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
                "31", "32", "33", "34", "35", "36", "37", "38", "39", "40",
                "41", "42", "43", "44", "45", "46", "47", "48", "49", "50",};
        //
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(quantity);

            // Set popupWindow height to 500px
            popupWindow.setHeight(500);

        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }

        //

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getActivity(), R.layout.support_simple_spinner_dropdown_item, items );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        quantity.setSelection(0);
        quantity.setAdapter(adapter);
        minimumPrice=(TextView)v.findViewById(R.id.bid_minBidPrice) ;
        buyNowPrice=(TextView)v.findViewById(R.id.bid_bidNowPrice);
        name=(TextView)v.findViewById(R.id.bid_selectedItem);
        min=getArguments().getString("minPrice");
        buyNow=getArguments().getString("buyNowPrice");
        acceptancePrice=getArguments().getString("acceptance");
        liquorName=getArguments().getString("ItemName");
        id=getArguments().getString("id");
        newidd=getArguments().getString("newid");
        maxprice=getArguments().getString("maxPrice");
        minimumPrice.setText(min);
        buyNowPrice.setText(buyNow);
        name.setText(liquorName);

        answersaccept=new ArrayList<>();
        answersreject=new ArrayList<>();

        answersaccept.add("Now that’s a bargain! Congrats!");
        answersaccept.add("You deserve a pat on your back, and a drink in your hand!");
        answersaccept.add("Venerations are in order, dear friend! Bravo!");
        answersaccept.add("Time to celebrate! Bring on the drinks!");
        answersaccept.add("Well done! Now let’s put it!");
        answersaccept.add("Why whine, when you can wine? Congrats!");
        answersaccept.add("It’s time to get your drink on!");
        answersaccept.add("Cheers! A hectic night is on the cards!");
        Collections.shuffle(answersaccept);

        answersreject.add("Good try… But not this time! Try again!");
        answersreject.add("Oooh, so close! Give it another go!");
        answersreject.add("Ah, alas. But no worries! Have another crack at it!");
        answersreject.add("Best to try again, buddy.");
        answersreject.add("Pfft. You can do better!");
        answersreject.add("Eeks, not this time!");
        answersreject.add("Success ain’t swift or easy, pal.");
        answersreject.add("Sigh. The less said the better. Try again!");
        answersreject.add("Loosen the purse strings a little!");
        answersreject.add("Don’t be a penny pincher! Try again!");
        answersreject.add("Do you want to be known as a tightwad?");
        answersreject.add("If you penny pinch, you won’t get your drink!");
        answersreject.add("Aha, you’re very close to the winning number!");
        answersreject.add("Not this time :( Bid again, buddy.");
        answersreject.add("No can do amigo. Bid again!");
        answersreject.add("Alas, not this time. Give it another go!");
        answersreject.add("Too bad! Have another crack at it!");
        answersreject.add("Shucks! Another try might do it! Go on!");
        answersreject.add("Aww that’s a shame. Try again!");
        answersreject.add("Fortune doesn’t always favour the brave. Be smart. Bid again!");
        Collections.shuffle(answersreject);


        quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                quant=quantity.getItemAtPosition(position).toString();

                itemCalculation(bid_et_makeoffer,quant,buyNow);
                Log.d("item",quant);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        bid_et_makeoffer.setFilters(new InputFilter[] { filter });

        bid_et_makeoffer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                itemCalculation(bid_et_makeoffer,quant,buyNow);
            }
        });


        buyNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                offer = bid_et_makeoffer.getText().toString();
                if (quant.equals("")|| offer.equals("")||Integer.parseInt(quant) == 0 || (Integer.parseInt(offer) == 0)) {
                    Toast.makeText(getActivity(), "Quantity or price can not be empty", Toast.LENGTH_SHORT).show();}
                else {
                    if (offer != null && offer != "") {

                        final float offerPrice = Float.parseFloat(String.valueOf(offer));
                        // <than min price--------2
                        if (offerPrice <= Float.parseFloat(String.valueOf(min))) {
                           // int idx = new Random().nextInt(lessMin.length);
                            //String random = (lessMin[idx]);
                            int idx = new Random().nextInt(reject.length);
                            String random = (reject[idx]);
                            showAlert("", random + new String(Character.toChars(0x1F61C)), "error");
                        }
                        // >than Bidacceptance---------3
                        else if (offerPrice >= Float.parseFloat(String.valueOf(acceptancePrice))) {
                            //int idx = new Random().nextInt(greatAccep.length);
                           // String random = (greatAccep[idx]);
                            int idx = new Random().nextInt(Dialogs.accept.length);
                            String random = (Dialogs.accept[idx]);

                            showAlert("" , random + new String(Character.toChars(0x1F60E)), "success");
                            gotopreview();
                        }
                        // >than max-------------------4
                        else if (offerPrice > Float.parseFloat(String.valueOf(maxprice)) && offerPrice > Float.parseFloat(String.valueOf(acceptancePrice))) {
                         //   int idx = new Random().nextInt(greatMax.length);
                           // String random = (greatMax[idx]);
                            int idx = new Random().nextInt(Dialogs.accept.length);
                            String random = (Dialogs.accept[idx]);
                            showAlert("", random+new String(Character.toChars(0x1F60E)), "success");
                            gotopreview();
                        }
                        // <than Bidacceptance and>min ---------5
                        else if (offerPrice < Float.parseFloat(String.valueOf(acceptancePrice)) && offerPrice > Float.parseFloat(String.valueOf(min))) {

                            int bidacc = Math.round(Float.parseFloat(String.valueOf(acceptancePrice)));
                            if (counter == 5) {
                                bidacc = bidacc + 10;
                                showAlert("", "Let me help you out! The last bid acceptance price was Rs." + bidacc + "" + new String(Character.toChars(0x1F61C)), "error");
                                counter = 0;
                            } else {
                               // int idx = new Random().nextInt(lessAccep.length);
                                //String random = (lessAccep[idx]);
                                int idx = new Random().nextInt(reject.length);
                                String random = (reject[idx]);
                                showAlert("", random + new String(Character.toChars(0x1F61C)), "error");
                            }

                            counter++;

                        } else {
                            showAlert("" , "Out of Range. "+ new String(Character.toChars(0x1F61C)), "success");

                        }
                    }
                }



            }
        });
        return v;
        }





    @Override
    public void onStop() {
        super.onStop();
        counter=0;
    }

    public static InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String blockCharacterSet = "~#^|$%*!@/()-'\":;,?{}=!$^';,?×÷<>{}€£¥₩%~`¤♡♥_|《》¡¿°•○●□■◇◆♧♣▲▼▶◀↑↓←→☆★▪:-);-):-D:-(:'(:O+N.";
            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    private void gotopreview() {
        db.openDB();
        if (liquorName != null && quant!= null && totalPrice.getText().toString() != null&&bid_et_makeoffer!=null) {
            String correct;
            if(Float.parseFloat(bid_et_makeoffer.getText().toString())>Float.parseFloat(buyNow)){
                correct=buyNow;
            }else {
                correct=bid_et_makeoffer.getText().toString();
            }
            db.add(newidd,liquorName, quant, totalPrice.getText().toString(),correct);
        }
        new editBidAcc().execute();
        String Domesticbeer = "164";
        String Importedbeer="165";
        String Breezer="192";
        if (checkbearid.equals(Domesticbeer)||checkbearid.equals(Importedbeer)||checkbearid.equals(Breezer))
        {
            OrderPreviewFragment orderPreviewFragment= new OrderPreviewFragment();
            android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, orderPreviewFragment);
            fragmentTransaction.addToBackStack(orderPreviewFragment.getClass().getName());
            fragmentTransaction.commit();
        }
        else{
            MixerFragment mixerFragment = new MixerFragment();
            android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, mixerFragment);
            fragmentTransaction.addToBackStack(mixerFragment.getClass().getName());
            fragmentTransaction.commit();
        }

    }

    private void showAlert(String title,String msg,String type) {
        switch(type){
            case "error":
                new AwesomeErrorDialog(getActivity())
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

            case "success":
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


            }

    }


    public static void itemCalculation(EditText bid_et_makeoffer,String quantity,String buyNow)
    {
        try
        {
            float Quantity = 0f;
            float Offer=0f;
            float buynw=0f;
            buynw=Float.parseFloat(buyNow);
            Offer=Float.parseFloat(bid_et_makeoffer.getText().toString());

            if(!quantity.equals(""))
            {
                Quantity = Float.parseFloat(quantity);
            }
            if(!bid_et_makeoffer.getText().toString().equals(""))
            {
                if(Offer>buynw){
                Offer = buynw;
                }else {
                    Offer=Float.parseFloat(bid_et_makeoffer.getText().toString());
                }
            }

            float TotalPrice = Quantity*Offer;

            Log.d("FinalTotal",TotalPrice+"");
            totalPrice.setText(String.valueOf(TotalPrice));

        }
        catch (Exception ignore)
        {

        }
    }

    public class editBidAcc extends AsyncTask<String, String, String> {


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
                    Log.d("newResponseBidding", updateResponse);
                    JSONObject jsonObject = new JSONObject(updateResponse);
                    if(jsonObject.getString("success").equals("1"))
                    {

                    }
                    else {
                        Toast.makeText(getActivity(),"Please Try Again",Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else {
                Toast.makeText(getActivity(),"Please Try Again",Toast.LENGTH_SHORT).show();
            }

        }

    }
    private void postUpdate()
    {

        HttpURLConnection conn = null;
        try {

            URL url = new URL(LIQUORUPDATE_API);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("store_id",storeId)
                    .appendQueryParameter("product_id",id)
                    .appendQueryParameter("bid_price",offer);

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

            Log.d("newstatus",offer);
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







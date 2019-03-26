package com.kre8tives.bareboneneww.Fragments;

import android.app.Fragment;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kre8tives.bareboneneww.Database.DBAdapter;

import com.kre8tives.bareboneneww.R;


import java.lang.reflect.Field;


import static com.kre8tives.bareboneneww.Adapter.CategoryAdapter.checkbearid;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyNowFragment extends android.support.v4.app.Fragment {

    Button buyNowButton;
    static TextView price;
    TextView name;
    Spinner quantity;
    static TextView totalPrice;
    String BuyNowPrice;
    String ItemName;
    String id,buynewid;
    DBAdapter db;
    String quant;
    public BuyNowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_buy_now, container, false);
        buyNowButton = (Button) v.findViewById(R.id.buyNowBidding);
        price = (TextView) v.findViewById(R.id.txtView_Price);
        //Spinner
        quantity = (Spinner) v.findViewById(R.id.categoryList);
        String[] items = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14","15"};
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
        //Spinner ends
        totalPrice = (TextView) v.findViewById(R.id.buyNow_totalPrice);
        name = (TextView) v.findViewById(R.id.buyNow_itemName);

        BuyNowPrice = getArguments().getString("buyNowPrice");
        ItemName = getArguments().getString("ItemName");
        id=getArguments().getString("id");

        buynewid=getArguments().getString("newidbuy");


        price.setText(BuyNowPrice);
        name.setText(ItemName);


        db = new DBAdapter(getActivity());
        quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                quant=quantity.getItemAtPosition(position).toString();
                itemCalculation(quant);
                Log.d("item",quant);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });



        buyNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (quant.equals("")) {

                    Toast.makeText(getActivity(), "Please enter quantity", Toast.LENGTH_LONG).show();

                } else if (Integer.parseInt(quant) == 0) {
                    Toast.makeText(getActivity(), "Quantity can not be zero!!", Toast.LENGTH_LONG).show();
                } else {

                    String Domesticbeer = "164",Breezer="192",Importedbeer="165";
                    String shootrs="182",classics="168",mrtini="197",wines="161";
                    String nonalco="167",white="200",red="201",spark="202",coolers="162";
                    String  mock="163",bottle="204";

                    if (checkbearid.equals(Domesticbeer)||checkbearid.equals(Importedbeer)||checkbearid.equals(Breezer)||checkbearid.equals(shootrs)||checkbearid.equals(classics)||checkbearid.equals(mrtini)||checkbearid.equals(wines)||checkbearid.equals(Domesticbeer)||checkbearid.equals(nonalco)||checkbearid.equals(white)||checkbearid.equals(red)||checkbearid.equals(spark)||checkbearid.equals(coolers)||checkbearid.equals(mock)||checkbearid.equals(bottle))

                    {
                        db.openDB();
                        if (ItemName != null && quant != null && totalPrice.getText().toString() != null && BuyNowPrice!=null) {
                            db.add(buynewid, ItemName, quant, totalPrice.getText().toString(),BuyNowPrice);

                        }


                        OrderPreviewFragment orderPreviewFragment= new OrderPreviewFragment();
                        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, orderPreviewFragment);
                        fragmentTransaction.addToBackStack(orderPreviewFragment.getClass().getName());
                        fragmentTransaction.commit();
                    }

                    else {
                        db.openDB();
                        if (ItemName != null && quant!= null && totalPrice.getText().toString() != null && BuyNowPrice!=null) {
                            db.add(buynewid, ItemName, quant.toString(), totalPrice.getText().toString(),BuyNowPrice);
                            Log.d("buynowpricecc",BuyNowPrice);
                        }

                        MixerFragment bid = new MixerFragment();
                        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, bid);
                        fragmentTransaction.addToBackStack(bid.getClass().getName());
                        fragmentTransaction.commit();



                    }
                }


            }
        });
        return v;

    }



    public static void itemCalculation(String quantity)
    {
        try
        {
            float Quantity = 0f;

            float Price = Float.parseFloat(price.getText().toString());

            if(!quantity.equals(""))
            {
                Quantity = Float.parseFloat(quantity);
            }

            float TotalPrice = Quantity*Price;

            Log.d("FinalTotal",TotalPrice+"");
            totalPrice.setText(String.valueOf(TotalPrice));

        }
        catch (Exception ignore)
        {

        }
    }



}

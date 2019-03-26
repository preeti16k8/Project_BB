package com.kre8tives.bareboneneww.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kre8tives.bareboneneww.Activity.StoreActivity;

import com.kre8tives.bareboneneww.R;
public class DashBoardFragment extends Fragment {
    TextView foodName, drinkName;
    ImageView foodImage, drinkImage;
    LinearLayout food,drinks;

    public DashBoardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dash_board, container, false);
        foodName = view.findViewById(R.id.foodText);
        drinkName = view.findViewById(R.id.drinksText);
        foodImage = view.findViewById(R.id.Imgview_food);
        drinkImage = view.findViewById(R.id.Imgview_drink);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");

        //Dashboard Two Categories
        food= view.findViewById(R.id.ll_food);
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodCategoryFragment foodCategoryFragment=new FoodCategoryFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, foodCategoryFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        drinks= view.findViewById(R.id.ll_drink);
        drinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemSettingsFragment itemSettingsFragment=new ItemSettingsFragment();
                getActivity().getSupportFragmentManager().beginTransaction()

                        .replace(R.id.content_frame, itemSettingsFragment,"ItemSetting").addToBackStack("ItemSetting")
                        .commit();

            }
        });
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    protected void exitByBackKey() {
        AlertDialog alertbox = new AlertDialog.Builder(getActivity())
                .setMessage("Do you want to exit the app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(getActivity(), StoreActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("EXIT", true);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }
                })
                .show();
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
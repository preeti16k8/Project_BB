package com.kre8tives.bareboneneww.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kre8tives.bareboneneww.Fragments.FoodSettingsFragment;
import com.kre8tives.bareboneneww.Model.CategoryFood;
import com.kre8tives.bareboneneww.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ashet on 05-10-2017.
 */

public class CategoryFoodAdapter extends RecyclerView.Adapter<CategoryFoodAdapter.CategoryfoodViewholder>
{


    private Context mContext;
    private List<CategoryFood> categoryfoodList;
    ArrayList<String> arrayList;
    public static String checkbearid;

    public CategoryFoodAdapter(List<CategoryFood> categoryfoodList, Context context) {
        this.mContext = context;
        this.categoryfoodList = categoryfoodList;
    }

    @Override
    public CategoryFoodAdapter.CategoryfoodViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.categoryfoodfragment_item, null);
        CategoryFoodAdapter.CategoryfoodViewholder categoryfoodViewholder = new CategoryFoodAdapter.CategoryfoodViewholder(view);
        return categoryfoodViewholder;

    }

    @Override
    public void onBindViewHolder(final CategoryFoodAdapter.CategoryfoodViewholder holder, final int position) {
        final CategoryFood categoryFood = categoryfoodList.get(position);
        holder.categoryfoodName.setText(categoryFood.getFoodCetagoryName());
        Picasso.with(mContext).load(categoryFood.getFoodCatagoryImage()).into(holder.categoryfoodImage);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkbearid = categoryFood.getFoodCategoryId();
                FoodSettingsFragment fooditemsfragment = new FoodSettingsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("categoryid", categoryFood.getFoodCategoryId());
                bundle.putString("categoryname", categoryFood.getFoodCetagoryName());
                //bundle.putString("vegnonveg",categoryfood.get);
                fooditemsfragment.setArguments(bundle);
                FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fooditemsfragment);
                fragmentTransaction.addToBackStack(fooditemsfragment.getClass().getName());
                fragmentTransaction.commit();



            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryfoodList.size();
    }

    public static class CategoryfoodViewholder extends RecyclerView.ViewHolder {
        TextView categoryfoodName;
        ImageView categoryfoodImage;
        View view;

        public CategoryfoodViewholder(View itemView) {
            super(itemView);
            view = itemView;
            categoryfoodImage = (ImageView) itemView.findViewById(R.id.categoryfoodImageView);
            categoryfoodName = (TextView) itemView.findViewById(R.id.categoryfoodTextView);
        }
    }
}


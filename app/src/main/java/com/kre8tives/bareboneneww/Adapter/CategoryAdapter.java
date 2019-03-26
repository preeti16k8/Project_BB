package com.kre8tives.bareboneneww.Adapter;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kre8tives.bareboneneww.Fragments.BrandItemFragment;
import com.kre8tives.bareboneneww.Model.Category;
import com.kre8tives.bareboneneww.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 4/11/2017.
 */


public class CategoryAdapter extends RecyclerView.Adapter <CategoryAdapter.CategoryViewholder> {
    private Context mContext;
    private List<Category> categoryList;
    ArrayList<String> arrayList;
    // public static String categoryidbear;
    public static String checkbearid;

    public CategoryAdapter(List<Category> categoryList, Context context) {
        this.mContext = context;
        this.categoryList = categoryList;
    }

    @Override
    public CategoryAdapter.CategoryViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.categoryfragment_item, null);
        CategoryAdapter.CategoryViewholder categoryViewholder = new CategoryAdapter.CategoryViewholder(view);
        return categoryViewholder;

    }

    @Override
    public void onBindViewHolder(final CategoryViewholder holder, final int position) {
        final Category category = categoryList.get(position);
        holder.categoryName.setText(category.getName());
        Picasso.with(mContext).load(category.getImageUrl()).into(holder.categoryImage);
      //  holder.categoryImage.setImageBitmap(highlightImage(BitmapFactory.decodeResource(getResources(), R.drawable.android_droid)));


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkbearid = category.getId();
                Log.d("Category Clicked",checkbearid);
                BrandItemFragment brandFragment = new BrandItemFragment();
                Bundle bundle = new Bundle();
                bundle.putString("categoryid", category.getId());
                bundle.putString("categoryname", category.getName());
                brandFragment.setArguments(bundle);
                FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, brandFragment);
                fragmentTransaction.addToBackStack(brandFragment.getClass().getName());
                fragmentTransaction.commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewholder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView categoryImage;
        View view;

        public CategoryViewholder(View itemView) {
            super(itemView);
            view = itemView;
            categoryImage = (ImageView) itemView.findViewById(R.id.categoryImageView);
            categoryName = (TextView) itemView.findViewById(R.id.categoryTextView);
        }
    }


}
package com.kre8tives.bareboneneww.Adapter;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kre8tives.bareboneneww.Fragments.OrderPreviewFragment;
import com.kre8tives.bareboneneww.MarketCrash.LiquorItemPriceFragment;
import com.kre8tives.bareboneneww.Model.BrandItem;
import com.kre8tives.bareboneneww.R;
import com.squareup.picasso.Picasso;

import java.util.List;



public class BrandItemAdapter extends RecyclerView.Adapter <BrandItemAdapter.BrandItemViewholder>{

    private Context mContext;
    int minteger;

    private List<BrandItem> brandItemList;

    public BrandItemAdapter(List<BrandItem> brandItemList, Context context){
        this.mContext = context;
        this.brandItemList = brandItemList;
    }
    @Override
    public BrandItemAdapter.BrandItemViewholder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.singleitem_branditem,null);
        BrandItemAdapter.BrandItemViewholder brandItemViewholder = new BrandItemAdapter.BrandItemViewholder(view);
        return brandItemViewholder;
    }

    @Override
    public void onBindViewHolder(BrandItemAdapter.BrandItemViewholder holder, int position) {

        Log.d("positionfind", String.valueOf(position));
        final BrandItem brandItem = brandItemList.get(position);
        Picasso.with(mContext).load(brandItem.getIimageUrl()).into(holder.brandItem_Image);
        holder.brandItem_Name.setText(brandItem.getIname());
        ///////////////Mocktaiils and all
        String[] catID = {"163","198","167","162"};
        String catd;
        if (brandItem.getIcategoryId().equals("163")||brandItem.getIcategoryId().equals("162")||brandItem.getIcategoryId().equals("198")||brandItem.getIcategoryId().equals("167")) {
            holder.pricecat.setVisibility(View.VISIBLE);
            holder.ll_Prices.setVisibility(View.GONE);
            holder.pricecat.setText("Rs. "+(brandItem.getBrandminprice()));


            ///clicked
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
                    LayoutInflater inflater=((Activity)mContext).getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                    dialogBuilder.setView(dialogView);
                    final TextView edt = (TextView) dialogView.findViewById(R.id.edit1);
                    //Plus minus
                    edt.setText("0");
                    final Button incr=dialogView.findViewById(R.id.increase);
                    final Button decr=dialogView.findViewById(R.id.decrease);
                    dialogBuilder.setMessage("Please Enter Quantity");

                    //
                    incr.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            minteger = minteger + 1;
                            if(minteger>100){
                                edt.setText("0");
                                minteger=0;
                            }else{
                                edt.setText(""+minteger);}

                        }
                    });
                    decr.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            minteger = minteger - 1;
                            if(minteger<0){
                                edt.setText("0");
                                minteger=0;
                            }else{
                                edt.setText(""+minteger);}


                        }
                    });
                    //ends plus minus
                    dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (edt.getText().toString().equals("")) {
                                Toast.makeText(mContext, "Please enter quantity", Toast.LENGTH_SHORT).show();
                            }
                            else if (Integer.parseInt(edt.getText().toString())==0){
                                Toast.makeText(mContext, "Quantity can not be zero!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                float quantity = Float.parseFloat(edt.getText().toString());
                                float price = Float.parseFloat(brandItem.getBrandminprice());
                                float TotalPrice = quantity * price;
                                FoodAdapter.Fname = brandItem.getIname();
                                FoodAdapter.Fprice = String.valueOf(TotalPrice);
                                FoodAdapter.Fnewprice= String.valueOf(price);
                                FoodAdapter.Fqty = edt.getText().toString();
                                FoodAdapter.Fid = brandItem.getIid();
                                FoodAdapter.FidnewId = brandItem.getNewId();
                                OrderPreviewFragment orderPreviewFragment = new OrderPreviewFragment();
                                FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.content_frame, orderPreviewFragment);
                                fragmentTransaction.addToBackStack(orderPreviewFragment.getClass().getName());
                                fragmentTransaction.commit();
                            }
                        }
                    });

                    AlertDialog b = dialogBuilder.create();
                    b.show();
                }


            });
            ///
            ///////////mocktails n all

        } else {
            holder.tv_brandimgPriceMax.setText(brandItem.getBrandmaxprice());
            holder.tv_brandimgPriceMin.setText(brandItem.getBrandminprice());

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LiquorItemPriceFragment liquorItemPriceFragment = new LiquorItemPriceFragment();
                    Bundle args = new Bundle();
                    args.putString("categoryId", brandItem.getIcategoryId());
                    args.putString("brandId", brandItem.getIbrandId());
                    args.putString("ItemId", brandItem.getIid());
                    args.putString("ItemName", brandItem.getIname());


                    liquorItemPriceFragment.setArguments(args);
                    FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, liquorItemPriceFragment);
                    fragmentTransaction.addToBackStack(liquorItemPriceFragment.getClass().getName());
                    fragmentTransaction.commit();
                }
            });
        }
    }



    @Override
    public int getItemCount() {
        return brandItemList.size();
    }
    public static class BrandItemViewholder extends RecyclerView.ViewHolder
    {
        ImageView brandItem_Image;
        TextView brandItem_Name,tv_brandimgPriceMax,tv_brandimgPriceMin;
        LinearLayout ll_Prices;
        TextView pricecat;

        View view;
        public BrandItemViewholder(View itemView) {
            super(itemView);
            view = itemView;
            pricecat=itemView.findViewById(R.id.priceothers);
            brandItem_Image = (ImageView) itemView.findViewById(R.id.brandItem_Image);
            brandItem_Name = (TextView) itemView.findViewById(R.id.brandItem_Name);
            tv_brandimgPriceMax=(TextView)itemView.findViewById(R.id.tv_brandimgPriceMax);
            tv_brandimgPriceMin=(TextView)itemView.findViewById(R.id.tv_brandimgPriceMin);
            ll_Prices=(LinearLayout)itemView.findViewById(R.id.ll_Prices);

        }
    }
}

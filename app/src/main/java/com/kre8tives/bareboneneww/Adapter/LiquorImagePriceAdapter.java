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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kre8tives.bareboneneww.Fragments.BiddingFragment;
import com.kre8tives.bareboneneww.Fragments.BuyNowFragment;
import com.kre8tives.bareboneneww.Model.LiquorImagePrice;
import com.kre8tives.bareboneneww.R;
import com.squareup.picasso.Picasso;
import java.util.List;
public class LiquorImagePriceAdapter extends RecyclerView.Adapter <LiquorImagePriceAdapter.BrandViewholder>{
    private Context mContext;
    private List<LiquorImagePrice> liquorImagePriceList;
    public LiquorImagePriceAdapter(List<LiquorImagePrice> liquorImagePriceList, Context context){
        this.mContext = context;
        this.liquorImagePriceList = liquorImagePriceList;
    }
    @Override
    public LiquorImagePriceAdapter.BrandViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.singleitem_lqourimgprice,null);
        LiquorImagePriceAdapter.BrandViewholder brandViewholder = new LiquorImagePriceAdapter.BrandViewholder(view);
        return brandViewholder;
    }

    @Override
    public void onBindViewHolder(LiquorImagePriceAdapter.BrandViewholder holder, int position) {
        final LiquorImagePrice liquorImagePrice = liquorImagePriceList.get(position);
        String cata=liquorImagePrice.getLcategoryId();
        holder.tv_liquorimgname.setText(liquorImagePrice.getLname());
        holder.tv_liquorimgname.setText(liquorImagePrice.getLname());
        holder.tv_liquorimgPriceMax.setText(liquorImagePrice.getLmaxPrice());
        holder.tv_liquorimgPriceMin.setText(liquorImagePrice.getLminPrice());
       holder.btn_imgpricebuynw.setText("buynow@"+liquorImagePrice.getLbuyNow());
        Picasso.with(mContext).load( liquorImagePrice.getLimageUrl()).into(holder.imageView_ItmPrice);
       holder.btn_imgpricebuynw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyNowFragment buyNowFragment=new BuyNowFragment();
                Bundle b=new Bundle();
                b.putString("buyNowPrice",liquorImagePrice.getLbuyNow());
                b.putString("ItemName",liquorImagePrice.getLname());
                b.putString("id",liquorImagePrice.getLid());
                b.putString("newidbuy",liquorImagePrice.getNewid());
                buyNowFragment.setArguments(b);
                FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, buyNowFragment);
                fragmentTransaction.addToBackStack(buyNowFragment.getClass().getName());
                fragmentTransaction.commit();
            }
        });

        holder.btn_imgpricebidnw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BiddingFragment biddingFragment=new BiddingFragment();
                Bundle bid=new Bundle();
                bid.putString("minPrice",liquorImagePrice.getLminPrice());
                bid.putString("buyNowPrice",liquorImagePrice.getLbuyNow());
                bid.putString("maxPrice",liquorImagePrice.getLmaxPrice());
                bid.putString("ItemName",liquorImagePrice.getLname());
                bid.putString("acceptance",liquorImagePrice.getLbidacceptance());
                bid.putString("id",liquorImagePrice.getLid());
                bid.putString("newid",liquorImagePrice.getNewid());
                biddingFragment.setArguments(bid);
                FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, biddingFragment);
                fragmentTransaction.addToBackStack(biddingFragment.getClass().getName());
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return liquorImagePriceList.size();
    }


    public static class BrandViewholder extends RecyclerView.ViewHolder
    {
        ImageView imageView_ItmPrice;
        TextView tv_liquorimgPriceMax,tv_liquorimgPriceMin,tv_liquorimgPriceBuyNw,tv_liquorimgname;
        Button btn_imgpricebuynw,btn_imgpricebidnw;
        View view;
        public BrandViewholder(View itemView) {
            super(itemView);
            view = itemView;
            imageView_ItmPrice = (ImageView) itemView.findViewById(R.id.imageView_ItmPrice);
            tv_liquorimgPriceMax = (TextView) itemView.findViewById(R.id.tv_liquorimgPriceMax);
            tv_liquorimgPriceMin = (TextView) itemView.findViewById(R.id.tv_liquorimgPriceMin);
            tv_liquorimgname = (TextView) itemView.findViewById(R.id.tv_liquorimgname);
            btn_imgpricebuynw=(Button)itemView.findViewById(R.id.btn_imgpricebuynw);
            btn_imgpricebidnw=(Button)itemView.findViewById(R.id.btn_imgpricebidnw);
        }
    }
}

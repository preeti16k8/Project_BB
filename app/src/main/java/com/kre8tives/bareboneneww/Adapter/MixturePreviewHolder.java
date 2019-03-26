package com.kre8tives.bareboneneww.Adapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.kre8tives.bareboneneww.Database.ItemClickListener;
import com.kre8tives.bareboneneww.R;

public class MixturePreviewHolder extends RecyclerView.ViewHolder {
        TextView nametxt, posTxt;
        ImageView image_delete;
        TextView biddindConfName;
        TextView biddingConfPrice;
        TextView biddingQuantity;
        TextView biddingConfSinglePrice;
        ItemClickListener itemClickListener;

        public MixturePreviewHolder(View itemView) {
            super(itemView);
            biddindConfName = (TextView) itemView.findViewById(R.id.biddingConfName);
            biddingConfPrice = (TextView) itemView.findViewById(R.id.biddingConfPrice);
            biddingQuantity = (TextView) itemView.findViewById(R.id.biddingConfQuantity);
            image_delete = (ImageView) itemView.findViewById(R.id.image_delete);
            biddingConfSinglePrice=(TextView)itemView.findViewById(R.id.biddingConfSinglePrice);
        }
    }
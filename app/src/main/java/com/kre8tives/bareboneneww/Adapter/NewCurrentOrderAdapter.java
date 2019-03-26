package com.kre8tives.bareboneneww.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kre8tives.bareboneneww.Model.NewCurrentOrder;
import com.kre8tives.bareboneneww.R;

import java.util.List;

public class NewCurrentOrderAdapter extends RecyclerView.Adapter <NewCurrentOrderAdapter.CurrentOrderViewholder>{
    private Context mContext;
    public static List<NewCurrentOrder> newCurrentOrderList;

    public NewCurrentOrderAdapter(List<NewCurrentOrder> newCurrentOrderList, Context context){
        this.mContext = context;
        this.newCurrentOrderList = newCurrentOrderList;
    }
    @Override
    public NewCurrentOrderAdapter.CurrentOrderViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.singleitem_newcurrentorder,parent,false);
        NewCurrentOrderAdapter.CurrentOrderViewholder currentOrderViewholder = new NewCurrentOrderAdapter.CurrentOrderViewholder(view);
        return currentOrderViewholder;
    }
    @Override
    public void onBindViewHolder(NewCurrentOrderAdapter.CurrentOrderViewholder holder, final int position) {
        NewCurrentOrder newCurrentOrder = newCurrentOrderList.get(position);

        holder.tv_nwcurrentItem.setText(newCurrentOrder.getCname());
        holder.tv_nwcurrentPrice.setText(newCurrentOrder.getCprice());
        holder.tv_nwcurrentQty.setText(newCurrentOrder.getCqty());
        //18% calculation
        holder.tv_nwcurrentsubtotal.setText(newCurrentOrder.getCsubtotal());
    }
    @Override
    public int getItemCount() {
        return newCurrentOrderList.size();
    }
    public static class CurrentOrderViewholder extends RecyclerView.ViewHolder
    {
        TextView tv_nwcurrentItem,tv_nwcurrentPrice,tv_nwcurrentQty,tv_nwcurrentStatus,tv_nwcurrentsubtotal;
        View view;
        public CurrentOrderViewholder(View itemView) {
            super(itemView);
            view = itemView;
            tv_nwcurrentItem = (TextView) itemView.findViewById(R.id.tv_nwcurrentItem);
            tv_nwcurrentPrice = (TextView) itemView.findViewById(R.id.tv_nwcurrentPrice);
            tv_nwcurrentQty = (TextView) itemView.findViewById(R.id.tv_nwcurrentQty);
            tv_nwcurrentsubtotal=(TextView)itemView.findViewById(R.id.tv_nwcurrentsubtotal);
        }
    }
}
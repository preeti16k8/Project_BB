package com.kre8tives.bareboneneww.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kre8tives.bareboneneww.R;
import com.kre8tives.bareboneneww.Orders.Item;
import com.kre8tives.bareboneneww.Orders.Orders;

import java.util.List;


public class OrderHistoryAdapter extends RecyclerView.Adapter <OrderHistoryAdapter.OrderHistoryViewholder>{
    private Context mContext;
    public static List<Orders> orderHistoryPojos;


    public OrderHistoryAdapter(List<Orders> orderHistoryPojos, Context context){
        this.mContext = context;
        this.orderHistoryPojos = orderHistoryPojos;
    }
    @Override
    public OrderHistoryAdapter.OrderHistoryViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.singleitem_orderhistory,null);
        OrderHistoryAdapter.OrderHistoryViewholder currentOrderViewholder = new OrderHistoryAdapter.OrderHistoryViewholder(view);
        return currentOrderViewholder;
    }

    @Override
    public void onBindViewHolder(OrderHistoryAdapter.OrderHistoryViewholder holder, final int position) {
        final Orders historyPojo = orderHistoryPojos.get(position);
        List<Item> items=historyPojo.getItems();
        Log.d("Histrysiz", "" + historyPojo.toString());
        holder.orderId.setText(historyPojo.getOrderId());
        String input=historyPojo.getOrderStatus();
        String status=input.substring(0,1).toUpperCase()+input.substring(1);
        holder.orderStatus.setText(status);
        holder.orderAmount.setText(historyPojo.getOrderAmount());
        holder.date.setText(historyPojo.getOrderDate());
        //Dynamic
        LayoutInflater layoutInflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addViewv = layoutInflater.inflate(R.layout.row_layout, null);
        TextView textOutt = (TextView)addViewv.findViewById(R.id.item_name);
        textOutt.setText("Items");
        textOutt.setTextSize(15);
        TextView quanttyy = (TextView)addViewv.findViewById(R.id.ItemQuantity);
        quanttyy.setText("Quantity");
        quanttyy.setTextSize(15);
        TextView textprice = (TextView)addViewv.findViewById(R.id.item_price);
        textprice.setText("Price");
        textprice.setTextSize(15);
        TextView subtotall = (TextView)addViewv.findViewById(R.id.subtotal);
        subtotall.setText(""+"Total");
        subtotall.setTextSize(15);
        holder.linearLayout.addView(addViewv);


        for(int i=0;i<items.size();i++){

        final View addView = layoutInflater.inflate(R.layout.row_layout, null);
        TextView textOut = (TextView)addView.findViewById(R.id.item_name);
        textOut.setText(items.get(i).getItemName());
        TextView quantty = (TextView)addView.findViewById(R.id.ItemQuantity);
        quantty.setText(items.get(i).getItemQuantity());
        TextView price = (TextView)addView.findViewById(R.id.item_price);
        price.setText(items.get(i).getItemPrice());
        TextView subtotal = (TextView)addView.findViewById(R.id.subtotal);
        subtotal.setText(""+items.get(i).getItemTotal());
        holder.linearLayout.addView(addView);}

    }
    @Override
    public int getItemCount() {
        return orderHistoryPojos.size();
    }
    public static class OrderHistoryViewholder extends RecyclerView.ViewHolder
    {
        TextView orderId,orderAmount,orderStatus,date;
        View view;
        LinearLayout linearLayout;

        public OrderHistoryViewholder(View itemView) {
            super(itemView);
            view = itemView;
            orderId = itemView.findViewById(R.id.orderId);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            orderAmount=itemView.findViewById(R.id.orderAmount);
            date=itemView.findViewById(R.id.orderDate);
            linearLayout=itemView.findViewById(R.id.items_layout);


        }
    }
}


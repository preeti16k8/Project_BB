package com.kre8tives.bareboneneww.ExpandableOrders;

import android.view.View;
import android.widget.TextView;

import com.kre8tives.bareboneneww.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class ItemsViewHolder extends ChildViewHolder {


    private TextView itemName,itemQuantity,itemPrice,itemtoal;

    public ItemsViewHolder(View itemView) {
        super(itemView);
        itemName = itemView.findViewById(R.id.expaitemname);
        itemQuantity=itemView.findViewById(R.id.expaitemquantity);
        itemPrice=itemView.findViewById(R.id.expaitemprice);
        itemtoal=itemView.findViewById(R.id.expaitemtotal);
    }

    public void setitem(String name,String quantyti,String price,String total) {
        itemName.setText(name);
        itemQuantity.setText(quantyti);
        itemPrice.setText(price);
        itemtoal.setText(total);
    }
}
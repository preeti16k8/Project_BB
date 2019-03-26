package com.kre8tives.bareboneneww.ExpandableOrders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kre8tives.bareboneneww.Orders.Item;
import com.kre8tives.bareboneneww.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class OrdersAdapter extends ExpandableRecyclerViewAdapter<OrdersViewHolder, ItemsViewHolder> {

    public OrdersAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }
    @Override
    public OrdersViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expa_orders_header, parent, false);
        return new OrdersViewHolder(view);
    }

    @Override
    public ItemsViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expa_items_content, parent, false);
        return new ItemsViewHolder(view);

    }

    @Override
    public void onBindChildViewHolder(ItemsViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final Item item=((ExpaOrders)group).getItems().get(childIndex);
        holder.setitem(item.getItemName(),item.getItemQuantity(),item.getItemPrice(),item.getItemTotal().toString());


    }

    @Override
    public void onBindGroupViewHolder(OrdersViewHolder holder, int flatPosition, ExpandableGroup group) {

        holder.setGenreTitle(group);

    }
}
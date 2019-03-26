package com.kre8tives.bareboneneww.ExpandableOrders;

import com.kre8tives.bareboneneww.Orders.Item;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;


import java.util.List;

public class ExpaOrders extends ExpandableGroup<Item> {

    private String OrderDate;



    private int iconResId;

    public int getIconResId() {
        return iconResId;
    }
    public String getOrderstatus() {
        return Orderstatus;
    }

    public String getOrdertotal() {
        return ordertotal;
    }

    private String Orderstatus;
    private String ordertotal;
    public ExpaOrders(String title, List<Item> items,String orderDate,String status,String total,int resid) {
        super(title, items);
        this.OrderDate=orderDate;
        this.Orderstatus=status;
        this.ordertotal=total;
        this.iconResId=resid;
    }
    public String getOrderDate() {
        return OrderDate;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExpaOrders)) return false;

        ExpaOrders genre = (ExpaOrders) o;
        if(getOrderstatus()==((ExpaOrders) o).getOrderstatus())
            return true;
        return getOrderDate() == genre.getOrderDate()&&
                getOrderstatus()==genre.getOrderstatus()&&
                getOrdertotal()==genre.getOrdertotal()&&
                getIconResId()==genre.getIconResId();


    }
}
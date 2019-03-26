package com.kre8tives.bareboneneww.Orders;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;





public class Item implements Parcelable {


    @SerializedName("_id")
    @Expose
    private String id;

    private String orderID;

    @SerializedName("item_name")
    @Expose
    private String itemName;


    @SerializedName("item_price")
    @Expose
    private String itemPrice;

    @SerializedName("item_quantity")
    @Expose
    private String itemQuantity;


    @SerializedName("item_total")
    @Expose
    private Integer itemTotal;


    public Item(String id, String orderId, String itemName, String itemPrice,
            String itemQuantity, Integer itemTotal) {
        this.id = id;
        this.orderID=orderId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.itemTotal = itemTotal;
    }


    public Item() {
    }

    protected Item(Parcel in) {
        id = in.readString();
        orderID = in.readString();
        itemName = in.readString();
        itemPrice = in.readString();
        itemQuantity = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public Integer getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(Integer itemTotal) {
        this.itemTotal = itemTotal;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemName);
        dest.writeString(itemPrice);
        dest.writeString(itemQuantity);
    }

}

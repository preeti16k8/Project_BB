package com.kre8tives.bareboneneww.Orders;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


 public class Orders {

     @SerializedName("order_id")
    @Expose
    private String orderId;

    @SerializedName("customer_id")
    @Expose
    private String customerId;

    @SerializedName("items")
    @Expose
    private List<Item> items = null;

    @SerializedName("order_amount")
    @Expose
    private String orderAmount;


    @SerializedName("order_date")
    @Expose
    private String orderDate;


    @SerializedName("order_status")
    @Expose
    private String orderStatus;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

}
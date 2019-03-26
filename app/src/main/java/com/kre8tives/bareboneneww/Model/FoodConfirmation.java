package com.kre8tives.bareboneneww.Model;

/**
 * Created by Sai Gowtham on 08-04-2017.
 */
public class FoodConfirmation {

    private String item,price,quantity;

    public FoodConfirmation() {
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public FoodConfirmation(String item, String price, String quantity) {

        this.item = item;
        this.price = price;
        this.quantity = quantity;
    }


}

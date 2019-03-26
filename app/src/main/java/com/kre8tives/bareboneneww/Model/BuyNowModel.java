package com.kre8tives.bareboneneww.Model;

/**
 * Created by user on 5/6/2017.
 */

public class BuyNowModel {
    private int id;
    private String name,position;
    private String quantity,price,newid,singleprice;
    private int image;

    public String getSingleprice() {
        return singleprice;
    }

    public BuyNowModel(int id,String newid, String name, String quantity, String price, String singleprice) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.newid = newid;
        this.singleprice = singleprice;
    }

    public void setSingleprice(String singleprice) {
        this.singleprice = singleprice;
    }

    public String getNewid() {
        return newid;
    }

    public void setNewid(String newid) {
        this.newid = newid;
    }

    public String getQuantity() {
        return quantity;
    }
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public BuyNowModel(int id, String name, String position, int image) {
        this.id = id;
        this.name = name;

        this.position = position;
        this.image = image;
    }
    public BuyNowModel(int id, String newid, String name, String quantity, String price) {
        this.price = price;
        this.id=id;
        this.newid=newid;
        this.name=name;
        this.quantity=quantity;


    }

    public BuyNowModel(int id, String name, String quantity, String price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public int getImage() {
        return image;
    }
    public void setImage(int image) {
        this.image = image;
    }
}
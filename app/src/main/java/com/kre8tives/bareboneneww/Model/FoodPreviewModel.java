package com.kre8tives.bareboneneww.Model;

/**
 * Created by user on 5/9/2017.
 */


public class FoodPreviewModel {
    private int id;

    public FoodPreviewModel(int id,String newid,String name, String quantity, String price, String singleprice) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.newid = newid;
        this.singleprice = singleprice;
    }

    private String name,position;
    private String quantity;
    private String price;
    private String newid;

    public String getNewid() {
        return newid;
    }

    public void setNewid(String newid) {
        this.newid = newid;
    }

    public String getSingleprice() {
        return singleprice;
    }

    public void setSingleprice(String singleprice) {
        this.singleprice = singleprice;
    }

    private String singleprice;
    private int image;

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

    public FoodPreviewModel(int id, String name, String position, int image) {
        this.id = id;
        this.name = name;

        this.position = position;
        this.image = image;
    }
    public FoodPreviewModel(int id, String newid, String name, String quantity, String price) {

        this.price = price;
        this.id = id;
        this.newid = newid;

        this.quantity = quantity;
        this.name = name;



    }

    public FoodPreviewModel(int id, String name, String quantity, String price) {
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

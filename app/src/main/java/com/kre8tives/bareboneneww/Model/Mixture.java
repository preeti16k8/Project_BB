package com.kre8tives.bareboneneww.Model;

/**
 * Created by Sai Gowtham on 07-04-2017.
 */
public class Mixture {
    public Mixture(String name, String price, String id, String imageUrl, String stock, String maxStock, String newid) {
        this.name = name;
        this.price = price;
        this.id = id;
        ImageUrl = imageUrl;
        this.stock = stock;
        this.maxStock = maxStock;
        this.newid = newid;
    }

    public String getNewid() {
        return newid;
    }

    public void setNewid(String newid) {
        this.newid = newid;
    }

    private String name,price,id,ImageUrl,stock,maxStock,newid;

    public Mixture() {
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getMaxStock() {
        return maxStock;
    }

    public void setMaxStock(String maxStock) {
        this.maxStock = maxStock;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Mixture(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Mixture(String name, String price) {

        this.name = name;
        this.price = price;
    }
}

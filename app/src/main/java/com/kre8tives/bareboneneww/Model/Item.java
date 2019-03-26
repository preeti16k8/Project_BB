package com.kre8tives.bareboneneww.Model;

/**
 * Created by Administrator on 4/11/2017.
 */

public class Item {


    public String name,maxPrice,minPrice,buyNowPrice,id,CategoryId,BrandId;
    //name,minPrice,maxPrice,buyNow,id,categoryId,brandId,stock;

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getBrandId() {
        return BrandId;
    }

    public void setBrandId(String brandId) {
        BrandId = brandId;
    }

    public Item(String name, String maxPrice, String minPrice, String buyNowPrice, String id, String categoryId, String brandId) {
        this.name = name;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;

        this.buyNowPrice = buyNowPrice;
        this.id = id;
        CategoryId = categoryId;
        BrandId = brandId;
    }

    public String getId() {
        return id;
    }

    public Item() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public Item(String id) {

        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getBuyNowPrice() {
        return buyNowPrice;
    }

    public void setBuyNowPrice(String buyNowPrice) {
        this.buyNowPrice = buyNowPrice;
    }

    public Item(String name, String maxPrice, String minPrice, String buyNowPrice) {

        this.name = name;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.buyNowPrice = buyNowPrice;
    }
}

package com.kre8tives.bareboneneww.Model;

/**
 * Created by Administrator on 4/22/2017.
 */

public class LiquorPrice {
    String name;
    String minPrice;
    String maxPrice;
    String buyNow;
    String id;
    String categoryId;
    String brandId;
    String stock;


    public String getBidacceptance() {
        return bidacceptance;
    }

    public void setBidacceptance(String bidacceptance) {
        this.bidacceptance = bidacceptance;
    }

    public LiquorPrice() {
    }

    public LiquorPrice(String name, String minPrice, String maxPrice,
                       String buyNow, String id, String categoryId, String brandId, String stock, String bidacceptance) {
        this.name = name;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.buyNow = buyNow;
        this.id = id;
        this.categoryId = categoryId;
        this.brandId = brandId;
        this.stock = stock;
        this.bidacceptance = bidacceptance;
    }

    String bidacceptance;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getBuyNow() {
        return buyNow;
    }

    public void setBuyNow(String buyNow) {
        this.buyNow = buyNow;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

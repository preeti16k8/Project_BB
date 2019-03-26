package com.kre8tives.bareboneneww.Model;

/**
 * Created by Administrator on 4/26/2017.
 */

public class Food {
    public Food() {
    }

    String id,foodName,foodPrice,imageUrl,stock,food_id,vegnon,ingredients;

    public String getFood_id(String foodid) {
        return food_id;
    }

    public String getVegnon() {
        return vegnon;
    }

    public void setVegnon(String vegnon) {
        this.vegnon = vegnon;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setFood_id(String food_id) {
        this.food_id = food_id;
    }

    public String getFood_id() {
        return food_id;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

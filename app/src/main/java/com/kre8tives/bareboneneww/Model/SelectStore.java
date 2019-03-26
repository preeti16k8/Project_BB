package com.kre8tives.bareboneneww.Model;

/**
 * Created by Administrator on 4/29/2017.
 */

public class SelectStore {
  String  name,imageUrl,id,customerId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public SelectStore() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SelectStore(String name, String imageUrl, String id) {
        this.name = name;
        this.imageUrl = imageUrl;

        this.id = id;
    }
}

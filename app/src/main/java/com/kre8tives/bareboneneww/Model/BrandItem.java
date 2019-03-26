package com.kre8tives.bareboneneww.Model;

/**
 * Created by user on 5/19/2017.
 */

public class BrandItem {
    public String getNewId() {
        return newId;
    }

    public BrandItem() {
    }

    public void setNewId(String newId) {

        this.newId = newId;
    }

    public String getBrandmaxprice() {
        return brandmaxprice;
    }

    public void setBrandmaxprice(String brandmaxprice) {
        this.brandmaxprice = brandmaxprice;
    }

    public String getBrandminprice() {
        return brandminprice;
    }

    public void setBrandminprice(String brandminprice) {
        this.brandminprice = brandminprice;
    }

    String Iname,IimageUrl,Iid,IcategoryId,IbrandId,newId,brandmaxprice,brandminprice;


    public String getIname() {
        return Iname;
    }

    public void setIname(String iname) {
        Iname = iname;
    }

    public String getIimageUrl() {
        return IimageUrl;
    }

    public void setIimageUrl(String iimageUrl) {
        IimageUrl = iimageUrl;
    }

    public String getIid() {
        return Iid;
    }

    public void setIid(String iid) {
        Iid = iid;
    }

    public String getIcategoryId() {
        return IcategoryId;
    }

    public void setIcategoryId(String icategoryId) {
        IcategoryId = icategoryId;
    }

    public String getIbrandId() {
        return IbrandId;
    }

    public void setIbrandId(String ibrandId) {
        IbrandId = ibrandId;
    }
}

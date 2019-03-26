package com.kre8tives.bareboneneww.Model;

/**
 * Created by Sai Gowtham on 07-04-2017.
 */
public class BiddingConfirmation {

  private String item,quantity,price,id;

//    protected BiddingConfirmation(Parcel in) {
//        item = in.readString();
//        quantity = in.readString();
//        price = in.readString();
//        id = in.readString();
//    }
//
//    public static final Creator<BiddingConfirmation> CREATOR = new Creator<BiddingConfirmation>() {
//        @Override
//        public BiddingConfirmation createFromParcel(Parcel in) {
//            return new BiddingConfirmation(in);
//        }
//
//        @Override
//        public BiddingConfirmation[] newArray(int size) {
//            return new BiddingConfirmation[size];
//        }
//    };

    public String getId() {
        return id;
    }

    public BiddingConfirmation() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public BiddingConfirmation(String id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
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

    public BiddingConfirmation(String item, String quantity, String price) {

        this.item = item;
        this.quantity = quantity;
        this.price = price;
    }



    }


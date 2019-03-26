package com.kre8tives.bareboneneww.Database;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Preeti on 23-05-2017.
 */

public class getOrderJsonarray {

    /** Retrieve the data from SQlite and Convert it into JSON format */

    DBAdapter db;
    Context context;
    JSONObject jsonObj;
    String name,quantity,price;
    int id;
    public JSONObject pollingJsonVal(){

        try{
            jsonObj= new JSONObject();
            JSONArray order_cart= new JSONArray();

                db=new DBAdapter(context);
                Cursor c=db.getAllPlayers();
                while(c.moveToNext()){
                    JSONObject jsonObjData= new JSONObject();
                     id=c.getInt(0);
                     name=c.getString(1);
                     quantity=c.getString(2);
                     price=c.getString(3);
                    jsonObjData.put("customer_id",1);
                    jsonObjData.put("item_id",id);
                    jsonObjData.put("item_name",name);
                    jsonObjData.put("item_quantity",quantity);
                    jsonObjData.put("price",price);
                    jsonObjData.put("qr_code",6);

                    order_cart.put(jsonObjData);

                }
                Log.d("nnnnname",name);
            jsonObj.put("order_cart",order_cart);
            Log.d("cart",order_cart.toString());
        }

        catch(JSONException e){
            System.out.println("Polling data Exception"+e);

        }
        System.out.println("JSON Object=="+jsonObj);
        return jsonObj;
    }

}
package com.kre8tives.bareboneneww.Retrofit;




import com.kre8tives.bareboneneww.Orders.Orders;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;

import retrofit2.http.POST;




public interface ApiInterface {
    @POST("orders_api.php")
    @FormUrlEncoded
    Call<List<Orders>> getHistoryOrders(@Field("customer_id") String customer_id,
                                        @Field("store_id") String store_id);

    @POST("cart_itemapi.php")
    @FormUrlEncoded
    Call<List<Orders>> getCurrentOrders(@Field("customer_id") String customer_id,
                                        @Field("store_id") String store_id);


}
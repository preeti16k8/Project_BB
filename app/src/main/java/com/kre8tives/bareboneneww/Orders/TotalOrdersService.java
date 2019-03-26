package com.kre8tives.bareboneneww.Orders;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.kre8tives.bareboneneww.Retrofit.ApiClient;
import com.kre8tives.bareboneneww.Retrofit.ApiInterface;
import com.kre8tives.bareboneneww.Util.Utilities;
import com.kre8tives.bareboneneww.Activity.MainActivity;
import com.kre8tives.bareboneneww.Adapter.SelectStoreAdapter;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TotalOrdersService extends IntentService {

    List<Orders> ordersList;
    int clear=0;
    String orderid;


    public TotalOrdersService() {
        super("TotalOrdersService");
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.d("orders","service started");
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        postClient();
    }


    public void postClient() {

        //Retrofit
            ApiInterface apiService;
            apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<List<Orders>> call = apiService.getCurrentOrders(MainActivity.userid, SelectStoreAdapter.storeId);
            call.enqueue(new Callback<List<Orders>>() {
                @Override
                public void onResponse(Call<List<Orders>> call, Response<List<Orders>> response) {
                    int statusCode = response.code();
                    Log.d("code", "" + statusCode);
                    ordersList = response.body();
                    if(ordersList.size()==0){

                        Utilities.removeKey("orderid",getApplicationContext());
                        Utilities.removeKey("qrcode",getApplicationContext());

                    }else{
                    for(int i=0;i<ordersList.size();i++){
                        String status=ordersList.get(i).getOrderStatus();
                        if(status.equals("bill_printed")){
                            Utilities.removeKey("orderid",getApplicationContext());
                            Utilities.removeKey("qrcode",getApplicationContext());
                            orderid=ordersList.get(i).getOrderId();
                            Log.d("helloo","hhhhhhhhh");
                            clear=1;
                            Toast.makeText(TotalOrdersService.this, "Your bill is printed", Toast.LENGTH_SHORT).show();
                        }
                    }
                   }


                }

                @Override
                public void onFailure(Call<List<Orders>> call, Throwable t) {
                    Log.i("onfailure","orderservice");
                }
            });
    }

    private void notify(String status) {

    }


    private void checkforstatus() {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("orders","service stopped");
    }
}
package com.kre8tives.bareboneneww.ExpandableOrders;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kre8tives.bareboneneww.Fragments.CategoryFragment;
import com.kre8tives.bareboneneww.Fragments.DashBoardFragment;
import com.kre8tives.bareboneneww.Fragments.FoodCategoryFragment;
import com.kre8tives.bareboneneww.Fragments.NewMixerFragment;
import com.kre8tives.bareboneneww.Orders.Item;
import com.kre8tives.bareboneneww.Orders.Orders;
import com.kre8tives.bareboneneww.R;
import com.kre8tives.bareboneneww.Retrofit.ApiClient;
import com.kre8tives.bareboneneww.Retrofit.ApiInterface;
import com.kre8tives.bareboneneww.Util.Utilities;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kre8tives.bareboneneww.Activity.MainActivity.userid;
import static com.kre8tives.bareboneneww.Adapter.SelectStoreAdapter.storeId;


public class NewCurrentOrdersFragment extends Fragment {

    public static List<Orders> historyPojoList;

    RecyclerView rv_orderhistory1;
    public static String orderid;
    ProgressDialog progressDialog;
    FloatingActionButton floatingActionButton;

    public NewCurrentOrdersFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
       // getActivity().startService(new Intent(getActivity(), TotalOrdersService.class));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_current_orders, container, false);
        rv_orderhistory1 = view.findViewById(R.id.rv_orderhistory);
        rv_orderhistory1.setLayoutManager(new LinearLayoutManager(getActivity()));
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        RecyclerView.ItemAnimator animator = rv_orderhistory1.getItemAnimator();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Current Orders");
        if (Utilities.checkConnection()) {
            new saveOrdersData().execute();
        } else {

            Toast.makeText(getActivity(), "Check your connection and try again.", Toast.LENGTH_SHORT).show();

        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Please Select!");
                alertDialog.setMessage("What you want to add?");
                alertDialog.setPositiveButton("Add Liquor", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CategoryFragment categoryFragment = new CategoryFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, categoryFragment);
                        fragmentTransaction.addToBackStack(categoryFragment.getClass().getName());
                        fragmentTransaction.commit();
                    }
                });
                alertDialog.setNegativeButton("Add Food", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FoodCategoryFragment foodCategoryFragment = new FoodCategoryFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, foodCategoryFragment);
                        fragmentTransaction.addToBackStack(foodCategoryFragment.getClass().getName());
                        fragmentTransaction.commit();
                    }
                });
                alertDialog.setNeutralButton("Add Mixer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        NewMixerFragment mixerFragment = new NewMixerFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, mixerFragment);
                        fragmentTransaction.addToBackStack(mixerFragment.getClass().getName());
                        fragmentTransaction.commit();
                    }
                });

                alertDialog.show();
            }
        });

        return view;
    }

    @Override

    public void onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.action_settings);

        item.setVisible(false);
    }

    public class saveOrdersData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity(),
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setMessage("Loading....");
            progressDialog.show();
            historyPojoList = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... params) {
            postClient();
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }
    }

    private void postClient() {

        //Retrofit
        ApiInterface apiService;
        apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<List<Orders>> call = apiService.getCurrentOrders(userid, storeId);
        call.enqueue(new Callback<List<Orders>>() {
            @Override
            public void onResponse(Call<List<Orders>> call, Response<List<Orders>> response) {
                int statusCode = response.code();
                Log.d("code", "" + statusCode);
                List<Orders> ordersList = response.body();
                historyPojoList = ordersList;
                List<ExpaOrders> orders=new ArrayList<>();

                if(ordersList.size()==0){
                    Toast.makeText(getActivity(), "No current orders", Toast.LENGTH_SHORT).show();
                }
                for(int i=0;i<ordersList.size();i++){
                    List<Item> items=new ArrayList<>();
                    items.addAll(ordersList.get(i).getItems());
                    String input=ordersList.get(i).getOrderStatus();
                    String status=input.substring(0,1).toUpperCase()+input.substring(1);
                    ExpaOrders expaOrderer =new ExpaOrders(ordersList.get(i).getOrderId(),items
                            ,ordersList.get(i).getOrderDate()
                            ,status
                            ,ordersList.get(i).getOrderAmount(), R.drawable.icon_arrow);

                    orders.add(expaOrderer);

                }
                Log.d("ordernewcurrent", String.valueOf(orders));
                OrdersAdapter adapter = new OrdersAdapter(orders);
                rv_orderhistory1.setAdapter(adapter);


                //
                if(progressDialog!=null){
                progressDialog.dismiss();}

            }

            @Override
            public void onFailure(Call<List<Orders>> call, Throwable t) {
                // Log error here since request failed
                Log.e("Retrofit", t.toString());
                progressDialog.dismiss();
                Snackbar snackbar;
                snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "You have no previous orders.", Snackbar.LENGTH_SHORT);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(getResources().getColor(R.color.red));
                TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(getResources().getColor(R.color.white));
                snackbar.show();


            }
        });


    }





    protected void exitByBackKey() {
        DashBoardFragment mixerFragment = new DashBoardFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, mixerFragment);
        fragmentTransaction.disallowAddToBackStack();
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    exitByBackKey();
                    return true;
                }

                return false;
            }
        });
    }
}

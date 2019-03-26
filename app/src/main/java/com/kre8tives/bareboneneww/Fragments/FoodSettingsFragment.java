package com.kre8tives.bareboneneww.Fragments;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.kre8tives.bareboneneww.Adapter.FoodAdapter;
import com.kre8tives.bareboneneww.Model.Food;
import com.kre8tives.bareboneneww.R;
import com.kre8tives.bareboneneww.Util.ItemOffsetDecoration;
import com.kre8tives.bareboneneww.Util.ServerConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.kre8tives.bareboneneww.Adapter.SelectStoreAdapter.storeId;
public class FoodSettingsFragment extends Fragment {
    public static String KEY_USER = "email", KEY_CATEGORY_ID = "id", FOOD_URL = "http://barebonesbar.com/bbapi/foodapi.php";
    Integer timeout = 0;
    String responseStr = "";
    List<Food> foodList;
    FoodAdapter foodAdapter;
    JSONArray foodArray;
    RecyclerView rv_food;
    String id;
    ProgressBar loader;
    public static String storeid,categoryId,categoryname;

    ImageView refresh;
    public FoodSettingsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view= inflater.inflate(R.layout.fragment_food_settings, container, false);
        foodList = new ArrayList<Food>();
        //doTheAutoRefresh();
        storeid=storeId;

        categoryId=getArguments().getString("categoryid");
        categoryname=getArguments().getString("categoryname");
        getActivity().setTitle(categoryname);
        rv_food=view.findViewById(R.id.rv_foodlist);
        rv_food.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_food.setVisibility(View.GONE);

        rv_food.setHasFixedSize(true);
        rv_food.setLayoutManager((new GridLayoutManager(getActivity(), 2)));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.spacing);
        rv_food.addItemDecoration(itemDecoration);
        refresh=(ImageView)view.findViewById(R.id.refreshFood);
        refresh.setVisibility(View.GONE);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timeout=0;
                refresh.setVisibility(View.GONE);
                loader.setVisibility(View.VISIBLE);
                new getFood().execute();
            }
        });
        loader=(ProgressBar)view.findViewById(R.id.foodLoader);
        loader.setVisibility(View.VISIBLE);
       new getFood().execute();
        return view;
    }


    public class getFood extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            foodList=new ArrayList<Food>();
        }

        @Override
        protected String doInBackground(String... params) {
            postFood();
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            loader.setVisibility(View.GONE);
            if (timeout == 1) {
                refresh.setVisibility(View.VISIBLE);
                rv_food.setVisibility(View.GONE);
            } else {
                rv_food.setVisibility(View.VISIBLE);
                refresh.setVisibility(View.GONE);

                if(responseStr==null)
                {
                    refresh.setVisibility(View.VISIBLE);
                    rv_food.setVisibility(View.GONE);
                }
                else {
                    try {
                        Log.d("FoodResponse", responseStr);
                        foodArray = new JSONArray(responseStr);
                        for (int i = 0; i < foodArray.length(); i++) {
                            Food food = new Food();
                            JSONObject jobject = foodArray.getJSONObject(i);
                            String name = jobject.getString("food_name");
                            String price=jobject.getString("price");
                            String image = jobject.getString("image");
                            String stock=jobject.getString("stock");
                            String id = jobject.getString("id");
                            String foodid = jobject.getString("_id");
                            String veg=jobject.getString("veg");
                            String ingre=jobject.getString("ingrediants");
                            food.setVegnon(veg);
                            food.setIngredients(ingre);
                            food.setFoodName(name);
                            food.setFoodPrice(price);
                            food.setStock(stock);
                            food.setImageUrl( "http://barebonesbar.com/bbapi/upload/" + image);
                            food.setId(id);
                            food.setFood_id(foodid);
                            foodList.add(food);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    foodAdapter = new FoodAdapter(foodList,getActivity());
                    rv_food.setAdapter(foodAdapter);

                }

            }
        }
    }
    private void postFood()
    {

        HttpURLConnection conn = null;
        try {

            URL url = new URL(FOOD_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("store_id", storeid)
                    .appendQueryParameter("category_id",categoryId);

            String query = builder.build().getEncodedQuery();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
            responseStr = ServerConnection.getData(conn);

            Log.d("logfoodapi",responseStr);
        }

        catch (Exception e) {
            timeout = 1;
        } finally {
            {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
    }
    /*protected void exitByBackKey() {
        FoodCategoryFragment foodCategoryFragment  = new FoodCategoryFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, foodCategoryFragment);
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
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    exitByBackKey();
                    return true;
                }
                return false;
            }
        });
    }*/


}
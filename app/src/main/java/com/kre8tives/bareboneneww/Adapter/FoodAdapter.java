package com.kre8tives.bareboneneww.Adapter;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kre8tives.bareboneneww.Fragments.OrderPreviewFragment;
import com.kre8tives.bareboneneww.Model.Food;
import com.kre8tives.bareboneneww.R;

import java.util.List;


public class FoodAdapter extends RecyclerView.Adapter <FoodAdapter.FoodViewholder>{

    private Context mContext;
    int minteger = 0;
    private List<Food> foodList;
    public static String Fname,Fprice,Fqty,Fid,FidnewId,Fnewprice;
    public FoodAdapter(List<Food> foodList, Context context){
        this.mContext = context;
        this.foodList = foodList;
    }
    @Override
    public FoodAdapter.FoodViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.food_list_item,null);
        FoodAdapter.FoodViewholder foodViewholder = new FoodAdapter.FoodViewholder(view);
        return foodViewholder;


    }

    @Override
    public void onBindViewHolder(FoodAdapter.FoodViewholder holder, int position) {
        final Food food = foodList.get(position);
        holder.foodPrice.setText(food.getFoodPrice());
        holder.foodImage.setText(food.getFoodName());
        holder.ingredients.setText(food.getIngredients());

        if(food.getVegnon().equals("0")){
            holder.veg.setVisibility(View.GONE);
            holder.nonveg.setVisibility(View.VISIBLE);

        }else {
            holder.nonveg.setVisibility(View.GONE);
            holder.veg.setVisibility(View.VISIBLE);

        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater=((Activity)mContext).getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                dialogBuilder.setView(dialogView);
                final TextView edt = (TextView) dialogView.findViewById(R.id.edit1);
                //Plus minus
                edt.setText("0");
                final Button incr=dialogView.findViewById(R.id.increase);
                final Button decr=dialogView.findViewById(R.id.decrease);
                dialogBuilder.setMessage("Please Enter Quantity");
                //
                incr.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        minteger = minteger + 1;
                        if(minteger>100){
                            edt.setText("0");
                            minteger=0;
                        }else{
                        edt.setText(""+minteger);}

                    }
                });
                decr.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        minteger = minteger - 1;
                        if(minteger<0){
                            edt.setText("0");
                            minteger=0;
                        }else{
                            edt.setText(""+minteger);}


                    }
                });
                //ends plus minus
                dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (edt.getText().toString().equals("")) {
                            Toast.makeText(mContext, "Please enter quantity", Toast.LENGTH_SHORT).show();
                        }
                        else if (Integer.parseInt(edt.getText().toString())==0){
                            Toast.makeText(mContext, "Quantity can not be zero!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            float quantity = Float.parseFloat(edt.getText().toString());
                            float price = Float.parseFloat(food.getFoodPrice());
                            float TotalPrice = quantity * price;
                            Fname = food.getFoodName();
                            Fprice = String.valueOf(TotalPrice);
                            Fnewprice= String.valueOf(price);
                            Fqty = edt.getText().toString();
                            Fid = food.getId();
                            FidnewId = food.getFood_id();
                            OrderPreviewFragment orderPreviewFragment = new OrderPreviewFragment();
                            FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame, orderPreviewFragment);
                            fragmentTransaction.addToBackStack(orderPreviewFragment.getClass().getName());
                            fragmentTransaction.commit();
                        }
                    }
                });

                AlertDialog b = dialogBuilder.create();
                b.show();
            }


        });

    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodViewholder extends RecyclerView.ViewHolder
    {
        TextView foodImage;
        TextView foodPrice,ingredients;
        ImageView veg,nonveg;
        View view;

        public FoodViewholder(View itemView) {
            super(itemView);
            view = itemView;
            foodImage = itemView.findViewById(R.id.foodList_Image);
            ingredients=itemView.findViewById(R.id.ingredients);
            foodPrice =  itemView.findViewById(R.id.foodPrice);
            veg=itemView.findViewById(R.id.veg);
            nonveg=itemView.findViewById(R.id.nonveg);
            veg.setVisibility(View.GONE);
            nonveg.setVisibility(View.GONE);



        }
    }


}

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.kre8tives.bareboneneww.Fragments.MixerFragment;
import com.kre8tives.bareboneneww.Fragments.OrderPreviewFragment;
import com.kre8tives.bareboneneww.Model.Mixture;
import com.kre8tives.bareboneneww.R;
import com.squareup.picasso.Picasso;

import java.util.List;
public class MixtureAdapter extends RecyclerView.Adapter <MixtureAdapter.MixtureViewholder>{
    private Context mContext;
    int minteger = 0;
    public static String mid,mname,mquantity,mprice,mnewid,mnewprice;
    private List<Mixture> mixtureList;
    public MixtureAdapter(List<Mixture> mixtureList, Context context){
        this.mContext = context;
        this.mixtureList = mixtureList;
    }
    @Override
    public MixtureAdapter.MixtureViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.mixer_item,null);
        MixtureAdapter.MixtureViewholder mixtureViewholder = new MixtureAdapter.MixtureViewholder(view);
        return mixtureViewholder;
    }
    @Override
    public void onBindViewHolder(final MixtureAdapter.MixtureViewholder holder, int position) {
        final Mixture mixture = mixtureList.get(position);
        holder.mixtureName.setText(mixture.getName());
        holder.mixturePrice.setText(mixture.getPrice());
        Picasso.with(mContext).load( mixture.getImageUrl()).into(holder.mixtureImage);
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
                dialogBuilder.setMessage("Please Enter Quantity");
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
                            float price = Float.parseFloat(mixture.getPrice());
                            float TotalPrice = quantity * price;
                            mid = mixture.getId();
                            mnewid = mixture.getNewid();
                            mprice = String.valueOf(TotalPrice);
                            mnewprice=String.valueOf(price);
                            mname = mixture.getName();
                            mquantity = edt.getText().toString();
                            OrderPreviewFragment orderPreviewFragment = new OrderPreviewFragment();
                            FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame, orderPreviewFragment);
                            fragmentTransaction.addToBackStack(orderPreviewFragment.getClass().getName());
                            fragmentTransaction.commit();
                        }
                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MixerFragment mixerFragment=new MixerFragment();
                        FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, mixerFragment);
                        fragmentTransaction.disallowAddToBackStack();
                        fragmentTransaction.commit();
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();
            }





        });
    }

    @Override
    public int getItemCount() {
        return mixtureList.size();
    }
    public static class MixtureViewholder extends RecyclerView.ViewHolder
    {
        ImageView mixtureImage;
        TextView mixtureName,mixturePrice;
        Spinner spinner_mxr_quantity;
        TextView tv_qnty;
        View view;
        public MixtureViewholder(View itemView) {
            super(itemView);
            view = itemView;
            mixtureImage = (ImageView) itemView.findViewById(R.id.mixtureList_Image);
            mixtureName = (TextView) itemView.findViewById(R.id.mixtureName);
            mixturePrice = (TextView) itemView.findViewById(R.id.mixturePrice);
        }
    }

}
package com.kre8tives.bareboneneww.Adapter;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.kre8tives.bareboneneww.Database.DBAdapter;
import com.kre8tives.bareboneneww.Model.BuyNowModel;
import com.kre8tives.bareboneneww.R;
import com.kre8tives.bareboneneww.Fragments.OrderPreviewFragment;

import java.util.ArrayList;

public class BuyNowAdapter extends RecyclerView.Adapter<BuyNowHolder> {
    DBAdapter db;
    int selectedPos;
    Context c;
   public static ArrayList<BuyNowModel> buyNowModelss;
    ArrayList<Float> calcListt = new ArrayList<>();
    public BuyNowAdapter(Context c, ArrayList<BuyNowModel> buyNowModels) {
        this.c = c;
        this.buyNowModelss = buyNowModels;
    }
    public BuyNowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(c).inflate(R.layout.orderpreview_item,parent,false);
        BuyNowHolder holder = new BuyNowHolder(v);
        return holder;
    }
    @Override
    public void onBindViewHolder(final BuyNowHolder holder, final int position) {
        holder.biddindConfName.setText(buyNowModelss.get(position).getName());
        holder.biddingQuantity.setText(buyNowModelss.get(position).getQuantity());
        holder.biddingConfPrice.setText(buyNowModelss.get(position).getPrice());
        holder.biddingConfSinglePrice.setText(buyNowModelss.get(position).getSingleprice());
            holder.image_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(c);
                    alertDialog.setTitle("Alert!");
                    alertDialog.setMessage("Are You Sure,You want to Delete?");
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            int id=buyNowModelss.get(position).getId();
                            deleteItem(id,position);
                        }
                    });
                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            });


    }


    @Override
    public int getItemCount() {
        return buyNowModelss.size();
    }

    public void deleteItem(int id,int selectedPos)
    {
        int idd=id;
        DBAdapter db=new DBAdapter(c);
        db.openDB();
        if(db.delete(idd)) {
            buyNowModelss.remove(selectedPos);
            ArrayList<Float> calcListt = new ArrayList<>();
            Cursor c = db.getAllPlayers();
            if (c.moveToFirst()) do {
                String price = c.getString(4);
                calcListt.add(Float.parseFloat(price));
            } while (c.moveToNext()) ;
            float sum = 0;
            for (int i = 0; i < calcListt.size(); i++) {
                OrderPreviewFragment.grandtotal.setText(String.valueOf(sum));
                sum = calcListt.get(i) + sum;
                Log.d("summm",String.valueOf(sum));
            }
            OrderPreviewFragment.grandtotal.setText(String.valueOf(sum));
            calcListt.clear();
        }
        else {
            Toast.makeText(c,"Unable To Delete",Toast.LENGTH_SHORT).show();
        }
        db.closeDB();
        this.notifyItemRemoved(selectedPos);
        this.notifyDataSetChanged();
    }
}



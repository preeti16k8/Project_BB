package com.kre8tives.bareboneneww.Adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.kre8tives.bareboneneww.Model.News;
import com.kre8tives.bareboneneww.R;
import java.util.List;
public class NewsAdapter extends RecyclerView.Adapter <NewsAdapter.CurrentOrderViewholder>{
    private Context mContext;
    private List<News> newsList;
    public NewsAdapter(List<News> newsList, Context context){
        this.mContext = context;
        this.newsList = newsList;
    }
    @Override
    public NewsAdapter.CurrentOrderViewholder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.news_item,null);
        NewsAdapter.CurrentOrderViewholder currentOrderViewholder = new NewsAdapter.CurrentOrderViewholder(view);
        return currentOrderViewholder;
    }
    @Override
    public void onBindViewHolder(NewsAdapter.CurrentOrderViewholder holder, int position) {
        final News orderModel = newsList.get(position);
        holder.title.setText(orderModel.getTitle());
        holder.news.setText(orderModel.getNews());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class CurrentOrderViewholder extends RecyclerView.ViewHolder
    {
        public TextView news,title;
        View view;
        public CurrentOrderViewholder(View itemView) {
            super(itemView);
            view = itemView;
            news = (TextView) itemView.findViewById(R.id.tv_news);
            title = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }
}
package com.ransankul.priceaction.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ransankul.priceaction.R;
import com.ransankul.priceaction.databinding.ItemRelatedNewsBinding;
import com.ransankul.priceaction.model.RelatedNews;

import java.util.ArrayList;

public class RelatedNewsAdapter extends RecyclerView.Adapter<RelatedNewsAdapter.RelatedNewsAdapterViewHolder>{

    private Context context;
    private ArrayList<RelatedNews> relatedNewsArrayList;

    public RelatedNewsAdapter(Context context, ArrayList<RelatedNews> relatedNewsArrayList) {
        this.context = context;
        this.relatedNewsArrayList = relatedNewsArrayList;
    }

    @NonNull
    @Override
    public RelatedNewsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RelatedNewsAdapter.RelatedNewsAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.item_related_news, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedNewsAdapterViewHolder holder, int position) {

        RelatedNews relatedNews = relatedNewsArrayList.get(position);
        holder.binding.timeTV.setText(relatedNews.getNewstime());
        holder.binding.headingTV.setText(relatedNews.getNewsHeading());
        holder.binding.subHeadingTV.setText(relatedNews.getNewsSubheading());
    }

    @Override
    public int getItemCount() {
        return relatedNewsArrayList.size();
    }

    public static class RelatedNewsAdapterViewHolder extends RecyclerView.ViewHolder{

        ItemRelatedNewsBinding binding;
        public RelatedNewsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemRelatedNewsBinding.bind(itemView);
        }
    }
}

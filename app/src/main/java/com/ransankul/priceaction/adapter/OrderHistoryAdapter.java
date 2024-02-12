package com.ransankul.priceaction.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ransankul.priceaction.R;
import com.ransankul.priceaction.databinding.ApiShownLayoutBinding;
import com.ransankul.priceaction.databinding.ItemOrderHistoryBinding;
import com.ransankul.priceaction.model.OrderHistory;
import com.ransankul.priceaction.model.PlatformApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>{

    Context context;
    ArrayList<OrderHistory> orderHistorylist;

    public OrderHistoryAdapter(Context context, ArrayList<OrderHistory> orderHistorylist) {
        this.context = context;
        this.orderHistorylist = orderHistorylist;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderHistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        OrderHistory orderHistory = orderHistorylist.get(position);

        holder.binding.tvstockSymbol.setText(orderHistory.getStockSymbol());
        holder.binding.tvOrderPlatform.setText(orderHistory.getPlatform());
        holder.binding.tvOrderQuantity.setText(orderHistory.getQuantity()+"");
        holder.binding.tvOrderStatus.setText(orderHistory.getOrderStatus());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        String formattedDate = dateFormat.format(orderHistory.getOrderTime());
        String formattedTime = timeFormat.format(orderHistory.getOrderTime());

        holder.binding.tvOrderTime.setText(formattedDate+" "+formattedTime);

        if(orderHistory.getOrderType().equals("BUY")){
            holder.binding.llbuy.setVisibility(View.VISIBLE);
            holder.binding.llsell.setVisibility(View.GONE);
        }else{
            holder.binding.llsell.setVisibility(View.VISIBLE);
            holder.binding.llbuy.setVisibility(View.GONE);
        }

        if(orderHistory.getOrderType().equalsIgnoreCase("buy")){
            holder.binding.llbuy.setVisibility(View.VISIBLE);
            holder.binding.llsell.setVisibility(View.GONE);
        }else{
            holder.binding.llbuy.setVisibility(View.GONE);
            holder.binding.llsell.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return orderHistorylist.size();
    }


    public static class OrderHistoryViewHolder extends RecyclerView.ViewHolder {

        ItemOrderHistoryBinding binding;
        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemOrderHistoryBinding.bind(itemView);
        }
    }
}

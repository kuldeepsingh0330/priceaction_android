package com.ransankul.priceaction.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ransankul.priceaction.R;
import com.ransankul.priceaction.activity.ChartWebviewActivity;
import com.ransankul.priceaction.databinding.ItemWatchlistBinding;
import com.ransankul.priceaction.model.RelatedNews;
import com.ransankul.priceaction.model.Watchlist;
import com.ransankul.priceaction.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.WatchlistViewholder> {

    private ArrayList<Watchlist> watchlistList;
    private Context context;

    public WatchlistAdapter(ArrayList<Watchlist> watchlistList, Context context) {
        this.watchlistList = watchlistList;
        this.context = context;
    }

    @NonNull
    @Override
    public WatchlistViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WatchlistViewholder(LayoutInflater.from(context).inflate(R.layout.item_watchlist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WatchlistViewholder holder, int position) {
        Watchlist watchlist = watchlistList.get(position);
        holder.binding.stocknameTV.setText(watchlist.getStockName());
        holder.binding.stocktypeIV.setText(watchlist.getStockExchangeName());
        holder.binding.stockpriceTV.setText(watchlist.getCurrentPrice());

        if(Double.parseDouble(watchlist.getChangeInPrice())>0){
            holder.binding.stockpriceTV.setTextColor(ContextCompat.getColor(context,R.color.green));
            holder.binding.stockincrementIV.setText("+"+watchlist.getChangeInPrice());
        }else if(Double.parseDouble(watchlist.getChangeInPrice())<0){
            holder.binding.stockpriceTV.setTextColor(ContextCompat.getColor(context,R.color.red));
            holder.binding.stockincrementIV.setText("-"+watchlist.getChangeInPrice());
        }else{
            holder.binding.stockpriceTV.setTextColor(ContextCompat.getColor(context,R.color.black));
            holder.binding.stockincrementIV.setText(watchlist.getChangeInPrice());
        }

        holder.itemView.setOnClickListener((v) -> {
            Intent intent = new Intent(context, ChartWebviewActivity.class);
            intent.putExtra("symbol",watchlist.getStockName().replaceAll(" ","_"));
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener((v) -> {
            String tradingSymbol = watchlist.getStockName().replaceAll(" ","_");
            showDeleteAlertDialog(watchlist);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return watchlistList.size();
    }

    public static class WatchlistViewholder extends RecyclerView.ViewHolder {


        ItemWatchlistBinding binding;
        public WatchlistViewholder(@NonNull View itemView) {
            super(itemView);
            binding = ItemWatchlistBinding.bind(itemView);
        }
    }

    public void updateData(ArrayList<Watchlist> watchlistList){
        this.watchlistList = watchlistList;
        notifyDataSetChanged();
    }

    private void showDeleteAlertDialog(Watchlist watchlist) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Delete Confirmation")
                .setMessage("Are you sure you want to delete?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteItem(watchlist);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void deleteItem(Watchlist watchlist) {
        String tokenValue = Constants.getTokenValue(context);
        String url = Constants.REMOVE_WATCHLIST_URL+watchlist.getInstrumentKey();

        StringRequest request = new StringRequest(Request.Method.DELETE,url,
                response -> {
                    if(response.contains("succesfully")){
                        watchlistList.remove(watchlist);
                        notifyDataSetChanged();
                    }else {
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    }
                },error -> {
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + tokenValue);
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

}

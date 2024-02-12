package com.ransankul.priceaction.adapter;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ransankul.priceaction.R;
import com.ransankul.priceaction.databinding.ApiShownLayoutBinding;
import com.ransankul.priceaction.databinding.ItemSearchInstrumentkeyBinding;
import com.ransankul.priceaction.model.InstrumentKey;
import com.ransankul.priceaction.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchInstrumentKeyAdapter extends RecyclerView.Adapter<SearchInstrumentKeyAdapter.InstrumentKeyViewHolder> {

    private Context context;
    private List<InstrumentKey> instrumentKeyList;
    private String platform;

    public SearchInstrumentKeyAdapter(Context context, List<InstrumentKey> instrumentKeyList,String platform) {
        this.context = context;
        this.instrumentKeyList = instrumentKeyList;
        this.platform = platform;
    }

    @NonNull
    @Override
    public InstrumentKeyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchInstrumentKeyAdapter.InstrumentKeyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_search_instrumentkey, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InstrumentKeyViewHolder holder, int position) {
        InstrumentKey instrumentKey = instrumentKeyList.get(position);

        holder.binding.tvtradingsymbol.setText(instrumentKey.getTradingSymbol().isEmpty()?"":instrumentKey.getTradingSymbol());
        holder.binding.tvstockinstrumentkey.setText(instrumentKey.getInstrumentKey());
        holder.binding.tvstockname.setText(instrumentKey.getStockName());


        holder.itemView.setOnClickListener(v -> {
            if(!platform.isEmpty()){
                ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("stockinstrumentkey", holder.binding.tvstockinstrumentkey.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Instrument Key copied to Clipboard", Toast.LENGTH_SHORT).show();
            }else{
                addToWatchlist(instrumentKey);
            }
        });
    }

    @Override
    public int getItemCount() {
        return instrumentKeyList.size();
    }

    public class InstrumentKeyViewHolder extends RecyclerView.ViewHolder {

        ItemSearchInstrumentkeyBinding binding;
        public InstrumentKeyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemSearchInstrumentkeyBinding.bind(itemView);
        }
    }

    private void addToWatchlist(InstrumentKey instrumentKey) {
        String tokenValue = Constants.getTokenValue(context);
        String url = Constants.ADD_WATCHLIST_URL+instrumentKey.getInstrumentKey();

        StringRequest request = new StringRequest(Request.Method.POST,url,
                response -> {
                    if(response.contains("succesfully")){
                        instrumentKeyList.remove(instrumentKey);
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

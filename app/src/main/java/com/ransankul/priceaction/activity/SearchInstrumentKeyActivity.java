package com.ransankul.priceaction.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.ransankul.priceaction.adapter.SearchInstrumentKeyAdapter;
import com.ransankul.priceaction.adapter.ShowApiAdapter;
import com.ransankul.priceaction.databinding.ActivitySearchInstrumentKeyBinding;
import com.ransankul.priceaction.model.InstrumentKey;
import com.ransankul.priceaction.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SearchInstrumentKeyActivity extends AppCompatActivity {

    private ActivitySearchInstrumentKeyBinding binding;

    List<InstrumentKey> instrumentKeyslist;
    SearchInstrumentKeyAdapter adapter;
    private int pageNumber;
    private String platform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchInstrumentKeyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pageNumber = 0;
        platform = getIntent().getStringExtra("platform");


        instrumentKeyslist = new ArrayList<>();
        adapter = new SearchInstrumentKeyAdapter(this,instrumentKeyslist,platform);
        binding.instrumentkeysearchRV.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.instrumentkeysearchRV.setLayoutManager(layoutManager);



        binding.etsearchapi.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                pageNumber = 0;
                loadInstrumentKeys();
                instrumentKeyslist.clear();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        binding.instrumentkeysearchRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    pageNumber++;
                    loadInstrumentKeys();
                }
            }
        });

    }

    private void loadInstrumentKeys(){
        String tokenValue = Constants.getTokenValue(this);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url;
        url = Constants.SEARCH_INSTRUMENT_KEY+pageNumber+ "?platform=UPSTOX" + "&query=" + binding.etsearchapi.getQuery().toString();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for(int i = 0;i<response.length();i++){
                            JSONObject object = response.getJSONObject(i);
                            InstrumentKey ik = new InstrumentKey();
                            ik.setId(object.getLong("id"));
                            ik.setStockName(object.getString("stockName"));
                            ik.setTradingSymbol(object.getString("tradingSymbol"));
                            ik.setInstrumentKey(object.getString("instrumentKey"));
                            instrumentKeyslist.add(ik);

                         }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                    }
                },
                error -> {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + tokenValue);
                return headers;
            }
        };

        queue.add(jsonArrayRequest);

    }
}
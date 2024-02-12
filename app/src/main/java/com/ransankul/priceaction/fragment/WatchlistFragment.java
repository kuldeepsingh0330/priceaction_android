package com.ransankul.priceaction.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ransankul.priceaction.R;
import com.ransankul.priceaction.activity.SearchInstrumentKeyActivity;
import com.ransankul.priceaction.adapter.RelatedNewsAdapter;
import com.ransankul.priceaction.adapter.WatchlistAdapter;
import com.ransankul.priceaction.databinding.FragmentWatchlistBinding;
import com.ransankul.priceaction.model.RelatedNews;
import com.ransankul.priceaction.model.Watchlist;
import com.ransankul.priceaction.upstoxutil.MarketDataFeed;
import com.ransankul.priceaction.util.Constants;
import com.upstox.ApiClient;
import com.upstox.Configuration;
import com.upstox.auth.OAuth;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;

public class WatchlistFragment extends Fragment {

    private FragmentWatchlistBinding binding;
    private String platformjwtToken;
    private List<String> watchlistStock;
    private ArrayList<String> tradingSymbolList;
    private WatchlistAdapter adapter;

    private ArrayList<Watchlist> watchlistArrayList;
    private ArrayList<RelatedNews> relatedNewsArrayList;
    private RelatedNewsAdapter relatedNewsAdapter;

    private int relatedNewspagenumber = 0;
    private boolean isCallingApi = true;

    public WatchlistFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentWatchlistBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        binding.progressBar2.setVisibility(View.VISIBLE);
        binding.recyclerViewWatchlist.setVisibility(View.GONE);
        binding.lltab.setVisibility(View.GONE);

        binding.relatednewsRV.setVisibility(View.GONE);
        binding.progressBar3.setVisibility(View.VISIBLE);

        watchlistStock = new ArrayList<>();
        tradingSymbolList = new ArrayList<>();
        watchlistArrayList = new ArrayList<>();
        relatedNewsArrayList = new ArrayList<>();

        loadplatformJwtTokenAndWatchlist();

        adapter = new WatchlistAdapter(watchlistArrayList,getContext());
        binding.recyclerViewWatchlist.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerViewWatchlist.setLayoutManager(layoutManager);

        relatedNewsAdapter = new RelatedNewsAdapter(getContext(),relatedNewsArrayList);
        binding.relatednewsRV.setAdapter(relatedNewsAdapter);
        LinearLayoutManager layoutManagerrelatednews = new LinearLayoutManager(getContext());
        binding.relatednewsRV.setLayoutManager(layoutManagerrelatednews);

        binding.relatednewsTV.setOnClickListener((v)->{
            relatedNewspagenumber = 0;
            isCallingApi = true;
            binding.stockPridictionRV.setVisibility(View.GONE);
            binding.progressBar3.setVisibility(View.VISIBLE);

            binding.stockPridictionTV.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            binding.relatednewsTV.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.grey));

            binding.stockPridictionRV.setClickable(true);
            binding.relatednewsRV.setClickable(false);

            relatedNewsArrayList.clear();
            loadAllRelatedNewslist();
        });

        binding.stockPridictionTV.setOnClickListener((v)->{
            binding.relatednewsRV.setVisibility(View.GONE);
            binding.progressBar3.setVisibility(View.VISIBLE);

            binding.relatednewsTV.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            binding.stockPridictionTV.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.grey));

            binding.stockPridictionRV.setClickable(false);
            binding.relatednewsRV.setClickable(true);

        });

        binding.relatednewsRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManagerrelatednews.getChildCount();
                int totalItemCount = layoutManagerrelatednews.getItemCount();
                int firstVisibleItemPosition = layoutManagerrelatednews.findFirstVisibleItemPosition();

                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {

                    loadAllRelatedNewslist();
                }
            }
        });

        binding.floatingActionBtn.setOnClickListener((v) -> {
            Intent intent = new Intent(getContext(), SearchInstrumentKeyActivity.class);
            intent.putExtra("platform","");
            intent.putExtra("tradingSymbolList",tradingSymbolList);
            getContext().startActivity(intent);
        });

        return view;
    }

    private void loadAllRelatedNewslist() {

        String tokenValue = Constants.getTokenValue(requireContext());
        String url = Constants.LOAD_ALL_RELATED_NEWS_URL+relatedNewspagenumber;
        
        StringRequest request = new StringRequest(Request.Method.GET,url,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        if(array.length() < 20) isCallingApi = false;
                        for(int i = 0;i<array.length();i++){
                            RelatedNews relatedNews = new RelatedNews();
                            relatedNews.setNewstime(array.getJSONObject(i).getString("newstime"));
                            relatedNews.setNewsHeading(array.getJSONObject(i).getString("newsHeading"));
                            relatedNews.setNewsSubheading(array.getJSONObject(i).getString("newsSubheading"));
                            relatedNewsArrayList.add(relatedNews);
                        }
                        relatedNewsAdapter.notifyDataSetChanged();
                        binding.relatednewsRV.setVisibility(View.VISIBLE);
                        binding.progressBar3.setVisibility(View.GONE);
                        relatedNewspagenumber++;
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        if(isCallingApi) requestQueue.add(request);

    }

    private void loadplatformJwtTokenAndWatchlist() {

        String tokenValue = Constants.getTokenValue(requireContext());
        String url = Constants.USERAPIMAPPING_LOADPLATFORMJWTTOKENAPI_URL;

        StringRequest request = new StringRequest(Request.Method.POST,url,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        platformjwtToken = array.getString(0);
                        JSONArray instrumentKeyArray = array.getJSONArray(1);
                        JSONArray tradingSymbolArray = array.getJSONArray(2);

                        for(int i = 0;i<instrumentKeyArray.length();i++){
                            watchlistStock.add(instrumentKeyArray.getString(i));
                            tradingSymbolList.add(tradingSymbolArray.getString(i));
                        }

                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                    getAuthorizedWebSocketUri();

                },error -> {
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + tokenValue);
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);

    }


    private void getAuthorizedWebSocketUri() {

        String url = Constants.UPSTOX_MARKET_FEED_WEBSOCKET_URL;

        StringRequest request = new StringRequest(Request.Method.GET,url,
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        WebSocketClient client = createWebSocketClient(URI.create(object.getJSONObject("data")
                                .getString("authorized_redirect_uri")));

                        client.connect();
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                },error -> {

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + platformjwtToken);
                headers.put("Api-Version", "2.0");
                headers.put("Accept", "application/json");
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

    private WebSocketClient createWebSocketClient(URI serverUri) {
        return new WebSocketClient(serverUri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                sendSubscriptionRequest(this);
                loadAllRelatedNewslist();
            }

            @Override
            public void onMessage(String message) {

            }

            @Override
            public void onMessage(ByteBuffer bytes) {

                String response = handleBinaryMessage(bytes);

                if(response != null && !response.isEmpty()){
                    try {
                        JSONObject object = new JSONObject(response).getJSONObject("feeds");
                        int i = 0;
                        for(String s : watchlistStock){
                            Watchlist watchlist = new Watchlist();
                            if(object.has(s)) {
                                JSONObject data = object.getJSONObject(s).getJSONObject("ff");

                                watchlist.setId(s);
                                if (s.contains("NSE_INDEX")) data = data.getJSONObject("indexFF");
                                else data = data.getJSONObject("marketFF");

                                double currentPrice = 0;

                                if (data.getJSONObject("ltpc").toString().contains("ltp"))
                                    currentPrice = data.getJSONObject("ltpc").getDouble("ltp");
                                Log.d("hhhhhhhhhhhhhhh",String.valueOf(currentPrice));
                                watchlist.setCurrentPrice(String.format("%.2f", currentPrice));
                                data = data.getJSONObject("marketOHLC")
                                        .getJSONArray("ohlc").getJSONObject(0);
                                if (data.toString().contains("open")) {
                                    double changeInPrice = currentPrice - data.getDouble("open");
                                    watchlist.setChangeInPrice(String.format("%.4f", changeInPrice));
                                    Log.d("hhhhhhhhhhhhhhh",String.valueOf(currentPrice));
                                } else watchlist.setChangeInPrice("0");

                                watchlist.setInstrumentKey(s);
                                watchlist.setStockName(tradingSymbolList.get(i).equals("") ? s.substring(s.indexOf("|") + 1) : tradingSymbolList.get(i));
                                watchlist.setStockExchangeName(s.substring(0, s.indexOf("|")).replace("_", " "));
                                if(watchlistArrayList.size()<watchlistStock.size())watchlistArrayList.add(watchlist);
                                else watchlistArrayList.set(watchlistArrayList.indexOf(watchlist),watchlist);
                                i++;

                                Handler mainHandler = new Handler(Looper.getMainLooper());
                                mainHandler.post(() -> updateUI());
                            }

                        }

                    } catch (JSONException e) {
                        Log.d("hhhhhhhhhhhhhhh",e.toString());
                    }
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println("Connection closed by " + (remote ? "remote peer" : "us") + ". Info: " + reason);
            }

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
            }
        };
    }

    private void updateUI() {
        binding.progressBar2.setVisibility(View.GONE);
        binding.recyclerViewWatchlist.setVisibility(View.VISIBLE);
        binding.lltab.setVisibility(View.VISIBLE);
        adapter.updateData(watchlistArrayList);
    }

    private void sendSubscriptionRequest(WebSocketClient client) {
        JsonObject requestObject = constructSubscriptionRequest();
        byte[] binaryData = requestObject.toString()
                .getBytes(StandardCharsets.UTF_8);

        client.send(binaryData);
    }

    private JsonObject constructSubscriptionRequest() {
        JsonObject dataObject = new JsonObject();
        dataObject.addProperty("mode", "full");


        JsonArray instrumentKeys = new Gson().toJsonTree(watchlistStock)
                .getAsJsonArray();
        dataObject.add("instrumentKeys", instrumentKeys);

        JsonObject mainObject = new JsonObject();
        mainObject.addProperty("guid", "someguid");
        mainObject.addProperty("method", "sub");
        mainObject.add("data", dataObject);

        return mainObject;
    }

    private String handleBinaryMessage(ByteBuffer bytes) {

        try {
            MarketDataFeed.FeedResponse feedResponse = MarketDataFeed.FeedResponse.parseFrom(bytes.array());
            return JsonFormat.printer().print(feedResponse);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}
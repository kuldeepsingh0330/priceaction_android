package com.ransankul.priceaction.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.ransankul.priceaction.adapter.OrderHistoryAdapter;
import com.ransankul.priceaction.databinding.FragmentApiBinding;
import com.ransankul.priceaction.databinding.FragmentDashboardBinding;
import com.ransankul.priceaction.model.OrderHistory;
import com.ransankul.priceaction.model.PlatformApi;
import com.ransankul.priceaction.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private ArrayList<OrderHistory> orderHistoryList;
    private OrderHistoryAdapter adapter;
    private int pageNumber;
    public DashboardFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater,container,false);
        View v = binding.getRoot();

        binding.orderHistoryRv.setVisibility(View.GONE);
        binding.progressBar2.setVisibility(View.VISIBLE);

        orderHistoryList = new ArrayList<>();

        loadOrderHistory();

        adapter = new OrderHistoryAdapter(getContext(),orderHistoryList);
        binding.orderHistoryRv.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.orderHistoryRv.setLayoutManager(layoutManager);

        binding.orderHistoryRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    pageNumber++;
                    loadOrderHistory();
                }
            }
        });

        binding.copyWebhokkUrlBtn.setOnClickListener(view -> {
            String url = Constants.WEB_HOOK_URL+Constants.getTokenValue(requireContext());
            ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Sequence", url);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getContext(), "Copied", Toast.LENGTH_SHORT).show();
        });

        return v;
    }

    private void loadOrderHistory() {
        String tokenValue = Constants.getTokenValue(requireContext());
        String url = Constants.LOAD_ORDER_HISTORY_URL+pageNumber;
        StringRequest request = new StringRequest(Request.Method.POST,url,
                response -> {
                    try {
                        JSONArray responseObj = new JSONArray(response);

                        for (int i = 0; i < responseObj.length(); i++) {
                            JSONObject jsonObject = (JSONObject) responseObj.get(i);
                            OrderHistory orderHistory = new OrderHistory();
                            orderHistory.setId(jsonObject.getLong("id"));
                            orderHistory.setStockSymbol(jsonObject.getString("stockSymbol"));
                            orderHistory.setOrderStatus(jsonObject.getString("orderStatus"));
                            orderHistory.setOrderType(jsonObject.getString("orderType"));
                            orderHistory.setPlatform(jsonObject.getString("platform"));
                            orderHistory.setQuantity(jsonObject.getInt("quantity"));


                            String dateString = jsonObject.getString("orderTime");
                            if(!dateString.equals("null")){
                                orderHistory.setOrderTime(new Date(Long.parseLong(dateString)));
                            }else{
                                orderHistory.setOrderTime(null);
                            }
                            orderHistoryList.add(orderHistory);
                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    adapter.notifyDataSetChanged();
                    binding.orderHistoryRv.setVisibility(View.VISIBLE);
                    binding.progressBar2.setVisibility(View.GONE);
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

}
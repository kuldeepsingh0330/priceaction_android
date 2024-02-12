package com.ransankul.priceaction.fragment;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ransankul.priceaction.R;
import com.ransankul.priceaction.activity.LoginActivity;
import com.ransankul.priceaction.adapter.ShowApiAdapter;
import com.ransankul.priceaction.databinding.FragmentAddapiBottomSheetBinding;
import com.ransankul.priceaction.databinding.FragmentApiBinding;
import com.ransankul.priceaction.model.Platform;
import com.ransankul.priceaction.model.PlatformApi;
import com.ransankul.priceaction.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ApiFragment extends Fragment {


    private FragmentApiBinding binding;

    ShowApiAdapter showApiAdapter;
    private ArrayList<PlatformApi> platformApiArrayList;

    private Dialog progressDialog;
    public ApiFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentApiBinding.inflate(inflater,container,false);
        View view = binding.getRoot();


        progressDialog = new Dialog(getContext());
        progressDialog.setContentView(R.layout.please_wait);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(), android.R.color.transparent)));

        platformApiArrayList = new ArrayList<>();

        showApiAdapter = new ShowApiAdapter(getContext(),platformApiArrayList,progressDialog);
        binding.showapiRV.setAdapter(showApiAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.showapiRV.setLayoutManager(layoutManager);

        binding.addapibtn.setOnClickListener(v -> {
            openBottomSheet();
        });

        binding.etsearchapi.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadAllAPI(newText);
                return false;
            }
        });

        return view;
    }

    public void openBottomSheet() {
        AddapiBottomSheetFragment bottomSheetFragment = new AddapiBottomSheetFragment();
        bottomSheetFragment.show(getChildFragmentManager(), "ADDAPI");
    }

    private void loadAllAPI(String query) {
        String tokenValue = Constants.getTokenValue(requireContext());
        String url = "";
        if(query.equals(""))  url = Constants.USERAPIMAPPING_LOADALLAPI_URL;
        else url = Constants.USERAPIMAPPING_SEARCHAPI_URL+"?q="+query;

        binding.showapiRV.setVisibility(View.GONE);
        binding.apifragmentprogressbar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST,url,
                response -> {
                    platformApiArrayList.clear();
                    try {
                        if(response.equals("Nothing Found")){
                            binding.showapiRV.setVisibility(View.VISIBLE);
                            binding.apifragmentprogressbar.setVisibility(View.GONE);
                            return;
                        }
                        JSONArray responseObj = new JSONArray(response);

                        for (int i = 0; i < responseObj.length(); i++) {
                            JSONObject jsonObject = (JSONObject) responseObj.get(i);
                            PlatformApi platformApi = new PlatformApi();
                            platformApi.setId(jsonObject.getLong("id"));
                            platformApi.setPlatform(jsonObject.getString("platform"));
                            platformApi.setOnOrOff(jsonObject.getBoolean("onOrOff"));
                            platformApi.setConnectedOrNot(jsonObject.getBoolean("connectedOrNot"));
                            platformApi.setApikey(jsonObject.getString("apikey"));
                            platformApi.setApisecret(jsonObject.getString("apisecret"));
                            platformApi.setApiname(jsonObject.getString("apiname"));
                            platformApi.setredirecturl(jsonObject.getString("redirecturl"));

                            String dateString = jsonObject.getString("lastTokenTime");
                            if(!dateString.equals("null")){
                                platformApi.setLastTokenTime(new Date(Long.parseLong(dateString)));
                            }else{
                                platformApi.setLastTokenTime(null);
                            }

                            platformApiArrayList.add(platformApi);
                        }


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    binding.showapiRV.setVisibility(View.VISIBLE);
                    binding.apifragmentprogressbar.setVisibility(View.GONE);
                    this.showApiAdapter.notifyDataSetChanged();

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

    @Override
    public void onResume() {
        super.onResume();
        platformApiArrayList.clear();
        binding.showapiRV.setVisibility(View.GONE);
        binding.apifragmentprogressbar.setVisibility(View.VISIBLE);
        loadAllAPI("");
        this.showApiAdapter.notifyDataSetChanged();
    }
}
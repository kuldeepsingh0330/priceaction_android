package com.ransankul.priceaction.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avatarfirst.avatargenlib.AvatarGenerator;
import com.ransankul.priceaction.databinding.FragmentUserBinding;
import com.ransankul.priceaction.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserFragment extends Fragment {

    private FragmentUserBinding binding;
    private List<String> platformlist;
    private int count;


    public UserFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        count = 0;

        platformlist = new ArrayList<>();

        loadUserDetails();
        loadAllPlatform();

        binding.platformDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                binding.otherProfileLayout.setVisibility(View.GONE);
                String platform = (String) adapterView.getSelectedItem();
                long dropdownid = adapterView.getSelectedItemId();

                if(dropdownid != 0){
                    loadOtherPlatformPortfolio(platform);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





        return view;
    }

    private void loadOtherPlatformPortfolio(String platform) {

        String tokenValue = Constants.getTokenValue(requireContext());
        String url = Constants.LOAD_USERDETAILS_URL+"/"+platform;

        StringRequest request = new StringRequest(Request.Method.POST,url,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject platformprofile = (new JSONObject(jsonArray.getString(0))).getJSONObject("data");
                        JSONObject platformequity = (new JSONObject(jsonArray.getString(1))).getJSONObject("data").getJSONObject("commodity");
                        JSONObject platformcommodity = (new JSONObject(jsonArray.getString(1))).getJSONObject("data").getJSONObject("equity");

                        binding.TVuserid.setText(platformprofile.getString("user_id"));
                        binding.TVusername.setText(platformprofile.getString("user_name"));
                        binding.TVemailid.setText(platformprofile.getString("email"));
                        binding.TVplatform.setText(platformprofile.getString("broker"));

                        binding.TVavailablemargin.setText(platformequity.getString("available_margin"));
                        binding.TVusedmargin.setText(platformequity.getString("used_margin"));
                        binding.TVpayinamount.setText(platformequity.getString("payin_amount"));

                        binding.TVavailablemarginComm.setText(platformcommodity.getString("available_margin"));
                        binding.TVusedmarginComm.setText(platformcommodity.getString("used_margin"));
                        binding.TVpayinamountComm.setText(platformcommodity.getString("payin_amount"));


                    } catch (JSONException e) {
                    }

                    binding.otherProfileLayout.setVisibility(View.VISIBLE);
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

    private void loadUserDetails() {
        String tokenValue = Constants.getTokenValue(requireContext());
        String url = Constants.LOAD_USERDETAILS_URL;

        StringRequest request = new StringRequest(Request.Method.POST,url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String username = jsonObject.getString("username");
                        binding.TVusername.setText(username);
                        binding.userphonenumberTV.setText("+91 "+jsonObject.getString("phoneNumber"));
                        binding.useremailTV.setText(jsonObject.getString("emailId"));
                        binding.addedapiTV.setText(jsonObject.getString("addedApi"));
                        binding.activeapiTV.setText(jsonObject.getString("activeApi"));

                        binding.userIV.setImageDrawable(new AvatarGenerator.AvatarBuilder(getContext())
                                .setLabel(username)
                                .setAvatarSize(120)
                                .setTextSize(30)
                                .toSquare()
                                .toCircle()
                                .build());

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    count++;
                    makeVisible();
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

    private void loadAllPlatform() {
        String tokenValue = Constants.getTokenValue(requireContext());
        String url = Constants.USERAPIMAPPING_LOADALLPLATFORM_URL;
        platformlist.clear();

        StringRequest request = new StringRequest(Request.Method.GET,url,
                response -> {
                    try {
                        JSONArray responseObj = new JSONArray(response);

                        for (int i = 0; i < responseObj.length(); i++) {
                            JSONObject jsonObject = (JSONObject) responseObj.get(i);
                            platformlist.add(jsonObject.getString("platform"));
                        }
                        setPlatformDropdown();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    count++;
                    makeVisible();
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

    private void makeVisible() {
        if(count == 2){
            binding.scrollview.setVisibility(View.VISIBLE);
            binding.progressbar.setVisibility(View.GONE);
        }
    }

    private void setPlatformDropdown() {
        makeVisible();
        platformlist.add(0,"Select platform");
        if(getContext() != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, platformlist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.platformDropdown.setAdapter(adapter);
        }
    }

}
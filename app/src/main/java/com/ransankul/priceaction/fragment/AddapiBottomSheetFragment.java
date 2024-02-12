package com.ransankul.priceaction.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ransankul.priceaction.R;
import com.ransankul.priceaction.activity.LoginActivity;
import com.ransankul.priceaction.activity.MainActivity;
import com.ransankul.priceaction.databinding.FragmentAddapiBottomSheetBinding;
import com.ransankul.priceaction.model.Platform;
import com.ransankul.priceaction.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddapiBottomSheetFragment extends BottomSheetDialogFragment {

    private FragmentAddapiBottomSheetBinding binding;
    private List<String> platformlist;
    private Dialog progressDialog;
    private String platform;
    private long dropdownid = 0L;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddapiBottomSheetBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.ll2.setVisibility(View.GONE);
        binding.progressbar.setVisibility(View.VISIBLE);

        platformlist = new ArrayList<>();
        loadAllPlatform();


        progressDialog = new Dialog(getContext());
        progressDialog.setContentView(R.layout.please_wait);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(requireContext(), android.R.color.transparent)));

        binding.etApiName.requestFocus();
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        binding.saveApiCredBtn.setOnClickListener(v -> {
            String apikey = binding.etApiKey.getText().toString().trim();
            String apisecret = binding.etApiSecret.getText().toString().trim();
            String apiname = binding.etApiName.getText().toString().trim();
            String redirectUrl = binding.etredirectUrl.getText().toString().trim();

            if(validateInput(dropdownid,apikey,apisecret,apiname,redirectUrl))
                saveApi(apikey,apisecret,apiname,redirectUrl);
            else Toast.makeText(requireContext(), "Enter all parameters", Toast.LENGTH_SHORT).show();
        });

        binding.closeBtn.setOnClickListener(v -> {
            dismiss();
        });

        binding.platformDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                platform = (String) adapterView.getSelectedItem();
                dropdownid = adapterView.getSelectedItemId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        return view;
    }

    private boolean validateInput(long platform, String apikey, String apisecret, String apiname, String redirectUrl) {
        if(platform != 0 && !apikey.equals("") && !apisecret.equals("") && !apiname.equals("") && !redirectUrl.equals(""))
            return true;
        else return false;
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

    private void setPlatformDropdown() {
        binding.ll2.setVisibility(View.VISIBLE);
        binding.progressbar.setVisibility(View.GONE);
        platformlist.add(0,"Select platform");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, platformlist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.platformDropdown.setAdapter(adapter);
    }

    private void saveApi(String apikey, String apisecret, String apiname,String redirectUrl) {

        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("platform",platform);
            jsonObject.put("apikey",apikey);
            jsonObject.put("apisecret",apisecret);
            jsonObject.put("apiname",apiname);
            jsonObject.put("redirecturl",redirectUrl);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String url = Constants.USERAPIMAPPING_ADD_URL;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,jsonObject,
                object -> {
                    progressDialog.dismiss();
                    try {
                        object.getString("id");
                        Toast.makeText(requireContext(), "Saved Succesfully", Toast.LENGTH_SHORT).show();
                        binding.etApiName.setText("");
                        binding.etApiKey.setText("");
                        binding.etApiSecret.setText("");
                        binding.etredirectUrl.setText("");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    showDialogWithOKButton("An error occurred. Please try again.");
                }){
                    @Override
                    public Map<String, String> getHeaders() {
                        String jwttoken = "Bearer "+getTokenValue(requireContext());
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Authorization", jwttoken);
                        return headers;
                    }
                };


        queue.add(request);
    }

    private void showDialogWithOKButton(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .create()
                .show();
    }

    public static String getTokenValue(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.KEY_STRING_VALUE, "");
    }




}

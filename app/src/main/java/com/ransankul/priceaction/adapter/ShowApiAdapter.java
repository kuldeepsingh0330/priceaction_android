package com.ransankul.priceaction.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ransankul.priceaction.R;
import com.ransankul.priceaction.activity.WebViewActivity;
import com.ransankul.priceaction.databinding.ApiShownLayoutBinding;
import com.ransankul.priceaction.model.PlatformApi;
import com.ransankul.priceaction.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShowApiAdapter extends RecyclerView.Adapter<ShowApiAdapter.ApiShowViewHolder>{

    private Context context;
    private ArrayList<PlatformApi> apiList;
    private Dialog progressDialog;


    public ShowApiAdapter(Context context, ArrayList<PlatformApi> apiList, Dialog progressDialog) {
        this.context = context;
        this.apiList = apiList;
        this.progressDialog = progressDialog;
    }

    @NonNull
    @Override
    public ApiShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShowApiAdapter.ApiShowViewHolder(LayoutInflater.from(context).inflate(R.layout.api_shown_layout, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ApiShowViewHolder holder, int position) {
        PlatformApi platformApi = apiList.get(position);


        holder.binding.tvapinameoroperater.setText(platformApi.getApiname()+" - "+platformApi.getPlatform());



        if(platformApi.getLastTokenTime() != null){
            holder.binding.ll4.setVisibility(View.VISIBLE);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

            String formattedDate = dateFormat.format(platformApi.getLastTokenTime());
            String formattedTime = timeFormat.format(platformApi.getLastTokenTime());

            holder.binding.tvlasttokentime.setText(formattedDate+" "+formattedTime);
        }else{
            holder.binding.ll4.setVisibility(View.GONE);
        }

        if(platformApi.isOnOrOff()){
            holder.binding.apionbutton.setText("DEACTIVATE");
            holder.binding.apionbutton.setBackgroundColor(ContextCompat.getColor(context, R.color.darkgrey));
        }else{
            holder.binding.apionbutton.setText("ACTIVATE");
            holder.binding.apionbutton.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
        }

        if(platformApi.isConnectedOrNot()){
            holder.binding.apiconnectbutton.setText("DISCONNECT");
            holder.binding.apiconnectbutton.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
        }else{
            holder.binding.apiconnectbutton.setText("CONNECT");
            holder.binding.apiconnectbutton.setBackgroundColor(ContextCompat.getColor(context, R.color.purple_500));
        }

        holder.binding.apideletebutton.setOnClickListener(v -> {
            deleteApi(platformApi);
        });

        holder.binding.apiconnectbutton.setOnClickListener(v -> {
            String c = holder.binding.apiconnectbutton.getText().toString();
            if(c.equals("CONNECT")){
                connectApi(platformApi);
            }else{
                disconnectApi(platformApi);
            }
        });

        holder.binding.apionbutton.setOnClickListener(v -> {
            activatedeactivateApi(platformApi);
        });

        holder.binding.ivshowapikey.setOnClickListener(n -> {
            String c = holder.binding.tvapikey.getText().toString();
            if(c.equals("XXXXXXXX")){
                holder.binding.tvapikey.setText(platformApi.getApikey());
            }else{
                holder.binding.tvapikey.setText(R.string.xxxxxxxx);
            }
        });



    }

    private void activatedeactivateApi(PlatformApi platformApi) {
        progressDialog.show();
        String tokenValue = Constants.getTokenValue(context);
        String url = Constants.USERAPIMAPPING_ONOFF_URL+platformApi.getId();
        StringRequest request = new StringRequest(Request.Method.POST,url,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(response);
                        object.getString("id");
                        platformApi.setOnOrOff(!platformApi.isOnOrOff());
                        notifyDataSetChanged();
                    } catch (JSONException e) {
                        showDialogWithOKButton("Something went wrong");
                    }
                },error -> {
            progressDialog.dismiss();
                showDialogWithOKButton("Invalid Credential");

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

    private void disconnectApi(PlatformApi platformApi) {
        progressDialog.show();
        String tokenValue = Constants.getTokenValue(context);
        String url = Constants.USERAPIMAPPING_CONNECTEDORDIS_URL+platformApi.getId();
        StringRequest request = new StringRequest(Request.Method.POST,url,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(response);

                        object.getString("id");
                        platformApi.setConnectedOrNot(!platformApi.isConnectedOrNot());
                        notifyDataSetChanged();
                    } catch (JSONException e) {
                        showDialogWithOKButton("Something went wrong");
                    }
                },error -> {
            progressDialog.dismiss();
            showDialogWithOKButton("Invalid Credential");

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

    private void connectApi(PlatformApi platformApi) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("client_Id",platformApi.getApikey());
        intent.putExtra("redirect_url",platformApi.getredirecturl());
        Log.d("hhhhhhhh",platformApi.getId().toString());
        intent.putExtra("api_id",platformApi.getId());
        context.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return apiList.size();
    }

    public class ApiShowViewHolder extends RecyclerView.ViewHolder {

        ApiShownLayoutBinding binding;
        public ApiShowViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ApiShownLayoutBinding.bind(itemView);
        }
    }


    private void deleteApi(PlatformApi platformApi) {
        progressDialog.show();
        String tokenValue = Constants.getTokenValue(context);
        String url = Constants.USERAPIMAPPING_REMOVE_URL+platformApi.getId();
        StringRequest request = new StringRequest(Request.Method.DELETE,url,
                response -> {
                    progressDialog.dismiss();
                    if(response.contains("removed succesfully")){
                        apiList.remove(platformApi);
                        notifyDataSetChanged();
                    }

                    showDialogWithOKButton(response);

                },error -> {
            progressDialog.dismiss();
            showDialogWithOKButton("Invalid Credential");

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

    private void showDialogWithOKButton(String message) {
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .create()
                .show();
    }



}



package com.ransankul.priceaction.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ransankul.priceaction.R;
import com.ransankul.priceaction.databinding.ActivityWbViewBinding;
import com.ransankul.priceaction.util.Constants;

import java.util.HashMap;
import java.util.Map;

import im.delight.android.webview.AdvancedWebView;

public class WebViewActivity extends AppCompatActivity implements AdvancedWebView.Listener{

    private ActivityWbViewBinding binding;
    private long api_id;

    private Dialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWbViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.please_wait);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, android.R.color.transparent)));


        Intent intent = getIntent();
        String client_Id = intent.getStringExtra("client_Id");
        String redirect_url = intent.getStringExtra("redirect_url");
        api_id = intent.getLongExtra("api_id",-1);


        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.setWebViewClient(new WebViewClient());
        binding.webview.setListener(this, this);

        String baseUrl = Constants.UPSTOX_LOGIN_DIALOG_URL;
        Uri.Builder uriBuilder = Uri.parse(baseUrl).buildUpon();

        uriBuilder.appendQueryParameter("client_id", client_Id);
        uriBuilder.appendQueryParameter("redirect_uri", redirect_url);
        uriBuilder.appendQueryParameter("response_type", "code");

        Map<String, String> headers = new HashMap<>();
        headers.put("Api-Version", "2.0");


        binding.webview.loadUrl(uriBuilder.toString(), headers);
    }


    @Override
    public void onPageStarted(String url, Bitmap favicon) {

        if(url.contains("localhost")){
            binding.webview.setVisibility(View.GONE);
            progressDialog.show();
            String u = Constants.LOCAL_UPSTOX_ACCESS_TOKEN_URL+url.substring(url.indexOf('?'))+"&id="+api_id;

            String tokenValue = Constants.getTokenValue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, u,
                    response -> {
                if(response.equals("201")){
                    showDialogWithOKButton("API connected succesfully");
                }else{
                    showDialogWithOKButton("Something went wrong. Try again");
                }
                    },
                    error -> {
                showDialogWithOKButton("Something went wrong. Try again");
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + tokenValue);
                    return headers;
                }
            };

            Volley.newRequestQueue(this).add(stringRequest);

        }
    }

    @Override
    public void onPageFinished(String url) {

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void showDialogWithOKButton(String message) {

        progressDialog.dismiss();
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    onBackPressed();
                })
                .setCancelable(false)
                .create()
                .show();
    }
}
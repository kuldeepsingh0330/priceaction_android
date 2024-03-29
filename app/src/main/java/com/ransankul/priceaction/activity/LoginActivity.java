package com.ransankul.priceaction.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ransankul.priceaction.R;
import com.ransankul.priceaction.databinding.ActivityLoginBinding;
import com.ransankul.priceaction.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private Dialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        progressDialog = new Dialog(LoginActivity.this);
        progressDialog.setContentView(R.layout.please_wait);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, android.R.color.transparent)));

        binding.buttonLogin.setOnClickListener(view -> {
            progressDialog.show();
            String userName = binding.editTextUsername.getText().toString().trim();
            String password = binding.editTextPassword.getText().toString().trim();

            createRequest(userName,password);

        });

        binding.textViewSignUp.setOnClickListener(view -> {

            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);

        });


        String tok = getTokenValue(getApplicationContext());
        if(!tok.equals("")){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity( intent);
            finish();
        }
    }

    void createRequest(String username, String password){
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username",username);
            jsonObject.put("password",password);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String url = Constants.LOGIN_USER_URL;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,jsonObject,
                object -> {
                    // Request successful
                    progressDialog.dismiss();
                    try {
                        String value = object.getString("token");
                        String msg = object.getString("msg");
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                        saveTokenValue(getApplicationContext(),value);
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    // Request failed
                    progressDialog.dismiss();
                    if (error.networkResponse != null) {
                        showDialogWithOKButton("Invalid username or password.");
                    } else {
                        showDialogWithOKButton("An error occurred. Please try again.");
                    }
                });
        queue.add(request);

    }

    public static String getTokenValue(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.KEY_STRING_VALUE, "");
    }




    private void showDialogWithOKButton(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .create()
                .show();
    }

    public static void saveTokenValue(Context context, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_STRING_VALUE, value);
        editor.apply();
    }

}
package com.ransankul.priceaction.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ransankul.priceaction.databinding.ActivityWbViewBinding;
import com.ransankul.priceaction.util.Constants;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

public class ChartWebviewActivity extends AppCompatActivity {

    private ActivityWbViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWbViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setFullScreenMode();
        String symbol = getIntent().getStringExtra("symbol");
        binding.webview.loadUrl(Constants.TRADING_VIEW_LOAD_CHART_URL+symbol);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setFullScreenMode() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
    }
}
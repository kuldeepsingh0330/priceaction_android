package com.ransankul.priceaction.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationBarView;
import com.ransankul.priceaction.R;
import com.ransankul.priceaction.databinding.ActivityMainBinding;
import com.ransankul.priceaction.fragment.ApiFragment;
import com.ransankul.priceaction.fragment.DashboardFragment;
import com.ransankul.priceaction.fragment.SequenceGeneratorFragment;
import com.ransankul.priceaction.fragment.UserFragment;
import com.ransankul.priceaction.fragment.WatchlistFragment;
import com.ransankul.priceaction.util.Constants;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainframelayout,new DashboardFragment());
        transaction.commit();

        binding.bottomNavigation.setOnItemSelectedListener(item -> {

            FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()){
                case R.id.menu_dashboard:
                    transaction1.replace(R.id.mainframelayout,new DashboardFragment());
                    break;
                case R.id.menu_api:
                    transaction1.replace(R.id.mainframelayout,new ApiFragment());
                    break;
                case R.id.menu_watchlist:
                    transaction1.replace(R.id.mainframelayout,new WatchlistFragment());
                    break;
                case R.id.menu_sequencegenerator:
                    transaction1.replace(R.id.mainframelayout,new SequenceGeneratorFragment());
                    break;
                case R.id.menu_user:
                    transaction1.replace(R.id.mainframelayout,new UserFragment());
                    break;
            }
            transaction1.commit();
            return true;
        });

        Log.d("hhhhhhhhhhhhhh",Constants.getTokenValue(this));

        
    }

}
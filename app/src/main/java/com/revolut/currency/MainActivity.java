package com.revolut.currency;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.revolut.currency.adapter.MainAdapter;
import com.revolut.currency.model.Country;
import com.revolut.currency.model.EventMesage;
import com.revolut.currency.remote.RateService;
import com.revolut.currency.viewmodel.MainActivityViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mainActivityViewModel;
    RecyclerView recyclerView;
    MainAdapter mainAdapter;
    MainActivity context;
    ProgressBar progressBar;

    private ArrayList<Country> countryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progressBar);

        context = this;

        mainActivityViewModel.init().observe(this, new Observer<List<Country>>() {

            @Override
            public void onChanged(List<Country> countries) {
                progressBar.setVisibility(View.GONE);
                startServiceForGettingRates(countries.get(0).getCurrencyName(), countries.get(0).getRate().get(1));
                countryList.addAll(countries);
                mainAdapter.notifyDataSetChanged();
            }

        });

        setupRecyclerView();

    }


    private void setupRecyclerView() {
        if (mainAdapter == null) {

            mainAdapter = new MainAdapter(countryList, new OnViewChanged() {

                @Override
                public void onItemClicked(int position) {
                    context.stopService(new Intent(context, RateService.class));
                    Country movedItem = countryList.remove(position);
                    countryList.add(0, movedItem);
                    Log.d("okh", "onItemClicked: " + countryList.get(0).getCurrencyName() + " "+ countryList.get(0).getRate());
                    mainAdapter.notifyDataSetChanged();


                }
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(mainAdapter);
            recyclerView.setItemViewCacheSize(countryList.size());
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setNestedScrollingEnabled(true);

            mainAdapter.notifyDataSetChanged();
//            startServiceForGettingRates(countryList.get(0).getCurrencyName());

        } else {
            mainAdapter.notifyDataSetChanged();
        }
    }


    private void startServiceForGettingRates(String countryTag, String amount) {
        Intent intent = new Intent(context, RateService.class);
        intent.putExtra("countryTag", countryTag);
        intent.putExtra("amount", amount);

        this.context.startService(intent);
    }

    @Subscribe
    public void stopServiceForGettingRates() {
        Intent intent = new Intent(context, RateService.class);
        this.context.stopService(intent);
    }

    @Override
    public void onBackPressed() {
//        stopService(new Intent(this, RateService.class));
        recyclerView.removeAllViewsInLayout();
        recyclerView.removeAllViews();
        countryList.clear();

        super.onBackPressed();
    }

    public void onStart() {
        super.onStart();

    }
    public void onStop(){
        super.onStop();
//        EventBus.getDefault().unregister(this);
    }

    @Override
    public void finish() {
        stopService(new Intent(this, RateService.class));
        super.finish();
    }

}

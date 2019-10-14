package com.revolut.currency;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.revolut.currency.adapter.MainAdapter;
import com.revolut.currency.model.Country;
import com.revolut.currency.remote.RateService;
import com.revolut.currency.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements ServiceCallback {

    private MainActivityViewModel mainActivityViewModel;
    RecyclerView recyclerView;
    MainAdapter mainAdapter;
    MainActivity context;
    ProgressBar progressBar;
    private RateService rateService;
    private boolean bound = false;
    MyReceiver mMessageReceiver;

    private ArrayList<Country> countryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progressBar);

        context = this;

        setUpData();

        setupRecyclerView();

    }

    public void setUpData() {
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mainActivityViewModel.init().observe(this, new Observer<List<Country>>() {

            @Override
            public void onChanged(List<Country> countries) {

                progressBar.setVisibility(View.GONE);
//                startServiceForGettingRates("EUR", "1");
                countryList.addAll(countries);
                mainAdapter.notifyDataSetChanged();
            }

        });
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

    @Override
    public void onBackPressed() {
        recyclerView.removeAllViewsInLayout();
        recyclerView.removeAllViews();
        countryList.clear();

        super.onBackPressed();
    }

    public void onStart() {
        mMessageReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RateService.MY_ACTION);
        registerReceiver(mMessageReceiver, intentFilter);

        //Start our own service
//        Intent intent = new Intent(MainActivity.this,
//                RateService.class);
//        startService(intent);
        super.onStart();

    }
    public void onStop(){
        super.onStop();
        unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void finish() {
        stopService(new Intent(this, RateService.class));
        super.finish();
    }

    @Override
    public void getCountry(String country, String amount) {

        if (bound) {
            mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
            mainActivityViewModel.init().observe(this, new Observer<List<Country>>() {

                @Override
                public void onChanged(List<Country> countries) {
                    countryList.addAll(countries);
                    mainAdapter.notifyDataSetChanged();
                }

            });
        }
    }


    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            Bundle bundle = arg1.getExtras();
            List<Country> countries = (List<Country>) bundle.getSerializable("DATAPASSED");
            Log.d("okh", "onReceive: " + countries.get(0).getRate());
            countryList.removeAll(countries);
            countryList.addAll(countries);
            mainAdapter.notifyDataSetChanged();

        }

    }
}

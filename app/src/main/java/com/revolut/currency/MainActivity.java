package com.revolut.currency;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
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
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mainActivityViewModel;
    RecyclerView recyclerView;
    MainAdapter mainAdapter;
    MainActivity context;

    private ArrayList<Country> countryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);

        context = this;

        mainActivityViewModel.init().observe(this, new Observer<List<Country>>() {

            @Override
            public void onChanged(List<Country> countries) {
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
                    Country movedItem = countryList.remove(position);
                    countryList.add(0, movedItem);
                    mainAdapter.notifyDataSetChanged();
                }
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(mainAdapter);
            recyclerView.setItemViewCacheSize(countryList.size());
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setNestedScrollingEnabled(true);


            mainAdapter.getNewCurrencyMutableLiveData().observe(context, new Observer<HashMap<String, String>>() {
                @Override
                public void onChanged(HashMap<String, String> countryHash) {
                    final List<String> keys = new ArrayList<>(countryHash.keySet());
                    Log.d("okh", "onChanged: " + keys.get(0)+ " " + countryHash.get(keys.get(0)));
                }

            });

            mainAdapter.notifyDataSetChanged();

        } else {
            mainAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        stopService(new Intent(this, RateService.class));
        recyclerView.removeAllViewsInLayout();
        recyclerView.removeAllViews();
        countryList.clear();

        super.onBackPressed();
    }

    @Override
    public void finish() {
        stopService(new Intent(this, RateService.class));
        super.finish();
    }

}

package com.revolut.currency;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.revolut.currency.adapter.MainAdapter;
import com.revolut.currency.model.Country;
import com.revolut.currency.remote.RateService;
import com.revolut.currency.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mainActivityViewModel;
    RateService rateService;
    RecyclerViewDragDropManager dragMgr;
    RecyclerView recyclerView;
    MainAdapter mainAdapter;
    MainActivity context;

    private List<Country> countryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        mainActivityViewModel.init();

        context = this;

        mainActivityViewModel.getCurrencyMutableLiveData().observe(this, new Observer<List<Country>>() {

            @Override
            public void onChanged(List<Country> currencies) {
                countryList.addAll(currencies);
                mainAdapter.notifyDataSetChanged();
            }

        });

        RateService.country = "BRl";
        setupRecyclerView();
    }


    private void setupRecyclerView() {
        if (mainAdapter == null) {

//            countryList = mainActivityViewModel.getCurrencyMutableLiveData().getValue();
            mainAdapter = new MainAdapter(countryList, new OnViewChanged() {

                @Override
                public void onTextChanged(String charSeq) {

//                    mainActivityViewModel.setNewCurrencyMutableLiveData(charSeq).observe(context, new Observer<List<Country>>() {
//                        @Override
//                        public void onChanged(List<Country> currencies) {
//                            mainAdapter.notifyDataSetChanged();
//                            Log.d("itemchange", currencies.get(0).getRate() + " "+currencies.get(0).getAmount() + " "+ currencies.get(0).getName());
//                        }
//                    });

                    mainActivityViewModel.getCurrencyMutableLiveData().observe(context, new Observer<List<Country>>() {

                        @Override
                        public void onChanged(List<Country> currencies) {
                            Log.d("amount", currencies.get(0).getRate());
                        }
                    });
                }

                @Override
                public void onItemClicked(int position) {
                    Country movedItem = countryList.remove(position);
                    Log.d("itemclick",  movedItem.getRate() + " "+movedItem.getRate() + " "+movedItem.getCountryName());
                    countryList.add(0, movedItem);
                    mainAdapter.notifyDataSetChanged();


                }
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(mainAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setNestedScrollingEnabled(true);

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

    public void clearList(){
        countryList.clear();
    }

}

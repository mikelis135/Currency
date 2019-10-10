package com.revolut.currency;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.revolut.currency.adapter.MainAdapter;
import com.revolut.currency.model.Currency;
import com.revolut.currency.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mainActivityViewModel;
    RecyclerViewDragDropManager dragMgr;
    RecyclerView recyclerView;
    MainAdapter mainAdapter;
    MainActivity context;

    private List<Currency> currencyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        setContentView(R.layout.activity_main);

        mainActivityViewModel.init();

        context = this;

        initializeRecycler();



    }

    private void initializeRecycler() {

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mainActivityViewModel.getCurrencyMutableLiveData().observe(this, new Observer<List<Currency>>() {

            @Override
            public void onChanged(List<Currency> currencies) {

            }
        });

        currencyList = mainActivityViewModel.getCurrencyMutableLiveData().getValue();

        mainAdapter = new MainAdapter(currencyList, new OnViewChanged() {

            @Override
            public void onTextChanged(String charSeq) {

//                    mainActivityViewModel.setNewCurrencyMutableLiveData(charSeq).observe(context, new Observer<List<Currency>>() {
//                        @Override
//                        public void onChanged(List<Currency> currencies) {
//                            mainAdapter.notifyDataSetChanged();
//                            Log.d("itemchange", currencies.get(0).getRate() + " "+currencies.get(0).getAmount() + " "+ currencies.get(0).getName());
//                        }
//                    });

                mainActivityViewModel.getCurrencyMutableLiveData().observe(context, new Observer<List<Currency>>() {

                    @Override
                    public void onChanged(List<Currency> currencies) {
                        Log.d("amount", currencies.get(0).getRate());
                    }
                });
            }

            @Override
            public void onItemClicked(int position) {
                Currency movedItem = currencyList.remove(position);
                Log.d("itemclick",  movedItem.getRate() + " "+movedItem.getRate() + " "+movedItem.getName());
                currencyList.add(0, movedItem);
                mainAdapter.notifyDataSetChanged();


            }
        });

        dragMgr = new RecyclerViewDragDropManager();
        dragMgr.setInitiateOnMove(true);
        recyclerView.setAdapter(dragMgr.createWrappedAdapter(mainAdapter));
        dragMgr.attachRecyclerView(recyclerView);
        mainAdapter.notifyDataSetChanged();

    }


    @Override
    public void onBackPressed() {
        recyclerView.removeAllViewsInLayout();
        recyclerView.removeAllViews();
        currencyList.clear();
        super.onBackPressed();
    }

    public void clearList(){
        currencyList.clear();
    }

}

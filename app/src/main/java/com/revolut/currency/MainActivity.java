package com.revolut.currency;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.revolut.currency.adapter.MainAdapter;
import com.revolut.currency.model.Currency;
import com.revolut.currency.viewmodel.MainActivityViewModel;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mainActivityViewModel;
    RecyclerViewDragDropManager dragMgr;
    private MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        setContentView(R.layout.activity_main);

        mainActivityViewModel.init();

        initializeRecycler();

        mainActivityViewModel.getCurrencyMutableLiveData().observe(this, new Observer<List<Currency>>() {
                    @Override
                    public void onChanged(List<Currency> currencies) {

                    }
                });
    }

    private void initializeRecycler() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        dragMgr = new RecyclerViewDragDropManager();
        dragMgr.setInitiateOnMove(true);
        dragMgr.setInitiateOnLongPress(true);

        mainAdapter = new MainAdapter(mainActivityViewModel.getCurrencyMutableLiveData().getValue());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(dragMgr.createWrappedAdapter(mainAdapter));

        dragMgr.attachRecyclerView(recyclerView);

    }

}

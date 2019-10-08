package com.revolut.currency.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;
import com.revolut.currency.R;
import com.revolut.currency.model.Currency;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> implements DraggableItemAdapter<MainAdapter.MyViewHolder> {

    private static final String TAG = "MainAdapter";
    private List<Currency> currencyList;

    public MainAdapter(List<Currency> currencyList) {
        setHasStableIds(true);
        this.currencyList = currencyList;
    }

    class MyViewHolder extends AbstractDraggableItemViewHolder {

        private TextView name;

        MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.currencyName);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "MainAdapter onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Log.d(TAG, "MainAdapter: " + currencyList.get(position).getName() + " " + position);
        holder.name.setText(currencyList.get(position).getName());
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Currency movedItem = currencyList.remove(position);
                currencyList.add(0, movedItem);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return currencyList.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return currencyList.size();
    }

    @Override
    public boolean onCheckCanStartDrag(@NonNull MyViewHolder holder, int position, int x, int y) {
        return false;
    }

    @Nullable
    @Override
    public ItemDraggableRange onGetItemDraggableRange(@NonNull MyViewHolder holder, int position) {
        return null;
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {
        Currency movedItem = currencyList.remove(fromPosition);
        currencyList.add(toPosition, movedItem);
    }

    @Override
    public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {
        return false;
    }

    @Override
    public void onItemDragStarted(int position) {

    }

    @Override
    public void onItemDragFinished(int fromPosition, int toPosition, boolean result) {

    }


}

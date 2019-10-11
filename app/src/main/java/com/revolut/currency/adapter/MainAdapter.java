package com.revolut.currency.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;
import com.revolut.currency.OnViewChanged;
import com.revolut.currency.R;
import com.revolut.currency.databinding.ListItemBinding;
import com.revolut.currency.model.Currency;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> implements DraggableItemAdapter<MainAdapter.MyViewHolder> {

    private List<Currency> currencyList;
    private OnViewChanged onViewChanged;


    public MainAdapter(List<Currency> currencyList,  OnViewChanged onViewChanged) {
        setHasStableIds(true);
        this.currencyList = currencyList;
        this.onViewChanged = onViewChanged;
    }

    class MyViewHolder extends AbstractDraggableItemViewHolder {

        ListItemBinding listItemBinding;

        MyViewHolder(@NonNull ListItemBinding itemView) {
            super(itemView.getRoot());
            listItemBinding = itemView;
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemBinding listItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item , parent, false);
        return new MyViewHolder(listItemBinding);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Currency currency = currencyList.get(position);
        holder.listItemBinding.setCurrency(currency);
        final EditText amountEdit = holder.listItemBinding.getRoot().findViewById(R.id.amount);
        TextView textView = holder.listItemBinding.getRoot().findViewById(R.id.currencyName);
//
//        amountEdit.setText(currency.getRate());
//        textView.setText(currency.getName());

        amountEdit.setSelection(amountEdit.getText().length());

        holder.listItemBinding.getRoot().findViewById(R.id.holderLayout).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (position != 0) {
                    onViewChanged.onItemClicked(position);
                    Log.d("amount", amountEdit.getEditableText().toString());
                }
            }
        });

        ((EditText) holder.listItemBinding.getRoot().findViewById(R.id.amount)).addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, final int i2) {

                    if (position == 0) {
                        onViewChanged.onTextChanged(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    @Override
    public long getItemId(int position) {
        return currencyList.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return null != currencyList ? currencyList.size() : 0;
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

package com.revolut.currency.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;
import com.revolut.currency.OnViewChanged;
import com.revolut.currency.R;
import com.revolut.currency.databinding.ListItemBinding;
import com.revolut.currency.model.Country;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> implements DraggableItemAdapter<MainAdapter.MyViewHolder> {

    private List<Country> countryList;
    private OnViewChanged onViewChanged;
    private String storyUrl = "http://www.geognos.com/api/en/countries/flag/";


    public MainAdapter(List<Country> countryList, OnViewChanged onViewChanged) {
        setHasStableIds(true);
        this.countryList = countryList;
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

        Country country = countryList.get(position);
        holder.listItemBinding.setCurrency(country);
        final EditText amountEdit = holder.listItemBinding.getRoot().findViewById(R.id.amount);
        final ImageView countryFlag = holder.listItemBinding.getRoot().findViewById(R.id.countryFlag);

        amountEdit.setSelection(amountEdit.getText().length());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_launcher_background);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(holder.itemView.getContext())
                .setDefaultRequestOptions(requestOptions)
                .load(storyUrl+ country.getCurrencyName().substring(0, 2)+".png")
                .thumbnail( 0.5f )
                .into(countryFlag);

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
        return countryList.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return null != countryList ? countryList.size() : 0;
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

package com.amoharib.graduationproject.buyer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amoharib.graduationproject.models.CartItem;
import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.buyer.viewholders.BillViewHolder;



import java.util.ArrayList;

public class BillAdapter extends RecyclerView.Adapter<BillViewHolder> {
    private ArrayList<CartItem> items;

    public BillAdapter(ArrayList<CartItem> items) {
        this.items = items;
    }

    @Override
    public BillViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_item, parent, false);
        return new BillViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BillViewHolder holder, int position) {
        CartItem item = items.get(position);
        holder.updateUI(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

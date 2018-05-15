package com.amoharib.foodorderapp.buyer.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amoharib.foodorderapp.buyer.activities.OrderActivity;
import com.amoharib.foodorderapp.models.CartItem;
import com.amoharib.foodorderapp.R;
import com.amoharib.foodorderapp.buyer.viewholders.OrderViewHolder;

import java.util.ArrayList;

/**
 * Created by AMoharib on 2018-03-12.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder> {

    private ArrayList<CartItem> cartItems;
    private OrderActivity activity;

    public OrderAdapter(ArrayList<CartItem> cartItems, OrderActivity activity) {
        this.cartItems = cartItems;
        this.activity = activity;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderViewHolder holder, int position) {
        final CartItem cartItem = cartItems.get(position);
        holder.updateUI(cartItem);

        holder.incQuantityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.incQuantity(holder.getAdapterPosition());

                if (!holder.decQuantityBtn.isEnabled()) holder.decQuantityBtn.setEnabled(true);
                notifyItemChanged(holder.getAdapterPosition());
            }
        });

        holder.decQuantityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartItem.getQuantity() > 1) {
                    activity.decQuantity(holder.getAdapterPosition());
                }
                if (cartItem.getQuantity() == 1) {
                    holder.decQuantityBtn.setEnabled(false);
                }
                notifyItemChanged(holder.getAdapterPosition());

            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }
}

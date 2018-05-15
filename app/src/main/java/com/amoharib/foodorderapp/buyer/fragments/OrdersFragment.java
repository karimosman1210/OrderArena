package com.amoharib.foodorderapp.buyer.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.amoharib.foodorderapp.R;
import com.amoharib.foodorderapp.buyer.adapters.UserOrderAdapter;
import com.amoharib.foodorderapp.interfaces.DataListeners;
import com.amoharib.foodorderapp.models.Order;
import com.amoharib.foodorderapp.services.DataService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends Fragment {


    private LinearLayout noOrdersLayout;
    private RecyclerView ordersRecycler;
    private ProgressBar progressBar;
    private ArrayList<Order> mOrders;
    private UserOrderAdapter userOrderAdapter;

    public OrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_orders, container, false);
        initView(v);


        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mOrders = new ArrayList<>();
        userOrderAdapter = new UserOrderAdapter(mOrders);

        ordersRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        ordersRecycler.setAdapter(userOrderAdapter);

        DataService.getInstance().getUsersOrders(userId, new DataListeners.OnOrderListener() {
            @Override
            public void onDataReceived(ArrayList<Order> orders) {
                progressBar.setVisibility(View.GONE);
                if (orders == null) {
                    noOrdersLayout.setVisibility(View.VISIBLE);
                } else {
                    ordersRecycler.setVisibility(View.VISIBLE);
                    userOrderAdapter.orders = orders;
                    userOrderAdapter.notifyDataSetChanged();
                }
            }
        });


        return v;
    }

    private void initView(View v) {
        noOrdersLayout = (LinearLayout) v.findViewById(R.id.no_orders_layout);
        ordersRecycler = (RecyclerView) v.findViewById(R.id.orders_recycler);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
    }
}

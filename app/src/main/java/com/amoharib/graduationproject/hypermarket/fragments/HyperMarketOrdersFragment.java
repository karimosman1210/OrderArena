package com.amoharib.graduationproject.hypermarket.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amoharib.graduationproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HyperMarketOrdersFragment extends Fragment {


    public HyperMarketOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hyper_market_orders, container, false);
    }

}

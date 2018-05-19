package com.amoharib.graduationproject.buyer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.hypermarket.activities.HyperMarketActivity;

public class CategoryActivity extends AppCompatActivity {

    FrameLayout hypermarket_framelayout;
    private Toolbar toolbar;
    private FrameLayout restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

    hypermarket_framelayout=(FrameLayout)findViewById(R.id.hyper_market_selection);


    hypermarket_framelayout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            startActivity(new Intent(CategoryActivity.this, HyperMarketActivity.class));

        }
    });
        initView();

        restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        restaurant = (FrameLayout) findViewById(R.id.restaurant);
    }
}

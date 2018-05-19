package com.amoharib.graduationproject.buyer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.hypermarket.activities.HyperMarketActivity;

public class CategoryActivity extends AppCompatActivity {

    FrameLayout hypermarket_framelayout;
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
    }
}

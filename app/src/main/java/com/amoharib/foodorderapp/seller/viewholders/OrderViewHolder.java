package com.amoharib.foodorderapp.seller.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amoharib.foodorderapp.R;
import com.amoharib.foodorderapp.utils.StringUtils;

public class OrderViewHolder extends RecyclerView.ViewHolder {

    private TextView dateTime;
    public Button viewDetailsBtn;

    public OrderViewHolder(View itemView) {
        super(itemView);
        dateTime = (TextView) itemView.findViewById(R.id.timestamp);
        viewDetailsBtn = (Button) itemView.findViewById(R.id.viewDetailsBtn);
    }

    public void updateView (long timestamp) {
        dateTime.setText(StringUtils.getFormattedDate(timestamp));
    }
}

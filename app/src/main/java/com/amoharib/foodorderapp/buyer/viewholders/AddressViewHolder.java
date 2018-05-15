package com.amoharib.foodorderapp.buyer.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amoharib.foodorderapp.models.Address;
import com.amoharib.foodorderapp.R;
import com.amoharib.foodorderapp.utils.StringUtils;

/**
 * Created by AMoharib on 2018-03-27.
 */

public class AddressViewHolder extends RecyclerView.ViewHolder {
    private TextView address;
    public Button editBtn, deleteBtn;

    public AddressViewHolder(View itemView) {
        super(itemView);
        address = (TextView) itemView.findViewById(R.id.compressed_address);
        editBtn = (Button) itemView.findViewById(R.id.edit_address_btn);
        deleteBtn = (Button) itemView.findViewById(R.id.delete_address_btn);

    }

    public void updateUI(Address address) {

        this.address.setText(StringUtils.getFormattedAddress(address));

    }
}

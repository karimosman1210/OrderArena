package com.amoharib.graduationproject.Pharmacy.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amoharib.graduationproject.R;
import com.amoharib.graduationproject.buyer.adapters.AddressAdapter;
import com.amoharib.graduationproject.interfaces.DataListeners;
import com.amoharib.graduationproject.models.Address;
import com.amoharib.graduationproject.services.DataService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class AddressSelectionPharmacy extends AppCompatActivity {
    private RecyclerView addressRecycler;
    private Button addNewAddressBtn;

    public ArrayList<Address> addresses = new ArrayList<>();
    private AddressAdapter adapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_selection_pharmacy);
        setSupportActionBar(toolbar);

    }
    private void initView() {
        addressRecycler = (RecyclerView) findViewById(R.id.addressRecycler);
        addNewAddressBtn = (Button) findViewById(R.id.add_new_address_btn);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }


}

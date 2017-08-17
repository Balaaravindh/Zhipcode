package com.falconnect.zipcode;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.RelativeLayout;

import com.falconnect.zipcode.Adapter.CustomerListAdapter;
import com.falconnect.zipcode.Adapter.CustomerListAdapterMain;
import com.falconnect.zipcode.Adapter.OnCustomerListChangedListener;
import com.falconnect.zipcode.Adapter.OnStartDragListener;
import com.falconnect.zipcode.Adapter.SimpleItemTouchHelperCallback;

import java.util.ArrayList;

public class RutaActivity extends AppCompatActivity implements OnCustomerListChangedListener, OnStartDragListener {

    ActionBar actionBar;
    ArrayList<String> directions = new ArrayList<>();
    ArrayList<String> addrress = new ArrayList<>();
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    ItemTouchHelper mItemTouchHelper;

    RelativeLayout edit_ruta_layout, cancel_ruta_layout, accept_ruta_layout;

    String destination_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta);

        actionBar = getSupportActionBar();
        actionBar.hide();

        initialize();

        destination_size = getIntent().getStringExtra("destination_size");
        int dest_size = Integer.parseInt(destination_size);
        directions.add("DIRECCION ORIGEN");

        for (int i = 0; i < dest_size; i++){

            directions.add("DIRECCION " + " " + String.valueOf(i));

        }

        addrress.add("Shastri Nagar, Adayar, Chennai, Tamil Nadu, India, 600 020");
        addrress.add("Velacherry, Chennai, Tamil Nadu, India, 600 020");
        addrress.add("Anna Nagar, Thirumangalam Chennai, Tamil Nadu, India, 600 020");
        addrress.add("Triplicane, Chennai, Tamil Nadu, India, 600 020");
        addrress.add("Thiruvanmiyur, Chennai, Tamil Nadu, India, 600 020");
        addrress.add("Vadapalani, Chennai, Tamil Nadu, India, 600 020");
        addrress.add("Vadapalani, Chennai, Tamil Nadu, India, 600 020");


        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        edit_ruta_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit_ruta_layout.setVisibility(View.GONE);
                accept_ruta_layout.setVisibility(View.VISIBLE);

                CustomerListAdapter mAdapter = new CustomerListAdapter(directions, addrress,
                        RutaActivity.this, RutaActivity.this, RutaActivity.this);
                ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
                mItemTouchHelper = new ItemTouchHelper(callback);
                mItemTouchHelper.attachToRecyclerView(mRecyclerView);
                mRecyclerView.setAdapter(mAdapter);
            }
        });

        cancel_ruta_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setup the adapter with empty list
                CustomerListAdapterMain mAdapter = new CustomerListAdapterMain(directions, addrress, RutaActivity.this);
                mRecyclerView.setAdapter(mAdapter);
            }
        });


        //setup the adapter with empty list
        CustomerListAdapterMain mAdapter = new CustomerListAdapterMain(directions, addrress, RutaActivity.this);
        mRecyclerView.setAdapter(mAdapter);

    }

    public void initialize(){
        mRecyclerView = (RecyclerView) findViewById(R.id.note_recycler_view);
        edit_ruta_layout = (RelativeLayout) findViewById(R.id.edit_ruta_layout);
        cancel_ruta_layout = (RelativeLayout) findViewById(R.id.cancel_ruta_layout);
        accept_ruta_layout = (RelativeLayout) findViewById(R.id.accept_ruta_layout);
    }

    @Override
    public void onStartDrag(CustomerListAdapter.ItemViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onNoteListChanged(ArrayList<String> customers, ArrayList<String> customers_address) {

    }

}

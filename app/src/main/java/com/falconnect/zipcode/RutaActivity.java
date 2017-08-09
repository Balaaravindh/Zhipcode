package com.falconnect.zipcode;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.ListView;

import com.falconnect.zipcode.Adapter.CustomerListAdapter;
import com.falconnect.zipcode.RutaPageCutomClasses.OnCustomerListChangedListener;
import com.falconnect.zipcode.RutaPageCutomClasses.OnStartDragListener;
import com.falconnect.zipcode.RutaPageCutomClasses.SimpleItemTouchHelperCallback;

import java.util.ArrayList;

public class RutaActivity extends AppCompatActivity implements OnCustomerListChangedListener, OnStartDragListener {

    ActionBar actionBar;
    ArrayList<String> directions = new ArrayList<>();
    ArrayList<String> addrress = new ArrayList<>();
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta);

        actionBar = getSupportActionBar();
        actionBar.hide();

        initialize();

        directions.add("DIRECCION ORIGEN");
        directions.add("DIRECCION 1");
        directions.add("DIRECCION 2");
        directions.add("DIRECCION 3");
        directions.add("DIRECCION 4");
        directions.add("DIRECCION 5");

        addrress.add("Shastri Nagar, Adayar, Chennai, Tamil Nadu, India, 600 020");
        addrress.add("Velacherry, Chennai, Tamil Nadu, India, 600 020");
        addrress.add("Anna Nagar, Thirumangalam Chennai, Tamil Nadu, India, 600 020");
        addrress.add("Triplicane, Chennai, Tamil Nadu, India, 600 020");
        addrress.add("Thiruvanmiyur, Chennai, Tamil Nadu, India, 600 020");
        addrress.add("Vadapalani, Chennai, Tamil Nadu, India, 600 020");

        actionBar = getSupportActionBar();
        actionBar.hide();
        initialize();

        directions.add("DIRECCION ORIGEN");
        directions.add("DIRECCION 1");
        directions.add("DIRECCION 2");
        directions.add("DIRECCION 3");
        directions.add("DIRECCION 4");
        directions.add("DIRECCION 5");

        addrress.add("Shastri Nagar, Adayar, Chennai, Tamil Nadu, India, 600 020");
        addrress.add("Velacherry, Chennai, Tamil Nadu, India, 600 020");
        addrress.add("Anna Nagar, Thirumangalam Chennai, Tamil Nadu, India, 600 020");
        addrress.add("Triplicane, Chennai, Tamil Nadu, India, 600 020");
        addrress.add("Thiruvanmiyur, Chennai, Tamil Nadu, India, 600 020");
        addrress.add("Vadapalani, Chennai, Tamil Nadu, India, 600 020");

        /*DestinationListAdapter destinationListAdapter = new DestinationListAdapter(MainActivity.this, directions, addrress);
        destinations.setAdapter(destinationListAdapter);*/

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //setup the adapter with empty list
        CustomerListAdapter mAdapter = new CustomerListAdapter(directions, addrress, this, this, this);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.setAdapter(mAdapter);

    }

    public void initialize(){
        mRecyclerView = (RecyclerView) findViewById(R.id.note_recycler_view);
    }

    @Override
    public void onStartDrag(CustomerListAdapter.ItemViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onNoteListChanged(ArrayList<String> customers, ArrayList<String> customers_address) {

    }

}

package com.falconnect.zipcode;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.falconnect.zipcode.Adapter.CustomerListAdapter;
import com.falconnect.zipcode.Adapter.CustomerListAdapterMain;
import com.falconnect.zipcode.Adapter.OnCustomerListChangedListener;
import com.falconnect.zipcode.Adapter.OnStartDragListener;
import com.falconnect.zipcode.Adapter.SimpleItemTouchHelperCallback;
import com.falconnect.zipcode.Model.RutaPageModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RutaActivity extends AppCompatActivity implements OnCustomerListChangedListener, OnStartDragListener {

    ActionBar actionBar;
    ArrayList<String> directions = new ArrayList<>();
    ArrayList<String> addrress = new ArrayList<>();
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    ItemTouchHelper mItemTouchHelper;
    RelativeLayout edit_ruta_layout, cancel_ruta_layout, accept_ruta_layout;
    String destination_size;
    String json_object;
    JSONObject json_object_new;
    public HashMap<String, ArrayList<String>> ruta_postion_details;
    public HashMap<String, String> rutapostiondetails;

    public static String community = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta);

        actionBar = getSupportActionBar();
        actionBar.hide();

        initialize();

        destination_size = getIntent().getStringExtra("destination_size");
        json_object = getIntent().getStringExtra("json_object");

        int dest_size = Integer.parseInt(destination_size);

        for (int i = 0; i <= dest_size; i++) {
            directions.add("DIRECCION " + " " + String.valueOf(i));
        }

        get_chage_postion();

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


    }

    private void get_chage_postion() {
        try {
            json_object_new = new JSONObject(json_object);

            ruta_postion_details = new HashMap<>();
            rutapostiondetails = new HashMap<>();

            JSONObject origins = json_object_new.getJSONObject("origin");

            community = origins.optString("community");

            JSONArray destinations = null;
            try {
                destinations = new JSONArray(json_object_new.getString("destinations"));

                if (destinations.length() == 1) {
                    try {
                        JSONObject newjsonobject = (JSONObject) destinations.get(0);
                        String job_accepted_destination_id = newjsonobject.optString("id");
                        String errand = newjsonobject.optString("errand");

                        JSONObject location = newjsonobject.getJSONObject("location");
                        String id = location.optString("id");
                        String user = location.optString("user");

                        JSONObject geo_location = location.getJSONObject("geolocation");
                        String latitude_desti = geo_location.optString("latitude");
                        String longitude_desti = geo_location.optString("longitude");

                        String name = location.optString("name");
                        String address = location.optString("address");
                        String references = location.optString("references");
                        String community_desti = location.optString("community");
                        String self_contact = location.optString("self_contact");
                        String stnumber = location.optString("stnumber");
                        String contact_name = location.optString("contact_name");
                        String phone_number1 = location.optString("phone_number1");
                        String phone_number2 = location.optString("phone_number2");
                        String email = location.optString("email");
                        String auto_generated = location.optString("auto_generated");

                        JSONObject approximate_geolocation = newjsonobject.getJSONObject("approximate_geolocation");
                        String latitude = approximate_geolocation.optString("latitude");
                        String longitude = approximate_geolocation.optString("longitude");

                        String addressee = newjsonobject.optString("addressee");
                        String observation = newjsonobject.optString("observation");
                        String position = newjsonobject.optString("position");
                        String contact_names = newjsonobject.optString("contact_name");
                        String phone_numbers1 = newjsonobject.optString("phone_number1");
                        String emails = newjsonobject.optString("email");
                        String completed = newjsonobject.optString("completed");
                        String order_number = newjsonobject.optString("order_number");
                        String tracking_number = newjsonobject.optString("tracking_number");
                        String was_delivered_to_contact = newjsonobject.optString("was_delivered_to_contact");
                        String was_delivered_to_watchman = newjsonobject.optString("was_delivered_to_watchman");
                        String delivery_remarks = newjsonobject.optString("delivery_remarks");
                        String delivery_contact_rut = newjsonobject.optString("delivery_contact_rut");
                        String delivery_contact_name = newjsonobject.optString("delivery_contact_name");
                        String delivery_contact_phone = newjsonobject.optString("delivery_contact_phone");

                        addrress.add(community_desti);

                        rutapostiondetails.put("job_accepted_destination_id", job_accepted_destination_id);
                        rutapostiondetails.put("errand", errand);
                        rutapostiondetails.put("id", id);
                        rutapostiondetails.put("user", user);
                        rutapostiondetails.put("latitude_desti", latitude_desti);
                        rutapostiondetails.put("longitude_desti", longitude_desti);
                        rutapostiondetails.put("name", name);
                        rutapostiondetails.put("address", address);
                        rutapostiondetails.put("references", references);
                        rutapostiondetails.put("community_desti", community_desti);
                        rutapostiondetails.put("self_contact", self_contact);
                        rutapostiondetails.put("stnumber", stnumber);
                        rutapostiondetails.put("contact_name", contact_name);
                        rutapostiondetails.put("phone_number1", phone_number1);
                        rutapostiondetails.put("phone_number2", phone_number2);
                        rutapostiondetails.put("email", email);
                        rutapostiondetails.put("auto_generated", auto_generated);
                        rutapostiondetails.put("latitude", latitude);
                        rutapostiondetails.put("longitude", longitude);
                        rutapostiondetails.put("addressee", addressee);
                        rutapostiondetails.put("observation", observation);
                        rutapostiondetails.put("position", position);
                        rutapostiondetails.put("contact_names", contact_names);
                        rutapostiondetails.put("phone_numbers1", phone_numbers1);
                        rutapostiondetails.put("emails", emails);
                        rutapostiondetails.put("completed", completed);
                        rutapostiondetails.put("order_number", order_number);
                        rutapostiondetails.put("tracking_number", tracking_number);
                        rutapostiondetails.put("was_delivered_to_contact", was_delivered_to_contact);
                        rutapostiondetails.put("was_delivered_to_watchman", was_delivered_to_watchman);
                        rutapostiondetails.put("delivery_remarks", delivery_remarks);
                        rutapostiondetails.put("delivery_contact_rut", delivery_contact_rut);
                        rutapostiondetails.put("delivery_contact_name", delivery_contact_name);
                        rutapostiondetails.put("delivery_contact_phone", delivery_contact_phone);

                        ArrayList<String> details = new ArrayList<>();
                        details.add(job_accepted_destination_id);
                        details.add(errand);
                        details.add(id);
                        details.add(user);
                        details.add(latitude_desti);
                        details.add(longitude_desti);
                        details.add(name);
                        details.add(address);
                        details.add(references);
                        details.add(community_desti);
                        details.add(self_contact);
                        details.add(stnumber);
                        details.add(contact_name);
                        details.add(phone_number1);
                        details.add(phone_number2);
                        details.add(email);
                        details.add(auto_generated);
                        details.add(latitude);
                        details.add(longitude);
                        details.add(addressee);
                        details.add(observation);
                        details.add(position);
                        details.add(contact_names);
                        details.add(phone_numbers1);
                        details.add(emails);
                        details.add(completed);
                        details.add(order_number);
                        details.add(tracking_number);
                        details.add(was_delivered_to_contact);
                        details.add(was_delivered_to_watchman);
                        details.add(delivery_remarks);
                        details.add(delivery_contact_rut);
                        details.add(delivery_contact_name);
                        details.add(delivery_contact_phone);

                        ruta_postion_details.put(String.valueOf(0 + 1), details);

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    for (int l = 0; l <= destinations.length(); l++) {
                        try {
                            JSONObject newjsonobject = (JSONObject) destinations.get(l);
                            String job_accepted_destination_id = newjsonobject.optString("id");
                            String errand = newjsonobject.optString("errand");

                            JSONObject location = newjsonobject.getJSONObject("location");
                            String id = location.optString("id");
                            String user = location.optString("user");

                            JSONObject geo_location = location.getJSONObject("geolocation");
                            String latitude_desti = geo_location.optString("latitude");
                            String longitude_desti = geo_location.optString("longitude");

                            String name = location.optString("name");
                            String address = location.optString("address");
                            String references = location.optString("references");
                            String community_desti = location.optString("community");
                            String self_contact = location.optString("self_contact");
                            String stnumber = location.optString("stnumber");
                            String contact_name = location.optString("contact_name");
                            String phone_number1 = location.optString("phone_number1");
                            String phone_number2 = location.optString("phone_number2");
                            String email = location.optString("email");
                            String auto_generated = location.optString("auto_generated");

                            JSONObject approximate_geolocation = location.getJSONObject("approximate_geolocation");
                            String latitude = approximate_geolocation.optString("latitude");
                            String longitude = approximate_geolocation.optString("longitude");

                            String addressee = newjsonobject.optString("addressee");
                            String observation = newjsonobject.optString("observation");
                            String position = newjsonobject.optString("position");
                            String contact_names = newjsonobject.optString("contact_name");
                            String phone_numbers1 = newjsonobject.optString("phone_number1");
                            String emails = newjsonobject.optString("email");
                            String completed = newjsonobject.optString("completed");
                            String order_number = newjsonobject.optString("order_number");
                            String tracking_number = newjsonobject.optString("tracking_number");
                            String was_delivered_to_contact = newjsonobject.optString("was_delivered_to_contact");
                            String was_delivered_to_watchman = newjsonobject.optString("was_delivered_to_watchman");
                            String delivery_remarks = newjsonobject.optString("delivery_remarks");
                            String delivery_contact_rut = newjsonobject.optString("delivery_contact_rut");
                            String delivery_contact_name = newjsonobject.optString("delivery_contact_name");
                            String delivery_contact_phone = newjsonobject.optString("delivery_contact_phone");

                            addrress.add(community_desti);

                            rutapostiondetails.put("job_accepted_destination_id", job_accepted_destination_id);
                            rutapostiondetails.put("errand", errand);
                            rutapostiondetails.put("id", id);
                            rutapostiondetails.put("user", user);
                            rutapostiondetails.put("latitude_desti", latitude_desti);
                            rutapostiondetails.put("longitude_desti", longitude_desti);
                            rutapostiondetails.put("name", name);
                            rutapostiondetails.put("address", address);
                            rutapostiondetails.put("references", references);
                            rutapostiondetails.put("community_desti", community_desti);
                            rutapostiondetails.put("self_contact", self_contact);
                            rutapostiondetails.put("stnumber", stnumber);
                            rutapostiondetails.put("contact_name", contact_name);
                            rutapostiondetails.put("phone_number1", phone_number1);
                            rutapostiondetails.put("phone_number2", phone_number2);
                            rutapostiondetails.put("email", email);
                            rutapostiondetails.put("auto_generated", auto_generated);
                            rutapostiondetails.put("latitude", latitude);
                            rutapostiondetails.put("longitude", longitude);
                            rutapostiondetails.put("addressee", addressee);
                            rutapostiondetails.put("observation", observation);
                            rutapostiondetails.put("position", position);
                            rutapostiondetails.put("contact_names", contact_names);
                            rutapostiondetails.put("phone_numbers1", phone_numbers1);
                            rutapostiondetails.put("emails", emails);
                            rutapostiondetails.put("completed", completed);
                            rutapostiondetails.put("order_number", order_number);
                            rutapostiondetails.put("tracking_number", tracking_number);
                            rutapostiondetails.put("was_delivered_to_contact", was_delivered_to_contact);
                            rutapostiondetails.put("was_delivered_to_watchman", was_delivered_to_watchman);
                            rutapostiondetails.put("delivery_remarks", delivery_remarks);
                            rutapostiondetails.put("delivery_contact_rut", delivery_contact_rut);
                            rutapostiondetails.put("delivery_contact_name", delivery_contact_name);
                            rutapostiondetails.put("delivery_contact_phone", delivery_contact_phone);


                            ArrayList<String> details = new ArrayList<>();
                            details.add(job_accepted_destination_id);
                            details.add(errand);
                            details.add(id);
                            details.add(user);
                            details.add(latitude_desti);
                            details.add(longitude_desti);
                            details.add(name);
                            details.add(address);
                            details.add(references);
                            details.add(community_desti);
                            details.add(self_contact);
                            details.add(stnumber);
                            details.add(contact_name);
                            details.add(phone_number1);
                            details.add(phone_number2);
                            details.add(email);
                            details.add(auto_generated);
                            details.add(latitude);
                            details.add(longitude);
                            details.add(addressee);
                            details.add(observation);
                            details.add(position);
                            details.add(contact_names);
                            details.add(phone_numbers1);
                            details.add(emails);
                            details.add(completed);
                            details.add(order_number);
                            details.add(tracking_number);
                            details.add(was_delivered_to_contact);
                            details.add(was_delivered_to_watchman);
                            details.add(delivery_remarks);
                            details.add(delivery_contact_rut);
                            details.add(delivery_contact_name);
                            details.add(delivery_contact_phone);

                            ruta_postion_details.put(String.valueOf(l + 1), details);

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                }

                CustomerListAdapterMain mAdapter = new CustomerListAdapterMain(RutaActivity.this, destination_size, community,
                        ruta_postion_details, directions);
                mRecyclerView.setAdapter(mAdapter);

                edit_ruta_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        edit_ruta_layout.setVisibility(View.GONE);
                        accept_ruta_layout.setVisibility(View.VISIBLE);

                        CustomerListAdapter mAdapter = new CustomerListAdapter(RutaActivity.this, destination_size, community,
                                ruta_postion_details, directions, RutaActivity.this, RutaActivity.this);
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
                        edit_ruta_layout.setVisibility(View.VISIBLE);
                        accept_ruta_layout.setVisibility(View.GONE);

                        CustomerListAdapterMain mAdapter = new CustomerListAdapterMain(RutaActivity.this, destination_size, community,
                                ruta_postion_details, directions);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                });


            } catch (JSONException ex) {
                ex.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void initialize() {
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
    public void onNoteListChanged(ArrayList<HashMap<String, ArrayList<String>>> customers) {

        HashMap<String, ArrayList<String>> values = new HashMap<>();
        for (int i = 0; i < customers.size(); i++) {
            for (int j = 0; j <  customers.get(i).size(); j++){
                values.put(String.valueOf(i), customers.get(j).get(String.valueOf(j)));
            }
        }
        Log.e("OnStartDragListener", values.toString());
    }

    @Override
    public void onNoteListChanged_Sample(ArrayList<String> customerss) {
        Log.e("customerss", customerss.toString());

    }
}

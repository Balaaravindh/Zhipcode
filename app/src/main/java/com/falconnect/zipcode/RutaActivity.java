package com.falconnect.zipcode;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.falconnect.zipcode.Adapter.CustomerListAdapter;
import com.falconnect.zipcode.Adapter.CustomerListAdapterMain;
import com.falconnect.zipcode.Adapter.HomeListViewAdapter;
import com.falconnect.zipcode.Adapter.OnCustomerListChangedListener;
import com.falconnect.zipcode.Adapter.OnStartDragListener;
import com.falconnect.zipcode.Adapter.SimpleItemTouchHelperCallback;
import com.falconnect.zipcode.Model.RutaPageModel;
import com.falconnect.zipcode.SessionManager.Orgin_destination_identy;
import com.falconnect.zipcode.SessionManager.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

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
    int i = 1;
    String json_object;
    HashMap<String, String> datas = new HashMap<>();
    JSONObject json_object_new;
    ArrayList<String> orgins_datas = new ArrayList<>();
    public HashMap<String, ArrayList<String>> ruta_postion_details;
    public HashMap<String, String> rutapostiondetails;
    HashMap<String, ArrayList<String>> new_ruta_postion_details = new HashMap<>();
    public static String community = null;
    JSONObject destinations = new JSONObject();
    String errand, job_accepted_destination_id;
    SessionManager sessionManager;
    HashMap<String, String> user;
    ImageView nav_icon_drawer;
    HashMap<String, String> datas_desti;
    HashMap<Integer, ArrayList<String>> datas_desti_multi  = new HashMap<>();;
    ArrayList<String> multi_lat = new ArrayList<>();
    ArrayList<String> multi_long = new ArrayList<>();
    ArrayList<HashMap<Integer, ArrayList<String>>> new_all_datas = new ArrayList<>();

    HashMap<String, ArrayList<String>> all_datas = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta);

        actionBar = getSupportActionBar();
        actionBar.hide();

        initialize();

        destination_size = getIntent().getStringExtra("destination_size");
        json_object = getIntent().getStringExtra("json_object");

        if (json_object == null){

        }else{

        }


        int dest_size = Integer.parseInt(destination_size);

        directions.add("DIRECCION ORIGIN");

        for (int i = 0; i < dest_size; i++) {
            directions.add("DIRECCION " + " " + String.valueOf(i + 1));
        }

        get_chage_postion();

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        accept_ruta_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                destination_change_API();
            }
        });

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
                        errand = newjsonobject.optString("errand");

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


                        ruta_postion_details.put(String.valueOf(destinations.length()), details);

                        new_ruta_postion_details = ruta_postion_details;

                        CustomerListAdapterMain mAdapter = new CustomerListAdapterMain(RutaActivity.this, destination_size, community,
                                ruta_postion_details, directions);
                        mRecyclerView.setAdapter(mAdapter);

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    for (int l = 0; l <= destinations.length(); l++) {
                        try {
                            JSONObject newjsonobject = (JSONObject) destinations.get(l);
                            job_accepted_destination_id = newjsonobject.optString("id");
                            errand = newjsonobject.optString("errand");

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
                            new_ruta_postion_details = ruta_postion_details;

                            CustomerListAdapterMain mAdapter = new CustomerListAdapterMain(RutaActivity.this, destination_size, community,
                                    ruta_postion_details, directions);
                            mRecyclerView.setAdapter(mAdapter);

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
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

                nav_icon_drawer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RutaActivity.this.finish();
                    }
                });

                cancel_ruta_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog = new Dialog(RutaActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.status_active_custom_alert);
                        TextView message = (TextView) dialog.findViewById(R.id.message);
                        message.setText("¿Seguro que quieres cancelar el encargo?");
                        message.setAllCaps(true);

                        TextView message1 = (TextView) dialog.findViewById(R.id.message1);
                        message1.setText("");

                        final Button positive_button = (Button) dialog.findViewById(R.id.possitive_button);
                        positive_button.setText("SI, ESTOY SEGURO");
                        positive_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                declin_special_errands();
                                dialog.dismiss();
                            }
                        });

                        Button negative_button = (Button) dialog.findViewById(R.id.negative_button);
                        negative_button.setText("NO");
                        negative_button.setTextColor(Color.RED);
                        negative_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
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

        nav_icon_drawer = (ImageView) findViewById(R.id.nav_icon_drawer);

        sessionManager = new SessionManager(RutaActivity.this);
    }

    @Override
    public void onStartDrag(CustomerListAdapter.ItemViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onNoteListChanged(ArrayList<HashMap<String, ArrayList<String>>> customers) {

    }

    @Override
    public void onNoteListChanged_Sample(ArrayList<ArrayList<String>> customers) {

        for (int i = 1; i <= ruta_postion_details.size(); i++) {
            ruta_postion_details.put(String.valueOf(i), customers.get(i));
        }
        new_ruta_postion_details = ruta_postion_details;

    }

    public void destination_change_API() {

        try {

            JSONArray jArray = new JSONArray();

            for (int i = 1; i <= new_ruta_postion_details.size(); i++) {
                JSONObject destination_id = new JSONObject();
                destination_id.put("id", new_ruta_postion_details.get(String.valueOf(i)).get(0));
                destination_id.put("errand", new_ruta_postion_details.get(String.valueOf(i)).get(1));

                JSONObject location_inside = new JSONObject();
                location_inside.put("id", new_ruta_postion_details.get(String.valueOf(i)).get(2));
                location_inside.put("user", new_ruta_postion_details.get(String.valueOf(i)).get(3));

                destination_id.put("location", location_inside);

                JSONObject geolocation = new JSONObject();
                geolocation.put("latitude", new_ruta_postion_details.get(String.valueOf(i)).get(4));
                geolocation.put("longitude", new_ruta_postion_details.get(String.valueOf(i)).get(5));

                destination_id.put("geolocation", geolocation);

                destination_id.put("name", new_ruta_postion_details.get(String.valueOf(i)).get(6));
                destination_id.put("address", new_ruta_postion_details.get(String.valueOf(i)).get(7));
                destination_id.put("references", new_ruta_postion_details.get(String.valueOf(i)).get(8));
                destination_id.put("community", new_ruta_postion_details.get(String.valueOf(i)).get(9));
                destination_id.put("self_contact", new_ruta_postion_details.get(String.valueOf(i)).get(10));
                destination_id.put("stnumber", new_ruta_postion_details.get(String.valueOf(i)).get(11));
                destination_id.put("contact_name", new_ruta_postion_details.get(String.valueOf(i)).get(12));
                destination_id.put("phone_number1", new_ruta_postion_details.get(String.valueOf(i)).get(13));
                destination_id.put("phone_number2", new_ruta_postion_details.get(String.valueOf(i)).get(14));
                destination_id.put("email", new_ruta_postion_details.get(String.valueOf(i)).get(15));
                destination_id.put("auto_generated", new_ruta_postion_details.get(String.valueOf(i)).get(16));

                JSONObject approximate_geolocation = new JSONObject();
                approximate_geolocation.put("latitude", new_ruta_postion_details.get(String.valueOf(i)).get(17));
                approximate_geolocation.put("longitude", new_ruta_postion_details.get(String.valueOf(i)).get(18));

                destination_id.put("approximate_geolocation", approximate_geolocation);

                destination_id.put("addressee", new_ruta_postion_details.get(String.valueOf(i)).get(19));
                destination_id.put("observation", new_ruta_postion_details.get(String.valueOf(i)).get(20));
                destination_id.put("position", i - 1);
                destination_id.put("contact_name", new_ruta_postion_details.get(String.valueOf(i)).get(22));
                destination_id.put("phone_number1", new_ruta_postion_details.get(String.valueOf(i)).get(23));
                destination_id.put("email", new_ruta_postion_details.get(String.valueOf(i)).get(24));
                destination_id.put("completed", new_ruta_postion_details.get(String.valueOf(i)).get(25));
                destination_id.put("order_number", new_ruta_postion_details.get(String.valueOf(i)).get(26));
                destination_id.put("tracking_number", new_ruta_postion_details.get(String.valueOf(i)).get(27));
                destination_id.put("was_delivered_to_contact", new_ruta_postion_details.get(String.valueOf(i)).get(28));
                destination_id.put("was_delivered_to_watchman", new_ruta_postion_details.get(String.valueOf(i)).get(29));
                destination_id.put("delivery_remarks", new_ruta_postion_details.get(String.valueOf(i)).get(30));
                destination_id.put("delivery_contact_rut", new_ruta_postion_details.get(String.valueOf(i)).get(31));
                destination_id.put("delivery_contact_name", new_ruta_postion_details.get(String.valueOf(i)).get(32));
                destination_id.put("delivery_contact_phone", new_ruta_postion_details.get(String.valueOf(i)).get(33));

                jArray.put(destination_id);
            }

            destinations.put("destinations", jArray);

            final String URL = ConstantAPI.RUTA_API + errand + "/rearrange_errand/";
            Log.e("URL", URL);

            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.PATCH, URL, destinations, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.e("Response", response.toString());
                    String result = response.optString("result");
                    String message = response.optString("message");

                    if (result.equals("1")) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(RutaActivity.this);
                        builder.setTitle("Actualizado Exitosamente");
                        builder.setMessage(message);
                        builder.setPositiveButton("De Acuerdo", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dashboard_datas();
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    } else {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(RutaActivity.this);
                        builder.setTitle("Error");
                        builder.setMessage(message);
                        builder.setPositiveButton("De Acuerdo", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO Auto-generated method stub
                    Log.e("Error", error.toString());

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    user = sessionManager.getUserDetails();
                    headers.put("Authorization", "Token" + " " + user.get("token"));
                    Log.e("params", headers.toString());
                    return headers;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(RutaActivity.this);
            requestQueue.add(jsObjRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void dashboard_datas() {

        final String URL = ConstantAPI.DASHBOARD;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String message = null;
                try {
                    message = response.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final JSONObject job_accepted = response.optJSONObject("job_accepted");

                if (job_accepted != null) {


                    try {
                        String job_accepted_id = job_accepted.optString("id");
                        String status = job_accepted.optString("status");
                        String was_picked = job_accepted.optString("was_picked");

                        String outcome = job_accepted.optString("outcome");
                        String charged_cost_messenger = job_accepted.optString("charged_cost_messenger");
                        String errand_type = job_accepted.optString("errand_type");

                        JSONObject originss = job_accepted.getJSONObject("origin");
                        String community = originss.optString("community");
                        String references = originss.optString("references");
                        String contact_name = originss.optString("contact_name");
                        String phone_number1 = originss.optString("phone_number1");
                        String phone_number2 = originss.optString("phone_number2");
                        String email = originss.optString("email");

                        JSONObject geos = originss.getJSONObject("geolocation");
                        String latitude = geos.optString("latitude");
                        String longitude = geos.optString("longitude");

                        datas.put("id", job_accepted_id);
                        datas.put("outcome", outcome);
                        datas.put("charged_cost_messenger", charged_cost_messenger);
                        datas.put("errand_type", errand_type);
                        datas.put("community", community);
                        datas.put("references", references);
                        datas.put("contact_name", contact_name);
                        datas.put("phone_number1", phone_number1);
                        datas.put("phone_number2", phone_number2);
                        datas.put("email", email);
                        datas.put("latitude", latitude);
                        datas.put("longitude", longitude);

                        all_datas.put("origin", orgins_datas);

                        JSONArray destinationsssss = null;
                        try {
                            destinationsssss = new JSONArray(job_accepted.getString("destinations"));

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        if (destinationsssss.length() == 1) {
                            destination_size = String.valueOf(destinationsssss.length());

                            JSONObject newjsonobjectsss = null;
                            JSONObject locationss = null;
                            try {
                                newjsonobjectsss = (JSONObject) destinationsssss.get(0);
                                job_accepted_destination_id = newjsonobjectsss.optString("id");

                                locationss = newjsonobjectsss.getJSONObject("location");
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                            String community_desti = locationss.optString("community");
                            String references_desti = locationss.optString("references");
                            String contact_name_desti = locationss.optString("contact_name");
                            String phone_number1_desti = locationss.optString("phone_number1");
                            String phone_number2_desti = locationss.optString("phone_number2");
                            String email_desti = locationss.optString("email");

                            JSONObject geo_locationss = locationss.getJSONObject("geolocation");
                            String latitude_desti = geo_locationss.optString("latitude");
                            String longitude_desti = geo_locationss.optString("longitude");

                            String observation = newjsonobjectsss.optString("observation");

                            datas_desti.put("id", job_accepted_destination_id);
                            datas_desti.put("community_desti", community_desti);
                            datas_desti.put("references_desti", references_desti);
                            datas_desti.put("contact_name_desti", contact_name_desti);
                            datas_desti.put("phone_number1_desti", phone_number1_desti);
                            datas_desti.put("phone_number2_desti", phone_number2_desti);
                            datas_desti.put("email_desti", email_desti);
                            datas_desti.put("latitude_desti", latitude_desti);
                            datas_desti.put("longitude_desti", longitude_desti);
                            datas_desti.put("observation", observation);

                            ArrayList<String> desti_values = new ArrayList<>();
                            desti_values.add(job_accepted_destination_id);
                            desti_values.add(community_desti);
                            desti_values.add(references_desti);
                            desti_values.add(contact_name_desti);
                            desti_values.add(phone_number1_desti);
                            desti_values.add(phone_number2_desti);
                            desti_values.add(email_desti);
                            desti_values.add(latitude_desti);
                            desti_values.add(longitude_desti);
                            desti_values.add(String.valueOf(1));

                            desti_values.add(observation);


                            datas_desti_multi.put(destinationsssss.length() - 1, desti_values);
                            new_all_datas.add(datas_desti_multi);

                        } else {
                            destination_size = String.valueOf(destinationsssss.length());
                            for (int l = 0; l < destinationsssss.length(); l++) {

                                JSONObject newjsonobjects = null;
                                JSONObject location = null;
                                try {
                                    newjsonobjects = (JSONObject) destinationsssss.get(l);

                                    job_accepted_destination_id = newjsonobjects.optString("id");

                                    location = newjsonobjects.getJSONObject("location");
                                    String community_desti_multi = location.optString("community");
                                    String references_desti_multi = location.optString("references");
                                    String contact_name_desti_multi = location.optString("contact_name");
                                    String phone_number1_desti_multi = location.optString("phone_number1");
                                    String phone_number2_desti_multi = location.optString("phone_number2");
                                    String email_desti_multi = location.optString("email");

                                    JSONObject geo_location_multi = location.getJSONObject("geolocation");

                                    String latitude_desti_multi = geo_location_multi.optString("latitude");
                                    String longitude_desti_multi = geo_location_multi.optString("longitude");

                                    String observation = newjsonobjects.optString("observation");


                                    multi_lat.add(latitude_desti_multi);
                                    multi_long.add(longitude_desti_multi);

                                    ArrayList<String> desti_values = new ArrayList<>();
                                    desti_values.add(job_accepted_destination_id);
                                    desti_values.add(community_desti_multi);
                                    desti_values.add(references_desti_multi);
                                    desti_values.add(contact_name_desti_multi);
                                    desti_values.add(phone_number1_desti_multi);
                                    desti_values.add(phone_number2_desti_multi);
                                    desti_values.add(email_desti_multi);
                                    desti_values.add(latitude_desti_multi);
                                    desti_values.add(longitude_desti_multi);
                                    desti_values.add(String.valueOf(l + 1));
                                    desti_values.add(observation);

                                    datas_desti_multi.put(l, desti_values);
                                    new_all_datas.add(datas_desti_multi);

                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                        Orgin_destination_identy orgin_destination_identy = new Orgin_destination_identy(RutaActivity.this);
                        orgin_destination_identy.createOrginDatas("origin");

                        if (destinationsssss.length() == 1) {
                            Intent intent = new Intent(RutaActivity.this, MapsActivity.class);
                            intent.putExtra("origin_datas", datas);
                            intent.putExtra("datas_desti", datas_desti);
                            intent.putExtra("errand_ids", job_accepted_id);
                            intent.putExtra("destination_size", destination_size);
                            intent.putExtra("destination_id", job_accepted_destination_id);
                            intent.putExtra("datas_desti_multi", datas_desti_multi);
                            intent.putExtra("status", status);
                            intent.putExtra("was_picked", was_picked);
                            intent.putExtra("json_object", job_accepted.toString());
                            startActivity(intent);
                            RutaActivity.this.finish();
                        } else {
                            Intent intent = new Intent(RutaActivity.this, MapsActivity.class);
                            intent.putExtra("origin_datas", datas);
                            intent.putExtra("datas_desti_multi", datas_desti_multi);
                            intent.putExtra("multi_lat", multi_lat);
                            intent.putExtra("multi_long", multi_long);
                            intent.putExtra("errand_ids", job_accepted_id);
                            intent.putExtra("destination_size", destination_size);
                            intent.putExtra("destination_id", job_accepted_destination_id);
                            intent.putExtra("status", status);
                            intent.putExtra("was_picked", was_picked);
                            intent.putExtra("json_object", job_accepted.toString());
                            startActivity(intent);
                            RutaActivity.this.finish();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e("Error", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                user = sessionManager.getUserDetails();
                headers.put("Authorization", "Token" + " " + user.get("token"));
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(RutaActivity.this);
        requestQueue.add(req);
    }

    private void declin_special_errands() {
        final String URL = ConstantAPI.CANCEL_ERRANDS + errand + "/cancel_errand/";
        Log.e("URL", URL);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PATCH, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String result = response.optString("result");
                String message = response.optString("message");

                if (result.equals("1")){

                    Intent intent = new Intent(RutaActivity.this, MainActivity.class);
                    startActivity(intent);

                }else{


                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e("Error", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                user = sessionManager.getUserDetails();
                headers.put("Authorization", "Token"+ " "+ user.get("token"));
                Log.e("params", headers.toString());
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(RutaActivity.this);
        requestQueue.add(req);
    }
}
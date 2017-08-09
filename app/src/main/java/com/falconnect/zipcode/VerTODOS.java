package com.falconnect.zipcode;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.falconnect.zipcode.Adapter.VerTodosAdapter;
import com.falconnect.zipcode.SessionManager.Orgin_destination_identy;
import com.falconnect.zipcode.SessionManager.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerTODOS extends AppCompatActivity {


    ArrayList<String> destination_array = new ArrayList<>();

    TextView amount_total;
    ListView vertodos_lisview;
    VerTodosAdapter verTodosAdapter;
    String amount;
    Button next;
    String errand_ids;
    HashMap<String, String> datas = new HashMap<>();
    HashMap<String, String> datas_desti = new HashMap<>();
    SessionManager sessionManager;
    HashMap<String, String> user;
    String destination_id, destination_size;

    ArrayList<String> orgins_datas = new ArrayList<>();
    HashMap<String, ArrayList<String>> all_datas = new HashMap<>();
    HashMap<Integer, ArrayList<String>> datas_desti_multi = new HashMap<>();

    ArrayList<HashMap<Integer, ArrayList<String>>> new_all_datas = new ArrayList<>();

    ArrayList<String> multi_lat = new ArrayList<>();
    ArrayList<String> multi_long = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_todos);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initilize();

        errand_ids = getIntent().getStringExtra("errand_id");
        destination_id = getIntent().getStringExtra("destination_id");
        destination_array = getIntent().getStringArrayListExtra("destination_array");
        destination_size = getIntent().getStringExtra("destination_size");
        errand_ids = getIntent().getStringExtra("errand_ids");
        amount = getIntent().getStringExtra("amount");

        amount = amount.replace(".00", "");
        amount_total.setText(amount);

        destination_array = getIntent().getStringArrayListExtra("destination_array");

        Log.e("destination_array", destination_array.toString());

        verTodosAdapter = new VerTodosAdapter(VerTODOS.this, destination_array);
        vertodos_lisview.setAdapter(verTodosAdapter);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data_get();
            }
        });

    }

    public void initilize() {
        sessionManager = new SessionManager(VerTODOS.this);
        user = sessionManager.getUserDetails();
        amount_total = (TextView) findViewById(R.id.amount_total);
        vertodos_lisview = (ListView) findViewById(R.id.destination_listview);

        next = (Button) findViewById(R.id.next);
    }

    public void data_get() {

        final String URL = ConstantAPI.ERRAND_ASSIGN + errand_ids + "/assign_errand/";

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PATCH, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String id = response.optString("id");
                    String outcome = response.optString("outcome");
                    String charged_cost_messenger = response.optString("charged_cost_messenger");
                    String errand_type = response.optString("errand_type");

                    JSONObject origins = response.getJSONObject("origin");
                    String community = origins.optString("community");
                    String references = origins.optString("references");
                    String contact_name = origins.optString("contact_name");
                    String phone_number1 = origins.optString("phone_number1");
                    String phone_number2 = origins.optString("phone_number2");
                    String email = origins.optString("email");

                    JSONObject geo = origins.getJSONObject("geolocation");
                    String latitude = geo.optString("latitude");
                    String longitude = geo.optString("longitude");

                    datas.put("id", id);
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

                    JSONArray destinations = null;

                    try {
                        destinations = new JSONArray(response.getString("destinations"));
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    if (destinations.length() == 1) {
                        JSONObject newjsonobject = null;
                        JSONObject location = null;
                        try {
                            newjsonobject = (JSONObject) destinations.get(0);
                            location = newjsonobject.getJSONObject("location");
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        String community_desti = location.optString("community");
                        String references_desti = location.optString("references");
                        String contact_name_desti = location.optString("contact_name");
                        String phone_number1_desti = location.optString("phone_number1");
                        String phone_number2_desti = location.optString("phone_number2");
                        String email_desti = location.optString("email");

                        JSONObject geo_location = location.getJSONObject("geolocation");
                        String latitude_desti = geo_location.optString("latitude");
                        String longitude_desti = geo_location.optString("longitude");

                        datas_desti.put("community_desti", community_desti);
                        datas_desti.put("references_desti", references_desti);
                        datas_desti.put("contact_name_desti", contact_name_desti);
                        datas_desti.put("phone_number1_desti", phone_number1_desti);
                        datas_desti.put("phone_number2_desti", phone_number2_desti);
                        datas_desti.put("email_desti", email_desti);
                        datas_desti.put("latitude_desti", latitude_desti);
                        datas_desti.put("longitude_desti", longitude_desti);

                        ArrayList<String> desti_values = new ArrayList<>();
                        desti_values.add(community_desti);
                        desti_values.add(references_desti);
                        desti_values.add(contact_name_desti);
                        desti_values.add(phone_number1_desti);
                        desti_values.add(phone_number2_desti);
                        desti_values.add(email_desti);
                        desti_values.add(latitude_desti);
                        desti_values.add(longitude_desti);

                        datas_desti_multi.put(destinations.length() - 1, desti_values);
                        new_all_datas.add(datas_desti_multi);

                    } else {
                        for (int l = 0; l < destinations.length(); l++) {
                            JSONObject newjsonobjects = null;
                            JSONObject location = null;
                            try {
                                newjsonobjects = (JSONObject) destinations.get(l);

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

                                multi_lat.add(latitude_desti_multi);
                                multi_long.add(longitude_desti_multi);

                                ArrayList<String> desti_values = new ArrayList<>();
                                desti_values.add(community_desti_multi);
                                desti_values.add(references_desti_multi);
                                desti_values.add(contact_name_desti_multi);
                                desti_values.add(phone_number1_desti_multi);
                                desti_values.add(phone_number2_desti_multi);
                                desti_values.add(email_desti_multi);
                                desti_values.add(latitude_desti_multi);
                                desti_values.add(longitude_desti_multi);

                                datas_desti_multi.put(l, desti_values);
                                new_all_datas.add(datas_desti_multi);

                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                    Orgin_destination_identy orgin_destination_identy = new Orgin_destination_identy(VerTODOS.this);
                    orgin_destination_identy.createOrginDatas("origin");

                    if (destinations.length() == 1) {
                        Intent intent = new Intent(VerTODOS.this, MapsActivity.class);
                        intent.putExtra("origin_datas", datas);
                        intent.putExtra("datas_desti", datas_desti);
                        intent.putExtra("errand_ids", errand_ids);
                        intent.putExtra("destination_size", destination_size);
                        intent.putExtra("destination_id", destination_id);
                        intent.putExtra("datas_desti_multi", datas_desti_multi);

                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(VerTODOS.this, MapsActivity.class);
                        intent.putExtra("origin_datas", datas);
                        intent.putExtra("datas_desti_multi", datas_desti_multi);
                        intent.putExtra("multi_lat", multi_lat);
                        intent.putExtra("multi_long", multi_long);
                        intent.putExtra("errand_ids", errand_ids);
                        intent.putExtra("destination_size", destination_size);
                        intent.putExtra("destination_id", destination_id);
                        startActivity(intent);

                        Log.e("destination_size", destination_size);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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

        RequestQueue requestQueue = Volley.newRequestQueue(VerTODOS.this);
        requestQueue.add(req);
    }
}

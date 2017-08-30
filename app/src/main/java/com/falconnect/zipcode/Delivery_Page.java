package com.falconnect.zipcode;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.falconnect.zipcode.Adapter.BilleteraListViewAdapter;
import com.falconnect.zipcode.SessionManager.Orgin_destination_identy;
import com.falconnect.zipcode.SessionManager.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Delivery_Page extends AppCompatActivity {

    TextView tv2;
    ProgressBar pBar2;
    int progressStatus;
    int pStatus = 0;
    RelativeLayout close_button;
    Button next;
    ImageView image;
    TextView amount_total, ver_todos;
    TextView origin_text, destination_last;
    ArrayList<String> destination_array = new ArrayList<>();
    ArrayList<String> destination_arrays = new ArrayList<>();
    String origin, amount, destino, errand_type, errand_ids;
    HashMap<String, String> datas = new HashMap<>();
    HashMap<String, String> datas_desti = new HashMap<>();
    SessionManager sessionManager;
    HashMap<String, String> user;
    private boolean isPaused = false;
    ArrayList<String> orgins_datas = new ArrayList<>();
    String destination_id, destination_size;
    HashMap<String,ArrayList<String>> all_datas = new HashMap<>();
    HashMap<Integer,ArrayList<String>> datas_desti_multi = new HashMap<>();
    ArrayList<HashMap<Integer, ArrayList<String>>> new_all_datas = new ArrayList<>();
    ArrayList<String> multi_lat = new ArrayList<>();
    ArrayList<String> multi_long = new ArrayList<>();
    ArrayList<HashMap<String, String>> map_location = new ArrayList<>();
    private Handler handler = new Handler();
    String job_accepted_destination_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.single_delivery);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initilize();
        sessionManager = new SessionManager(Delivery_Page.this);
        user = sessionManager.getUserDetails();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("destinations", "null").apply();
        origin = getIntent().getStringExtra("origin");
        amount = getIntent().getStringExtra("amount");
        errand_type = getIntent().getStringExtra("errand_type");
        destino = getIntent().getStringExtra("destination");
        errand_ids = getIntent().getStringExtra("errand_id");
        destination_id = getIntent().getStringExtra("destination_id");
        destination_array = getIntent().getStringArrayListExtra("destination_array");

        destination_size = getIntent().getStringExtra("destination_size");

        if (destination_array == null) {

        } else {
            destination_arrays.add(origin);
            for (int i = 0; i < destination_array.size(); i++) {
                destination_arrays.add(destination_array.get(i));
            }
        }

        if (errand_type.equals("ENTREGAS")) {
            ver_todos.setVisibility(View.GONE);
            Glide.with(Delivery_Page.this)
                    .load(R.drawable.dashboard_one)
                    .into(image);
        } else {
            ver_todos.setVisibility(View.VISIBLE);
            Glide.with(Delivery_Page.this)
                    .load(R.drawable.dashboard_multi)
                    .into(image);
        }

        if(destination_size == null){
        }else{
            destination_size.equals(destination_size);
        }

        amount = amount.replace(".00", "");
        amount_total.setText(amount);

        origin_text.setText(origin);
        destination_last.setText(destino);
        amount_total.setTypeface(null, Typeface.BOLD);
        origin_text.setTypeface(null, Typeface.BOLD);
        destination_last.setTypeface(null, Typeface.BOLD);

        ////Timer
        final CountDownTimer countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                pStatus = (int) (millisUntilFinished / 1000);
                tv2.setText("" + pStatus + " " + "SEC");
            }

            public void onFinish() {
                tv2.setText("" + "0" + " " + "SEG");
                Delivery_Page.this.finish();
            }
        }.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pBar2.setProgress(progressStatus);
                        }
                    });
                }
            }
        }).start();


        ver_todos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Delivery_Page.this, VerTODOS.class);
                intent.putExtra("destination_array", destination_arrays);
                intent.putExtra("errand_ids", errand_ids);
                intent.putExtra("amount", amount);
                intent.putExtra("destination_id", destination_id);
                intent.putExtra("destination_size", destination_size);
                startActivity(intent);
            }
        });

        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Delivery_Page.this.finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data_get();
            }
        });

    }

    public void initilize() {
        origin_text = (TextView) findViewById(R.id.origin_text);
        destination_last = (TextView) findViewById(R.id.destination_last);
        close_button = (RelativeLayout) findViewById(R.id.close_button);
        next = (Button) findViewById(R.id.next);
        tv2 = (TextView) findViewById(R.id.seconds_reminder);
        pBar2 = (ProgressBar) findViewById(R.id.pb);
        amount_total = (TextView) findViewById(R.id.amount_total);
        ver_todos = (TextView) findViewById(R.id.ver_todos);

        image = (ImageView) findViewById(R.id.images);

    }

    public void data_get() {

        final String URL = ConstantAPI.ERRAND_ASSIGN + errand_ids + "/assign_errand/";

        Log.e("URL", URL);

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
                            job_accepted_destination_id = newjsonobject.optString("id");
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

                        String observation = response.optString("observation");


                        datas_desti.put("job_accepted_destination_id", job_accepted_destination_id);
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

                        datas_desti_multi.put(destinations.length()-1, desti_values);
                        new_all_datas.add(datas_desti_multi);

                    } else {
                        for (int l = 0; l < destinations.length(); l++) {

                            JSONObject newjsonobjects = null;
                            JSONObject location = null;
                            try {
                                newjsonobjects = (JSONObject) destinations.get(l);

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

                                String observation = response.optString("observation");

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
                    Orgin_destination_identy orgin_destination_identy = new Orgin_destination_identy(Delivery_Page.this);
                    orgin_destination_identy.createOrginDatas("origin");

                    if(destinations.length() == 1){
                        Intent intent = new Intent(Delivery_Page.this, MapsActivity.class);
                        intent.putExtra("origin_datas", datas);
                        intent.putExtra("datas_desti", datas_desti);
                        intent.putExtra("errand_ids", errand_ids);
                        intent.putExtra("destination_size", destination_size);
                        intent.putExtra("destination_id", destination_id);
                        intent.putExtra("datas_desti_multi", datas_desti_multi);

                        intent.putExtra("status", "null");
                        intent.putExtra("was_picked", "null");
                        intent.putExtra("json_object", response.toString());
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(Delivery_Page.this, MapsActivity.class);
                        intent.putExtra("origin_datas", datas);
                        intent.putExtra("datas_desti_multi", datas_desti_multi);
                        intent.putExtra("multi_lat", multi_lat);
                        intent.putExtra("multi_long", multi_long);
                        intent.putExtra("errand_ids", errand_ids);
                        intent.putExtra("destination_size", destination_size);
                        intent.putExtra("destination_id", destination_id);

                        intent.putExtra("status", "null");
                        intent.putExtra("was_picked", "null");
                        intent.putExtra("json_object", response.toString());
                        startActivity(intent);

                        Log.e("destination_size", destination_size);
                    }
                }catch (JSONException e) {
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
                headers.put("Authorization", "Token"+ " "+ user.get("token"));
                Log.e("params", headers.toString());
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Delivery_Page.this);
        requestQueue.add(req);
    }
}
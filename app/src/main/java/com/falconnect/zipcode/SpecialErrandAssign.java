package com.falconnect.zipcode;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.falconnect.zipcode.SessionManager.Orgin_destination_identy;
import com.falconnect.zipcode.SessionManager.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SpecialErrandAssign extends AppCompatActivity {

    RelativeLayout accept_button_layout, decline_button_layout;
    TextView gana_amount, errand_type;
    ImageView errand_image, errand_destination_size;
    HashMap<String, String> datas = new HashMap<>();
    HashMap<String,ArrayList<String>> all_datas = new HashMap<>();
    ArrayList<String> orgins_datas = new ArrayList<>();
    String job_accepted_destination_id;
    String charged_cost_messenger, errand_type_string, id, num_destinations;
    HashMap<String, String> datas_desti = new HashMap<>();
    HashMap<Integer,ArrayList<String>> datas_desti_multi = new HashMap<>();
    ArrayList<HashMap<Integer, ArrayList<String>>> new_all_datas = new ArrayList<>();
    ArrayList<String> multi_lat = new ArrayList<>();
    ArrayList<String> multi_long = new ArrayList<>();
    String destination_id;

    SessionManager sessionManager;
    HashMap<String, String> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_errand_assign);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initilize();


        //    "job_queued": {
        //        "charged_cost_messenger": 11231,
        //        "errand_type": "MULTIDESTINOS",
        //        "id": 1602
        //        "num_destinations" = 6;
        //    }

        int[] images = {
                R.drawable.blue_one,
                R.drawable.blue_two,
                R.drawable.blue_three,
                R.drawable.blue_four,
                R.drawable.blue_five,
                R.drawable.blue_six,
                R.drawable.blue_seven,
                R.drawable.blue_eight,
                R.drawable.blue_nine,
                R.drawable.blue_ten
        };


        charged_cost_messenger = getIntent().getStringExtra("charged_cost_messenger");
        errand_type_string = getIntent().getStringExtra("errand_type");
        id = getIntent().getStringExtra("id");
        num_destinations = getIntent().getStringExtra("num_destinations");


        gana_amount.setText(charged_cost_messenger);
        errand_type.setText(errand_type_string);

        errand_destination_size.setBackgroundResource(images[Integer.parseInt(num_destinations) - 1]);

        if (errand_type_string.equals("MULTIDESTINOS")){
            errand_image.setBackgroundResource(R.drawable.entregamultidestinodashboard);
        }else{
            errand_image.setBackgroundResource(R.drawable.entregadashboard);
        }

        accept_button_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assign_special_errands();
            }
        });

        decline_button_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                declin_special_errands();
            }
        });

    }

    private void declin_special_errands() {
        final String URL = ConstantAPI.CANCEL_ERRANDS + id + "/cancel_errand/";

        Log.e("URL", URL);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PATCH, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String result = response.optString("result");
                String message = response.optString("message");

                if (result.equals("1")){

                    Intent intent = new Intent(SpecialErrandAssign.this, MainActivity.class);
                    startActivity(intent);

                }else{
                    final Dialog dialog = new Dialog(SpecialErrandAssign.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.status_active_custom_alert);
                    TextView messages = (TextView) dialog.findViewById(R.id.message);
                    messages.setText("¿Seguro que quieres cancelar el encargo?");
                    messages.setAllCaps(true);

                    TextView message1 = (TextView) dialog.findViewById(R.id.message1);
                    message1.setText(message);

                    final Button positive_button = (Button) dialog.findViewById(R.id.possitive_button);
                    positive_button.setText("SI, ESTOY SEGURO");
                    positive_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
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

        RequestQueue requestQueue = Volley.newRequestQueue(SpecialErrandAssign.this);
        requestQueue.add(req);
    }

    private void assign_special_errands() {

        final String URL = ConstantAPI.ERRAND_ASSIGN + id + "/assign_errand/";

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

                        datas_desti.put("job_accepted_destination_id", job_accepted_destination_id);
                        datas_desti.put("community_desti", community_desti);
                        datas_desti.put("references_desti", references_desti);
                        datas_desti.put("contact_name_desti", contact_name_desti);
                        datas_desti.put("phone_number1_desti", phone_number1_desti);
                        datas_desti.put("phone_number2_desti", phone_number2_desti);
                        datas_desti.put("email_desti", email_desti);
                        datas_desti.put("latitude_desti", latitude_desti);
                        datas_desti.put("longitude_desti", longitude_desti);

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

                                datas_desti_multi.put(l, desti_values);
                                new_all_datas.add(datas_desti_multi);

                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }

                        }
                    }
                    Orgin_destination_identy orgin_destination_identy = new Orgin_destination_identy(SpecialErrandAssign.this);
                    orgin_destination_identy.createOrginDatas("origin");

                    if(destinations.length() == 1){
                        Intent intent = new Intent(SpecialErrandAssign.this, MapsActivity.class);
                        intent.putExtra("origin_datas", datas);
                        intent.putExtra("datas_desti", datas_desti);
                        intent.putExtra("errand_ids", id);
                        intent.putExtra("destination_size", num_destinations);
                        intent.putExtra("destination_id", destination_id);
                        intent.putExtra("datas_desti_multi", datas_desti_multi);

                        intent.putExtra("status", "null");
                        intent.putExtra("was_picked", "null");
                        intent.putExtra("json_object", response.toString());
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(SpecialErrandAssign.this, MapsActivity.class);
                        intent.putExtra("origin_datas", datas);
                        intent.putExtra("datas_desti_multi", datas_desti_multi);
                        intent.putExtra("multi_lat", multi_lat);
                        intent.putExtra("multi_long", multi_long);
                        intent.putExtra("errand_ids", id);
                        intent.putExtra("destination_size", num_destinations);
                        intent.putExtra("destination_id", destination_id);

                        intent.putExtra("status", "null");
                        intent.putExtra("was_picked", "null");
                        intent.putExtra("json_object", response.toString());
                        startActivity(intent);

                        Log.e("destination_size", num_destinations);
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

        RequestQueue requestQueue = Volley.newRequestQueue(SpecialErrandAssign.this);
        requestQueue.add(req);
    }

    public void initilize(){
        sessionManager = new SessionManager(SpecialErrandAssign.this);

        gana_amount = (TextView) findViewById(R.id.gana_amount);
        errand_type = (TextView) findViewById(R.id.errand_type);

        errand_image = (ImageView) findViewById(R.id.errand_image);
        errand_destination_size = (ImageView) findViewById(R.id.errand_destination_size);

        accept_button_layout = (RelativeLayout) findViewById(R.id.accept_button_layout);
        decline_button_layout = (RelativeLayout) findViewById(R.id.decline_button_layout);

    }
}

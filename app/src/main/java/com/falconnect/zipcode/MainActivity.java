package com.falconnect.zipcode;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.falconnect.zipcode.Adapter.HomeListViewAdapter;
import com.falconnect.zipcode.Adapter.NavigationDrawerAdapter;
import com.falconnect.zipcode.Navigation.NavigationDrawer;
import com.falconnect.zipcode.SessionManager.Orgin_destination_identy;
import com.falconnect.zipcode.SessionManager.SessionManager;
import com.navdrawer.SimpleSideDrawer;
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    HashMap<String, String> datas = new HashMap<>();
    HashMap<String, String> datas_desti = new HashMap<>();
    ArrayList<String> orgins_datas = new ArrayList<>();
    String destination_id, destination_size;
    HashMap<String,ArrayList<String>> all_datas = new HashMap<>();
    HashMap<Integer,ArrayList<String>> datas_desti_multi = new HashMap<>();
    ArrayList<HashMap<Integer, ArrayList<String>>> new_all_datas = new ArrayList<>();
    ArrayList<String> multi_lat = new ArrayList<>();
    ArrayList<String> multi_long = new ArrayList<>();

    String job_accepted_destination_id;


    //////////////
    ListView listview;
    TextView rate_main;
    ArrayList<String> origins = new ArrayList<>();
    ArrayList<String> amount = new ArrayList<>();
    ArrayList<String> errand_types = new ArrayList<>();

    HomeListViewAdapter homeListViewAdapter;
    ImageView nav_icon_drawer;
    SimpleSideDrawer mNav;
    ListView listnew;
    ArrayList<HashMap<String, String>> destination_list;
    HashMap<Integer, ArrayList<String>> destinationsss;

    //ProgressDialog
    ProgressDialog barProgressDialog;

    ArrayList<String> destination_single = new ArrayList<>();
    ArrayList<String> destination_multi_values = new ArrayList<>();
    ArrayList<String> destination_multi = new ArrayList<>();
    RelativeLayout header_search_list, second_rate;
    ArrayList<String> errandids = new ArrayList<>();
    ArrayList<String> destinatio_ids = new ArrayList<>();
    boolean status;
    RelativeLayout second_layout;

    ///Navigation Items
    RelativeLayout profile_page;
    ImageView profile_pic;
    TextView profile_name;
    ToggleButton toggleButton, single_status, multi_status;
    String id, token, idoc, mobile_number, photo, bank_code, account_num, payment, bank_name, busy, available_for_deliveries, available_for_multiple;;
    String main_id, username, firstname, lastname, main_email, balance;
    SessionManager sessionManager;
    HashMap<String, String> user;
    boolean status_active;
    boolean status_active_multi;

    ///Listview click event
    RelativeLayout third, refresh;

    //Permission
    public static final int RequestPermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        intialize();

        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {
                        WRITE_EXTERNAL_STORAGE,
                        ACCESS_FINE_LOCATION,
                        ACCESS_COARSE_LOCATION,
                        CALL_PHONE
                }, RequestPermissionCode);


        //Progress Bar
        barProgressDialog = ProgressDialog.show(MainActivity.this, "Cargando...", "Por Favor Espera...", true);

        sessionManager = new SessionManager(MainActivity.this);
        user = sessionManager.getUserDetails();

        header_search_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        second_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        String val = user.get("balance");
        val = val.replace(".0", "");
        rate_main.setText(val);

        mNav = new SimpleSideDrawer(this);
        mNav.setLeftBehindContentView(R.layout.navigation_drawer);

        //SIDE MENU DRAWER
        nav_icon_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNav.toggleLeftDrawer();
            }
        });

        //NAVIGATE TO PROFILE SCREEN
        profile_page = (RelativeLayout) mNav.findViewById(R.id.profile_page);
        profile_name = (TextView) mNav.findViewById(R.id.profile_name);

        profile_name.setText(user.get("first_name") + " " + user.get("last_name"));

        profile_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                mNav.closeLeftSide();
                MainActivity.this.finish();
            }
        });

        profile_pic = (ImageView) mNav.findViewById(R.id.profile_avatar);

        if (user.get("photo").isEmpty()) {
            Glide.with(MainActivity.this)
                    .load(R.drawable.default_avatar)
                    .into(profile_pic);
        } else {
            Glide.with(MainActivity.this)
                    .load(R.drawable.default_avatar)
                    .into(profile_pic);
        }

        second_layout = (RelativeLayout) mNav.findViewById(R.id.second_layout);
        second_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNav.closeLeftSide();
            }
        });

        ////TOGGLE BUTTON
        toggleButton = (ToggleButton) mNav.findViewById(R.id.toggleButton1);
        single_status = (ToggleButton) mNav.findViewById(R.id.single_status);
        multi_status = (ToggleButton) mNav.findViewById(R.id.multi_status);

        if (user.get("busy").equals("true")) {
            toggleButton.setChecked(false);
        } else {
            toggleButton.setChecked(true);
        }

        if (user.get("available_for_deliveries").equals("true") && user.get("available_for_multiple").equals("true")) {
            single_status.setChecked(true);
            multi_status.setChecked(true);
        } else if(user.get("available_for_deliveries").equals("true") && user.get("available_for_multiple").equals("false")){
            single_status.setChecked(true);
            multi_status.setChecked(false);
        } else if(user.get("available_for_deliveries").equals("false") && user.get("available_for_multiple").equals("true")){
            single_status.setChecked(false);
            multi_status.setChecked(true);
        }

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if (isChecked == true) {
                    user = sessionManager.getUserDetails();
                    if (user.get("busy").equals("true")) {
                        status = false;
                        Log.e("trueeeee", "false");
                        status_online_offline();
                    } else if (user.get("busy").equals("false")) {
                        status = true;
                        Log.e("trueeeee", "true");
                        status_online_offline();
                    }
                } else {
                    final Dialog dialog = new Dialog(MainActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.status_active_custom_alert);
                    Button positive_button = (Button) dialog.findViewById(R.id.possitive_button);
                    positive_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            user = sessionManager.getUserDetails();
                            if (user.get("busy").equals("true")) {
                                status = false;
                                Log.e("trueeeee", "false");
                                status_online_offline();
                            } else if (user.get("busy").equals("false")) {
                                status = true;
                                Log.e("trueeeee", "true");
                                status_online_offline();
                            }
                            dialog.dismiss();
                        }
                    });
                    Button negative_button = (Button) dialog.findViewById(R.id.negative_button);
                    negative_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toggleButton.setChecked(true);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }

            }
        });

        single_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    user = sessionManager.getUserDetails();
                    if (user.get("available_for_deliveries").equals("true")) {
                        status_active = false;
                        status_active_multi = true;
                        multi_status.setChecked(true);
                        Log.e("trueeeee", "false");
                        status_check_multi();
                    } else if (user.get("available_for_deliveries").equals("false")) {
                        status_active = true;
                        status_active_multi = false;
                        multi_status.setChecked(false);
                        Log.e("trueeeee", "true");
                        status_check_multi();
                    }
                }else{
                    user = sessionManager.getUserDetails();
                    if (user.get("available_for_deliveries").equals("true")) {
                        status_active = false;
                        status_active_multi = true;
                        multi_status.setChecked(true);
                        Log.e("trueeeee", "false");
                        status_check_multi();
                    } else if (user.get("available_for_deliveries").equals("false")) {
                        status_active = true;
                        status_active_multi = false;
                        multi_status.setChecked(false);
                        Log.e("trueeeee", "true");
                        status_check_multi();
                    }
                }
            }
        });

        multi_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    user = sessionManager.getUserDetails();
                    if (user.get("available_for_multiple").equals("true")) {
                        status_active_multi = false;
                        status_active = true;
                        single_status.setChecked(true);
                        Log.e("trueeeee", "false");
                        status_check_multi();
                    } else if (user.get("available_for_multiple").equals("false")) {
                        status_active_multi = true;
                        status_active = false;
                        single_status.setChecked(false);
                        Log.e("trueeeee", "true");
                        status_check_multi();
                    }
                }else {
                    user = sessionManager.getUserDetails();
                    if (user.get("available_for_multiple").equals("true")) {
                        status_active_multi = false;
                        status_active = true;
                        single_status.setChecked(true);
                        Log.e("trueeeee", "false");
                        status_check_multi();
                    } else if (user.get("available_for_multiple").equals("false")) {
                        status_active_multi = true;
                        status_active = false;
                        single_status.setChecked(false);
                        Log.e("trueeeee", "true");
                        status_check_multi();
                    }
                }
            }
        });

        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Progress Bar
                barProgressDialog = ProgressDialog.show(MainActivity.this, "Cargando...", "Por Favor Espera...", true);
                dashboard_datas();
                origins.clear();
                errandids.clear();
                destinatio_ids.clear();
                destination_single.clear();
                destination_multi_values.clear();
                destinationsss.clear();
                amount.clear();
                errand_types.clear();
            }
        });

        //NAVIGATION DRAWER LISTVIEW
        NavigationDrawerAdapter adapter = new NavigationDrawerAdapter(MainActivity.this, NavigationDrawer.web, NavigationDrawer.imageId);
        listnew = (ListView) mNav.findViewById(R.id.nav_list_view);
        listnew.setAdapter(adapter);

        dashboard_datas();


    }

    private void status_check_multi() {
        final String URL = ConstantAPI.REGISTER_API + user.get("id") + "/";
        Log.e("url", URL);
        JSONObject jsonObject_user = new JSONObject();
        try {
            jsonObject_user.put("available_for_deliveries", status_active);
            jsonObject_user.put("available_for_multiple", status_active_multi);
            Log.e("jsonObject_user", jsonObject_user.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.PATCH, URL, jsonObject_user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    id = response.getString("id");
                    token = response.getString("token");
                    idoc = response.getString("idoc");
                    mobile_number = response.getString("mobile_number");
                    photo = response.getString("photo");
                    bank_code = response.getString("bank_code");
                    bank_name = response.getString("bank_name");
                    account_num = response.getString("rut_account_number");
                    payment = response.getString("want_payment");
                    busy = response.getString("busy");
                    available_for_deliveries = response.getString("available_for_deliveries");
                    available_for_multiple = response.getString("available_for_multiple");

                    JSONObject jsonArray = response.getJSONObject("user");

                    main_id = jsonArray.getString("id");
                    username = jsonArray.getString("username");
                    firstname = jsonArray.getString("first_name");
                    lastname = jsonArray.getString("last_name");
                    main_email = jsonArray.getString("email");
                    balance = jsonArray.getString("balance");

                    sessionManager.clear_user();

                    sessionManager.createLoginSession(id, token, idoc, mobile_number,
                            photo, bank_name, bank_code, account_num, payment,
                            main_id, username, firstname, lastname, main_email, balance, available_for_deliveries, available_for_multiple, busy);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub

            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jsObjRequest);
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

                if (job_accepted == null){

                    String errand = null;
                    String destinations_id = null;

                    destinationsss = new HashMap<>();
                    try {
                        JSONArray jsonArray = response.getJSONArray("data");

                        destination_list = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonobject = (JSONObject) jsonArray.get(i);

                            String id = jsonobject.optString("id");
                            String outcome = jsonobject.optString("outcome");
                            String charged_cost_messenger = jsonobject.optString("charged_cost_messenger");
                            String errand_type = jsonobject.optString("errand_type");

                            JSONObject newjsonobjects = (JSONObject) jsonArray.get(i);
                            JSONObject origin = newjsonobjects.getJSONObject("origin");
                            String community = origin.optString("community");

                            amount.add(charged_cost_messenger);
                            origins.add(community);
                            errand_types.add(errand_type);
                        }
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONArray destinations = new JSONArray(jsonArray.getJSONObject(j).getString("destinations"));
                            if (destinations.length() == 1) {
                                JSONObject newjsonobject = (JSONObject) destinations.get(0);
                                destinations_id = newjsonobject.optString("id");
                                errand = newjsonobject.optString("errand");
                                destinatio_ids.add(destinations_id);
                                errandids.add(errand);

                                JSONObject location = newjsonobject.getJSONObject("location");
                                String desti_community = location.optString("community");
                                destination_single.add(desti_community);
                                destination_multi_values.add("1");
                                ArrayList<String> val = new ArrayList<>();
                                val.add("0");
                                destinationsss.put(j, val);
                            } else {
                                destination_multi.clear();
                                for (int l = 0; l < destinations.length(); l++) {
                                    JSONObject newjsonobjects = (JSONObject) destinations.get(l);
                                    destinations_id = newjsonobjects.optString("id");
                                    errand = newjsonobjects.optString("errand");

                                    JSONObject location = newjsonobjects.getJSONObject("location");
                                    String desti_community = location.optString("community");
                                    destination_multi.add(desti_community);
                                    destinationsss.put(j, destination_multi);
                                }
                                errandids.add(errand);
                                destinatio_ids.add(destinations_id);
                                destination_single.add("0");
                                destination_multi_values.add(destinations.length() + " " + "DESTINOS");
                            }
                        }

                        barProgressDialog.dismiss();

                       /* if (homeListViewAdapter == null){

                            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage(message);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();

                        }else{

                        }*/
                        homeListViewAdapter = new HomeListViewAdapter(MainActivity.this, origins, errandids, destinatio_ids, destination_single, destination_multi_values, destinationsss, amount, errand_types);
                        listview.setAdapter(homeListViewAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Log.e("errands", "null");
                    try {
                        String job_accepted_id = job_accepted.optString("id");
                        String status =  job_accepted.optString("status");
                        String was_picked =  job_accepted.optString("was_picked");

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

                            datas_desti.put("id", job_accepted_destination_id);
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
                        Orgin_destination_identy orgin_destination_identy = new Orgin_destination_identy(MainActivity.this);
                        orgin_destination_identy.createOrginDatas("origin");

                        barProgressDialog.dismiss();

                        if (destinationsssss.length() == 1) {
                            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
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
                        } else {
                            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
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

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ///else end

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

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(req);

    }

    @Override
    public void onRestart() {
        super.onRestart();
        startActivity(getIntent());
        finish();
    }

    private void status_online_offline() {

        final String URL = ConstantAPI.REGISTER_API + user.get("id") + "/";
        JSONObject jsonObject_user = new JSONObject();
        try {
            jsonObject_user.put("busy", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.PATCH, URL, jsonObject_user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    id = response.getString("id");
                    token = response.getString("token");
                    idoc = response.getString("idoc");
                    mobile_number = response.getString("mobile_number");
                    photo = response.getString("photo");
                    bank_code = response.getString("bank_code");
                    bank_name = response.getString("bank_name");
                    account_num = response.getString("rut_account_number");
                    payment = response.getString("want_payment");
                    busy = response.getString("busy");
                    available_for_deliveries = response.getString("available_for_deliveries");
                    available_for_multiple = response.getString("available_for_multiple");

                    JSONObject jsonArray = response.getJSONObject("user");

                    main_id = jsonArray.getString("id");
                    username = jsonArray.getString("username");
                    firstname = jsonArray.getString("first_name");
                    lastname = jsonArray.getString("last_name");
                    main_email = jsonArray.getString("email");
                    balance = jsonArray.getString("balance");

                    sessionManager.clear_user();

                    sessionManager.createLoginSession(id, token, idoc, mobile_number,
                            photo, bank_name, bank_code, account_num, payment,
                            main_id, username, firstname, lastname, main_email, balance, available_for_deliveries, available_for_multiple, busy);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub

            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jsObjRequest);
    }

    public void intialize() {
        header_search_list = (RelativeLayout) findViewById(R.id.header_search_list);
        second_rate = (RelativeLayout) findViewById(R.id.second_rate);
        nav_icon_drawer = (ImageView) findViewById(R.id.nav_icon_drawer);
        listview = (ListView) findViewById(R.id.listcontents);
        rate_main = (TextView) findViewById(R.id.rate_main);
        third = (RelativeLayout) findViewById(R.id.third);
        refresh = (RelativeLayout) findViewById(R.id.refresh);



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean Camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadPhoneStatePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean Network_state = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean Phone_state = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                }

                break;
        }
    }
}


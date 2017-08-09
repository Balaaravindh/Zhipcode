package com.falconnect.zipcode;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.falconnect.zipcode.Adapter.HomeListViewAdapter;
import com.falconnect.zipcode.Adapter.NavigationDrawerAdapter;
import com.falconnect.zipcode.Adapter.ProfileListViewAdapter;
import com.falconnect.zipcode.Navigation.NavigationDrawer;
import com.falconnect.zipcode.SessionManager.SessionManager;
import com.navdrawer.SimpleSideDrawer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    RelativeLayout header, second_rate;
    TextView profile_name;
    TextView profile_names;
    ImageView profile_pic;
    RelativeLayout profile_page;
    RelativeLayout second_layout;
    ListView profile_listview;
    ArrayList<String> profile_details;
    ArrayList<Integer> profile_details_image;
    ProfileListViewAdapter profileListViewAdapter;
    Button cerrar_sesion;
    ImageView edit_name_image;

    ImageView nav_icon_drawer;
    ToggleButton toggleButton, single_status, multi_status;

    String id, token, idoc, mobile_number, photo, bank_code, bank_name, account_num, busy, payment, available_for_deliveries, available_for_multiple;;;
    String main_id, username, firstname, lastname, main_email, balance;

    boolean status;
    SessionManager sessionManager;
    HashMap<String, String> user;
    SimpleSideDrawer mNav;
    ListView listnew;
    boolean status_active;
    boolean status_active_multi;
    FrameLayout frame_layout;

    //ProgressDialog
    ProgressDialog barProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        intialize();

        barProgressDialog = ProgressDialog.show(ProfileActivity.this, "Cargando...", "Por Favor Espera...", true);

        sessionManager = new SessionManager(ProfileActivity.this);
        user = sessionManager.getUserDetails();

        ////////////////////////////NAVIGATION DRAWER//////////////////////////
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
        profile_names = (TextView) mNav.findViewById(R.id.profile_name);

        profile_names.setText(user.get("first_name") + " " + user.get("last_name"));


        profile_pic = (ImageView) mNav.findViewById(R.id.profile_avatar);

        if(user.get("photo").isEmpty()){
            Glide.with(ProfileActivity.this)
                    .load(R.drawable.default_avatar)
                    .into(profile_pic);
        }else{
            Glide.with(ProfileActivity.this)
                    .load(R.drawable.default_avatar)
                    .into(profile_pic);
        }

        profile_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNav.closeLeftSide();
            }
        });

        second_layout = (RelativeLayout) mNav.findViewById(R.id.second_layout);
        second_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                mNav.closeLeftSide();
            }
        });

        ////TOGGLE BUTTON
        toggleButton = (ToggleButton) mNav.findViewById(R.id.toggleButton1);
        single_status = (ToggleButton) mNav.findViewById(R.id.single_status);
        multi_status = (ToggleButton) mNav.findViewById(R.id.multi_status);

        if (user.get("busy").equals("true")) {
            toggleButton.setChecked(true);
        }else{
            toggleButton.setChecked(false);
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
                    final Dialog dialog = new Dialog(ProfileActivity.this);
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

        //NAVIGATION DRAWER LISTVIEW
        NavigationDrawerAdapter adapter = new NavigationDrawerAdapter(ProfileActivity.this, NavigationDrawer.web, NavigationDrawer.imageId);
        listnew = (ListView) mNav.findViewById(R.id.nav_list_view);
        listnew.setAdapter(adapter);

        //////////////END OF NAVIGATION DRAWER////////////////////////////////

        frame_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        second_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        profile_name.setTypeface(null, Typeface.BOLD);

        profile_details_image = new ArrayList<>();

        profile_details_image.add(R.drawable.phonealt);
        profile_details_image.add(R.drawable.passwordsecure);
        profile_details_image.add(R.drawable.mailprofile);

        profile_details = new ArrayList<>();

        final String URL = ConstantAPI.PROFILE_API + user.get("id") + "/";
        Log.e("URL", URL);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, URL, null,  new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.e("Response", response.toString());
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

                    JSONObject jsonArray = response.getJSONObject("user");

                    main_id = jsonArray.getString("id");
                    username = jsonArray.getString("username");
                    firstname = jsonArray.getString("first_name");
                    lastname = jsonArray.getString("last_name");
                    main_email = jsonArray.getString("email");
                    balance = jsonArray.getString("balance");


                    barProgressDialog.dismiss();

                    profile_details.add(mobile_number);
                    profile_details.add("CAMBIAR CONTRASENA");
                    profile_details.add(main_email);

                    profile_name.setText(firstname + " " + lastname);

                    profileListViewAdapter = new ProfileListViewAdapter(ProfileActivity.this, profile_details, profile_details_image);
                    profile_listview.setAdapter(profileListViewAdapter);

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub

            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
        requestQueue.add(jsObjRequest);

        profile_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    Log.e("POSITION ---->", String.valueOf(position));
                    Intent intent = new Intent(ProfileActivity.this, EditProfileNumberActivity.class);
                    intent.putExtra("mobile_number", mobile_number);
                    startActivity(intent);
                    Log.d("mobile_number", mobile_number);
                }else if (position == 1){
                    Log.e("POSITION ---->", String.valueOf(position));
                    Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
                    startActivity(intent);
                }else if (position == 2){
                    Log.e("POSITION ---->", String.valueOf(position));
                    Intent intent = new Intent(ProfileActivity.this, ChangeEmailidActivity.class);
                    intent.putExtra("email", main_email);
                    startActivity(intent);
                    Log.d("email", main_email);
                }else{
                    Log.e("POSITION ---->", "NULL");
                }
            }
        });

        edit_name_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileNameActivity.class);
                intent.putExtra("first_name", firstname);
                intent.putExtra("last_name", lastname);
                startActivity(intent);
            }
        });


        //Button Click Listner
        cerrar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(ProfileActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.profile_custom_alert);

                Button positive_button = (Button) dialog.findViewById(R.id.possitive_button);
                positive_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        sessionManager.clear();
                    }
                });
                Button negative_button = (Button) dialog.findViewById(R.id.negative_button);
                negative_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

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


        RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
        requestQueue.add(jsObjRequest);
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
                            photo, bank_name, bank_code,account_num, payment,
                            main_id, username, firstname, lastname, main_email, balance, available_for_deliveries,available_for_multiple,busy );


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

        RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
        requestQueue.add(jsObjRequest);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        startActivity(getIntent());
        finish();
    }

    public void intialize(){

        header = (RelativeLayout) findViewById(R.id.header_search_list);
        second_rate = (RelativeLayout) findViewById(R.id.second_rate);
        profile_name = (TextView) findViewById(R.id.profile_name2);
        nav_icon_drawer = (ImageView) findViewById(R.id.nav_icon_drawer);
        cerrar_sesion = (Button) findViewById(R.id.cerrar_sesion);

        edit_name_image = (ImageView) findViewById(R.id.edit_name_image);
        frame_layout = (FrameLayout) findViewById(R.id.frame_layout);
        profile_listview = (ListView) findViewById(R.id.profile_listview);

    }

}

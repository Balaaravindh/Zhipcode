package com.falconnect.zipcode;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.falconnect.zipcode.Adapter.NavigationDrawerAdapter;
import com.falconnect.zipcode.Adapter.ProfileListViewAdapter;
import com.falconnect.zipcode.Navigation.NavigationDrawer;
import com.falconnect.zipcode.SessionManager.SessionManager;
import com.navdrawer.SimpleSideDrawer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RankingActivity extends AppCompatActivity {

    ///Navigation Items
    RelativeLayout profile_page;
    ImageView profile_pic;
    TextView profile_name;
    TextView second_name, second_name_rate;
    ToggleButton toggleButton, single_status, multi_status;

    SessionManager sessionManager;
    HashMap<String, String> user;

    ImageView nav_icon_drawer;
    RelativeLayout second_layout;
    boolean status_active;
    boolean status_active_multi;
    SimpleSideDrawer mNav;
    ListView listnew;
    boolean status;
    String id, token, idoc, mobile_number, photo, address, bank_code, account_num, payment, bank_name, busy, available_for_deliveries, available_for_multiple;;
    String main_id, username, firstname, lastname, main_email, balance;
    RelativeLayout header_search_list, ranking_titlesss;
    String positives, percentage, negatives;
    FrameLayout frame_layout;
    TextView rank_rate, rank_disrate, ranking_rate;
    ImageView white_image;
    ProgressDialog barProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initilize();

        //Progress Bar
        barProgressDialog = ProgressDialog.show(RankingActivity.this, "Cargando...", "Por Favor Espera...", true);

        ///////////////////Navigation Drawer////////////////////////
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
        second_name = (TextView) mNav.findViewById(R.id.second_name);

        profile_name.setText(user.get("first_name") + " " + user.get("last_name"));
        second_name.setText(user.get("first_name") + " " + user.get("last_name"));

        second_name_rate = (TextView) mNav.findViewById(R.id.second_name_rate);
        String vals = user.get("balance");
        vals = vals.replace(".0", "");
        second_name_rate.setText(vals);


        profile_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RankingActivity.this, ProfileActivity.class);
                startActivity(intent);
                mNav.closeLeftSide();
                RankingActivity.this.finish();
            }
        });

        second_layout = (RelativeLayout) mNav.findViewById(R.id.second_layout);
        second_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RankingActivity.this, MainActivity.class);
                startActivity(intent);
                mNav.closeLeftSide();
                RankingActivity.this.finish();
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

        ranking_titlesss = (RelativeLayout) findViewById(R.id.ranking_titlesss);
        ranking_titlesss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

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

        frame_layout = (FrameLayout) findViewById(R.id.frame_layout);
        frame_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if (isChecked == true) {
                    single_status.setEnabled(true);
                    multi_status.setEnabled(true);
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
                    final Dialog dialog = new Dialog(RankingActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.status_active_custom_alert);
                    Button positive_button = (Button) dialog.findViewById(R.id.possitive_button);
                    positive_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            single_status.setEnabled(false);
                            multi_status.setEnabled(false);
                            status_active_multi = false;
                            single_status.setChecked(false);
                            status_active_multi = false;
                            multi_status.setChecked(false);
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
//                        status_active_multi = true;
//                        multi_status.setChecked(true);
                        Log.e("trueeeee", "false");
                        status_check_multi();
                    } else if (user.get("available_for_deliveries").equals("false")) {
                        status_active = true;
//                        status_active_multi = false;
//                        multi_status.setChecked(false);
                        Log.e("trueeeee", "true");
                        status_check_multi();
                    }
                }else{
                    user = sessionManager.getUserDetails();
                    if (user.get("available_for_deliveries").equals("true")) {
                        status_active = false;
//                        status_active_multi = true;
//                        multi_status.setChecked(true);
                        Log.e("trueeeee", "false");
                        status_check_multi();
                    } else if (user.get("available_for_deliveries").equals("false")) {
                        status_active = true;
//                        status_active_multi = false;
//                        multi_status.setChecked(false);
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
//                        status_active = true;
//                        single_status.setChecked(true);
                        Log.e("trueeeee", "false");
                        status_check_multi();
                    } else if (user.get("available_for_multiple").equals("false")) {
                        status_active_multi = true;
//                        status_active = false;
//                        single_status.setChecked(false);
                        Log.e("trueeeee", "true");
                        status_check_multi();
                    }
                }else {
                    user = sessionManager.getUserDetails();
                    if (user.get("available_for_multiple").equals("true")) {
                        status_active_multi = false;
                        //status_active = true;
                        //single_status.setChecked(true);
                        Log.e("trueeeee", "false");
                        status_check_multi();
                    } else if (user.get("available_for_multiple").equals("false")) {
                        status_active_multi = true;
                        //status_active = false;
                        //single_status.setChecked(false);
                        Log.e("trueeeee", "true");
                        status_check_multi();
                    }
                }
            }
        });

        //NAVIGATION DRAWER LISTVIEW
        NavigationDrawerAdapter adapter = new NavigationDrawerAdapter(RankingActivity.this, NavigationDrawer.web, NavigationDrawer.imageId);
        listnew = (ListView) mNav.findViewById(R.id.nav_list_view);
        listnew.setAdapter(adapter);
        ////////////End of Nabigation Drawer//////////

        header_search_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //Ranking API
        final String URL = ConstantAPI.PROFILE_API + user.get("id") + "/";
        Log.e("URL", URL);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, URL, null,  new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Response", response.toString());
                try {
                    JSONObject jsonArray = response.getJSONObject("ranking");

                    positives = jsonArray.getString("positives");
                    percentage = jsonArray.getString("percentage");
                    negatives = jsonArray.getString("negatives");

                    white_image.setVisibility(View.GONE);
                    barProgressDialog.dismiss();

                    rank_rate.setText(positives + " " + "POSITIVOS");
                    ranking_rate.setText(percentage);
                    rank_disrate.setText(negatives + " " + "NEGATIVOS");

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

        RequestQueue requestQueue = Volley.newRequestQueue(RankingActivity.this);
        requestQueue.add(jsObjRequest);

    }

    public void initilize(){
        sessionManager = new SessionManager(RankingActivity.this);
        user = sessionManager.getUserDetails();
        nav_icon_drawer = (ImageView) findViewById(R.id.nav_icon_drawer);
        header_search_list = (RelativeLayout) findViewById(R.id.header_search_list);

        rank_rate = (TextView) findViewById(R.id.rank_rate);
        rank_disrate = (TextView) findViewById(R.id.rank_disrate);
        ranking_rate = (TextView) findViewById(R.id.ranking_rate);
        white_image = (ImageView) findViewById(R.id.white_image);
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

        RequestQueue requestQueue = Volley.newRequestQueue(RankingActivity.this);
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

        RequestQueue requestQueue = Volley.newRequestQueue(RankingActivity.this);
        requestQueue.add(jsObjRequest);
    }

}

package com.falconnect.zipcode;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.falconnect.zipcode.Adapter.BilleteraListViewAdapter;
import com.falconnect.zipcode.Adapter.HistorialListViewAdapter;
import com.falconnect.zipcode.Adapter.NavigationDrawerAdapter;
import com.falconnect.zipcode.Navigation.NavigationDrawer;
import com.falconnect.zipcode.SessionManager.Orgin_destination_identy;
import com.falconnect.zipcode.SessionManager.SessionManager;
import com.navdrawer.SimpleSideDrawer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HistrorialActivity extends AppCompatActivity {

    ///Navigation Items
    RelativeLayout profile_page;
    ImageView profile_pic;
    TextView profile_name;

    ToggleButton toggleButton, single_status, multi_status;
    SessionManager sessionManager;
    HashMap<String, String> user;
    ProgressDialog barProgressDialog;

    FrameLayout frame_layout;
    View view_top;
    ImageView nav_icon_drawer;
    RelativeLayout second_layout;
    boolean status_active;
    boolean status_active_multi;
    SimpleSideDrawer mNav;
    ListView listnew;
    boolean status;
    String id, token, idoc, mobile_number, photo, address, bank_code, account_num, payment, bank_name, busy, available_for_deliveries, available_for_multiple;
    String main_id, username, firstname, lastname, main_email, balance;
    LinearLayout tabless;
    RelativeLayout header_search_list;
    ListView historial_datas;

    ArrayList<String> dates = new ArrayList<>();
    ArrayList<String> nro_details = new ArrayList<>();
    ArrayList<String> gana_details = new ArrayList<>();
    HistorialListViewAdapter historialListViewAdapter;


    JSONArray destinations = null;
    ArrayList<String> community_list = new ArrayList<>();
    ArrayList<String> dest = new ArrayList<>();
    ArrayList<String> destination_single= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histrorial);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initialize();

        barProgressDialog = ProgressDialog.show(HistrorialActivity.this, "Cargando...", "Por Favor Espera...", true);

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

        profile_name.setText(user.get("first_name") + " " + user.get("last_name"));

        profile_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistrorialActivity.this, ProfileActivity.class);
                startActivity(intent);
                mNav.closeLeftSide();
                HistrorialActivity.this.finish();
            }
        });

        second_layout = (RelativeLayout) mNav.findViewById(R.id.second_layout);
        second_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistrorialActivity.this, MainActivity.class);
                startActivity(intent);
                mNav.closeLeftSide();
                HistrorialActivity.this.finish();
            }
        });

        ////TOGGLE BUTTON
        toggleButton = (ToggleButton) mNav.findViewById(R.id.toggleButton1);
        single_status = (ToggleButton) mNav.findViewById(R.id.single_status);
        multi_status = (ToggleButton) mNav.findViewById(R.id.multi_status);

        if (user.get("busy").equals("true")) {
            toggleButton.setChecked(true);
        } else {
            toggleButton.setChecked(false);
        }

        if (user.get("available_for_deliveries").equals("true") && user.get("available_for_multiple").equals("true")) {
            single_status.setChecked(true);
            multi_status.setChecked(true);
        } else if (user.get("available_for_deliveries").equals("true") && user.get("available_for_multiple").equals("false")) {
            single_status.setChecked(true);
            multi_status.setChecked(false);
        } else if (user.get("available_for_deliveries").equals("false") && user.get("available_for_multiple").equals("true")) {
            single_status.setChecked(false);
            multi_status.setChecked(true);
        }

        header_search_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

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
                    final Dialog dialog = new Dialog(HistrorialActivity.this);
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
                } else {
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
                } else {
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

        //Navigation Drawer Listview
        NavigationDrawerAdapter adapter = new NavigationDrawerAdapter(HistrorialActivity.this, NavigationDrawer.web, NavigationDrawer.imageId);
        listnew = (ListView) mNav.findViewById(R.id.nav_list_view);
        listnew.setAdapter(adapter);
        //End of Navigation Drawer

        tabless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        frame_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        view_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        get_historial_Datas();
    }

    public void get_historial_Datas() {
        final String URL = ConstantAPI.COMPLETED;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonobject = (JSONObject) jsonArray.get(i);
                        String charged_cost_messenger = jsonobject.optString("charged_cost_messenger");
                        String created = jsonobject.optString("created");

                        JSONObject origins = jsonobject.getJSONObject("origin");
                        String stnumbers = origins.optString("stnumber");
                        String community = origins.optString("community");

                        destinations = new JSONArray(jsonArray.getJSONObject(i).getString("destinations"));
                        dest.add(String.valueOf(destinations.length()));

                        //passing values
                        community_list.add(community);
                        dates.add(created);
                        nro_details.add(stnumbers);
                        gana_details.add(charged_cost_messenger);
                        if (destinations.length() > 1) {
                            JSONObject newjsonobject = (JSONObject) destinations.get(destinations.length() - 1);
                            JSONObject location = newjsonobject.getJSONObject("location");
                            String desti_community = location.optString("community");
                            destination_single.add(desti_community);
                        } else {
                            JSONObject newjsonobject = (JSONObject) destinations.get(0);
                            JSONObject location = newjsonobject.getJSONObject("location");
                            String desti_community = location.optString("community");
                            destination_single.add(desti_community);
                        }
                    }

                    barProgressDialog.dismiss();
                    historialListViewAdapter = new HistorialListViewAdapter(HistrorialActivity.this, dates, nro_details, gana_details);
                    historial_datas.setAdapter(historialListViewAdapter);

                    historial_datas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(HistrorialActivity.this, History_DetailPage.class);
                            intent.putExtra("amount", gana_details.get(position));
                            intent.putExtra("community_list", community_list.get(position));
                            intent.putExtra("destination_size", dest.get(position));
                            intent.putExtra("destination", destination_single.get(position));
                            startActivity(intent);
                        }
                    });
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

        RequestQueue requestQueue = Volley.newRequestQueue(HistrorialActivity.this);
        requestQueue.add(req);
    }

    public void initialize() {
        header_search_list = (RelativeLayout) findViewById(R.id.header_search_list);
        historial_datas = (ListView) findViewById(R.id.historial_datas);
        tabless = (LinearLayout) findViewById(R.id.tabless);
        sessionManager = new SessionManager(HistrorialActivity.this);
        user = sessionManager.getUserDetails();
        nav_icon_drawer = (ImageView) findViewById(R.id.nav_icon_drawer);
        frame_layout = (FrameLayout) findViewById(R.id.frame_layout);
        view_top = (View) findViewById(R.id.view_top);
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


        RequestQueue requestQueue = Volley.newRequestQueue(HistrorialActivity.this);
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


        RequestQueue requestQueue = Volley.newRequestQueue(HistrorialActivity.this);
        requestQueue.add(jsObjRequest);
    }

}

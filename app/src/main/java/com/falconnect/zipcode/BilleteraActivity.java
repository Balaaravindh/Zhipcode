package com.falconnect.zipcode;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
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
import android.widget.ListView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.falconnect.zipcode.Adapter.BilleteraListViewAdapter;
import com.falconnect.zipcode.Adapter.NavigationDrawerAdapter;
import com.falconnect.zipcode.Adapter.ProfileListViewAdapter;
import com.falconnect.zipcode.Navigation.NavigationDrawer;
import com.falconnect.zipcode.ServiceHandler.ServiceHandler;
import com.falconnect.zipcode.SessionManager.SessionManager;
import com.google.gson.JsonArray;
import com.navdrawer.SimpleSideDrawer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.NameValuePair;

public class BilleteraActivity extends AppCompatActivity {

    ListView date_billetera;
    ArrayList<String> datess = new ArrayList<>();
    ArrayList<String> amount = new ArrayList<>();
    BilleteraListViewAdapter billeteraListViewAdapter;
    Button button_cobrar;
    TextView amount_bill, banco_text, cuenta_text;
    SessionManager sessionManager;
    HashMap<String, String> user;
    boolean status;
    boolean want_payments;
    RelativeLayout bank_edit;
    String id, token, idoc, mobile_number, photo, address, bank_code, account_num, payment, bank_name, busy, available_for_deliveries, available_for_multiple;;
    String main_id, username, firstname, lastname, main_email, balance;
    RelativeLayout below;
    TextView textview_empty;
    FrameLayout frame_layout;
    SimpleSideDrawer mNav;
    RelativeLayout header_search_list;
    ListView listnew;
    ///Navigation Items
    RelativeLayout profile_page;
    ImageView profile_pic;
    TextView profile_name;
    TextView profile_name_edit;
    ToggleButton toggleButton, single_status, multi_status;
    ImageView nav_icon_drawer;
    RelativeLayout second_layout;
    boolean status_active;
    boolean status_active_multi;
    ImageView white_image;

    //ProgressDialog
    ProgressDialog barProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billetera);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        intialize();

        //Progress Bar
        barProgressDialog = ProgressDialog.show(BilleteraActivity.this, "Cargando...", "Por Favor Espera...", true);

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
                Intent intent = new Intent(BilleteraActivity.this, ProfileActivity.class);
                startActivity(intent);
                mNav.closeLeftSide();
                BilleteraActivity.this.finish();
            }
        });

        second_layout = (RelativeLayout) mNav.findViewById(R.id.second_layout);
        second_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BilleteraActivity.this, MainActivity.class);
                startActivity(intent);
                mNav.closeLeftSide();
                BilleteraActivity.this.finish();
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
                    final Dialog dialog = new Dialog(BilleteraActivity.this);
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
        NavigationDrawerAdapter adapter = new NavigationDrawerAdapter(BilleteraActivity.this, NavigationDrawer.web, NavigationDrawer.imageId);
        listnew = (ListView) mNav.findViewById(R.id.nav_list_view);
        listnew.setAdapter(adapter);
        ////////////End of Nabigation Drawer//////////

        walet_listview_getdata();
        details_getdata();

        bank_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BilleteraActivity.this, RegistrarionFivePaso.class);
                startActivity(intent);
            }
        });

        if (user.get("want_payment").equals("true")) {
            button_cobrar.setBackgroundColor(Color.parseColor("#FA2608"));
        } else if (user.get("want_payment").equals("false")) {
            button_cobrar.setBackgroundColor(Color.parseColor("#C0C0C0"));
        }

        button_cobrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(BilleteraActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.status_active_custom_alert);

                TextView message = (TextView) dialog.findViewById(R.id.message);
                message.setText("ESTAS SEGURO QUE DESEAS COBRAR EL SALDO ACUMULADO?");

                final Button positive_button = (Button) dialog.findViewById(R.id.possitive_button);
                positive_button.setText("SI, ESTOY SEGURO");
                positive_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialogs = new Dialog(BilleteraActivity.this);
                        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialogs.setContentView(R.layout.continue_billing);
                        Button positive_buttons = (Button) dialogs.findViewById(R.id.possitive_button);

                        positive_buttons.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();

                                user = sessionManager.getUserDetails();
                                if (user.get("want_payment").equals("true")) {
                                    want_payments = false;
                                    Log.e("trueeeee", "false");
                                    want_pay();
                                } else if (user.get("want_payment").equals("false")) {
                                    want_payments = true;
                                    Log.e("trueeeee", "true");
                                    want_pay();
                                }
                                dialogs.dismiss();
                            }
                        });
                        dialogs.show();
                    }
                });

                Button negative_button = (Button) dialog.findViewById(R.id.negative_button);
                negative_button.setText("NO");
                negative_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        header_search_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        frame_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void status_check_multi() {
        //Progress Bar
        barProgressDialog = ProgressDialog.show(BilleteraActivity.this, "Cargando...", "Por Favor Espera...", true);
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

                    barProgressDialog.dismiss();
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


        RequestQueue requestQueue = Volley.newRequestQueue(BilleteraActivity.this);
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


        RequestQueue requestQueue = Volley.newRequestQueue(BilleteraActivity.this);
        requestQueue.add(jsObjRequest);
    }

    public void want_pay() {

        final String URL = ConstantAPI.REGISTER_API + user.get("id") + "/";

        JSONObject jsonObject_user = new JSONObject();
        try {
            jsonObject_user.put("want_payment", want_payments);
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


                    if (payment.equals("true")) {
                        button_cobrar.setBackgroundColor(Color.parseColor("#FA2608"));
                        Log.e("trueeeee", payment);
                    } else if (payment.equals("false")) {
                        button_cobrar.setBackgroundColor(Color.parseColor("#C0C0C0"));
                        Log.e("trueeeee", payment);
                    }


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


        RequestQueue requestQueue = Volley.newRequestQueue(BilleteraActivity.this);
        requestQueue.add(jsObjRequest);
    }

    private void walet_listview_getdata() {

        final String URL = ConstantAPI.PAYMENTS;

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonobject = (JSONObject) jsonArray.get(i);

                        String amounts = jsonobject.optString("amount");
                        String payment_date = jsonobject.optString("payment_date");

                        amount.add(amounts);
                        datess.add(payment_date.substring(0, payment_date.indexOf(' ')));
                    }

                    if(datess.size() > 3){
                        below.setVisibility(View.VISIBLE);
                    }else{
                        below.setVisibility(View.GONE);
                    }

                    if(datess.size() == 0){
                        textview_empty.setVisibility(View.VISIBLE);
                        date_billetera.setVisibility(View.GONE);
                    }else{
                        textview_empty.setVisibility(View.GONE);
                        date_billetera.setVisibility(View.VISIBLE);
                    }

                    white_image.setVisibility(View.GONE);
                    barProgressDialog.dismiss();

                    billeteraListViewAdapter = new BilleteraListViewAdapter(BilleteraActivity.this, datess, amount);
                    date_billetera.setAdapter(billeteraListViewAdapter);

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
                headers.put("Authorization", "Token"+ " "+ user.get("token"));
                Log.e("params", headers.toString());
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(BilleteraActivity.this);
        requestQueue.add(req);
    }

    private void details_getdata() {

        double rate = Double.parseDouble(user.get("balance"));
        String amount = Integer.toString((int)rate);
        amount_bill.setText(amount);
        banco_text.setText(user.get("bank_name"));
        cuenta_text.setText(user.get("rut_account_number"));

    }

    @Override
    public void onRestart() {
        super.onRestart();
        startActivity(getIntent());
        finish();
    }

    public void intialize() {
        sessionManager = new SessionManager(BilleteraActivity.this);
        user = sessionManager.getUserDetails();
        date_billetera = (ListView) findViewById(R.id.date_billetera);
        nav_icon_drawer = (ImageView) findViewById(R.id.nav_icon_drawer);
        amount_bill = (TextView) findViewById(R.id.amount_bill);
        banco_text = (TextView) findViewById(R.id.banco_text);
        cuenta_text = (TextView) findViewById(R.id.cuenta_text);
        button_cobrar = (Button) findViewById(R.id.button_cobrar);
        bank_edit = (RelativeLayout) findViewById(R.id.bank_edit);

        below = (RelativeLayout) findViewById(R.id.below);

        white_image = (ImageView) findViewById(R.id.white_image);

        textview_empty = (TextView) findViewById(R.id.textview_empty);
        header_search_list = (RelativeLayout)  findViewById(R.id.header_search_list);
        frame_layout = (FrameLayout) findViewById(R.id.frame_layout);


    }

}

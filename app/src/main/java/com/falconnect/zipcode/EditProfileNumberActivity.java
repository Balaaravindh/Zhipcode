package com.falconnect.zipcode;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.falconnect.zipcode.Adapter.NavigationDrawerAdapter;
import com.falconnect.zipcode.Navigation.NavigationDrawer;
import com.falconnect.zipcode.SessionManager.SessionManager;
import com.navdrawer.SimpleSideDrawer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class EditProfileNumberActivity extends AppCompatActivity {

    Button guardar;
    LinearLayout back;
    String edit_number;
    String mobile_numbers;
    EditText number_key;

    String id, token, idoc, mobile_number, photo, bank_code, bank_name, account_num, busy, payment, available_for_deliveries, available_for_multiple;;;
    String main_id, username, firstname, lastname, main_email, balance;
    boolean status;
    SessionManager sessionManager;
    HashMap<String, String> user;
    SimpleSideDrawer mNav;
    ListView listnew;

    ///Navigation Items
    RelativeLayout profile_page;
    ImageView profile_pic;
    TextView profile_name;
    TextView profile_name_edit;
    ToggleButton toggleButton;
    ImageView nav_icon_drawer;
    RelativeLayout second_layout, header_search_list;
    FrameLayout frame_layout;
    RelativeLayout content;

    //ProgressDialog
    ProgressDialog barProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_number);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        intialize();

        mobile_numbers = getIntent().getStringExtra("mobile_number");

        sessionManager = new SessionManager(EditProfileNumberActivity.this);
        user = sessionManager.getUserDetails();

        if(mobile_numbers == null){

        }else{
            mobile_numbers = mobile_numbers.replace("+56 ", "");
            number_key.setText("+56 " + mobile_numbers);
            Selection.setSelection(number_key.getText(), number_key.getText().length());
            number_key.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // TODO Auto-generated method stub
                }
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // TODO Auto-generated method stub
                }
                @Override
                public void afterTextChanged(Editable s) {
                    if(!s.toString().contains("+56 ")){
                        number_key.setText("+56 ");
                        Selection.setSelection(number_key.getText(), number_key.getText().length());
                    }
                }
            });
        }
        guardar.setTypeface(null, Typeface.BOLD);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditProfileNumberActivity.this.finish();
            }
        });

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
        profile_pic = (ImageView) mNav.findViewById(R.id.profile_avatar);

        if(user.get("photo").isEmpty()){
            Glide.with(EditProfileNumberActivity.this)
                    .load(R.drawable.default_avatar)
                    .into(profile_pic);
        }else{
            Glide.with(EditProfileNumberActivity.this)
                    .load(R.drawable.default_avatar)
                    .into(profile_pic);
        }

        profile_name.setText(user.get("first_name") + " " + user.get("last_name"));
        profile_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileNumberActivity.this, ProfileActivity.class);
                startActivity(intent);
                mNav.closeLeftSide();
                EditProfileNumberActivity.this.finish();
            }
        });

        second_layout = (RelativeLayout) mNav.findViewById(R.id.second_layout);
        second_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileNumberActivity.this, MainActivity.class);
                startActivity(intent);
                mNav.closeLeftSide();
                EditProfileNumberActivity.this.finish();
            }
        });

        header_search_list = (RelativeLayout) findViewById(R.id.header_search_list);
        header_search_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        content = (RelativeLayout) findViewById(R.id.content);
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        frame_layout = (FrameLayout) findViewById(R.id.frame_layout);
        frame_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        ////TOGGLE BUTTON
        toggleButton = (ToggleButton) mNav.findViewById(R.id.toggleButton1);
        if (user.get("busy").equals("true")) {
            toggleButton.setChecked(true);
        }else{
            toggleButton.setChecked(false);
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
                    final Dialog dialog = new Dialog(EditProfileNumberActivity.this);
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

        //NAVIGATION DRAWER LISTVIEW
        NavigationDrawerAdapter adapter = new NavigationDrawerAdapter(EditProfileNumberActivity.this, NavigationDrawer.web, NavigationDrawer.imageId);
        listnew = (ListView) mNav.findViewById(R.id.nav_list_view);
        listnew.setAdapter(adapter);
        ////////////End of Navigation Drawer//////////

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barProgressDialog = ProgressDialog.show(EditProfileNumberActivity.this, "Cargando...", "Por Favor Espera...", true);
                final String URL = ConstantAPI.EDIT_PROFILE_NUMBER + user.get("id") + "/";
                Log.e("URL", URL);
                edit_number = number_key.getText().toString().trim();
                JSONObject jsonObject_user = new JSONObject();
                try {
                    jsonObject_user.put("mobile_number", edit_number);
                    Log.e("jsObjRequest", jsonObject_user.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.PATCH, URL, jsonObject_user, new Response.Listener<JSONObject>() {
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

                            EditProfileNumberActivity.this.finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.e("Error" , error.toString());
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(EditProfileNumberActivity.this);
                requestQueue.add(jsObjRequest);
            }
        });
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

        RequestQueue requestQueue = Volley.newRequestQueue(EditProfileNumberActivity.this);
        requestQueue.add(jsObjRequest);
    }

    public void intialize(){
        number_key = (EditText) findViewById(R.id.number_key);
        guardar = (Button) findViewById(R.id.guardar);
        back = (LinearLayout) findViewById(R.id.back);

        nav_icon_drawer = (ImageView) findViewById(R.id.nav_icon_drawer);

    }

}

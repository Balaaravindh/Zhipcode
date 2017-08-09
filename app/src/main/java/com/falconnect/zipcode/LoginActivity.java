package com.falconnect.zipcode;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.falconnect.zipcode.SessionManager.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    Button login;
    String email, passwrd;
    EditText login_edittext, passwrd_edittext;
    String id, token, idoc, mobile_number, photo, bank_code, bank_name, account_num, busy, payment, available_for_deliveries, available_for_multiple;
    String main_id, username, firstname, lastname, main_email, balance;
    SessionManager sessionManager;
    HashMap<String, String> user;

    //ProgressDialog
    ProgressDialog barProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        intialize();

        sessionManager = new SessionManager(LoginActivity.this);

        user = sessionManager.getUserDetails();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = login_edittext.getText().toString();
                passwrd = passwrd_edittext.getText().toString();
                LoginUser(email, passwrd);
            }
        });
    }

    private void LoginUser(final String mail, final String pass) {
        barProgressDialog = ProgressDialog.show(LoginActivity.this, "Cargando...", "Por Favor Espera...", true);
        final String URL = ConstantAPI.LOGIN_API;
        final JSONObject jsonObject_user = new JSONObject();
        try {
            jsonObject_user.put("email", mail);
            jsonObject_user.put("password", pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject_user, new Response.Listener<JSONObject>() {
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
                    available_for_deliveries = response.getString("available_for_deliveries");
                    available_for_multiple = response.getString("available_for_multiple");

                    JSONObject jsonArray = response.getJSONObject("user");

                    main_id = jsonArray.getString("id");
                    username = jsonArray.getString("username");
                    firstname = jsonArray.getString("first_name");
                    lastname = jsonArray.getString("last_name");
                    main_email = jsonArray.getString("email");
                    balance = jsonArray.getString("balance");

                    Log.e("badasd", main_email);

                    sessionManager.clear_user();
                    sessionManager.createLoginSession(id, token, idoc, mobile_number,
                            photo, bank_name, bank_code,account_num, payment,
                            main_id, username, firstname, lastname, main_email, balance, available_for_deliveries,available_for_multiple,busy );

                    barProgressDialog.dismiss();

                    Intent intet = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intet);
                    LoginActivity.this.finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String error_response = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        JSONObject error_obj = new JSONObject(error_response);
                        JSONArray error_jsonArray = error_obj.getJSONArray("password");

                        final Dialog error_dialog = new Dialog(LoginActivity.this);
                        error_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        error_dialog.setContentView(R.layout.error_alert);

                        TextView error_message = (TextView) error_dialog.findViewById(R.id.error_message);
                        error_message.setText("Password:" + error_jsonArray.get(0).toString());

                        final Button error_positive_button = (Button) error_dialog.findViewById(R.id.error_possitive_button);
                        error_positive_button.setText("Ok");
                        error_positive_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                error_dialog.dismiss();
                                barProgressDialog.dismiss();
                            }
                        });
                        error_dialog.show();
                    } catch (UnsupportedEncodingException e1) {
                         e1.printStackTrace();
                    } catch (JSONException e2) {
                         e2.printStackTrace();
                    }
                }
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(jsObjRequest);
    }

    public void intialize() {
        login_edittext = (EditText) findViewById(R.id.login_edittext);
        passwrd_edittext = (EditText) findViewById(R.id.passwrd_edittext);
        login = (Button) findViewById(R.id.login);
    }

}

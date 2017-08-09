package com.falconnect.zipcode;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationPaso extends AppCompatActivity {

    ActionBar actionBar;
    TextView reenviar_codigo;
    Button confirm_button_codigo;
    EditText regist_text;
    String regi_number;
    String values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_paso);

        actionBar = getSupportActionBar();
        actionBar.hide();

        initilize();

        regi_number = getIntent().getStringExtra("numbers");

        reenviar_codigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(RegistrationPaso.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.registration_alert);
                TextView message = (TextView) dialog.findViewById(R.id.message);
                message.setText("HEMOS ENVIADO UN CODIGO CONFIRMACION AL" + " " + regi_number);
                Button positive_button = (Button) dialog.findViewById(R.id.possitive_button);
                positive_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        get_sms_verification_code();
                        dialog.dismiss();
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

        confirm_button_codigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_verification();
            }
        });

    }

    public void initilize(){
        regist_text = (EditText) findViewById(R.id.regist_text) ;
        reenviar_codigo = (TextView) findViewById(R.id.reenviar_codigo);
        confirm_button_codigo = (Button) findViewById(R.id.confirm_button_codigo);
    }

    public void get_verification() {
        final String URL = ConstantAPI.VERIFY_SMS;
        values = regist_text.getText().toString().trim();
        final JSONObject jsonObject_user = new JSONObject();
        try {
            jsonObject_user.put("telephone", regi_number);
            jsonObject_user.put("code", values);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject_user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.e("Response", response.toString());

                try {
                    String result = response.getString("result");
                    String message = response.getString("message");

                    if (result.equals("1")){
                        Intent intent = new Intent(RegistrationPaso.this, RegistrationActivityPasotwo.class);
                        intent.putExtra("numbers", regi_number);
                        intent.putExtra("numbers_codes", values);
                        startActivity(intent);
                        RegistrationPaso.this.finish();
                    }else{

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

        RequestQueue requestQueue = Volley.newRequestQueue(RegistrationPaso.this);
        requestQueue.add(jsObjRequest);
    }

    public void get_sms_verification_code() {
        final String URL = ConstantAPI.SMS;
        final JSONObject jsonObject_user = new JSONObject();
        try {
            jsonObject_user.put("telephone", regi_number);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject_user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.e("Response", response.toString());

                try {
                    String result = response.getString("result");
                    String message = response.getString("message");
                    String code = response.getString("code");
                    String telephone = response.getString("telephone");

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

        RequestQueue requestQueue = Volley.newRequestQueue(RegistrationPaso.this);
        requestQueue.add(jsObjRequest);
    }


}

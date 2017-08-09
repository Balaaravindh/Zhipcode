package com.falconnect.zipcode;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class RegistrationActivity extends AppCompatActivity {

    ActionBar actionBar;
    Button confirm_button;
    EditText number;

    String numbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        actionBar = getSupportActionBar();
        actionBar.hide();

        intialize();

        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(RegistrationActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.registration_alert);

                Button positive_button = (Button) dialog.findViewById(R.id.possitive_button);
                positive_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        numbers = number.getText().toString().trim();
                        get_sms_verification_code();
                    }


                });
                Button negative_button = (Button) dialog.findViewById(R.id.negative_button);
                negative_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        RegistrationActivity.this.finish();
                        Intent intent = new Intent(RegistrationActivity.this, FirstActivity.class);
                        startActivity(intent);
                    }
                });
                dialog.show();
            }
        });


    }


    public void get_sms_verification_code() {
        final String URL = ConstantAPI.SMS;
        final JSONObject jsonObject_user = new JSONObject();
        try {
            jsonObject_user.put("telephone", numbers);
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

                    if (result.equals("1")){
                        Intent intent = new Intent(RegistrationActivity.this, RegistrationPaso.class);
                        intent.putExtra("numbers", numbers);
                        startActivity(intent);
                        RegistrationActivity.this.finish();
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

        RequestQueue requestQueue = Volley.newRequestQueue(RegistrationActivity.this);
        requestQueue.add(jsObjRequest);
    }

    public void intialize() {
        confirm_button = (Button) findViewById(R.id.confirm_button);
        number = (EditText) findViewById(R.id.regist_telephone);

        number.setText("+91");
        Selection.setSelection(number.getText(), number.getText().length());

        number.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().contains("+91")) {
                    number.setText("+91");
                    Selection.setSelection(number.getText(), number.getText().length());

                }

            }
        });
    }

}

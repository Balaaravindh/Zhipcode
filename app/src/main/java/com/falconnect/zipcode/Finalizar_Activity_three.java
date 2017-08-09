package com.falconnect.zipcode;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.falconnect.zipcode.SessionManager.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Finalizar_Activity_three extends AppCompatActivity {


    Button final_button;
    String errand_ids;
    SessionManager sessionManager;
    HashMap<String, String> user;
    String destination_id;

    //ProgressDialog
    ProgressDialog barProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar__three);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initilize();

        errand_ids = getIntent().getStringExtra("errand_ids");
        destination_id = getIntent().getStringExtra("destination_id");

        final_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final_button_api();
            }
        });

    }

    public void final_button_api(){
        final String URL = ConstantAPI.ERRAND_ASSIGN + errand_ids + "/complete_errand/";

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PATCH, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Response", response.toString());

                Intent intent = new Intent(Finalizar_Activity_three.this, MainActivity.class);
                startActivity(intent);
                Finalizar_Activity_three.this.finish();
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

        RequestQueue requestQueue = Volley.newRequestQueue(Finalizar_Activity_three.this);
        requestQueue.add(req);
    }

    public void initilize(){
        sessionManager = new SessionManager(Finalizar_Activity_three.this);
        user = sessionManager.getUserDetails();
        final_button = (Button) findViewById(R.id.finalir);
    }
}

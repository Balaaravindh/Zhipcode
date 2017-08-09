package com.falconnect.zipcode;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class Negative_comments extends AppCompatActivity {

    EditText enviar_opinion_textbox;
    Button enviar_opinion;
    String edit_text_values;
    String errand_ids;
    SessionManager sessionManager;
    HashMap<String, String> user;
    String destination_id;

    //ProgressDialog
    ProgressDialog barProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negative_comments);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        intilize();

        errand_ids = getIntent().getStringExtra("errand_ids");
        destination_id = getIntent().getStringExtra("destination_id");


        enviar_opinion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_text_values = enviar_opinion_textbox.getText().toString();
                negative_comment();

            }
        });
    }

    public void intilize() {

        sessionManager = new SessionManager(Negative_comments.this);
        user = sessionManager.getUserDetails();
        enviar_opinion_textbox = (EditText) findViewById(R.id.enviar_opinion_textbox);
        enviar_opinion = (Button) findViewById(R.id.enviar_opinion);

    }

    public void negative_comment() {
         final String URL = ConstantAPI.ERRAND_ASSIGN + errand_ids + "/";

        JSONObject thumbs_user = new JSONObject();
        try {
            thumbs_user.put("client_rate", "good");
            thumbs_user.put("client_rate_post_notes", edit_text_values);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PATCH, URL, thumbs_user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Response", response.toString());

                Intent intent = new Intent(Negative_comments.this, Finalizar_Activity_three.class);
                intent.putExtra("errand_ids", errand_ids);
                intent.putExtra("destination_id", destination_id);
                startActivity(intent);

                Negative_comments.this.finish();

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
        RequestQueue requestQueue = Volley.newRequestQueue(Negative_comments.this);
        requestQueue.add(req);
    }

}

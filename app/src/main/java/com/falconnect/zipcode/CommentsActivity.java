package com.falconnect.zipcode;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.falconnect.zipcode.Adapter.BilleteraListViewAdapter;
import com.falconnect.zipcode.SessionManager.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentsActivity extends AppCompatActivity {

    ActionBar actionBar;
    ImageView positive, negative;
    RelativeLayout image;
    SessionManager sessionManager;
    HashMap<String, String> user;
    String destination_id;
    String errand_ids;
    String destination_size;
    HashMap<String, String> orgin_Datas;
    HashMap<String, String> datas_desti;
    HashMap<Integer, ArrayList<String>> datas_desti_multi;
    ArrayList<String> multi_lat;
    ArrayList<String> multi_long;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        actionBar = getSupportActionBar();
        actionBar.hide();

        intialize();

        errand_ids = getIntent().getStringExtra("errand_ids");
        destination_id = getIntent().getStringExtra("destination_id");
        destination_size = getIntent().getStringExtra("destination_size");

        ////////////////GET VALUES
        orgin_Datas = (HashMap<String, String>) getIntent().getSerializableExtra("origin_datas");
        datas_desti = (HashMap<String, String>) getIntent().getSerializableExtra("datas_desti");
        datas_desti_multi = (HashMap<Integer, ArrayList<String>>) getIntent().getSerializableExtra("datas_desti_multi");

        multi_lat = getIntent().getStringArrayListExtra("multi_lat");
        multi_long = getIntent().getStringArrayListExtra("multi_long");

        if (datas_desti == null) {
        } else {
        }
        if (orgin_Datas == null) {
        } else {
        }
        if (datas_desti_multi == null) {
        } else {

        }

        if (multi_lat == null) {

        } else {
        }

        if (multi_long == null) {

        } else {
        }

        if(destination_size == null){

        }else{

        }

        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postive_thumbs();
            }
        });

        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                negative_thumbs();
            }
        });

    }

    private void negative_thumbs() {

            Intent intent = new Intent(CommentsActivity.this, Negative_comments.class);
            intent.putExtra("errand_ids", errand_ids);
            intent.putExtra("destination_id", destination_id);
            startActivity(intent);
            CommentsActivity.this.finish();

    }

    private void postive_thumbs() {

        final String URL = ConstantAPI.ERRAND_ASSIGN + errand_ids + "/";

        JSONObject thumbs_user = new JSONObject();
        try {
            thumbs_user.put("client_rate", "good");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PATCH, URL, thumbs_user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Response", response.toString());

                Intent intent = new Intent(CommentsActivity.this, Finalizar_Activity_three.class);
                intent.putExtra("errand_ids", errand_ids);
                intent.putExtra("destination_id", destination_id);
                startActivity(intent);
                CommentsActivity.this.finish();
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

        RequestQueue requestQueue = Volley.newRequestQueue(CommentsActivity.this);
        requestQueue.add(req);

    }

    public void intialize() {
        sessionManager = new SessionManager(CommentsActivity.this);
        user = sessionManager.getUserDetails();
        positive = (ImageView) findViewById(R.id.image_thumbsup);
        negative = (ImageView) findViewById(R.id.image_thumbsdown);
        image = (RelativeLayout) findViewById(R.id.image_pickup);

    }

}

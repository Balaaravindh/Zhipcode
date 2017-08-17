package com.falconnect.zipcode;

import android.util.Base64;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComprobanteActivity extends AppCompatActivity {

    Button titular, conserje, otro;
    Button solicitud_finalzada;
    RelativeLayout otro_textbox;
    EditText observacion;
    String destination_id, destination_size;

    //Action
    ActionBar actionBar;

    HashMap<String, String> orgin_Datas;
    HashMap<String, String> datas_desti;
    HashMap<Integer, ArrayList<String>> datas_desti_multi;
    HashMap<Integer, ArrayList<String>> datas_desti_multi_values= new HashMap<>();
    ArrayList<String> multi_lat;
    ArrayList<String> multi_long;
    EditText observacion_otro, rut, telefono, nombre;
    String rut_text, obse_text_otro, telefono_text, nombre_text;
    String observacion_text;
    String errand_ids;
    SessionManager sessionManager;
    HashMap<String, String> user;
    int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprobante);

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

        titular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value = 0;
                titular.setBackgroundResource(R.drawable.map_next_buttons_color);
                titular.setTextColor(Color.WHITE);
                solicitud_finalzada.setVisibility(View.VISIBLE);
            }
        });


        conserje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value = 1;
                titular.setVisibility(View.GONE);
                otro.setVisibility(View.GONE);
                solicitud_finalzada.setVisibility(View.VISIBLE);
                conserje.setBackgroundResource(R.drawable.map_next_buttons_color);
                conserje.setTextColor(Color.WHITE);
                observacion.setVisibility(View.VISIBLE);
            }
        });

        otro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value = 2;
                titular.setVisibility(View.GONE);
                conserje.setVisibility(View.GONE);
                otro_textbox.setVisibility(View.VISIBLE);
                otro.setBackgroundResource(R.drawable.map_next_buttons_color);
                otro.setTextColor(Color.WHITE);

                solicitud_finalzada.setVisibility(View.VISIBLE);
            }
        });

        solicitud_finalzada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(value == 0){
                    titular();
                }else if(value == 1){
                    observacion_text = observacion.getText().toString();
                    vigilante_conserje_fun();

                }else if (value == 2){
                    nombre_text = nombre.getText().toString();
                    obse_text_otro = observacion_otro.getText().toString();
                    telefono_text = telefono.getText().toString();
                    rut_text = rut.getText().toString();
                    otro_fun();
                }
            }
        });


    }


    private void titular(){
        final String URL = ConstantAPI.ERRAND_ASSIGN + errand_ids + "/destinations/" + destination_id + "/";

        JSONObject thumbs_user = new JSONObject();
        try {
            thumbs_user.put("was_delivered_to_watchman", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PATCH, URL, thumbs_user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Response", response.toString());
                Log.e("datas_desti_multi", datas_desti_multi.toString());

                if (datas_desti_multi.size() != 0){

                    datas_desti_multi.remove(0);

                    for (int i = 0; i < datas_desti_multi.size(); i++) {
                        datas_desti_multi_values.put(i, datas_desti_multi.get(i+1));
                    }

                    Log.e("datas_desti_multi", String.valueOf(datas_desti_multi_values.size()));
                    Log.e("datas_desti_multi", datas_desti_multi_values.toString());
                }
                if(datas_desti_multi.size() == 0){
                    Intent intent = new Intent(ComprobanteActivity.this, CommentsActivity.class);
                    intent.putExtra("errand_ids", errand_ids);
                    intent.putExtra("destination_id", destination_id);
                    intent.putExtra("destination_size", destination_size);
                    startActivity(intent);
                    ComprobanteActivity.this.finish();
                }else{
                    Intent intent = new Intent(ComprobanteActivity.this, MapsActivity.class);
                    intent.putExtra("errand_ids", errand_ids);
                    intent.putExtra("destination_id", destination_id);
                    intent.putExtra("destination_size", destination_size);
                    intent.putExtra("origin_datas", orgin_Datas);
                    intent.putExtra("datas_desti_multi", datas_desti_multi_values);
                    intent.putExtra("multi_lat", multi_lat);
                    intent.putExtra("multi_long", multi_long);
                    startActivity(intent);
                    ComprobanteActivity.this.finish();
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

        RequestQueue requestQueue = Volley.newRequestQueue(ComprobanteActivity.this);
        requestQueue.add(req);
    }

    private void otro_fun() {

        final String URL = ConstantAPI.ERRAND_ASSIGN + errand_ids + "/destinations/" + destination_id + "/";

        JSONObject thumbs_user = new JSONObject();
        try {
            thumbs_user.put("was_delivered_to_watchman", true);
            thumbs_user.put("delivery_contact_name", nombre_text);
            thumbs_user.put("delivery_contact_rut", rut_text);
            thumbs_user.put("delivery_contact_phone", telefono_text);
            thumbs_user.put("delivery_remarks", obse_text_otro);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PATCH, URL, thumbs_user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Response", response.toString());
                if(destination_size.equals("1")){
                    Intent intent = new Intent(ComprobanteActivity.this, CommentsActivity.class);
                    intent.putExtra("errand_ids", errand_ids);
                    intent.putExtra("destination_id", destination_id);
                    intent.putExtra("destination_size", destination_size);
                    startActivity(intent);
                    ComprobanteActivity.this.finish();
                }else{
                    Intent intent = new Intent(ComprobanteActivity.this, MapsActivity.class);
                    intent.putExtra("errand_ids", errand_ids);
                    intent.putExtra("destination_id", destination_id);
                    intent.putExtra("destination_size", destination_size);
                    intent.putExtra("origin_datas", orgin_Datas);
                    intent.putExtra("datas_desti_multi", datas_desti_multi);
                    intent.putExtra("multi_lat", multi_lat);
                    intent.putExtra("multi_long", multi_long);
                    startActivity(intent);
                    ComprobanteActivity.this.finish();
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

        RequestQueue requestQueue = Volley.newRequestQueue(ComprobanteActivity.this);
        requestQueue.add(req);
    }

    private void vigilante_conserje_fun() {
        final String URL = ConstantAPI.ERRAND_ASSIGN + errand_ids + "/destinations/" + destination_id + "/";

        JSONObject thumbs_user = new JSONObject();
        try {
            thumbs_user.put("was_delivered_to_watchman", true);
            thumbs_user.put("delivery_remarks", observacion_text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PATCH, URL, thumbs_user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Response", response.toString());
                if(destination_size.equals("1")){
                    Intent intent = new Intent(ComprobanteActivity.this, CommentsActivity.class);
                    intent.putExtra("errand_ids", errand_ids);
                    intent.putExtra("destination_id", destination_id);
                    intent.putExtra("destination_size", destination_size);
                    startActivity(intent);
                    ComprobanteActivity.this.finish();
                }else{
                    Intent intent = new Intent(ComprobanteActivity.this, MapsActivity.class);
                    intent.putExtra("errand_ids", errand_ids);
                    intent.putExtra("destination_id", destination_id);
                    intent.putExtra("destination_size", destination_size);
                    intent.putExtra("origin_datas", orgin_Datas);
                    intent.putExtra("datas_desti_multi", datas_desti_multi);
                    intent.putExtra("multi_lat", multi_lat);
                    intent.putExtra("multi_long", multi_long);
                    startActivity(intent);
                    ComprobanteActivity.this.finish();
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

        RequestQueue requestQueue = Volley.newRequestQueue(ComprobanteActivity.this);
        requestQueue.add(req);

    }

    public void intialize(){
        sessionManager = new SessionManager(ComprobanteActivity.this);
        user = sessionManager.getUserDetails();
        titular = (Button) findViewById(R.id.titular);
        conserje = (Button) findViewById(R.id.conserje);
        otro = (Button) findViewById(R.id.otro);
        solicitud_finalzada = (Button) findViewById(R.id.solicitud_finalzada);
        otro_textbox = (RelativeLayout) findViewById(R.id.otro_textbox);
        observacion = (EditText) findViewById(R.id.observacion);

        nombre = (EditText) findViewById(R.id.nombre);
        rut = (EditText) findViewById(R.id.rut);
        telefono = (EditText) findViewById(R.id.telefono);
        observacion_otro = (EditText) findViewById(R.id.observacion_otro);

    }

}

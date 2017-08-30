package com.falconnect.zipcode;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.falconnect.zipcode.SessionManager.JsonObject;
import com.falconnect.zipcode.SessionManager.Orgin_destination_identy;
import com.falconnect.zipcode.SessionManager.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class No_EntregadoActivity extends AppCompatActivity {

    Button envira_observation;
    EditText observation_cancel;
    String observe;
    SessionManager sessionManager;
    HashMap<String, String> user;

    String job_accepted_destination_id;
    JsonObject jsonObject;
    ArrayList<String> orgins_datas = new ArrayList<>();
    HashMap<String,ArrayList<String>> all_datas = new HashMap<>();
    HashMap<String, String> orgin_Datas;
    ArrayList<HashMap<Integer, ArrayList<String>>> new_all_datas = new ArrayList<>();
    HashMap<String, String> datas_desti;
    HashMap<Integer, ArrayList<String>> datas_desti_multi;
    String destination_id, destination_size;
    ArrayList<String> multi_lat;
    ArrayList<String> multi_long;
    HashMap<Integer, ArrayList<String>> datas_desti_multi_values = new HashMap<>();
    String errand_ids;
    int value;
    HashMap<String, String> datas = new HashMap<>();
    ProgressDialog barProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no__entregado);

        ActionBar action = getSupportActionBar();
        action.hide();

        errand_ids = getIntent().getStringExtra("errand_ids");
        destination_id = getIntent().getStringExtra("destination_id");
        destination_size = getIntent().getStringExtra("destination_size");

        ////////////////GET VALUES
        orgin_Datas = (HashMap<String, String>) getIntent().getSerializableExtra("origin_datas");
        datas_desti = (HashMap<String, String>) getIntent().getSerializableExtra("datas_desti");
        datas_desti_multi = (HashMap<Integer, ArrayList<String>>) getIntent().getSerializableExtra("datas_desti_multi");

        multi_lat = getIntent().getStringArrayListExtra("multi_lat");
        multi_long = getIntent().getStringArrayListExtra("multi_long");

        initlize();

        envira_observation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                observe = observation_cancel.getText().toString();

                if (observe.equals("")) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(No_EntregadoActivity.this);
                    builder.setTitle("Error");
                    builder.setMessage("Por favor ingrese la observación");
                    builder.setPositiveButton("De Acuerdo", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                } else {
                    api_cancel();
                }
            }
        });

    }

    private void api_cancel() {
        JSONObject jsonObject_user = new JSONObject();
        try {
            jsonObject_user.put("delivery_remarks", observe);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        destination_id = datas_desti_multi.get(0).get(0);

        final String URL = ConstantAPI.CANCEL_ERRANDS_NOEND + errand_ids + "/destinations/" + destination_id;
        Log.e("URL", URL);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PATCH, URL, jsonObject_user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.e("respones", response.toString());

                String charged_cost_messenger = response.optString("charged_cost_messenger");

                if (datas_desti_multi.size() != 0) {
                    datas_desti_multi.remove(0);
                    for (int i = 0; i < datas_desti_multi.size(); i++) {
                        datas_desti_multi_values.put(i, datas_desti_multi.get(i + 1));
                    }
                }
                String Storedata = PreferenceManager.getDefaultSharedPreferences(No_EntregadoActivity.this).getString("destinations", "null");
                if(Storedata != "null") {
                    PreferenceManager.getDefaultSharedPreferences(No_EntregadoActivity.this).edit().putString("destinations", Storedata +"$"+ destination_id).apply();
                } else {
                    PreferenceManager.getDefaultSharedPreferences(No_EntregadoActivity.this).edit().putString("destinations", destination_id).apply();
                }
                if (datas_desti_multi.isEmpty()) {
                    Intent intent = new Intent(No_EntregadoActivity.this, Finalizar_Activity_three.class);
                    intent.putExtra("errand_ids", errand_ids);
                    intent.putExtra("destination_id",  destination_id);
                    intent.putExtra("charged_cost_messenger", charged_cost_messenger);
                    startActivity(intent);
                    No_EntregadoActivity.this.finish();
                } else {
                    Intent intent = new Intent(No_EntregadoActivity.this, MapsActivity.class);
                    intent.putExtra("errand_ids", errand_ids);
                    intent.putExtra("destination_id", destination_id);
                    intent.putExtra("destination_size", destination_size);
                    intent.putExtra("origin_datas", orgin_Datas);
                    intent.putExtra("datas_desti_multi", datas_desti_multi_values);
                    intent.putExtra("multi_lat", multi_lat);
                    intent.putExtra("multi_long", multi_long);
                    startActivity(intent);
                    No_EntregadoActivity.this.finish();
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

        RequestQueue requestQueue = Volley.newRequestQueue(No_EntregadoActivity.this);
        requestQueue.add(req);

    }

    public void initlize() {
        sessionManager = new SessionManager(No_EntregadoActivity.this);
        envira_observation = (Button) findViewById(R.id.envira_observation);
        observation_cancel = (EditText) findViewById(R.id.observation_cancel);
        jsonObject = new JsonObject(No_EntregadoActivity.this);
    }
}

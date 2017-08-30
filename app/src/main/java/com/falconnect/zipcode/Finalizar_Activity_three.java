package com.falconnect.zipcode;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.falconnect.zipcode.Crop.Crop;
import com.falconnect.zipcode.SessionManager.JsonObject;
import com.falconnect.zipcode.SessionManager.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Finalizar_Activity_three extends AppCompatActivity {


    Button final_button;
    String errand_ids;
    SessionManager sessionManager;
    HashMap<String, String> user;
    String destination_id;
    String charged_cost_messenger;

    TextView ganancia_1000;

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
        charged_cost_messenger = getIntent().getStringExtra("charged_cost_messenger");
        final_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final_button_api();
            }
        });

        String amount = charged_cost_messenger.replace(".00", "");

        ganancia_1000.setText("GANANCIA" + " " + amount);

    }

    public void final_button_api() {

        barProgressDialog = ProgressDialog.show(Finalizar_Activity_three.this, "Cargando...", "Por Favor Espera...", true);

        final String URL = ConstantAPI.ERRAND_ASSIGN + errand_ids + "/complete_errand/";

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PATCH, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                barProgressDialog.dismiss();
                try {
                    final JSONObject job_queued_response = response.optJSONObject("job_queued");
                    //    "job_queued": {
                    //        "charged_cost_messenger": 11231,
                    //        "errand_type": "MULTIDESTINOS",
                    //        "id": 1602
                    //        "num_destinations" = 6;
                    //    }
                   /*JSONObject job_queued_response = new JSONObject();
                    JSONObject jsonObject_user = new JSONObject();
                    jsonObject_user.put("charged_cost_messenger", 11231);
                    jsonObject_user.put("errand_type", "MULTIDESTINOS");
                    jsonObject_user.put("id", 1602);
                    jsonObject_user.put("num_destinations", 6);
                    job_queued_response.put("job_accepted" ,jsonObject_user);*/

                    if (job_queued_response == null || job_queued_response.equals("")) {
                        Intent intent = new Intent(Finalizar_Activity_three.this, MainActivity.class);
                        startActivity(intent);
                        Finalizar_Activity_three.this.finish();

                    } else {

                        //JSONObject job_queued_responsess = job_queued_response.getJSONObject("job_accepted");

                        String charged_cost_messenger = job_queued_response.optString("charged_cost_messenger");
                        String errand_type = job_queued_response.optString("errand_type");
                        String id = job_queued_response.optString("id");
                        String num_destinations = job_queued_response.optString("num_destinations");

                        Intent intent = new Intent(Finalizar_Activity_three.this, SpecialErrandAssign.class);
                        intent.putExtra("charged_cost_messenger", charged_cost_messenger);
                        intent.putExtra("errand_type", errand_type);
                        intent.putExtra("id", id);
                        intent.putExtra("num_destinations", num_destinations);
                        startActivity(intent);

                    }

                } catch (Exception ex) {

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

        RequestQueue requestQueue = Volley.newRequestQueue(Finalizar_Activity_three.this);
        requestQueue.add(req);
    }


    public void initilize() {
        sessionManager = new SessionManager(Finalizar_Activity_three.this);
        user = sessionManager.getUserDetails();
        final_button = (Button) findViewById(R.id.finalir);
        ganancia_1000 = (TextView) findViewById(R.id.ganancia_1000);
    }

}

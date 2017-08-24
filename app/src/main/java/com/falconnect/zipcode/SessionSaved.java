package com.falconnect.zipcode;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.falconnect.zipcode.SessionManager.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SessionSaved extends Service {

    private static final int HANDLER_DELAY = 300000;

    SessionManager sessionManager;
    HashMap<String, String> user;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //getting systems default ringtone

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                api_integration();
                handler.postDelayed(this, HANDLER_DELAY);
            }
        }, HANDLER_DELAY);


        return START_STICKY;
    }


    private void api_integration( ) {

        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetails();

        final String URL = ConstantAPI.PROFILE_API + user.get("id") + "/";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, URL, null,  new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String id = response.getString("id");
                    String token = response.getString("token");
                    String idoc = response.getString("idoc");
                    String mobile_number = response.getString("mobile_number");
                    String photo = response.getString("photo");
                    String bank_code = response.getString("bank_code");
                    String bank_name = response.getString("bank_name");
                    String account_num = response.getString("rut_account_number");
                    String payment = response.getString("want_payment");
                    String busy = response.getString("busy");

                    JSONObject jsonArray = response.getJSONObject("user");

                    String main_id = jsonArray.getString("id");
                    String username = jsonArray.getString("username");
                    String firstname = jsonArray.getString("first_name");
                    String lastname = jsonArray.getString("last_name");
                    String main_email = jsonArray.getString("email");
                    String balance = jsonArray.getString("balance");
                    String available_for_deliveries = jsonArray.getString("available_for_deliveries");
                    String available_for_multiple = jsonArray.getString("available_for_multiple");

                    sessionManager.clear();

                    sessionManager.createLoginSession(id, token, idoc, mobile_number,
                            photo, bank_name, bank_code, account_num, payment,
                            main_id, username, firstname, lastname, main_email, balance,
                            available_for_deliveries, available_for_multiple, busy);


                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub

            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsObjRequest);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}
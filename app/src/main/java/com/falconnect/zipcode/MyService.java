package com.falconnect.zipcode;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.falconnect.zipcode.MapModules.GPSTracker;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class MyService extends Service {

    private static final int HANDLER_DELAY = 3000;
    GPSTracker gps;
    public static final String BASE_URL = "http://stage.zhipcode.com/api/mobile/v1/positions/";

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
                get_location();
                handler.postDelayed(this, HANDLER_DELAY);
            }
        }, HANDLER_DELAY);


        return START_STICKY;
    }

    private void get_location() {
        gps = new GPSTracker(getApplicationContext());
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            api_integration(latitude, longitude);

        } else {
        }
    }

    private void api_integration(double latitude, double longitude) {

        JSONObject json_object = new JSONObject();
        try {
            json_object.put("latitude", latitude);
            json_object.put("longitude", longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.e("json_object", BASE_URL);
        Log.e("json_object", json_object.toString());

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, BASE_URL, json_object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.e("response", response.toString());

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
                headers.put("Authorization", "Token"+ " "+ "d5338c866e6ba5f5381c470d8533668d58101a08");
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(req);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}
package com.falconnect.zipcode;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.falconnect.zipcode.SessionManager.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class RegistrarionFivePaso extends AppCompatActivity {

    ActionBar actionBar;
    Spinner bank;
    ArrayList<String> bank_array = new ArrayList<>();
    ArrayList<String> bank_array_id = new ArrayList<>();
    int position_bank;
    int value;
    String selected_bank;
    String acc_num;
    EditText account_number;
    TextView bank_names;
    Button confirm_guardar_five;
    ArrayAdapter<String> spinner_bank_adapter;
    public ArrayList<HashMap<String, String>> bankmap_list = new ArrayList<>();
    HashMap<String, String> bank_map = new HashMap<>();
    String numbers, numbers_codes, email, password;
    String frst_name, last_name, i_doc;
    String id, token, idoc, mobile_number, photo, address, bank_code, account_num, payment, bank_name, busy, available_for_deliveries, available_for_multiple;
    String main_id, username, firstname, lastname, main_email, balance;
    SessionManager sessionManager;
    HashMap<String, String> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarion_five_paso);

        actionBar = getSupportActionBar();
        actionBar.hide();

        initialize();

        numbers = getIntent().getStringExtra("numbers");
        numbers_codes = getIntent().getStringExtra("numbers_codes");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        frst_name = getIntent().getStringExtra("frst_name");
        last_name = getIntent().getStringExtra("last_name");
        i_doc = getIntent().getStringExtra("i_doc");

        sessionManager = new SessionManager(RegistrarionFivePaso.this);
        user = sessionManager.getUserDetails();
        if (user.get("rut_account_number") == null) {
            bank_names.setVisibility(View.GONE);
            value = 0;
        } else {
            value = 1;
            bank.setVisibility(View.GONE);
            bank_names.setVisibility(View.VISIBLE);
            bank_names.setText(user.get("bank_name"));
            account_number.setText(user.get("rut_account_number"));
        }

        bank_names.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bank.setVisibility(View.VISIBLE);
                bank_names.setVisibility(View.GONE);
            }
        });

        bank_array.add("SELECCIONA TU BANCO");
        bank_array_id.add("0");

        InputStream inputStream = getResources().openRawResource(R.raw.bankdetails);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int ctr;
        try {
            ctr = inputStream.read();
            while (ctr != -1) {
                byteArrayOutputStream.write(ctr);
                ctr = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.v("Text Data", byteArrayOutputStream.toString());
        try {
            JSONObject jObject = new JSONObject(byteArrayOutputStream.toString());
            String jObjectResult = jObject.getString("result");
            String jObjectmessage = jObject.getString("message");

            Log.e("sadasda", jObjectResult);
            Log.e("sadasda", jObjectmessage);

            JSONArray business_index = jObject.getJSONArray("bank_code");
            for (int i = 0; i < business_index.length(); i++) {
                String bank_id = business_index.getJSONObject(i).getString("bank_id");
                String bank_name = business_index.getJSONObject(i).getString("bank_name");

                bank_map.put("bank_id", bank_id);
                bank_map.put("bank_name", bank_name);

                bankmap_list.add(bank_map);

                bank_array.add(bank_name);
                bank_array_id.add(bank_id);
            }
            Log.e("sadasda", business_index.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        spinner_bank_adapter = new ArrayAdapter<String>(RegistrarionFivePaso.this, R.layout.spinner_single_item, bank_array) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinner_bank_adapter.setDropDownViewResource(R.layout.spinner_single_item);
        bank.setAdapter(spinner_bank_adapter);
        bank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if (position > 0) {
                    selected_bank = String.valueOf(bank_array_id.get(position));
                    position_bank = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        confirm_guardar_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acc_num = account_number.getText().toString().trim();
                if (value == 0) {
                    Log.e("back_name_change", "0");
                    register();
                } else {
                    Log.e("back_name_change", "1");
                    back_name_change();
                }
            }
        });
    }

    public void back_name_change() {

        final String URL = ConstantAPI.REGISTER_API + user.get("id") + "/";
        Log.e("url", URL);
        JSONObject jsonObject_user = new JSONObject();

        try {
            jsonObject_user.put("bank_code", selected_bank);
            jsonObject_user.put("rut_account_number", acc_num);
            Log.e("Params", jsonObject_user.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.PATCH, URL, jsonObject_user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.e("clear", response.toString());
                try {
                    id = response.getString("id");
                    token = response.getString("token");
                    idoc = response.getString("idoc");
                    mobile_number = response.getString("mobile_number");
                    photo = response.getString("photo");
                    bank_code = response.getString("bank_code");
                    bank_name = response.getString("bank_name");
                    account_num = response.getString("rut_account_number");
                    payment = response.getString("want_payment");
                    busy = response.getString("busy");
                    available_for_deliveries = response.getString("available_for_deliveries");
                    available_for_multiple = response.getString("available_for_multiple");

                    JSONObject jsonArray = response.getJSONObject("user");

                    main_id = jsonArray.getString("id");
                    username = jsonArray.getString("username");
                    firstname = jsonArray.getString("first_name");
                    lastname = jsonArray.getString("last_name");
                    main_email = jsonArray.getString("email");
                    balance = jsonArray.getString("balance");

                    Log.e("badasd", main_email);

                    sessionManager.clear_user();
                    sessionManager.createLoginSession(id, token, idoc, mobile_number,
                            photo, bank_name, bank_code, account_num, payment,
                            main_id, username, firstname, lastname, main_email, balance, available_for_deliveries, available_for_multiple, busy);

                    RegistrarionFivePaso.this.finish();

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

        RequestQueue requestQueue = Volley.newRequestQueue(RegistrarionFivePaso.this);
        requestQueue.add(jsObjRequest);

    }

    private void register() {

        final String URL = ConstantAPI.REGISTER_API;

        JSONObject jsonObject_user = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("first_name", frst_name);
            jsonObject.put("last_name", last_name);
            jsonObject.put("email", email);
            jsonObject.put("password", password);

            jsonObject_user.put("user", jsonObject);
            jsonObject_user.put("idoc", i_doc);
            jsonObject_user.put("mobile_number", numbers);
            jsonObject_user.put("address", "");
            jsonObject_user.put("bank_code", selected_bank);
            jsonObject_user.put("rut_account_number", acc_num);

            Log.e("Params", jsonObject_user.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject_user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Response", response.toString());
                try {
                    id = response.getString("id");
                    token = response.getString("token");
                    idoc = response.getString("idoc");
                    mobile_number = response.getString("mobile_number");
                    photo = response.getString("photo");
                    address = response.getString("address");
                    bank_code = response.getString("bank_code");
                    account_num = response.getString("rut_account_number");
                    payment = response.getString("want_payment");
                    busy = response.getString("busy");
                    available_for_deliveries = response.getString("available_for_deliveries");
                    available_for_multiple = response.getString("available_for_multiple");

                    JSONObject jsonArray = response.getJSONObject("user");

                    main_id = jsonArray.getString("id");
                    username = jsonArray.getString("username");
                    firstname = jsonArray.getString("first_name");
                    lastname = jsonArray.getString("last_name");
                    main_email = jsonArray.getString("email");
                    balance = jsonArray.getString("balance");

                    sessionManager.createLoginSession(id, token, idoc, mobile_number,
                            photo, bank_name, bank_code, account_num, payment,
                            main_id, username, firstname, lastname, main_email, balance, available_for_deliveries, available_for_multiple, busy);

                    Intent intent = new Intent(RegistrarionFivePaso.this, MainActivity.class);
                    startActivity(intent);

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

        RequestQueue requestQueue = Volley.newRequestQueue(RegistrarionFivePaso.this);
        requestQueue.add(jsObjRequest);

    }

    public void initialize() {

        bank = (Spinner) findViewById(R.id.bank);
        confirm_guardar_five = (Button) findViewById(R.id.confirm_guardar_five);
        account_number = (EditText) findViewById(R.id.tu_apaellido_text);

        bank_names = (TextView) findViewById(R.id.bank_name);
    }
}

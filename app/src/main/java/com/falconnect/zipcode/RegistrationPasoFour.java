package com.falconnect.zipcode;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegistrationPasoFour extends AppCompatActivity {

    ActionBar actionBar;
    Button confirm_guardar;
    String numbers, numbers_codes , email, password;
    String frst_name, last_name, i_doc;
    EditText tu_nombre_text, tu_apaellido_text, tu_rut_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_paso_four);

        actionBar = getSupportActionBar();
        actionBar.hide();

        initialize();

        numbers = getIntent().getStringExtra("numbers");
        numbers_codes = getIntent().getStringExtra("numbers_codes");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");

        confirm_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frst_name = tu_nombre_text.getText().toString().trim();
                last_name = tu_apaellido_text.getText().toString().trim();
                i_doc = tu_rut_text.getText().toString().trim();

                Intent intent = new Intent(RegistrationPasoFour.this, RegistrarionFivePaso.class);
                intent.putExtra("numbers", numbers);
                intent.putExtra("numbers_codes", numbers_codes);
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                intent.putExtra("frst_name", frst_name);
                intent.putExtra("last_name", last_name);
                intent.putExtra("i_doc", i_doc);
                startActivity(intent);
            }
        });
    }

    public void initialize(){
        tu_nombre_text = (EditText) findViewById(R.id.tu_nombre_text);
        tu_apaellido_text = (EditText) findViewById(R.id.tu_apaellido_text);
        tu_rut_text = (EditText) findViewById(R.id.tu_rut_text);
        confirm_guardar = (Button) findViewById(R.id.confirm_guardar);
    }

}

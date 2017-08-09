package com.falconnect.zipcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegistrationPasoThree extends AppCompatActivity {

    ActionBar actionBar;
    Button confirm_button_contrasea;
    String numbers, numbers_codes , email;
    EditText contrasena_text;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_paso_three);

        actionBar = getSupportActionBar();
        actionBar.hide();

        initialize();

        numbers = getIntent().getStringExtra("numbers");
        numbers_codes = getIntent().getStringExtra("numbers_codes");
        email = getIntent().getStringExtra("email");

        confirm_button_contrasea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password = contrasena_text.getText().toString().trim();
                Intent intent = new Intent(RegistrationPasoThree.this, RegistrationPasoFour.class);
                intent.putExtra("numbers", numbers);
                intent.putExtra("numbers_codes", numbers_codes);
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                startActivity(intent);
            }
        });
    }

    public void initialize(){

        contrasena_text = (EditText) findViewById(R.id.contrasena_text);
        confirm_button_contrasea = (Button) findViewById(R.id.confirm_button_contrasea);

    }

}

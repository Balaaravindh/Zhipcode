package com.falconnect.zipcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegistrationActivityPasotwo extends AppCompatActivity {

    ActionBar actionBar;
    Button confirm_button_email;
    String numbers, numbers_codes , email;
    EditText regist_tiemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_pasotwo);

        actionBar = getSupportActionBar();
        actionBar.hide();

        initialize();

        numbers = getIntent().getStringExtra("numbers");
        numbers_codes = getIntent().getStringExtra("numbers_codes");

        confirm_button_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = regist_tiemail.getText().toString().trim();
                Intent intent = new Intent(RegistrationActivityPasotwo.this, RegistrationPasoThree.class);
                intent.putExtra("numbers", numbers);
                intent.putExtra("numbers_codes", numbers_codes);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });
    }

    public void initialize(){
        regist_tiemail = (EditText) findViewById(R.id.regist_tiemail);
        confirm_button_email = (Button) findViewById(R.id.confirm_button_email);
    }

}

package com.falconnect.zipcode;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class FirstActivity extends AppCompatActivity {

    Button register, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_first);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        intialize();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
                startActivity(intent);
                FirstActivity.this.finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inte = new Intent(FirstActivity.this, RegistrationActivity.class);
                startActivity(inte);
                FirstActivity.this.finish();
            }
        });
    }

    public void intialize() {
        login = (Button) findViewById(R.id.login_button);
        register = (Button) findViewById(R.id.register_button);
    }

}

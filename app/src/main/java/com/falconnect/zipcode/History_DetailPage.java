package com.falconnect.zipcode;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class History_DetailPage extends AppCompatActivity {

    TextView amount, name_sec, orgin_name, destination_name, orgin_count, destination_count;
    Button volver;
    RelativeLayout relativeLayout;
    String amounts, origins, destination_size, destino;
    ImageView images;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.history_detailpage);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        intialize();

        amounts = getIntent().getStringExtra("amount");
        origins = getIntent().getStringExtra("community_list");
        destination_size = getIntent().getStringExtra("destination_size");
        destino = getIntent().getStringExtra("destination");

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                History_DetailPage.this.finish();
            }
        });

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                History_DetailPage.this.finish();
            }
        });

        double rate = Double.parseDouble(amounts);
        String values_cost = Integer.toString((int)rate);
        amount.setText(values_cost);
        orgin_name.setText(origins);
        orgin_count.setText("0");
        destination_count.setText(destination_size);
        destination_name.setText(destino);


        if(destination_size.equals("1")){
            Glide.with(History_DetailPage.this)
                    .load(R.drawable.dashboard_one)
                    .into(images);
        }else{
            Glide.with(History_DetailPage.this)
                    .load(R.drawable.dashboard_multi)
                    .into(images);
        }


    }

    public void intialize() {

        amount = (TextView) findViewById(R.id.amount_total);
        name_sec = (TextView) findViewById(R.id.seconds_reminder);
        orgin_name = (TextView) findViewById(R.id.origin_text);
        destination_name = (TextView) findViewById(R.id.destination_last);
        orgin_count = (TextView) findViewById(R.id.origin_values);
        destination_count = (TextView) findViewById(R.id.destination_values);

        images = (ImageView) findViewById(R.id.images);

        volver = (Button) findViewById(R.id.volver);

        relativeLayout = (RelativeLayout) findViewById(R.id.close_button);

    }

}

package com.falconnect.zipcode.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.falconnect.zipcode.BilleteraActivity;
import com.falconnect.zipcode.Centero_de_ayuda_Activity;
import com.falconnect.zipcode.HistrorialActivity;
import com.falconnect.zipcode.R;
import com.falconnect.zipcode.RankingActivity;
import com.falconnect.zipcode.TerminosActivity;

public class NavigationDrawerAdapter extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] web;
    private final Integer[] imageId;

    public NavigationDrawerAdapter(Activity context, String[] web, Integer[] imageId) {
        super(context, R.layout.navigation_drawer_single_item, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;

    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.navigation_drawer_single_item, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        RelativeLayout item = (RelativeLayout) rowView.findViewById(R.id.list_layout);

        txtTitle.setText(web[position]);
        imageView.setImageResource(imageId[position]);


        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(web[position].equals("BILLETERA")){
                    Intent intent = new Intent(context, BilleteraActivity.class);
                    context.startActivity(intent);
                    context.finish();
                }else if(web[position].equals("HISTORIAL")){
                    Intent intent = new Intent(context, HistrorialActivity.class);
                    context.startActivity(intent);
                    context.finish();
                } else if(web[position].equals("RANKING")){
                    Intent intent = new Intent(context, RankingActivity.class);
                    context.startActivity(intent);
                    context.finish();
                } else if(web[position].equals(String.valueOf("TÉRMINOS"))){
                    Intent intent = new Intent(context, TerminosActivity.class);
                    context.startActivity(intent);
                    context.finish();
                } else if(web[position].equals("CENTRO DE AYUDA")){
                    Intent intent = new Intent(context, Centero_de_ayuda_Activity.class);
                    context.startActivity(intent);
                    context.finish();
                }
            }
        });

        return rowView;

    }
}
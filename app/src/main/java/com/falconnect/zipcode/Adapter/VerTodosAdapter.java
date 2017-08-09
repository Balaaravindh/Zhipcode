package com.falconnect.zipcode.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.falconnect.zipcode.Delivery_Page;
import com.falconnect.zipcode.R;

import java.util.ArrayList;


public class VerTodosAdapter extends ArrayAdapter<String> {

    ViewHolder holder;
    ArrayList<String> destination;
    private Context context;
    ArrayList<String> numbers = new ArrayList<>();

    public VerTodosAdapter(Context context, ArrayList<String> destination) {
        super(context, R.layout.list_main_content, destination);
        this.context = context;
        this.destination = destination;
    }

    @Override
    public int getCount() {
        return destination.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.destination_single, null);

            holder = new ViewHolder();

            holder.origin_text = (TextView) convertView.findViewById(R.id.origin_text);
            holder.origin = (TextView) convertView.findViewById(R.id.origin);
            holder.origin_values_text = (TextView) convertView.findViewById(R.id.origin_values_text);
            holder.view1 = (View) convertView.findViewById(R.id.view1);
            holder.view2 = (View) convertView.findViewById(R.id.view2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.origin_text.setText(destination.get(position));
        holder.origin_text.setTypeface(null, Typeface.BOLD);

        for (int i= 0; i< destination.size(); i++){
            numbers.add(String.valueOf(i));
        }

        if(position == 0){
            holder.view1.setVisibility(View.GONE);
            holder.view2.setVisibility(View.VISIBLE);
        }else if (position == destination.size() - 1){
            holder.view1.setVisibility(View.VISIBLE);
            holder.view2.setVisibility(View.GONE);
        }

        holder.origin_values_text.setText(numbers.get(position));

        if(position > 0 ){
            holder.origin.setText("DESTINO");
        }else{
            holder.origin.setText("ORIGEN");
        }

        return convertView;
    }

    private class ViewHolder {
        TextView origin_text, origin_values_text ,origin ;
        View view1, view2;
    }

}
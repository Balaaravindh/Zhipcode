package com.falconnect.zipcode.Adapter;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.falconnect.zipcode.Delivery_Page;
import com.falconnect.zipcode.MapsActivity;
import com.falconnect.zipcode.R;

import java.util.ArrayList;
import java.util.HashMap;


public class HomeListViewAdapter extends ArrayAdapter<String> {

    ViewHolder holder;
    ArrayList<String> origins;
    ArrayList<String> amount;
    ArrayList<String> errand_type;
    ArrayList<String> destination_list_single;
    ArrayList<String> destination_list_multi_values;
    ArrayList<String> errand_ids;
    ArrayList<String> destinatio_ids;

    HashMap<Integer, ArrayList<String>> destination_list_multi;
    ArrayList<String> des = new ArrayList<>();
    private Context context;

    public HomeListViewAdapter(Context context, ArrayList<String> origins, ArrayList<String> errand_ids, ArrayList<String> destinatio_ids,
                               ArrayList<String> destination_list_single, ArrayList<String> destination_list_multi_values,
                               HashMap<Integer, ArrayList<String>> destination_list_multi, ArrayList<String> amount,
                               ArrayList<String> errand_type) {
        super(context, R.layout.list_main_content, errand_type);
        this.context = context;
        this.origins = origins;
        this.errand_ids = errand_ids;
        this.destinatio_ids = destinatio_ids;
        this.amount = amount;
        this.destination_list_single = destination_list_single;
        this.destination_list_multi_values = destination_list_multi_values;
        this.destination_list_multi = destination_list_multi;
        this.errand_type = errand_type;
    }

    @Override
    public int getCount() {
        return errand_type.size();
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
            convertView = inflater.inflate(R.layout.list_main_content, null);

            holder = new ViewHolder();

            holder.lacastellana = (TextView) convertView.findViewById(R.id.lacastellana);
            holder.altamira = (TextView) convertView.findViewById(R.id.altamira);
            holder.entregas = (TextView) convertView.findViewById(R.id.entregas);

            holder.images_del = (ImageView) convertView.findViewById(R.id.images_del);
            holder.button_rate = (Button) convertView.findViewById(R.id.button_rate);
            holder.category_item = (FrameLayout) convertView.findViewById(R.id.category_item);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.altamira.setText(origins.get(position));
        holder.entregas.setText(errand_type.get(position));

        String rate = amount.get(position);
        rate = rate.replace(".00", "");
        holder.button_rate.setText(rate);

        if (errand_type.get(position).equals("ENTREGAS")) {
            Glide.with(getContext())
                    .load(R.drawable.dashboard_one)
                    .into(holder.images_del);
            holder.lacastellana.setText(destination_list_single.get(position));

        } else {
            Glide.with(getContext())
                    .load(R.drawable.dashboard_multi)
                    .into(holder.images_del);
            holder.lacastellana.setText(destination_list_multi_values.get(position));

        }


        holder.category_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Delivery_Page.class);
                intent.putExtra("origin", origins.get(position));
                intent.putExtra("amount", amount.get(position));
                intent.putExtra("errand_id", errand_ids.get(position));
                Log.e("errands_ids", errand_ids.get(position));
                intent.putExtra("destination_id", destinatio_ids.get(position));
                if (errand_type.get(position).equals("ENTREGAS")) {
                    intent.putExtra("destination", destination_list_single.get(position));
                    intent.putExtra("destination_size","1");
                } else {
                    intent.putExtra("destination", destination_list_multi.get(position).get(0));
                    for (int m = 0; m < destination_list_multi.get(position).size(); m++) {
                        des.add(destination_list_multi.get(position).get(m));
                    }
                    intent.putExtra("destination_array", des);
                    intent.putExtra("destination_size", String.valueOf(des.size()));
                    intent.putExtra("one", "1");

                }
                intent.putExtra("errand_type", errand_type.get(position));
                context.startActivity(intent);
            }
        });

        return convertView;
    }



    private class ViewHolder {
        TextView lacastellana, altamira, entregas;
        ImageView images_del;
        Button button_rate;
        FrameLayout category_item;
    }

}
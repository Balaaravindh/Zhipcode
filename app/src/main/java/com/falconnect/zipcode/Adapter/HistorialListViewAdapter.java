package com.falconnect.zipcode.Adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.falconnect.zipcode.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class HistorialListViewAdapter extends ArrayAdapter<String> {

    private Context context;
    ViewHolder holder;
    ArrayList<String> dates;
    ArrayList<String> npr_details;
    ArrayList<String> gana_details;

    public HistorialListViewAdapter(Context context, ArrayList<String> dates, ArrayList<String> npr_details, ArrayList<String> gana_details) {
        super(context, R.layout.historial_list_view, dates);
        this.context = context;
        this.dates = dates;
        this.npr_details = npr_details;
        this.gana_details = gana_details;

    }

    @Override
    public int getCount() {
        return dates.size();
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
            convertView = inflater.inflate(R.layout.historial_list_view, null);

            holder = new ViewHolder();

            holder.dates_data = (TextView) convertView.findViewById(R.id.dates_data);
            holder.nro_data = (TextView) convertView.findViewById(R.id.nro_data);
            holder.ganancia_data = (TextView) convertView.findViewById(R.id.ganancia_data);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.dates_data.setText(dates.get(position).substring(0, 10));

        if (npr_details.get(position).equals("null")){
            holder.nro_data.setText("-");
        }else {
            holder.nro_data.setText(npr_details.get(position));
        }

        double rate = Double.parseDouble(gana_details.get(position));
        String amount = Integer.toString((int)rate);
        holder.ganancia_data.setText(amount);

        return convertView;
    }

    private class ViewHolder {
        TextView dates_data, nro_data, ganancia_data;
    }

}
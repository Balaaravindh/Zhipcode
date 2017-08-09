package com.falconnect.zipcode.Adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.falconnect.zipcode.R;

import java.util.ArrayList;


public class BilleteraListViewAdapter extends ArrayAdapter<String> {

    private Context context;
    ViewHolder holder;
    ArrayList<String> dates;
    ArrayList<String> amount;


    public BilleteraListViewAdapter(Context context, ArrayList<String> dates, ArrayList<String> amount) {
        super(context, R.layout.billetera_single_item, dates);
        this.context = context;
        this.dates = dates;
        this.amount = amount;

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
            convertView = inflater.inflate(R.layout.billetera_single_item, null);

            holder = new ViewHolder();

            holder.datesss = (TextView) convertView.findViewById(R.id.datesss);
            holder.amount_text = (TextView) convertView.findViewById(R.id.amount_text);
            holder.view2 = (View) convertView.findViewById(R.id.view2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if(dates.size() >= 0){
            holder.view2.setVisibility(View.VISIBLE);
        }else {

        }


        holder.datesss.setText(dates.get(position));
        double rate = Double.parseDouble(amount.get(position));
        String amount = Integer.toString((int)rate);
        holder.amount_text.setText(amount);

        return convertView;
    }

    private class ViewHolder {
        TextView datesss, amount_text;
        View view2;
    }

}
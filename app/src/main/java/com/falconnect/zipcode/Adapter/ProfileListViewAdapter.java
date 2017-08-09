package com.falconnect.zipcode.Adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.falconnect.zipcode.R;

import java.util.ArrayList;


public class ProfileListViewAdapter extends ArrayAdapter<String> {

    private Context context;
    ViewHolder holder;

    ArrayList<String> profile_details;
    ArrayList<Integer> images;

    public ProfileListViewAdapter(Context context, ArrayList<String> profile_details, ArrayList<Integer> images) {
        super(context, R.layout.profile_listview_single_item, profile_details);
        this.context = context;
        this.profile_details = profile_details;
        this.images = images;
    }

    @Override
    public int getCount() {
        return profile_details.size();
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
            convertView = inflater.inflate(R.layout.profile_listview_single_item, null);

            holder = new ViewHolder();

            holder.phone_number = (TextView) convertView.findViewById(R.id.phone_number);
            holder.call_image = (ImageView) convertView.findViewById(R.id.call_image);

            holder.view2 = (View) convertView.findViewById(R.id.view2);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(profile_details.size() >= 0){
            holder.view2.setVisibility(View.VISIBLE);
        }else {

        }

        Glide.with(getContext())
                .load(images.get(position))
                .into(holder.call_image);

        holder.phone_number.setText(profile_details.get(position));
        holder.phone_number.setTypeface(null, Typeface.BOLD);

        return convertView;
    }

    private class ViewHolder {
        TextView phone_number;
        ImageView call_image;
        View view2;
    }

}
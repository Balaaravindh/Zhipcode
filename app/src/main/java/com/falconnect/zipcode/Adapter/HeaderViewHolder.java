package com.falconnect.zipcode.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.falconnect.zipcode.R;



public class HeaderViewHolder extends RecyclerView.ViewHolder {

    TextView direction_orgins, orgin_addresss, destination_points_numbers;

    public HeaderViewHolder(View itemView) {

        super(itemView);
        direction_orgins = (TextView) itemView.findViewById(R.id.direction_orgins);
        orgin_addresss = (TextView) itemView.findViewById(R.id.orgin_addresss);
        destination_points_numbers = (TextView) itemView.findViewById(R.id.destination_points_numbers);


    }
}

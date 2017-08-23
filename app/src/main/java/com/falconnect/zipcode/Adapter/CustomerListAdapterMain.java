package com.falconnect.zipcode.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.falconnect.zipcode.R;
import java.util.ArrayList;
import java.util.HashMap;


public class CustomerListAdapterMain extends RecyclerView.Adapter<CustomerListAdapterMain.ItemViewHolder> implements RutaAdapter {

    Context mContext;
    String dest_size, communitys;
    ArrayList<String> directions;
    public HashMap<String, ArrayList<String>> ruta_postion_details;



    public CustomerListAdapterMain(Context context, String destination_size, String community,
                                   HashMap<String, ArrayList<String>> ruta_postion_detail, ArrayList<String> direction) {
        mContext = context;
        dest_size = destination_size;
        communitys = community;
        ruta_postion_details = ruta_postion_detail;
        directions = direction;

    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.destination_list, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {

        if (position == 0){
            holder.direction_orgin.setText("DIRECTION ORGIN");
            holder.orgin_address.setText(communitys);
            holder.destination_points_number.setText("0");
        }else {
            holder.direction_orgin.setText(directions.get(position));
            holder.orgin_address.setText(ruta_postion_details.get(String.valueOf(position)).get(9));
            holder.destination_points_number.setText(String.valueOf(position));
        }

    }

    @Override
    public int getItemCount() {
        return directions.size();
    }

    @Override
    public String getItem(int position) {

        return ruta_postion_details.get(String.valueOf(position)).get(position);

    }




    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public final TextView direction_orgin, orgin_address,destination_points_number;
        public final CardView card_destination;

        public ItemViewHolder(View itemView) {
            super(itemView);
            destination_points_number = (TextView) itemView.findViewById(R.id.destination_points_number);
            direction_orgin = (TextView) itemView.findViewById(R.id.direction_orgin);
            orgin_address = (TextView) itemView.findViewById(R.id.orgin_address);
            card_destination = (CardView) itemView.findViewById(R.id.card_destination);

        }
    }
}
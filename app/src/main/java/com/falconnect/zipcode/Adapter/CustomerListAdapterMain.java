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


public class CustomerListAdapterMain extends RecyclerView.Adapter<CustomerListAdapterMain.ItemViewHolder> {

    Context mContext;
    ArrayList<String> customers;
    ArrayList<String> customers_address;

    public CustomerListAdapterMain(ArrayList<String> customersss, ArrayList<String> customers_addresssss, Context context) {
        customers = customersss;
        customers_address = customers_addresssss;
        mContext = context;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.destination_list, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {

        holder.direction_orgin.setText(customers.get(position));
        holder.orgin_address.setText(customers_address.get(position));
        holder.destination_points_number.setText(String.valueOf(position));

    }

    @Override
    public int getItemCount() {
        return customers.size();
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
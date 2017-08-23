package com.falconnect.zipcode.Adapter;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.falconnect.zipcode.Model.RutaPageModel;
import com.falconnect.zipcode.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class CustomerListAdapter extends
        RecyclerView.Adapter<CustomerListAdapter.ItemViewHolder> implements ItemTouchHelperAdapter {

    Context mContext;
    String dest_size, communitys;
    ArrayList<String> directions;
    public static HashMap<String, ArrayList<String>> ruta_postion_details;
    public static ArrayList<HashMap<String, ArrayList<String>>> ruta_postion_change = new ArrayList<>();

    private OnStartDragListener mDragStartListener;
    private OnCustomerListChangedListener mListChangedListener;

    public CustomerListAdapter(Context context, String destination_size, String community,
                               HashMap<String, ArrayList<String>> ruta_postion_detail, ArrayList<String> direction,
                               OnStartDragListener dragLlistener,
                               OnCustomerListChangedListener listChangedListener) {

        mContext = context;
        mDragStartListener = dragLlistener;
        mListChangedListener = listChangedListener;

        dest_size = destination_size;
        communitys = community;
        ruta_postion_details = ruta_postion_detail;
        directions = direction;

        for (int j = 0; j < directions.size(); j++){
            ruta_postion_change.add(ruta_postion_details);
        }
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

        holder.card_destination.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (position == 0){

                }else {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        mDragStartListener.onStartDrag(holder);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return directions.size();
    }

    @Override
    public String getItem(int position) {

        return ruta_postion_details.get(String.valueOf(position)).get(position);

    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {

        if (fromPosition == 0 || toPosition == 0){

        }else {

            Collections.swap(ruta_postion_change, fromPosition, toPosition);
            Collections.swap(directions, fromPosition, toPosition);

            mListChangedListener.onNoteListChanged(ruta_postion_change);

            mListChangedListener.onNoteListChanged_Sample(directions);

            notifyItemMoved(fromPosition, toPosition);
        }
    }

    @Override
    public void onItemDismiss(int position) {

    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {
        public final TextView direction_orgin, orgin_address,destination_points_number;
        public final CardView card_destination;

        public ItemViewHolder(View itemView) {
            super(itemView);
            destination_points_number = (TextView) itemView.findViewById(R.id.destination_points_number);
            direction_orgin = (TextView) itemView.findViewById(R.id.direction_orgin);
            orgin_address = (TextView) itemView.findViewById(R.id.orgin_address);
            card_destination = (CardView) itemView.findViewById(R.id.card_destination);

        }

        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
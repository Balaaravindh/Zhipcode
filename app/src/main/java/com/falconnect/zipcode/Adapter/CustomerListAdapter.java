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

import com.falconnect.zipcode.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CustomerListAdapter extends
        RecyclerView.Adapter<CustomerListAdapter.ItemViewHolder> implements ItemTouchHelperAdapter {

    Context mContext;
    String dest_size, communitys;
    ArrayList<String> directions;
    public static HashMap<String, ArrayList<String>> ruta_postion_details;
    public static ArrayList<HashMap<String, ArrayList<String>>> ruta_postion_change = new ArrayList<>();
    private OnStartDragListener mDragStartListener;
    private OnCustomerListChangedListener mListChangedListener;

    ArrayList<ArrayList<String>> new_Array_list = new ArrayList<>();

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

        for (int j = 0; j < directions.size(); j++) {
            ruta_postion_change.add(ruta_postion_details);
        }

        for (int i = 0; i < ruta_postion_detail.size(); i++){
            new_Array_list.add(ruta_postion_detail.get(String.valueOf(i)));
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


        holder.direction_orgin.setText(directions.get(position));
        holder.orgin_address.setText(new_Array_list.get(position).get(9));
        holder.destination_points_number.setText(String.valueOf(position + 1));

        holder.card_destination.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
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

        Collections.swap(new_Array_list, fromPosition, toPosition);
        mListChangedListener.onNoteListChanged_Sample(new_Array_list);
        notifyItemMoved(fromPosition, toPosition);

        swap(fromPosition, toPosition, new_Array_list);


    }

    @Override
    public void onItemDismiss(int position) {

    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {
        public final TextView direction_orgin, orgin_address, destination_points_number;
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

    public void swap(int x, int y, ArrayList<ArrayList<String>> myList) {

        ArrayList<String> s = myList.get(x);
        ArrayList<String> ss = myList.get(y);

        myList.set(x, ss);
        myList.set(y, s);

        Log.e("size_1", myList.toString());

    }
}
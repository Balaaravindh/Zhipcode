package com.falconnect.zipcode.Adapter;

import java.util.ArrayList;
import java.util.HashMap;

public interface OnStartDragListener {

    void onStartDrag(CustomerListAdapter.ItemViewHolder viewHolder);

    void onNoteListChanged( ArrayList<HashMap<String, ArrayList<String>>> customers);

    void onNoteListChanged_Sample( ArrayList<ArrayList<String>> customers);
}
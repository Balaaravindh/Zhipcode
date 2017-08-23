package com.falconnect.zipcode.Adapter;

import java.util.ArrayList;
import java.util.HashMap;

public interface OnStartDragListener {

    void onStartDrag(CustomerListAdapter.ItemViewHolder viewHolder);

    void onNoteListChanged(ArrayList<HashMap<String, ArrayList<String>>> customers);
}
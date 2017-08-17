package com.falconnect.zipcode.Adapter;

public interface OnStartDragListener {
    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag.
     */
    void onStartDrag(CustomerListAdapter.ItemViewHolder viewHolder);
}
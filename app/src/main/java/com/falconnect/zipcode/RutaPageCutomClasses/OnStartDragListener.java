package com.falconnect.zipcode.RutaPageCutomClasses;

import com.falconnect.zipcode.Adapter.CustomerListAdapter;

public interface OnStartDragListener {
    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag.
     */
    void onStartDrag(CustomerListAdapter.ItemViewHolder viewHolder);
}
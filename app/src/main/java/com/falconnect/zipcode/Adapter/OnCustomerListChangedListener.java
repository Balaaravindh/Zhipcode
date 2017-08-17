package com.falconnect.zipcode.Adapter;

import java.util.ArrayList;

public interface OnCustomerListChangedListener {
    void onNoteListChanged(ArrayList<String> customers, ArrayList<String> customers_address);
}
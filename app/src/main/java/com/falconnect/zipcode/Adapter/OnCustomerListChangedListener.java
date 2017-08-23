package com.falconnect.zipcode.Adapter;


import java.util.ArrayList;
import java.util.HashMap;

public interface OnCustomerListChangedListener {

    void onNoteListChanged(ArrayList<HashMap<String, ArrayList<String>>>  customers);

    void onNoteListChanged_Sample(ArrayList<String>  customerss);
 }
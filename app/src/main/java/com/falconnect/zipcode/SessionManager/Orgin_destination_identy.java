package com.falconnect.zipcode.SessionManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.falconnect.zipcode.LoginActivity;

import java.util.HashMap;

public class Orgin_destination_identy {

    public static final String KEY_ORIGIN = "origin";
    private static final String PREF_NAME = "ORIGINDATAS";
    private static final String IS_LOGIN = "ISORIGIN";
    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    HashMap<String, String> user;

    public Orgin_destination_identy(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createOrginDatas(String origin) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ORIGIN, origin);

        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        user = new HashMap<String, String>();
        user.put(KEY_ORIGIN, pref.getString(KEY_ORIGIN, null));
        return user;
    }

    public void clear_orgin() {
        editor.clear();
        editor.commit();

    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

}

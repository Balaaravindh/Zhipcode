package com.falconnect.zipcode.SessionManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class Jsonstr {

    public static final String KEY_ORIGIN = "json_object";

    private static final String JSONOBJECT = "JSONOBJECT";

    private static final String ISJSONOBJECT = "ISJSONOBJECT";
    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    HashMap<String, String> user;

    public Jsonstr(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(JSONOBJECT, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createjson_object(String origin) {
        editor.putBoolean(ISJSONOBJECT, true);
        editor.putString(KEY_ORIGIN, origin);

        editor.commit();
    }

    public HashMap<String, String> getjson_object() {
        user = new HashMap<String, String>();
        user.put(KEY_ORIGIN, pref.getString(KEY_ORIGIN, null));
        return user;
    }

    public void clear_orgin() {
        editor.clear();
        editor.commit();

    }

    public boolean isLoggedIn() {
        return pref.getBoolean(ISJSONOBJECT, false);
    }

}

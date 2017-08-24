package com.falconnect.zipcode.SessionManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.falconnect.zipcode.LoginActivity;

import java.util.HashMap;

public class SessionManager {

    public static final String KEY_ID = "id";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_IDOC = "idoc";
    public static final String KEY_MOBILE = "mobile_number";
    public static final String KEY_PHOTO = "photo";
    public static final String KEY_BANK_CODE = "bank_code";
    public static final String KEY_BANK_NAME = "bank_name";
    public static final String KEY_ACCOUNT = "rut_account_number";
    public static final String KEY_PAYMENTE = "want_payment";
    public static final String KEY_BUSY = "busy";
    public static final String KEY_DEL_SINGLE = "available_for_deliveries";
    public static final String KEY_DEL_MULTI = "available_for_multiple";

    public static final String KEY_MID = "main_id";
    public static final String KEY_UNAME = "username";
    public static final String KEY_FNAME = "first_name";
    public static final String KEY_LNAME = "last_name";
    public static final String KEY_MAINEMAIL = "email";
    public static final String KEY_BALANCE = "balance";

    private static final String PREF_NAME = "AndroidHivePref";
    private static final String IS_LOGIN = "IsLoggedIn";

    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    HashMap<String, String> user;

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String id, String token, String idoc, String mobile, String image,String bank_name,
                                   String bank,String account, String payment, String main_id, String username, String f_name,
                                   String l_name, String emails, String balance, String available_for_deliveries,
                                   String available_for_multiple, String busy) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_IDOC, idoc);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_PHOTO, image);
        editor.putString(KEY_BANK_NAME, bank_name);
        editor.putString(KEY_BANK_CODE, bank);
        editor.putString(KEY_ACCOUNT, account);
        editor.putString(KEY_PAYMENTE, payment);
        editor.putString(KEY_DEL_SINGLE, available_for_deliveries);
        editor.putString(KEY_DEL_MULTI, available_for_multiple);
        editor.putString(KEY_BUSY, busy);

        editor.putString(KEY_MID, main_id);
        editor.putString(KEY_UNAME, username);
        editor.putString(KEY_FNAME, f_name);
        editor.putString(KEY_LNAME, l_name);
        editor.putString(KEY_MAINEMAIL, emails);
        editor.putString(KEY_BALANCE, balance);

        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {

        user = new HashMap<String, String>();
        user.put(KEY_ID, pref.getString(KEY_ID, null));
        user.put(KEY_TOKEN, pref.getString(KEY_TOKEN, null));
        user.put(KEY_IDOC, pref.getString(KEY_IDOC, null));
        user.put(KEY_MOBILE, pref.getString(KEY_MOBILE, null));
        user.put(KEY_PHOTO, pref.getString(KEY_PHOTO, null));
        user.put(KEY_BANK_CODE, pref.getString(KEY_BANK_CODE, null));
        user.put(KEY_BANK_NAME, pref.getString(KEY_BANK_NAME, null));
        user.put(KEY_ACCOUNT, pref.getString(KEY_ACCOUNT, null));
        user.put(KEY_PAYMENTE, pref.getString(KEY_PAYMENTE, null));
        user.put(KEY_DEL_SINGLE, pref.getString(KEY_DEL_SINGLE, null));
        user.put(KEY_DEL_MULTI, pref.getString(KEY_DEL_MULTI, null));
        user.put(KEY_BUSY, pref.getString(KEY_BUSY, null));
        user.put(KEY_MID, pref.getString(KEY_MID, null));
        user.put(KEY_UNAME, pref.getString(KEY_UNAME, null));
        user.put(KEY_FNAME, pref.getString(KEY_FNAME, null));
        user.put(KEY_LNAME, pref.getString(KEY_LNAME, null));
        user.put(KEY_MAINEMAIL, pref.getString(KEY_MAINEMAIL, null));
        user.put(KEY_BALANCE, pref.getString(KEY_BALANCE, null));

        return user;
    }

    public void clear_user() {
        editor.clear();
        editor.commit();
    }

    public void  clear(){
        Log.e("600000", "600000");
        editor.clear();
        editor.commit();
        Intent intente = new Intent(_context, LoginActivity.class);
        _context.startActivity(intente);
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

}

package com.falconnect.zipcode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import com.falconnect.zipcode.SessionManager.SessionManager;
import com.onesignal.OneSignal;

import java.util.HashMap;

public class SplashScreen extends Activity {

    private static int SPLASH_TIME_OUT = 2000;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        startService(new Intent(this, MyService.class));

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();

        if (isNetworkAvailable()) {
            if (user.get("id") == null) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        //open activity
                        Intent i = new Intent(SplashScreen.this, FirstActivity.class);
                        startActivity(i);
                        // activity finish
                        SplashScreen.this.finish();
                    }
                }, SPLASH_TIME_OUT);
            }
            else {
                //open activity
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                SplashScreen.this.finish();
            }
        } else {
            Intent intent = new Intent(SplashScreen.this, InternetConnectivity.class);
            startActivity(intent);
        }


    }

    // Check Internet Connection!!!
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}

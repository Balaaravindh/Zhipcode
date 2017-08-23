package com.falconnect.zipcode.ProgressBar;

import android.app.Activity;
import android.app.ProgressDialog;


public class AppProgress {


    ProgressDialog objProgressDialog;
    public AppProgress(Activity objActivity)
    {

        objProgressDialog = new ProgressDialog(objActivity);
        objProgressDialog.show(objActivity, "Cargando...", "Por Favor Espera...", true);
        objProgressDialog.setIndeterminate(false);
        objProgressDialog.setCancelable(false);
    }

    /**
     * Progress bar Showing
     */
    public void Show()
    {
        objProgressDialog.show();
    }

    /**
     * Progress bar Hiding
     */
    public void Hide()
    {
        objProgressDialog.dismiss();
    }
}

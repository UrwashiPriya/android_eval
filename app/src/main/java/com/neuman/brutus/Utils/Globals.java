package com.neuman.brutus.Utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;

public class Globals {

    public String PREF_COOKIES = "PREF_COOKIES";
    public String API_BASE = "http://heyneuman.com:3000/";
    public Boolean save_creds = true;

    public Globals() {}

    public ProgressDialog progressDialog(Context context, Boolean cancelable) {
        Sprite doubleBounce = new DoubleBounce();
        ProgressDialog prog_dialog = new ProgressDialog(context);
        prog_dialog.setIndeterminateDrawable(doubleBounce);
        prog_dialog.setIndeterminate(true);
        prog_dialog.setCancelable(cancelable);

        return prog_dialog;
    }
}

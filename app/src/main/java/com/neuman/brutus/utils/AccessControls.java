package com.neuman.brutus.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.JsonObject;
import com.neuman.brutus.Home;
import com.neuman.brutus.offline.mode.OffSyncUserOps;
import com.neuman.brutus.retrofit.Client;
import com.neuman.brutus.retrofit.models.SimpleResponse;


import afu.org.checkerframework.checker.oigj.qual.O;
import retrofit2.Call;
import retrofit2.Response;

public class AccessControls {

    public Context context;
    public Boolean ret = false;
    OffSyncUserOps offSyncUserOps = new OffSyncUserOps();

    public AccessControls(Context context) {
        this.context = context;
    }

    public void validate_user(String username, String password, Boolean save_creds, Context context, Intent intent) {

        offSyncUserOps.offsync_validate_user(username, password, context, intent, new retrofit2.Callback<SimpleResponse>(){
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                if (response.body() != null && response.body().getSuccess().contains("true")) {
                    offSyncUserOps.writeto_offsync(response.body(), context, 1);

                    if (save_creds) {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("username", username);
                        editor.putString("password", password);
                        editor.apply();
                    }

                    if (context != null && intent != null) {
                        context.startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) { }
        });
    }


}

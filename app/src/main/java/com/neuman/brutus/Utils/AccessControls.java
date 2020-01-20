package com.neuman.brutus.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.JsonObject;
import com.neuman.brutus.Retrofit.Client;
import com.neuman.brutus.Retrofit.models.SimpleResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class AccessControls {

    public Context context;
    public Globals g = new Globals();
    public Boolean ret = false;

    public AccessControls(Context context) {
        this.context = context;
    }

    public void validate_user(String username, String password, Boolean save_creds, Context context, Intent intent, ProgressDialog dialog) {
        JsonObject login_req = new JsonObject();
        login_req.addProperty("login", username);
        login_req.addProperty("password", password);

        dialog.show();

        Client.getService(this.context).user_login(login_req).enqueue(new retrofit2.Callback<SimpleResponse>(){
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                if (response.body() != null && response.body().getSuccess().contains("true")) {
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

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }


}

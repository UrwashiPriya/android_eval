package com.neuman.brutus.Utils;

import android.content.Context;
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

    public AccessControls(Context context) {
        this.context = context;
    }

    public boolean validate_user(String username, String password, Boolean save_creds) {

        JsonObject login_req = new JsonObject();
        login_req.addProperty("login", username);
        login_req.addProperty("password", password);

        Client.getService(this.context).user_login(login_req).enqueue(new retrofit2.Callback<SimpleResponse>(){

            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                try {
                    System.out.println("gggggg"+response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (save_creds) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = prefs.edit();

                    editor.putString("username", username);
                    editor.putString("password", password);

                    editor.apply();
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {

            }
        });

        return false;
    }
}

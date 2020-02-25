package com.neuman.brutus.offline.mode;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.neuman.brutus.retrofit.Client;
import com.neuman.brutus.retrofit.models.RomaFilters;
import com.neuman.brutus.retrofit.models.RomaResponse;
import com.neuman.brutus.retrofit.models.SimpleResponse;
import com.neuman.brutus.storage.OffSyncDbHandler;

import java.util.ArrayList;

import retrofit2.Callback;

public class OffSyncUserOps {
    private JsonObject fetch_params = null;
    private OffSyncDbHandler offSyncDbHandler;
    Gson gson = new Gson();

    public void offsync_validate_user(String username, String password, Context context, Intent intent, Callback<SimpleResponse> callback) {

        fetch_params = new JsonObject();
        fetch_params.addProperty("login", username);
        fetch_params.addProperty("password", password);

        if (isNetworkAvailable(context)) {
            Client.getService(context).user_login(fetch_params).enqueue(callback);
        } else {
            SimpleResponse response = gson.fromJson(readfrom_offsync(fetch_params, context), SimpleResponse.class);
            System.out.println("Yee Success: "+response.getSuccess());
            if (context != null && intent != null) {
                context.startActivity(intent);
            }
        }
    }

    public void writeto_offsync(SimpleResponse simpleResponse, Context context, Integer max_stored) {
        if (fetch_params != null) {
            offSyncDbHandler = new OffSyncDbHandler(context, 1);
            offSyncDbHandler.pushOffsyncRequest(fetch_params.toString(), gson.toJson(simpleResponse), "", "roma_fetch_req", 0, 0);
        }
    }

    private String readfrom_offsync(JsonObject fetch_params, Context context) {
        offSyncDbHandler = new OffSyncDbHandler(context, 1);
        return offSyncDbHandler.readOffsyncRequest(fetch_params.toString());
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}

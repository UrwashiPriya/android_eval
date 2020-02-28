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

public class UserOpsOffSync {
    private JsonObject fetch_params = null;
    private OffSyncDbHandler offSyncDbHandler;
    private Gson gson = new Gson();

    public void offsync_validate_user(String username, String password, Context context, Intent intent, Callback<SimpleResponse> callback) {

        fetch_params = new JsonObject();
        fetch_params.addProperty("login", username);
        fetch_params.addProperty("password", password);

        if (isNetworkAvailable(context)) {
            Client.getService(context).user_login(fetch_params).enqueue(callback);
        } else {
            SimpleResponse response = gson.fromJson(readfrom_offsync(fetch_params, context), SimpleResponse.class);
            if (response != null && response.getSuccess().contains("true") && intent != null) {
                context.startActivity(intent);
            }
        }
    }

    public void writeto_offsync(SimpleResponse simpleResponse, JsonObject fetch_params, String type, Context context, Integer max_stored) {
        offSyncDbHandler = new OffSyncDbHandler(context, 1);
        offSyncDbHandler.pushOffSyncRequest(fetch_params.toString(), gson.toJson(simpleResponse), "", type, 0, 1, max_stored);
    }

    private String readfrom_offsync(JsonObject fetch_params, Context context) {
        offSyncDbHandler = new OffSyncDbHandler(context, 1);
        return offSyncDbHandler.readOffSyncRequest(fetch_params.toString());
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}

package com.neuman.brutus.offline.mode;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.google.gson.JsonParser;
import com.neuman.brutus.fragments.AssetFragment;
import com.neuman.brutus.retrofit.Client;
import com.neuman.brutus.retrofit.models.AttributeReponse;
import com.neuman.brutus.retrofit.models.Roma;
import com.neuman.brutus.retrofit.models.RomaFilters;
import com.neuman.brutus.retrofit.models.RomaResponse;
import com.neuman.brutus.retrofit.models.SimpleResponse;
import com.neuman.brutus.storage.OffSyncDbHandler;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RomaOpsOffSync {
    private JsonObject fetch_params = null;
    private OffSyncDbHandler offSyncDbHandler;
    Gson gson = new Gson();

    public void createRoma(JsonObject request, Context context, Callback<SimpleResponse> callback) {
        if (isNetworkAvailable(context)) {
            Client.getService(context).create_roma(request).enqueue(callback);
        } else {
            writeRequestOffSync(request.toString(), "create_roma_request", context);
        }
    }

    public RomaResponse fetchRoma(JsonObject fetch_params, Context context, Callback<RomaResponse> callback) {
        if (isNetworkAvailable(context)) {
            Client.getService(context).roma_fetch(fetch_params).enqueue(callback);
        } else {
            return gson.fromJson(readOffSync(fetch_params, context), RomaResponse.class);
        }

        return null;
    }

    public AttributeReponse fetchRomaModuleAttrs(JsonObject fetch_params, Context context, Callback<AttributeReponse> callback) {
        if (isNetworkAvailable(context)) {
            Client.getService(context).fetch_roma_mod_attrs(fetch_params).enqueue(callback);
        } else {
            return gson.fromJson(readOffSync(fetch_params, context), AttributeReponse.class);
        }

        return null;
    }

    private void writeRequestOffSync(String request, String type, Context context) {
        offSyncDbHandler = new OffSyncDbHandler(context, 1);
        offSyncDbHandler.pushOffSyncRequest(request, "", "", type, 1, 0, null);
    }

    public void writeResponseOffSync(String romaResponseStr, String fetch_params, String type, Context context, Integer max_stored) {
        offSyncDbHandler = new OffSyncDbHandler(context, 1);
        offSyncDbHandler.pushOffSyncRequest(fetch_params, romaResponseStr, "", type, 0, 0, max_stored);
    }

    private String readOffSync(JsonObject fetch_params, Context context) {
        offSyncDbHandler = new OffSyncDbHandler(context, 1);
        return offSyncDbHandler.readOffSyncRequest(fetch_params.toString());
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}

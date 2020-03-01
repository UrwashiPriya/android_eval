package com.neuman.brutus.offline.mode;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.neuman.brutus.retrofit.Client;
import com.neuman.brutus.retrofit.models.ClusterResponse;
import com.neuman.brutus.retrofit.models.RomaFilters;
import com.neuman.brutus.retrofit.models.RomaResponse;
import com.neuman.brutus.retrofit.models.SimpleResponse;
import com.neuman.brutus.storage.OffSyncDbHandler;

import java.util.ArrayList;

import retrofit2.Callback;

public class AccountOpsOffSync {
    private JsonObject fetch_params = null;
    private OffSyncDbHandler offSyncDbHandler;
    Gson gson = new Gson();

    public ClusterResponse fetchClustersOffSync(JsonObject fetch_params, Context context, Callback<ClusterResponse> callback) {

        this.fetch_params = fetch_params;

        if (isNetworkAvailable(context)) {
            Client.getService(context).account_cluster_fetch(fetch_params).enqueue(callback);
        } else {
            return readOffSync(fetch_params, context);
        }

        return null;
    }

    public Boolean createClusterOffSync(JsonObject request, Context context, Callback<ClusterResponse> callback) {
        if (isNetworkAvailable(context)) {
            Client.getService(context).account_cluster_create(request).enqueue(callback);
            return true;
        } else {
            writeRequestOffSync(request.toString(), "create_cluster_request", context);
        }
        return false;
    }

    public Boolean editRomaOffSync(JsonObject request, Context context, Callback<SimpleResponse> callback) {
        if (isNetworkAvailable(context)) {
            Client.getService(context).update_roma(request).enqueue(callback);
            return true;
        } else {
            writeRequestOffSync(request.toString(), "edit_roma_request", context);
        }

        return false;
    }

    public void writeRequestOffSync(String request, String type, Context context) {
        offSyncDbHandler = new OffSyncDbHandler(context, 1);
        offSyncDbHandler.pushOffSyncRequest(request, "", "", type, 1, 0, null);
    }

    public void writeImageUploadRequestOffSync(String request, String code, String account, String type, Context context) {
        offSyncDbHandler = new OffSyncDbHandler(context, 1);
        offSyncDbHandler.pushOffSyncRequest(request, "", code, account, "", type, 1, 0, null);
    }

    public void writeResponseOffSync(ClusterResponse clusterResponse, JsonObject fetch_params, Context context, String type, Integer max_stored) {
        offSyncDbHandler = new OffSyncDbHandler(context, 1);
        offSyncDbHandler.pushOffSyncRequest(fetch_params.toString(), gson.toJson(clusterResponse), "", type, 0, 0, max_stored);
    }
    private ClusterResponse readOffSync(JsonObject fetch_params, Context context) {
        offSyncDbHandler = new OffSyncDbHandler(context, 1);
        return gson.fromJson(offSyncDbHandler.readOffSyncRequest(fetch_params.toString()), ClusterResponse.class);
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}

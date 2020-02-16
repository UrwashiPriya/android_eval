package com.neuman.brutus.offline.mode;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.neuman.brutus.Home;
import com.neuman.brutus.retrofit.Client;
import com.neuman.brutus.retrofit.models.RomaFilters;
import com.neuman.brutus.retrofit.models.RomaResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class RomaOps {

    Context context;
    JsonObject fetch_params = new JsonObject();

    public void offsync_fetch_roma(String account, String roma_module_id, String offset, String limit, ArrayList<RomaFilters> filters, Fragment cur, Fragment nxt, FragmentManager fragmentManager, Home homme) {
        fetch_params.addProperty("account", account);
        fetch_params.addProperty("roma_module_id", roma_module_id);
        fetch_params.add("filters", new JsonParser().parse(filters.toString()));
        fetch_params.addProperty("offset", offset);
        fetch_params.addProperty("limit", limit);


        if (isNetworkAvailable()) {
            Client.getService(context).roma_fetch(fetch_params).enqueue(new retrofit2.Callback<RomaResponse>() {
                @Override
                public void onResponse(Call<RomaResponse> call, Response<RomaResponse> response) {
                    if (response.body() != null && response.body().getSuccess().contains("true")) {
                        Bundle bundle = nxt.getArguments();
                        bundle.putSerializable("response", response.body());
                        nxt.setArguments(bundle);

                        fragmentManager.beginTransaction().hide(cur).show(nxt).commit();
                        homme.cur = nxt;
                    }
                }

                @Override
                public void onFailure(Call<RomaResponse> call, Throwable t) { }
            });
        } else {

        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}

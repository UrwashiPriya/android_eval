package com.neuman.brutus.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.neuman.brutus.retrofit.Client;
import com.neuman.brutus.retrofit.models.RomaFilters;
import com.neuman.brutus.retrofit.models.RomaResponse;

import org.json.JSONException;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class RomaOps {

    Context context;
    public RomaOps(Context context) { this.context = context; }

    public void fetch_roma(String account, String roma_module_id, String offset, String limit, ArrayList<RomaFilters> filters, Intent intent, ProgressDialog dialog) throws JSONException {
        JsonObject fetch_params = new JsonObject();
        fetch_params.addProperty("account", account);
        fetch_params.addProperty("roma_module_id", roma_module_id);
        fetch_params.add("filters", new JsonParser().parse(filters.toString()));
        fetch_params.addProperty("offset", offset);
        fetch_params.addProperty("limit", limit);

        dialog.show();

        Client.getService(this.context).roma_fetch(fetch_params).enqueue(new retrofit2.Callback<RomaResponse>(){
            @Override
            public void onResponse(Call<RomaResponse> call, Response<RomaResponse> response) {
                if (response.body() != null && response.body().getSuccess().contains("true")) {

                    System.out.println(response.body());

//                    if (context != null && intent != null) {
//                        context.startActivity(intent);
//                    }
                }

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<RomaResponse> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    public void fetch_roma(String account, String roma_module_id, String offset, String limit, ArrayList<RomaFilters> filters, Fragment cur, Fragment nxt, FragmentManager fragmentManager, ProgressBar progressBar) {
        JsonObject fetch_params = new JsonObject();
        fetch_params.addProperty("account", account);
        fetch_params.addProperty("roma_module_id", roma_module_id);
        fetch_params.add("filters", new JsonParser().parse(filters.toString()));
        fetch_params.addProperty("offset", offset);
        fetch_params.addProperty("limit", limit);

        if (progressBar != null) { progressBar.setVisibility(View.VISIBLE); }

        Client.getService(this.context).roma_fetch(fetch_params).enqueue(new retrofit2.Callback<RomaResponse>(){
            @Override
            public void onResponse(Call<RomaResponse> call, Response<RomaResponse> response) {
                if (response.body() != null && response.body().getSuccess().contains("true")) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("response", response.body());
                    nxt.setArguments(bundle);

                    System.out.println(response.body());
                    System.out.println("()()() CODE ()()() "+response.body().getRoma().get(0).getCode());

                    fragmentManager.beginTransaction().hide(cur).show(nxt).commit();
                }

                if (progressBar != null) { progressBar.setVisibility(View.INVISIBLE); }
            }

            @Override
            public void onFailure(Call<RomaResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}

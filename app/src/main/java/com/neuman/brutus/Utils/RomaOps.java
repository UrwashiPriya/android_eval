package com.neuman.brutus.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.neuman.brutus.Retrofit.Client;
import com.neuman.brutus.Retrofit.models.RomaFilters;
import com.neuman.brutus.Retrofit.models.RomaResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class RomaOps {

    Context context;
    public RomaOps(Context context) { this.context = context; }

    public void fetch_roma(String account, String roma_module_id, String offset, String limit, ArrayList<RomaFilters> filters, Intent intent, ProgressDialog dialog) {
        JsonObject fetch_params = new JsonObject();
        fetch_params.addProperty("acccount", account);
        fetch_params.addProperty("roma_module_id", roma_module_id);
        fetch_params.addProperty("filters", new Gson().toJson(filters));
        fetch_params.addProperty("offset", offset);
        fetch_params.addProperty("limit", limit);

        dialog.show();

        Client.getService(this.context).roma_fetch(fetch_params).enqueue(new retrofit2.Callback<RomaResponse>(){
            @Override
            public void onResponse(Call<RomaResponse> call, Response<RomaResponse> response) {
                if (response.body() != null && response.body().getSuccess().contains("true")) {

                    System.out.println(response.body());

                    if (context != null && intent != null) {
                        context.startActivity(intent);
                    }
                }

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<RomaResponse> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }
}

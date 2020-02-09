package com.neuman.brutus.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.neuman.brutus.Home;
import com.neuman.brutus.R;
import com.neuman.brutus.retrofit.Client;
import com.neuman.brutus.retrofit.models.AttributeReponse;
import com.neuman.brutus.retrofit.models.ClusterResponse;
import com.neuman.brutus.retrofit.models.Clusters;
import com.neuman.brutus.retrofit.models.RomaFilters;
import com.neuman.brutus.retrofit.models.RomaResponse;
import com.neuman.brutus.retrofit.models.SimpleResponse;

import org.json.JSONException;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RomaOps {

    private Context context;
    private Bundle bundle;
    public ArrayList<Clusters> existing_clusters;
    public ArrayList<Integer> clusters = new ArrayList<>();

    public RomaOps(Context context) { this.context = context; }

    public void fetch_roma(String account, String roma_module_id, String offset, String limit, ArrayList<RomaFilters> filters, Intent intent, ProgressDialog dialog) throws JSONException {
        JsonObject fetch_params = new JsonObject();
        fetch_params.addProperty("account", account);
        fetch_params.addProperty("roma_module_id", roma_module_id);
        fetch_params.add("filters", new JsonParser().parse(filters.toString()));
        fetch_params.addProperty("offset", offset);
        fetch_params.addProperty("limit", limit);

        dialog.show();

        Client.getService(context).roma_fetch(fetch_params).enqueue(new retrofit2.Callback<RomaResponse>(){
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

        Client.getService(context).roma_fetch(fetch_params).enqueue(new retrofit2.Callback<RomaResponse>(){
            @Override
            public void onResponse(Call<RomaResponse> call, Response<RomaResponse> response) {
                if (response.body() != null && response.body().getSuccess().contains("true")) {

                    Bundle bundle = nxt.getArguments();
                    bundle.putSerializable("response", response.body());
                    nxt.setArguments(bundle);

                    System.out.println(response.body());
                }

                if (progressBar != null) { progressBar.setVisibility(View.INVISIBLE); }
                fragmentManager.beginTransaction().hide(cur).show(nxt).commit();
            }

            @Override
            public void onFailure(Call<RomaResponse> call, Throwable t) {
                if (progressBar != null) { progressBar.setVisibility(View.INVISIBLE); }
                fragmentManager.beginTransaction().hide(cur).show(nxt).commit();
            }
        });
    }

    public void fetch_roma_mod_attrs(String account, String roma_module_id, String offset, String limit, Fragment cur, Fragment nxt, FragmentManager fragmentManager) {

        JsonObject fetch_params = new JsonObject();
        fetch_params.addProperty("account", account);
        fetch_params.addProperty("roma_module_id", roma_module_id);
        fetch_params.addProperty("offset", offset);
        fetch_params.addProperty("limit", limit);

        JsonObject tag_params = new JsonObject();
        tag_params.addProperty("offset", offset);
        tag_params.addProperty("limit", limit);
        tag_params.addProperty("account", account);

        Client.getService(context).fetch_roma_mod_attrs(fetch_params).enqueue(new retrofit2.Callback<AttributeReponse>(){
            @Override
            public void onResponse(Call<AttributeReponse> call, Response<AttributeReponse> response) {
                if (response.body() != null && response.body().getSuccess().contains("true")) {

                    bundle = new Bundle();
                    bundle.putSerializable("attributes", response.body().getAttributes());

                    Client.getService(context).account_cluster_fetch(tag_params).enqueue(new retrofit2.Callback<ClusterResponse>(){

                        @Override
                        public void onResponse(Call<ClusterResponse> call, Response<ClusterResponse> response) {
                            bundle.putSerializable("clusters", response.body().getClusters());
                            nxt.setArguments(bundle);

                            FragmentTransaction fragmentTransaction = fragmentManager.findFragmentByTag("add_roma")==null?fragmentManager.beginTransaction().add(R.id.frag_container, nxt, "add_roma").hide(cur).show(nxt):fragmentManager.beginTransaction().hide(cur).show(nxt);
                            fragmentTransaction.commit();
                        }

                        @Override
                        public void onFailure(Call<ClusterResponse> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<AttributeReponse> call, Throwable t) {
            }
        });
    }

    public void create_roma(Context context, JsonObject add_cluster_request, JsonObject add_roma_request, Fragment assets, Home homme) {

        System.out.println("OhWeHeee!");

        Client.getService(context).account_cluster_create(add_cluster_request).enqueue(new Callback<ClusterResponse>() {
            @Override
            public void onResponse(Call<ClusterResponse> call, Response<ClusterResponse> response) {
                if (response.body()!=null && response.body().getSuccess().equals("true")) {
                    for (Clusters cl: response.body().getClusters()) { existing_clusters.add(cl); clusters.add(cl.getId()); }
                    add_roma_request.add("cluster", new JsonParser().parse(clusters.toString()));
                    Client.getService(context).create_roma(add_roma_request).enqueue(new Callback<SimpleResponse>() {
                        @Override
                        public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                            if(response.body()!=null && response.body().getSuccess().equals("true")) {
                                homme.fragManager.transition(homme.cur, assets, null);
                            }
                        }

                        @Override
                        public void onFailure(Call<SimpleResponse> call, Throwable t) {
                            System.out.println("onFailure");
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ClusterResponse> call, Throwable t) {
                add_roma_request.add("cluster", new JsonParser().parse(clusters.toString()));
                Client.getService(context).create_roma(add_roma_request).enqueue(new Callback<SimpleResponse>() {
                    @Override
                    public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                        if(response.body()!=null && response.body().getSuccess().equals("true")) {
                            homme.fragManager.transition(homme.cur, assets, null);
                        }
                    }

                    @Override
                    public void onFailure(Call<SimpleResponse> call, Throwable t) {
                        System.out.println("onFailure");
                    }
                });
            }
        });
    }
}

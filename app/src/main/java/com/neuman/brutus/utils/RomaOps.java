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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.neuman.brutus.Home;
import com.neuman.brutus.R;
import com.neuman.brutus.fragments.AssetFragment;
import com.neuman.brutus.offline.mode.AccountOpsOffSync;
import com.neuman.brutus.offline.mode.RomaOpsOffSync;
import com.neuman.brutus.retrofit.Client;
import com.neuman.brutus.retrofit.models.AttributeReponse;
import com.neuman.brutus.retrofit.models.ClusterResponse;
import com.neuman.brutus.retrofit.models.Clusters;
import com.neuman.brutus.retrofit.models.RomaFilters;
import com.neuman.brutus.retrofit.models.RomaResponse;
import com.neuman.brutus.retrofit.models.SimpleResponse;
import com.neuman.brutus.retrofit.models.UploadResponse;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RomaOps {

    public ArrayList<Clusters> existing_clusters;
    public ArrayList<Integer> current_roma_clusters = new ArrayList<>();
    private AccountOpsOffSync accountOpsOffSync = new AccountOpsOffSync();
    private RomaOpsOffSync romaOpsOffSync = new RomaOpsOffSync();

    public RomaOps() { }

//    public void view_roma(String account, String roma_module_id, String offset, String limit, Fragment cur, Fragment nxt, FragmentManager fragmentManager, Home homme) {
//
//        JsonObject tag_params = new JsonObject();
//        tag_params.addProperty("offset", offset);
//        tag_params.addProperty("limit", limit);
//        tag_params.addProperty("account", account);
//
//
//        Client.getService(context).account_cluster_fetch(tag_params).enqueue(new retrofit2.Callback<ClusterResponse>() {
//
//            @Override
//            public void onResponse(Call<ClusterResponse> call, Response<ClusterResponse> response) {
//
//                bundle = nxt.getArguments();
//                bundle.putSerializable("clusters", response.body().getClusters());
//                nxt.setArguments(bundle);
//
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().add(R.id.frag_container, nxt, "add_roma").hide(cur).show(nxt);
//                fragmentTransaction.commit();
//
////                homme.cur = nxt;
//            }
//
//            @Override
//            public void onFailure(Call<ClusterResponse> call, Throwable t) {
//                System.out.println("error baby!! ");
//            }
//        });
//    }

//    public void fetch_roma(String account, String roma_module_id, String offset, String limit, ArrayList<RomaFilters> filters, Fragment cur, Fragment nxt, FragmentManager fragmentManager, ProgressBar progressBar, Home homme) {
//        JsonObject fetch_params = new JsonObject();
//        fetch_params.addProperty("account", account);
//        fetch_params.addProperty("roma_module_id", roma_module_id);
//        fetch_params.add("filters", new JsonParser().parse(filters.toString()));
//        fetch_params.addProperty("offset", offset);
//        fetch_params.addProperty("limit", limit);
//
//        if (progressBar != null) { progressBar.setVisibility(View.VISIBLE); }
//
//        Client.getService(context).roma_fetch(fetch_params).enqueue(new retrofit2.Callback<RomaResponse>(){
//            @Override
//            public void onResponse(Call<RomaResponse> call, Response<RomaResponse> response) {
//                if (response.body() != null && response.body().getSuccess().contains("true")) {
//
//                    Bundle bundle = nxt.getArguments();
//                    bundle.putSerializable("response", response.body());
//                    nxt.setArguments(bundle);
//                }
//
//                if (progressBar != null) { progressBar.setVisibility(View.INVISIBLE); }
//                fragmentManager.beginTransaction().hide(cur).show(nxt).commit();
////                homme.cur = nxt;
//            }
//
//            @Override
//            public void onFailure(Call<RomaResponse> call, Throwable t) {
//                if (progressBar != null) { progressBar.setVisibility(View.INVISIBLE); }
//                fragmentManager.beginTransaction().hide(cur).show(nxt).commit();
//            }
//        });
//    }

//    public void fetch_roma_mod_attrs(String account, String roma_module_id, String offset, String limit, Fragment cur, Fragment nxt, FragmentManager fragmentManager, Home homme) {
//
//        JsonObject fetch_params = new JsonObject();
//        fetch_params.addProperty("account", account);
//        fetch_params.addProperty("roma_module_id", roma_module_id);
//        fetch_params.addProperty("offset", offset);
//        fetch_params.addProperty("limit", limit);
//
//        JsonObject tag_params = new JsonObject();
//        tag_params.addProperty("offset", offset);
//        tag_params.addProperty("limit", limit);
//        tag_params.addProperty("account", account);
//
//        Client.getService(context).fetch_roma_mod_attrs(fetch_params).enqueue(new retrofit2.Callback<AttributeReponse>() {
//            @Override
//            public void onResponse(Call<AttributeReponse> call, Response<AttributeReponse> response) {
//                if (response.body() != null && response.body().getSuccess().contains("true")) {
//
//                    bundle = new Bundle();
//                    bundle.putSerializable("attributes", response.body().getAttributes());
//
//                    Client.getService(context).account_cluster_fetch(tag_params).enqueue(new retrofit2.Callback<ClusterResponse>() {
//
//                        @Override
//                        public void onResponse(Call<ClusterResponse> call, Response<ClusterResponse> response) {
//                            bundle.putSerializable("clusters", response.body().getClusters());
//                            nxt.setArguments(bundle);
//
//                            FragmentTransaction fragmentTransaction = fragmentManager.findFragmentByTag("add_roma")==null?fragmentManager.beginTransaction().add(R.id.frag_container, nxt, "add_roma").hide(cur).show(nxt):fragmentManager.beginTransaction().hide(cur).show(nxt);
//                            fragmentTransaction.commit();
//
////                            homme.cur = nxt;
//                        }
//
//                        @Override
//                        public void onFailure(Call<ClusterResponse> call, Throwable t) {
//
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onFailure(Call<AttributeReponse> call, Throwable t) {
//            }
//        });
//    }

    public void create_roma(Context context, JsonObject add_cluster_request, JsonObject add_roma_request, Home homme) {

        System.out.println("I mean blahblahsheep!!");

        if (add_cluster_request.get("cluster").getAsJsonArray().size()>0) {
            Boolean isClusAddDone = accountOpsOffSync.createClusterOffSync(add_cluster_request, context, new Callback<ClusterResponse>() {
                @Override
                public void onResponse(Call<ClusterResponse> call, Response<ClusterResponse> response) {
                    if (response.body() != null && response.body().getSuccess().equals("true")) {
                        for (Clusters cl : response.body().getClusters()) {
                            existing_clusters.add(cl);
                            current_roma_clusters.add(cl.getId());
                        }

                        add_roma_request.add("cluster", new JsonParser().parse(current_roma_clusters.toString()));
                        romaOpsOffSync.createRoma(add_roma_request, context, new Callback<SimpleResponse>() {
                            @Override
                            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                                if (response.body() != null && response.body().getSuccess().equals("true")) {
                                    homme.fragmentHandler.transition(new AssetFragment(), "VIEWASSETS", null);
                                }
                            }

                            @Override
                            public void onFailure(Call<SimpleResponse> call, Throwable t) { }
                        });
                    }
                }

                @Override
                public void onFailure(Call<ClusterResponse> call, Throwable t) { }
            });

            if (!isClusAddDone) {
                add_roma_request.add("cluster", new JsonParser().parse(current_roma_clusters.toString()));
                accountOpsOffSync.writeRequestOffSync(add_roma_request.toString(), "add_roma_request", context);
                homme.fragmentHandler.transition(new AssetFragment(), "ASSETS", null);
            }

        } else {
            add_roma_request.add("cluster", new JsonParser().parse(current_roma_clusters.toString()));
            romaOpsOffSync.createRoma(add_roma_request, context, new Callback<SimpleResponse>() {
                @Override
                public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                    if (response.body() != null && response.body().getSuccess().equals("true")) {
                        homme.fragmentHandler.transition(new AssetFragment(), "VIEWASSETS", null);
                    }
                }

                @Override
                public void onFailure(Call<SimpleResponse> call, Throwable t) { }
            });
        }
    }
//
//    public void update_roma(Context context, JsonObject add_cluster_request, JsonObject update_roma_request, Fragment assets, Home homme) {
//
//        Client.getService(context).account_cluster_create(add_cluster_request).enqueue(new Callback<ClusterResponse>() {
//            @Override
//            public void onResponse(Call<ClusterResponse> call, Response<ClusterResponse> response) {
//                if (response.body()!=null && response.body().getSuccess().equals("true")) {
//                    for (Clusters cl: response.body().getClusters()) { existing_clusters.add(cl); current_roma_clusters.add(cl.getId()); }
//
//                    update_roma_request.add("cluster", new JsonParser().parse(current_roma_clusters.toString()));
//
//                    Client.getService(context).update_roma(update_roma_request).enqueue(new Callback<SimpleResponse>() {
//                        @Override
//                        public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
//                            if(response.body()!=null && response.body().getSuccess().equals("true")) {
////                                homme.fragManager.transition(homme.cur, assets, null);
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<SimpleResponse> call, Throwable t) {
//                            System.out.println("onFailure");
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ClusterResponse> call, Throwable t) {
//                update_roma_request.add("cluster", new JsonParser().parse(current_roma_clusters.toString()));
//                Client.getService(context).update_roma(update_roma_request).enqueue(new Callback<SimpleResponse>() {
//                    @Override
//                    public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
//                        if(response.body()!=null && response.body().getSuccess().equals("true")) {
////                            homme.fragManager.transition(homme.cur, assets, null);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<SimpleResponse> call, Throwable t) {
//                        System.out.println("onFailure");
//                    }
//                });
//            }
//        });
//    }

//    public JsonArray upload_image_as_attribute(JsonArray image_attributes, String roma_code) {
//        for (int i=0; i<image_attributes.size(); i++) {
//            final int j = i;
//            File file = new File(image_attributes.get(i).getAsJsonObject().get("value").getAsString());
//            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
//
//            Client.getService(context).upload_file_as_attribute(body, "1", roma_code, image_attributes.get(i).getAsJsonObject().get("id").getAsString()).enqueue(new Callback<UploadResponse>() {
//                @Override
//                public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
//                    image_attributes.remove(j);
//                }
//
//                @Override
//                public void onFailure(Call<UploadResponse> call, Throwable t) {
//
//                }
//            });
//        }
//
//        return image_attributes;
}

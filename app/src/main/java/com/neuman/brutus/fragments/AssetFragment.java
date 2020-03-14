package com.neuman.brutus.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.neuman.brutus.Home;
import com.neuman.brutus.R;
import com.neuman.brutus.adapters.AssetListAdapter;
import com.neuman.brutus.offline.mode.RomaOpsOffSync;
import com.neuman.brutus.retrofit.models.RomaFilters;
import com.neuman.brutus.retrofit.models.RomaResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class AssetFragment extends Fragment {

    ListView listView;
    ListAdapter listAdapter;
    RomaResponse romaResponse;
    Gson gson = new Gson();
    RomaOpsOffSync offSyncRomaOps = new RomaOpsOffSync();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view_ = inflater.inflate(R.layout.fragment_assets, container, false);
        listView = view_.findViewById(R.id.asset_list);

        Home homme = (Home) getActivity();
        Button btn = view_.findViewById(R.id.add_button);
        btn.setOnClickListener(v -> {
            homme.fragmentHandler.transition(new AddRoma(), "ADDROMA", "ASSETS");
        });

        ArrayList<RomaFilters> filters = new ArrayList<>();
        JsonObject fetch_params = new JsonObject();
        fetch_params.addProperty("account", "1");
        fetch_params.addProperty("roma_module_id", "1");
        fetch_params.add("filters", new JsonParser().parse(filters.toString()));
        fetch_params.addProperty("offset", "0");
        fetch_params.addProperty("limit", "10");

        RomaResponse romaResponseOffSync = offSyncRomaOps.fetchRoma(fetch_params, getActivity(), new retrofit2.Callback<RomaResponse>(){
            @Override
            public void onResponse(Call<RomaResponse> call, Response<RomaResponse> response) {
                if (response.body() != null && response.body().getSuccess().contains("true")) {
                    romaResponse = response.body();
                    offSyncRomaOps.writeResponseOffSync(gson.toJson(romaResponse), fetch_params.toString(), "roma_fetch_req", getActivity(), 1);
                    createListAdapter();
                }
            }

            @Override
            public void onFailure(Call<RomaResponse> call, Throwable t) { }
        });

        if (romaResponseOffSync!=null && romaResponseOffSync.getSuccess().contains("true")) {
            romaResponse = romaResponseOffSync;
            createListAdapter();
        }

        return view_;
    }

    @Nullable
    @Override
    public Object getEnterTransition() {
        createListAdapter();
        return super.getEnterTransition();
    }

    void createListAdapter() {
        if (romaResponse!=null && !romaResponse.getRoma().toString().equals("false") && !romaResponse.getRoma().isEmpty()) {
            Fragment fragment = new AssetViewEdit();
            listAdapter = new AssetListAdapter(getActivity(), romaResponse.getCodeList(), romaResponse.getEqTypeList(), romaResponse.getImageList());
            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener((parent, view, position, id) -> {

                Home homme = (Home) getActivity();
                Bundle bundle = new Bundle();
                bundle.putSerializable("asset", romaResponse.getRoma().get(position));
                fragment.setArguments(bundle);

                homme.fragmentHandler.transition(fragment, "VIEWASSET", "ASSETS");
            });
        }
    }
}

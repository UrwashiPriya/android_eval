package com.neuman.brutus.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.neuman.brutus.Home;
import com.neuman.brutus.R;
import com.neuman.brutus.adapters.AssetListAdapter;
import com.neuman.brutus.offline.mode.OffSyncRomaOps;
import com.neuman.brutus.retrofit.Client;
import com.neuman.brutus.retrofit.models.RomaFilters;
import com.neuman.brutus.retrofit.models.RomaResponse;
import com.neuman.brutus.utils.RomaOps;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class AssetFragment extends Fragment {

    ListView listView;
    ListAdapter listAdapter;
    RomaResponse romaResponse;
    OffSyncRomaOps offSyncRomaOps = new OffSyncRomaOps();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view_ = inflater.inflate(R.layout.fragment_assets, container, false);
        listView = view_.findViewById(R.id.asset_list);

        Home homme = (Home) getActivity();
        Button btn = view_.findViewById(R.id.add_button);
        btn.setOnClickListener(v -> {
            homme.fragmentHandler.transition("ADDROMA", "ASSETS");
        });

        offSyncRomaOps.offsync_fetch_roma(new ArrayList<>(), "1", "0", "10", getActivity(), new retrofit2.Callback<RomaResponse>(){
            @Override
            public void onResponse(Call<RomaResponse> call, Response<RomaResponse> response) {
                if (response.body() != null && response.body().getSuccess().contains("true")) {
                    romaResponse = response.body();
                    romaResponse = offSyncRomaOps.writeto_offsync(romaResponse, 1);
                    createListAdapter();
                }
            }

            @Override
            public void onFailure(Call<RomaResponse> call, Throwable t) { }
        });

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

            listAdapter = new AssetListAdapter(getActivity(), romaResponse.getCodeList(), romaResponse.getEqTypeList(), romaResponse.getImageList());
            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener((parent, view, position, id) -> {

                Home homme = (Home) getActivity();
                Bundle bundle = new Bundle();
                bundle.putSerializable("asset", romaResponse.getRoma().get(position));

                Fragment fragment = new AssetViewEdit();
                fragment.setArguments(bundle);

                homme.fragmentHandler.transition(fragment, "VIEWASSET", "ASSETS");
            });
        }
    }
}

package com.neuman.brutus.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.neuman.brutus.R;
import com.neuman.brutus.adapters.SupplyListAdapter;
import com.neuman.brutus.retrofit.Client;
import com.neuman.brutus.retrofit.models.RomaFilters;
import com.neuman.brutus.retrofit.models.RomaResponse;
import com.neuman.brutus.utils.Tools;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class SupplyFragment extends Fragment {

    ListView listView;
    ListAdapter listAdapter;
    RomaResponse romaResponse;

    public SupplyFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("name", "Supplies");
        this.setArguments(bundle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_supplies, container, false);
        listView = view.findViewById(R.id.supply_list);

        ArrayList<RomaFilters> filters = new ArrayList<>();
        JsonObject fetch_params = new JsonObject();
        fetch_params.addProperty("account", "1");
        fetch_params.addProperty("roma_module_id", "4");
        fetch_params.add("filters", new JsonParser().parse(filters.toString()));
        fetch_params.addProperty("offset", "0");
        fetch_params.addProperty("limit", "10");

        Client.getService(getActivity()).roma_fetch(fetch_params).enqueue(new retrofit2.Callback<RomaResponse>(){
            @Override
            public void onResponse(Call<RomaResponse> call, Response<RomaResponse> response) {
                if (response.body() != null && response.body().getSuccess().contains("true")) {
                    romaResponse = response.body();
                    getEnterTransition();
                }
            }

            @Override
            public void onFailure(Call<RomaResponse> call, Throwable t) { }
        });

        return view;
    }

    @Nullable
    @Override
    public Object getEnterTransition() {

//        Bundle bundle = getArguments();
//        RomaResponse romaResponse = null;
//
//        try {
//            romaResponse = (RomaResponse) bundle.getSerializable("response");
//        } catch (NullPointerException n) {
//            Log.d("NPE Ticket", n.getMessage());
//        }
//
        if (romaResponse!=null && !romaResponse.getRoma().toString().equals("false") && !romaResponse.getRoma().isEmpty()) {

            listAdapter = new SupplyListAdapter(getActivity(), romaResponse.getCodeList(), romaResponse.getEqTypeList(), romaResponse.getImageList());

            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener((parent, view, position, id) -> {
                System.out.println("Touch" + position);
            });
        }

        return super.getEnterTransition();
    }
}

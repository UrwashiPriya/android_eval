package com.neuman.brutus.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.neuman.brutus.R;
import com.neuman.brutus.retrofit.models.RomaResponse;

public class AssetFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_assets, container, false);
        return view;
    }

    @Nullable
    @Override
    public Object getEnterTransition() {
        System.out.println("Resume Asset Page");

        try {
            Bundle bundle = getArguments();
            RomaResponse romaResponse = (RomaResponse) bundle.getSerializable("response");
            System.out.println("How'd we do? \n"+romaResponse.getRoma().get(0).getCode());
        } catch (Exception e) {
            Log.d("ERROR","MUCH Error"+e.getMessage());
        }

        return super.getEnterTransition();
    }
}

package com.neuman.brutus.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.neuman.brutus.R;
import com.neuman.brutus.retrofit.models.Roma;


public class AssetView extends Fragment {

    Roma roma;
    LinearLayout asset_layout;
    TextInputEditText textInputEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_assets, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();

        asset_layout = view.findViewById(R.id.asset_layout);

        try { roma = (Roma) bundle.getSerializable("asset"); } catch (NullPointerException n) { Log.d("NPE", n.getMessage()); }

        if (roma!=null && !roma.toString().equals("false")) {

            View v = getLayoutInflater().inflate(R.layout.widget_input_text_dark, null);
            textInputEditText = v.findViewById(R.id.textinputdis_dark);
            textInputEditText.setText(roma.getCode());
            asset_layout.addView(v);

            v = getLayoutInflater().inflate(R.layout.widget_input_text_dark, null);
            textInputEditText = v.findViewById(R.id.textinputdis_dark);
            textInputEditText.setText(String.valueOf(roma.getAdded_by()));
            asset_layout.addView(v);
        }
    }

    @Nullable
    @Override
    public Object getEnterTransition() {
        return super.getEnterTransition();
    }
}

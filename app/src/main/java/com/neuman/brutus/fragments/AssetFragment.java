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

import com.neuman.brutus.Home;
import com.neuman.brutus.R;
import com.neuman.brutus.adapters.AssetListAdapter;
import com.neuman.brutus.retrofit.models.RomaResponse;
import com.neuman.brutus.utils.RomaOps;

public class AssetFragment extends Fragment {

    ListView listView;
    ListAdapter listAdapter;
    RomaResponse romaResponse;
    AssetView asset;
    RomaOps romaOps;

    public AssetFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("name", "Assets");
        this.setArguments(bundle);
        romaOps = new RomaOps(this.getActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view_ = inflater.inflate(R.layout.fragment_assets, container, false);
        listView = view_.findViewById(R.id.asset_list);
        CreateRoma addRoma = new CreateRoma();
        Home homme = (Home) getActivity();

        Button btn = view_.findViewById(R.id.add_button);
        btn.setOnClickListener(v -> {
            romaOps.fetch_roma_mod_attrs("1", "1", "0", "100", homme.cur, addRoma, homme.fr_man);
            homme.cur = addRoma;
        });

        return view_;
    }

    @Nullable
    @Override
    public Object getEnterTransition() {

        Bundle bundle = getArguments();
        romaResponse = null;

        try { romaResponse = (RomaResponse) bundle.getSerializable("response"); } catch (NullPointerException n) { Log.d("NPE", n.getMessage()); }

        if (romaResponse!=null && !romaResponse.getRoma().toString().equals("false") && !romaResponse.getRoma().isEmpty()) {

            listAdapter = new AssetListAdapter(getActivity(), romaResponse.getCodeList(), romaResponse.getEqTypeList(), romaResponse.getImageList());
            listView.setAdapter(listAdapter);

            listView.setOnItemClickListener((parent, view, position, id) -> {

                Bundle view_bundle = new Bundle();
                view_bundle.putSerializable("asset", romaResponse.getRoma().get(position));
                asset = new AssetView();
                asset.setArguments(view_bundle);

                Home homme = (Home) getActivity();
                homme.fr_man.beginTransaction().add(R.id.frag_container, asset, "asset").hide(homme.cur).show(asset).commit();
                homme.cur = asset;
            });
        }

        return super.getEnterTransition();
    }
}

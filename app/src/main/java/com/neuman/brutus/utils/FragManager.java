package com.neuman.brutus.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.neuman.brutus.R;
import com.neuman.brutus.retrofit.models.RomaFilters;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FragManager {

    HashMap<Integer, Fragment> fragmentHashMap;
    FloatingActionButton fab;
    FragmentManager fragmentManager;
    Activity activity;
    RomaOps romaOps;

    public FragManager(HashMap<Integer, Fragment> fragmentHashMap, FragmentManager fram, FloatingActionButton fab, Activity activity, Context context, Fragment home) {
        this.fragmentHashMap = fragmentHashMap;
        this.fragmentManager = fram;
        this.fab = fab;
        this.activity = activity;
        this.romaOps = new RomaOps(context);

        Iterator iterator = this.fragmentHashMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry el = (Map.Entry)iterator.next();

            ImageView iv = activity.findViewById(Integer.valueOf(el.getKey().toString()));
            iv.setOnClickListener((View.OnClickListener) context);

            Fragment fr = (Fragment) el.getValue();

            if (fr == home) {
                fram.beginTransaction().add(R.id.frag_container, fr, el.getKey().toString()).commit();
            } else {
                fram.beginTransaction().add(R.id.frag_container, fr, el.getKey().toString()).hide(fr).commit();
            }
        }
    }

    public void transition(Fragment cur, Fragment nxt, ProgressBar progressBar) {

        String account = "1", roma_module_id = "1", offset = "0", limit = "10";
        ArrayList<RomaFilters> filters = new ArrayList<>();

        this.romaOps.fetch_roma(account, roma_module_id, offset, limit, filters, cur, nxt, fragmentManager, progressBar);
    }
}

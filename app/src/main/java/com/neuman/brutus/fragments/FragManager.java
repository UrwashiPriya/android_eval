package com.neuman.brutus.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.neuman.brutus.R;
import com.neuman.brutus.fragments.AssetView;
import com.neuman.brutus.retrofit.models.RomaFilters;
import com.neuman.brutus.retrofit.models.RomaResponse;
import com.neuman.brutus.utils.RomaOps;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FragManager {

    HashMap<Integer, Fragment> fragmentHashMap;
    FloatingActionButton fab;
    public FragmentManager fragmentManager;
    Activity activity;
    RomaOps romaOps;

    public FragManager(HashMap<Integer, Fragment> fragmentHashMap, FragmentManager fragmentManager, FloatingActionButton fab, Activity activity, Context context, Fragment home) {
        this.fragmentHashMap = fragmentHashMap;
        this.fragmentManager = fragmentManager;
        this.fab = fab;
        this.activity = activity;
        this.romaOps = new RomaOps(context);

        Iterator iterator = this.fragmentHashMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry el = (Map.Entry)iterator.next();

            ImageView iv = activity.findViewById(Integer.valueOf(el.getKey().toString()));
            iv.setOnClickListener((View.OnClickListener) context);

            Fragment fr = (Fragment) el.getValue();
            fragmentManager.beginTransaction().add(R.id.frag_container, fr, el.getKey().toString()).hide(fr).commit();
        }
    }

    public void transition(Fragment cur, Fragment nxt, ProgressBar progressBar) {

        String next, account = "1", offset = "0", limit = "10";
        ArrayList<RomaFilters> filters = new ArrayList<>();

        Bundle bundle = nxt.getArguments();
        next = bundle.getString("name");

        if (next != null && !next.equals("")) {
            switch (next) {
                case "Home":
                    fragmentManager.beginTransaction().hide(cur).show(nxt).commit();
                    break;
                case "Assets":
                    this.romaOps.fetch_roma(account, "1", offset, limit, filters, cur, nxt, fragmentManager, progressBar);
                    break;
                case "Ticket":
                    this.romaOps.fetch_roma(account, "2", offset, limit, filters, cur, nxt, fragmentManager, progressBar);
                    break;
                case "Expense":
                    this.romaOps.fetch_roma(account, "3", offset, limit, filters, cur, nxt, fragmentManager, progressBar);
                    break;
                case "Supplies":
                    this.romaOps.fetch_roma(account, "4", offset, limit, filters, cur, nxt, fragmentManager, progressBar);
                    break;
                case "Account":
                    this.romaOps.fetch_roma(account, "5", offset, limit, filters, cur, nxt, fragmentManager, progressBar);
                    break;
            }
        }



    }
}

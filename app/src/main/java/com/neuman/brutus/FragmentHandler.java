package com.neuman.brutus;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.neuman.brutus.Home;
import com.neuman.brutus.R;
import com.neuman.brutus.retrofit.models.RomaFilters;
import com.neuman.brutus.utils.Globals;
import com.neuman.brutus.utils.RomaOps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FragmentHandler {

    HashMap<Integer, Fragment> fragmentHashMap;
    FragmentManager fragmentManager;
    Activity activity;
    RomaOps romaOps;
    Home homme;
    Globals g;

    public FragmentHandler(HashMap<Integer, Fragment> fragmentHashMap, FragmentManager fragmentManager, Activity activity, Context context, Fragment home) {
        this.fragmentHashMap = fragmentHashMap;
        this.fragmentManager = fragmentManager;
        this.activity = activity;
        this.romaOps = new RomaOps(context);
        homme = (Home) context;

        for (Map.Entry<Integer, Fragment> el : this.fragmentHashMap.entrySet()) {
            ImageView iv = activity.findViewById(Integer.valueOf(el.getKey().toString()));
            iv.setOnClickListener((View.OnClickListener) context);
            Fragment fr = el.getValue();
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
                    fragmentManager.beginTransaction().hide(cur).show(g.fr_home).commit();
                    break;
                case "Assets":
                    this.romaOps.fetch_roma(account, "1", offset, limit, filters, cur, nxt, fragmentManager, progressBar, homme);
                    break;
                case "Ticket":
                    this.romaOps.fetch_roma(account, "2", offset, limit, filters, cur, nxt, fragmentManager, progressBar, homme);
                    break;
                case "Expense":
                    this.romaOps.fetch_roma(account, "3", offset, limit, filters, cur, nxt, fragmentManager, progressBar, homme);
                    break;
                case "Supplies":
                    this.romaOps.fetch_roma(account, "4", offset, limit, filters, cur, nxt, fragmentManager, progressBar, homme);
                    break;
                case "Account":
                    this.romaOps.fetch_roma(account, "5", offset, limit, filters, cur, nxt, fragmentManager, progressBar, homme);
                    break;
            }
        }



    }
}

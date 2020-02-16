package com.neuman.brutus.utils;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.fragment.app.Fragment;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.gson.JsonArray;
import com.neuman.brutus.R;
import com.neuman.brutus.fragments.AssetFragment;
import com.neuman.brutus.fragments.HomeFragment;
import com.neuman.brutus.fragments.MoreFragment;
import com.neuman.brutus.fragments.OrgFragment;
import com.neuman.brutus.fragments.SupplyFragment;
import com.neuman.brutus.fragments.TicketFragment;
import com.neuman.brutus.retrofit.models.Attributes;

import java.util.ArrayList;
import java.util.HashMap;

public class Globals {

    public String PREF_COOKIES = "PREF_COOKIES";
    public String API_BASE = "http://heyneuman.com:3000/";
    public Boolean save_creds = true;
    public ArrayList<Attributes> roma_attributes;
    public JsonArray attribute_request = new JsonArray();

    public HashMap<Integer, Fragment> fragments = new HashMap<>();

    final public Fragment fr_home = new HomeFragment();
    final public Fragment fr_assets = new AssetFragment();
    final public Fragment fr_tickets = new TicketFragment();
    final public Fragment fr_org = new OrgFragment();
    final public Fragment fr_spares = new SupplyFragment();
    final public Fragment fr_more = new MoreFragment();

    public Fragment cur, nxt;

    public Globals() {
        fragments.put(R.id.menu_nav_1, fr_home);
        fragments.put(R.id.menu_nav_2, fr_assets);
        fragments.put(R.id.menu_nav_3, fr_tickets);
        fragments.put(R.id.menu_nav_4, fr_org);
        fragments.put(R.id.menu_nav_5, fr_spares);
        fragments.put(R.id.fab_btn, fr_more);
    }

    public ProgressDialog progressDialog(Context context, Boolean cancelable) {
        Sprite doubleBounce = new DoubleBounce();
        ProgressDialog prog_dialog = new ProgressDialog(context);
        prog_dialog.setIndeterminateDrawable(doubleBounce);
        prog_dialog.setIndeterminate(true);
        prog_dialog.setCancelable(cancelable);

        return prog_dialog;
    }
}


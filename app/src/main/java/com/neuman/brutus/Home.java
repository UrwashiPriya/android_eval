package com.neuman.brutus;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.neuman.brutus.fragments.AssetFragment;
import com.neuman.brutus.fragments.HomeFragment;
import com.neuman.brutus.fragments.OrgFragment;
import com.neuman.brutus.fragments.SupplyFragment;
import com.neuman.brutus.fragments.TicketFragment;
import com.neuman.brutus.fragments.FragManager;

import java.util.HashMap;

public class Home extends AppCompatActivity implements View.OnClickListener{

    FloatingActionButton fab;
    public FragManager fragManager;
    ProgressBar spinner = null;
    public FragmentManager fr_man = getSupportFragmentManager();

    HashMap<Integer, Fragment> fragmentHashMap = new HashMap<>();

    public Fragment cur;

    final Fragment fr_home = new HomeFragment();
    final Fragment fr_assets = new AssetFragment();
    final Fragment fr_tickets = new TicketFragment();
    final Fragment fr_org = new OrgFragment();
    final Fragment fr_spares = new SupplyFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        fragmentHashMap.put(R.id.menu_nav_1, fr_home);
        fragmentHashMap.put(R.id.menu_nav_2, fr_assets);
        fragmentHashMap.put(R.id.menu_nav_3, fr_tickets);
        fragmentHashMap.put(R.id.menu_nav_4, fr_org);
        fragmentHashMap.put(R.id.menu_nav_5, fr_spares);

        fab = findViewById(R.id.fab_btn);

        fragManager = new FragManager(fragmentHashMap, fr_man, fab,this, this, fr_home);
        fr_man.beginTransaction().show(fr_home).commit();
        cur = fr_home;
    }

    @Override
    public void onClick(View v) {
        System.out.println("kokoko");
        Fragment next = fragmentHashMap.get(v.getId());
        if (next!=cur && next!=null) {
            fragManager.transition(cur, next, spinner);
            cur = next;
        }
    }
}

package com.neuman.brutus;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.neuman.brutus.utils.Globals;

import java.util.HashMap;

public class Home extends AppCompatActivity implements View.OnClickListener{

    public FragmentHandler fragManager;
    public FragmentManager fr_man = getSupportFragmentManager();
    ProgressBar spinner = null;
    Globals g = new Globals();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        fragManager = new FragmentHandler(g.fragments, fr_man,this, this, g);
        fr_man.beginTransaction().show(g.fr_home).commit();
        g.cur = g.fr_home;

        ImageView iv = findViewById(R.id.menu_nav_1);
        iv.setOnClickListener(this);
        getSupportFragmentManager().beginTransaction().add(R.id.frag_container, g.fr_home, "home").commit();
    }

    @Override
    public void onClick(View v) {
        String next = v.getTag().toString();
        if (next!=g.cur && next!=null) {
            fragManager.transition(g.cur, next, spinner);
            g.cur = next;
        }
    }
}

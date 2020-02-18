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

    public FragmentHandler fragmentHandler;
    public FragmentManager fr_man = getSupportFragmentManager();
    Globals g;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        g = new Globals();
        g.addFragments(getSupportFragmentManager());

        imageView = findViewById(R.id.menu_nav_1);
        imageView.setTag("HOME");
        imageView.setOnClickListener(this);

        imageView = findViewById(R.id.menu_nav_2);
        imageView.setTag("ASSETS");
        imageView.setOnClickListener(this);

        imageView = findViewById(R.id.menu_nav_3);
        imageView.setTag("TICKETS");
        imageView.setOnClickListener(this);

        imageView = findViewById(R.id.menu_nav_4);
        imageView.setTag("ORGANISATION");
        imageView.setOnClickListener(this);

        imageView = findViewById(R.id.menu_nav_5);
        imageView.setTag("SPARES");
        imageView.setOnClickListener(this);

        imageView = findViewById(R.id.fab_btn);
        imageView.setTag("MORE");
        imageView.setOnClickListener(this);

        fragmentHandler = new FragmentHandler(fr_man, g);
        getSupportFragmentManager().beginTransaction().show(g.fragments.get("HOME")).commit();
        g.cur = "HOME";
    }

    @Override
    public void onClick(View v) {
        String next = v.getTag().toString();
        if (!next.equals(g.cur)) {
            fragmentHandler.transition(next);
            g.cur = next;
        }
    }
}

package com.neuman.brutus;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.neuman.brutus.utils.Globals;

public class FragmentHandler {

    FragmentManager fragmentManager;
    Globals g;

    FragmentHandler(FragmentManager fragmentManager, Globals g) {
        this.fragmentManager = fragmentManager;
        this.g = g;
    }

    public void transition(String next) {
        if (next != null && !next.equals("") && !next.equals(g.cur) && !g.cur.equals("")) {
            fragmentManager.beginTransaction().hide(g.fragments.get(g.cur)).show(g.fragments.get(next)).commit();
            g.cur = next;
        }
    }

    public void transition(Fragment next, String nxt) {
        if (nxt != null && !nxt.equals("") && !nxt.equals(g.cur) && !g.cur.equals("")) {
            fragmentManager.beginTransaction().add(R.id.frag_container, next, nxt).hide(g.fragments.get(g.cur)).show(next).commit();
            g.fragments.put(nxt, next);
            g.cur = nxt;
        }
    }
}

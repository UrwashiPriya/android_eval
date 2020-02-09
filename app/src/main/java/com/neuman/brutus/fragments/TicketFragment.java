package com.neuman.brutus.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.neuman.brutus.R;
import com.neuman.brutus.adapters.TicketListAdapter;
import com.neuman.brutus.retrofit.models.RomaResponse;
import com.neuman.brutus.utils.Tools;

public class TicketFragment extends Fragment {

    ListView listView;
    ListAdapter listAdapter;

    public TicketFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("name", "Ticket");
        this.setArguments(bundle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tickets, container, false);
        listView = view.findViewById(R.id.ticket_list);

        return view;
    }

    @Nullable
    @Override
    public Object getEnterTransition() {

        Bundle bundle = getArguments();
        RomaResponse romaResponse = null;

        try {
            romaResponse = (RomaResponse) bundle.getSerializable("response");
        } catch (NullPointerException n) {
            Log.d("NPE Ticket", n.getMessage());
        }

        if (romaResponse!=null && !romaResponse.getRoma().toString().equals("false") && !romaResponse.getRoma().isEmpty()) {

            listAdapter = new TicketListAdapter(getActivity(), romaResponse.getCodeList(), romaResponse.getEqTypeList(), romaResponse.getImageList());

            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener((parent, view, position, id) -> {
                System.out.println("Touch"+position);
            });
        }

        return super.getEnterTransition();
    }
}

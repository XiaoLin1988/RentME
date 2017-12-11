package com.android.jianchen.rentme.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.jianchen.rentme.Adapter.EditDoodleAdapter;
import com.android.jianchen.rentme.Models.Effect;
import com.android.jianchen.rentme.PhotoEditActivity;
import com.android.jianchen.rentme.R;
import com.android.jianchen.rentme.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emerald on 6/15/2017.
 */
public class EditOverlayDoodleFragment extends Fragment {
    private RecyclerView recyclerView;
    private EditDoodleAdapter adapter;
    private List<Effect> itemList;

    public EditOverlayDoodleFragment() {
        itemList = new ArrayList<>();
        Effect item0 = new Effect(Constants.ACTION_DOODLE, R.drawable.doodle1, R.drawable.doodle1, "Doodle1");
        itemList.add(item0);
        Effect item1 = new Effect(Constants.ACTION_DOODLE, R.drawable.doodle2, R.drawable.doodle2, "Doodle1");
        itemList.add(item1);
        Effect item2 = new Effect(Constants.ACTION_DOODLE, R.drawable.doodle3, R.drawable.doodle3, "Doodle3");
        itemList.add(item2);
        //Item item3 = new Item(R.drawable.item3, R.drawable.item3);
        //itemList.add(item3);
        //Effect item4 = new Effect(R.drawable.item4, R.drawable.item4);
        //itemList.add(item4);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        adapter = new EditDoodleAdapter(itemList, ((PhotoEditActivity)(getActivity())).itemClickListener());
        View view = inflater.inflate(R.layout.fragment_edit_overlay_doodles, null);
        recyclerView = (RecyclerView)view.findViewById(R.id.list_item);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getBaseContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }
}

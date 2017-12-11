package com.android.jianchen.rentme.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.jianchen.rentme.Adapter.EditScaleAdapter;
import com.android.jianchen.rentme.Models.Effect;
import com.android.jianchen.rentme.PhotoEditActivity;
import com.android.jianchen.rentme.R;
import com.android.jianchen.rentme.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emerald on 6/15/2017.
 */
public class EditScaleFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditScaleAdapter adapter;
    private List<Effect> effectList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_edit_scale, null);
        effectList = new ArrayList<>();
        /*
        Effect effect1 = new Effect(Constants.ACTION_CROP, R.drawable.edit_crop_disable, R.drawable.edit_crop_enable, "Crop");
        effectList.add(effect1);
        */
        Effect effect2 = new Effect(Constants.ACTION_ROTATE_RIGHT, R.drawable.edit_right_disable, R.drawable.edit_right_enable, "Right");
        effectList.add(effect2);
        Effect effect3 = new Effect(Constants.ACTION_ROTATE_LEFT, R.drawable.edit_left_disable, R.drawable.edit_left_enable, "Left");
        effectList.add(effect3);
        Effect effect4 = new Effect(Constants.ACTION_FLIP_HORIZONTAL, R.drawable.edit_flip_horizontal_disable, R.drawable.edit_flip_horizontal_enable, "Flip");
        effectList.add(effect4);
        Effect effect5 = new Effect(Constants.ACTION_FLIP_VERTICAL, R.drawable.edit_flip_vertical_disable, R.drawable.edit_flip_vertical_enable, "Upside");
        effectList.add(effect5);
        adapter = new EditScaleAdapter(effectList, ((PhotoEditActivity)getActivity()).effectClickLister());
        recyclerView = (RecyclerView)view.findViewById(R.id.list_effect);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getBaseContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }
}


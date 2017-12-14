package com.android.jianchen.rentme.activity.me.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.jianchen.rentme.activity.me.adapter.EditScaleAdapter;
import com.android.jianchen.rentme.model.rentme.EffectModel;
import com.android.jianchen.rentme.activity.me.PhotoEditActivity;
import com.android.jianchen.rentme.R;
import com.android.jianchen.rentme.helper.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emerald on 6/15/2017.
 */
public class EditScaleFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditScaleAdapter adapter;
    private List<EffectModel> effectModelList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_edit_scale, null);
        effectModelList = new ArrayList<>();
        /*
        EffectModel effect1 = new EffectModel(Constants.ACTION_CROP, R.drawable.edit_crop_disable, R.drawable.edit_crop_enable, "Crop");
        effectModelList.add(effect1);
        */
        EffectModel effectModel2 = new EffectModel(Constants.ACTION_ROTATE_RIGHT, R.drawable.edit_right_disable, R.drawable.edit_right_enable, "Right");
        effectModelList.add(effectModel2);
        EffectModel effectModel3 = new EffectModel(Constants.ACTION_ROTATE_LEFT, R.drawable.edit_left_disable, R.drawable.edit_left_enable, "Left");
        effectModelList.add(effectModel3);
        EffectModel effectModel4 = new EffectModel(Constants.ACTION_FLIP_HORIZONTAL, R.drawable.edit_flip_horizontal_disable, R.drawable.edit_flip_horizontal_enable, "Flip");
        effectModelList.add(effectModel4);
        EffectModel effectModel5 = new EffectModel(Constants.ACTION_FLIP_VERTICAL, R.drawable.edit_flip_vertical_disable, R.drawable.edit_flip_vertical_enable, "Upside");
        effectModelList.add(effectModel5);
        adapter = new EditScaleAdapter(effectModelList, ((PhotoEditActivity)getActivity()).effectClickLister());
        recyclerView = (RecyclerView)view.findViewById(R.id.list_effect);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getBaseContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }
}


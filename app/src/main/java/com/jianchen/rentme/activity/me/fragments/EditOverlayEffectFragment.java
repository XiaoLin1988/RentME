package com.jianchen.rentme.activity.me.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jianchen.rentme.activity.me.adapter.EditEffectAdapter;
import com.jianchen.rentme.model.photoedit.EffectModel;
import com.jianchen.rentme.activity.me.PhotoEditActivity;
import com.jianchen.rentme.R;
import com.jianchen.rentme.helper.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emerald on 6/15/2017.
 */
public class EditOverlayEffectFragment extends Fragment {
    private RecyclerView recyclerView;
    private EditEffectAdapter adapter;
    private List<EffectModel> filterList;

    public EditOverlayEffectFragment() {
        filterList = new ArrayList<>();
        EffectModel filter0 = new EffectModel(Constants.ACTION_ORIGINAL, R.drawable.effect_original, R.drawable.effect_original, "Original");
        filterList.add(filter0);
        EffectModel filter1 = new EffectModel(Constants.ACTION_BOOST, R.drawable.effect_boost, R.drawable.effect_boost, "Boost");
        filterList.add(filter1);
        EffectModel filter2 = new EffectModel(Constants.ACTION_COLORDEPTH, R.drawable.effect_colordepth, R.drawable.effect_colordepth, "ColorDepth");
        filterList.add(filter2);
        EffectModel filter3 = new EffectModel(Constants.ACTION_COLORFILTER, R.drawable.effect_colorbalance, R.drawable.effect_colorbalance, "Color Filter");
        filterList.add(filter3);
        EffectModel filter4 = new EffectModel(Constants.ACTION_EMBOSS, R.drawable.effect_emboss, R.drawable.effect_emboss, "Emboss");
        filterList.add(filter4);
        EffectModel filter5 = new EffectModel(Constants.ACTION_GAMMA, R.drawable.effect_gamma, R.drawable.effect_gamma, "Gamma");
        filterList.add(filter5);
        EffectModel filter6 = new EffectModel(Constants.ACTION_GAUSSIAN, R.drawable.effect_gaussian, R.drawable.effect_gaussian, "GaussianBlur");
        filterList.add(filter6);
        EffectModel filter7 = new EffectModel(Constants.ACTION_GRAYSCALE, R.drawable.effect_grayscale, R.drawable.effect_grayscale, "Grayscale");
        filterList.add(filter7);
        EffectModel filter8 = new EffectModel(Constants.ACTION_HUE, R.drawable.effect_invert, R.drawable.effect_invert, "Hue");
        filterList.add(filter8);
        EffectModel filter9 = new EffectModel(Constants.ACTION_INVERT, R.drawable.effect_hue, R.drawable.effect_hue, "Invert");
        filterList.add(filter9);
        EffectModel filter10 = new EffectModel(Constants.ACTION_NOISE, R.drawable.effect_noise, R.drawable.effect_noise, "Noise");
        filterList.add(filter10);
        EffectModel filter11 = new EffectModel(Constants.ACTION_SEPIA, R.drawable.effect_sepia, R.drawable.effect_sepia, "Sepia");
        filterList.add(filter11);
        EffectModel filter12 = new EffectModel(Constants.ACTION_SHARPEN, R.drawable.effect_sharpen, R.drawable.effect_sharpen, "Sharpen");
        filterList.add(filter12);
        EffectModel filter13 = new EffectModel(Constants.ACTION_SKETCH, R.drawable.effect_sketch, R.drawable.effect_sketch, "Sketch");
        filterList.add(filter13);
        EffectModel filter14 = new EffectModel(Constants.ACTION_TINT, R.drawable.effect_tint, R.drawable.effect_tint, "Tint");
        filterList.add(filter14);
        EffectModel filter15 = new EffectModel(Constants.ACTION_VIGNETTE, R.drawable.effect_vignette, R.drawable.effect_vignette, "Vignette");
        filterList.add(filter15);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        adapter = new EditEffectAdapter(filterList, ((PhotoEditActivity)(getActivity())).filterClickListener());
        View view = inflater.inflate(R.layout.fragment_edit_overlay_effects, null);
        recyclerView = (RecyclerView)view.findViewById(R.id.list_filter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getBaseContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }
}

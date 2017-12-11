package com.android.jianchen.rentme.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.jianchen.rentme.Adapter.EditEffectAdapter;
import com.android.jianchen.rentme.Models.Effect;
import com.android.jianchen.rentme.PhotoEditActivity;
import com.android.jianchen.rentme.R;
import com.android.jianchen.rentme.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emerald on 6/15/2017.
 */
public class EditOverlayEffectFragment extends Fragment {
    private RecyclerView recyclerView;
    private EditEffectAdapter adapter;
    private List<Effect> filterList;

    public EditOverlayEffectFragment() {
        filterList = new ArrayList<>();
        Effect filter0 = new Effect(Constants.ACTION_ORIGINAL, R.drawable.effect_original, R.drawable.effect_original, "Original");
        filterList.add(filter0);
        Effect filter1 = new Effect(Constants.ACTION_BOOST, R.drawable.effect_boost, R.drawable.effect_boost, "Boost");
        filterList.add(filter1);
        Effect filter2 = new Effect(Constants.ACTION_COLORDEPTH, R.drawable.effect_colordepth, R.drawable.effect_colordepth, "ColorDepth");
        filterList.add(filter2);
        Effect filter3 = new Effect(Constants.ACTION_COLORFILTER, R.drawable.effect_colorbalance, R.drawable.effect_colorbalance, "Color Filter");
        filterList.add(filter3);
        Effect filter4 = new Effect(Constants.ACTION_EMBOSS, R.drawable.effect_emboss, R.drawable.effect_emboss, "Emboss");
        filterList.add(filter4);
        Effect filter5 = new Effect(Constants.ACTION_GAMMA, R.drawable.effect_gamma, R.drawable.effect_gamma, "Gamma");
        filterList.add(filter5);
        Effect filter6 = new Effect(Constants.ACTION_GAUSSIAN, R.drawable.effect_gaussian, R.drawable.effect_gaussian, "GaussianBlur");
        filterList.add(filter6);
        Effect filter7 = new Effect(Constants.ACTION_GRAYSCALE, R.drawable.effect_grayscale, R.drawable.effect_grayscale, "Grayscale");
        filterList.add(filter7);
        Effect filter8 = new Effect(Constants.ACTION_HUE, R.drawable.effect_invert, R.drawable.effect_invert, "Hue");
        filterList.add(filter8);
        Effect filter9 = new Effect(Constants.ACTION_INVERT, R.drawable.effect_hue, R.drawable.effect_hue, "Invert");
        filterList.add(filter9);
        Effect filter10 = new Effect(Constants.ACTION_NOISE, R.drawable.effect_noise, R.drawable.effect_noise, "Noise");
        filterList.add(filter10);
        Effect filter11 = new Effect(Constants.ACTION_SEPIA, R.drawable.effect_sepia, R.drawable.effect_sepia, "Sepia");
        filterList.add(filter11);
        Effect filter12 = new Effect(Constants.ACTION_SHARPEN, R.drawable.effect_sharpen, R.drawable.effect_sharpen, "Sharpen");
        filterList.add(filter12);
        Effect filter13 = new Effect(Constants.ACTION_SKETCH, R.drawable.effect_sketch, R.drawable.effect_sketch, "Sketch");
        filterList.add(filter13);
        Effect filter14 = new Effect(Constants.ACTION_TINT, R.drawable.effect_tint, R.drawable.effect_tint, "Tint");
        filterList.add(filter14);
        Effect filter15 = new Effect(Constants.ACTION_VIGNETTE, R.drawable.effect_vignette, R.drawable.effect_vignette, "Vignette");
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

package com.jianchen.rentme.activity.me.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jianchen.rentme.R;

/**
 * Created by emerald on 6/15/2017.
 */
public class EditOverlayFragment extends Fragment implements View.OnClickListener {
    private EditOverlayFrameFragment frameFragment;
    private EditOverlayDoodleFragment doodleFragment;
    private EditOverlayEffectFragment effectFragment;

    private Button frame, item, filter;

    public EditOverlayFragment() {
        frameFragment = new EditOverlayFrameFragment();
        doodleFragment = new EditOverlayDoodleFragment();
        effectFragment = new EditOverlayEffectFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_edit_overlay, null);

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.subeffectselector, frameFragment);
        ft.commit();

        frame = (Button)view.findViewById(R.id.overlay_frame);
        frame.setOnClickListener(this);

        item = (Button)view.findViewById(R.id.overlay_doodle);
        item.setTextColor(Color.parseColor("#959595"));
        item.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        item.setOnClickListener(this);

        filter = (Button)view.findViewById(R.id.overlay_effect);
        filter.setTextColor(Color.parseColor("#959595"));
        filter.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        filter.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.overlay_frame) {
            /*
            ((PhotoEditActivity)getActivity()).setCurrentBitmap();
            ((PhotoEditActivity)getActivity()).image_holder.deleteDoodle();
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.replace(R.id.subeffectselector, frameFragment);
            ft.commit();
            */
            frame.setBackground(getResources().getDrawable(R.drawable.overlay_sub_sel));
            frame.setTextColor(Color.parseColor("#E5E5E5"));

            item.setTextColor(Color.parseColor("#959595"));
            item.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            filter.setTextColor(Color.parseColor("#959595"));
            filter.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } else if (v.getId() == R.id.overlay_doodle) {
            /*
            ((PhotoEditActivity)getActivity()).setCurrentBitmap();
            ((PhotoEditActivity)getActivity()).image_holder.deleteDoodle();
            */
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.replace(R.id.subeffectselector, doodleFragment);
            ft.commit();

            item.setBackground(getResources().getDrawable(R.drawable.overlay_sub_sel));
            item.setTextColor(Color.parseColor("#E5E5E5"));

            frame.setTextColor(Color.parseColor("#959595"));
            frame.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            filter.setTextColor(Color.parseColor("#959595"));
            filter.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } else if (v.getId() == R.id.overlay_effect) {
            /*
            ((PhotoEditActivity)getActivity()).setCurrentBitmap();
            ((PhotoEditActivity)getActivity()).image_holder.deleteDoodle();
            */
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.replace(R.id.subeffectselector, effectFragment);
            ft.commit();

            filter.setBackground(getResources().getDrawable(R.drawable.overlay_sub_sel));
            filter.setTextColor(Color.parseColor("#E5E5E5"));

            item.setTextColor(Color.parseColor("#959595"));
            item.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            frame.setTextColor(Color.parseColor("#959595"));
            frame.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}

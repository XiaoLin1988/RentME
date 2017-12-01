package com.android.emerald.rentme.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.android.emerald.rentme.PhotoEditActivity;
import com.android.emerald.rentme.R;

/**
 * Created by emerald on 6/15/2017.
 */
public class EditFilterFragment extends Fragment {
    private SeekBar seekBar1;
    private SeekBar seekBar2;
    private SeekBar seekBar3;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_edit_filter, null);
        seekBar1 = (SeekBar)view.findViewById(R.id.seek1);
        seekBar1.setMax(255);
        //seekBar1.setProgress(127);
        seekBar1.setTag(1);

        seekBar1.setOnSeekBarChangeListener((PhotoEditActivity) getActivity());

        seekBar2 = (SeekBar)view.findViewById(R.id.seek2);
        seekBar2.setMax(100);
        //seekBar2.setProgress(127);
        seekBar2.setTag(2);
        seekBar2.setOnSeekBarChangeListener((PhotoEditActivity) getActivity());

        seekBar3 = (SeekBar)view.findViewById(R.id.seek3);
        seekBar3.setMax(100);
        //seekBar3.setProgress(127);
        seekBar3.setTag(3);
        seekBar3.setOnSeekBarChangeListener((PhotoEditActivity) getActivity());
        seekBar1.setProgress(0);
        seekBar2.setProgress(0);
        seekBar3.setProgress(0);
        return view;
    }
}

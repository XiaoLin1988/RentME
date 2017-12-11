package com.android.jianchen.rentme.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.jianchen.rentme.Adapter.EditFrameAdapter;
import com.android.jianchen.rentme.Models.Effect;
import com.android.jianchen.rentme.PhotoEditActivity;
import com.android.jianchen.rentme.R;
import com.android.jianchen.rentme.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emerald on 6/15/2017.
 */
public class EditOverlayFrameFragment extends Fragment {
    private RecyclerView recyclerView;
    private EditFrameAdapter adapter;
    private List<Effect> frameList;

    public EditOverlayFrameFragment() {
        frameList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Effect frame = new Effect(Constants.ACTION_FRAME, R.drawable.frame001 + i, R.drawable.frame001 + i, "frame");
            frameList.add(frame);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        adapter = new EditFrameAdapter(frameList, ((PhotoEditActivity)(getParentFragment().getActivity())).frameClickListener());
        View view = inflater.inflate(R.layout.fragment_edit_overlay_frames, null);
        recyclerView = (RecyclerView)view.findViewById(R.id.list_frames);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getBaseContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }
}

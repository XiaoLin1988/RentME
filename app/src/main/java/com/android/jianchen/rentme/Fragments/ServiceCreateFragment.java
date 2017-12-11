package com.android.jianchen.rentme.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.jianchen.rentme.Interface.OnSkillSelectListener;
import com.android.jianchen.rentme.Models.SkillModel;
import com.android.jianchen.rentme.R;

/**
 * Created by emerald on 6/26/2017.
 */
public class ServiceCreateFragment extends Fragment implements OnSkillSelectListener {
    private Context context;

    private Fragment step0;
    private Fragment step1;

    public static ServiceCreateFragment newInstance(Context ctx) {
        ServiceCreateFragment fragment = new ServiceCreateFragment();
        fragment.context = ctx;
        fragment.prepareData();
        return fragment;
    }

    private void prepareData() {
        step0 = ProjectCreate0Fragment.newInstance(context, this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_create, container, false);

        FragmentManager fm = getChildFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        fm.beginTransaction();

        ft.add(R.id.service_create_container, step0);
        ft.commit();

        return view;
    }

    @Override
    public void onSkillSelect(SkillModel item) {
        step1 = ServiceCreate1Fragment.newInstance(context, item);

        FragmentManager fm = getChildFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        fm.beginTransaction();

        ft.replace(R.id.service_create_container, step1);
        ft.commit();
    }
}

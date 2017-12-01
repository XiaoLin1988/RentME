package com.android.emerald.rentme.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.emerald.rentme.MainActivity;
import com.android.emerald.rentme.Models.UserModel;
import com.android.emerald.rentme.R;

/**
 * Created by emerald on 6/23/2017.
 */
public class ProjectCreate2Fragment extends Fragment implements View.OnClickListener {
    private UserModel talent;

    private TextView txtService;
    private TextView txtProfile;

    private ProjectCreate20Fragment create20;
    private ProjectCreate21Fragment create21;

    public static ProjectCreate2Fragment newInstance(UserModel t) {
        ProjectCreate2Fragment fragment = new ProjectCreate2Fragment();
        fragment.talent = t;
        fragment.initNestFragments();
        return fragment;
    }

    public ProjectCreate2Fragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity)getContext()).setPageTitel("Talent information");

        View view = inflater.inflate(R.layout.fragment_project_create4, container, false);

        initViewVariables(view);
        return view;
    }

    private void initNestFragments() {
        create20 = ProjectCreate20Fragment.newInstance(talent.getId());
        create21 = ProjectCreate21Fragment.newInstance(talent);
    }

    private void initViewVariables(View view) {
        txtService = (TextView)view.findViewById(R.id.txt_project_create2_service);
        txtService.setOnClickListener(this);

        txtProfile = (TextView)view.findViewById(R.id.txt_project_create2_profile);
        txtProfile.setOnClickListener(this);

        FragmentManager fm = getChildFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        fm.beginTransaction();

        ft.replace(R.id.container_talent, create20);
        ft.commit();
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getChildFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        fm.beginTransaction();

        switch (v.getId()) {
            case R.id.txt_project_create2_service:
                ft.replace(R.id.container_talent, create20);
                txtService.setTextColor(getResources().getColor(R.color.colorPrimary));
                txtProfile.setTextColor(getResources().getColor(R.color.colorBlack));
                break;
            case R.id.txt_project_create2_profile:
                ft.replace(R.id.container_talent, create21);
                txtService.setTextColor(getResources().getColor(R.color.colorBlack));
                txtProfile.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
        }
        ft.commit();
    }
}

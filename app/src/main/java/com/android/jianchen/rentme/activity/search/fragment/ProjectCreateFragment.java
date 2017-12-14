package com.android.jianchen.rentme.activity.search.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.jianchen.rentme.R;
import com.android.jianchen.rentme.activity.root.MainActivity;
import com.android.jianchen.rentme.helper.delegator.OnSkillSelectListener;
import com.android.jianchen.rentme.model.rentme.SkillModel;
import com.android.jianchen.rentme.model.rentme.UserModel;

/**
 * Created by emerald on 6/6/2017.
 */
public class ProjectCreateFragment extends Fragment implements OnSkillSelectListener {

    private Context context;

    private Fragment step0;
    private Fragment step1;
    private Fragment step2;

    public static ProjectCreateFragment newInstance(Context context) {
        ProjectCreateFragment post = new ProjectCreateFragment();
        post.context = context;
        post.prepareData();
        return post;
    }

    private void prepareData() {
        step0 = SelectSkillFragment.newInstance(context, this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_create, container, false);

        initViewVariables();

        return view;
    }

    private void initViewVariables() {
        FragmentManager fm = getChildFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        fm.beginTransaction();

        if (context == null)
            context = getContext();

        ft.add(R.id.project_create_container, step0);
        ft.commit();
    }

    public void selectSkill(SkillModel s) {
        FragmentManager fm = getChildFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        fm.beginTransaction();

        step1 = MapFragment.newInstance(context, s);
        ft.replace(R.id.project_create_container, step1);
        ft.commit();
    }

    public void selectTalent(UserModel u, SkillModel s) {
        FragmentManager fm = getChildFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        fm.beginTransaction();

        //step2 = ProjectCreate21Fragment.newInstance(u, s);
        //step2 = ProjectCreate2Fragment.newInstance(u);
        ft.replace(R.id.project_create_container, step2);
        ft.commit();
    }

    public void goPrevStep(int curStep) {
        FragmentManager fm = getChildFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        fm.beginTransaction();

        if (curStep == 1) {
            ft.replace(R.id.project_create_container, step0);
            ft.commit();
            ((MainActivity)context).setCreateStep(1);
        } else if (curStep == 2) {
            ft.replace(R.id.project_create_container, step1);
            ft.commit();
            ((MainActivity)context).setCreateStep(1);
        } else  if (curStep == 3) {
            ft.replace(R.id.project_create_container, step2);
            ft.commit();
            ((MainActivity)context).setCreateStep(2);
        }
    }

    @Override
    public void onSkillSelect(SkillModel item) {
        selectSkill(item);
    }
}

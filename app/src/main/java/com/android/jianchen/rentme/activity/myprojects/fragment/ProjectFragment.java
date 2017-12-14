package com.android.jianchen.rentme.activity.myprojects.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.jianchen.rentme.activity.myprojects.adapter.ProjectPagerAdapter;
import com.android.jianchen.rentme.R;

import java.util.ArrayList;

/**
 * Created by emerald on 6/5/2017.
 */
public class ProjectFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private Context context;

    private RelativeLayout completed, inprogress;
    private View view1, view2;

    private ViewPager pager;
    private ProjectPagerAdapter pagerAdapter;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    //private ListFragment fragments;

    public static ProjectFragment newInstance(Context context) {
        ProjectFragment project = new ProjectFragment();
        project.context = context;

        return project;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myprojects, container, false);
        initViewVariables(view);
        return view;
    }

    private void initViewVariables(View view) {
        pager = (ViewPager)view.findViewById(R.id.pager_project);
        fragments = new ArrayList<>();
        pagerAdapter = new ProjectPagerAdapter(getFragmentManager(), fragments);
        pager.setAdapter(pagerAdapter);
        pagerAdapter.addFragment(ProjectInProgressFragment.newInstance(getContext()));
        pagerAdapter.addFragment(ProjectCompleteFragement.newInstance(getContext()));
        pagerAdapter.notifyDataSetChanged();
        pager.addOnPageChangeListener(this);

        completed = (RelativeLayout)view.findViewById(R.id.project_completed);
        completed.setOnClickListener(this);

        inprogress = (RelativeLayout)view.findViewById(R.id.project_inprogress);
        inprogress.setOnClickListener(this);

        view1 = (View)view.findViewById(R.id.view1);
        view2 = (View)view.findViewById(R.id.view2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.project_completed:
                pager.setCurrentItem(0);
                break;
            case R.id.project_inprogress:
                pager.setCurrentItem(1);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                view1.setBackgroundResource(R.color.colorWhite);
                view2.setBackgroundResource(R.color.colorPrimary);
                break;
            case 1:
                view1.setBackgroundResource(R.color.colorPrimary);
                view2.setBackgroundResource(R.color.colorWhite);
                break;
        }
        pagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

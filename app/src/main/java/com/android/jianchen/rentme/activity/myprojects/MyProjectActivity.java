package com.android.jianchen.rentme.activity.myprojects;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.jianchen.rentme.R;
import com.android.jianchen.rentme.activity.me.PreviewActivity;
import com.android.jianchen.rentme.activity.me.ServiceCreateActivity;
import com.android.jianchen.rentme.activity.myprojects.adapter.ProjectPagerAdapter;
import com.android.jianchen.rentme.activity.myprojects.fragment.ProjectCompleteFragement;
import com.android.jianchen.rentme.activity.myprojects.fragment.ProjectInProgressFragment;
import com.android.jianchen.rentme.helper.Constants;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by emerald on 12/15/2017.
 */
public class MyProjectActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    @Bind(R.id.pager_project)
    ViewPager pager;
    ProjectPagerAdapter pagerAdapter;
    ArrayList<Fragment> fragments = new ArrayList<>();

    @Bind(R.id.project_completed)
    RelativeLayout completed;

    @Bind(R.id.project_inprogress)
    RelativeLayout inprogress;

    @Bind(R.id.view1)
    View view1;

    @Bind(R.id.view2)
    View view2;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    protected void onCreate(Bundle saveBundle) {
        super.onCreate(saveBundle);
        setContentView(R.layout.activity_myproject);
        ButterKnife.bind(this);

        prepareActionBar();
        initViews();
    }

    private void prepareActionBar() {
        setSupportActionBar(toolbar);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
        bar.setTitle(null);
    }

    private void initViews() {
        fragments = new ArrayList<>();
        pagerAdapter = new ProjectPagerAdapter(getSupportFragmentManager(), fragments);
        pager.setAdapter(pagerAdapter);
        pagerAdapter.addFragment(ProjectInProgressFragment.newInstance(this));
        pagerAdapter.addFragment(ProjectCompleteFragement.newInstance(this));
        pagerAdapter.notifyDataSetChanged();
        pager.addOnPageChangeListener(this);

        completed.setOnClickListener(this);
        inprogress.setOnClickListener(this);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.project_completed:
                pager.setCurrentItem(0);
                break;
            case R.id.project_inprogress:
                pager.setCurrentItem(1);
                break;
        }
    }
}

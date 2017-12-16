package com.android.jianchen.rentme.activity.myprojects;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.jianchen.rentme.R;
import com.android.jianchen.rentme.activity.me.PreviewActivity;
import com.android.jianchen.rentme.activity.me.ProfileActivity;
import com.android.jianchen.rentme.activity.me.ServiceCreateActivity;
import com.android.jianchen.rentme.activity.myprojects.adapter.ProjectPagerAdapter;
import com.android.jianchen.rentme.activity.myprojects.events.ProjectChangeEvent;
import com.android.jianchen.rentme.activity.myprojects.fragment.ProjectCompleteFragement;
import com.android.jianchen.rentme.activity.myprojects.fragment.ProjectInProgressFragment;
import com.android.jianchen.rentme.activity.root.SocialLoginActivity;
import com.android.jianchen.rentme.activity.root.customview.DrawerArrowDrawable;
import com.android.jianchen.rentme.activity.search.SelectSkillActivity;
import com.android.jianchen.rentme.helper.Constants;
import com.android.jianchen.rentme.helper.utils.Utils;
import com.android.jianchen.rentme.model.rentme.UserModel;
import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.github.siyamed.shapeimageview.CircularImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by emerald on 12/15/2017.
 */
public class MyProjectActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

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

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private CircularImageView imgAvatar;
    private TextView txtUsername;

    private UserModel curUser;

    protected void onCreate(Bundle saveBundle) {
        super.onCreate(saveBundle);
        setContentView(R.layout.activity_myproject);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        curUser = Utils.retrieveUserInfo(this);

        prepareActionBar();
        initViews();
        initDrawer();
    }

    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(ProjectChangeEvent event) {
        if (event.getType() == 2) {

        }
    }

    private void prepareActionBar() {
        setSupportActionBar(toolbar);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
        bar.setTitle("My Projects");

        DrawerArrowDrawable drawerArrowDrawable = new DrawerArrowDrawable(getResources());
        drawerArrowDrawable.setStrokeColor(getResources().getColor(R.color.colorWhite));
        bar.setHomeAsUpIndicator(drawerArrowDrawable);
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

    private void initDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);

        View headerViewView =  navigationView.getHeaderView(0);
        imgAvatar = (CircularImageView) headerViewView.findViewById(R.id.img_nav_avatar);
        txtUsername = (TextView)headerViewView.findViewById(R.id.txt_nav_username);

        if (curUser.getLoginMode().equals(Constants.LOGINMODE_GOOGLE)) {

            Glide.with(this).load(curUser.getGgProfileUrl()).asBitmap().centerCrop().placeholder(R.drawable.placeholder).into(imgAvatar);
            txtUsername.setText(curUser.getGgName());

        }
        else if (curUser.getLoginMode().equals(Constants.LOGINMODE_FACEBOOK)) {

            Glide.with(this).load(curUser.getFbProfileUrl()).asBitmap().centerCrop().placeholder(R.drawable.placeholder).into(imgAvatar);
            txtUsername.setText(curUser.getFbName());

        }
        else if (curUser.getLoginMode().equals(Constants.LOGINMODE_EMAIL)) {

            if (curUser.getAvatar() != null && !curUser.getAvatar().equals("") && !curUser.getAvatar().equals("null")) {
                Glide.with(this).load(curUser.getAvatar()).asBitmap().centerCrop().placeholder(R.drawable.placeholder).into(imgAvatar);
            }
            txtUsername.setText(curUser.getName());
        }

        navigationView.setNavigationItemSelectedListener(this);
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
                drawer.openDrawer(Gravity.LEFT);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_post:
                Intent intent = new Intent(this, SelectSkillActivity.class);
                intent.putExtra(Constants.EXTRA_ACTION_TYPE, Constants.ACTION_CREATE_PROJECT);
                startActivity(intent);

                break;
            case R.id.nav_edit:
                startActivity(new Intent(this, ProfileActivity.class));

                finish();
                break;
            case R.id.nav_projects:
                break;
            case R.id.nav_signout:
                Utils.saveUserInfo(this, null);
                startActivity(new Intent(this, SocialLoginActivity.class));

                // facebook log out
                LoginManager.getInstance().logOut();

                finish();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

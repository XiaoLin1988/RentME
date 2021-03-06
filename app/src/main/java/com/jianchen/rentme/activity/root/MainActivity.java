package com.jianchen.rentme.activity.root;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianchen.rentme.activity.myprojects.fragment.ProjectFragment;
import com.jianchen.rentme.activity.search.fragment.ProjectCreateFragment;
import com.jianchen.rentme.model.rentme.UserModel;
import com.jianchen.rentme.activity.search.TalentDetailActivity;
import com.jianchen.rentme.R;
import com.jianchen.rentme.helper.service.FirebaseTracker;
import com.jianchen.rentme.helper.service.GPSTracker;
import com.jianchen.rentme.activity.me.ServiceCreateActivity;
import com.jianchen.rentme.helper.Constants;
import com.jianchen.rentme.helper.utils.Utils;
import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.github.siyamed.shapeimageview.CircularImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;

    private CircularImageView imgAvatar;
    private TextView txtUsername;

    private Toolbar toolbar;
    private ImageView btnMenu;
    private TextView txtPage;
    private ImageView btnPlus;

    private UserModel curUser;

    private Fragment project, post, service, createservice;

    private int createStep = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(MainActivity.this, GPSTracker.class));
        startService(new Intent(MainActivity.this, FirebaseTracker.class));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        curUser = Utils.retrieveUserInfo(getApplicationContext());

        initVariables();
        initViewVariables();
    }

    private void initVariables() {
        project = ProjectFragment.newInstance(MainActivity.this);

        post = ProjectCreateFragment.newInstance(MainActivity.this);
        /*
        service = ServiceFragment.newInstance(MainActivity.this);
        */
    }

    private void initViewVariables() {

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        btnMenu = (ImageView)findViewById(R.id.btn_container_menu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        btnPlus = (ImageView)findViewById(R.id.btn_container_plus);
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateService();
            }
        });

        txtPage = (TextView)findViewById(R.id.txt_container_page);
        txtPage.setText("Project list");

        navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);

        View headerViewView =  navigationView.getHeaderView(0);
        imgAvatar = (CircularImageView) headerViewView.findViewById(R.id.img_nav_avatar);
        txtUsername = (TextView)headerViewView.findViewById(R.id.txt_nav_username);

        if (curUser.getLoginMode().equals(Constants.LOGINMODE_GOOGLE)) {

            Glide.with(MainActivity.this).load(curUser.getGgProfileUrl()).asBitmap().centerCrop().placeholder(R.drawable.placeholder).into(imgAvatar);
            txtUsername.setText(curUser.getGgName());

        } else if (curUser.getLoginMode().equals(Constants.LOGINMODE_FACEBOOK)) {

            Glide.with(MainActivity.this).load(curUser.getFbProfileUrl()).asBitmap().centerCrop().placeholder(R.drawable.placeholder).into(imgAvatar);
            txtUsername.setText(curUser.getFbName());

        } else if (curUser.getLoginMode().equals(Constants.LOGINMODE_EMAIL)) {

            if (curUser.getAvatar() != null && !curUser.getAvatar().equals("") && !curUser.getAvatar().equals("null")) {
                Glide.with(MainActivity.this).load(curUser.getAvatar()).asBitmap().centerCrop().placeholder(R.drawable.placeholder).into(imgAvatar);
            }
            txtUsername.setText(curUser.getName());
        }

        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        fm.beginTransaction();
        Fragment project = new ProjectFragment();
        ft.add(R.id.frame_container, project);
        ft.commit();
    }

    public void openCreateProject() {
        btnPlus.setVisibility(View.GONE);

        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        fm.beginTransaction();

        setPageTitel("Create project");

        ft.replace(R.id.frame_container, post);
        ft.commit();
    }

    public void openMyProjects() {
        btnPlus.setVisibility(View.GONE);

        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        fm.beginTransaction();

        setPageTitel("Project list");

        project = ProjectFragment.newInstance(MainActivity.this);
        ft.replace(R.id.frame_container, project);
        ft.commit();
    }

    public void openCreateService() {
        /*
        btnPlus.setVisibility(View.GONE);

        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        fm.beginTransaction();

        setPageTitel("Create service");

        ft.replace(R.id.frame_container, createservice);
        ft.commit();
        */

        Intent intent = new Intent(this, ServiceCreateActivity.class);
        startActivityForResult(intent, Constants.REQUEST_SERVICE_CREATE);
    }

    private void openMyServices() {
        btnPlus.setVisibility(View.VISIBLE);

        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        fm.beginTransaction();

        setPageTitel("My Services");

        ft.replace(R.id.frame_container, service);
        ft.commit();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*
            case R.id.nav_services:
                openMyServices();
                break;
            */
            case R.id.nav_post:
                openCreateProject();
                break;
            case R.id.nav_edit:
                Intent intent = new Intent(MainActivity.this, TalentDetailActivity.class);
                intent.putExtra(Constants.KEY_USER, curUser);

                startActivity(intent);
                break;
            case R.id.nav_projects:
                openMyProjects();
                break;
            case R.id.nav_signout:
                Utils.saveUserInfo(getApplicationContext(), null);
                startActivity(new Intent(MainActivity.this, SocialLoginActivity.class));

                // facebook log out
                LoginManager.getInstance().logOut();

                finish();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setCreateStep(int step) {
        this.createStep = step;
    }

    public void setPageTitel(String title) {
        txtPage.setText(title);
    }

    /*
    @Override
    public void onBackPressed() {
        if (createStep > 0) {
            ((ProjectCreateFragment)post).goPrevStep(createStep);
        } else {

        }
    }
    */
}

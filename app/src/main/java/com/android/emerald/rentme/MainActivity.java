package com.android.emerald.rentme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.emerald.rentme.Fragments.ProjectCompleteFragement;
import com.android.emerald.rentme.Fragments.ProjectCreateFragment;
import com.android.emerald.rentme.Fragments.ProjectFragment;
import com.android.emerald.rentme.Fragments.ServiceCreateFragment;
import com.android.emerald.rentme.Fragments.ServiceFragment;
import com.android.emerald.rentme.Models.UserModel;
import com.android.emerald.rentme.Service.FirebaseTracker;
import com.android.emerald.rentme.Service.GPSTracker;
import com.android.emerald.rentme.Utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;

    private CircleImageView imgAvatar;
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
        //project = new ProjectFragment();
        project = ProjectFragment.newInstance(MainActivity.this);
        post = ProjectCreateFragment.newInstance(MainActivity.this);
        service = ServiceFragment.newInstance(MainActivity.this);
        createservice = ServiceCreateFragment.newInstance(MainActivity.this);
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

        //drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        /*
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.side_menu_open, R.string.side_menu_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        */
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        /*
        if (curUser.getType() == 2) {
            navigationView.getMenu().findItem(R.id.nav_post).setVisible(false);
        }
        */
        View headerViewView =  navigationView.getHeaderView(0);
        imgAvatar = (CircleImageView)headerViewView.findViewById(R.id.img_nav_avatar);
        if (!curUser.getAvatar().equals("") && curUser.getAvatar() != null && !curUser.getAvatar().equals("null")) {
            //Bitmap myBitmap = BitmapFactory.decodeFile(curUser.getAvatar());
            //imgAvatar.setImageBitmap(myBitmap);
            Picasso.with(MainActivity.this).load(curUser.getAvatar()).into(imgAvatar);
        }
        txtUsername = (TextView)headerViewView.findViewById(R.id.txt_nav_username);
        txtUsername.setText(curUser.getName());
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
        //project = ProjectFragment.newInstance(MainActivity.this);

        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        fm.beginTransaction();

        setPageTitel("Project list");

        ft.replace(R.id.frame_container, project);
        ft.commit();
    }

    public void openCreateService() {
        btnPlus.setVisibility(View.GONE);

        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        fm.beginTransaction();

        setPageTitel("Create service");

        ft.replace(R.id.frame_container, createservice);
        ft.commit();
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
            case R.id.nav_services:
                openMyServices();
                //openCreateService();
                break;
            case R.id.nav_post:
                openCreateProject();
                break;
            case R.id.nav_edit:
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                break;
            case R.id.nav_projects:
                openMyProjects();
                break;
            case R.id.nav_signout:
                Utils.saveUserInfo(getApplicationContext(), null);
                startActivity(new Intent(MainActivity.this, LoginActivity.class));

                /*
                Intent intent = new Intent(getApplicationContext(), GPSTracker.class);
                stopService(intent);
                */
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

    @Override
    public void onBackPressed() {
        if (createStep > 0) {
            ((ProjectCreateFragment)post).goPrevStep(createStep);
        } else {
            /*
            Utils.saveUserInfo(getApplicationContext(), null);

            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            */
            /*
            Intent intent = new Intent(getApplicationContext(), GPSTracker.class);
            stopService(intent);
            */
            //finish();
        }
    }
}

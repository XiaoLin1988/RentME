package com.android.jianchen.rentme.activity.me;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jianchen.rentme.R;
import com.android.jianchen.rentme.activity.me.adapter.GalleryPagerAdapter;
import com.android.jianchen.rentme.activity.me.events.ServiceChangeEvent;
import com.android.jianchen.rentme.activity.myprojects.MyProjectActivity;
import com.android.jianchen.rentme.activity.root.SocialLoginActivity;
import com.android.jianchen.rentme.activity.root.customview.DrawerArrowDrawable;
import com.android.jianchen.rentme.activity.search.SelectSkillActivity;
import com.android.jianchen.rentme.activity.search.adapter.SkillServiceRecyclerAdapter;
import com.android.jianchen.rentme.helper.Constants;
import com.android.jianchen.rentme.helper.delegator.OnProjectCreateListener;
import com.android.jianchen.rentme.helper.delegator.OnServiceClickListener;
import com.android.jianchen.rentme.helper.listener.AppBarStateListener;
import com.android.jianchen.rentme.helper.network.retrofit.RestClient;
import com.android.jianchen.rentme.helper.network.retrofit.UserClient;
import com.android.jianchen.rentme.helper.utils.Utils;
import com.android.jianchen.rentme.model.rentme.ArrayModel;
import com.android.jianchen.rentme.model.rentme.ObjectModel;
import com.android.jianchen.rentme.model.rentme.ProjectModel;
import com.android.jianchen.rentme.model.rentme.ServiceModel;
import com.android.jianchen.rentme.model.rentme.SkillServiceModel;
import com.android.jianchen.rentme.model.rentme.UserAvatarModel;
import com.android.jianchen.rentme.model.rentme.UserModel;
import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.rd.PageIndicatorView;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements OnServiceClickListener, NavigationView.OnNavigationItemSelectedListener, OnProjectCreateListener {
    private UserModel userModel;

    @Bind(R.id.img_profile_cover)
    ImageView imgCover;

    @Bind(R.id.pager_cover)
    ViewPager pagerSubCover;
    GalleryPagerAdapter adapterCover;
    @Bind(R.id.ind_profile2)
    PageIndicatorView indicator;

    @Bind(R.id.img_profile_main)
    ImageView imgMain;

    @Bind(R.id.txt_profile_name)
    TextView txtName;

    @Bind(R.id.txt_profile_mood)
    TextView txtMood;

    @Bind(R.id.txt_profile_location)
    TextView txtLocation;

    @Bind(R.id.txt_profile_joined)
    TextView txtJoined;

    @Bind(R.id.txt_profile_earned)
    TextView txtEarning;

    @Bind(R.id.img_profile_email)
    ImageView imgEmail;

    @Bind(R.id.img_profile_facebook)
    ImageView imgFacebook;

    @Bind(R.id.img_profile_google)
    ImageView imgGoogle;

    @Bind(R.id.img_profile_wechat)
    ImageView imgWechat;

    @Bind(R.id.recycler_services)
    RecyclerView recyclerServices;
    SkillServiceRecyclerAdapter adapterSkillService;

    @BindString(R.string.error_load)
    String errLoad;

    @BindString(R.string.error_network)
    String errNetwork;

    @BindString(R.string.joined)
    String joined;

    private Menu menu;

    private Toolbar toolbar;
    private AVLoadingIndicatorView loadingContent;

    private DrawerArrowDrawable drawerArrowDrawable;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private CircularImageView imgAvatar;
    private TextView txtUsername;

    protected void onCreate(Bundle saveBundle) {
        super.onCreate(saveBundle);
        setContentView(R.layout.activity_profile2);

        ButterKnife.bind(this);

        EventBus.getDefault().register(this);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        prepareActionBar();

        userModel = Utils.retrieveUserInfo(this);

        initViews();
    }

    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(ServiceChangeEvent event) {
        if (event.getType() == 0) {
            adapterSkillService.remove(event.getResult());
        } else if (event.getType() == 1) {
            adapterSkillService.addService(event.getResult());
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
        bar.setTitle(null);

        AppBarLayout appBar = (AppBarLayout)findViewById(R.id.appBar);
        appBar.addOnOffsetChangedListener(new AppBarStateListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.COLLAPSED) {
                    drawerArrowDrawable = new DrawerArrowDrawable(getResources());
                    drawerArrowDrawable.setStrokeColor(getResources().getColor(R.color.colorBlack));
                    getSupportActionBar().setHomeAsUpIndicator(drawerArrowDrawable);

                    if (menu != null) {
                        MenuItem menuEdit = menu.findItem(R.id.action_edit);
                        Drawable editDrawable = menuEdit.getIcon();
                        Drawable editWrap = DrawableCompat.wrap(editDrawable);
                        DrawableCompat.setTint(editWrap, getResources().getColor(R.color.colorBlack));
                        menuEdit.setIcon(editWrap);

                        MenuItem menuCreate = menu.findItem(R.id.action_create);
                        Drawable createDrawable = menuCreate.getIcon();
                        Drawable createWrap = DrawableCompat.wrap(createDrawable);
                        DrawableCompat.setTint(createWrap, getResources().getColor(R.color.colorBlack));
                        menuCreate.setIcon(createWrap);
                    }
                } else if (state == State.EXPANDED){

                    drawerArrowDrawable = new DrawerArrowDrawable(getResources());
                    drawerArrowDrawable.setStrokeColor(getResources().getColor(R.color.colorWhite));
                    getSupportActionBar().setHomeAsUpIndicator(drawerArrowDrawable);

                    if (menu != null) {
                        MenuItem menuEdit = menu.findItem(R.id.action_edit);
                        Drawable editDrawable = menuEdit.getIcon();
                        Drawable editWrap = DrawableCompat.wrap(editDrawable);
                        DrawableCompat.setTint(editWrap, getResources().getColor(R.color.colorWhite));
                        menuEdit.setIcon(editWrap);

                        MenuItem menuCreate = menu.findItem(R.id.action_create);
                        Drawable createDrawable = menuCreate.getIcon();
                        Drawable createWrap = DrawableCompat.wrap(createDrawable);
                        DrawableCompat.setTint(createWrap, getResources().getColor(R.color.colorWhite));
                        menuCreate.setIcon(createWrap);
                    }
                }
            }
        });
    }

    private void initViews() {
        loadingContent = (AVLoadingIndicatorView)findViewById(R.id.loading_profile_content);
        loadingContent.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingContent.hide();
                loadingContent.setVisibility(View.GONE);
                findViewById(R.id.lyt_profile_content).setVisibility(View.VISIBLE);
            }
        }, 1500);

        if (userModel.getEmail() == null || userModel.getEmail().equals("")) {
            imgEmail.setImageResource(R.drawable.email_u);
        } else {
            imgEmail.setImageResource(R.drawable.email_a);
        }

        if (userModel.getFbId() == null || userModel.getFbId().equals("")) {
            imgFacebook.setImageResource(R.drawable.facebook_u);
        } else {
            imgFacebook.setImageResource(R.drawable.facebook_a);
        }

        if (userModel.getGgId() == null || userModel.getGgId().equals("")) {
            imgGoogle.setImageResource(R.drawable.google_u);
        } else {
            imgGoogle.setImageResource(R.drawable.google_a);
        }

        if (userModel.getWxId() == null || userModel.getWxId().equals("")) {
            imgWechat.setImageResource(R.drawable.wechat_u);
        } else {
            imgWechat.setImageResource(R.drawable.wechat_a);
        }

        txtName.setText(userModel.getName());
        txtMood.setText(userModel.getDescription());
        if (userModel.getAddress() == null || userModel.getAddress().equals(""))
            txtLocation.setText("Address is not set.");
        else
            txtLocation.setText(userModel.getAddress());

        if (userModel.getEarning() == 0) {
            txtEarning.setText("No earning");
        } else {
            txtEarning.setText(Utils.getUserEarning(userModel.getEarning()));
        }
        if (userModel.getCtime() != null) {
            Date date = Utils.stringToDate(userModel.getCtime());
            txtJoined.setText(joined + " " + Utils.beautifyDate(date, false));
        }

        if (userModel.getCoverImg() == null || userModel.getCoverImg().size() == 0) {
            int pos = new Random().nextInt(5) % 5;
            pagerSubCover.setVisibility(View.GONE);
            switch (pos) {
                case 0:
                    Glide.with(this).load(R.drawable.cover1).asBitmap().fitCenter().into(imgCover);
                    break;
                case 1:
                    Glide.with(this).load(R.drawable.cover2).asBitmap().fitCenter().into(imgCover);
                    break;
                case 2:
                    Glide.with(this).load(R.drawable.cover3).asBitmap().fitCenter().into(imgCover);
                    break;
                case 3:
                    Glide.with(this).load(R.drawable.cover4).asBitmap().fitCenter().into(imgCover);
                    break;
                case 4:
                    Glide.with(this).load(R.drawable.cover5).asBitmap().fitCenter().into(imgCover);
                    break;
            }
        }
        else {
            // cover image
            adapterCover = new GalleryPagerAdapter(this, pagerSubCover, userModel.getCoverImg());
            pagerSubCover.setAdapter(adapterCover);
            indicator.setViewPager(pagerSubCover);
        }

        // main profile image
        if (userModel.getAvatar() == null || userModel.getAvatar() == "") { // empty avatar

        }
        else { // existing avatar
            Glide.with(this).load(userModel.getAvatar()).asBitmap().fitCenter().placeholder(R.drawable.profile_empty).into(imgMain);
        }


        adapterSkillService = new SkillServiceRecyclerAdapter(this, new ArrayList<SkillServiceModel>(), this);
        recyclerServices.setAdapter(adapterSkillService);
        recyclerServices.setLayoutManager(new LinearLayoutManager(this));
        recyclerServices.setNestedScrollingEnabled(false);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);

        View headerViewView =  navigationView.getHeaderView(0);
        imgAvatar = (CircularImageView) headerViewView.findViewById(R.id.img_nav_avatar);
        txtUsername = (TextView)headerViewView.findViewById(R.id.txt_nav_username);

        if (userModel.getLoginMode().equals(Constants.LOGINMODE_GOOGLE)) {

            Glide.with(this).load(userModel.getGgProfileUrl()).asBitmap().centerCrop().placeholder(R.drawable.placeholder).into(imgAvatar);
            txtUsername.setText(userModel.getGgName());

        }
        else if (userModel.getLoginMode().equals(Constants.LOGINMODE_FACEBOOK)) {

            Glide.with(this).load(userModel.getFbProfileUrl()).asBitmap().centerCrop().placeholder(R.drawable.placeholder).into(imgAvatar);
            txtUsername.setText(userModel.getFbName());

        }
        else if (userModel.getLoginMode().equals(Constants.LOGINMODE_EMAIL)) {

            if (userModel.getAvatar() != null && !userModel.getAvatar().equals("") && !userModel.getAvatar().equals("null")) {
                Glide.with(this).load(userModel.getAvatar()).asBitmap().centerCrop().placeholder(R.drawable.placeholder).into(imgAvatar);
            }
            txtUsername.setText(userModel.getName());
        }

        navigationView.setNavigationItemSelectedListener(this);

        getUserServices();
        getProfileImages();
    }

    private void getProfileImages() {
        RestClient<UserClient> restClient = new RestClient<>();
        UserClient userClient = restClient.getAppClient(UserClient.class);

        Call<ObjectModel<UserAvatarModel>> call = userClient.getProfileImages(userModel.getId());
        call.enqueue(new Callback<ObjectModel<UserAvatarModel>>() {
            @Override
            public void onResponse(Call<ObjectModel<UserAvatarModel>> call, Response<ObjectModel<UserAvatarModel>> response) {
                if (response.isSuccessful()) {
                    UserAvatarModel avatars = response.body().getData();

                    if (avatars.getMainProfile().size() > 0) {
                        userModel.setAvatar(avatars.getMainProfile().get(0));
                        Utils.saveUserInfo(ProfileActivity.this, userModel);
                        Glide.with(ProfileActivity.this).load(userModel.getAvatar()).asBitmap().fitCenter().placeholder(R.drawable.profile_empty).into(imgMain);
                    }

                    if (avatars.getSubProfile().size() > 0) {
                        userModel.setCoverImg(avatars.getSubProfile());
                        Utils.saveUserInfo(ProfileActivity.this, userModel);
                        adapterCover = new GalleryPagerAdapter(ProfileActivity.this, pagerSubCover, userModel.getCoverImg());
                        pagerSubCover.setAdapter(adapterCover);
                        indicator.setViewPager(pagerSubCover);

                        imgCover.setVisibility(View.GONE);
                        pagerSubCover.setVisibility(View.VISIBLE);
                    }

                } else {
                    Toast.makeText(ProfileActivity.this, errLoad, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ObjectModel<UserAvatarModel>> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, errNetwork, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserServices() {
        RestClient<UserClient> restClient = new RestClient<>();
        UserClient userClient = restClient.getAppClient(UserClient.class);

        Call<ArrayModel<ServiceModel>> call = userClient.getUserServices(userModel.getId());
        call.enqueue(new Callback<ArrayModel<ServiceModel>>() {
            @Override
            public void onResponse(Call<ArrayModel<ServiceModel>> call, Response<ArrayModel<ServiceModel>> response) {
                if (response.isSuccessful()) {
                    ArrayList<ServiceModel> services = response.body().getData();
                    for (int i = 0; i < services.size(); i++) {
                        adapterSkillService.addService(services.get(i));
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, errLoad, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayModel<ServiceModel>> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, errNetwork, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onServiceClick(View view, ServiceModel service) {
        ServiceDetailActivity.navigate(this, view, this, service);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(Gravity.LEFT);
                return true;
            case R.id.action_create:
                Intent intent = new Intent(ProfileActivity.this, SelectSkillActivity.class);
                intent.putExtra(Constants.EXTRA_ACTION_TYPE, Constants.ACTION_CREATE_SERVICE);
                startActivityForResult(intent, Constants.REQUEST_SERVICE_CREATE);
                return true;
            case R.id.action_edit:
                Intent intent1 = new Intent(ProfileActivity.this, PreviewActivity.class);
                startActivityForResult(intent1, Constants.REQUEST_PREVIEW);
                //startActivity(intent1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);

        MenuItem menuEdit = menu.findItem(R.id.action_edit);
        Drawable editDrawable = menuEdit.getIcon();
        Drawable editWrap = DrawableCompat.wrap(editDrawable);
        DrawableCompat.setTint(editWrap, getResources().getColor(R.color.colorWhite));
        menuEdit.setIcon(editWrap);

        MenuItem menuCreate = menu.findItem(R.id.action_create);
        Drawable createDrawable = menuCreate.getIcon();
        Drawable createWrap = DrawableCompat.wrap(createDrawable);
        DrawableCompat.setTint(createWrap, getResources().getColor(R.color.colorWhite));
        menuCreate.setIcon(createWrap);

        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    public void onActivityResult(int reqCode, int resCode, Intent data) {
        if (reqCode == Constants.REQUEST_SERVICE_CREATE && resCode == RESULT_OK) {
            ServiceModel service = (ServiceModel)data.getSerializableExtra(Constants.KEY_SERVICE);
            adapterSkillService.addService(service);
        } else if (reqCode == Constants.REQUEST_PREVIEW && resCode == RESULT_OK) {
            userModel = Utils.retrieveUserInfo(this);
            Glide.with(ProfileActivity.this).load(userModel.getAvatar()).asBitmap().centerCrop().placeholder(R.drawable.profile_empty).into(imgAvatar);
            Glide.with(ProfileActivity.this).load(userModel.getAvatar()).asBitmap().centerCrop().placeholder(R.drawable.profile_empty).into(imgMain);

            txtName.setText(userModel.getName());
            txtUsername.setText(userModel.getName());
            txtMood.setText(userModel.getDescription());
            txtLocation.setText(userModel.getAddress());
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

                break;
            case R.id.nav_projects:
                startActivity(new Intent(this, MyProjectActivity.class));
                finish();
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

    @Override
    public void onProjectCreate(ProjectModel project) {
    }
}

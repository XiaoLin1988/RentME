package com.jianchen.rentme.activity.search;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jianchen.rentme.activity.me.ServiceDetailActivity;
import com.jianchen.rentme.activity.me.adapter.GalleryPagerAdapter;
import com.jianchen.rentme.activity.myprojects.events.ProjectChangeEvent;
import com.jianchen.rentme.activity.search.adapter.SkillServiceRecyclerAdapter;
import com.jianchen.rentme.helper.delegator.OnProjectCreateListener;
import com.jianchen.rentme.helper.delegator.OnServiceClickListener;
import com.jianchen.rentme.helper.listener.AppBarStateListener;
import com.jianchen.rentme.model.rentme.ArrayModel;
import com.jianchen.rentme.model.rentme.ObjectModel;
import com.jianchen.rentme.model.rentme.ProjectModel;
import com.jianchen.rentme.model.rentme.ServiceModel;
import com.jianchen.rentme.model.rentme.SkillServiceModel;
import com.jianchen.rentme.model.rentme.UserAvatarModel;
import com.jianchen.rentme.model.rentme.UserModel;
import com.jianchen.rentme.R;
import com.jianchen.rentme.helper.network.retrofit.RestClient;
import com.jianchen.rentme.helper.network.retrofit.UserClient;
import com.jianchen.rentme.helper.Constants;
import com.jianchen.rentme.helper.utils.Utils;
import com.bumptech.glide.Glide;
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

public class TalentDetailActivity extends AppCompatActivity implements OnServiceClickListener, OnProjectCreateListener {
    private UserModel userModel;

    @Bind(R.id.img_profile_cover)
    ImageView imgCover;

    @Bind(R.id.pager_subcover)
    ViewPager pagerSubCover;
    GalleryPagerAdapter adapterCover;
    @Bind(R.id.ind_subcover)
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

    @Bind(R.id.txt_no_data)
    TextView txtNoData;

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

    protected void onCreate(Bundle saveBundle) {
        super.onCreate(saveBundle);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        prepareActionBar();

        userModel = (UserModel)getIntent().getSerializableExtra(Constants.KEY_USER);

        initViews();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(ProjectChangeEvent event) {
        if (event.getType() == 1) {
            finish();
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
                    Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
                    upArrow.setColorFilter(getResources().getColor(R.color.colorBlack), PorterDuff.Mode.SRC_ATOP);
                    getSupportActionBar().setHomeAsUpIndicator(upArrow);
                } else if (state == State.EXPANDED){
                    Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
                    upArrow.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
                    getSupportActionBar().setHomeAsUpIndicator(upArrow);
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
        if (userModel.getAddress().equals(""))
            txtLocation.setText("Address is not set");
        else
            txtLocation.setText(userModel.getAddress());

        if (userModel.getEarning() == 0) {
            txtEarning.setText("No earning");
        } else {
            txtEarning.setText(Utils.getUserEarning(userModel.getEarning()));
        }
        Date date = Utils.stringToDate(userModel.getCtime());
        txtJoined.setText(joined + " " + Utils.beautifyDate(date, false));

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
            imgCover.setVisibility(View.GONE);
            pagerSubCover.setVisibility(View.VISIBLE);

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
                        Glide.with(TalentDetailActivity.this).load(userModel.getAvatar()).asBitmap().fitCenter().placeholder(R.drawable.profile_empty).into(imgMain);
                    }

                    if (avatars.getSubProfile().size() > 0) {
                        userModel.setCoverImg(avatars.getSubProfile());
                        adapterCover = new GalleryPagerAdapter(TalentDetailActivity.this, pagerSubCover, userModel.getCoverImg());
                        pagerSubCover.setAdapter(adapterCover);
                        indicator.setViewPager(pagerSubCover);

                        imgCover.setVisibility(View.GONE);
                        pagerSubCover.setVisibility(View.VISIBLE);
                    }

                } else {
                    Toast.makeText(TalentDetailActivity.this, errLoad, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ObjectModel<UserAvatarModel>> call, Throwable t) {
                Toast.makeText(TalentDetailActivity.this, errNetwork, Toast.LENGTH_SHORT).show();
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
                    recyclerServices.setVisibility(View.VISIBLE);
                    txtNoData.setVisibility(View.GONE);
                } else {
                    Toast.makeText(TalentDetailActivity.this, errLoad, Toast.LENGTH_SHORT).show();
                    recyclerServices.setVisibility(View.GONE);
                    txtNoData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ArrayModel<ServiceModel>> call, Throwable t) {
                Toast.makeText(TalentDetailActivity.this, errNetwork, Toast.LENGTH_SHORT).show();
                recyclerServices.setVisibility(View.GONE);
                txtNoData.setVisibility(View.VISIBLE);
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
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int reqCode, int resCode, Intent data) {
        if (reqCode == Constants.REQUEST_SERVICE_CREATE && resCode == RESULT_OK) {
            ServiceModel service = (ServiceModel)data.getSerializableExtra(Constants.KEY_SERVICE);
            adapterSkillService.addService(service);
        }
    }

    @Override
    public void onProjectCreate(ProjectModel project) {
        finish();
    }
}

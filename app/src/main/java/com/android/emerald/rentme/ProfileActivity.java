package com.android.emerald.rentme;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.emerald.rentme.Adapter.ServiceRecyclerAdapter;
import com.android.emerald.rentme.Adapter.SkillServiceRecyclerAdapter;
import com.android.emerald.rentme.Interface.OnServiceClickListener;
import com.android.emerald.rentme.Models.ArrayModel;
import com.android.emerald.rentme.Models.ServiceModel;
import com.android.emerald.rentme.Models.SkillServiceModel;
import com.android.emerald.rentme.Models.UserModel;
import com.android.emerald.rentme.RestAPI.RestClient;
import com.android.emerald.rentme.RestAPI.UserClient;
import com.android.emerald.rentme.Utils.Constants;
import com.android.emerald.rentme.Utils.Utils;
import com.bumptech.glide.Glide;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements OnServiceClickListener {
    private UserModel userModel;

    @Bind(R.id.img_profile_cover)
    ImageView imgCover;

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

    @Bind(R.id.img_profile_mobile)
    ImageView imgMobile;

    @Bind(R.id.img_profile_facebook)
    ImageView imgFacebook;

    @Bind(R.id.img_profile_google)
    ImageView imgGoogle;

    @Bind(R.id.img_profile_wechat)
    ImageView imgWechat;

    @Bind(R.id.recycler_projects)
    RecyclerView recyclerServices;
    SkillServiceRecyclerAdapter adapterSkillService;

    @BindString(R.string.error_load)
    String errLoad;

    @BindString(R.string.error_network)
    String errNetwork;

    @BindString(R.string.joined)
    String joined;

    private Toolbar toolbar;
    private AVLoadingIndicatorView loadingContent;

    protected void onCreate(Bundle saveBundle) {
        super.onCreate(saveBundle);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        prepareActionBar();

        userModel = (UserModel)getIntent().getSerializableExtra(Constants.KEY_USER);

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

        Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.colorBlack), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        bar.setTitle(null);
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

        if (userModel.getPhone() == null || userModel.getPhone().equals("")) {
            imgMobile.setImageResource(R.drawable.mobile_u);
        } else {
            imgMobile.setImageResource(R.drawable.mobile_a);
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
        txtLocation.setText(userModel.getAddress());
        Date date = Utils.stringToDate(userModel.getCtime());
        txtJoined.setText(joined + " " + Utils.beautifyDate(date, false));

        Glide.with(this).load(userModel.getCoverImg()).asBitmap().fitCenter().placeholder(R.drawable.cover).into(imgCover);
        Glide.with(this).load(userModel.getMainImg()).asBitmap().fitCenter().placeholder(R.drawable.main).into(imgMain);


        adapterSkillService = new SkillServiceRecyclerAdapter(this, new ArrayList<SkillServiceModel>(), this);
        recyclerServices.setAdapter(adapterSkillService);
        recyclerServices.setLayoutManager(new LinearLayoutManager(this));
        recyclerServices.setNestedScrollingEnabled(false);

        getUserServices();
    }

    private void getUserServices() {
        RestClient<UserClient> restClient = new RestClient<>();
        UserClient userClient = restClient.getAppClient(UserClient.class);

        Call<ArrayModel<ServiceModel>> call = userClient.getUserSkills(userModel.getId());
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
        ServiceDetailActivity2.navigate(this, view, service);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

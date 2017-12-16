package com.android.jianchen.rentme.activity.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.jianchen.rentme.R;
import com.android.jianchen.rentme.activity.me.ServiceCreateActivity;
import com.android.jianchen.rentme.activity.me.events.ServiceChangeEvent;
import com.android.jianchen.rentme.activity.myprojects.events.ProjectChangeEvent;
import com.android.jianchen.rentme.activity.search.adapter.SkillGalleryAdapter;
import com.android.jianchen.rentme.helper.Constants;
import com.android.jianchen.rentme.helper.delegator.OnSkillSelectListener;
import com.android.jianchen.rentme.helper.network.retrofit.RestClient;
import com.android.jianchen.rentme.helper.network.retrofit.SkillClient;
import com.android.jianchen.rentme.model.rentme.ArrayModel;
import com.android.jianchen.rentme.model.rentme.SkillModel;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by emerald on 12/15/2017.
 */
public class SelectSkillActivity extends AppCompatActivity implements OnSkillSelectListener {

    @Bind(R.id.recycler_skill)
    RecyclerView recyclerSkill;
    ArrayList<SkillModel> skills;
    SkillGalleryAdapter adapterSkills;

    @Bind(R.id.loading_indicator)
    AVLoadingIndicatorView loading;

    @BindString(R.string.error_load)
    String errLoad;

    @BindString(R.string.error_network)
    String errNetwork;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private int action;

    protected void onCreate(Bundle saveBundle) {
        super.onCreate(saveBundle);
        setContentView(R.layout.activity_skill_select);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        action = getIntent().getIntExtra(Constants.EXTRA_ACTION_TYPE, 0);

        prepareActionBar();
        initViews();
        getCategories();
    }

    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(ServiceChangeEvent event) {
        if (event.getType() == 1) {
            finish();
        }
    }

    @Subscribe
    public void onEvent(ProjectChangeEvent event) {
        if (event.getType() == 1) {
            finish();
        }
    }

    private void initViews() {
        skills = new ArrayList<>();
        recyclerSkill.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerSkill.setItemAnimator(new DefaultItemAnimator());
        adapterSkills = new SkillGalleryAdapter(this, skills);
        adapterSkills.setOnSkillSelectListener(this);
        recyclerSkill.setAdapter(adapterSkills);
    }

    private void prepareActionBar() {
        setSupportActionBar(toolbar);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
        bar.setTitle("Select Skill");
    }

    private void getCategories() {
        loading.show();
        recyclerSkill.setVisibility(View.GONE);

        RestClient<SkillClient> restClient = new RestClient<>();
        SkillClient skillClient = restClient.getAppClient(SkillClient.class);

        Call<ArrayModel<SkillModel>> call = skillClient.getCategories();
        call.enqueue(new Callback<ArrayModel<SkillModel>>() {
            @Override
            public void onResponse(Call<ArrayModel<SkillModel>> call, Response<ArrayModel<SkillModel>> response) {
                loading.hide();
                loading.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body().getStatus()) {
                    if (response.body().getData().size() > 0) {
                        recyclerSkill.setVisibility(View.VISIBLE);
                        adapterSkills.refreshData(response.body().getData());
                    }
                } else {
                    Toast.makeText(SelectSkillActivity.this, errLoad, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayModel<SkillModel>> call, Throwable t) {
                loading.hide();
                loading.setVisibility(View.GONE);

                Toast.makeText(SelectSkillActivity.this, errNetwork, Toast.LENGTH_SHORT).show();
            }
        });
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
    public void onSkillSelect(SkillModel item) {
        if (action == Constants.ACTION_CREATE_PROJECT) {
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra(Constants.KEY_SKILL, item);
            startActivity(intent);
        } else if (action == Constants.ACTION_CREATE_SERVICE) {
            Intent intent = new Intent(this, ServiceCreateActivity.class);
            intent.putExtra(Constants.KEY_SKILL, item);
            startActivity(intent);
        }
    }
}

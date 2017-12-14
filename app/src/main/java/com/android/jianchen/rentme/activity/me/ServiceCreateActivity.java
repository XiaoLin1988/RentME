package com.android.jianchen.rentme.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.android.jianchen.rentme.activity.search.fragment.SelectSkillFragment;
import com.android.jianchen.rentme.activity.me.fragments.ServiceCreateFragment;
import com.android.jianchen.rentme.helper.delegator.OnSkillSelectListener;
import com.android.jianchen.rentme.model.rentme.SkillModel;
import com.android.jianchen.rentme.R;
import com.android.jianchen.rentme.helper.Constants;

/**
 * Created by emerald on 12/8/2017.
 */
public class ServiceCreateActivity extends AppCompatActivity implements OnSkillSelectListener {
    private Fragment step0;
    private Fragment step1;

    protected void onCreate(Bundle saveBundle) {
        super.onCreate(saveBundle);
        setContentView(R.layout.activity_service_create);

        initData();
        initViews();
    }

    private void initData() {
        step0 = SelectSkillFragment.newInstance(this, this);
    }

    private void initViews() {
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        fm.beginTransaction();

        ft.add(R.id.service_create_container, step0);
        ft.commit();
    }

    @Override
    public void onSkillSelect(SkillModel item) {
        step1 = ServiceCreateFragment.newInstance(this, item);

        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        fm.beginTransaction();

        ft.replace(R.id.service_create_container, step1);
        ft.commit();
    }

    public void onActivityResult(int reqCode, int resCode, Intent data) {
        if (reqCode == Constants.REQUEST_POST_PHOTO && resCode == RESULT_OK) {

        } else if (reqCode == Constants.REQUEST_POST_VIDEO && resCode == RESULT_OK) {
            String videoLink = data.getStringExtra(Constants.KEY_VIDEO);
        } else if (reqCode == Constants.REQUEST_POST_WEB && resCode == RESULT_OK) {
            String webLink = data.getStringExtra(Constants.KEY_WEB);
        } else {
            super.onActivityResult(reqCode, resCode, data);
        }
    }
}

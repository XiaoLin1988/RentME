package com.android.emerald.rentme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.android.emerald.rentme.Fragments.ProjectCreate0Fragment;
import com.android.emerald.rentme.Fragments.ServiceCreate1Fragment;
import com.android.emerald.rentme.Fragments.ServiceCreate2Fragment;
import com.android.emerald.rentme.Interface.OnSkillSelectListener;
import com.android.emerald.rentme.Models.Intro;
import com.android.emerald.rentme.Models.SkillModel;
import com.android.emerald.rentme.Utils.Constants;

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
        step0 = ProjectCreate0Fragment.newInstance(this, this);
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
        step1 = ServiceCreate2Fragment.newInstance(this, item);

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

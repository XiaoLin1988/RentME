package com.jianchen.rentme.activity.myprojects;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jianchen.rentme.R;
import com.jianchen.rentme.activity.myprojects.adapter.ImageDetailAdapter;
import com.jianchen.rentme.helper.Constants;

import java.util.ArrayList;

/**
 * Created by emerald on 10/6/2017.
 */
public class ImageDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager pager;
    private ArrayList<String> urls;
    private int position;

    protected void onCreate(Bundle saveBundle) {
        super.onCreate(saveBundle);
        setContentView(R.layout.activity_image_detail);
        urls = (ArrayList<String>) getIntent().getSerializableExtra(Constants.KEY_SUB_IMAGE);
        position = getIntent().getIntExtra(Constants.KEY_SUB_POSITION, 0);
        initUI();
    }

    private void initUI() {
        pager = (ViewPager)findViewById(R.id.pager_gallery_detail);
        ImageDetailAdapter adapter = new ImageDetailAdapter(ImageDetailActivity.this, urls);
        pager.setAdapter(adapter);
        pager.setCurrentItem(position);

        findViewById(R.id.img_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }
}

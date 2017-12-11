package com.android.jianchen.rentme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jianchen.rentme.Models.ProjectModel;
import com.android.jianchen.rentme.Utils.Constants;
import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by emerald on 6/5/2017.
 */
public class ProjectCompleteDetailActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject> {

    private ProjectModel project;

    private ImageView btnBack;
    private TextView txtPageTitle;

    private ImageView imgPreview;

    private TextView txtTitle;
    private TextView txtDescription;
    private TextView txtBalance;

    private Button btnLeave;

    protected void onCreate(Bundle savedBundle) {
        super.onCreate(savedBundle);
        setContentView(R.layout.activity_project_complete_detail);

        project = (ProjectModel) getIntent().getSerializableExtra(Constants.EXTRA_PROJECT_DETAIL);

        initViewVariables();
    }

    private void initViewVariables() {

        btnBack = (ImageView)findViewById(R.id.btn_project_complete_detail_back);
        btnBack.setOnClickListener(this);

        txtPageTitle = (TextView)findViewById(R.id.txt_project_complete_detail_page);
        txtPageTitle.setText(project.getSv_title());

        imgPreview = (ImageView)findViewById(R.id.img_complete_detail_preview);
        Glide.with(ProjectCompleteDetailActivity.this).load(project.getSv_preview())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .into(imgPreview);

        txtTitle = (TextView)findViewById(R.id.txt_project_complete_detail_title);
        txtTitle.setText(project.getSv_title());

        txtDescription = (TextView)findViewById(R.id.txt_project_complete_detail_description);
        txtDescription.setText(project.getSv_detail());

        txtBalance = (TextView)findViewById(R.id.txt_project_complete_detail_balance);
        txtBalance.setText(String.valueOf(project.getSv_balance()));

        btnLeave = (Button)findViewById(R.id.btn_project_complete_detail_leave);
        btnLeave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_project_complete_detail_leave:
                Intent intent = new Intent(this, LeaveReviewActivity.class);
                intent.putExtra(Constants.EXTRA_REVIEW_TYPE, Constants.VALUE_SERVICE);
                intent.putExtra(Constants.KEY_SERVICE, project);
                startActivity(intent);
                break;
            case R.id.btn_project_complete_detail_back:
                finish();
                break;
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            boolean status = response.getBoolean("status");
            if (status) {
                Toast.makeText(ProjectCompleteDetailActivity.this, "Review left successfully.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(ProjectCompleteDetailActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(ProjectCompleteDetailActivity.this, "Review left failure.", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        finish();
    }
}

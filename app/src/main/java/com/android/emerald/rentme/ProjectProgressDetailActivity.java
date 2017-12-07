package com.android.emerald.rentme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.emerald.rentme.Models.ProjectModel;
import com.android.emerald.rentme.Utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wallet.PaymentMethodTokenizationParameters;
import com.google.android.gms.wallet.PaymentMethodTokenizationType;
import com.google.android.gms.wallet.Wallet;
import com.google.gson.Gson;

/**
 * Created by emerald on 6/5/2017.
 */
public class ProjectProgressDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ProjectModel project;

    private ImageView btnBack;
    private TextView txtPage;

    private ImageView imgPreview;

    private TextView txtTitle;
    private TextView txtDescription;
    private TextView txtTimeframe;

    private Button btnChat, btnPay;

    protected void onCreate(Bundle savedBundle) {
        super.onCreate(savedBundle);
        setContentView(R.layout.activity_project_progress_detail);

        String json = getIntent().getStringExtra(Constants.EXTRA_PROJECT_DETAIL);
        Gson gson = new Gson();
        project = gson.fromJson(json, ProjectModel.class);

        initViewVariables();
    }

    private void initViewVariables() {
        btnBack = (ImageView)findViewById(R.id.btn_project_progress_detail_back);
        btnBack.setOnClickListener(this);

        txtPage = (TextView)findViewById(R.id.txt_project_progress_detail_page);
        txtPage.setText(project.getName());

        imgPreview = (ImageView)findViewById(R.id.img_progress_detail_preview);
        Glide.with(ProjectProgressDetailActivity.this).load(project.getPreview())
                .thumbnail(0.5f)
                .crossFade()
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgPreview);

        txtTitle = (TextView)findViewById(R.id.txt_project_progress_detail_title);
        txtTitle.setText(project.getName());

        txtDescription = (TextView)findViewById(R.id.txt_project_progress_detail_description);
        txtDescription.setText(project.getDescription());

        txtTimeframe = (TextView)findViewById(R.id.txt_project_progress_detail_timeframe);
        txtTimeframe.setText(Integer.toString(project.getTimeframe()));

        btnChat = (Button)findViewById(R.id.btn_project_progress_detail_chat);
        btnChat.setOnClickListener(this);

        btnPay = (Button)findViewById(R.id.btn_project_progress_detail_pay);
        btnPay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_project_progress_detail_chat:
                Intent intent = new Intent(ProjectProgressDetailActivity.this, ChattingActivity.class);
                String json = getIntent().getStringExtra(Constants.EXTRA_PROJECT_DETAIL);
                intent.putExtra(Constants.EXTRA_PROJECT_DETAIL, project.getId());
                startActivity(intent);
                finish();
                break;
            case R.id.btn_project_progress_detail_pay:
                startActivity(new Intent(ProjectProgressDetailActivity.this, PayActivity.class));
                break;
            case R.id.btn_project_progress_detail_back:
                finish();
                break;
        }
    }

    private void requestPayment() {

    }

    public void onBackPressed() {
        finish();
    }
}

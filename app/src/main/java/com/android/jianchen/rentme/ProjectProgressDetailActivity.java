package com.android.jianchen.rentme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jianchen.rentme.Events.ProjectCompleteEvent;
import com.android.jianchen.rentme.Models.ObjectModel;
import com.android.jianchen.rentme.Models.ProjectModel;
import com.android.jianchen.rentme.RestAPI.ProjectClient;
import com.android.jianchen.rentme.RestAPI.RestClient;
import com.android.jianchen.rentme.Utils.Constants;
import com.android.jianchen.rentme.Utils.DialogUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by emerald on 6/5/2017.
 */
public class ProjectProgressDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ProjectModel service;

    private ImageView btnBack;
    private TextView txtPage;

    private ImageView imgPreview;

    private TextView txtTitle;
    private TextView txtDescription;
    private TextView txtBalance;

    private Button btnComplete, btnPay;

    protected void onCreate(Bundle savedBundle) {
        super.onCreate(savedBundle);
        setContentView(R.layout.activity_project_progress_detail);

        service = (ProjectModel) getIntent().getSerializableExtra(Constants.EXTRA_PROJECT_DETAIL);

        initViewVariables();
    }

    private void initViewVariables() {
        btnBack = (ImageView)findViewById(R.id.btn_project_progress_detail_back);
        btnBack.setOnClickListener(this);

        txtPage = (TextView)findViewById(R.id.txt_project_progress_detail_page);
        txtPage.setText(service.getSv_title());

        imgPreview = (ImageView)findViewById(R.id.img_progress_detail_preview);
        Glide.with(ProjectProgressDetailActivity.this).load(service.getSv_preview())
                .thumbnail(0.5f)
                .crossFade()
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgPreview);

        txtTitle = (TextView)findViewById(R.id.txt_project_progress_detail_title);
        txtTitle.setText(service.getSv_title());

        txtDescription = (TextView)findViewById(R.id.txt_project_progress_detail_description);
        txtDescription.setText(service.getSv_detail());

        txtBalance = (TextView)findViewById(R.id.txt_project_progress_detail_balance);
        txtBalance.setText(Integer.toString(service.getSv_balance()));

        btnComplete = (Button)findViewById(R.id.btn_project_progress_detail_complete);
        btnComplete.setOnClickListener(this);

        /*
        btnPay = (Button)findViewById(R.id.btn_project_progress_detail_pay);
        btnPay.setOnClickListener(this);
        */
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_project_progress_detail_chat:
                Intent intent = new Intent(ProjectProgressDetailActivity.this, ChattingActivity.class);
                String json = getIntent().getStringExtra(Constants.EXTRA_PROJECT_DETAIL);
                intent.putExtra(Constants.EXTRA_PROJECT_DETAIL, service.getSv_id());
                startActivity(intent);
                finish();
                break;
            case R.id.btn_project_progress_detail_complete:
                final ProgressDialog dialog = DialogUtil.showProgressDialog(this, "Completing project");

                RestClient<ProjectClient> restClient = new RestClient<>();
                ProjectClient client = restClient.getAppClient(ProjectClient.class);
                Call<ObjectModel<Boolean>> call = client.completeProject(service.getPr_id());
                call.enqueue(new Callback<ObjectModel<Boolean>>() {
                    @Override
                    public void onResponse(Call<ObjectModel<Boolean>> call, Response<ObjectModel<Boolean>> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body().getStatus()) {
                            EventBus.getDefault().post(new ProjectCompleteEvent(service));
                            finish();
                        } else {
                            Toast.makeText(ProjectProgressDetailActivity.this, getResources().getString(R.string.error_load), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ObjectModel<Boolean>> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(ProjectProgressDetailActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.btn_project_progress_detail_back:
                finish();
                break;
        }
    }

    public void onBackPressed() {
        finish();
    }
}

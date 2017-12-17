package com.jianchen.rentme.activity.myprojects;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jianchen.rentme.activity.me.adapter.ReviewRecyclerAdapter;
import com.jianchen.rentme.activity.root.MainActivity;
import com.jianchen.rentme.helper.network.retrofit.ProjectClient;
import com.jianchen.rentme.helper.network.retrofit.RestClient;
import com.jianchen.rentme.helper.network.retrofit.ServiceClient;
import com.jianchen.rentme.helper.utils.DialogUtil;
import com.jianchen.rentme.helper.utils.Utils;
import com.jianchen.rentme.model.rentme.ArrayModel;
import com.jianchen.rentme.model.rentme.ObjectModel;
import com.jianchen.rentme.model.rentme.ProjectModel;
import com.jianchen.rentme.R;
import com.jianchen.rentme.helper.Constants;
import com.jianchen.rentme.model.rentme.ReviewModel;
import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

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

    private RecyclerView recyclerReviews;
    private ArrayList<ReviewModel> reviews;
    private ReviewRecyclerAdapter adapterReviews;

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

        if (project.getTalent_id() == Utils.retrieveUserInfo(ProjectCompleteDetailActivity.this).getId()) {
            btnLeave.setVisibility(View.GONE);
        }

        if (project.getPr_stts() == 2) {
            btnLeave.setVisibility(View.GONE);

            reviews = new ArrayList<>();
            recyclerReviews = (RecyclerView)findViewById(R.id.recycler_reviews);
            adapterReviews = new ReviewRecyclerAdapter(recyclerReviews, reviews, Constants.VALUE_SERVICE);
            recyclerReviews.setAdapter(adapterReviews);
            recyclerReviews.setLayoutManager(new LinearLayoutManager(ProjectCompleteDetailActivity.this));
            recyclerReviews.setNestedScrollingEnabled(false);

            getProjectReviews();
        }
    }

    private void getProjectReviews() {
        final ProgressDialog dialog = DialogUtil.showProgressDialog(this, "Please wait while loading");
        RestClient<ProjectClient> restClient = new RestClient<>();
        ProjectClient projectClient = restClient.getAppClient(ProjectClient.class);

        Call<ArrayModel<ReviewModel>> call = projectClient.getProjectReview(project.getPr_id(), Utils.retrieveUserInfo(ProjectCompleteDetailActivity.this).getId());
        call.enqueue(new Callback<ArrayModel<ReviewModel>>() {
            @Override
            public void onResponse(Call<ArrayModel<ReviewModel>> call, retrofit2.Response<ArrayModel<ReviewModel>> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body().getStatus()) {
                    if (response.body().getData().size() > 0)
                        adapterReviews.addReviews(response.body().getData());
                } else {
                    Toast.makeText(ProjectCompleteDetailActivity.this, getResources().getString(R.string.error_load), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayModel<ReviewModel>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(ProjectCompleteDetailActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_project_complete_detail_leave:
                Intent intent = new Intent(this, LeaveReviewActivity.class);
                intent.putExtra(Constants.EXTRA_REVIEW_TYPE, Constants.VALUE_SERVICE);
                intent.putExtra(Constants.KEY_REVIEW_ID, project.getSv_id());

                startActivityForResult(intent, Constants.REQUEST_REVIEW);
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

    public void onActivityResult(int reqCode, int resCode, Intent data) {
        if (reqCode == Constants.REQUEST_REVIEW && resCode == RESULT_OK) {
            RestClient<ProjectClient> restClient = new RestClient<>();
            ProjectClient projectClient = restClient.getAppClient(ProjectClient.class);
            Call<ObjectModel<Boolean>> call = projectClient.reviewProject(project.getPr_id());
            call.enqueue(new Callback<ObjectModel<Boolean>>() {
                @Override
                public void onResponse(Call<ObjectModel<Boolean>> call, retrofit2.Response<ObjectModel<Boolean>> response) {
                    btnLeave.setVisibility(View.GONE);

                    reviews = new ArrayList<>();
                    recyclerReviews = (RecyclerView)findViewById(R.id.recycler_reviews);
                    adapterReviews = new ReviewRecyclerAdapter(recyclerReviews, reviews, Constants.VALUE_SERVICE);
                    recyclerReviews.setAdapter(adapterReviews);
                    recyclerReviews.setLayoutManager(new LinearLayoutManager(ProjectCompleteDetailActivity.this));

                    getProjectReviews();
                }

                @Override
                public void onFailure(Call<ObjectModel<Boolean>> call, Throwable t) {
                    Toast.makeText(ProjectCompleteDetailActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

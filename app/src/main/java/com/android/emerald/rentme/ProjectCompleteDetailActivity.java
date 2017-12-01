package com.android.emerald.rentme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.emerald.rentme.Models.ProjectModel;
import com.android.emerald.rentme.Models.UserModel;
import com.android.emerald.rentme.Task.APIRequester;
import com.android.emerald.rentme.Utils.Constants;
import com.android.emerald.rentme.Utils.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by emerald on 6/5/2017.
 */
public class ProjectCompleteDetailActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject> {

    private ProjectModel project;
    private UserModel curUser;

    private ImageView btnBack;
    private TextView txtPageTitle;

    private ImageView imgPreview;

    private TextView txtTitle;
    private TextView txtDescription;
    private TextView txtTimeframe;
    private SimpleRatingBar rating;
    private EditText editReview;
    private TextView txtWriter;

    private Button btnLeave;

    protected void onCreate(Bundle savedBundle) {
        super.onCreate(savedBundle);
        setContentView(R.layout.activity_project_complete_detail);

        curUser = Utils.retrieveUserInfo(getBaseContext());

        Gson gson = new Gson();
        String json = getIntent().getStringExtra(Constants.EXTRA_PROJECT_DETAIL);
        project = gson.fromJson(json, ProjectModel.class);

        initViewVariables();
    }

    private void initViewVariables() {

        btnBack = (ImageView)findViewById(R.id.btn_project_complete_detail_back);
        btnBack.setOnClickListener(this);

        txtPageTitle = (TextView)findViewById(R.id.txt_project_complete_detail_page);
        txtPageTitle.setText(project.getName());

        imgPreview = (ImageView)findViewById(R.id.img_complete_detail_preview);
        Glide.with(ProjectCompleteDetailActivity.this).load(project.getPreview())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgPreview);

        txtTitle = (TextView)findViewById(R.id.txt_project_complete_detail_title);
        txtTitle.setText(project.getName());

        txtDescription = (TextView)findViewById(R.id.txt_project_complete_detail_description);
        txtDescription.setText(project.getDescription());

        txtTimeframe = (TextView)findViewById(R.id.txt_project_complete_detail_timeframe);
        txtTimeframe.setText(Integer.toString(project.getTimeframe()));

        rating = (SimpleRatingBar)findViewById(R.id.rating_project_complete_detail);

        editReview = (EditText)findViewById(R.id.edit_project_complete_detail_review);

        txtWriter = (TextView)findViewById(R.id.txt_project_complete_detail_writer);

        btnLeave = (Button)findViewById(R.id.btn_project_complete_detail_leave);
        btnLeave.setOnClickListener(this);

        if (project.getTalent_score() != 0 && project.getConsumer_score() != 0) {
            if (curUser.getType() == 1) {
                rating.setRating(project.getConsumer_score());
                editReview.setText(project.getConsumer_review());
                txtWriter.setText(Integer.toString(project.getTalent_id()));
            } else if (curUser.getType() == 2) {
                rating.setRating(project.getTalent_score());
                editReview.setText(project.getTalent_review());
                txtWriter.setText(Integer.toString(project.getConsumer_id()));
            }
            rating.setIndicator(true);
            editReview.setEnabled(false);
            txtWriter.setVisibility(View.GONE);

            btnLeave.setVisibility(View.GONE);
        } else {
            if (curUser.getType() == 1 && project.getTalent_score() != 0) {
                rating.setVisibility(View.GONE);
                editReview.setVisibility(View.GONE);
                txtWriter.setVisibility(View.GONE);

                btnLeave.setVisibility(View.GONE);
            } else if (curUser.getType() == 2 && project.getConsumer_score() != 0) {
                rating.setVisibility(View.GONE);
                editReview.setVisibility(View.GONE);
                txtWriter.setVisibility(View.GONE);

                btnLeave.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_project_complete_detail_leave:
                if (rating.getRating() == 0 || editReview.getText().toString().equals("")) {
                    Toast.makeText(ProjectCompleteDetailActivity.this, "You should input ratings and reviews.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (curUser.getType() == 1) {
                    String url = Constants.API_ROOT_URL + Constants.API_REVIEW_TALENT;

                    Map<String, String> params = new HashMap<>();
                    params.put("projectid", Integer.toString(project.getId()));
                    params.put("talent_score", Integer.toString((int)rating.getRating()));
                    params.put("talent_review", editReview.getText().toString());

                    APIRequester requester = new APIRequester(Request.Method.POST, url, params, this, null);
                    AppController.getInstance().addToRequestQueue(requester, Constants.JSON_REQUEST);
                } else if (curUser.getType() == 2) {
                    String url = Constants.API_ROOT_URL + Constants.API_REVIEW_CONSUMER;

                    Map<String, String> params = new HashMap<>();
                    params.put("projectid", Integer.toString(project.getId()));
                    params.put("consumer_score", Integer.toString((int)rating.getRating()));
                    params.put("consumer_review", editReview.getText().toString());

                    APIRequester requester = new APIRequester(Request.Method.POST, url, params, this, null);
                    AppController.getInstance().addToRequestQueue(requester, Constants.JSON_REQUEST);
                }
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

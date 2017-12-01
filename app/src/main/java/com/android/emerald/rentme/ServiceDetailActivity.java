package com.android.emerald.rentme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.emerald.rentme.Interface.OnPaidListener;
import com.android.emerald.rentme.Models.ServiceModel;
import com.android.emerald.rentme.Task.APIRequester;
import com.android.emerald.rentme.Utils.Constants;
import com.android.emerald.rentme.Utils.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by emerald on 6/26/2017.
 */
public class ServiceDetailActivity extends AppCompatActivity implements ViewTreeObserver.OnScrollChangedListener, View.OnClickListener, OnPaidListener {
    private ScrollView scrlDetail;
    private LinearLayout lytSelect;

    private ImageView imgPreview;
    private ImageView imgBack;

    private TextView txtTitle;
    private TextView txtDescription;
    private TextView txtUsername;
    private TextView txtUserDescription;
    private CircleImageView imgUseravatar;
    private CircleImageView imgRateavatar;
    private TextView txtRatename;
    private TextView txtRatedate;
    private SimpleRatingBar ratingScore;
    private TextView txtRatereview;

    private Button btnBuy;

    private ServiceModel service;

    protected void onCreate(Bundle savedBundle) {
        super.onCreate(savedBundle);
        setContentView(R.layout.activity_service_detail);

        String json = getIntent().getStringExtra(Constants.EXTRA_SERVICE_DETAIL);
        Gson gson = new Gson();
        service = gson.fromJson(json, ServiceModel.class);

        getTalent();

        initViewVariables();
    }

    private void initViewVariables() {
        scrlDetail = (ScrollView)findViewById(R.id.scrl_service_detail);
        scrlDetail.getViewTreeObserver().addOnScrollChangedListener(this);
        lytSelect = (LinearLayout)findViewById(R.id.lyt_select);

        imgPreview = (ImageView)findViewById(R.id.img_service_detail_preview);
        Picasso.with(ServiceDetailActivity.this).load(service.getPreview()).resize(Constants.IMAGE_MAX_SIZE, Constants.IMAGE_MAX_SIZE).centerCrop().into(imgPreview);

        imgBack = (ImageView)findViewById(R.id.img_service_detail_back);
        imgBack.setOnClickListener(this);

        txtTitle = (TextView)findViewById(R.id.txt_service_detail_title);
        txtTitle.setText(service.getTitle());

        txtDescription = (TextView)findViewById(R.id.txt_service_detail_description);
        txtDescription.setText(service.getDetail());

        txtUsername = (TextView)findViewById(R.id.txt_service_detail_username);

        txtUserDescription = (TextView)findViewById(R.id.txt_service_detail_userdescription);

        imgUseravatar = (CircleImageView)findViewById(R.id.img_service_detail_useravatar);

        imgRateavatar = (CircleImageView)findViewById(R.id.img_service_detail_rateavatar);
        txtRatename = (TextView)findViewById(R.id.txt_service_detail_ratename);
        ratingScore = (SimpleRatingBar)findViewById(R.id.rate_service_detail_score);
        //txtRatedate = (TextView)findViewById(R.id.txt_service_detail_ratedate);
        txtRatereview = (TextView)findViewById(R.id.txt_service_detail_ratereview);

        btnBuy = (Button)findViewById(R.id.btn_service_detail_buy);
        btnBuy.setOnClickListener(this);
    }

    private void getTalent() {
        String url = Constants.API_ROOT_URL + Constants.API_USER_GET_BY_ID;

        Map<String, String> params = new HashMap<>();
        params.put("userid", Integer.toString(service.getTalent_id()));

        APIRequester requester = new APIRequester(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status")) {
                        JSONArray datas = response.getJSONArray("data");
                        if (datas.length() > 0) {
                            JSONObject data = datas.getJSONObject(0);
                            txtUsername.setText(data.getString("name"));
                            if (!data.getString("description").equals("null") && data.getString("description") != null) {
                                txtUserDescription.setText(data.getString("description"));
                            }
                            Glide.with(ServiceDetailActivity.this).load(data.getString("avatar")).into(imgUseravatar);
                        } else {
                            Toast.makeText(ServiceDetailActivity.this, "Sorry. Talent account is closed", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(ServiceDetailActivity.this, response.getString("data"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getReviews();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ServiceDetailActivity.this, "Please check your network state", Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(requester, Constants.JSON_REQUEST);
    }

    private void getReviews() {
        String url = Constants.API_ROOT_URL + Constants.API_SERVICE_REVIEW;

        Map<String, String> params = new HashMap<>();
        params.put("service_id", Integer.toString(service.getId()));

        APIRequester requester = new APIRequester(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status")) {
                        JSONArray data = response.getJSONArray("data");
                        if (data.length() > 0) {
                            JSONObject lastReview = data.getJSONObject(0);
                            Glide.with(ServiceDetailActivity.this).load(lastReview.getString("avatar")).into(imgRateavatar);
                            txtRatename.setText(lastReview.getString("name"));
                            txtRatename.setVisibility(View.VISIBLE);
                            txtRatereview.setText(lastReview.getString("talent_review"));
                            txtRatereview.setVisibility(View.VISIBLE);
                            ratingScore.setRating((float) lastReview.getDouble("talent_score"));
                            ratingScore.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ServiceDetailActivity.this, "Please check your network state", Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(requester, Constants.JSON_REQUEST);
    }

    private void createProject() {
        String url = Constants.API_ROOT_URL + Constants.API_PROJECT_CREATE;

        Map<String, String> params = new HashMap<>();
        params.put("title", service.getTitle());
        params.put("description", service.getDetail());
        params.put("consumer_id", Integer.toString(Utils.retrieveUserInfo(ServiceDetailActivity.this).getId()));
        params.put("talent_id", Integer.toString(service.getTalent_id()));
        params.put("skill", service.getSkill());
        params.put("preview", service.getPreview());

        APIRequester requester = new APIRequester(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(ServiceDetailActivity.this, "You bought this service successfully", Toast.LENGTH_LONG).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ServiceDetailActivity.this, "Please check your network state", Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(requester, Constants.JSON_REQUEST);
    }

    @Override
    public void onScrollChanged() {
        int scrollY = scrlDetail.getScrollY();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        if (scrollY > 400)
            lytSelect.setVisibility(View.VISIBLE);
        else
            lytSelect.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_service_detail_back:
                finish();
                break;
            case R.id.btn_service_detail_buy:
                //createProject();
                PayActivity.navigate(this, this, service.getBalance());
                break;
        }
    }

    @Override
    public void onPaid(String detail) {
        try {
            JSONObject data = new JSONObject(detail);
            JSONObject response = data.getJSONObject("response");
            String create_time = response.getString("create_time");
            String id = response.getString("id");
            String state = response.getString("state");

            if (state.equals("approved")) {
                createProject();
            } else {
                Toast.makeText(ServiceDetailActivity.this, "You bought this service successfully", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

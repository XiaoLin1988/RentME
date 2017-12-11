package com.android.jianchen.rentme.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.jianchen.rentme.Adapter.ReviewRecyclerAdapter;
import com.android.jianchen.rentme.Interface.OnLoadMoreListener;
import com.android.jianchen.rentme.LeaveReviewActivity;
import com.android.jianchen.rentme.Models.ArrayModel;
import com.android.jianchen.rentme.Models.ReviewModel;
import com.android.jianchen.rentme.R;
import com.android.jianchen.rentme.RestAPI.RestClient;
import com.android.jianchen.rentme.RestAPI.ServiceClient;
import com.android.jianchen.rentme.Utils.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by emerald on 12/8/2017.
 */
public class ReviewDialog extends Dialog implements View.OnClickListener, OnLoadMoreListener {
    RecyclerView recyclerReview;
    ReviewRecyclerAdapter adapterReview;

    private int type = Constants.VALUE_SERVICE;
    private int serviceId;
    private ArrayList<ReviewModel> reviews;

    private LinearLayout lytReview, lytRate;
    private ImageView imgRate;

    public ReviewDialog(Context context, int sid, ArrayList<ReviewModel> rl) {
        super(context);
        reviews = rl;
        serviceId = sid;

        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_review);
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationSlide;

        initDialog();
    }

    private void initDialog() {
        findViewById(R.id.img_close).setOnClickListener(this);

        recyclerReview = (RecyclerView)findViewById(R.id.recycler_reviews);
        recyclerReview.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterReview = new ReviewRecyclerAdapter(recyclerReview, reviews);
        adapterReview.setOnLoadMoreListener(this);
        recyclerReview.setAdapter(adapterReview);
        recyclerReview.setNestedScrollingEnabled(true);

        imgRate = (ImageView)findViewById(R.id.img_review_rate);
        lytRate = (LinearLayout)findViewById(R.id.lyt_review_rate);
        lytRate.setOnClickListener(this);

        lytReview = (LinearLayout)findViewById(R.id.lyt_review_review);
        lytReview.setOnClickListener(this);
    }

    public void setType(int type) {
        this.type = type;
        if (type == Constants.VALUE_REVIEW) {
            findViewById(R.id.ryt_review).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_close:
                dismiss();
                break;
            case R.id.lyt_review_rate:

                break;
            case R.id.lyt_review_review:
                Intent intent = new Intent(getContext(), LeaveReviewActivity.class);
                intent.putExtra(Constants.EXTRA_REVIEW_TYPE, Constants.VALUE_REVIEW);
                //intent.putExtra(Constants.KEY_REVIEW, review);
                getContext().startActivity(intent);
                break;
        }
    }

    @Override
    public void onLoadMore(int curpage) {
        RestClient<ServiceClient> restClient = new RestClient<>();
        ServiceClient serviceClient = restClient.getAppClient(ServiceClient.class);

        Call<ArrayModel<ReviewModel>> call = serviceClient.getServiceReview(serviceId, curpage);
        call.enqueue(new Callback<ArrayModel<ReviewModel>>() {
            @Override
            public void onResponse(Call<ArrayModel<ReviewModel>> call, retrofit2.Response<ArrayModel<ReviewModel>> response) {
                adapterReview.setLoaded();
                if (response.isSuccessful()) {
                    ArrayModel<ReviewModel> r = response.body();
                    reviews = r.getData();
                    if (reviews.size() > 0) {
                        adapterReview.addReviews(reviews);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayModel<ReviewModel>> call, Throwable t) {
                adapterReview.setLoaded();
            }
        });
    }
}

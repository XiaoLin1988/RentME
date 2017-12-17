package com.jianchen.rentme.activity.me.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jianchen.rentme.activity.me.adapter.ReviewRecyclerAdapter;
import com.jianchen.rentme.helper.delegator.OnLoadMoreListener;
import com.jianchen.rentme.model.rentme.ArrayModel;
import com.jianchen.rentme.model.rentme.ObjectModel;
import com.jianchen.rentme.model.rentme.ReviewModel;
import com.jianchen.rentme.R;
import com.jianchen.rentme.helper.network.retrofit.RestClient;
import com.jianchen.rentme.helper.network.retrofit.ReviewClient;
import com.jianchen.rentme.helper.network.retrofit.ServiceClient;
import com.jianchen.rentme.helper.Constants;
import com.jianchen.rentme.helper.utils.Utils;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by emerald on 12/8/2017.
 */
public class ReviewDialog extends Dialog implements View.OnClickListener, OnLoadMoreListener, EditProfileDialog.OnUpdateListener {
    RecyclerView recyclerReview;
    ReviewRecyclerAdapter adapterReview;

    private ArrayList<ReviewModel> reviews;

    private LinearLayout lytReview, lytRate;
    private ImageView imgRate;

    private int type = Constants.VALUE_SERVICE;

    private int serviceId;
    //private int reviewId;
    private ReviewModel review;

    // Service Review Dialog
    public ReviewDialog(Context context, int sid, ArrayList<ReviewModel> rl) {
        super(context);

        serviceId = sid;
        type = Constants.VALUE_SERVICE;
        reviews = rl;

        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_review);
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationSlide;

        initDialog();
    }

    // Review Review Dialog
    public ReviewDialog(Context context, ReviewModel rv) {
        super(context);

        //reviewId = rid;

        type = Constants.VALUE_REVIEW;
        review = rv;

        reviews = new ArrayList<>();
        //reviews.add(new ReviewModel());

        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_review);
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationSlide;

        initDialog();

        getReviewReviews(0);
    }

    private void initDialog() {
        findViewById(R.id.img_close).setOnClickListener(this);

        recyclerReview = (RecyclerView)findViewById(R.id.recycler_reviews);
        recyclerReview.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterReview = new ReviewRecyclerAdapter(recyclerReview, reviews, type);
        adapterReview.setOnLoadMoreListener(this);
        recyclerReview.setAdapter(adapterReview);
        recyclerReview.setNestedScrollingEnabled(true);

        imgRate = (ImageView)findViewById(R.id.img_review_rate);
        lytRate = (LinearLayout)findViewById(R.id.lyt_review_rate);
        lytRate.setOnClickListener(this);

        lytReview = (LinearLayout)findViewById(R.id.lyt_review_review);
        lytReview.setOnClickListener(this);

        if (type == Constants.VALUE_REVIEW) {
            findViewById(R.id.ryt_review).setVisibility(View.VISIBLE);
            findViewById(R.id.lyt_origin_review).setVisibility(View.VISIBLE);
            CircularImageView imgAvatar = (CircularImageView)findViewById(R.id.img_review_avatar);
            Glide.with(getContext()).load(review.getUser_avatar()).asBitmap().centerCrop().placeholder(R.drawable.profile_empty).into(imgAvatar);
            TextView txtName = (TextView)findViewById(R.id.txt_review_name);
            txtName.setText(review.getUser_name());
            TextView txtDate = (TextView)findViewById(R.id.txt_review_date);
            Date date = Utils.stringToDate(review.getRv_ctime());
            txtDate.setText(Utils.beautifyDate(date, false));
            ReadMoreTextView txtContent = (ReadMoreTextView)findViewById(R.id.txt_review_content);
            txtContent.setText(review.getRv_content());
            if (review.isRated())
                imgRate.setImageResource(R.drawable.heart_fill);
        }
    }

    private void createRate() {
        RestClient<ReviewClient> restClient = new RestClient<>();
        ReviewClient reviewClient = restClient.getAppClient(ReviewClient.class);

        Call<ObjectModel<Integer>> call = reviewClient.createRate(Constants.VALUE_REVIEW, review.getId(), Utils.retrieveUserInfo(getContext()).getId());
        call.enqueue(new Callback<ObjectModel<Integer>>() {
            @Override
            public void onResponse(Call<ObjectModel<Integer>> call, Response<ObjectModel<Integer>> response) {
                if (response.isSuccessful() && response.body().getStatus()) {
                    int r = response.body().getData();
                    if (r == 0) {
                        imgRate.setImageResource(R.drawable.heart);
                    } else {
                        imgRate.setImageResource(R.drawable.heart_fill);
                    }
                }
            }

            @Override
            public void onFailure(Call<ObjectModel<Integer>> call, Throwable t) {

            }
        });
    }

    private void createReview(String review) {
        RestClient<ReviewClient> restClient = new RestClient<>();
        ReviewClient reviewClient = restClient.getAppClient(ReviewClient.class);

        Call<ObjectModel<Integer>> call = reviewClient.createReview(Constants.VALUE_REVIEW, this.review.getId(), review, 1, Utils.retrieveUserInfo(getContext()).getId());

        call.enqueue(new Callback<ObjectModel<Integer>>() {
            @Override
            public void onResponse(Call<ObjectModel<Integer>> call, Response<ObjectModel<Integer>> response) {
                if (response.isSuccessful() && response.body().getStatus()) {

                }
            }

            @Override
            public void onFailure(Call<ObjectModel<Integer>> call, Throwable t) {

            }
        });
    }

    private void getServiceReviews(int curpage) {
        RestClient<ServiceClient> restClient = new RestClient<>();
        ServiceClient serviceClient = restClient.getAppClient(ServiceClient.class);

        Call<ArrayModel<ReviewModel>> call = serviceClient.getServiceReview(serviceId, Utils.retrieveUserInfo(getContext()).getId());
        call.enqueue(new Callback<ArrayModel<ReviewModel>>() {
            @Override
            public void onResponse(Call<ArrayModel<ReviewModel>> call, Response<ArrayModel<ReviewModel>> response) {
                if (response.isSuccessful() && response.body().getStatus()) {
                    adapterReview.setLoaded();
                    if (response.isSuccessful()) {
                        ArrayModel<ReviewModel> r = response.body();
                        reviews = r.getData();
                        if (reviews.size() > 0) {
                            adapterReview.addReviews(reviews);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayModel<ReviewModel>> call, Throwable t) {
                adapterReview.setLoaded();
            }
        });
    }

    private void getReviewReviews(int curpage) {
        RestClient<ReviewClient> restClient = new RestClient<ReviewClient>();
        ReviewClient reviewClient = restClient.getAppClient(ReviewClient.class);

        Call<ArrayModel<ReviewModel>> call = reviewClient.getReviewReview(review.getId(), Utils.retrieveUserInfo(getContext()).getId(), curpage);
        call.enqueue(new Callback<ArrayModel<ReviewModel>>() {
            @Override
            public void onResponse(Call<ArrayModel<ReviewModel>> call, Response<ArrayModel<ReviewModel>> response) {
                if (response.isSuccessful() && response.body().getStatus()) {
                    adapterReview.setLoaded();
                    if (response.isSuccessful()) {
                        ArrayModel<ReviewModel> r = response.body();
                        reviews = r.getData();
                        if (reviews.size() > 0) {
                            adapterReview.addReviews(reviews);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayModel<ReviewModel>> call, Throwable t) {
                adapterReview.setLoaded();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_close:
                dismiss();
                break;
            case R.id.lyt_review_rate:
                createRate();
                break;
            case R.id.lyt_review_review:

                EditProfileDialog dialog = new EditProfileDialog(getContext());
                dialog.setUpdateListener(this);
                dialog.setTag(99); // 99 means review dialogue
                dialog.show();

                break;

        }
    }

    @Override
    public void onLoadMore(int curpage) {
        /*
        if (type == Constants.VALUE_SERVICE) {
            getServiceReviews(curpage);
        } else if (type == Constants.VALUE_REVIEW) {
            getReviewReviews(curpage);
        }
        */
    }

    @Override
    public void onUpdate(String review, int tag) {

        createReview(review);

    }

}

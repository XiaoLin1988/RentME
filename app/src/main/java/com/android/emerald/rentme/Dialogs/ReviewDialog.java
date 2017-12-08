package com.android.emerald.rentme.Dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.android.emerald.rentme.Adapter.ReviewRecyclerAdapter;
import com.android.emerald.rentme.Interface.OnLoadMoreListener;
import com.android.emerald.rentme.Listener.EndlessRecyclerViewScrollListener;
import com.android.emerald.rentme.Models.ArrayModel;
import com.android.emerald.rentme.Models.ReviewModel;
import com.android.emerald.rentme.R;
import com.android.emerald.rentme.RestAPI.RestClient;
import com.android.emerald.rentme.RestAPI.ServiceClient;
import com.android.emerald.rentme.Utils.DialogUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by emerald on 12/8/2017.
 */
public class ReviewDialog extends Dialog implements View.OnClickListener, OnLoadMoreListener {
    RecyclerView recyclerReview;
    ReviewRecyclerAdapter adapterReview;

    private int serviceId;
    private ArrayList<ReviewModel> reviews;

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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_close:
                dismiss();
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

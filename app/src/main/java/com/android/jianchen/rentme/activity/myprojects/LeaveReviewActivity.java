package com.android.jianchen.rentme.activity.myprojects;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jianchen.rentme.activity.me.adapter.VideoLinkRecyclerAdapter;
import com.android.jianchen.rentme.activity.me.adapter.WebLinkRecyclerAdapter;
import com.android.jianchen.rentme.activity.me.dialogs.PhotoDialog;
import com.android.jianchen.rentme.activity.me.dialogs.VideoLinkDialog;
import com.android.jianchen.rentme.activity.me.dialogs.WebLinkDialog;
import com.android.jianchen.rentme.helper.delegator.OnDialogSelectListener;
import com.android.jianchen.rentme.helper.delegator.OnPostVideoListener;
import com.android.jianchen.rentme.helper.delegator.OnPostWebListener;
import com.android.jianchen.rentme.helper.listener.LoadCompleteListener;
import com.android.jianchen.rentme.model.rentme.ObjectModel;
import com.android.jianchen.rentme.model.rentme.WebLinkModel;
import com.android.jianchen.rentme.R;
import com.android.jianchen.rentme.helper.network.retrofit.CommonClient;
import com.android.jianchen.rentme.helper.network.retrofit.RestClient;
import com.android.jianchen.rentme.helper.network.retrofit.ReviewClient;
import com.android.jianchen.rentme.helper.Constants;
import com.android.jianchen.rentme.helper.utils.DialogUtil;
import com.android.jianchen.rentme.helper.utils.Utils;
import com.willy.ratingbar.ScaleRatingBar;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by emerald on 12/10/2017.
 */
public class LeaveReviewActivity extends AppCompatActivity implements View.OnClickListener, OnPostVideoListener, OnPostWebListener {
    @Bind(R.id.img_close)
    ImageView imgClose;
    @Bind(R.id.txt_done)
    TextView txtDone;
    @Bind(R.id.edit_review_content)
    EditText editContent;
    @Bind(R.id.rating_review)
    ScaleRatingBar rating;
    @Bind(R.id.btn_post_add)
    FloatingActionButton btnAdd;

    @Bind(R.id.recycler_photos)
    RecyclerView recyclerPhoto;

    @Bind(R.id.recycler_web_links)
    RecyclerView recyclerWeb;
    WebLinkRecyclerAdapter adapterWebLink;
    ArrayList<WebLinkModel> webLinkModels;

    @Bind(R.id.recycler_videos)
    RecyclerView recyclerVideo;;
    VideoLinkRecyclerAdapter adapterVideo;
    ArrayList<String> videoLinks;

    private int reviewType;
    /*
    private ProjectModel service;
    private ReviewModel review;
    */
    private int reviewId;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_review_leave);
        ButterKnife.bind(this);

        reviewType = getIntent().getIntExtra(Constants.EXTRA_REVIEW_TYPE, Constants.VALUE_SERVICE);
        reviewId = getIntent().getIntExtra(Constants.KEY_REVIEW_ID, 0);
        /*
        if (reviewType == Constants.VALUE_SERVICE) {
            service = (ProjectModel)getIntent().getSerializableExtra(Constants.KEY_SERVICE);
        } else {
            review = (ReviewModel)getIntent().getSerializableExtra(Constants.KEY_REVIEW);
        }
        */

        initViews();
    }

    private void initViews() {
        btnAdd.setOnClickListener(this);
        txtDone.setOnClickListener(this);
        imgClose.setOnClickListener(this);

        webLinkModels = new ArrayList<>();
        adapterWebLink = new WebLinkRecyclerAdapter(webLinkModels);
        recyclerWeb.setLayoutManager(new LinearLayoutManager(this));
        recyclerWeb.setAdapter(adapterWebLink);

        videoLinks = new ArrayList<>();
        adapterVideo = new VideoLinkRecyclerAdapter(videoLinks);
        recyclerVideo.setLayoutManager(new LinearLayoutManager(this));
        recyclerVideo.setAdapter(adapterVideo);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_post_add:
                CharSequence[] options = {"Add image", "Add Youtube/Vimeo video link", "Add web link"};
                DialogUtil.showSelectDialog(this, options, new OnDialogSelectListener() {
                    @Override
                    public void onDialogSelect(int position) {
                        if (position == 0) {
                            PhotoDialog dialog = new PhotoDialog(LeaveReviewActivity.this);
                            dialog.show();
                        } else if (position == 1) {
                            VideoLinkDialog dialog = new VideoLinkDialog(LeaveReviewActivity.this);
                            dialog.setVideoListener(LeaveReviewActivity.this);
                            dialog.show();
                        } else if (position == 2) {
                            WebLinkDialog dialog = new WebLinkDialog(LeaveReviewActivity.this);
                            dialog.setWebListener(LeaveReviewActivity.this);
                            dialog.show();
                        }
                    }
                });
                break;
            case R.id.txt_done:
                if (validate()) {
                    createReview();
                }
                break;
            case R.id.img_close:
                finish();
                break;
        }
    }

    private boolean validate() {
        if (editContent.getText().toString().equals("")) {
            Toast.makeText(this, "Please input your review", Toast.LENGTH_SHORT).show();
            return false;
        } else if (rating.getRating() < 1) {
            Toast.makeText(this, "Please select at least one score", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void createReview() {
        final ProgressDialog dialog = DialogUtil.showProgressDialog(this, "Please wait while creating review");

        /*
        ReviewModel r = new ReviewModel();
        r.setRv_content(editContent.getText().toString());
        r.setRv_score((int)rating.getRating());
        if (reviewType == Constants.VALUE_SERVICE) {
            r.setRv_type(Constants.VALUE_SERVICE);
            r.setRv_fid(reviewId);
        } else if (reviewType == Constants.VALUE_REVIEW) {
            r.setRv_type(Constants.VALUE_REVIEW);
            r.setRv_fid(reviewId);
        }
        r.setRv_usr_id(Utils.retrieveUserInfo(this).getId());
        */

        RestClient<ReviewClient> restClient = new RestClient<>();
        ReviewClient reviewClient = restClient.getAppClient(ReviewClient.class);

        Call<ObjectModel<Integer>> call = reviewClient.createReview(reviewType, reviewId, editContent.getText().toString(), (int)rating.getRating(), Utils.retrieveUserInfo(this).getId());
        call.enqueue(new Callback<ObjectModel<Integer>>() {
            @Override
            public void onResponse(Call<ObjectModel<Integer>> call, Response<ObjectModel<Integer>> response) {
                if (response.isSuccessful() && response.body().getStatus()) {
                    int reviewId = response.body().getData();

                    RestClient<CommonClient> restClient1 = new RestClient<>();
                    CommonClient commonClient = restClient1.getAppClient(CommonClient.class);
                    int loadCount = 0;
                    if (webLinkModels.size() > 0)
                        loadCount ++;
                    if (videoLinks.size() > 0)
                        loadCount ++;
                    if (loadCount > 0) {
                        final LoadCompleteListener loadListener = new LoadCompleteListener(loadCount) {
                            @Override
                            public void onLoaded() {
                                dialog.dismiss();
                                finish();
                            }
                        };
                        if (webLinkModels.size() > 0) {
                            JSONStringer webs = new JSONStringer();
                            try {
                                webs = webs.array();
                                for (int i = 0; i < webLinkModels.size(); i++) {
                                    webs.object();
                                    webs.key("title").value(webLinkModels.get(i).getTitle());
                                    webs.key("content").value(webLinkModels.get(i).getContent());
                                    webs.key("thumbnail").value(webLinkModels.get(i).getThumbnail());
                                    webs.key("link").value(webLinkModels.get(i).getLink());
                                    webs.endObject();
                                }
                                webs.endArray();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Call<ObjectModel<String>> call1 = commonClient.uploadWebs(Constants.VALUE_REVIEW, reviewId, webs);
                            call1.enqueue(new Callback<ObjectModel<String>>() {
                                @Override
                                public void onResponse(Call<ObjectModel<String>> call, Response<ObjectModel<String>> response) {
                                    loadListener.setLoaded();
                                    if (response.isSuccessful() && response.body().getStatus()) {
                                        int a = 0;
                                        a += 5;
                                    } else {

                                    }
                                }

                                @Override
                                public void onFailure(Call<ObjectModel<String>> call, Throwable t) {
                                    loadListener.setLoaded();
                                }
                            });
                        }
                        if (videoLinks.size() > 0) {
                            Call<ObjectModel<String>> call1 = commonClient.uploadVideos(Constants.VALUE_REVIEW, reviewId, videoLinks);
                            call1.enqueue(new Callback<ObjectModel<String>>() {
                                @Override
                                public void onResponse(Call<ObjectModel<String>> call, Response<ObjectModel<String>> response) {
                                    loadListener.setLoaded();
                                    if (response.isSuccessful() && response.body().getStatus()) {
                                        int a = 0;
                                        a += 5;
                                    } else {

                                    }
                                }

                                @Override
                                public void onFailure(Call<ObjectModel<String>> call, Throwable t) {
                                    loadListener.setLoaded();
                                }
                            });
                        }
                    } else {
                        dialog.dismiss();
                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(LeaveReviewActivity.this, getResources().getString(R.string.error_load), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ObjectModel<Integer>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(LeaveReviewActivity.this, getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPostVideo(String link) {
        adapterVideo.addItem(link);
    }

    @Override
    public void onPostWeb(WebLinkModel webLinkModel) {
        adapterWebLink.addItem(webLinkModel);
    }
}

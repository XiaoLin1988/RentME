package com.android.jianchen.rentme.Adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jianchen.rentme.Dialogs.ReviewDialog;
import com.android.jianchen.rentme.Interface.OnLoadMoreListener;
import com.android.jianchen.rentme.Listener.SingleClickListener;
import com.android.jianchen.rentme.Models.ArrayModel;
import com.android.jianchen.rentme.Models.ObjectModel;
import com.android.jianchen.rentme.Models.ReviewModel;
import com.android.jianchen.rentme.R;
import com.android.jianchen.rentme.RestAPI.RestClient;
import com.android.jianchen.rentme.RestAPI.ReviewClient;
import com.android.jianchen.rentme.Utils.Constants;
import com.android.jianchen.rentme.Utils.Utils;
import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by emerald on 12/8/2017.
 */
public class ReviewRecyclerAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_LOADING = 0;

    private Context context;
    public ArrayList<ReviewModel> reviews;

    private int curPage = 0;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private boolean loading = false;
    private OnLoadMoreListener onLoadMoreListener;

    public ReviewRecyclerAdapter(RecyclerView recycler, ArrayList<ReviewModel> rl) {
        reviews = rl;

        if (recycler.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recycler.getLayoutManager();

            recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        curPage ++;
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore(curPage);
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    public void setOnLoadMoreListener (OnLoadMoreListener listener) {
        onLoadMoreListener = listener;
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_review, parent, false);

            vh = new ReviewViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_loading, parent, false);

            vh = new LoadingViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ReviewViewHolder) {
            ReviewModel review = reviews.get(position);

            Glide.with(context).load(review.getUser_avatar()).asBitmap().centerCrop().placeholder(R.drawable.main).into(((ReviewViewHolder)holder).imgAvatar);

            ((ReviewViewHolder)holder).txtName.setText(review.getUser_name());
            Date date = Utils.stringToDate(review.getRv_ctime());
            ((ReviewViewHolder)holder).txtDate.setText(Utils.beautifyDate(date, false));
            ((ReviewViewHolder)holder).txtContent.setText(review.getRv_content());

            ((ReviewViewHolder)holder).bindRate(review);
            //((ReviewViewHolder)holder).bindRateDetail(review);
            ((ReviewViewHolder)holder).bindReviewDetail(review);
        } else {
            ((LoadingViewHolder)holder).loading.show();
        }
    }

    public int getItemViewType(int position) {
        return reviews.get(position).getId() != 0 ? VIEW_ITEM : VIEW_LOADING;
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void addReviews(ArrayList<ReviewModel> rl) {
        reviews.remove(reviews.size() - 1);
        reviews.addAll(rl);
        reviews.add(new ReviewModel());
        notifyDataSetChanged();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        public CircularImageView imgAvatar;
        public TextView txtName;
        public TextView txtDate;
        public TextView txtContent;
        public LinearLayout lytDetail;
        public LinearLayout lytReview;
        public ImageView imgReview;
        public TextView txtReviewCount;
        public LinearLayout lytRate;
        public ImageView imgRate;
        public TextView txtRateCount;

        public ReviewViewHolder(View itemView) {
            super(itemView);

            imgAvatar = (CircularImageView)itemView.findViewById(R.id.img_review_avatar);
            txtName = (TextView)itemView.findViewById(R.id.txt_review_name);
            txtDate = (TextView)itemView.findViewById(R.id.txt_review_date);
            txtContent = (TextView)itemView.findViewById(R.id.txt_review_content);
            lytDetail = (LinearLayout)itemView.findViewById(R.id.lyt_review_detail);
            lytReview = (LinearLayout)itemView.findViewById(R.id.lyt_review_review);
            txtReviewCount = (TextView)itemView.findViewById(R.id.txt_review_count);
            imgReview = (ImageView)itemView.findViewById(R.id.img_review_review);
            lytRate = (LinearLayout)itemView.findViewById(R.id.lyt_review_rate);
            txtRateCount = (TextView)itemView.findViewById(R.id.txt_rate_count);
            imgRate = (ImageView)itemView.findViewById(R.id.img_review_rate);
        }

        public void bindReviewDetail(final ReviewModel review) {
            lytReview.setOnClickListener(new SingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    RestClient<ReviewClient> restClient = new RestClient<ReviewClient>();
                    ReviewClient reviewClient = restClient.getAppClient(ReviewClient.class);

                    Call<ArrayModel<ReviewModel>> call = reviewClient.getReviewReviews(review.getId());
                    call.enqueue(new Callback<ArrayModel<ReviewModel>>() {
                        @Override
                        public void onResponse(Call<ArrayModel<ReviewModel>> call, Response<ArrayModel<ReviewModel>> response) {
                            if (response.isSuccessful() && response.body().getStatus()) {
                                ArrayList<ReviewModel> r = response.body().getData();
                                ReviewDialog dialog = new ReviewDialog(context, review.getId(), r);
                                dialog.setType(Constants.VALUE_REVIEW);
                                dialog.show();
                            } else {
                                Toast.makeText(context, context.getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayModel<ReviewModel>> call, Throwable t) {
                            Toast.makeText(context, context.getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

        public void bindRate(final ReviewModel review) {
            lytRate.setOnClickListener(new SingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    RestClient<ReviewClient> restClient = new RestClient<>();
                    ReviewClient reviewClient = restClient.getAppClient(ReviewClient.class);

                    Call<ObjectModel<Integer>> call = reviewClient.createRate(1, review.getId(), Utils.retrieveUserInfo(context).getId());
                    call.enqueue(new Callback<ObjectModel<Integer>>() {
                        @Override
                        public void onResponse(Call<ObjectModel<Integer>> call, Response<ObjectModel<Integer>> response) {
                            if (response.isSuccessful() && response.body().getStatus()) {
                                int r = response.body().getData();
                                int previousCount = Integer.parseInt(txtRateCount.getText().toString());
                                if (r == 0) {
                                    txtRateCount.setText(String.valueOf(previousCount - 1));
                                    imgRate.setBackgroundResource(R.drawable.heart);
                                } else {
                                    txtRateCount.setText(String.valueOf(previousCount + 1));
                                    imgRate.setBackgroundResource(R.drawable.heart_fill);
                                }
                            } else {
                                Toast.makeText(context, context.getResources().getString(R.string.error_load), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ObjectModel<Integer>> call, Throwable t) {
                            Toast.makeText(context, context.getResources().getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public AVLoadingIndicatorView loading;

        public LoadingViewHolder(View itemView) {
            super(itemView);

            loading = (AVLoadingIndicatorView)itemView.findViewById(R.id.loading_indicator);
        }
    }
}

package com.android.jianchen.rentme.RestAPI;

import com.android.jianchen.rentme.Models.ArrayModel;
import com.android.jianchen.rentme.Models.ObjectModel;
import com.android.jianchen.rentme.Models.ReviewModel;
import com.android.jianchen.rentme.Utils.Constants;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by emerald on 12/10/2017.
 */
public interface ReviewClient {
    @POST(Constants.API_REVIEW_CREATE)
    @FormUrlEncoded
    Call<ObjectModel<Integer>> createReview(@Field("type") int type, @Field("foreign_id") int foreign_id,
                                            @Field("content") String content, @Field("score") int score, @Field("user_id") int user_id);


    @POST(Constants.API_RATE_CREATE)
    @FormUrlEncoded
    Call<ObjectModel<Integer>> createRate(@Field("type") int type, @Field("foreign_id") int foreign_id, @Field("user_id") int user_id);

    /*
    @POST(Constants.API_REVIEW_REVIEW)
    @FormUrlEncoded
    Call<ArrayModel<ReviewModel>> getReviewReviews(@Field("review_id") int review_id);
    */
    @POST(Constants.API_REVIEW_REVIEW)
    @FormUrlEncoded
    Call<ArrayModel<ReviewModel>> getReviewReview(@Field("review_id") int review_id, @Field("user_id") int user_id, @Field("curpage") int curpage);
}

package com.jianchen.rentme.helper.network.retrofit;

import com.jianchen.rentme.model.rentme.ArrayModel;
import com.jianchen.rentme.model.rentme.ObjectModel;
import com.jianchen.rentme.model.rentme.ReviewModel;
import com.jianchen.rentme.helper.Constants;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by emerald on 12/7/2017.
 */
public interface ServiceClient {
    @POST(Constants.API_SERVICE_CREATE)
    @FormUrlEncoded
    Call<ObjectModel<Integer>> createService(@Field("talent_id") int talent_id, @Field("skill_id") int skill_id, @Field("title") String title,
                                             @Field("balance") String balance, @Field("detail") String detail);

    @POST(Constants.API_SERVICE_REVIEW)
    @FormUrlEncoded
    Call<ArrayModel<ReviewModel>> getServiceReview(@Field("service_id") int service_id, @Field("user_id") int user_id);

    @POST(Constants.API_SERVICE_DELETE)
    @FormUrlEncoded
    Call<ObjectModel<Boolean>> deleteService(@Field("service_id") int service_id);
}

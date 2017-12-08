package com.android.emerald.rentme.RestAPI;

import com.android.emerald.rentme.Models.ArrayModel;
import com.android.emerald.rentme.Models.ReviewModel;
import com.android.emerald.rentme.Utils.Constants;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by emerald on 12/7/2017.
 */
public interface ServiceClient {
    @POST(Constants.API_SERVICE_REVIEW)
    @FormUrlEncoded
    Call<ArrayModel<ReviewModel>> getServiceReview(@Field("service_id") int service_id, @Field("curpage") int curpage);
}

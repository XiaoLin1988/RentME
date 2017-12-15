package com.android.jianchen.rentme.helper.network.retrofit;

import com.android.jianchen.rentme.helper.Constants;
import com.android.jianchen.rentme.model.rentme.ArrayModel;
import com.android.jianchen.rentme.model.rentme.ServiceModel;
import com.android.jianchen.rentme.model.rentme.SkillModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by emerald on 12/15/2017.
 */
public interface SkillClient {
    @GET(Constants.API_SKILL_GET)
    Call<ArrayModel<SkillModel>> getCategories();
}

package com.jianchen.rentme.helper.network.retrofit;

import com.jianchen.rentme.helper.Constants;
import com.jianchen.rentme.model.rentme.ArrayModel;
import com.jianchen.rentme.model.rentme.SkillModel;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by emerald on 12/15/2017.
 */
public interface SkillClient {
    @GET(Constants.API_SKILL_GET)
    Call<ArrayModel<SkillModel>> getCategories();
}

package com.android.jianchen.rentme.RestAPI;

import com.android.jianchen.rentme.Models.ArrayModel;
import com.android.jianchen.rentme.Models.ObjectModel;
import com.android.jianchen.rentme.Models.ServiceModel;
import com.android.jianchen.rentme.Models.UserModel;
import com.android.jianchen.rentme.Utils.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by emerald on 12/6/2017.
 */
public interface UserClient {
    @POST(Constants.API_USER_LOGIN)
    @FormUrlEncoded
    Call<ObjectModel<UserModel>> loginUser(@Field("name") String name, @Field("password") String password);

    @POST(Constants.API_USER_SEARCH_BY_LOCATION)
    @FormUrlEncoded
    Call<ArrayList<UserModel>> searchByLocation(@Field("latitude") double latitude, @Field("longitude") double longitude, @Field("skill") String skill);

    @POST(Constants.API_USER_SERVICE)
    @FormUrlEncoded
    Call<ArrayModel<ServiceModel>> getUserSkills(@Field("talentid") int talentid);
}

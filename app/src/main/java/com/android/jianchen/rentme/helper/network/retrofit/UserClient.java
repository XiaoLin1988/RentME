package com.android.jianchen.rentme.helper.network.retrofit;

import com.android.jianchen.rentme.model.rentme.ArrayModel;
import com.android.jianchen.rentme.model.rentme.ObjectModel;
import com.android.jianchen.rentme.model.rentme.ServiceModel;
import com.android.jianchen.rentme.model.rentme.UserModel;
import com.android.jianchen.rentme.helper.Constants;

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

    @POST(Constants.API_USER_SIGNUP)
    @FormUrlEncoded
    Call<ObjectModel<Integer>> signupUser(@Field("name") String name, @Field("email") String email, @Field("password") String password);

    @POST(Constants.API_USER_SEARCH_BY_LOCATION)
    @FormUrlEncoded
    Call<ArrayList<UserModel>> searchByLocation(@Field("latitude") double latitude, @Field("longitude") double longitude, @Field("skill") String skill);

    @POST(Constants.API_USER_SERVICE)
    @FormUrlEncoded
    Call<ArrayModel<ServiceModel>> getUserSkills(@Field("talentid") int talentid);


    @POST(Constants.API_USER_GOOGLE_CHECK)
    @FormUrlEncoded
    Call<ObjectModel<UserModel>> checkGoogleUser(@Field("email") String email, @Field("name") String name, @Field("avatar") String avatar);


    @POST(Constants.API_USER_FACEBOOK_CHECK)
    @FormUrlEncoded
    Call<ObjectModel<UserModel>> checkFacebookUser(@Field("email") String email, @Field("name") String name, @Field("avatar") String avatar);
}

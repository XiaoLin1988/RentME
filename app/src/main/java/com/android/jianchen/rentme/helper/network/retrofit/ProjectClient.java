package com.android.jianchen.rentme.helper.network.retrofit;

import com.android.jianchen.rentme.model.rentme.ArrayModel;
import com.android.jianchen.rentme.model.rentme.ObjectModel;
import com.android.jianchen.rentme.model.rentme.ProjectModel;
import com.android.jianchen.rentme.helper.Constants;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by emerald on 12/10/2017.
 */
public interface ProjectClient {
    @POST(Constants.API_PROJECT_CREATE2)
    @FormUrlEncoded
    Call<ObjectModel<Integer>> createProject(@Field("buyer_id") int buyer_id, @Field("service_id") int service_id);

    @POST(Constants.API_PROJECT_FINISH)
    @FormUrlEncoded
    Call<ObjectModel<Boolean>> completeProject(@Field("project_id") int project_id);

    @POST(Constants.API_PROJECT_INPROGRESS)
    @FormUrlEncoded
    Call<ArrayModel<ProjectModel>> getMyProgressProjects(@Field("userid") int user_id);


    @POST(Constants.API_PROJECT_COMPLETED)
    @FormUrlEncoded
    Call<ArrayModel<ProjectModel>> getMyCompletedProjects(@Field("userid") int user_id);
}

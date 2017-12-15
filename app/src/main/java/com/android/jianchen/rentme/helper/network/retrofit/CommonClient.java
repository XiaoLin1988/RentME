package com.android.jianchen.rentme.helper.network.retrofit;

import com.android.jianchen.rentme.model.rentme.ArrayModel;
import com.android.jianchen.rentme.model.rentme.ObjectModel;
import com.android.jianchen.rentme.helper.Constants;

import org.json.JSONStringer;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by emerald on 12/9/2017.
 */
public interface CommonClient {
    @Multipart
    @POST(Constants.API_COMMON_UPLOAD_IMAGE)
    Call<ArrayModel<String>> uploadPhotos(@Part("type") RequestBody type, @Part("foreign_id") RequestBody foreign_id, @Part ArrayList<MultipartBody.Part> upload_file);

    @POST(Constants.API_COMMON_UPLOAD_WEB)
    @FormUrlEncoded
    Call<ObjectModel<String>> uploadWebs(@Field("type") int type, @Field("foreign_id") int foreign_id, @Field("web") JSONStringer webs);

    @POST(Constants.API_COMMON_UPLOAD_VIDEO)
    @FormUrlEncoded
    Call<ObjectModel<String>> uploadVideos(@Field("type") int type, @Field("foreign_id") int foreign_id, @Field("video[]") ArrayList<String> videos);
}

package com.android.emerald.rentme.RestAPI;

import com.android.emerald.rentme.Models.ObjectModel;
import com.android.emerald.rentme.Utils.Constants;

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
    Call<ObjectModel<String>> uploadImages(@Part("type") RequestBody type, @Part("foreign_id") RequestBody foreign_id, @Part ArrayList<MultipartBody.Part> upload_file);

    @POST(Constants.API_COMMON_UPLOAD_LINK)
    @FormUrlEncoded
    Call<ObjectModel<String>> uploadLinks(@Field("type") RequestBody type, @Field("foreign_id") RequestBody foreign_id, @Field("links") ArrayList<String> links);
}

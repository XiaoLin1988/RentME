package com.android.jianchen.rentme.helper.network.retrofit;

import com.android.jianchen.rentme.helper.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by emerald on 7/3/2017.
 */
public class RestClient<T> {
    private T service;

    public T getAppClient(Class<? extends T> type) {
        if (service == null) {
            OkHttpClient okClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            Retrofit client = new Retrofit.Builder()
                    .baseUrl(Constants.API_ROOT_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okClient)
                    .build();

            service = client.create(type);
        }

        return service;
    }
}

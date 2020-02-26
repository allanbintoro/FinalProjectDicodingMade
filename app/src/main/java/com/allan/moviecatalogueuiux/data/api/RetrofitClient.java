package com.allan.moviecatalogueuiux.data.api;

import com.allan.moviecatalogueuiux.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public MovieApi getClientMovie() {
        Retrofit retrofit = new Retrofit
                .Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.BASE_URL)
                .build();
        return retrofit.create(MovieApi.class);
    }

}

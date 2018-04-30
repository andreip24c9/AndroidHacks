package com.andrei.host.domain.deps.module;

import com.andrei.host.domain.data.networking.NetworkApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Andrei on 25/03/2018.
 */

@Module
public class NetworkModule {

    private static final String BASE_URL = "https://raw.githubusercontent.com/";

    private static final int CONNECTION_TIMEOUT = 5; // 5 seconds
    private static final int READ_TIMEOUT = 5; // 5 seconds


    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    NetworkApi provideNetworkApi(Retrofit retrofit) {
        return retrofit.create(NetworkApi.class);
    }
}

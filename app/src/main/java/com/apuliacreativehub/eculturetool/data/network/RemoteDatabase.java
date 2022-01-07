package com.apuliacreativehub.eculturetool.data.network;

import androidx.annotation.NonNull;

import com.apuliacreativehub.eculturetool.data.TokenManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public abstract class RemoteDatabase {
    static OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @NonNull
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + TokenManager.getToken())
                    .build();
            return chain.proceed(newRequest);
        }
    }).build();
}

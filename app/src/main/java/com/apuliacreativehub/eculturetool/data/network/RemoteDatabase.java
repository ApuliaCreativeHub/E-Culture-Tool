package com.apuliacreativehub.eculturetool.data.network;

import androidx.annotation.NonNull;

import com.apuliacreativehub.eculturetool.BuildConfig;
import com.apuliacreativehub.eculturetool.data.UserPreferencesManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteDatabase {
    private static final String baseUrl = BuildConfig.API_URL;

    protected static OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @NonNull
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + UserPreferencesManager.getToken())
                    .build();
            return chain.proceed(newRequest);
        }
    }).build();

    public static RemoteUserDAO provideRemoteUserDAO() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(RemoteUserDAO.class);
    }

    public static RemotePlaceDAO provideRemotePlaceDAO() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(RemotePlaceDAO.class);
    }

    public static RemoteZoneDAO provideRemoteZoneDAO() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(RemoteZoneDAO.class);
    }

    public static RemoteObjectDAO provideRemoteObjectDAO() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(RemoteObjectDAO.class);
    }

    public static RemotePathDAO provideRemotePathDAO() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(RemotePathDAO.class);
    }
}
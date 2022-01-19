package com.apuliacreativehub.eculturetool.data.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserRemoteDatabase extends RemoteDatabase {

    public static RemoteUserDAO provideRemoteUserDAO() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://hiddenfile.ml/ecultureapi/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(RemoteUserDAO.class);
    }

}
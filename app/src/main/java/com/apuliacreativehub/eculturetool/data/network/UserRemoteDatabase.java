package com.apuliacreativehub.eculturetool.data.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserRemoteDatabase {

    public static RemoteUserDAO provideRemoteUserDAO(){
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("https://hiddenfile.ml/ecultureapi/")
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(RemoteUserDAO.class);
    }
}

package com.apuliacreativehub.eculturetool.data.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserRemoteDatabase {

    public static RemoteUserDAO provideRemoteUserDAO(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://hiddenfile.ml/ecultureapi/")
                //.baseUrl("http://192.168.1.243:9090/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(RemoteUserDAO.class);
    }
}

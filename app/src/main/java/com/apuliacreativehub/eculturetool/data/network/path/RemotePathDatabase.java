package com.apuliacreativehub.eculturetool.data.network.path;

import com.apuliacreativehub.eculturetool.data.network.RemoteDatabase;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemotePathDatabase extends RemoteDatabase {
    public static RemotePathDAO provideRemotePathDAO() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://hiddenfile.ml/ecultureapi/")
                //.baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(RemotePathDAO.class);
    }
}

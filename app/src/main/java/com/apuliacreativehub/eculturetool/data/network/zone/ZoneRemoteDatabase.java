package com.apuliacreativehub.eculturetool.data.network.zone;

import com.apuliacreativehub.eculturetool.data.network.RemoteDatabase;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ZoneRemoteDatabase extends RemoteDatabase {
    public static RemoteZoneDAO provideRemoteZoneDAO() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://hiddenfile.ml/ecultureapi/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(RemoteZoneDAO.class);
    }
}

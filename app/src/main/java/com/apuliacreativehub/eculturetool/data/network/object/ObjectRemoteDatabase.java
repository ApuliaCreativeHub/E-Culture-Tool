package com.apuliacreativehub.eculturetool.data.network.object;

import com.apuliacreativehub.eculturetool.data.network.RemoteDatabase;
import com.apuliacreativehub.eculturetool.data.network.place.RemotePlaceDAO;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ObjectRemoteDatabase extends RemoteDatabase {
    public static RemoteObjectDAO provideRemoteObjectDAO() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://hiddenfile.ml/ecultureapi/")
                //.baseUrl("http://192.168.1.243:9090/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(RemoteObjectDAO.class);
    }
}

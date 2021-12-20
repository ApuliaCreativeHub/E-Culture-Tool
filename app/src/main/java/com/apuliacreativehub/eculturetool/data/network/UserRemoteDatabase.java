package com.apuliacreativehub.eculturetool.data.network;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class UserRemoteDatabase {

    @Provides
    @Singleton
    public static RemoteUserDAO provideRemoteUserDAO(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(" https://hiddenfile.ml/ecultureapi/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(RemoteUserDAO.class);
    }
}

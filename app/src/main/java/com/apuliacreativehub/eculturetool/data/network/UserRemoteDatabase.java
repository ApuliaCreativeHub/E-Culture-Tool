package com.apuliacreativehub.eculturetool.data.network;

import android.app.Application;

import androidx.appcompat.app.AppCompatActivity;

import com.apuliacreativehub.eculturetool.data.entity.User;
import com.apuliacreativehub.eculturetool.di.ECultureTool;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class UserRemoteDatabase {

    @Provides
    public static RemoteUserDAO provideRemoteUserDAO(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(" https://hiddenfile.ml/ecultureapi/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(RemoteUserDAO.class);
    }
}

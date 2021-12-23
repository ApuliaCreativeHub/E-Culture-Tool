package com.apuliacreativehub.eculturetool.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.apuliacreativehub.eculturetool.data.entity.Token;
import com.apuliacreativehub.eculturetool.data.entity.User;
import com.apuliacreativehub.eculturetool.data.network.RemoteUserDAO;
import com.apuliacreativehub.eculturetool.data.network.UserRemoteDatabase;

import java.io.IOException;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Response;

public class UserRepository{
    private final RemoteUserDAO remoteUserDAO;
    private final Executor executor;

    public UserRepository(Executor executor) {
        remoteUserDAO = UserRemoteDatabase.provideRemoteUserDAO();
        this.executor = executor;
    }

    public MutableLiveData<RepositoryNotification<String>> registerUser(User user) {
        Call<Void> call = remoteUserDAO.RegisterUser(user);
        MutableLiveData<RepositoryNotification<String>> registrationResult = new MutableLiveData<>();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<Void> response = call.execute();
                    Log.d("RETROFITRESPONSE", String.valueOf(response.code()));
                    RepositoryNotification<String> repositoryNotification = new RepositoryNotification<>();
                    repositoryNotification.setData(String.valueOf(response.code()));
                    registrationResult.postValue(repositoryNotification);
                } catch (IOException ioe) {
                    RepositoryNotification<String> repositoryNotification = new RepositoryNotification<>();
                    repositoryNotification.setException(ioe);
                    registrationResult.postValue(repositoryNotification);
                    Log.e("RETROFITERROR", ioe.getMessage());
                }
            }
        });
        return registrationResult;
    }

    public MutableLiveData<RepositoryNotification<Token>> loginUser(User user) {
        Call<Token> call = remoteUserDAO.LoginUser(user);
        MutableLiveData<RepositoryNotification<Token>> loginResult = new MutableLiveData<>();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<Token> response = call.execute();
                    Log.d("RETROFITRESPONSE", String.valueOf(response.code()));
                    RepositoryNotification<Token> repositoryNotification = new RepositoryNotification<>();
                    repositoryNotification.setData(response.body());
                    loginResult.postValue(repositoryNotification);
                } catch (IOException ioe) {
                    RepositoryNotification<Token> repositoryNotification = new RepositoryNotification<>();
                    repositoryNotification.setException(ioe);
                    loginResult.postValue(repositoryNotification);
                    Log.e("RETROFITERROR", ioe.getMessage());
                }
            }
        });
        return loginResult;
    }
}

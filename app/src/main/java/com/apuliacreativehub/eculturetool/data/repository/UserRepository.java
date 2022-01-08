package com.apuliacreativehub.eculturetool.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.apuliacreativehub.eculturetool.data.entity.User;
import com.apuliacreativehub.eculturetool.data.entity.UserWithToken;
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

    public MutableLiveData<RepositoryNotification<Void>> registerUser(User user) {
        Call<Void> call = remoteUserDAO.RegisterUser(user);
        MutableLiveData<RepositoryNotification<Void>> registrationResult = new MutableLiveData<>();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<Void> response = call.execute();
                    Log.d("RETROFITRESPONSE", String.valueOf(response.code()));
                    RepositoryNotification<Void> repositoryNotification = new RepositoryNotification<>();
                    if(response.isSuccessful()){
                        repositoryNotification.setData(response.body());
                    }else{
                        if(response.errorBody()!=null){
                            repositoryNotification.setErrorMessage(response.errorBody().string());
                        }
                    }
                    registrationResult.postValue(repositoryNotification);
                } catch (IOException ioe) {
                    RepositoryNotification<Void> repositoryNotification = new RepositoryNotification<>();
                    repositoryNotification.setException(ioe);
                    registrationResult.postValue(repositoryNotification);
                    Log.e("RETROFITERROR", ioe.getMessage());
                }
            }
        });
        return registrationResult;
    }

    public MutableLiveData<RepositoryNotification<UserWithToken>> loginUser(UserWithToken uwt) {
        Call<UserWithToken> call = remoteUserDAO.LoginUser(uwt);
        MutableLiveData<RepositoryNotification<UserWithToken>> loginResult = new MutableLiveData<>();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<UserWithToken> response = call.execute();
                    Log.d("RETROFITRESPONSE", String.valueOf(response.code()));
                    RepositoryNotification<UserWithToken> repositoryNotification = new RepositoryNotification<>();
                    if (response.isSuccessful()) {
                        repositoryNotification.setData(response.body());
                    } else {
                        if(response.errorBody()!=null){
                            repositoryNotification.setErrorMessage(response.errorBody().string());
                        }
                    }
                    loginResult.postValue(repositoryNotification);
                } catch (IOException ioe) {
                    RepositoryNotification<UserWithToken> repositoryNotification = new RepositoryNotification<>();
                    repositoryNotification.setException(ioe);
                    loginResult.postValue(repositoryNotification);
                    Log.e("RETROFITERROR", ioe.getMessage());
                }
            }
        });
        return loginResult;
    }

    public void logoutUser() {
        Call<Void> call = remoteUserDAO.LogoutUser();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<Void> response = call.execute();
                    Log.d("RETROFITRESPONSE", String.valueOf(response.code()));
                } catch (IOException ioe) {
                    Log.e("RETROFITERROR", ioe.getMessage());
                }
            }
        });
    }
}
